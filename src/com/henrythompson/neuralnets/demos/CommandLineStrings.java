package com.henrythompson.neuralnets.demos;

import java.lang.Object;import java.lang.String; /**
 * Contains String constants used as inputs and as outputs in the command line application
 * starting from the class {@code Demos}
 */
public class CommandLineStrings {
    public static final String COMMAND_ANDOR_DEMO = "ANDORDEMO";
    public static final String COMMAND_CLASSIFIER_DEMO = "CLASSIFIERDEMO";
    public static final String COMMAND_PERCEPTRON_DEMO = "PERCEPTRONDEMO";
    public static final String COMMAND_XOR_DEMO = "XORDEMO";
    public static final String COMMAND_OCR_DEMO = "OCRDEMO";
    public static final String COMMAND_QUIT = "QUIT";

    public static final Object INVALID_COMMAND_MESSAGE =
            "The following commands are accepted:\n" +

            COMMAND_OCR_DEMO +
                    "\t\t\t\tProduces several neural networks which can all be potentially used in Optical Character Recognition\n" +
                    "\t\t\t\t\tsoftware. It uses a set of training samples which must be present in a specified folder to produce\n" +
                    "\t\t\t\t\tnetworks each with a single hidden layer of sizes from 20 to 80. Each of these 60 networks are\n" + "" +
                    "\t\t\t\t\texported to the specified output folder when a certain set of CEE values are reached, after which\n" +
                    "\t\t\t\t\tthey are reloaded by the program and their performances assessed against a set of unseen training\n" +
                    "\t\t\t\t\tsamples. The performances are exported in CSV format. The  statistics for how many epochs were\n"+
                    "\t\t\t\t\trequired to train each network are also exported in CSV format to the output folder.\n\n" +
                    "\t\t\t\t\tArguments:\n\n" +
                    "\t\t\t\t\t" + COMMAND_OCR_DEMO + " [outputFolder] [trainingSamplesFolder] [unseenSamplesFolder]\n\n" +

            COMMAND_ANDOR_DEMO + "\t\t\tDemonstrates how neural networks can be used as logical AND and logical OR gates\n" +
            COMMAND_CLASSIFIER_DEMO + "\t\tDemonstrates how neural networks can classify points as being within areas in a vectorspace\n" +
            COMMAND_PERCEPTRON_DEMO + "\t\tBasic demonstration of how perceptrons can classifty points about a linear line\n" +
            COMMAND_XOR_DEMO + "\t\t\t\tDemonstrates how neural networks can be used as logical XOR gates\n" +
            COMMAND_QUIT + "\t\t\t\tQuit application";

    public static final String WELCOME_MESSAGE = "Type a command or 'HELP' to begin";
}
