package io.github.sullis.httpclient.example;

import com.google.common.collect.ImmutableList;
import com.google.common.io.Files;
import org.junit.jupiter.api.Test;

import static io.github.sullis.httpclient.example.RegexUtil.DEFAULT_PATTERN;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.StringWriter;
import java.net.URL;
import java.net.http.HttpClient;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class UrlProcessorTest extends AbstractTest {
    @Test
    public void happyPath() throws Exception {
        final String outputFilename = this.getClass().getSimpleName() + "-" + UUID.randomUUID().toString() + ".txt";
        final File outputFile = new File(outputFilename);
        ImmutableList<URL> urls = ImmutableList.of(
                new URL("https://google.com"),
                new URL("https://twitter.com"));
        try {
            HttpClient client = HttpClientUtil.build();
            OutputFileWriter outputFileWriter = new OutputFileWriter(outputFilename);
            StringWriter listenerWriter = new StringWriter();
            UrlProcessorListener listener = new UrlProcessorListenerImpl(listenerWriter);
            ResponseBodyProcessor responseBodyProcessor = new ResponseBodyProcessorImpl(outputFileWriter, DEFAULT_PATTERN);
            UrlProcessor processor = new UrlProcessor(client,
                    Optional.of(listener),
                    urls.stream(),
                    20,
                    responseBodyProcessor);
            CompletableFuture<UrlProcessorResult> future = processor.execute();
            UrlProcessorResult result = future.get();
            assertEquals(2, result.processedCount());
            assertEquals(0, result.failureCount());
            assertEquals("Processing URL: https://google.com\nProcessing URL: https://twitter.com\n",
                    listenerWriter.toString());
            assertTrue(outputFile.exists());
            ImmutableList<String> lines = Files.asCharSource(outputFile, CharSetUtil.DEFAULT_CHARSET).readLines();
            assertEquals(urls.size(), lines.size());
            assertEquals("\"https://google.com\",true", lines.get(0));
            assertEquals("\"https://twitter.com\",true", lines.get(1));
        } finally {
            outputFile.delete();
        }

    }
}
