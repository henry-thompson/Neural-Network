package com.henrythompson.neuralnets.unittests.unittests.stoppingconditions;

import com.henrythompson.neuralnets.TrainingSample;
import com.henrythompson.neuralnets.stoppingconditions.CEEStoppingCondition;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class CEEStoppingConditionTest {

    @Test
    public void ceeStoppingConditionStopsWhenTargetReached() {
        double target = 0.01;
        CEEStoppingCondition condition = new CEEStoppingCondition(target);

        List<TrainingSample> samples = fourTrainingSamples();
        condition.onTrainingStart(samples, null);

        // Expecting (0,0)
        condition.onSampleTested(samples.get(0), new double[]{0.015, 0.1});

        // Expecting (0,1)
        condition.onSampleTested(samples.get(1), new double[]{0.02, 0.99});

        // Expecting (0,1)
        condition.onSampleTested(samples.get(2), new double[]{0.08, 0.992});

        // Expecting (1,1)
        condition.onSampleTested(samples.get(3), new double[]{0.99, 0.99});

        Assert.assertTrue("CEECondition is just below target, condition should stop", condition.shouldStop());
        condition.onEpochFinished(1);
    }

    @Test
    public void ceeStoppingConditionDoesntStopWhenTargetReached() {
        double target = 0.01;
        CEEStoppingCondition condition = new CEEStoppingCondition(target);
        conditionShouldNotStop(condition, 1);
    }

    @Test
    public void ceeShouldBeClearedBetweenEpochs() throws Exception {
        double target = 0.01;
        CEEStoppingCondition condition = new CEEStoppingCondition(target);
        conditionShouldNotStop(condition, 1);
        conditionShouldNotStop(condition, 2);
    }

    private void conditionShouldNotStop(CEEStoppingCondition condition, int epoch) {
        List<TrainingSample> samples = fourTrainingSamples();
        condition.onTrainingStart(samples, null);

        // Expecting (0,0)
        condition.onSampleTested(samples.get(0), new double[]{0.015, 0.1});

        // Expecting (0,1)
        condition.onSampleTested(samples.get(1), new double[]{0.02, 0.99});

        // Expecting (0,1)
        condition.onSampleTested(samples.get(2), new double[]{0.08, 0.99});

        // Expecting (1,1)
        condition.onSampleTested(samples.get(3), new double[]{0.99, 0.99});

        Assert.assertFalse("CEECondition is just above target, condition should not stop", condition.shouldStop());
        condition.onEpochFinished(epoch);
    }

    private List<TrainingSample> fourTrainingSamples() {
        List<TrainingSample> samples = new ArrayList<>();
        samples.add(new TrainingSample(new double[]{0, 0}, new double[]{0, 0}));
        samples.add(new TrainingSample(new double[]{0, 1}, new double[]{0, 1}));
        samples.add(new TrainingSample(new double[]{1, 0}, new double[]{0, 1}));
        samples.add(new TrainingSample(new double[]{1, 1}, new double[]{1, 1}));

        return samples;
    }
}