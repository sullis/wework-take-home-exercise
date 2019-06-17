package io.github.sullis.httpclient.example;

import org.junit.jupiter.api.Test;

import static io.github.sullis.httpclient.example.TestUtil.makeRandomUrl;
import static org.junit.jupiter.api.Assertions.*;

import com.google.common.collect.ImmutableList;
import com.google.common.io.Files;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.UUID;

public class OutputFileWriterTest extends AbstractTest {
    @Test
    public void writeThenOverwrite() throws Exception {
        final URL aaa = makeRandomUrl();
        final URL bbb = makeRandomUrl();
        final URL ccc = makeRandomUrl();
        final URL ddd = makeRandomUrl();
        final String filename = this.getClass().getSimpleName() + "-" + UUID.randomUUID().toString() + ".txt";
        final File file = new File(filename);
        try {
            assertFalse(file.exists());
            saveFileAndVerify(filename, true, aaa, bbb);
            assertTrue(file.exists());

            // overwrite the file with new content
            saveFileAndVerify(filename, false, ccc, ddd);
            assertTrue(file.exists());

        } finally {
            file.delete();
        }
    }

    private static void saveFileAndVerify(String filename, boolean urlSuccess, URL... urls) throws IOException {
        final File file = new File(filename);
        final OutputFileWriter writer = new OutputFileWriter(filename);
        for (URL u : urls) {
            writer.writeLine(u, urlSuccess);
        }
        writer.close();
        assertTrue(file.exists());
        ImmutableList<String> fileContent = Files.asCharSource(file, CharSetUtil.DEFAULT_CHARSET).readLines();
        assertEquals(urls.length, fileContent.size());
        for (int i = 0; i < urls.length; i++) {
            String expected = "\"" + urls[i] + "\"," + urlSuccess;
            assertEquals(expected, fileContent.get(i));
        }

    }
}
