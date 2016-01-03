package com.henrythompson.neuralnets.unittests.unittests.layers;

import com.henrythompson.neuralnets.layers.SoftmaxLayer;
import org.junit.Assert;
import org.junit.Test;

public class SoftmaxLayerTest {
    @Test
    public void testActivationFunction() {
        SoftmaxLayer layer = new SoftmaxLayer(3);

        double[] output = layer.processInput(new double[]{0.1, 2.0, -1.2});
        Assert.assertArrayEquals("Method should apply Softmax function to each input",
                new double[]{0.12565, 0.84010, 0.03424}, output, 5E-5);
    }

    @Test
    public void testActivationDerivative() {
        SoftmaxLayer layer = new SoftmaxLayer(3);
        layer.processInput(new double[]{0.1, 2.0, -1.2});

        Assert.assertArrayEquals("Method should apply derivative of Softmax function to each input",
                new double[]{0.10986, 0.134330, 0.03307}, layer.getActivationDerivative(), 5E-5);
    }

    @Test
    public void nameIsSoftmax() {
        SoftmaxLayer layer = new SoftmaxLayer(3);
        Assert.assertEquals("Name of SoftmaxLayer should be 'softmax'", "softmax", layer.typeName());
    }
}

