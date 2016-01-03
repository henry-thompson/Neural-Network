package com.henrythompson.neuralnets.layers;

import java.lang.Math;import java.lang.Override;import java.lang.String; /**
 * This class represents a layer of neurons which applies the Sigmoid function
 * to their inputs and pass the result as outputs.
 *
 * @author Henry Thompson
 */
public class SigmoidLayer extends AbstractLayer {
    public SigmoidLayer(int size) {
        super(size);
    }

    @Override
    protected double[] activationFunction(double[] netInput) {
        int n = size();
        double[] output = new double[n];

        for (int k = 0; k < n; k++) {
            output[k] = 1 / (1 + Math.exp(-netInput[k]));
        }

        return output;
    }

    @Override
    public double[] getActivationDerivative() {
        double[] outputs = getLastOutput();

        int n = outputs.length;
        double[] results = new double[n];

        for (int i = 0; i < n; i++) {
            double out = outputs[i];
            results[i] = out * (1 - out);
        }

        return results;
    }

    @Override
    public String typeName() {
        return "sigmoid";
    }
}