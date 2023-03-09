package com.java.test.junior.load.service;

import com.java.test.junior.load.exception.GenericLoaderException;
import com.java.test.junior.load.exception.GenericReaderException;
import com.java.test.junior.load.exception.GenericWriterException;
import com.java.test.junior.load.util.CsvFileReader;
import com.java.test.junior.load.util.CsvFileWriter;
import com.zaxxer.hikari.HikariDataSource;
import org.postgresql.PGConnection;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.io.*;
import java.sql.SQLException;
import java.time.LocalDateTime;

/**
 * @author artiom.spac
 * @version java-test-junior
 * @apiNote 09.03.2023
 */
@Service
public class ProductLoaderService implements EntityLoader {

    private final DataSource dataSource;

    public ProductLoaderService(HikariDataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * @param path file path to be imported
     * @param userId id of the products owner
     */
    @Override
    public void load(String path, Long userId) {
        try {
            String string = "," + userId + "," + LocalDateTime.now() + "," + LocalDateTime.now();
            String headers = ",user_id,created_at,updated_at";
            BufferedReader br = CsvFileReader.read(path);
            String fileName = CsvFileWriter.writeToFile(br, string, headers);
            PGConnection conn = dataSource.getConnection().unwrap(PGConnection.class);
            conn.getCopyAPI()
                    .copyIn(
                            "COPY product(name, price, description, user_id, created_at, updated_at) FROM STDIN (FORMAT csv, HEADER)",
                            new BufferedReader(new FileReader(fileName)));
            File file = new File(fileName);
            if (file.exists()) {
                file.delete();
            }
        } catch (SQLException e) {
            throw new GenericLoaderException("Provided file contains malformed data");
        } catch (FileNotFoundException e) {
            throw new GenericLoaderException("Provided file not found");
        } catch (IOException e) {
            throw new GenericLoaderException("Failed to read provided file");
        } catch (GenericReaderException | GenericWriterException e) {
            throw new GenericLoaderException(e.getMessage());
        }
    }
}
