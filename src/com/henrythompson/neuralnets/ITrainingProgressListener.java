package com.henrythompson.neuralnets;

/**
 * Callbacks for events which occur during the training of a neural network
 */
public interface ITrainingProgressListener {
    /**
     * Called before the neural network is about to start training, but before any
     * training has actually been performed.
     */
    void onTrainingStart();

    /**
     * Called when an epoch is complete.
     * @param epoch The number of epochs that have now completely finished since
     *              training started.
     */
    void onEpochComplete(int epoch);

    /**
     * Called when a neural network has been trained based on a single sample
     * @param sample The sample on which the neural network has just been trained
     */
    void onSampleTrained(TrainingSample sample);

    /**
     * Called when all training is complete. This could either be because training
     * was successfully completed (i.e. the StoppingCriteria supplied was successfully
     * fulfilled) or the training was aborted.
     * @param stats The statistics summarising the training that has just finished.
     */
    void onTrainingComplete(TrainingStatistics stats);
}