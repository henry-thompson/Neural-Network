package com.henrythompson.neuralnets.importexport;

import java.io.IOException;
import java.io.InputStream;
import java.lang.Double;import java.lang.NumberFormatException;import java.lang.String;import java.lang.System;import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import com.henrythompson.neuralnets.IWeights;
import com.henrythompson.neuralnets.NeuralNetwork;
import com.henrythompson.neuralnets.Synapse;
import com.henrythompson.neuralnets.Weights;
import com.henrythompson.neuralnets.layers.*;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Creates a neural network from an XML-based serialisation format, as produced
 * by {@code NeuralNetworkExporter}
 */
public class NeuralNetworkImporter {
    /** The input from which the XML-based serialised data should be obtained */
    private final InputStream mInput;

    /** @param input The input from which the XML-based serialised data should be obtained */
    public NeuralNetworkImporter(InputStream input) {
        mInput = input;
    }

    /**
     * Builds the neural network described by the XML found in the {@code InputStream} provided
     *
     * @return The neural network described by the XML found in the {@code InputStream} provided
     */
    public NeuralNetwork importNetwork() {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document dom = db.parse(mInput);

            Element root = dom.getDocumentElement();
            NodeList synapseList = root.getElementsByTagName("synapse");

            return buildNeuralNetwork(synapseList);

        } catch (ParserConfigurationException | SAXException | IOException | DOMException | NumberFormatException |
                LayerTypeNotRecognisedException | MismatchingLayerSizeException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            System.out.println("Unable to create neural network");
        }

        return null;
    }

    /**
     * Constructs a NeuralNetwork from the list of synapse DOM elements in the XML
     * @param synapses The synapse DOM elements
     * @return The NeuralNetwork represented by the XML
     * @throws LayerTypeNotRecognisedException If the type of any layer in the XML file is not recognised
     * @throws MismatchingLayerSizeException If the size of a layer is not matched by the number of weights
     * or biases specified in another part of the XML file
     */
    private NeuralNetwork buildNeuralNetwork(NodeList synapses) throws LayerTypeNotRecognisedException, MismatchingLayerSizeException {
        List<LayerPlaceholder> tempLayers = new ArrayList<>();
        List<SynapsePlaceholder> tempSynapses = new ArrayList<>();

        parse(synapses, tempLayers, tempSynapses);

        // Now, build the network from the placeholders
        List<AbstractLayer> layers = new ArrayList<>();

        for (LayerPlaceholder l : tempLayers) {
            layers.add(createLayer(l));
        }

        return new NeuralNetwork(createSynapses(tempSynapses, layers));
    }

    /**
     * Builds a list of Synapses from the synapse placeholders and the AbstractLayers
     * @param synapseHolders The synapse holders obtained by parsing the XML
     * @param layers The layers constructed by parsing the XML
     * @return A list of ordered synapses linking every layer in the neural network, which may be used to
     * create a new NeuralNetwork object
     */
    private ArrayList<Synapse> createSynapses(List<SynapsePlaceholder> synapseHolders, List<AbstractLayer> layers) {
        ArrayList<Synapse> synapses = new ArrayList<>();

        for (int i = 0; i < synapseHolders.size(); i++) {
            SynapsePlaceholder s = synapseHolders.get(i);
            synapses.add(new Synapse(layers.get(i), layers.get(i + 1), s.mWeights));
        }

        return synapses;
    }

    /**
     * Creates a layer of the type specified in the XML file
     * @param layer The LayerPlaceholder containing the configuration of this layer by the XML file
     * @return The layer of the type and configuration specified in the XML file
     * @throws LayerTypeNotRecognisedException If the layer type specified in the XML is not recognised
     */
    private AbstractLayer createLayer(LayerPlaceholder layer) throws LayerTypeNotRecognisedException {
        switch (layer.getType()) {
            case "linear":
                return new LinearLayer(layer.getSize());
            case "sigmoid":
                return new SigmoidLayer(layer.getSize());
            case "softmax":
                return new SoftmaxLayer(layer.getSize());
            case "threshold":
                return new ThresholdLayer(layer.getSize());
            default:
                throw new LayerTypeNotRecognisedException("Unrecognised layer type '" + layer.getType() + "'");
        }
    }

    /**
     * Parses the DOM elements representing the synapses, filling in the lists of layer and
     * synapse placeholders passed to the method
     * @param synapseList The DOM elements representing the synapses of the neural network
     * @param tempLayers The list into which the temporary placeholders for each layer are to be put
     * @param tempSynapses The list into which the temporary placeholders for each synapse are to be put
     * @throws MismatchingLayerSizeException If the numbers of weights to a layer are not matched in
     * the weights specified by the XML file.
     */
    private void parse(NodeList synapseList, List<LayerPlaceholder> tempLayers, List<SynapsePlaceholder> tempSynapses) throws MismatchingLayerSizeException {
        for (int i = 0; i < synapseList.getLength(); i++) {
            Element synapse = (Element) synapseList.item(i);

            if (i == 0) {
                tempLayers.add(parseFromLayer(synapse));
            }

            LayerPlaceholder currentLayer = parseToLayer(tempLayers, synapse);

            NodeList fromItems = synapse.getElementsByTagName("from");
            int fromSize = fromItems.getLength();
            int toSize = parseAndCheckToLayerSize(fromItems);

            if (i == 0) {
                tempLayers.get(0).setSize(fromSize);
            }

            currentLayer.setSize(toSize);

            IWeights weights = new Weights(fromSize, toSize);
            parseWeights(fromItems, fromSize, toSize, weights);
            parseBiases(tempSynapses, synapse, toSize, weights);
        }
    }

    /**
     * Obtains the number of elements in the to layers, checking that there are a consistent number
     * of weights between the from and to layers specified by the XML
     * specified in the XML
     * @param fromItems The DOM elements representing the from layers
     * @return The number of elements in the to layers
     * @throws MismatchingLayerSizeException If there are an inconsistent number of to layers specified
     * by the weights in the from layer DOM elements
     */
    private int parseAndCheckToLayerSize(NodeList fromItems) throws MismatchingLayerSizeException {
        int size = ((Element) fromItems.item(0)).getElementsByTagName("to").getLength();

        for (int i = 0; i < fromItems.getLength(); i++) {
            if (((Element) fromItems.item(i)).getElementsByTagName("to").getLength() != size) {
                throw new MismatchingLayerSizeException();
            }
        }

        return size;
    }

    /**
     * Parses the weights between the two layers into an IWeights object
     * @param fromItems The DOM elements representing the layer from which the weights apply
     * @param fromSize The size of the layer from which the weights apply
     * @param toSize The size of the layer to which the weights apply
     * @param weights The IWeights objects into which the weights data should be parsed
     */
    private void parseWeights(NodeList fromItems, int fromSize, int toSize, IWeights weights) {
        for (int from = 0; from < fromSize; from++) {
            Element fromItem = (Element) fromItems.item(from);
            NodeList toItems = fromItem.getElementsByTagName("to");

            for (int to = 0; to < toSize; to++) {
                Element toItem = (Element) toItems.item(to);
                double weight = Double.parseDouble(toItem.getTextContent());

                weights.setWeight(from, to, weight);
            }
        }
    }

    /**
     * Creates a LayerPlaceholder representing the from layer described by the synapse XML DOM
     * element provided
     * @param synapse The synapse XML DOM element from which to extract the from layer
     * @return The LayerPlaceholder representing the from layer described by the synapse XML DOM
     * element provided
     */
    private LayerPlaceholder parseFromLayer(Element synapse) {
        String fromType = synapse.getAttribute("from");
        return new LayerPlaceholder(fromType);
    }

    /**
     * Creates a LayerPlaceholder representing the to layer described by the synapse XML DOM
     * element provided
     * @param synapse The synapse XML DOM element from which to extract the to layer
     * @return The LayerPlaceholder representing the to layer described by the synapse XML DOM
     * element provided
     */
    private LayerPlaceholder parseToLayer(List<LayerPlaceholder> tempLayers, Element synapse) {
        String toType = synapse.getAttribute("to");
        LayerPlaceholder currentLayer = new LayerPlaceholder(toType);
        tempLayers.add(currentLayer);
        return currentLayer;
    }

    /**
     * Parses the biases between the two layers into an IWeights object
     * @param tempSynapses The synapse placeholders containing the bias data for this layer
     * @param synapse The DOM element representing the synapse to which these biases belong
     * @param toSize The size of the layer to which the weights apply
     * @param weights The IWeights objects into which the bias data should be parsed
     */
    private void parseBiases(List<SynapsePlaceholder> tempSynapses, Element synapse, int toSize, IWeights weights) {
        NodeList biasItems = synapse.getElementsByTagName("biases");
        Element biasElement = (Element) biasItems.item(0);
        NodeList biasToItems = biasElement.getElementsByTagName("to");

        for (int to = 0; to < toSize; to++) {
            Element toItem = (Element) biasToItems.item(to);
            double bias = Double.parseDouble(toItem.getTextContent());

            weights.setBias(to, bias);
        }

        tempSynapses.add(new SynapsePlaceholder(weights));
    }

    /**
     * Temporarily holds the data for a layer as specified in the XML
     */
    private static class LayerPlaceholder {
        /** The string describing the type of the layer in the XML */
        private final String mType;
        /** The number of neurons in this layer */
        private int mSize;

        /**
         * @param type The string describing the type of the layer in the XML
         */
        public LayerPlaceholder(String type) {
            mType = type;
        }

        /**
         * @return The number of neurons in this layer
         */
        public int getSize() {
            return mSize;
        }

        /**
         * @return The string describing the type of the layer in the XML
         */
        public String getType() {
            return mType;
        }

        /**
         * Sets the number of neurons in this layer
         * @param size The number of neurons in this layer
         */
        public void setSize(int size) {
            mSize = size;
        }
    }

    /**
     * Temporarily holds the data for a synapse as specified in the XML
     */
    private static class SynapsePlaceholder {
        /** The weights in the synapse */
        private final IWeights mWeights;

        /**
         * @param weights The weights in the synapse
         */
        public SynapsePlaceholder(IWeights weights) {
            mWeights = weights;
        }

        /**
         * @return The weights in the synapse
         */
        public IWeights getWeights() {
            return mWeights;
        }
    }

}