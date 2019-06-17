package io.github.sullis.httpclient.example;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestUtil {
    static public final File twelveUrls = load("twelve-urls.txt");
    static public final File noHeader = load("no-header.txt");
    static public final File blankLines = load("blank-lines.txt");
    static public final File oneBadLine = load("one-bad-line.txt");

    static public URL makeRandomUrl() {
        try {
            return new URL("https://" + UUID.randomUUID() + ".com");
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    static private File load(String filename) {
        File f = new File("./src/test/resources/" + filename);
        assertTrue(f.exists());
        assertFalse(f.isDirectory());
        return f;
    }
}
