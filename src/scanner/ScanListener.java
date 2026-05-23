package scanner;

public interface ScanListener {
    void onLog(String message);
    void onProgressUpdate(int indexed, int skipped, int failed, int folders, int loops, int images, long bytes, double duration);
}