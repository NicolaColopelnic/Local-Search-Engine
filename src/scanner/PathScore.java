package scanner;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class PathScore {
    private static final Map<String, Double> extensions = new HashMap<>();

    static {
        extensions.put(".java", 40.0);
        extensions.put(".md", 20.0);
        extensions.put(".txt", 10.0);
        extensions.put(".jpg", 15.0);
        extensions.put(".png", 15.0);
        extensions.put(".xml", 5.0);
    }

    public double calculateScore(File file) {
        double score = 0.0;
        String path = file.getAbsolutePath().toLowerCase();
        String fileName = file.getName().toLowerCase();

        if(path.contains("\\src\\") || path.contains("/src/")) {
            score += 50.0;
        }

        int lastDot = fileName.lastIndexOf(".");
        if (lastDot != -1) {
            String extension = fileName.substring(lastDot);
            score += extensions.getOrDefault(extension, 0.0);
        }

        long depth = path.chars().filter(ch -> ch == '\\' || ch == '/').count();
        score -= (depth * 10.0);

        long sizeKB = file.length() / 1024;
        if (sizeKB > 0 && sizeKB < 10) {
            score += 10.0;
        }

        return score;
    }
}
