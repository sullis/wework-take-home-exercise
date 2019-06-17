package io.github.sullis.httpclient.example;

import com.google.common.collect.ImmutableList;
import io.atlassian.fugue.Either;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.Iterator;

public class FileParserTest extends AbstractTest {
    private File twelveUrls = load("twelve-urls.txt");
    private File noHeader = load("no-header.txt");
    private File blankLines = load("blank-lines.txt");
    private File oneBadLine = load("one-bad-line.txt");

    @Test
    public void fileWithTwelveUrls() throws Exception {
        checkFile(twelveUrls, 13, 12, 1);
    }

    @Test
    public void fileWithNoHeader() throws Exception {
        checkFile(noHeader, 12, 12, 0);
    }

    @Test
    public void fileWithOneBadLine() throws Exception {
        ImmutableList<URL> urls = checkFile(oneBadLine, 4, 2, 1);
        assertEquals(
                "[https://facebook.com/, https://google.com/]",
                urls.toString());
    }

    @Test
    public void fileWithBlankLines() throws Exception {
        checkFile(blankLines, 0, 0, 0);
    }


    private ImmutableList<URL> checkFile(File file, int expectedTotal, int expectedUrlCount, int expectedHeaderCount) throws Exception {
        ImmutableList.Builder urlListBuilder = ImmutableList.builder();
        try (FileInputStream input = new FileInputStream(file)) {
            Iterator<Either<IgnoredLine, URL>> iter = new FileParser().parse(input, CharSetUtil.DEFAULT_CHARSET).iterator();
            long urlCount = 0;
            long headerCount = 0;
            long total = 0;
            while (iter.hasNext()) {
                Either<IgnoredLine, URL> item = iter.next();
                total++;
                if (item.isRight()) {
                    urlCount++;
                    urlListBuilder.add(item.right().get());
                }
                if (item.isLeft() && (item.left().get().isHeader())) {
                    headerCount++;
                }
            }
            assertEquals(expectedTotal, total);
            assertEquals(expectedHeaderCount, headerCount);
            assertEquals(expectedUrlCount, urlCount);
            assertFalse(iter.hasNext());
        }
        return urlListBuilder.build();
    }

    @Test
    public void parseIteratorForEachRemaining() throws Exception {
        ImmutableList.Builder urlListBuilder = ImmutableList.builder();
        try (FileInputStream input = new FileInputStream(twelveUrls)) {
            Iterator<Either<IgnoredLine, URL>> iter = new FileParser().parse(input, CharSetUtil.DEFAULT_CHARSET).iterator();
            iter.forEachRemaining(line -> {
                if (line.isRight()) {
                    urlListBuilder.add(line.right().get());
                }
            });
            assertFalse(iter.hasNext());
        }
        assertEquals(12, urlListBuilder.build().size());
    }

    static private File load(String filename) {
        File f = new File("./src/test/resources/" + filename);
        assertTrue(f.exists());
        assertFalse(f.isDirectory());
        return f;
    }
}
