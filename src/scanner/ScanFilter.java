package scanner;

import database.Configuration;
import java.io.File;

public class ScanFilter {
    private final String[] ignoredFolders;
    private final String[] allowedExtensions;

    public ScanFilter(Configuration config) {
        // read the list from config.txt
        String rawFolders = config.get("ignoreFolders", ".git,target"); // default if the file is missing
        this.ignoredFolders = rawFolders.split(",");

        String rawExtensions = config.get("textExtensions", ".md");
        this.allowedExtensions = rawExtensions.split(",");
    }

    // determine which files to ignore
    public boolean isIgnored(File file) {
        String name = file.getName();

        for (String folder : ignoredFolders) {
            if (name.equals(folder.trim())) return true;
        }

        // defaults
        return name.startsWith(".") || name.endsWith(".db");
    }

    // determines if the file contains searchable text content
    public boolean isTextFile(String name) {
        String lowerName = name.toLowerCase();

        for (String ext : allowedExtensions) {
            if (lowerName.endsWith(ext.trim())) {
                return true;
            }
        }
        return false;
    }
}