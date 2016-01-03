package com.henrythompson.neuralnets.demos.ocr;

import com.henrythompson.neuralnets.*;
import com.henrythompson.neuralnets.importexport.NeuralNetworkExporter;
import com.henrythompson.neuralnets.networkbuilders.MultiClassifierNetworkBuilder;
import com.henrythompson.neuralnets.trainingstrategies.GradientDescentStrategy;
import com.henrythompson.neuralnets.trainingstrategies.ITrainingStrategy;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.Override;import java.lang.String;import java.lang.System;import java.util.ArrayList;
import java.util.List;

public class OcrTrainer implements ITrainingProgressListener {
    private final String mOutputFolder;
    private final int mHiddenLayerSize;
    private final List<TrainingSample> mSamples;
    private ITrainingStrategy mTrainer;
    private NeuralNetwork mNetwork;

    private int mAttempts = 0;
    private final ArrayList<OcrTrainingSummary> mSummaries;

    public OcrTrainer(String outputFolder, int hiddenLayerSize, List<TrainingSample> samples) {
        mOutputFolder = outputFolder;
        mHiddenLayerSize = hiddenLayerSize;
        mSamples = samples;
        mSummaries = new ArrayList<>();

        createNeuralNetwork();
        createTrainer();
    }

    private void createTrainer() {
        mTrainer = new GradientDescentStrategy(mNetwork, 0.1);
    }

    public ArrayList<OcrTrainingSummary> train() {
        final OcrStoppingCondition condition = new OcrStoppingCondition(value -> {
            File f = new File(mOutputFolder + "\\-" + mHiddenLayerSize + "-" + value + "-.ann");

            try {
                PrintWriter writer = new PrintWriter(f);
                new NeuralNetworkExporter(mNetwork, writer).export();
            } catch (IOException e) {
                System.out.println("Failed to save result for value " + value);
                System.out.println(e.getMessage());
                e.printStackTrace();
                return;
            }

            System.out.println("Saved network with hidden layer of size " + mHiddenLayerSize + ", Mean CEE = " + value);
        }, MILESTONE_CEES, 0.001);

        mTrainer.trainOnline(mSamples, condition, this);
        return mSummaries;
    }

    private void createNeuralNetwork() {
        mNetwork = new MultiClassifierNetworkBuilder(35, 26)
                .addLayer(mHiddenLayerSize)
                .setRandomizationAmplitude(0.3)
                .create();
    }

    @Override
    public void onTrainingStart() {
        System.out.println("Training with hidden layer of size: " + mHiddenLayerSize);
    }

    @Override
    public void onTrainingComplete(TrainingStatistics stats) {
        mSummaries.add(new OcrTrainingSummary(mHiddenLayerSize, mAttempts, stats));

        if (stats.wasAborted()) {
            ++mAttempts;

            if (mAttempts <= 4) {
                System.out.println("Training aborted. Retrying - Attempt " + mAttempts);
                retry();

            } else {
                System.out.println("Training aborted. Maximum retries reached. Continuing...");
            }

            return;
        }

        System.out.println("Training success. Continuing onto next...");
    }

    private void retry() {
        createNeuralNetwork();
        createTrainer();
        train();
    }

    @Override
    public void onSampleTrained(TrainingSample sample) {

    }

    @Override
    public void onEpochComplete(int epoch) {
        if (epoch > 1000000) {
            // Training has almost certainly got stuck in a local minima. Quit.
            mTrainer.abortTraining();
        }
    }

    private static final double[] MILESTONE_CEES =  new double[]{
            3.25, 3.0, 2.75, 2.5, 2.25, 2.0, 1.75, 1.5, 1.25,
            1.0, 0.8, 0.6, 0.5, 0.4, 0.3, 0.2, 0.1, 0.075,
            0.05, 0.025, 0.01, 0.0075, 0.005, 0.0025, 0.001
    };
}