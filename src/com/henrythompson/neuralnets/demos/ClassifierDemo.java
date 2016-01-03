package com.henrythompson.neuralnets.demos;

import java.lang.Math;import java.lang.Override;import java.lang.StringBuilder;import java.lang.System;import java.util.ArrayList;

import com.henrythompson.neuralnets.TrainingSample;
import com.henrythompson.neuralnets.NeuralNetwork;
import com.henrythompson.neuralnets.stoppingconditions.RMSEStoppingCondition;
import com.henrythompson.neuralnets.trainingstrategies.GradientDescentStrategy;
import com.henrythompson.neuralnets.ITrainingProgressListener;
import com.henrythompson.neuralnets.TrainingStatistics;
import com.henrythompson.neuralnets.networkbuilders.MultiClassifierNetworkBuilder;
import com.henrythompson.neuralnets.trainingstrategies.ITrainingStrategy;

/**
 * Demonstrates how a single-layer neural network can identify which region on a graph a given
 * point is if the graph is split into an arbitrary number of regions defined by
 * linear equations.
 */
public class ClassifierDemo implements ITrainingProgressListener {
    /** The time, in milliseconds, when the training began */
    private long mTrainingStart;
    /** The neural network used to demonstrate the behaviour */
    private NeuralNetwork mNetwork;
    /** The training strategy used to train the network */
    private ITrainingStrategy mTrainer;

    /** The root-mean squared error at which training should stop */
    private final double mTargetRmse = 0.1;
    /** The learning rate at whch training should proceed */
    private final double mLearningRate = 0.1;

    /**
     * Runs the demonstration
     */
    public void run() {
        mNetwork = new MultiClassifierNetworkBuilder(2, 3)
                .addLayer(50)
                .setRandomizationAmplitude(0.5)
                .create();

        mTrainer = new GradientDescentStrategy(mNetwork, mLearningRate);
        mTrainer.trainOnline(generateTrainingSet(), new RMSEStoppingCondition(mTargetRmse), this);
    }

    /**
     * @return A list of 100 training samples which will allow the neural network to approximate
     * where on the graphs each region lies.
     */
    private ArrayList<TrainingSample> generateTrainingSet() {
        ArrayList<TrainingSample> samples = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            double x = Math.random() * 50;
            double y = Math.random() * 50;

            // There are three regions, separated by y=-3x+8 and y=2x+5

            // Test if it's above the values for y=-3x+8
            if (y > ((-3 * x) + 8)) {
                samples.add(new TrainingSample(new double[]{x,y}, new double[]{1,0,0}));
            } else {
                // Is it above or below y=2x+5?
                if (y > ((2 * x) + 5)) {
                    samples.add(new TrainingSample(new double[]{x,y}, new double[]{0,1,0}));
                } else {
                    samples.add(new TrainingSample(new double[]{x,y}, new double[]{0,0,1}));
                }
            }
        }

        return samples;
    }

    @Override
    public void onTrainingStart() {
        mTrainingStart = System.currentTimeMillis();
        System.out.println("Beginning training...");
    }

    @Override
    public void onTrainingComplete(TrainingStatistics stats) {
        if (!stats.wasAborted()) {
            double timeTaken = (System.currentTimeMillis() - mTrainingStart) / 1000D;

            printNetworkOutput();

            System.out.println("Training Complete");
            System.out.println("Epochs: " + stats.getEpochs());
            System.out.println("Time: " + timeTaken + "s");
        }
    }

    private void printNetworkOutput() {
        System.out.println("Showing output on unseen samples");

        StringBuilder b = new StringBuilder();

        for (TrainingSample s: generateTrainingSet()) {
            b.append("Input: ( ");

            for (int i = 0; i < s.getInput().length; i++) {
                b.append(s.getInput()[i]);
                b.append(' ');
            }

            b.append("); Output: ( ");

            double[] output = mNetwork.processInput(s.getInput());

            for (double o : output) {
                b.append(o);
                b.append(' ');
            }

            b.append(")\n");
        }

        System.out.print(b.toString());
    }

    @Override
    public void onSampleTrained(TrainingSample sample) {
        // Nothing to do
    }

    @Override
    public void onEpochComplete(int epoch) {
        if (epoch % 100 == 0) {
            System.out.println("Epoch: " + epoch);
        }

        if (epoch >= 1000) {
            mTrainer.abortTraining();
            System.out.println("Aborted: 1,000th epoch reached");
            System.out.println("Caught in local minima");
        }
    }
}
