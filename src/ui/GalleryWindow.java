package ui;

import search.SearchResult;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class GalleryWindow extends JDialog {
    public GalleryWindow(List<SearchResult> results) {
        setTitle("Image Gallery View");
        setSize(800, 600);
        setLocationRelativeTo(null);

        JPanel grid = new JPanel(new GridLayout(0, 3, 10, 10));
        grid.setBackground(Color.DARK_GRAY);

        for (SearchResult r : results) {
            // only add images to the gallery
            if (r.dominantColor() != null && !r.dominantColor().isEmpty()) {
                grid.add(createGalleryItem(r));
            }
        }

        add(new JScrollPane(grid));
        setVisible(true);
    }

    private JPanel createGalleryItem(SearchResult r) {
        JPanel item = new JPanel(new BorderLayout());
        item.setBackground(Color.LIGHT_GRAY);

        ImageIcon icon = new ImageIcon(r.path());
        Image img = icon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
        item.add(new JLabel(new ImageIcon(img)), BorderLayout.CENTER);

        JLabel label = new JLabel(r.fileName(), SwingConstants.CENTER);
        item.add(label, BorderLayout.SOUTH);

        return item;
    }
}