package com.henrythompson.neuralnets.demos.ocr;

import com.henrythompson.neuralnets.NeuralNetwork;
import com.henrythompson.neuralnets.importexport.NeuralNetworkImporter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.lang.Double;import java.lang.Integer;import java.lang.String;import java.lang.System;import java.util.ArrayList;
import java.util.List;

public class OutputtedOcrNetworksImporter {
    private final String mOutputDirectory;

    public OutputtedOcrNetworksImporter(String containingDirectory) {
        mOutputDirectory = containingDirectory;
    }

    public List<TrainedOcrNetwork> retrieveNetworks() throws FileNotFoundException {
        File[] files = new File(mOutputDirectory).listFiles();

        if (files == null) {
            throw new FileNotFoundException(directoryListingFailedExceptionMessage());
        }

        return loadNetworks(files);
    }

    private ArrayList<TrainedOcrNetwork> loadNetworks(File[] files) throws FileNotFoundException {
        ArrayList<TrainedOcrNetwork> networks = new ArrayList<>();

        for (File f : files) {
            try {
                if (f.getName().endsWith(".ann")) {
                    networks.add(importNeuralNetwork(f));
                }
            } catch (InvalidFilenameException e) {
                System.out.println(e.getMessage());
            }
        }

        return networks;
    }

    private String directoryListingFailedExceptionMessage() {
        return "Failed to list directory " + mOutputDirectory;
    }

    private TrainedOcrNetwork importNeuralNetwork(File file) throws FileNotFoundException, InvalidFilenameException {
        String[] name = file.getName().split("-");

        if (name.length >= 4) {
            throw new InvalidFilenameException(invalidFilenameExceptionMessage(file.getName()));
        }

        int hiddenLayerSize = Integer.parseInt(name[name.length - 3]);
        double cee = Double.parseDouble(name[name.length - 2]);

        NeuralNetwork network = new NeuralNetworkImporter(new FileInputStream(file)).importNetwork();
        return new TrainedOcrNetwork(hiddenLayerSize, cee, network);
    }

    private String invalidFilenameExceptionMessage(String filename) {
        return "Invalid name " + filename + "Sample files must be of format -[layer size]-[CEE value]-.ann";
    }
}
