package com.henrythompson.neuralnets.stoppingconditions;

import com.henrythompson.neuralnets.NeuralNetwork;
import com.henrythompson.neuralnets.TrainingSample;

import java.util.List;

/**
 * Represents the conditions required to halt training on a neural network. Over the
 * course of training, the callbacks below will be called. Based on this information,
 * implementations of this interface should decide whether or not to continue the
 * training. For example, if some equation determines that the network is trained
 * closely enough to match the training samples provided, then shouldStop() should
 * return true; and no more epochs of training shall be run.
 */
public interface IStoppingCondition {
    /**
     * Called before any training has begun.
     * @param samples The samples with which the network will be trained
     * @param network The network which will be trained.
     */
    void onTrainingStart(List<TrainingSample> samples, NeuralNetwork network);

    /**
     * Called immediately after a sample is run through a network
     * @param sample The training sample just run through the network
     * @param output The corresponding output from the neural network
     */
    void onSampleTested(TrainingSample sample, double[] output);

    /**
     * Called at the very end of the epoch, when the next one is just about to begin.
     * @param epoch The number of epochs successfully completed, including the one just ending
     */
    void onEpochFinished(int epoch);

    /**
     * @return {@code true} if the training should now halt; or {@code false} if training shold continue
     */
    boolean shouldStop();
}