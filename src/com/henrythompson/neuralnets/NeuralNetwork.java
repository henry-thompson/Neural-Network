package com.henrythompson.neuralnets;

import com.henrythompson.neuralnets.layers.AbstractLayer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Holds a group of synapses which link together neuron layers forming
 * a feed-forward neural network.
 */
public class NeuralNetwork {
    /** The synapses holding the AbstractLayers representing the network */
    private final List<Synapse> mSynapses;

    /** The AbstractLayers comprising this network */
    private final List<AbstractLayer> mLayers;

    /**
     * @param synapses The synapses holding the AbstractLayers which form this neural network.
     *                 The synapses must be in order, where the 0th synapse holds the input
     *                 layer and the one immediately following it, and so on until the synapse
     *                 at the end of the list holds the output neurons. This class trusts that the
     *                 list of synapses provided forms a chain of AbstractLayers, where the
     *                 from layer of one synapse is the to layer of the previous synapse on the
     *                 list.
     */
    public NeuralNetwork(List<Synapse> synapses) {
        mSynapses = synapses;
        mLayers = extractLayers();
    }

    /**
     * Feeds the given input through the neural network.
     * @param input The value to feed to the input layer of the network.
     * @return The output from the output layer of the network.
     */
    public double[] processInput(double[] input) {
        mSynapses.get(0).getFromLayer().processInput(input);
        getInputLayer().processInput(input);
        return getOutputLayer().getLastOutput();
    }

    /**
     * @return The AbstractLayer representing the input layer, i.e. the
     * layer into which inputs are fed.
     */
    public AbstractLayer getInputLayer() {
        return mSynapses.get(0).getFromLayer();
    }

    /**
     * @return The final layer in the neural network, out of which the result
     * of feeding the input through every layer is produced.
     */
    public AbstractLayer getOutputLayer() {
        return mSynapses.get(mSynapses.size() - 1).getToLayer();
    }

    /**
     * @param index The zero-based index of the synapse required
     * @return The synapse at the index provided, where the zeroth index represents
     * the input layer
     */
    public Synapse getSynapse(int index) {
        return mSynapses.get(index);
    }

    /**
     * @return A shallow copy of the list of synapses which constitute this neural
     * network.
     */
    public ArrayList<Synapse> getSynapses() {
        return new ArrayList<>(mSynapses);
    }

    /**
     * @return A shallow copy of the list of {@code AbstractLayers} which constitute this neural
     * network
     */
    public ArrayList<AbstractLayer> getLayers() {
        return new ArrayList<>(mLayers);
    }

    /**
     * Takes the list of synapses and extracts from them an ordered list of the {@code AbstractLayers} which
     * they contain, where the zeroth element in the list is the input layer.
     * @return The list of {@code AbstractLayers} which constitute this neural network
     */
    private List<AbstractLayer> extractLayers() {
        List<AbstractLayer> layers = mSynapses.stream()
                .map(Synapse::getFromLayer)
                .collect(Collectors.toList());

        layers.add(getOutputLayer());

        return layers;
    }
}