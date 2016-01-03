package com.henrythompson.neuralnets;

/**
 * Holds data which summarises the key information about training a neural
 * network.
 */
public class TrainingStatistics {
    /** The number of epochs that were required to complete training */
    private final int mEpochs;

    /** The time that was required to complete training, in milliseconds */
    private final long mTimeTaken;

    /** Whether the training attempt was aborted */
    private final boolean mAborted;

    /**
     * @param epochs The number of epochs that were required to complete training
     * @param timeTaken The time that was required to complete training, in milliseconds
     * @param aborted Whether the training attempt was aborted
     */
    public TrainingStatistics(final int epochs, final long timeTaken, final boolean aborted) {
        mEpochs = epochs;
        mTimeTaken = timeTaken;
        mAborted = aborted;
    }

    /**
     * @return The number of epochs that were required to complete training
     */
    public int getEpochs() {
        return mEpochs;
    }

    /**
     * @return The time that was required to complete training, in milliseconds
     */
    public long getTimeTaken() {
        return mTimeTaken;
    }

    /**
     * @return {@code true} if the training attempt was aborted; {@code false} otherwise
     */
    public boolean wasAborted() {
        return mAborted;
    }
}