package com.henrythompson.neuralnets.unittests.unittests.layers;

import com.henrythompson.neuralnets.layers.ThresholdLayer;
import org.junit.Assert;
import org.junit.Test;

public class ThresholdLayerTest {
    @Test
    public void testActivationFunction() {
        ThresholdLayer layer = new ThresholdLayer(4);

        double[] output = layer.processInput(new double[]{0.1, 2.0, -1.2, 0.0});
        Assert.assertArrayEquals("Method should apply Threshold function to each input",
                new double[]{1.0, 1.0, 0.0, 0.0}, output, 5E-5);
    }

    @Test
    public void testActivationDerivative() {
        ThresholdLayer layer = new ThresholdLayer(3);
        layer.processInput(new double[]{0.1, 2.0, -1.2});

        Assert.assertArrayEquals("Method should apply derivative of Threshold function to each input",
                new double[]{1.0, 1.0, 1.0}, layer.getActivationDerivative(), 5E-5);
    }

    @Test
    public void nameIsThreshold() {
        ThresholdLayer layer = new ThresholdLayer(3);
        Assert.assertEquals("Name of ThresholdLayer should be 'threshold'", "threshold", layer.typeName());
    }
}
