package io.github.sullis.httpclient.example;

import com.google.common.collect.ImmutableList;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.StringWriter;
import java.net.URL;
import java.net.http.HttpClient;
import java.util.Optional;

public class TaskProcessorTest {
    @Test
    public void happyPath() throws Exception {
        ImmutableList<URL> urls = ImmutableList.of(
                new URL("https://google.com"),
                new URL("https://twitter.com"));
        StringWriter writer = new StringWriter();
        HttpClient client = HttpClientUtil.build();
        TaskProcessorListener listener = new TaskProcessorListenerImpl(writer);
        TaskProcessor processor = new TaskProcessor(client,
                Optional.of(listener),
                urls.stream(),
                20);
        processor.execute();
        assertEquals("Processing URL: https://google.com\nProcessing URL: https://twitter.com\n",
                writer.toString());
    }
}
