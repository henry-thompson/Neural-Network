package com.henrythompson.neuralnets.layers;

import java.lang.Override;import java.lang.String;import java.lang.System;

/**
 * This class represents a layer of neurons which pass
 * the value they were given as inputs as outputs, without
 * performing any transformation.
 *
 * @author Henry Thompson
 */
public class LinearLayer extends AbstractLayer {

    public LinearLayer(int size) {
        super(size);
    }

    @Override
    protected double[] activationFunction(double[] netInput) {
        double[] result = new double[netInput.length];
        System.arraycopy(netInput, 0, result, 0, netInput.length);

        return result;
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
        return "linear";
    }
}