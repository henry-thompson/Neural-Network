package com.henrythompson.neuralnets;

import com.henrythompson.neuralnets.layers.AbstractLayer;import java.lang.IllegalArgumentException;import java.lang.Override;

/**
 * Represents a connection between two layers of neurons. The
 * connections are each represented by a set of weights. For a
 * connection from the n<sup>th</sup> neuron of a layer to the
 * m<sup>th</sup> neuron of another, the connection is represented
 * by mWeights[n].get(m).
 * @author Henry Thompson
 *
 */
public class Synapse implements OutputListener {
    /** The weights between the previous layer
     * and the next layer. Each weight represents
     * a connection.
     */
    private final IWeights mWeights;

    /** The layer from which the connection is made */
    private final AbstractLayer mFromLayer;

    /** The layer to which the connection is made */
    private final AbstractLayer mToLayer;

    /** The number of neurons in the from layer */
    private final int mFromLayerSize;

    /** The number of neurons in the to layer */
    private final int mToLayerSize;

    public Synapse(AbstractLayer fromLayer, AbstractLayer toLayer) {
        this(fromLayer, toLayer, new Weights(fromLayer.size(), toLayer.size()));
    }

    /** Instantiates a new Synapse connecting the neurons between
     * the from layer to the to layer
     * @param fromLayer The layer from which inputs should flow
     * @param toLayer The layer to which outputs should flow
     * @param weights The weights for each connection between neurons.
     * The size of the weights be consistent with the size of the fromLayer
     * and the toLayer.
     */
    public Synapse(AbstractLayer fromLayer, AbstractLayer toLayer, IWeights weights) {
        if (weights.getFromLayerSize() != fromLayer.size() && weights.getToLayerSize() != toLayer.size()) {
            throw new IllegalArgumentException("The size of the weights "
                    + "matrix should be consistent with the sizes of the "
                    + "input and output layers");
        }

        mFromLayer = fromLayer;
        mToLayer = toLayer;
        mWeights = weights;

        mFromLayerSize = mFromLayer.size();
        mToLayerSize = mToLayer.size();

        mFromLayer.addOutputListener(this);
    }

    /**
     * Sets each weight in the synape to be a random value
     * within a range of -amplitude to amplitude. For
     * example, if {@code amplitude == 0.4} then each weight
     * will be set to a value within the range of -0.4 and
     * 0.4
     * @param amplitude Each weight will be set to a random
     * weight whose value lies between amplitude and
     * -amplitude
     */
    public void randomiseWeights(double amplitude) {
        mWeights.randomize(amplitude);
    }

    @Override
    public void onOutput(double[] output) {
        double[] netInputs = calculateNetInputs(output);
        mToLayer.processInput(netInputs);
    }

    public AbstractLayer getToLayer() {
        return mToLayer;
    }

    public AbstractLayer getFromLayer() {
        return mFromLayer;
    }

    /** For each neuron in the to layer, this function
     * will calculate the net input to each neuron in
     * the to layer, by weighting every value and then
     * summing the results.
     *
     * @param output The output of the from layer to be
     * weighted and summed
     * @return The weighted, biased, summed input to each
     * individual neuron in the to layer
     */
    private double[] calculateNetInputs(double[] output) {
        double[] result = new double[mToLayerSize];

        for (int i = 0; i < mToLayerSize; i++) {
            for (int j = 0; j < mFromLayerSize; j++) {
                result[i] += output[j] * mWeights.getWeight(j, i);
            }
            result[i] += mWeights.getBias(i);
        }

        return result;
    }

    public IWeights getWeights() {
        return mWeights;
    }
}