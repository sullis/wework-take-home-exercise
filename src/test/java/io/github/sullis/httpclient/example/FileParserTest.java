package io.github.sullis.httpclient.example;

import com.google.common.collect.ImmutableList;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.net.URL;

public class FileParserTest {
    private File twelveUrls = new File("./src/test/resources/twelve-urls.txt");

    @Test
    public void happyPath() throws Exception {
        FileParser.FileParseResult result = new FileParser().parse(twelveUrls);
        assertEquals(0, result.getProblems().size());
        ImmutableList<URL> urls = result.getUrls();
        assertEquals(12, urls.size());
        assertEquals("[https://facebook.com/, https://twitter.com/, https://google.com/, https://youtube.com/, https://wordpress.org/, https://adobe.com/, https://blogspot.com/, https://wikipedia.org/, https://linkedin.com/, https://wordpress.com/, https://yahoo.com/, https://amazon.com/]",
                urls.toString());
    }
}
