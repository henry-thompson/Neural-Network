package com.henrythompson.neuralnets.unittests.unittests.trainingstrategies;

import com.henrythompson.neuralnets.NeuralNetwork;
import com.henrythompson.neuralnets.Synapse;
import com.henrythompson.neuralnets.TrainingSample;
import com.henrythompson.neuralnets.Weights;
import com.henrythompson.neuralnets.stoppingconditions.IStoppingCondition;
import com.henrythompson.neuralnets.stoppingconditions.MaxEpochStoppingCondition;
import com.henrythompson.neuralnets.trainingstrategies.AbstractTrainingStrategy;
import com.henrythompson.neuralnets.unittests.unittests.MockLayer;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AbstractTrainingStrategyTest {

    @Test
    public void testGetLearningRate() throws Exception {
        double learningRate = 0.24234;
        AbstractTrainingStrategy strategy = new MockTrainingStrategy(new NeuralNetwork(generateSynapses()), learningRate);

        Assert.assertEquals("Method getLearningRate should return learning rate supplied in constructor",
                learningRate, strategy.getLearningRate(), 0.0);
    }

    @Test
    public void testAbortTraining() throws Exception {
        AbstractTrainingStrategy strategy = new MockTrainingStrategy(new NeuralNetwork(generateSynapses()), 0.1);

        Assert.assertFalse("AbstractTrainingStrategy should not be aborted when constructed", strategy.isAborted());
        strategy.abortTraining();
        Assert.assertTrue("AbstractTrainingStrategy should be aborted after abortTraining called", strategy.isAborted());
    }

    @Test
    public void trainOnlineCallsTrainCorrectNumberOfTimes() throws Exception {
        // To test that trainonline listens to the stopping condition
        MockTrainingStrategy strategy = new MockTrainingStrategy(new NeuralNetwork(generateSynapses()), 0.1);
        strategy.trainOnline(fourTrainingSamples(), new MaxEpochStoppingCondition(3), null);

        Assert.assertEquals("Method train() should have been called twelve times (four times, i.e. once per" +
                "connection between neurons, in each of the three epochs)", 12, strategy.getTrainCallCount());
    }

    @Test
    public void trainOnlineStopsAfterAbortCalled() throws Exception {
        MockTrainingStrategy strategy = new MockTrainingStrategy(new NeuralNetwork(generateSynapses()), 0.1);
        strategy.trainOnline(fourTrainingSamples(), callsAbortAfterThreeEpochsStoppingCondition(strategy), null);

        Assert.assertEquals("Method train() should have been called twelve times (four times, i.e. once per" +
                "connection between neurons, in each of the three epochs)", 12, strategy.getTrainCallCount());
    }

    @Test
    public void trainOnlineCallsOnSampleOnTrainingStartFirst() throws Exception {
        NeuralNetwork network = new NeuralNetwork(generateSynapses());
        MockTrainingStrategy strategy = new MockTrainingStrategy(network, 0.1);
        strategy.trainOnline(fourTrainingSamples(), checksOnTrainingStartCalledFirst(), null);
    }

    @Test
    public void trainOnlineCallsOnSampleTestedWithCorrectParameters() throws Exception {
        NeuralNetwork network = new NeuralNetwork(generateSynapses());
        MockTrainingStrategy strategy = new MockTrainingStrategy(network, 0.1);
        strategy.trainOnline(fourTrainingSamples(), checksSampleTestedCalledCorrectly(network), null);
    }

    @Test
    public void trainOnlineSetErrorGradients() throws Exception {
        NeuralNetwork network = new NeuralNetwork(generateSynapses());
        MockTrainingStrategy strategy = new MockTrainingStrategy(network, 0.1);
        strategy.trainOnline(fourTrainingSamples(), new MaxEpochStoppingCondition(3), null);

        double[] actual = network.getOutputLayer().getErrorGradients();
        Assert.assertArrayEquals("Train online should set correct error gradients",
                new double[]{-1.0, -1.0}, actual, 0.0);
    }

    private IStoppingCondition checksOnTrainingStartCalledFirst() {
        return new IStoppingCondition() {
            private boolean mAnythingElseCalledFirst = false;
            private int mEpoch = 0;

            @Override
            public void onTrainingStart(List<TrainingSample> samples, NeuralNetwork network) {
                Assert.assertFalse("onTrainingStart should be called before any other callback",
                        mAnythingElseCalledFirst);
            }

            @Override
            public void onSampleTested(TrainingSample sample, double[] output) {
                mAnythingElseCalledFirst = true;
            }

            @Override
            public void onEpochFinished(int epoch) {
                mEpoch++;
                mAnythingElseCalledFirst = true;
            }

            @Override
            public boolean shouldStop() {
                mAnythingElseCalledFirst = true;
                return mEpoch == 1;
            }
        };
    }

    private IStoppingCondition checksSampleTestedCalledCorrectly(NeuralNetwork network) {
        return new IStoppingCondition() {
            private int mEpoch;

            @Override
            public void onTrainingStart(List<TrainingSample> samples, NeuralNetwork network) {}

            @Override
            public void onSampleTested(TrainingSample sample, double[] output) {
                Assert.assertTrue("Sample testing should be equivalent to feeding sample through network",
                        Arrays.equals(output, network.processInput(sample.getInput())));
            }

            @Override
            public void onEpochFinished(int epoch) {
                mEpoch++;
            }

            @Override
            public boolean shouldStop() {
                return mEpoch == 1;
            }
        };
    }

    private IStoppingCondition callsAbortAfterThreeEpochsStoppingCondition(AbstractTrainingStrategy strategy) {
        return new IStoppingCondition() {
            private int mEpoch;

            @Override
            public void onTrainingStart(List<TrainingSample> samples, NeuralNetwork network) {
            }

            @Override
            public void onSampleTested(TrainingSample sample, double[] output) {

            }

            @Override
            public void onEpochFinished(int epoch) {
                mEpoch++;

                if (mEpoch == 3) {
                    strategy.abortTraining();
                }
            }

            @Override
            public boolean shouldStop() {
                return false;
            }
        };
    }

    private List<TrainingSample> fourTrainingSamples() {
        List<TrainingSample> samples = new ArrayList<>();
        samples.add(new TrainingSample(new double[]{0, 0}, new double[]{0, 0}));
        samples.add(new TrainingSample(new double[]{0, 1}, new double[]{0, 1}));
        samples.add(new TrainingSample(new double[]{1, 0}, new double[]{0, 1}));
        samples.add(new TrainingSample(new double[]{1, 1}, new double[]{1, 1}));

        return samples;
    }

    private List<Synapse> generateSynapses() {
        MockLayer layer1 = new MockLayer(2);
        MockLayer layer2 = new MockLayer(2);

        List<Synapse> synapses = new ArrayList<>();

        // We know this trains to completion if we start with all weights set to 0
        Weights weights = new Weights(2, 2);

        synapses.add(new Synapse(layer1, layer2, weights));
        return synapses;
    }
}