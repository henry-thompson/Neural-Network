package com.henrythompson.neuralnets.demos;

import java.lang.Math;import java.lang.Override;import java.lang.System;import java.util.ArrayList;

import com.henrythompson.neuralnets.NeuralNetwork;
import com.henrythompson.neuralnets.TrainingSample;
import com.henrythompson.neuralnets.TrainingStatistics;
import com.henrythompson.neuralnets.networkbuilders.PerceptronBuilder;
import com.henrythompson.neuralnets.stoppingconditions.RMSEStoppingCondition;
import com.henrythompson.neuralnets.trainingstrategies.GradientDescentStrategy;
import com.henrythompson.neuralnets.stoppingconditions.IStoppingCondition;
import com.henrythompson.neuralnets.ITrainingProgressListener;
import com.henrythompson.neuralnets.trainingstrategies.ITrainingStrategy;

/**
 * A demonstration of how a very basic perceptron is able to classify whether
 * points are on one side or another of a linear graph.
 */
public class PerceptronDemo implements ITrainingProgressListener {
    /** The perceptron to be trained */
    private final NeuralNetwork mPerceptron;
    /** The training strategy to be used in training the perceptron */
    private final ITrainingStrategy mTrainer;

    /** The root-mean square error at which to stop training */
    private final double mTargetRmse = 0.01;
    /** The number of training samples to produce */
    private final int mTrainingSamplesCount = 10000;
    /** The learning rate at which to perform training */
    private final double mLearningRate = 500;

    public PerceptronDemo() {
        mPerceptron = new PerceptronBuilder(2, 1)
                .useSigmoidOutputLayer(true)
                .create();

        mTrainer = new GradientDescentStrategy(mPerceptron, mLearningRate);
    }

    /**
     * Runs the perceptron training test which involves learning to
     * recognise points on either side of the line {@code y = x}
     */
    public void run() {
        IStoppingCondition condition = new RMSEStoppingCondition(mTargetRmse);
        ArrayList<TrainingSample> samples = generateTrainingSamples(mTrainingSamplesCount);
        mTrainer.trainOnline(samples, condition, this);
    }
    
    /**
     * Generates the specified number of random samples of points at either side
     * for the line y = x
     * @param number The number of training samples to generate
     * @return The training samples
     */
    private ArrayList<TrainingSample> generateTrainingSamples(int number) {
        ArrayList<TrainingSample> samples = new ArrayList<>();
        
        for (int i = 0; i < number; i++) {
            double x = Math.random() * 10;
            double y = Math.random() * 10;
            
            double output = x > y ? 0 : 1;
            samples.add(new TrainingSample(new double[]{x, y}, new double[]{output}));
        }
        
        return samples;
    }

    long start;

    @Override
    public void onTrainingStart() {
        start = System.currentTimeMillis();
        System.out.println("Starting training on " + mTrainingSamplesCount + " samples");
    }

    @Override
    public void onTrainingComplete(TrainingStatistics stats) {
        if (!stats.wasAborted()) {
            double timeTaken = (System.currentTimeMillis() - start) / 1000D;

            double w_11 = mPerceptron.getSynapse(0).getWeights().getWeight(0, 0);
            double w_21 = mPerceptron.getSynapse(0).getWeights().getWeight(1, 0);
            System.out.println("Weights: " + " (" + w_11 + ", " + w_21 + ")");

            System.out.println("Training Complete");
            System.out.println("Time taken: " + timeTaken + "s");
        }
    }

    @Override
    public void onSampleTrained(TrainingSample sample) {
        //System.out.println("SSE: " + trainer.getSse());
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