package database;

// the container that carries information about a file from the Scanner and Indexer to the Database
// it holds the location (path), physical stats (size, lastModified), the fingerprint (checksum), the actual text (content) of a file

public record FileDocument(String path, String fileName, long lastModified, long size, String content,
                           String checksum) {
}