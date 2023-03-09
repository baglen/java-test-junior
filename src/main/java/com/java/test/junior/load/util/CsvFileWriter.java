package com.java.test.junior.load.util;

import com.java.test.junior.load.exception.GenericWriterException;

import java.io.*;
import java.time.LocalDateTime;

/**
 * @author artiom.spac
 * @version java-test-junior
 * @apiNote 09.03.2023
 */
public class CsvFileWriter {

    public static String writeToFile(BufferedReader br, String string, String header) throws GenericWriterException {
        if(br == null){
            throw new RuntimeException("Provided reader is null");
        }
        BufferedWriter bw;
        String homeDir = System.getProperty("user.home");
        File outputFile = new File(homeDir,"temp" + LocalDateTime.now() + ".csv");
        try {
            bw = new BufferedWriter(new FileWriter(outputFile));
            String line = "";
            int lineCounter = 0;
            while ((line = br.readLine()) != null) {
                lineCounter++;
                if(lineCounter == 1){
                    line = line + header;
                } else {
                    line = line + string;
                }
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) {
            throw new GenericWriterException("Failed to write temp file");
        }
        try {
            br.close();
            bw.close();
        } catch (IOException ex) {
            throw new GenericWriterException("Failed to close write file");
        }
        return outputFile.getAbsolutePath();
    }
}

