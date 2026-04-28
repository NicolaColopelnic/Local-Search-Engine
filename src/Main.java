import database.Configuration;
import database.DatabaseManager;
import database.FileRepository;
import scanner.FileScanner;
import scanner.Indexer;
import search.*;
import search.LastModifiedRanking;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException, SQLException {
        Configuration config = new Configuration();

        String path = config.get("rootDirectory", "C:\\");
        String query = config.get("searchTerm", "Java");

        DatabaseManager.initialize();
        FileRepository repository = new FileRepository();
        Indexer indexer = new Indexer(repository);
        FileScanner scanner = new FileScanner(indexer, config);

        long startTime = System.currentTimeMillis();

        System.out.println();
        scanner.scanDirectory(path);

        long endTime = System.currentTimeMillis();
        double durationSeconds = (endTime - startTime) / 1000.0;

        System.out.println();
        System.out.println("SCAN REPORT");
        System.out.println("Execution Time: " + durationSeconds + " seconds");
        System.out.println("Folders Traversed: " + scanner.getFoldersScanned());
        System.out.println("Loops Detected: " + scanner.getLoopsDetected());
        System.out.println("Files Indexed: " + indexer.getFilesIndexed());
        System.out.println("Files Skipped: " + indexer.getFilesSkipped());
        System.out.println("Files Failed: " + indexer.getFilesFailed());

        long totalBytes = indexer.getTotalBytesIndexed();
        if (totalBytes < 1024 * 1024) {
            double kbIndexed = totalBytes / 1024.0;
            System.out.printf("Total Data Indexed: %.2f KB\n", kbIndexed);
        } else {
            double mbIndexed = totalBytes / (1024.0 * 1024.0);
            System.out.printf("Total Data Indexed: %.2f MB\n", mbIndexed);
        }
        System.out.println();

        SearchManager searcher = new SearchManager();
        Scanner inputScanner = new Scanner(System.in);

        while(true) {
            System.out.println("Enter search query: ");
            String searchQuery = inputScanner.nextLine();
            if(searchQuery.equals("exit")) {
                System.out.println("Program closed.");
                break;
            }
            searcher.setRankingStrategy(new ScoreRanking());
            searcher.search(searchQuery);

            searcher.setRankingStrategy(new AlphabeticRanking());
            searcher.search(searchQuery);

            searcher.setRankingStrategy(new LastModifiedRanking());
            searcher.search(searchQuery);

            searcher.setRankingStrategy(new LastAccessedRanking());
            searcher.search(searchQuery);
        }

    }
}
