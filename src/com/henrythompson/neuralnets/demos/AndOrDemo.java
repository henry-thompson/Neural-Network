package com.henrythompson.neuralnets.demos;

import com.henrythompson.neuralnets.NeuralNetwork;
import com.henrythompson.neuralnets.networkbuilders.PerceptronBuilder;import java.lang.System;

/**
 * Demonstrates how neural networks can easily be used to simulate logical AND and
 * logical OR gates
 */
public class AndOrDemo {

    /**
     * Runs the logical AND/OR demo
     */
    public void run() {
        NeuralNetwork and = andNetwork();
        NeuralNetwork or = orNetwork();

        for (int i = 0; i < 4; i++) {
            double[] input = new double[]{i % 2, i < 2 ? 0 : 1};
            double[] outputAnd = and.processInput(input);
            System.out.println(input[0] + " AND " + input[1] + " = " + outputAnd[0]);

            double[] outputOr = or.processInput(input);
            System.out.println(input[0] + " OR " + input[1] + " = " + outputOr[0]);
            System.out.println();
        }
    }

    /**
     * @return A new instance of a network capable of acting as a logical AND
     */
    private NeuralNetwork andNetwork() {
        return new PerceptronBuilder(2, 1)
                .setWeights(AND_WEIGHTS)
                .create();
    }

    /**
     * @return A new instance of a network capable of acting as a logical OR
     */
    private NeuralNetwork orNetwork() {
        return new PerceptronBuilder(2, 1)
                .setWeights(OR_WEIGHTS)
                .create();
    }

    /** The weights required in a network to produce logical AND behaviour. The 1s
     *  are the weights between each input neuron and the output neuron; the -1 is
     *  the bias value for the output neuron */
    private static final double[][] AND_WEIGHTS = {{1}, {1}, {-1}};

    /** The weights required in a network to produce logical AND behaviour. The 1s
     *  are the weights between each input neuron and the output neuron; the 0 is
     *  the bias value for the output neuron */
    private static final double[][] OR_WEIGHTS = {{1}, {1}, {0}};
}