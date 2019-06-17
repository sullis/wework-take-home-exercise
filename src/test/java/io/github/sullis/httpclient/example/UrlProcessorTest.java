package io.github.sullis.httpclient.example;

import com.google.common.collect.ImmutableList;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.StringWriter;
import java.net.URL;
import java.net.http.HttpClient;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class UrlProcessorTest extends AbstractTest {
    @Test
    public void happyPath() throws Exception {
        ImmutableList<URL> urls = ImmutableList.of(
                new URL("https://google.com"),
                new URL("https://twitter.com"));
        StringWriter writer = new StringWriter();
        HttpClient client = HttpClientUtil.build();
        UrlProcessorListener listener = new UrlProcessorListenerImpl(writer);
        UrlProcessor processor = new UrlProcessor(client,
                Optional.of(listener),
                urls.stream(),
                20);
        CompletableFuture<UrlProcessorResult> future = processor.execute();
        UrlProcessorResult result = future.get();
        assertEquals(2, result.successCount());
        assertEquals(0, result.failureCount());
        assertEquals("Processing URL: https://google.com\nProcessing URL: https://twitter.com\n",
                writer.toString());
    }
}
