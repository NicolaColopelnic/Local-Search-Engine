package ui;

import search.SearchResult;

import javax.swing.*;
import java.util.List;

public class GalleryWidget implements Widget {
    @Override
    public boolean activate(String query, List<SearchResult> results) {
        // trigger if the query contains "color" or if there is at least one image
        boolean hasImages = results.stream().anyMatch(r -> r.dominantColor() != null && !r.dominantColor().isEmpty());
        if(query.contains("color:") || hasImages) {
            return true;
        }
        return false;
    }

    @Override
    public JButton getComponent(List<SearchResult> results) {
        JButton btn = new JButton("View as gallery");
        btn.addActionListener(e -> new GalleryWindow(results));
        return btn;
    }
}
