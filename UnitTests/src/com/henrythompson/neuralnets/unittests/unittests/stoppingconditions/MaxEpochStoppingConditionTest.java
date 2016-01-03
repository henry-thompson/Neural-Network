package com.henrythompson.neuralnets.unittests.unittests.stoppingconditions;

import com.henrythompson.neuralnets.TrainingSample;
import com.henrythompson.neuralnets.stoppingconditions.MaxEpochStoppingCondition;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class MaxEpochStoppingConditionTest {

    @Test
    public void shouldNotStopWhenConditionInitialised() {
        MaxEpochStoppingCondition condition = new MaxEpochStoppingCondition(2);
        Assert.assertFalse("Condition should not stop before training started", condition.shouldStop());
    }

    @Test
    public void shouldNotStopAfterTrainingStarts() throws Exception {
        MaxEpochStoppingCondition condition = new MaxEpochStoppingCondition(2);

        condition.onTrainingStart(trainingSamples(), null);
        Assert.assertFalse("Condition should not stop straight after training starts", condition.shouldStop());
    }

    @Test
    public void shouldNotStoppedBeforeMaxEpochsReached() throws Exception {
        MaxEpochStoppingCondition condition = new MaxEpochStoppingCondition(2);

        List<TrainingSample> samples = trainingSamples();

        condition.onTrainingStart(samples, null);
        simulateEpoch(condition, samples, 1, true, "Condition should not stop before required epochs passed", false);
    }

    @Test
    public void shouldStopWhenMaxEpochsReached() throws Exception {
        MaxEpochStoppingCondition condition = new MaxEpochStoppingCondition(2);

        List<TrainingSample> samples = trainingSamples();

        condition.onTrainingStart(samples, null);
        simulateEpoch(condition, samples, 1);
        simulateEpoch(condition, samples, 2, true, "Condition should stop when required epochs passed", true);
    }

    @Test
    public void shouldStopAfterMaxEpochsPassed() throws Exception {
        MaxEpochStoppingCondition condition = new MaxEpochStoppingCondition(2);

        List<TrainingSample> samples = trainingSamples();

        condition.onTrainingStart(samples, null);
        simulateEpoch(condition, samples, 1);
        simulateEpoch(condition, samples, 2);
        simulateEpoch(condition, samples, 3, true, "Condition should stop after required epochs passed", true);
    }

    private void simulateEpoch(MaxEpochStoppingCondition condition, List<TrainingSample> samples, int epoch) throws Exception {
        simulateEpoch(condition, samples, epoch, false, null, false);
    }

    private void simulateEpoch(MaxEpochStoppingCondition condition, List<TrainingSample> samples, int epoch,
                               boolean assertShouldStop, String assertMessage, boolean shouldStop) throws Exception {
        condition.onSampleTested(samples.get(0), new double[2]);
        condition.onSampleTested(samples.get(1), new double[2]);

        if (assertShouldStop) {
            Assert.assertTrue(assertMessage, condition.shouldStop() == shouldStop);
        }

        condition.onEpochFinished(epoch);
    }

    private List<TrainingSample> trainingSamples() {
        List<TrainingSample> samples = new ArrayList<>();
        samples.add(new TrainingSample(new double[]{0, 0}, new double[]{0, 0}));
        samples.add(new TrainingSample(new double[]{0, 1}, new double[]{0, 1}));

        return samples;
    }
}