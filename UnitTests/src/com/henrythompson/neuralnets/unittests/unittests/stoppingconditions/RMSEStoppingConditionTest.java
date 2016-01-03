package com.henrythompson.neuralnets.unittests.unittests.stoppingconditions;

import com.henrythompson.neuralnets.TrainingSample;
import com.henrythompson.neuralnets.stoppingconditions.RMSEStoppingCondition;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class RMSEStoppingConditionTest {
    @Test
    public void shouldNotStopWhenConditionInitialised() {
        RMSEStoppingCondition condition = new RMSEStoppingCondition(0.1);
        Assert.assertFalse("Condition should not stop before training started", condition.shouldStop());
    }

    @Test
    public void shouldNotStoppedBeforeRMSEsReached() throws Exception {
        RMSEStoppingCondition condition = new RMSEStoppingCondition(0.1);

        List<TrainingSample> samples = trainingSamples();

        condition.onTrainingStart(samples, null);
        condition.onSampleTested(samples.get(0), FAIL_OUTPUT);
        Assert.assertFalse("Condition should not stop before RMSE reached", condition.shouldStop());
        condition.onEpochFinished(1);
    }

    @Test
    public void shouldStopWhenRMSEsReached() throws Exception {
        RMSEStoppingCondition condition = new RMSEStoppingCondition(0.1);

        List<TrainingSample> samples = trainingSamples();

        condition.onTrainingStart(samples, null);
        condition.onSampleTested(samples.get(0), PASS_OUTPUT);
        Assert.assertTrue("Condition should stop when required RMSE reached", condition.shouldStop());
        condition.onEpochFinished(1);
    }

    private List<TrainingSample> trainingSamples() {
        List<TrainingSample> samples = new ArrayList<>();
        samples.add(new TrainingSample(new double[]{1, 0}, new double[]{0, 1}));

        return samples;
    }

    /**
     * Output which fails RMSE test when on sample produced by trainingSamples()
     */
    private static final double[] FAIL_OUTPUT = new double[]{0.5, 0.5};

    /**
     * Output which passes RMSE test when on sample produced by trainingSamples()
     */
    private static final double[] PASS_OUTPUT = new double[]{0.0001, 0.9999};
}