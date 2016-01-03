package com.henrythompson.neuralnets.demos;

import java.lang.Override;import java.lang.System;import java.util.ArrayList;

import com.henrythompson.neuralnets.*;
import com.henrythompson.neuralnets.networkbuilders.XORNetworkBuilder;
import com.henrythompson.neuralnets.stoppingconditions.RMSEStoppingCondition;
import com.henrythompson.neuralnets.trainingstrategies.GradientDescentStrategy;
import com.henrythompson.neuralnets.stoppingconditions.IStoppingCondition;
import com.henrythompson.neuralnets.trainingstrategies.ITrainingStrategy;

/** Demonstrates how a neural network can act a a logical XOR gate */
public class XORDemo implements ITrainingProgressListener {
    /* The maximum absolute value with which to randomise the weights between neurons */
    private final double mWeightsAmplitude = 1.0;
    /** The learning rate at which to perform training */
    private final double mLearningRate = 0.12;

    /** The training strategy with which to train the network */
    private ITrainingStrategy mTrainer;
    /** The time in milliseconds when the training was started */
    private long mStart;

    /**
     * Run the logical XOR demo
     */
    public void run() {
        final NeuralNetwork xor = new XORNetworkBuilder()
                .setRandomisationAmplitude(mWeightsAmplitude)
                .create();
        
        mTrainer = new GradientDescentStrategy(xor, mLearningRate);

        IStoppingCondition condition = new RMSEStoppingCondition(0.05);
        mTrainer.trainOnline(generateXORTrainingSet(), condition, this);
    }

    @Override
    public void onTrainingStart() {
        mStart = System.currentTimeMillis();
        System.out.println("Beginning training");
    }

    @Override
    public void onTrainingComplete(TrainingStatistics stats) {
        if (!stats.wasAborted()) {
            double timeTaken = (System.currentTimeMillis() - mStart) / 1000D;

            System.out.println("Training complete");
            System.out.println("Time taken: " + (timeTaken) + "s");
            System.out.println("Epochs: " + stats.getEpochs());
        }
    }

    @Override
    public void onSampleTrained(TrainingSample sample) {
        // Nothing to do
    }

    @Override
    public void onEpochComplete(int epoch) {
        if (epoch >= 50000) {
            mTrainer.abortTraining();
            System.out.println("Aborted: 50,000 epoch reached");
            System.out.println("Caught in local minima");
        }
    }

    /** Generates a training set on which the network can be trained to act as logical XOR */
    private ArrayList<TrainingSample> generateXORTrainingSet() {
        ArrayList<TrainingSample> samples = new ArrayList<>();

        samples.add(new TrainingSample(new double[]{0, 0}, new double[]{0}));
        samples.add(new TrainingSample(new double[]{0, 1}, new double[]{1}));
        samples.add(new TrainingSample(new double[]{1, 0}, new double[]{1}));
        samples.add(new TrainingSample(new double[]{1, 1}, new double[]{0}));

        return samples;
    }
}
