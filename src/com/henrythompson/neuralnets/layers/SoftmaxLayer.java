package com.henrythompson.neuralnets.layers;

import java.lang.Math;import java.lang.Override;import java.lang.String; /**
 * This class represents a layer of neurons which applies the Softmax function
 * to their inputs and pass the result as outputs.
 *
 * @author Henry Thompson
 */
public class SoftmaxLayer extends AbstractLayer {

    public SoftmaxLayer(int size) {
        super(size);
    }

    @Override
    protected double[] activationFunction(double[] netInputs) {
        int n = size();

        double sum = 0;
        double[] output = new double[n];

        for (int k = 0; k < n; k++) {
            output[k] = Math.exp(netInputs[k]);
            sum += output[k];
        }
        
        for (int k = 0; k < n; k++) {
            output[k] = output[k] / sum;
        }
        
        return output;
    }

    @Override
    public double[] getActivationDerivative() {
        double[] outputs = getLastOutput();

        int  n = outputs.length;
        double[] result = new double[outputs.length];

        for (int k = 0; k < n; k++) {
            result[k] = outputs[k] * (1 - outputs[k]);
        }

        return result;
    }

    @Override
    public String typeName() {
        return "softmax";
    }
}
