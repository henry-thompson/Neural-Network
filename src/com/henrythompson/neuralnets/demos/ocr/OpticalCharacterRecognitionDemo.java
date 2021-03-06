package com.henrythompson.neuralnets.demos.ocr;

import java.io.File;import java.io.FileNotFoundException;import java.lang.String;import java.lang.System;
import java.util.ArrayList;import java.util.List;

import com.henrythompson.neuralnets.*;

/**
 * Runs the OpticalCharacterRecognition program. The purpose of this class is so that we are able
 * to produce several networks all capable of optical character recognition (OCR) such that we can
 * compare their performances and analyse things such as:
 *
 * - What Cross-Entropy Error (CEE) value produces the best compromise between a network with too
 *   close a fit vs accuracy?
 * - What is the optimum number of neurons in the hidden layer?
 *
 * The way it does this is split into 3 different parts.
 *
 * 1) PRODUCE AND TRAIN NETWORKS
 *    Firstly, we produce neural networks with a single hidden layer and train it on samples. These
 *    samples must be provided as 35 comma-separated numerical values which act as the inputs to
 *    the network, and the filename must be "[letter].ann" where [letter] is the letter of the
 *    alphabet the input represents. For an explanation of how the 35 input values are generated,
 *    see the Extended Project Writeup.
 *
 *    We train networks with a hidden layer of size between 20 and 80 to obtain 60 networks overall.
 *    Each time the network approaches any from a set of "milestone" CEEs ({@see OcrStoppingCondition})
 *    we save each network to the OutputDirectory using the XML format generated by
 *    <code>NeuralNetworkExporter</code> with a filename specifying its hidden layer size and the
 *    CEE value at which it was saved.
 *
 *    Statistics on training are also saved, such as the number of epochs required to train the
 *    network to that CEE and the time taken. These are saved in a CSV file.
 *
 *  2) RELOAD AND TEST NETWORKS
 *     Once all networks are trained we load each one back into the program and test it against
 *     samples that it hasn't seen yet. We count the number of letters each letter correctly
 *     identifies, which is then its "accuracy rating".
 *
 *  3) RANK NETWORKS
 *     We order the networks by the accuracy ratings, and save these statistics to a CSV file
 *     in the output directory.
 *
 *  Right now this is one single program. It may be useful to separate out each section part of the
 *  process into its own command in the Command Line but for the purposes of this project where I
 *  am only interested in the final statistics produced, this is fine.
 *
 */
public class OpticalCharacterRecognitionDemo {
    /** The directory into which all files for statistics and the networks should be saved **/
    private final String mOutputDirectory;

    /** The directory containing the samples with which to train the networks **/
    private final String mTrainingSamplesDirectory;

    /** The directory containing the samples with which to assess the accuracy of trained networks **/
    private final String mUnseenSamplesDirectory;

    /**
     * @param outputFolder The directory into which all files for statistics and the networks should be saved
     * @param trainingSamplesDirectory The directory containing the samples with which to train the networks
     * @param unseenSamplesDirectory The directory containing the samples with which to assess the accuracy of trained networks
     */
    public OpticalCharacterRecognitionDemo(String outputFolder, String trainingSamplesDirectory, String unseenSamplesDirectory) {
        mOutputDirectory = outputFolder;
        mTrainingSamplesDirectory = trainingSamplesDirectory;
        mUnseenSamplesDirectory = unseenSamplesDirectory;
    }

    /**
     * Runs this program using the values passed to the object via the constructor
     */
    public void run() {
        if (createOutputFolder()) {
            List<TrainingSample> trainingSamples = loadTrainingSamples();

            if (trainingSamples != null) {
                List<OcrTrainingSummary> summaries = train(trainingSamples);
                exportTrainingStatistics(summaries);

                List<TrainedOcrNetwork> rankedNetworks = rankNetworks();
                saveRankings(rankedNetworks);
            }
        }
    }

    /**
     * Export rankings to the Rankings.csv file in the output directory
     * @param rankedNetworks The networks whose rankings are to be exported. These networks
     *                       which must all have been ranked before being passed to this method.
     */
    private void saveRankings(List<TrainedOcrNetwork> rankedNetworks) {
        if (rankedNetworks != null) {
            System.out.println("Exporting ranked network data");
            try {
                new OcrNetworkAccuracyRatingsExporter(rankedNetworks, mOutputDirectory + "\\Rankings.csv").saveNetworkRankResults();
            } catch (FileNotFoundException e) {
                System.out.println("Unable to save rankings data due to FileNotFoundExcpeion. Details:");
                System.out.println(e.getMessage());
                return;
            }
            System.out.println("Ranked network data export complete. See " + mOutputDirectory + "\\Rankings.csv");
        }
    }

    /**
     * Export the statistics about the training of each network to Statistics.csv in the output
     * directory.
     * @param summaries The summaries generated by the training procedure.
     */
    private void exportTrainingStatistics(List<OcrTrainingSummary> summaries) {
        System.out.println("Exporting training statistics");
        new OcrTrainingStatisticsCsvExporter(mOutputDirectory + "\\Statistics.csv", summaries).saveStatistics();
        System.out.println("Training statistics export complete. See " + mOutputDirectory + "\\Statistics.csv");
    }

    /**
     * Generate the appropriate networks and train each one against the provided
     * training samples.
     * @param samples The unseen training samples with which to train the neural network.
     * @return A list of objects which contains the training summaries for each configuration
     * of the neural network.
     */
    private ArrayList<OcrTrainingSummary> train(List<TrainingSample> samples) {
        System.out.println("Beginning training");
        ArrayList<OcrTrainingSummary> summaries = new ArrayList<>();

        for (int hiddenLayerSize = 20; hiddenLayerSize <= 80; hiddenLayerSize++) {
            summaries.addAll(new OcrTrainer(mOutputDirectory, hiddenLayerSize, samples).train());
        }

        System.out.println("Finished training");
        return summaries;
    }

    /**
     * Load the samples with which to train the networks into TrainingSample objects
     * @return The list of training samples loaded from the training samples directory
     */
    private List<TrainingSample> loadTrainingSamples() {
        try {
            System.out.println("Loading training samples");
            return new OcrSamplesImporter(mTrainingSamplesDirectory).loadSamples();
        } catch (FileNotFoundException e) {
            System.out.println("Training samples failed to load");
            System.out.print(e.getMessage());
            return null;
        }
    }

    /**
     * Creates the output directory, if it doesn't exist
     * @return {@code true} if the directory was found or successfully created;
     * {@code false} otherwise
     */
    private boolean createOutputFolder() {
        File outputDir = new File(mOutputDirectory);

        if (!outputDir.exists() || !outputDir.isDirectory()) {
            if (!outputDir.mkdirs()) {
                System.out.println("Failed to find or create output folder");
                return false;
            }
        }

        System.out.println("Output folder successfully found/created");
        return true;
    }

    /**
     * Loads the networks generated via the train() method back into the program
     * and tests each one for the accuracy rating.
     * @return A list of networks with their accuracy ratings
     */
    private List<TrainedOcrNetwork> rankNetworks() {
        try {
            System.out.println("Importing unseen samples");
            List<TrainingSample> unseenSamples = new OcrSamplesImporter(mUnseenSamplesDirectory).loadSamples();

            System.out.println("Re-importing outputted networks");
            List<TrainedOcrNetwork> networks = new OutputtedOcrNetworksImporter(mOutputDirectory).retrieveNetworks();

            System.out.println("Ranking networks");
            new OcrAccuracyRanker(networks, unseenSamples).rank();

            System.out.println("Ranking successful");

            return networks;

        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
}