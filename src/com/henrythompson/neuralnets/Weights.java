package com.henrythompson.neuralnets;

import java.lang.IllegalArgumentException;import java.lang.Math;import java.lang.Override; /**
 * Holds the connection weights between two layers of neurons. The
 * layer from which the value is coming is called the "from layer",
 * and the layer to which the value is being passed is called the
 * "to layers". They contain "from neurons" and "to neurons"
 * respectively. There is one extra row in the from layer array
 * at the end which represents the bias for each neuron in the to
 * layer. This can be thought of as having an invisible "bias neuron"
 * at the end of the from layer which always outputs 1, and the biases
 * are therefore represented by the weights of the synapse between
 * it and each of the neurons in the from layer to which it connects.
 * 
 * @author Henry Thompson
 */
public class Weights implements IWeights {
    /** Holds the weights between layers in a
     * 2-Dimensional array. The connection
     * weight between the i<sup>th</sup>
     * neuron in the from layer and the
     * j<sup>th</sup> neuron in the to layer
     * is found at mWeights[i][j].
     */
    private final double[][] mWeights;

    /** Instantiates a new set of weights between two layers
     *
     * @param fromLayerSize The number of neurons in the layer the synapse is connected from.
     *                      1 will always be added to this in the size of the weights matrix
     *                      for the biases.
     * @param toLayerSize The number of neurons in the layer the synapse is connected to.
     *
     */
    public Weights(int fromLayerSize, int toLayerSize) {
        if (fromLayerSize < 0 || toLayerSize < 0) {
            throw new IllegalArgumentException("The size of both the fromLayer and toLayer should be positive");
        }

        mWeights = new double[fromLayerSize + 1][toLayerSize];
    }

    /**
     * Instantiates a new set of weights between two layers. There
     * <b>must</b> be an extra row in the from layer array (the first
     * array) to represent the biases for each neuron in the to layer.
     *
     * @param weights The weights between the two layers. Make
     * sure that the size of the array is consistent with the
     * number of neurons in the fromLayer + 1 and the toLayer.
     */
    public Weights(double[][] weights) {
        mWeights = weights;
    }

    /**
     * Return the bias to the specified neuron.
     * @param toNeuronIndex The index of the neuron in the to layer
     * whose bias the function should return.
     * @return The bias of the specified neuron.
     */
    @Override
    public double getBias(int toNeuronIndex) {
        return mWeights[mWeights.length - 1][toNeuronIndex];
    }

    /**
     * Sets the weight for the connection between the fromIndex<sup>th</sup>
     * neuron in the from layer and the toIndex<sup>th</sup> neuron in the
     * to layer
     * @param fromNeuronIndex The index of the neuron in the from layer from
     * which this synapse is connected. The index of the final neuron in the
     * from layer + 1 gives the "bias" for each neuron in the to layer.
     * @param toNeuronIndex The index of the neuron in the to layer to which
     * this synapse connects.
     * @param weight The weight to set for this connection
     */
    @Override
    public void setWeight(int fromNeuronIndex, int toNeuronIndex, double weight) {
        mWeights[fromNeuronIndex][toNeuronIndex] = weight;
    }

    /**
     * Gets the weight for the connection between the fromIndex<sup>th</sup>
     * neuron in the from layer and the toIndex<sup>th</sup> neuron in the
     * to layer
     * @param fromNeuronIndex The index of the neuron in the from layer from
     * which this synapse is connected. The index of the final neuron in the
     * from layer + 1 gives the "bias" for each neuron in the to layer.
     * @param toNeuronIndex The index of the neuron in the to layer to which
     * this synapse connects.
     */
    @Override
    public double getWeight(int fromNeuronIndex, int toNeuronIndex) {
        return mWeights[fromNeuronIndex][toNeuronIndex];
    }

    /**
     * Sets the bias for the specified neuron in the to layer.
     * @param toNeuronIndex The index of the neuron whose bias should be
     * set
     * @param bias The bias to set the neuron.
     */
    @Override
    public void setBias(int toNeuronIndex, double bias) {
        mWeights[mWeights.length - 1][toNeuronIndex] = bias;
    }

    /**
     * Adjusts the weight for the connection between the fromIndex<sup>the</sup>
     * neuron in the from layer and the toIndex<sup>th</sup> neuron in the to
     * layer by the specified amount
     * @param fromIndex The index of the neuron in the from layer from which this
     * weight starts. The index of the final neuron in the from layer + 1 gives the
     * "bias" for each neuron in the to layer.
     * @param toIndex The index of the neuron in the to layer to which this weight
     * goes
     * @param delta The difference to be added onto the specified weight
     */
    @Override
    public void adjustWeight(int fromIndex, int toIndex, double delta) {
        mWeights[fromIndex][toIndex] += delta;
    }

    /**
     * @return The number of neurons in the from layer
     */
    @Override
    public int getFromLayerSize() {
        return mWeights.length - 1;
    }

    /**
     * @return The number of neurons in the to layer
     */
    @Override
    public int getToLayerSize() {
        return mWeights[0].length;
    }

    /**
     * Sets each weight to be a random value within a range of
     * -amplitude to amplitude. For example, if amplitude == 0.4
     * then each weight will be set to a value within the range
     * of -0.4 and 0.4
     * @param amplitude Each weight will be set to a random weight
     * whose value lies between amplitude and -amplitude
     */
    @Override
    public void randomize(final double amplitude) {
        double upperBound = Math.abs(amplitude);
        double lowerBound = -upperBound;
        double range = upperBound - lowerBound;

        for (int i = 0; i < mWeights.length; i++) {
            for (int j = 0; j < mWeights[0].length; j++) {
                double randomWeight = lowerBound + Math.random() * range;
                setWeight(i, j, randomWeight);
            }
        }
    }
}