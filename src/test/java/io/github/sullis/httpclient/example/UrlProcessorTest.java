package io.github.sullis.httpclient.example;

import com.google.common.collect.ImmutableList;
import com.google.common.io.Files;
import org.junit.jupiter.api.Test;

import static io.github.sullis.httpclient.example.RegexUtil.DEFAULT_PATTERN;
import static io.github.sullis.httpclient.example.TestUtil.ASIA_URL_LIST;
import static org.junit.jupiter.api.Assertions.*;
import static io.github.sullis.httpclient.example.TestUtil.URL_LIST;
import java.io.File;
import java.io.StringWriter;
import java.net.URL;
import java.net.http.HttpClient;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;


public class UrlProcessorTest extends AbstractTest {
    @Test
    public void asiaUrls() throws Exception {
        // You might be wondering 'why does this test exist?'
        // Short Answer:
        // Accessing China websites and Japan websites can be slower
        // than websites in North America
        doTest(ASIA_URL_LIST,1);
    }

    @Test
    public void maxConcurrentHttpRequests1() throws Exception {
      doTest(URL_LIST,1);
    }

    @Test
    public void maxConcurrentHttpRequests2() throws Exception {
        doTest(URL_LIST,2);
    }

    @Test
    public void maxConcurrentHttpRequests99() throws Exception {
        doTest(URL_LIST,99);
    }

    private void doTest(ImmutableList<URL> urls, final int maxConcurrentHttpRequests) throws Exception {
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
                    urls.stream(),
                    maxConcurrentHttpRequests,
                    responseBodyProcessor);
            CompletableFuture<UrlProcessorResult> future = processor.execute();
            UrlProcessorResult result = future.get();
            assertTrue(result.processedCount() > 0);
            outputFileWriter.close();
            assertTrue(outputFile.exists());
            assertTrue(listenerWriter.toString().contains("Processing URL: " + urls.get(0)));
            assertTrue(listenerWriter.toString().contains("Processing URL: " + urls.get(urls.size() - 1)));
            ImmutableList<String> outputFileLines = Files.asCharSource(outputFile, CharSetUtil.DEFAULT_CHARSET).readLines();
            assertTrue(outputFileLines.size() > 0);
        } finally {
            outputFile.delete();
        }

    }
}
