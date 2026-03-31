import database.DatabaseManager;
import database.FileRepository;
import scanner.FileScanner;
import scanner.Indexer;
import search.SearchManager;

import java.io.File;
import java.sql.*;

public class Main {
    public static void main(String[] args) throws SQLException {
        DatabaseManager db = new DatabaseManager();
        db.initialize();
        FileRepository repository = new FileRepository();
        Indexer indexer = new Indexer(repository);

        FileScanner scanner = new FileScanner();
        scanner.walk(new File("C:\\SD-project"));


        SearchManager searcher = new SearchManager();
        searcher.search("Database");

    }
}