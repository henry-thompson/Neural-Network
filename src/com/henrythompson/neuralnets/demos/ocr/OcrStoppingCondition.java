package com.henrythompson.neuralnets.demos.ocr;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.Double;import java.lang.Override;import java.util.List;

import com.henrythompson.neuralnets.NeuralNetwork;
import com.henrythompson.neuralnets.TrainingSample;
import com.henrythompson.neuralnets.stoppingconditions.IStoppingCondition;

/**
 * This is a special stopping condition which also calls a listener when certain
 * "milestone" mean cross-entropy error values are reached. This is mainly to
 * enable the OpticalCharacterRecognitionDemo to save the state of the neural
 * network to disk at these milestone mean CEE values.
 * {@see OpticalCharacterRecognitionDemo} for an explanation of why this is done.
 * This stopping condition halts training when the mean CEE value reaches a
 * target value, provided in the constructor.
 */
public class OcrStoppingCondition implements IStoppingCondition {
    public interface OCRTestCeeListener {
        void onKeyCeeValueReached(double value);
    }
    
    private double mTotalCee;
    private double mTargetCee;
    private int mSampleCount;
    
    private OCRTestCeeListener mListener;

    private double[] mMilestoneCees;

    private double mSmallestCeeSoFar = Double.MAX_VALUE;

    public OcrStoppingCondition(OCRTestCeeListener listener, double[] milestoneCees, double targetCee) {
        mTargetCee = targetCee;
        mListener = listener;
        mMilestoneCees = milestoneCees;
    }

    @Override
    public void onTrainingStart(List<TrainingSample> samples, final NeuralNetwork network) {
        mTotalCee = 0;
        mSampleCount = samples.size();
    }

    @Override
    public void onSampleTested(final TrainingSample sample, final double[] output) {
        mTotalCee += sample.getTotalCrossEntropyError(output);
    }

    @Override
    public void onEpochFinished(int epoch) {
        mTotalCee = 0;
    }

    @Override
    public boolean shouldStop() {
        // Mean CEE
        double mCee = mTotalCee / mSampleCount;
        checkKeyCeeValuesReached(mCee);
        
        return mCee <= mTargetCee;
    }
    
    private void checkKeyCeeValuesReached(double cee) {
        for (double milestone: mMilestoneCees) {
            if (cee <= milestone && mSmallestCeeSoFar > milestone) {
                mListener.onKeyCeeValueReached(milestone);
            } else if (cee > milestone) {
                break;
            }
        }

        if (cee < mSmallestCeeSoFar) {
            mSmallestCeeSoFar = cee;
        }
    }
}