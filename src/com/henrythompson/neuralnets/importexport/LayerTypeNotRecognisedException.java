package com.henrythompson.neuralnets.importexport;

import java.lang.Exception;import java.lang.String; /**
 * Thrown when the string describing the type of the layer in the XML is not recognised
 */
public class LayerTypeNotRecognisedException extends Exception {
    public LayerTypeNotRecognisedException(String message) {
        super(message);
    }
}
