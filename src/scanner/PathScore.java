package scanner;

import java.io.File;

public class PathScore {
    public double calculateScore(File file) {
        double score = 0.0;
        String path = file.getAbsolutePath().toLowerCase();

        if(path.contains("\\src\\") || path.contains("/src/")) {
            score += 50.0;
        }

        if(path.endsWith(".java")) {
            score += 30.0;
        } else if(path.endsWith(".md")) {
            score += 10.0;
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
