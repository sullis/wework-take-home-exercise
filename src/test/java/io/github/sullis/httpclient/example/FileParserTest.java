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

    @Test
    public void happyPath() throws Exception {
        FileInputStream input = new FileInputStream(twelveUrls);
        Iterator<Either<LineParseProblem, URL>> iter = new FileParser().parse(input, CharSetUtil.DEFAULT_CHARSET);
        long urlCount = 0;
        long total = 0;
        while (iter.hasNext()) {
          Either<LineParseProblem, URL> item = iter.next();
          total++;
          if (item.isRight()) {
              urlCount++;
          }
        }
        assertEquals(total, urlCount);
        assertEquals(12, urlCount);
        assertFalse(iter.hasNext());
        input.close();
    }
}
