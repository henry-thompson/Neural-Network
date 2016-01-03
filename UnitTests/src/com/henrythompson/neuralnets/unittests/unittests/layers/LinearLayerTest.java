package com.henrythompson.neuralnets.unittests.unittests.layers;

import com.henrythompson.neuralnets.layers.LinearLayer;
import org.junit.Assert;
import org.junit.Test;

public class LinearLayerTest {
    @Test
    public void testActivationFunction() {
        LinearLayer layer = new LinearLayer(3);
        double[] input = new double[]{1.0, 2.0, 3.0};
        layer.processInput(input);

        Assert.assertArrayEquals("LinearLayer output should equal input",
                input, layer.getLastOutput(), 0.0);
    }

    @Test
    public void activationFunctionDoesNotReturnSameArrayObject() {
        LinearLayer layer = new LinearLayer(3);
        double[] input = new double[]{1.0, 2.0, 3.0};
        double[] output = layer.processInput(input);

        Assert.assertNotEquals("Activation function should not return same object as input", input, output);
    }

    @Test
    public void activationDerivativeIsOne() {
        LinearLayer layer = new LinearLayer(3);
        double[] input = new double[]{1.0, 2.0, 3.0};
        layer.processInput(input);

        Assert.assertArrayEquals("All activation derivatives of LinearLayout are 1",
                layer.getActivationDerivative(), new double[]{1.0, 1.0, 1.0}, 0.0);
    }

    @Test
    public void nameIsLinear() {
        LinearLayer layer = new LinearLayer(3);
        Assert.assertEquals("Name of LinearLayer should be 'linear'", "linear", layer.typeName());
    }
}
