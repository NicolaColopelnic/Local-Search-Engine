package ui;

import search.SearchResult;

import javax.swing.*;
import java.util.List;

public class CodeWidget implements Widget {
    @Override
    public boolean activate(String query, List<SearchResult> results) {
        // trigger if > 50% of files end in .java
        long javaCount = results.stream().filter(r -> r.fileName().endsWith(".java")).count();
        if(!results.isEmpty() && (double) javaCount / results.size() > 0.5) {
            return true;
        }
        return false;
    }

    @Override
    public JButton getComponent(List<SearchResult> results) {
        JButton btn = new JButton("Code files analysis");
        btn.addActionListener(e -> {
            int totalFiles = results.size();
            JOptionPane.showMessageDialog(null,
                    "Project Metrics for this Search:\n" +
                            "- Java Source Files: " + totalFiles + "\n" +
                            "- Language: Java 24"
                           );
        });        return btn;
    }
}
