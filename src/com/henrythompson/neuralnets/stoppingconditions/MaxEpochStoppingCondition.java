package com.henrythompson.neuralnets.stoppingconditions;

import java.lang.Override;import java.util.List;

import com.henrythompson.neuralnets.NeuralNetwork;
import com.henrythompson.neuralnets.TrainingSample;
/**
 * Stopping condition which halts training once a certain number of epochs have passed
 */
public class MaxEpochStoppingCondition implements IStoppingCondition {
    private int mCurrentEpoch = 1;
    private int mMaxEpoch;

    public MaxEpochStoppingCondition(int maxEpoch) {
        mMaxEpoch = maxEpoch;
    }

    @Override
    public void onTrainingStart(List<TrainingSample> samples, NeuralNetwork network) {
        mCurrentEpoch = 0;
    }

    @Override
    public void onSampleTested(TrainingSample sample, double[] output) {
        // Ignore
    }

    @Override
    public void onEpochFinished(int epoch) {
        mCurrentEpoch++;
    }

    @Override
    public boolean shouldStop() {
        return mCurrentEpoch + 1 >= mMaxEpoch;
    }
}