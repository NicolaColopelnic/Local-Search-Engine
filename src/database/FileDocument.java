package database;
public record FileDocument(String path, String fileName, long lastModified, long size, String content) {}