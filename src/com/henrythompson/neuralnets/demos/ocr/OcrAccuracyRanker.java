package com.henrythompson.neuralnets.demos.ocr;

import com.henrythompson.neuralnets.TrainingSample;

import java.util.Collections;import java.util.List;

/**
 * By seeing how many of a set of unseen samples of letters that a set of
 * trained OCR networks correctly guesses, ranks the relative performances
 * of each network against each other. The "accuracy rating" assigned to
 * each one is a count of how many of the training samples the network
 * correctly guesses.
 */
public class OcrAccuracyRanker {
    private final List<TrainedOcrNetwork> mNetworks;
    private final List<TrainingSample> mSamples;

    public OcrAccuracyRanker(List<TrainedOcrNetwork> networks, List<TrainingSample> samples) {
        mNetworks = networks;
        mSamples = samples;
    }

    public void rank() {
        mNetworks.forEach(this::testNetworkAccuracy);
        Collections.sort(mNetworks, (n1, n2) -> n2.getAccuracyRating() - n1.getAccuracyRating());
    }

    private void testNetworkAccuracy(TrainedOcrNetwork network) {
        int accuracyRating = 0;

        for (TrainingSample s : mSamples) {
            if (didNetworkGuessCorrectly(network, s)) {
                accuracyRating++;
            }
        }

        network.setAccuracyRating(accuracyRating);
    }

    private boolean didNetworkGuessCorrectly(TrainedOcrNetwork network, TrainingSample sample) {
        double highestProbability = -1;
        boolean correct = false;

        double[] expectedOutput = sample.getExpectedOutput();
        double[] output = network.getNetwork().processInput(sample.getInput());

        for (int i = 0; i < output.length; i++) {
            if (output[i] > highestProbability) {
                highestProbability = output[i];
                correct = expectedOutput[i] == 1;
            }
        }

        return correct;
    }
}
