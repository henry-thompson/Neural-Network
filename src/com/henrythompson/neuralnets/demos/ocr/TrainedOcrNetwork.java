package com.henrythompson.neuralnets.demos.ocr;

import com.henrythompson.neuralnets.NeuralNetwork;

public class TrainedOcrNetwork {
    private final int mHiddenLayerSize;
    private final double mCee;
    private final NeuralNetwork mNetwork;
    private int mAccuracyRating;

    public TrainedOcrNetwork(int hiddenLayerSize, double cee, NeuralNetwork network) {
        mHiddenLayerSize = hiddenLayerSize;
        mCee = cee;
        mNetwork = network;
    }

    public int getHiddenLayerSize() {
        return mHiddenLayerSize;
    }

    public double getCee() {
        return mCee;
    }

    public NeuralNetwork getNetwork() {
        return mNetwork;
    }

    public int getAccuracyRating() {
        return mAccuracyRating;
    }

    public void setAccuracyRating(int accuracyRating) {
        mAccuracyRating = accuracyRating;
    }
}
