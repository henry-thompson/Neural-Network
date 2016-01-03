package com.henrythompson.neuralnets.networkbuilders;

import java.lang.IllegalArgumentException;import java.util.ArrayList;

import com.henrythompson.neuralnets.*;
import com.henrythompson.neuralnets.layers.AbstractLayer;
import com.henrythompson.neuralnets.layers.LinearLayer;
import com.henrythompson.neuralnets.layers.SigmoidLayer;
import com.henrythompson.neuralnets.layers.ThresholdLayer;

public class PerceptronBuilder {
        private int mInputLayerSize;
        private int mOutputLayerSize;
        private boolean mUseSigmoidLayer;

        private IWeights mWeights;

        private double mRndWeightAmplitude = 0.2;

        public PerceptronBuilder(int inputLayerSize, int outputLayerSize) {
            mInputLayerSize = inputLayerSize;
            mOutputLayerSize = outputLayerSize;
        }

        public PerceptronBuilder setWeights(double[][] weights) {
            setWeights(new Weights(weights));
            return this;
        }

        public PerceptronBuilder setWeights(IWeights weights) {
            if (weights == null ||
                    weights.getFromLayerSize() != mInputLayerSize ||
                    weights.getToLayerSize() != mOutputLayerSize) {
                throw new IllegalArgumentException("The size of the to and " +
                        "from layers in the weights must correspond to the " +
                        "size of the input and output layers in the perceptron\n" +
                        "Expecting (" + mInputLayerSize + "," + mOutputLayerSize + ") " +
                        "but recieved (" +
                        weights.getFromLayerSize() + "," + weights.getToLayerSize() + ")"
                );
            }

            mWeights = weights;
            return this;
        }

        public PerceptronBuilder setWeightRandomizationAmplitude(double amplitude) {
            mRndWeightAmplitude = amplitude;
            return this;
        }

        public PerceptronBuilder useSigmoidOutputLayer(boolean use) {
            mUseSigmoidLayer = use;
            return this;
        }

        public NeuralNetwork create() {
            return new NeuralNetwork(generateSynapses());
        }

        private ArrayList<AbstractLayer> generateLayers() {
            ArrayList<AbstractLayer> layers = new ArrayList<>();

            LinearLayer inputLayer = new LinearLayer(mInputLayerSize);
            AbstractLayer outputLayer;

            if (mUseSigmoidLayer) {
                outputLayer = new SigmoidLayer(mOutputLayerSize);
            } else {
                outputLayer = new ThresholdLayer(mOutputLayerSize);
            }

            layers.add(inputLayer);
            layers.add(outputLayer);

            return layers;
        }

        private ArrayList<Synapse> generateSynapses() {
            ArrayList<Synapse> synapses = new ArrayList<>();
            ArrayList<AbstractLayer> layers = generateLayers();

            if (mWeights == null) {
                mWeights = new Weights(mInputLayerSize, mOutputLayerSize);
                mWeights.randomize(mRndWeightAmplitude);
            }

            Synapse synapse = new Synapse(layers.get(0), layers.get(1), mWeights);
            synapses.add(synapse);

            return synapses;
        }
    }