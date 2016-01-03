package com.henrythompson.neuralnets.layers;

import java.lang.Override;import java.lang.String; /**
 * This class represents a layer of neurons which gives an output of 1
 * if the input is greater than 0; or an output of 0 if the input
 * is less than or equal ot 0.
 *
 * @author Henry Thompson
 */
public class ThresholdLayer extends AbstractLayer {

    public ThresholdLayer(int size) {
        super(size);
    }

    @Override
    protected double[] activationFunction(double[] netInput) {
        int n = size();
        double[] output = new double[n];

        for (int k = 0; k < n; k++) {
            output[k] = netInput[k] > 0 ? 1 : 0;
        }

        return output;
    }

    @Override
    public double[] getActivationDerivative() {
        int n = getLastOutput().length;
        double[] result = new double[n];

        for (int i = 0; i < n; i++) {
            result[i] = 1;
        }

        return result;
    }

    @Override
    public String typeName() {
        return "threshold";
    }
}