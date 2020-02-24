package Scenario1.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class CsvProvider {

    private static final String COMMA_DELIMITER = ",";

    public List<String> CsvProvider(String pathToFile) {
        List<String> records = new ArrayList<>();

        try (
                BufferedReader br = new BufferedReader(new FileReader(pathToFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(COMMA_DELIMITER);
                records.addAll(Arrays.asList(values));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return records;
    }

}
