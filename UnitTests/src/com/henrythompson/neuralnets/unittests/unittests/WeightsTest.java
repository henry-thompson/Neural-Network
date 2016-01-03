package com.henrythompson.neuralnets.unittests.unittests;

import com.henrythompson.neuralnets.Weights;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class WeightsTest {

    @Test
    public void weightsWithNegativeSizeDisallowed() throws Exception {
        try {
            new Weights(-1, -1);
        } catch (IllegalArgumentException e) {
            return;
        }

        Assert.fail("Weights should not allow instantiation with negative sizes");
    }

    @Test
    public void testSetAndGetWeight() throws Exception {
        Weights weights = new Weights(3, 3);
        Assert.assertNotEquals("Weight should not be 0.5 after instantiation", 0.5, weights.getWeight(0, 0), 0.0);
        weights.setWeight(0, 0, 0.5);
        Assert.assertEquals("Weight should be set to 0.5 after instantiation", 0.5, weights.getWeight(0, 0), 0.0);
    }

    @Test
    public void setWeightDisallowsSettingBiasAtIndexTooLarge() {
        Weights weights = new Weights(3, 3);

        try {
            weights.setWeight(3, 3, 0.1);
        } catch (IndexOutOfBoundsException e) {
            return;
        }

        Assert.fail("Weights should disallow setting a weight at an index which is too large");
    }

    @Test
    public void setWeightDisallowsSettingBiasAtNegativeIndex() {
        Weights weights = new Weights(3, 3);

        try {
            weights.setWeight(-1, -1, 0.1);
        } catch (IndexOutOfBoundsException e) {
            return;
        }

        Assert.fail("Weights should disallow setting a weight at an index which is negative");
    }

    @Test
    public void getWeightDisallowsSettingBiasAtIndexTooLarge() {
        Weights weights = new Weights(3, 3);

        try {
            weights.getWeight(3, 3);
        } catch (IndexOutOfBoundsException e) {
            return;
        }

        Assert.fail("Weights should disallow setting a weight at an index which is too large");
    }

    @Test
    public void getWeightDisallowsSettingBiasAtNegativeIndex() {
        Weights weights = new Weights(3, 3);

        try {
            weights.getWeight(-1, -1);
        } catch (IndexOutOfBoundsException e) {
            return;
        }

        Assert.fail("Weights should disallow setting a weight at an index which is negative");
    }

    @Test
    public void testSetAndGetBias() throws Exception {
        Weights weights = new Weights(3, 3);
        Assert.assertNotEquals("Weight should not be 0.5 after instantiation", 0.5, weights.getBias(0), 0.0);
        weights.setBias(0, 0.5);
        Assert.assertEquals("Weight should be set to 0.5 after instantiation", 0.5, weights.getBias(0), 0.0);
    }

    @Test
    public void setBiasDisallowsIndicesWhichAreTooLarge() {
        Weights weights = new Weights(3, 3);

        try {
            weights.setBias(3, 0.1);
        } catch (IndexOutOfBoundsException e) {
            return;
        }

        Assert.fail("Weights should disallow setting a weight at an index which is too large");
    }

    @Test
    public void setBiasDisallowsNegativeIndices() {
        Weights weights = new Weights(3, 3);

        try {
            weights.setBias(-1, 0.1);
        } catch (IndexOutOfBoundsException e) {
            return;
        }

        Assert.fail("Weights should disallow setting a weight at an index which is too large");
    }

    @Test
    public void getBiasDisallowsIndicesWhichAreTooLarge() {
        Weights weights = new Weights(3, 3);

        try {
            weights.getBias(3);
        } catch (IndexOutOfBoundsException e) {
            return;
        }

        Assert.fail("Weights should disallow setting a weight at an index which is too large");
    }

    @Test
    public void getBiasDisallowsNegativeIndices() {
        Weights weights = new Weights(3, 3);

        try {
            weights.getBias(-1);
        } catch (IndexOutOfBoundsException e) {
            return;
        }

        Assert.fail("Weights should disallow setting a weight at an index which is too large");
    }

    @Test
    public void testAdjustWeight() throws Exception {
        Weights weights = new Weights(3, 3);

        Assert.assertNotEquals("Weight should not be set to -0.5 when Weights object is initialised",
                -0.5, weights.getWeight(0, 0), 0.0);
        weights.adjustWeight(0, 0, -0.5);
        Assert.assertEquals("Method adjustWeight should adjust weight correctly", -0.5, weights.getWeight(0, 0), 0.0);
    }

    @Test
    public void testGetFromLayerSize() throws Exception {
        Weights weights = new Weights(3, 4);
        Assert.assertEquals("Method getFromLayerSize should correctly return supplied from layer size",
                3, weights.getFromLayerSize());
    }

    @Test
    public void testGetToLayerSize() throws Exception {
        Weights weights = new Weights(3, 4);
        Assert.assertEquals("Method getToLayerSize should correctly return supplied from layer size",
                4, weights.getToLayerSize());
    }

    @Test
    public void testRandomize() throws Exception {
        // Very hard to test the randomness of the values. Instead, we will test
        // to make sure that the weights are no longer all zero (statistically
        // this shouldn't occur) and that they lie within the amplitude. Since
        // this is random and statistically could by chance pass when it should
        // fail, we repeat this 100 times.

        for (int i = 0; i < 100; i++) {
            Weights weights = new Weights(1, 2);

            Assert.assertEquals("Wieghts should be initialised to zero", weights.getWeight(0, 0), 0.0, 0.0);
            Assert.assertEquals("Wieghts should be initialised to zero", weights.getWeight(0, 1), 0.0, 0.0);

            double amplitude = 3 * Math.random();
            weights.randomize(amplitude);

            Assert.assertNotEquals("Weights should (statistically) not be exactly zero)",
                    weights.getWeight(0, 0), 0.0, 0.0);
            Assert.assertNotEquals("Wieghts should (statistically) not be exactly zero",
                    weights.getWeight(0, 1), 0.0, 0.0);

            Assert.assertTrue("Weights should be less than amplitude", Math.abs(weights.getWeight(0, 0)) < amplitude);
            Assert.assertTrue("Weights should be less than amplitude", Math.abs(weights.getWeight(0, 1)) < amplitude);
        }
    }
}