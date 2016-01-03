package com.henrythompson.neuralnets.stoppingconditions;

import java.lang.Math;import java.lang.Override;import java.util.List;

import com.henrythompson.neuralnets.NeuralNetwork;
import com.henrythompson.neuralnets.TrainingSample;

/**
 * Stopping condition which halts training once the root-mean square error
 * value has gone below a certain given value
 */
public class RMSEStoppingCondition implements IStoppingCondition {
    private double mTotalSse;
    private final double mTargetRmse;
    private int mSampleCount;

    public RMSEStoppingCondition(double targetRmse) {
        mTargetRmse = targetRmse;
    }

    @Override
    public void onTrainingStart(List<TrainingSample> samples, NeuralNetwork network) {
        mTotalSse = 0;
        mSampleCount = samples.size();
    }

    @Override
    public void onSampleTested(TrainingSample sample, double[] output) {
        mTotalSse += sample.getTotalSumSquaredError(output);
    }

    @Override
    public void onEpochFinished(int epoch) {
        mTotalSse = 0;
    }

    @Override
    public boolean shouldStop() {
        double mse = mTotalSse / mSampleCount;
        double rmse = Math.sqrt(mse);

        return rmse <= mTargetRmse;
    }
}