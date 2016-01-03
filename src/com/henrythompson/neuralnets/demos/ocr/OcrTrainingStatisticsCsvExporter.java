package com.henrythompson.neuralnets.demos.ocr;

import com.henrythompson.neuralnets.TrainingStatistics;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.lang.String;import java.lang.StringBuilder;import java.lang.System;import java.util.List;

public class OcrTrainingStatisticsCsvExporter {
    private final String mOutputFilename;
    private final List<OcrTrainingSummary> mSummaries;

    public OcrTrainingStatisticsCsvExporter(String outputFilename, List<OcrTrainingSummary> summaries) {
        mOutputFilename = outputFilename;
        mSummaries = summaries;
    }

    public void saveStatistics() {
        System.out.println("Saving statistics");

        File output = new File(mOutputFilename);
        PrintWriter writer;

        try {
            writer = new PrintWriter(output);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("Error: failed to save statistics");
            return;
        }

        writer.append(generateStatisticsCsvContents());
        writer.close();

        System.out.println("Statistics saved successfully");
    }

    private String generateStatisticsCsvContents() {
        StringBuilder builder = new StringBuilder("Hidden Layer Size,Attempt,Aborted,Epochs,Time Taken\n");

        for (OcrTrainingSummary summary: mSummaries) {
            TrainingStatistics stats = summary.getTrainingStatistics();

            builder.append(summary.getHiddenLayerNumber() + "," + summary.getAttemptNumber() + ",");
            builder.append(stats.wasAborted() + "," + stats.getEpochs() + "," + stats.getTimeTaken() + "\n");
        }

        return builder.toString();
    }
}
