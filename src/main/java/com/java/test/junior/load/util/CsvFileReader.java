package com.java.test.junior.load.util;

import com.java.test.junior.load.exception.GenericReaderException;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * @author artiom.spac
 * @version java-test-junior
 * @apiNote 09.03.2023
 */
public class CsvFileReader {

    public static BufferedReader read(String path) throws GenericReaderException{
        BufferedReader reader;
        try {
            if (path.startsWith("http")) {
                URL url = new URL(path);
                URLConnection conn = url.openConnection();
                reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            } else {
                reader = new BufferedReader(new InputStreamReader(new FileInputStream(path)));
            }
        } catch (MalformedURLException e) {
            throw new GenericReaderException("Provided url is malformed");
        } catch (FileNotFoundException e) {
            throw new GenericReaderException("Provided file not found");
        } catch (IOException e) {
            throw new GenericReaderException("Could not read provided file");
        }
        return reader;
    }
}
