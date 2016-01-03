package com.henrythompson.neuralnets.layers;

import com.henrythompson.neuralnets.OutputListener;
import com.henrythompson.neuralnets.Synapse;

import java.lang.IllegalArgumentException;import java.lang.String;import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a layer of neurons. It accepts
 * a set of input values and weights which are summed
 * and to which a transformation function is performed,
 * producing a set of output values. Connections
 * between layers is managed by the {@link Synapse}
 * class.
 * 
 * @author Henry Thompson
 */
public abstract class AbstractLayer {
    /** Number of neurons in this layer */
    private final int mSize;

    /** The error gradients for each neuron in this layer */
    private double[] mErrorGradients;

    /** List of output listeners registered to receive any
     * outputs from this layer */
    private final List<OutputListener> mOutputListeners;
    private double[] mLastOutput;

    /** Instantiates a new AbstractLayer object with the specified
     * number of neurons
     * @param size The number of neurons in this layer
     */
    public AbstractLayer(int size) {
        if (size < 0) {
            throw new IllegalArgumentException("Layer cannot have negative size");
        }

        mSize = size;
        mOutputListeners = new ArrayList<>();
    }

    /** Returns the number of neurons in the layer */
    public int size() {
        return mSize;
    }

    /** Registers a new output listener with this layer */
    public void addOutputListener(OutputListener listener) {
        mOutputListeners.add(listener);
    }

    /**
     * @return A shallow copy of the list of listeners registered on this {@code AbstractLayer}
     */
    public List<OutputListener> getOutputLayers() {
        return new ArrayList<>(mOutputListeners);
    }

    /** Processes the specified input
     * @param input The net input values of each of the neurons
     * in this layer
     * @return The output of this layer when that set of inputs
     * was run through it
     */
    public double[] processInput(double[] input) {
        if (input.length != mSize) {
            throw new IllegalArgumentException("Length of input must match size of layer");
        }

        mLastOutput = activationFunction(input);

        for (OutputListener ol: mOutputListeners) {
            ol.onOutput(mLastOutput);
        }

        return mLastOutput;
    }

    public double[] getLastOutput() {
        return mLastOutput;
    }

    public void setErrorGradient(int index, double gradient) {
        if (mErrorGradients == null) {
            mErrorGradients = new double[mSize];
        }

        if (index < mSize) {
            mErrorGradients[index] = gradient;
        }
    }

    public void setErrorGradients(double[] gradients) {
        if (gradients.length != mSize) {
            throw new IllegalArgumentException("Size of error gradients must match size of layer");
        }

        mErrorGradients = gradients;
    }

    public double getErrorGradient(int index) {
        return mErrorGradients[index];
    }

    public double[] getErrorGradients() {
        return mErrorGradients;
    }

    /** Performs the activation function on a particular net input
     * @param netInputs The net input value
     * @return The output of this neuron when this net input value
     * is run through it
     */
    protected abstract double[] activationFunction(double[] netInputs);
    public abstract double[] getActivationDerivative();

    public abstract String typeName();
}