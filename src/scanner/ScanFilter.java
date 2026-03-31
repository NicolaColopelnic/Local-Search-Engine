package scanner;

import java.io.File;

public class ScanFilter {
    private final String[] ignoredFolders;
    private final String[] allowedExtensions;

    public ScanFilter() {
        this.ignoredFolders = new String[]{".git", ".idea", "target", "loop_trap"};
        this.allowedExtensions = new String[]{".txt", ".md", ".java", ".xml"};
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