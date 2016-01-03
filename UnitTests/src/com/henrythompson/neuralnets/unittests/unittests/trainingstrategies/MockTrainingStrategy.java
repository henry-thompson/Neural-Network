package com.henrythompson.neuralnets.unittests.unittests.trainingstrategies;

import com.henrythompson.neuralnets.NeuralNetwork;
import com.henrythompson.neuralnets.Synapse;
import com.henrythompson.neuralnets.TrainingSample;
import com.henrythompson.neuralnets.trainingstrategies.AbstractTrainingStrategy;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class MockTrainingStrategy extends AbstractTrainingStrategy {
    private int mTrainCallCount = 0;

    /**
     * Create a new instance of {@code AbstractTrainingStrategy}
     *
     * @param network      The {@code NeuralNetwork} to be trained
     * @param learningRate The learning rate to use
     */
    public MockTrainingStrategy(NeuralNetwork network, double learningRate) {
        super(network, learningRate);
    }

    @Override
    public double[] getOutputErrorGradients(double[] actualOutput, TrainingSample sample) {
        throw new NotImplementedException();
    }

    @Override
    public void train(Synapse synapse) {
        mTrainCallCount++;
    }

    public int getTrainCallCount() {
        return mTrainCallCount;
    }
}
