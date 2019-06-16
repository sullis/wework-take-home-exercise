package io.github.sullis.httpclient.example;

import com.google.common.collect.ImmutableList;
import io.atlassian.fugue.Either;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.Iterator;

public class FileParserTest {
    private File twelveUrls = new File("./src/test/resources/twelve-urls.txt");
    private File blankLines = new File("./src/test/resources/blank-lines.txt");

    @Test
    public void fileWithTwelveUrls() throws Exception {
        checkFile(twelveUrls, 13, 12, 1);
    }

    @Test
    public void fileWithBlankLines() throws Exception {
        checkFile(blankLines, 0, 0, 0);
    }


    private void checkFile(File file, int expectedTotal, int expectedUrlCount, int expectedHeaderCount) throws Exception {
        try (FileInputStream input = new FileInputStream(file)) {
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
            assertEquals(expectedTotal, total);
            assertEquals(expectedHeaderCount, headerCount);
            assertEquals(expectedUrlCount, urlCount);
            assertFalse(iter.hasNext());
        }
    }

    @Test
    public void parseIteratorForEachRemaining() throws Exception {
        ImmutableList.Builder urlListBuilder = ImmutableList.builder();
        try (FileInputStream input = new FileInputStream(twelveUrls)) {
            Iterator<Either<LineStatus, URL>> iter = new FileParser().parse(input, CharSetUtil.DEFAULT_CHARSET);
            iter.forEachRemaining(line -> {
                if (line.isRight()) {
                    urlListBuilder.add(line.right().get());
                }
            });
            assertFalse(iter.hasNext());
        }
        assertEquals(12, urlListBuilder.build().size());
    }

}
