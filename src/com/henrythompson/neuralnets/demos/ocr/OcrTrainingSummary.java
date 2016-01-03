package com.henrythompson.neuralnets.demos.ocr;

import com.henrythompson.neuralnets.TrainingStatistics;

public class OcrTrainingSummary {
    private final int mHiddenLayerNumber;
    private final int mAttemptNumber;
    private final TrainingStatistics mStats;

    public OcrTrainingSummary(int hiddenLayerNumber, int attemptNumber, TrainingStatistics stats) {
        mHiddenLayerNumber = hiddenLayerNumber;
        mAttemptNumber = attemptNumber;
        mStats = stats;
    }

    public int getHiddenLayerNumber() {
        return mHiddenLayerNumber;
    }

    public int getAttemptNumber() {
        return mAttemptNumber;
    }

    public TrainingStatistics getTrainingStatistics() {
        return mStats;
    }
}
