package com.henrythompson.neuralnets.demos.ocr;

/**
 * Indicates that a file containing an OCR network does not have a filename
 * conforming to the format expected.
 */
public class InvalidFilenameException extends Throwable {
    public InvalidFilenameException(String message) {
        super(message);
    }
}
