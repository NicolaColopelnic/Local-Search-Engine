import database.Configuration;
import database.DatabaseManager;
import database.FileRepository;
import scanner.FileScanner;
import scanner.Indexer;
import search.*;
import ui.SearchUI;

import java.io.IOException;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws IOException, SQLException {
        Configuration config = new Configuration();
        DatabaseManager.initialize();
        FileRepository repository = new FileRepository();
        Indexer indexer = new Indexer(repository);
        FileScanner scanner = new FileScanner(indexer, config);
        SearchManager searcher = new SearchManager();
        HistoryManager historyManager = new HistoryManager();
        searcher.addObserver(historyManager);

        SearchUI ui = new SearchUI(searcher, historyManager);
        indexer.setListener(ui);
        long startTime = System.currentTimeMillis();

        scanner.scanDirectory(config.get("rootDirectory", "D:/SD-project"));

        long endTime = System.currentTimeMillis();
        double durationSeconds = (endTime - startTime) / 1000.0;

        ui.onProgressUpdate(
                indexer.getFilesIndexed(), indexer.getFilesSkipped(),
                indexer.getFilesFailed(), scanner.getFoldersScanned(),
                scanner.getLoopsDetected(), indexer.getImagesIndexed(),
                indexer.getTotalBytesIndexed(), durationSeconds
        );

        ui.setVisible(true);

    }
}
