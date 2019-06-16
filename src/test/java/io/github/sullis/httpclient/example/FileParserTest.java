package io.github.sullis.httpclient.example;

import io.atlassian.fugue.Either;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.Iterator;

public class FileParserTest {
    private File twelveUrls = new File("./src/test/resources/twelve-urls.txt");

    @Test
    public void happyPath() throws Exception {
        try (FileInputStream input = new FileInputStream(twelveUrls)) {
            Iterator<Either<LineStatus, URL>> iter = new FileParser().parse(input, CharSetUtil.DEFAULT_CHARSET);
            long urlCount = 0;
            long headerCount = 0;
            long total = 0;
            while (iter.hasNext()) {
                Either<LineStatus, URL> item = iter.next();
                total++;
                if (item.isRight()) {
                    urlCount++;
                }
                if (item.isLeft() && (item.left().get().isHeader())) {
                    headerCount++;
                }
            }
            assertEquals(13, total);
            assertEquals(1, headerCount);
            assertEquals(12, urlCount);
            assertFalse(iter.hasNext());
        }
    }
}
