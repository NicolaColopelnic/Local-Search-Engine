package ui;

import search.SearchResult;
import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class WidgetFactory {
    private static List<Widget> widgets;
    static {
        widgets = new ArrayList<>();
        widgets.add(new GalleryWidget());
        widgets.add(new CodeWidget());
    }

    public static List<JButton> getWidgets(String query, List<SearchResult> results) {
        List<JButton> widgets = new ArrayList<>();
        for(Widget w: WidgetFactory.widgets) {
            if(w.activate(query, results)) {
                widgets.add(w.getComponent(results));
            }
        }
        return widgets;
    }
}
