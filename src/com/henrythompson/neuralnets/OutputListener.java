package com.henrythompson.neuralnets;

/** Provides an interface through which other classes
 * can recieve the output of a layer
 * @author Henry Thompson
 */
public interface OutputListener {
    /** Called when the layer produces an output
     * @param output The values of the output from
     * each neuron in the layer
     */
    void onOutput(double[] output);
}
