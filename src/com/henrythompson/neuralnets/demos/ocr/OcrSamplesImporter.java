package com.henrythompson.neuralnets.demos.ocr;

import com.henrythompson.neuralnets.TrainingSample;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.Double;import java.lang.String;import java.util.ArrayList;
import java.util.List;

/**
 * Imports training samples from disk.
 *
 * The training samples must be saved in a comma-separated format, where the nth comma-separated
 * value represents the value to input into the neural network. There must be 35 of these values -
 * the reason for this is explained in the Extended Project writeup
 */
public class OcrSamplesImporter {
    private final File mSamplesDirectory;

    public OcrSamplesImporter(String samplesDirectoryPath) {
        mSamplesDirectory = new File(samplesDirectoryPath);
    }

    public ArrayList<TrainingSample> loadSamples() throws FileNotFoundException {
        if (!mSamplesDirectory.exists() || !mSamplesDirectory.isDirectory()) {
            throw new FileNotFoundException(directoryNotFoundExceptionMessage());
        }

        ArrayList<TrainingSample> samples = new ArrayList<>();
        File[] files = mSamplesDirectory.listFiles();

        if (files == null) {
            throw new FileNotFoundException(directoryListingFailedExceptionMessage());
        }

        for (File f : files) {
            if (f.getName().endsWith(".csv")) {
                loadSample(f, samples);
            }
        }

        return samples;
    }

    private String directoryListingFailedExceptionMessage() {
        return "Failed to list directory " + mSamplesDirectory;
    }

    private String directoryNotFoundExceptionMessage() {
        return "Cannot find directory " + mSamplesDirectory + ". This folder must exist and contain " +
                "all of the training samples. Please try again once this is done.";
    }

    public void loadSample(File file, List<TrainingSample> samples) {
        double[] expectedOutput = getExpectedOutput(file.getName());

        if (expectedOutput != null) {
            double[] values = getSampleValues(file);

            if (values != null) {
                samples.add(new TrainingSample(values, expectedOutput));
            }
        }
    }

    private double[] getSampleValues(File f) {
        String contents = new FileToStringReader(f).readContents();

        if (contents == null) {
            return null;
        }

        String[] items = contents.split(",");
        double[] values = new double[35];

        for (int i = 0; i < 35; i++) {
            values[i] = Double.parseDouble(items[i]);
        }

        return values;
    }

    private double[] getExpectedOutput(String letterName) {
        double[] result = new double[26];

        letterName = letterName.split("_")[0];

        // 65 is ASCII for A
        int letterIndex = letterName.charAt(0) - 65;

        result[letterIndex] = 1;
        return result;
    }
}
