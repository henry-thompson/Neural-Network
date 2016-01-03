package com.henrythompson.neuralnets.unittests.unittests;

import com.henrythompson.neuralnets.IWeights;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class MockWeights implements IWeights {
    private final int mFromLayerSize;
    private final int mToLayerSize;
    private boolean mRandomiseCalled = false;
    private double mRandomisedAmplitude = -1.0;

    public MockWeights(int fromLayerSize, int toLayerSize) {
        mFromLayerSize = fromLayerSize;
        mToLayerSize = toLayerSize;
    }

    @Override
    public double getBias(int toNeuronIndex) {
        return 0;
    }

    @Override
    public void setWeight(int fromNeuronIndex, int toNeuronIndex, double weight) {
        throw new NotImplementedException();
    }

    @Override
    public double getWeight(int fromNeuronIndex, int toNeuronIndex) {
        return 1;
    }

    @Override
    public void setBias(int toNeuronIndex, double bias) {
        throw new NotImplementedException();
    }

    @Override
    public void adjustWeight(int fromIndex, int toIndex, double delta) {
        throw new NotImplementedException();
    }

    @Override
    public int getFromLayerSize() {
        return mFromLayerSize;
    }

    @Override
    public int getToLayerSize() {
        return mToLayerSize;
    }

    @Override
    public void randomize(double amplitude) {
        mRandomiseCalled = true;
        mRandomisedAmplitude = amplitude;
    }

    public boolean randomiseCalled() {
        return mRandomiseCalled;
    }

    public double getRandomisedAmplitude() {
        return mRandomisedAmplitude;
    }
}