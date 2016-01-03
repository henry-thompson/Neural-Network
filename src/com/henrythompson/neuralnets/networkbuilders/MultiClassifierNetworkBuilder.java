package com.henrythompson.neuralnets.networkbuilders;

import java.util.ArrayList;
import java.util.List;

import com.henrythompson.neuralnets.*;
import com.henrythompson.neuralnets.layers.AbstractLayer;
import com.henrythompson.neuralnets.layers.SigmoidLayer;
import com.henrythompson.neuralnets.layers.SoftmaxLayer;
import com.henrythompson.neuralnets.layers.ThresholdLayer;

public class MultiClassifierNetworkBuilder {
    private final List<AbstractLayer> mLayers;
    private final List<Synapse> mSynapses;
    private double mAmplitude = 0.2;

    private int mOutputSize;

    public MultiClassifierNetworkBuilder(int inputs, int outputs) {
        mLayers = new ArrayList<>();
        mSynapses = new ArrayList<>();

        mLayers.add(new ThresholdLayer(inputs));
        mOutputSize = outputs;
    }

    public MultiClassifierNetworkBuilder addLayer(int size) {
        if (size > 0) {
            mLayers.add(new SigmoidLayer(size));
        }

        return this;
    }

    public MultiClassifierNetworkBuilder setRandomizationAmplitude(double amplitude) {
        mAmplitude = amplitude;
        return this;
    }

    public NeuralNetwork create() {
        mLayers.add(new SoftmaxLayer(mOutputSize));
        generateSynapses();

        return new NeuralNetwork(mSynapses);
    }

    private void generateSynapses() {
        for (int i = 0; i < mLayers.size() - 1; i++) {
            AbstractLayer input = mLayers.get(i);
            AbstractLayer output = mLayers.get(i + 1);

            Synapse synapse = new Synapse(input, output);
            synapse.randomiseWeights(mAmplitude);

            mSynapses.add(synapse);
        }
    }
}