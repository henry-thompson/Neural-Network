package com.henrythompson.neuralnets.networkbuilders;

import java.util.ArrayList;

import com.henrythompson.neuralnets.*;
import com.henrythompson.neuralnets.layers.AbstractLayer;
import com.henrythompson.neuralnets.layers.LinearLayer;
import com.henrythompson.neuralnets.layers.SigmoidLayer;

public class XORNetworkBuilder {
    private double mWeightRandomisationAmplitude = 0.2;

    public XORNetworkBuilder setRandomisationAmplitude(double amplitude) {
        mWeightRandomisationAmplitude = amplitude;
        return this;
    }

    public NeuralNetwork create() {
        return new NeuralNetwork(generateSynapses());
    }
    
    private ArrayList<AbstractLayer> generateLayers() {
        ArrayList<AbstractLayer> layers = new ArrayList<>();
        layers.add(new LinearLayer(2));
        layers.add(new SigmoidLayer(2));
        layers.add(new SigmoidLayer(1));
        
        return layers;
    }
    
    private ArrayList<Synapse> generateSynapses() {
        ArrayList<Synapse> synapses = new ArrayList<>();
        ArrayList<AbstractLayer> layers = generateLayers();

        Synapse synapse1 = new Synapse(layers.get(0), layers.get(1));
        synapse1.randomiseWeights(mWeightRandomisationAmplitude);
        
        Synapse synapse2 = new Synapse(layers.get(1), layers.get(2));
        synapse2.randomiseWeights(mWeightRandomisationAmplitude);
        
        synapses.add(synapse1);
        synapses.add(synapse2);
        
        return synapses;
    }
}