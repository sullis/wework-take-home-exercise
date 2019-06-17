package io.github.sullis.httpclient.example;

import com.google.common.collect.ImmutableList;
import com.google.common.io.CharSource;
import com.google.common.io.Files;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class OutputFileWriterTest extends AbstractTest {
    @Test
    public void writeThenOverwrite() throws Exception {
        final String aaa = "AAA-" + UUID.randomUUID();
        final String bbb = "BBB-" + UUID.randomUUID();
        final String ccc = "CCC-" + UUID.randomUUID();
        final String ddd = "DDD-" + UUID.randomUUID();
        final String filename = this.getClass().getSimpleName() + "-" + UUID.randomUUID().toString() + ".txt";
        final File file = new File(filename);
        try {
            assertFalse(file.exists());
            saveFileAndVerify(filename, aaa, bbb);
            assertTrue(file.exists());

            // overwrite the file with new content
            saveFileAndVerify(filename, ccc, ddd);
            assertTrue(file.exists());

        } finally {
            file.delete();
        }
    }

    private static void saveFileAndVerify(String filename, String... lines) throws IOException {
        final File file = new File(filename);
        final OutputFileWriter writer = new OutputFileWriter(filename);
        for (String line : lines) {
            writer.writeLine(line);
        }
        writer.close();
        assertTrue(file.exists());
        ImmutableList<String> fileContent = Files.asCharSource(file, CharSetUtil.DEFAULT_CHARSET).readLines();
        assertEquals(lines.length, fileContent.size());
        for (int i = 0; i < lines.length; i++) {
            assertEquals(lines[i], fileContent.get(i));
        }

    }
}
