package scanner;

import java.nio.file.*;
import java.security.MessageDigest;
import java.util.HexFormat;

public class CheckSum {
    public static String calculateChecksum(Path path) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] fileBytes = Files.readAllBytes(path);
        byte[] encodedHash = digest.digest(fileBytes);
        return HexFormat.of().formatHex(encodedHash);
    }
}