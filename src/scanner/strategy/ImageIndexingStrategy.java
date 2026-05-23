package scanner.strategy;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ImageIndexingStrategy implements IndexingStrategy {
    @Override
    public String extractData(File file) {
        try {
            BufferedImage img = ImageIO.read(file);
            if(img == null) return "unknown";

            // counters for color buckets
            Map<String, Integer> colorCounts = new HashMap<>();
            colorCounts.put("red", 0);
            colorCounts.put("green", 0);
            colorCounts.put("blue", 0);
            colorCounts.put("yellow", 0);
            colorCounts.put("white", 0);
            colorCounts.put("black", 0);

            for(int y = 0; y < img.getHeight(); y+=5) {
                for(int x = 0; x < img.getWidth(); x+=5) {
                    int rgb = img.getRGB(x, y);
                    Color color = new Color(rgb);

                    String category = getColor(color);
                    colorCounts.put(category, colorCounts.getOrDefault(category, 0) + 1);
                }
            }
            String dominantColor = "unknown";
            int max = -1;

            for(Map.Entry<String, Integer> entry : colorCounts.entrySet()) {
                if(entry.getValue() > max) {
                    max = entry.getValue();
                    dominantColor = entry.getKey();
                }
            }
            return dominantColor;
        } catch(Exception e) {
            return "unknown";
        }
    }

    private String getColor(Color color) {
        int r = color.getRed();
        int g = color.getGreen();
        int b = color.getBlue();

        if (r > 240 && g > 240 && b > 240) return "white";
        if (r < 30 && g < 30 && b < 30) return "black";
        if (r > 150 && g > 150 && b < 100) return "yellow";
        if (r > g && r > b) return "red";
        if (g > r && g > b) return "green";
        if (b > r && b > g) return "blue";

        return "gray";
    }
}
