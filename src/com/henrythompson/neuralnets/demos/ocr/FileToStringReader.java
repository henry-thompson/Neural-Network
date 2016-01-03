package com.henrythompson.neuralnets.demos.ocr;

import java.io.*;import java.io.BufferedReader;import java.io.File;import java.io.FileReader;import java.io.IOException;import java.io.Reader;import java.lang.String;import java.lang.StringBuilder;import java.lang.System;

/**
 * Takes a file and reads the data contained inside it as a string
 */
public class FileToStringReader {
    private final File mFile;

    public FileToStringReader(File file) {
        mFile = file;
    }

    public String readContents() {
        BufferedReader br = null;

        try {
            br = new BufferedReader(new FileReader(mFile));
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }

            return sb.toString();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeReader(br);
        }

        return null;
    }

    private void closeReader(Reader reader) {
        try {
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
