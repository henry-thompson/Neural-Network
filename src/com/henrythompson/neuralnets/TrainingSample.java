package com.henrythompson.neuralnets;

import java.lang.Math; /**
 * Represnts a training sample on which the neural network can be trained. This
 * class holds both a specified input as well as the output expected should that
 * input be passed to it. It also provides methods to calculate statistics on how
 * close a given output is to the expected output.
 */
public class TrainingSample {
    /** The input to feed the neural network */
    private final double[] mInput;

    /** The output expected from the neural network if {@code mInput} was fed into it */
    private final double[] mExpectedOutput;

    /**
     * @param input The input to feed the neural network
     * @param expectedOutput The output expected from the neural network if the given input was fed into it
     */
    public TrainingSample(double[] input, double[] expectedOutput) {
        mInput = input;
        mExpectedOutput = expectedOutput;
    }

    /**
     * @return The input to feed the neural network
     */
    public double[] getInput() {
        return mInput;
    }

    /**
     * @return The output expected from the neural network if the given input was fed into it
     */
    public double[] getExpectedOutput() {
        return mExpectedOutput;
    }

    /**
     * Calculates the sum squared error between each value of an output and the expected
     * value of that output.
     * @param actualOutput The output values to compare with the expected output values
     * @return The sum squared error between each value of an output and the expected
     * value of that ouput.
     */
    public double[] getSumSquaredError(double[] actualOutput) {
        int n = actualOutput.length;
        double[] errors = new double[n];

        for (int i = 0; i < n; i++) {
            errors[i] = 0.5 * Math.pow((mExpectedOutput[i] - actualOutput[i]), 2);
        }

        return errors;
    }

    /**
     * Calculates the sum of all the sum squared errors between each value of an output and
     * the expected value of that output.
     * @param actualOutput The output values to compare with the expected output values
     * @return The sum of all the sum squared errors between each value of an output and
     * the expected value of that output.
     */
    public double getTotalSumSquaredError(double[] actualOutput) {
        double[] errors = getSumSquaredError(actualOutput);
        double sse = 0;

        for (double error : errors) {
            sse += error;
        }

        return sse;
    }

    /**
     * Calculates the cross-entropy error between each value of an output and the expected
     * value of that output.
     * @param actualOutput The output values to compare with the expected output values
     * @return The cross-entropy error between each value of an output and the expected
     * value of that ouput.
     */
    public double[] getCrossEntropyError(double[] actualOutput) {
        int n = actualOutput.length;
        double[] errors = new double[n];

        for (int i = 0; i < n; i++) {
            errors[i] = - (mExpectedOutput[i] * Math.log(actualOutput[i]));
        }

        return errors;
    }

    /**
     * Calculates the sum of all the cross-entropy errors between each value of an output and
     * the expected value of that output.
     * @param actualOutput The output values to compare with the expected output values
     * @return The sum of all the cross-entropy errors between each value of an output and
     * the expected value of that output.
     */
    public double getTotalCrossEntropyError(double[] actualOutput) {
        double[] errors = getCrossEntropyError(actualOutput);
        double cee = 0;

        for (double error : errors) {
            cee += error;
        }

        return cee;
    }

    /**
     * Calculated the difference between each value of an output and the expected
     * value of that output.
     * @param actualOutput The output values to compare with the expected output values
     * @return The difference between each value of an output and the expected
     * value of that ouput.
     */
    public double[] getDifference(double[] actualOutput) {
        int n = actualOutput.length;
        double[] errors = new double[n];

        for (int i = 0; i < n; i++) {
            errors[i] = mExpectedOutput[i] - actualOutput[i];
        }

        return errors;
    }
}