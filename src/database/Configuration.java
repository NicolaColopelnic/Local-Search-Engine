package database;

import java.io.FileInputStream;
import java.util.Properties;

public class Configuration {
    private final Properties props = new Properties();

    public Configuration() {
        try (FileInputStream f = new FileInputStream("config.txt")) {
            props.load(f);
        } catch (Exception e) {
            System.err.println("Could not find config.txt.");
        }
    }

    public String get(String key, String defaultValue) {
        return props.getProperty(key, defaultValue);
    }
}