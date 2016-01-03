package com.henrythompson.neuralnets.trainingstrategies;

import com.henrythompson.neuralnets.*;
import com.henrythompson.neuralnets.layers.AbstractLayer;
import com.henrythompson.neuralnets.NeuralNetwork;
import com.henrythompson.neuralnets.stoppingconditions.IStoppingCondition;

import java.lang.System;import java.util.Collections;
import java.util.List;

/**
 * Trainers are the classes which perform the training on an entire network. An
 * instance of the network to be trained must be passed to the trainer upon
 * instantiation, and the trainer will perform the appropriate training algorithm.
 * Subclasses will implement their own learning algorithms.
 * @author Henry Thompson
 */
public abstract class AbstractTrainingStrategy implements ITrainingStrategy {
    /** The network this {@code AbstractTrainingStrategy} object is training **/
    private final NeuralNetwork mNetwork;

    /** The learning rate **/
    private final double mLearningRate;

    /** The layers contained in the network */
    private final List<AbstractLayer> mLayers;

    /** The synapses contained in the network */
    private final List<Synapse> mSynapses;

    /** Set to {@code true} if the current training run
     * is aborted */
    private boolean mAborted = false;

    /**
     * Create a new instance of {@code AbstractTrainingStrategy}
     * @param network The {@code NeuralNetwork} to be trained
     * @param learningRate The learning rate to use */
    public AbstractTrainingStrategy(NeuralNetwork network, double learningRate) {
        mNetwork = network;
        mLearningRate = learningRate;

        mLayers = mNetwork.getLayers();
        mSynapses = mNetwork.getSynapses();
    }

    /** @return The learning rate being used in training */
    public double getLearningRate() {
        return mLearningRate;
    }

    /** @return The final layer in the network to be trained */
    private AbstractLayer getOutputLayer() {
        return mLayers.get(mLayers.size() - 1);
    }

    /**
     * This method will perform online training using the backpropagation algorithm
     * for a given set of training samples. Any listeners will be notified when the
     * relevant event occurs.
     * @param trainingSet The sample set to train the network
     * @param condition The criteria necessary for training to stop
     * @param listener Interface which is notified when certain training
     * events occur
     */
    public void trainOnline(List<TrainingSample> trainingSet, IStoppingCondition condition, ITrainingProgressListener listener) {
        long start = System.nanoTime();

        sendTrainingStart(listener);
        condition.onTrainingStart(trainingSet, mNetwork);
        int epoch = 0;

        AbstractLayer outputLayer = getOutputLayer();

        while (!mAborted) {
            epoch++;
            Collections.shuffle(trainingSet);

            for (TrainingSample sample: trainingSet) {
                double[] output = mNetwork.processInput(sample.getInput());
                condition.onSampleTested(sample, output);
            }

            if (condition.shouldStop()) {
                break;
            }

            for (TrainingSample sample: trainingSet) {
                double[] output = mNetwork.processInput(sample.getInput());
                double[] difference = sample.getDifference(output);
                outputLayer.setErrorGradients(difference);

                performTraining();
                sendSampleTrained(listener, sample);
            }

            sendEpochComplete(listener, epoch);
            condition.onEpochFinished(epoch);
        }

        TrainingStatistics stats = new TrainingStatistics(epoch, System.nanoTime() - start, mAborted);
        sendTrainingComplete(listener, stats);
        mAborted = false;
    }

    /** Notifies a listener that training is starting
     * @param listener The listener to notify
     */
    private void sendTrainingStart(ITrainingProgressListener listener) {
        if (listener != null) {
            listener.onTrainingStart();
        }
    }

    /** Notifies a listener that a sample is trained
     * @param listener The listener to notify
     * @param sample The sample which has been trained
     */
    private void sendSampleTrained(ITrainingProgressListener listener,
            TrainingSample sample) {
        if (listener != null) {
            listener.onSampleTrained(sample);
        }
    }

    /** Notifies a listener that an epoch is completed
     * @param listener The listener to notify
     * @param epoch The current epoch
     */
    private void sendEpochComplete(ITrainingProgressListener listener,
            int epoch) {
        if (listener != null) {
            listener.onEpochComplete(epoch);
        }
    }

    /** Notifies a listener that training is finished
     * @param listener The listener to notify
     */
    private void sendTrainingComplete(ITrainingProgressListener listener, TrainingStatistics stats) {
        if (listener != null) {
            listener.onTrainingComplete(stats);
        }
    }

    /**
     * Trains each synapse, starting at the final synapse
     * and moving its way forwards. The actual implementation
     * of the training algorithm is left to subclasses.
     */
    private void performTraining() {
        mAborted = false;

        int n = mSynapses.size() - 1;

        for (int i = n; i >= 0 && !mAborted; i--) {
            Synapse synapse = mSynapses.get(i);
            train(synapse);
        }

        mAborted = false;
    }

    /** Aborts any currently running training */
    public void abortTraining() {
        mAborted = true;
    }

    /**
     * Returns an appropriate error gradient for each output
     * neuron and this training algorithm
     * @param actualOutput The actual output of the network
     * when the sample input was fed into it
     * @param sample The actual sample itself
     * @return The appropriate error gradients
     */
    public abstract double[] getOutputErrorGradients(double[] actualOutput, TrainingSample sample);

    /**
     * The actual implementation of the training algorithm
     * @param synapse The {@code Synapse} to be trained
     */
    public abstract void train(Synapse synapse);

    public boolean isAborted() {
        return mAborted;
    }
}