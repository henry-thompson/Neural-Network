package com.henrythompson.neuralnets;

/**
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
public interface IWeights {
    /**
     * Return the bias to the specified neuron.
     * @param toNeuronIndex The index of the neuron in the to layer
     * whose bias the function should return.
     * @return The bias of the specified neuron.
     */
    double getBias(int toNeuronIndex);

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
    void setWeight(int fromNeuronIndex, int toNeuronIndex, double weight);

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
    double getWeight(int fromNeuronIndex, int toNeuronIndex);

    /**
     * Sets the bias for the specified neuron in the to layer.
     * @param toNeuronIndex The index of the neuron whose bias should be
     * set
     * @param bias The bias to set the neuron.
     */
    void setBias(int toNeuronIndex, double bias);

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
    void adjustWeight(int fromIndex, int toIndex, double delta);

    /**
     * @return The number of neurons in the from layer
     */
    int getFromLayerSize();

    /**
     * @return The number of neurons in the to layer
     */
    int getToLayerSize();

    /**
     * Sets each weight to be a random value within a range of
     * -amplitude to amplitude. For example, if amplitude == 0.4
     * then each weight will be set to a value within the range
     * of -0.4 and 0.4
     * @param amplitude Each weight will be set to a random weight
     * whose value lies between amplitude and -amplitude
     */
    void randomize(double amplitude);
}
