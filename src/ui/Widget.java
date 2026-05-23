package ui;

import search.SearchResult;
import javax.swing.*;
import java.util.List;

public interface Widget {
    boolean activate(String query, List<SearchResult> results);
    JButton getComponent(List<SearchResult> results);
}
