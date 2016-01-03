package com.henrythompson.neuralnets.trainingstrategies;

import com.henrythompson.neuralnets.TrainingSample;
import com.henrythompson.neuralnets.stoppingconditions.IStoppingCondition;
import com.henrythompson.neuralnets.ITrainingProgressListener;

import java.util.List;

public interface ITrainingStrategy {
    /**
     * This method will perform online training for a given set of training
     * samples. Any listeners will be notified when the relevant event occurs.
     * @param trainingSet The sample set to train the network
     * @param condition The criteria necessary for training to stop
     * @param listener Interface which is notified when certain training
     * events occur
     */
    void trainOnline(final List<TrainingSample> trainingSet, final IStoppingCondition condition, final ITrainingProgressListener listener);

    /** Aborts any currently running training */
    void abortTraining();
}
