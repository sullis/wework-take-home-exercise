package io.github.sullis.httpclient.example;

import com.google.common.collect.ImmutableList;

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

    static public final ImmutableList<URL> URL_LIST = ImmutableList.of(
            makeUrl("https://google.com"),
            makeUrl("https://twitter.com"),
            makeUrl("https://microsoft.com"),
            makeUrl("https://www.cdc.gov"),
            makeUrl("https://npr.org"),
            makeUrl("https://craigslist.org"),
            makeUrl("https://nytimes.com")
    );

    static public URL makeUrl(String s) {
        try {
            return new URL(s);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
    static public URL makeRandomUrl() {
        return makeUrl("https://" + UUID.randomUUID() + ".com");
    }

    static private File load(String filename) {
        File f = new File("./src/test/resources/" + filename);
        assertTrue(f.exists());
        assertFalse(f.isDirectory());
        return f;
    }
}
