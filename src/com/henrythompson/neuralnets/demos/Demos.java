package com.henrythompson.neuralnets.demos;

import com.henrythompson.neuralnets.demos.ocr.OpticalCharacterRecognitionDemo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;import java.lang.String;import java.lang.System;

/**
 * Controls the command-line interface from which all the demonstrations of the
 * neural networks written as part of this Extended Project can be run.
 *
 * To understand how to use this program, run it with this class as the main
 * class and type 'HELP'.
 */
public class Demos {
    /**
     * The entry point of the program
     * @param args Should be empty. Doesn't matter if not.
     * @throws IOException if the command line InputStream cannot be found.
     */
    public static void main(String[] args) throws IOException {
        System.out.println(CommandLineStrings.WELCOME_MESSAGE);
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

        while (true) {
            String[] commands = in.readLine().split(" ");

            switch (commands[0]) {
                case CommandLineStrings.COMMAND_OCR_DEMO:
                    runOpticalCharcterRecognitionDemo(commands);
                    continue;
                case CommandLineStrings.COMMAND_ANDOR_DEMO:
                    new AndOrDemo().run();
                    continue;
                case CommandLineStrings.COMMAND_CLASSIFIER_DEMO:
                    new ClassifierDemo().run();
                    continue;
                case CommandLineStrings.COMMAND_PERCEPTRON_DEMO:
                    new PerceptronDemo().run();
                    continue;
                case CommandLineStrings.COMMAND_XOR_DEMO:
                    new XORDemo().run();
                case CommandLineStrings.COMMAND_QUIT:
                    return;
                default:
                    System.out.println(CommandLineStrings.INVALID_COMMAND_MESSAGE);
            }
        }
    }

    /**
     * Runs the Optical Character Recognition demo.
     * @param args The arguments provided to the command line by the user. This
     *             should include the command as well as the arguments, where the
     *             command is the zeroth value in the string array.
     */
    private static void runOpticalCharcterRecognitionDemo(String[] args) {
        if (args.length != 4) {
            System.out.println(CommandLineStrings.INVALID_COMMAND_MESSAGE);
            return;
        }

        String outputDirectory = args[1];
        String trainingSamplesDirectory = args[2];
        String unseenSamplesDirectory = args[3];

        new OpticalCharacterRecognitionDemo(outputDirectory, trainingSamplesDirectory, unseenSamplesDirectory).run();
    }
}