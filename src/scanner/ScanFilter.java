package scanner;
import java.io.File;

public class ScanFilter {
    public boolean isTextFile(String name) {
        String lower = name.toLowerCase();
        return lower.endsWith(".txt") || lower.endsWith(".java") || lower.endsWith(".md");
    }
}