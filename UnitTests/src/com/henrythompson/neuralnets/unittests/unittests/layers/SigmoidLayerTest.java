package com.henrythompson.neuralnets.unittests.unittests.layers;

import com.henrythompson.neuralnets.layers.SigmoidLayer;
import org.junit.Assert;
import org.junit.Test;

public class SigmoidLayerTest {
    @Test
    public void testActivationFunction() {
        SigmoidLayer layer = new SigmoidLayer(3);

        double[] output = layer.processInput(new double[]{0.1, 2.0, -1.2});
        Assert.assertArrayEquals("Method should apply Sigmoid function to each input",
                new double[]{0.52498, 0.88080, 0.23148}, output, 5E-5);
    }

    @Test
    public void testActivationDerivative() {
        SigmoidLayer layer = new SigmoidLayer(3);
        layer.processInput(new double[]{0.1, 2.0, -1.2});

        Assert.assertArrayEquals("Method should apply derivative of Sigmoid function to each input",
                new double[]{0.24938, 0.10499, 0.17789}, layer.getActivationDerivative(), 5E-5);
    }

    @Test
    public void nameIsSigmoid() {
        SigmoidLayer layer = new SigmoidLayer(3);
        Assert.assertEquals("Name of SigmoidLayer should be 'sigmoid'", "sigmoid", layer.typeName());
    }
}
