package com.henrythompson.neuralnets.unittests.unittests.layers;

import com.henrythompson.neuralnets.layers.AbstractLayer;
import com.henrythompson.neuralnets.OutputListener;
import com.henrythompson.neuralnets.unittests.unittests.MockLayer;
import org.junit.Assert;
import org.junit.Test;

public class AbstractLayerTest {

    @Test
    public void constructorSetsCorrectSize() throws Exception {
        int size = 34;
        AbstractLayer layer = new MockLayer(size);

        Assert.assertEquals("AbstractLayer size should equal the size specified in the constructor", size, layer.size());
    }

    @Test
    public void constructorDisallowsNegativeLayerSize() throws Exception {
        try {
            new MockLayer(-1);
        } catch (IllegalArgumentException e) {
            return;
        }

        Assert.fail("AbstractLayer did not throw IllegalArgumentException when specified layer size was negative");
    }

    @Test
    public void addedOutputListenerProperlyRegistered() throws Exception {
        int size = 34;
        AbstractLayer layer = new MockLayer(size);
        OutputListener listener = o -> {};

        Assert.assertFalse("OutputListener should not be registered by AbstractLayer before being added",
                layer.getOutputLayers().contains(listener));

        layer.addOutputListener(listener);

        Assert.assertTrue("Added OutputListener should be registered by AbstractLayer",
                layer.getOutputLayers().contains(listener));
    }

    @Test
    public void outputListenerRecievesOutputEvent() throws Exception {
        int size = 3;
        boolean[] listenerCalled = new boolean[]{false};

        AbstractLayer layer = new MockLayer(size);
        OutputListener listener = o -> listenerCalled[0] = true;

        layer.addOutputListener(listener);

        Assert.assertFalse("Listener should not be called before input processed", listenerCalled[0]);
        layer.processInput(new double[]{1.0, 2.0, 3.0});
        Assert.assertTrue("Listener should be called after input processed", listenerCalled[0]);
    }

    @Test
    public void processInputAppliesActivationFunction() throws Exception {
        int size = 3;

        AbstractLayer layer = new MockLayer(size);
        OutputListener listener = output -> Assert.assertArrayEquals(
                "Activation function should be called on the inputs", new double[]{2.0, 3.0, 4.0}, output, 0.0);

        layer.addOutputListener(listener);
        layer.processInput(new double[]{1.0, 2.0, 3.0});
    }

    @Test
    public void processInputRejectsIncorrectlySizedInput() throws Exception {
        int size = 3;
        AbstractLayer layer = new MockLayer(size);

        try {
            layer.processInput(new double[size + 1]);
        } catch (IllegalArgumentException e) {
            return;
        }

        Assert.fail("AbstractLayer should throw IllegalArgumentException when input of incorrect length processed");
    }

    @Test
    public void processInputSetsLastOutput() throws Exception {
        int size = 3;

        AbstractLayer layer = new MockLayer(size);

        Assert.assertNull("Layer should not have a previous ouput when created", layer.getLastOutput());
        layer.processInput(new double[]{1.0, 2.0, 3.0});

        Assert.assertArrayEquals("getLastOutput should get the outputs produced last by processInput",
                new double[]{2.0, 3.0, 4.0}, layer.getLastOutput(), 0.0);
    }

    @Test
    public void testGetLastOutput() throws Exception {
        AbstractLayer layer = new MockLayer(3);
        layer.processInput(new double[]{1.0, 2.0, 3.0});
    }

    @Test
    public void errorGradientsShouldBeNullWhenLayerInstantiated() {
        AbstractLayer layer = new MockLayer(3);
        Assert.assertNull("Error gradient should null when AbstractLayer instantiated", layer.getErrorGradients());
    }

    @Test
    public void testSetErrorGradient() throws Exception {
        AbstractLayer layer = new MockLayer(3);

        layer.setErrorGradient(0, 5.0);
        Assert.assertEquals("Error gradient should be set by setErrorGradient",
                5.0, layer.getErrorGradient(0), 0.0);
    }

    @Test
    public void testSetErrorGradients() throws Exception {
        AbstractLayer layer = new MockLayer(3);
        layer.setErrorGradients(new double[]{4.0, 5.0, 6.0});
        Assert.assertArrayEquals("Error gradients should be set by setErrorGradients",
                new double[]{4.0, 5.0, 6.0}, layer.getErrorGradients(), 0.0);
    }

    @Test
    public void setErrorGradientsDoesntAcceptGradientsOfIncorrectLength() throws Exception {
        int size = 3;
        AbstractLayer layer = new MockLayer(size);

        Assert.assertNull("Error gradient should null when AbstractLayer instantiated", layer.getErrorGradients());

        try {
            layer.setErrorGradients(new double[size + 1]);
        } catch (IllegalArgumentException e) {
            return;
        }

        Assert.fail("setErrorGradients should throw IllegalArgumentException if length of" +
                "error gradients array doesn't match layer size");
    }
}