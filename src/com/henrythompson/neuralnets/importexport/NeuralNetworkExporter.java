package com.henrythompson.neuralnets.importexport;

import com.henrythompson.neuralnets.IWeights;
import com.henrythompson.neuralnets.NeuralNetwork;
import com.henrythompson.neuralnets.Synapse;

import java.io.*;
import java.io.FileNotFoundException;import java.io.IOException;import java.io.Writer;import java.lang.String;import java.lang.StringBuilder;import java.util.List;

/**
 * Exports a neural network to an XML-based file format so that it can
 * be re-imported at a later date.
 */
public class NeuralNetworkExporter {
    /** The neural network to be exported */
    private final NeuralNetwork mNetwork;
    /** The writer to which the XML should be written */
    private final Writer mWriter;

    /**
     * @param network The neural network to be exported
     * @param writer The writer to which the XML should be written
     */
    public NeuralNetworkExporter(NeuralNetwork network, Writer writer) {
        mNetwork = network;
        mWriter = writer;
    }

    /**
     * Saves the neural network provided in the constructor to disk with a
     * XML-based file format, so that it can be imported and inflated again
     * by {@code NeuralNetworkImporter}
     * @throws FileNotFoundException If the file provided cannot be created or
     * overwritten
     */
    public void export() throws IOException {
        // try-catch-finally to guarantee mWriter.close() called
        try {
            mWriter.append(generateXml());
            mWriter.flush();
        } catch (IOException e) {
            throw e;
        } finally {
            mWriter.close();
        }
    }

    /**
     * Generates the XML which represents the given neural network
     * @return The String representation of the XML to be saved
     */
    private String generateXml() {
        StringBuilder xml = new StringBuilder("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
        xml.append("<neuralnetwork>\n");
        
        // Synapses
        List<Synapse> synapses = mNetwork.getSynapses();

        for (Synapse synapse: synapses) {
            writeSynapseXml(synapse, xml);
        }

        xml.append("</neuralnetwork>");
        
        return xml.toString();
    }

    /**
     * Appends the given synapse to the XML file
     * @param synapse The synapse to append
     * @param xml The StringBuilder to which the synapse XML should be appended
     */
    private void writeSynapseXml(Synapse synapse, StringBuilder xml) {
        String fromType = synapse.getFromLayer().typeName();
        String toType = synapse.getToLayer().typeName();

        xml.append("   <synapse from=\"" + fromType + "\" to=\"" + toType + "\">\n");

        IWeights weights = synapse.getWeights();
        writeWeightsXml(weights, xml);
        writeBiasesXml(weights, xml);

        xml.append("   </synapse>\n");
    }

    /**
    * Appends the given biases to the XML file
    * @param weights The weights containing the biases to append
    * @param xml The StringBuilder to which the biases XML should be appended
    */
    private void writeBiasesXml(IWeights weights, StringBuilder xml) {
        int biasWeightsRowIndex = weights.getFromLayerSize();

        xml.append("      <biases>\n");

        for (int to = 0; to < weights.getToLayerSize(); to++) {
            xml.append("         <to>" + weights.getWeight(biasWeightsRowIndex, to) + "</to>\n");
        }

        xml.append("      </biases>\n");
    }

    /**
     * Appends the given weights to the XML file
     * @param weights The weights to append
     * @param xml The StringBuilder to which the weights XML should be appended
     */
    private void writeWeightsXml(IWeights weights, StringBuilder xml) {
        for (int from = 0; from < weights.getFromLayerSize(); from++) {
            xml.append("      <from>\n");

            for (int to = 0; to < weights.getToLayerSize(); to++) {
                xml.append("         <to>" + weights.getWeight(from, to) + "</to>\n");
            }

            xml.append("      </from>\n");
        }
    }
}
