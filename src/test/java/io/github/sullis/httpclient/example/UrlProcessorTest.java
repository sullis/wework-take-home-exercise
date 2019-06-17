package io.github.sullis.httpclient.example;

import com.google.common.collect.ImmutableList;
import org.junit.jupiter.api.Test;
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
        ImmutableList<URL> urls = ImmutableList.of(
                new URL("https://google.com"),
                new URL("https://twitter.com"));
        try {
            HttpClient client = HttpClientUtil.build();
            OutputFileWriter outputFileWriter = new OutputFileWriter(outputFilename);
            StringWriter listenerWriter = new StringWriter();
            UrlProcessorListener listener = new UrlProcessorListenerImpl(listenerWriter);
            UrlProcessor processor = new UrlProcessor(client,
                    Optional.of(listener),
                    urls.stream(),
                    20,
                    outputFileWriter);
            CompletableFuture<UrlProcessorResult> future = processor.execute();
            UrlProcessorResult result = future.get();
            assertEquals(2, result.successCount());
            assertEquals(0, result.failureCount());
            assertEquals("Processing URL: https://google.com\nProcessing URL: https://twitter.com\n",
                    listenerWriter.toString());
        } finally {
            File f = new File(outputFilename);
            f.delete();
        }

    }
}
