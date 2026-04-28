package ui;

import scanner.*;
import search.*;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class SearchUI extends JFrame implements ScanListener {
    private JTextArea logArea, reportArea, resultsArea;
    private JTextField queryField;
    private JComboBox<String> strategyBox;
    private SearchManager searchManager;

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, HH:mm:ss");

    public SearchUI(SearchManager manager) {
        this.searchManager = manager;
        setTitle("Local Search Engine");
        setSize(900, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JTabbedPane tabs = new JTabbedPane();

        // tabs:
        logArea = new JTextArea();
        logArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        logArea.setEditable(false);
        tabs.addTab("Indexing Logs", new JScrollPane(logArea));

        reportArea = new JTextArea();
        reportArea.setFont(new Font("Monospaced", Font.BOLD, 15));
        reportArea.setEditable(false);
        tabs.addTab("Scan Report", new JScrollPane(reportArea));

        tabs.addTab("Search", createSearchPanel());

        add(tabs);
    }

    private JPanel createSearchPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        JPanel top = new JPanel(new FlowLayout());

        queryField = new JTextField(30);
        strategyBox = new JComboBox<>(new String[]{"Score", "Alphabetical", "Last Modified", "Last Accessed"});
        JButton btn = new JButton("Search");

        top.add(new JLabel("Query:")); top.add(queryField);
        top.add(new JLabel("Sort:")); top.add(strategyBox);
        top.add(btn);

        resultsArea = new JTextArea();
        resultsArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        resultsArea.setEditable(false);
        resultsArea.setMargin(new Insets(10, 10, 10, 10));

        btn.addActionListener(e -> runSearch());
        queryField.addActionListener(e -> runSearch());

        panel.add(top, BorderLayout.NORTH);
        panel.add(new JScrollPane(resultsArea), BorderLayout.CENTER);
        return panel;
    }

    @Override
    public void onLog(String message) {
        logArea.append(message + "\n");
    }

    @Override
    public void onProgressUpdate(int indexed, int skipped, int failed, int folders, int loops, long bytes, double duration) {
        String report = String.format("""
    FINAL SCAN REPORT
    
    Execution Time:      %.3f seconds
    
    Filesystem Navigation:
    - Folders Traversed: %d
    - Loops Detected:    %d
    
    Database Synchronization:
    - Files Indexed:     %d
    - Files Skipped:     %d
    - Files Failed:      %d
    
    Storage Metrics:
    - Total Data Size:   %.2f KB
    """, duration, folders, loops, indexed, skipped, failed, bytes / 1024.0);

        reportArea.setText(report);
    }

    private void runSearch() {
        String selected = (String) strategyBox.getSelectedItem();

        if (selected != null) {
            switch (selected) {
                case "Alphabetical" -> searchManager.setRankingStrategy(new AlphabeticRanking());
                case "Last Modified" -> searchManager.setRankingStrategy(new LastModifiedRanking());
                case "Last Accessed" -> searchManager.setRankingStrategy(new LastAccessedRanking());
                default -> searchManager.setRankingStrategy(new ScoreRanking());
            }
        }

        try {
            List<SearchResult> results = searchManager.search(queryField.getText());
            StringBuilder sb = new StringBuilder();

            if (results.isEmpty()) {
                sb.append("No results found.");
            } else {
                for (SearchResult r : results) {
                    sb.append("FILE: ").append(r.fileName()).append("\n");

                    if (selected.equals("Score")) {
                        sb.append("RANK SCORE: ").append(String.format("%.2f", r.score())).append("\n");
                    } else if (selected.equals("Last Modified")) {
                        String date = dateFormat.format(new Date(r.lastModified()));
                        sb.append("MODIFIED:   ").append(date).append("\n");
                    } else if (selected.equals("Last Accessed")) {
                        String date = dateFormat.format(new Date(r.lastAccessed()));
                        sb.append("ACCESSED:   ").append(date).append("\n");
                    }

                    sb.append("CONTEXT:    ").append(r.preview()).append("\n");
                    sb.append("-".repeat(70)).append("\n\n");
                }
            }
            resultsArea.setText(sb.toString());
            resultsArea.setCaretPosition(0);
        } catch (Exception ex) {
            resultsArea.setText("Search Error: " + ex.getMessage());
        }
    }
}