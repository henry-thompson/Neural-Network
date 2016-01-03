package com.henrythompson.neuralnets.unittests.unittests;

import com.henrythompson.neuralnets.NeuralNetwork;
import com.henrythompson.neuralnets.Synapse;
import com.henrythompson.neuralnets.layers.AbstractLayer;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

public class NeuralNetworkTest {
    private static class NeuralNetworkHolder {
        public NeuralNetwork network;
        public MockLayer layer1;
        public MockLayer layer2;
        public int firstLayerSize;
        public int secondLayerSize;
        public Synapse synapse;
    }

    private NeuralNetworkHolder createNeuralNetwork() {
        int firstLayerSize = 3;
        int secondLayerSize = 4;

        MockLayer layer1 = new MockLayer(firstLayerSize);
        MockLayer layer2 = new MockLayer(secondLayerSize);

        ArrayList<Synapse> synapses = new ArrayList<>();
        synapses.add(new Synapse(layer1, layer2, new MockWeights(firstLayerSize, secondLayerSize)));

        NeuralNetwork network = new NeuralNetwork(synapses);
        NeuralNetworkHolder holder = new NeuralNetworkHolder();

        holder.layer1 = layer1;
        holder.layer2 = layer2;
        holder.firstLayerSize = firstLayerSize;
        holder.secondLayerSize = secondLayerSize;
        holder.network = network;
        holder.synapse = synapses.get(0);

        return holder;
    }

    @Test
    public void testProcessInput() throws Exception {
        NeuralNetwork network = createNeuralNetwork().network;

        double[] output = network.processInput(new double[]{0.0, 1.0, 2.0});
        Assert.assertArrayEquals("Method processInput(...) did not obtain correct output",
                new double[]{7.0, 7.0, 7.0, 7.0}, output, 0.0);
    }

    @Test
    public void processInputDisallowsInputWhichIsTooSmall() throws Exception {
        NeuralNetwork network = createNeuralNetwork().network;

        try {
            network.processInput(new double[]{1.0});
        } catch (IllegalArgumentException e) {
            return;
        }

        Assert.fail("Network should throw illegal argument if input too short");
    }

    @Test
    public void processInputDisallowsInputWhichIsTooLarge() throws Exception {
        NeuralNetwork network = createNeuralNetwork().network;

        try {
            network.processInput(new double[]{1.0, 2.0, 3.0, 4.0});
        } catch (IllegalArgumentException e) {
            return;
        }

        Assert.fail("Network should throw illegal argument if input too large");
    }

    @Test
    public void testGetInputLayer() throws Exception {
        NeuralNetworkHolder holder = createNeuralNetwork();

        Assert.assertEquals("NeuralNetwork should retrieve first layer correctly",
                holder.layer1, holder.network.getInputLayer());
    }

    @Test
    public void testGetOutputLayer() throws Exception {
        NeuralNetworkHolder holder = createNeuralNetwork();

        Assert.assertEquals("NeuralNetwork should retrieve final layer correctly",
                holder.layer2, holder.network.getOutputLayer());
    }

    @Test
    public void testGetSynapse() throws Exception {
        NeuralNetworkHolder holder = createNeuralNetwork();

        Assert.assertEquals("NeuralNetwork should retrieve specified synapse correctly",
                holder.synapse, holder.network.getSynapse(0));
    }

    @Test
    public void testGetSynapses() throws Exception {
        NeuralNetworkHolder holder = createNeuralNetwork();
        ArrayList<Synapse> synapses = new ArrayList<>();
        synapses.add(holder.synapse);

        Assert.assertEquals("NeuralNetwork should return all of its synapses",
                synapses, holder.network.getSynapses());

    }

    @Test
    public void testGetLayers() throws Exception {
        NeuralNetworkHolder holder = createNeuralNetwork();
        ArrayList<AbstractLayer> layers = new ArrayList<>();
        layers.add(holder.layer1);
        layers.add(holder.layer2);

        Assert.assertEquals("NeuralNetwork should return all of its synapses",
                layers, holder.network.getLayers());

    }
}