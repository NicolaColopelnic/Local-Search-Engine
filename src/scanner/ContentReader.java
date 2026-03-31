package scanner;

import java.io.File;
import java.nio.file.Files;

// extract the data of a file before sending to indexer

public class ContentReader {
    public String readAll(File file) {
        try {
            return Files.readString(file.toPath());
        } catch (Exception e) {
            System.err.println("Error reading " + file.getName() + ": " + e.getMessage());
            return "";
        }
    }
}