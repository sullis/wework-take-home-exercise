package io.github.sullis.httpclient.example;

import com.google.common.collect.ImmutableList;
import com.google.common.io.Files;
import org.junit.jupiter.api.Test;

import static io.github.sullis.httpclient.example.RegexUtil.DEFAULT_PATTERN;
import static org.junit.jupiter.api.Assertions.*;
import static io.github.sullis.httpclient.example.TestUtil.URL_LIST;
import java.io.File;
import java.io.StringWriter;
import java.net.http.HttpClient;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;


public class UrlProcessorTest extends AbstractTest {
    @Test
    public void maxConcurrentHttpRequests1() throws Exception {
      doTest(1);
    }

    @Test
    public void maxConcurrentHttpRequests2() throws Exception {
        doTest(2);
    }

    @Test
    public void maxConcurrentHttpRequests99() throws Exception {
        doTest(99);
    }

    private void doTest(final int maxConcurrentHttpRequests) throws Exception {
        final String outputFilename = this.getClass().getSimpleName() + "-" + UUID.randomUUID().toString() + ".txt";
        final File outputFile = new File(outputFilename);
        try {
            HttpClient client = HttpClientUtil.build();
            OutputFileWriter outputFileWriter = new OutputFileWriter(outputFilename);
            StringWriter listenerWriter = new StringWriter();
            UrlProcessorListener listener = new UrlProcessorListenerImpl(listenerWriter);
            ResponseBodyProcessor responseBodyProcessor = new ResponseBodyProcessorImpl(outputFileWriter, DEFAULT_PATTERN);
            UrlProcessor processor = new UrlProcessor(client,
                    Optional.of(listener),
                    URL_LIST.stream(),
                    maxConcurrentHttpRequests,
                    responseBodyProcessor);
            CompletableFuture<UrlProcessorResult> future = processor.execute();
            UrlProcessorResult result = future.get();
            assertEquals(7, result.processedCount());
            assertEquals(1, result.failureCount());
            outputFileWriter.close();
            assertTrue(outputFile.exists());
            assertTrue(listenerWriter.toString().contains("Processing URL: " + URL_LIST.get(0)));
            assertTrue(listenerWriter.toString().contains("Processing URL: " + URL_LIST.get(URL_LIST.size() - 1)));
            ImmutableList<String> outputFileLines = Files.asCharSource(outputFile, CharSetUtil.DEFAULT_CHARSET).readLines();
            assertEquals(7, outputFileLines.size(), "Lines:" + outputFileLines);
        } finally {
            outputFile.delete();
        }

    }
}
