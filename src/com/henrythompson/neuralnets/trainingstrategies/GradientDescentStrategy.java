package com.henrythompson.neuralnets.trainingstrategies;

import com.henrythompson.neuralnets.*;
import com.henrythompson.neuralnets.layers.AbstractLayer;
import com.henrythompson.neuralnets.NeuralNetwork;import java.lang.Override;

public class GradientDescentStrategy extends AbstractTrainingStrategy {

    public GradientDescentStrategy(NeuralNetwork network, double learningRate) {
        super(network, learningRate);
    }

    /**
     * Trains the synapse using the Gradient Descent algorithm
     * @param synapse The {@code Synapse} to be trained
     */
    @Override
    public void train(Synapse synapse) {
        IWeights weights = synapse.getWeights();

        AbstractLayer fromLayer = synapse.getFromLayer();
        int fromLayerSize = fromLayer.size();

        AbstractLayer toLayer = synapse.getToLayer();
        int toLayerSize = toLayer.size();

        double[] lastInput = fromLayer.getLastOutput();

        // Learning rate may be varied over time, but it must be
        // constant over an epoch
        double learningRate = getLearningRate();

        for (int i = 0; i <= fromLayerSize; i++) {
            // If i == fromLayerSize then we want the bias neuron,
            // which always outputs 1
            double z_i = (i != fromLayerSize) ? lastInput[i] : 1;

            for (int j = 0; j < toLayerSize; j++) {
                double error = toLayer.getErrorGradient(j);
                double deltaw_ij = learningRate * error * z_i;

                weights.adjustWeight(i, j, deltaw_ij);
            }
        }

        // Now set the error for the previous layer
        double[] delta_k = toLayer.getErrorGradients();
        double[] derivatives = fromLayer.getActivationDerivative();

        for (int i = 0; i < fromLayerSize; i++) {
            double delta_j = 0;

            for (int j = 0; j < toLayerSize; j++) {
                double w_jk = weights.getWeight(i, j);
                delta_j += derivatives[i] * delta_k[j] * w_jk;
            }

            fromLayer.setErrorGradient(i, delta_j);
        }
    }

    @Override
    public double[] getOutputErrorGradients(double[] actualOutput, TrainingSample sample) {
        return sample.getDifference(actualOutput);
    }
}