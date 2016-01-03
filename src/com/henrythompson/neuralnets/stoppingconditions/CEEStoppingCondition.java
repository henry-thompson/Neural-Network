package com.henrythompson.neuralnets.stoppingconditions;

import java.lang.Override;import java.lang.System;import java.util.List;

import com.henrythompson.neuralnets.NeuralNetwork;
import com.henrythompson.neuralnets.TrainingSample;

/**
 * Stopping condition which halts training once the cross-entropy error
 * value has gone below a certain given value
 */
public class CEEStoppingCondition implements IStoppingCondition {
    private double mTotalCee;
    private final double mTargetCee;
    private int mSampleCount;

    public CEEStoppingCondition(double targetCee) {
        mTargetCee = targetCee;
    }

    @Override
    public void onTrainingStart(List<TrainingSample> samples, NeuralNetwork network) {
        mTotalCee = 0;
        mSampleCount = samples.size();
    }

    @Override
    public void onSampleTested(TrainingSample sample, double[] output) {
        mTotalCee += sample.getTotalCrossEntropyError(output);
    }

    @Override
    public void onEpochFinished(int epoch) {
        mTotalCee = 0;
    }

    @Override
    public boolean shouldStop() {
        // Mean CEE
        double mcee = mTotalCee / mSampleCount;
        System.out.println(mcee);

        return mcee <= mTargetCee;
    }
}