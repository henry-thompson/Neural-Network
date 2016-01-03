package com.henrythompson.neuralnets.unittests.unittests;

import com.henrythompson.neuralnets.Synapse;
import org.junit.Assert;
import org.junit.Test;

public class SynapseTest {

    @Test
    public void testRandomiseWeights() throws Exception {
        int fromLayerSize = 3;
        int toLayerSize = 4;

        MockWeights weights = new MockWeights(fromLayerSize, toLayerSize);

        Synapse synapse = new Synapse(
                new MockLayer(fromLayerSize),
                new MockLayer(toLayerSize),
                weights);

        double randomisationAmplitude = 0.3;

        Assert.assertFalse("Method randomise on the weights should not have been called yet", weights.randomiseCalled());
        Assert.assertNotEquals("Randomisation amplitude on weights should not be set yet",
                randomisationAmplitude, weights.getRandomisedAmplitude(), 0.0);

        synapse.randomiseWeights(randomisationAmplitude);

        Assert.assertTrue("Method randomiseWeights should call randomise on the weights", weights.randomiseCalled());
        Assert.assertEquals("Method randomiseWeights should pass amplitude to randomise on the weights",
                randomisationAmplitude, weights.getRandomisedAmplitude(), 0.0);
    }

    @Test
    public void testOnOutput() throws Exception {
        int fromLayerSize = 3;
        int toLayerSize = 4;

        MockLayer toLayer = new MockLayer(toLayerSize);

        Synapse synapse = new Synapse(new MockLayer(fromLayerSize), toLayer,
                new MockWeights(fromLayerSize, toLayerSize));

        synapse.onOutput(new double[]{0.0, 1.2, -2.0});

        Assert.assertTrue("Syanpse should pass its input into the next layer when the previous layer calls onOutput",
                toLayer.isProcessInputCalled());
        Assert.assertArrayEquals("Synapse should pass its inputs to the next layer after being processed by weights",
                new double[]{-0.8, -0.8, -0.8, -0.8}, toLayer.getProcessedInputs(), 0.0);
    }

    @Test
    public void testGetToLayer() throws Exception {
        int fromLayerSize = 3;
        int toLayerSize = 4;

        MockLayer toLayer = new MockLayer(toLayerSize);

        Synapse synapse = new Synapse(new MockLayer(fromLayerSize), toLayer,
                new MockWeights(fromLayerSize, toLayerSize));

        Assert.assertEquals("getToLayer should return specified to layer",
                toLayer, synapse.getToLayer());
    }

    @Test
    public void testGetFromLayer() throws Exception {
        int fromLayerSize = 3;
        int toLayerSize = 4;

        MockLayer fromLayer = new MockLayer(fromLayerSize);

        Synapse synapse = new Synapse(fromLayer, new MockLayer(fromLayerSize),
                new MockWeights(fromLayerSize, toLayerSize));

        Assert.assertEquals("getFromLayer should return specified to layer",
                fromLayer, synapse.getFromLayer());
    }

    @Test
    public void testGetWeights() throws Exception {
        int fromLayerSize = 3;
        int toLayerSize = 4;

        MockWeights weights = new MockWeights(fromLayerSize, toLayerSize);

        Synapse synapse = new Synapse(new MockLayer(fromLayerSize), new MockLayer(fromLayerSize), weights);

        Assert.assertEquals("getWeights should return supplied weights",
                weights, synapse.getWeights());
    }
}