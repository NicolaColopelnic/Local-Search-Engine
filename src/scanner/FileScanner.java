package scanner;

import database.DatabaseManager;
import java.io.File;
import java.nio.file.Files;

public class FileScanner {
    private DatabaseManager db = new DatabaseManager();

    // recursive walk through directory tree
    public void walk(File folder) {
        File[] list = folder.listFiles();
        if (list == null) return;

        for (File file : list) {
            if (file.isDirectory()) {
                walk(file); // recursive call for subdirectories
            } else if (file.getName().endsWith(".txt")) {
                try {
                    String content = Files.readString(file.toPath());
                    db.save(file.getAbsolutePath(), file.getName(), content);
                    System.out.println("Indexed: " + file.getName());
                } catch (Exception e) { System.err.println("Failed: " + file.getName()); }
            }
        }
    }
}