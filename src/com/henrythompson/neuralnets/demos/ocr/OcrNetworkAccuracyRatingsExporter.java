package com.henrythompson.neuralnets.demos.ocr;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.lang.String;import java.lang.StringBuilder;import java.util.List;

/**
 * Saves the ratings for a given list of networks to a CSV file
 */
public class OcrNetworkAccuracyRatingsExporter {
    private final List<TrainedOcrNetwork> mNetworks;
    private final String mExportFilename;

    public OcrNetworkAccuracyRatingsExporter(List<TrainedOcrNetwork> networks, String exportFilename) {
        mNetworks = networks;
        mExportFilename = exportFilename;
    }

    public void saveNetworkRankResults() throws FileNotFoundException {
        StringBuilder builder = new StringBuilder("Hidden Layer Size,CEE,Score\n");

        for (TrainedOcrNetwork network : mNetworks) {
            builder.append(network.getHiddenLayerSize() + "," + network.getCee() + "," + network.getAccuracyRating() + "\n");
        }

        PrintWriter writer = new PrintWriter(mExportFilename);
        writer.append(builder.toString());
        writer.close();
    }
}
