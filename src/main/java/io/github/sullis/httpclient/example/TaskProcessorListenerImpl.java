package io.github.sullis.httpclient.example;

import java.io.IOException;
import java.net.URL;
import java.io.Writer;

public class TaskProcessorListenerImpl
        implements TaskProcessorListener {
    private Writer _writer;

    public TaskProcessorListenerImpl(Writer writer) {
        _writer = writer;
    }

    @Override
    public void statusFetchStarted(URL url) {
        try {
            _writer.write("Fetching URL: " + url + "\n");
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
