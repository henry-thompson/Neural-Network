package com.henrythompson.neuralnets.unittests.unittests;

import com.henrythompson.neuralnets.layers.AbstractLayer;

public class MockLayer extends AbstractLayer {
    private boolean mProcessInputCalled = false;
    private double[] mProcessedInputs;

    /**
     * Instantiates a new AbstractLayer object with the specified
     * number of neurons
     *
     * @param size The number of neurons in this layer
     */
    public MockLayer(int size) {
        super(size);
    }

    @Override
    public double[] processInput(double[] inputs) {
        mProcessInputCalled = true;
        mProcessedInputs = inputs;
        return super.processInput(inputs);
    }

    @Override
    public double[] activationFunction(double[] netInputs) {
        double[] result = new double[netInputs.length];

        for (int i = 0; i < result.length; i++) {
            result[i] = netInputs[i] + 1.0;
        }

        return result;
    }

    @Override
    public double[] getActivationDerivative() {
        return null;
    }

    @Override
    public String typeName() {
        return "mock";
    }

    public double[] getProcessedInputs() {
        return mProcessedInputs;
    }

    public boolean isProcessInputCalled() {
        return mProcessInputCalled;
    }
}
