package io.github.sullis.httpclient.example;

import java.io.IOException;
import java.net.URL;
import java.io.Writer;

public class UrlProcessorListenerImpl
        implements UrlProcessorListener {
    private Writer _writer;

    public UrlProcessorListenerImpl(Writer writer) {
        _writer = writer;
    }

    @Override
    public synchronized void statusProcessingUrl(URL url) {
        try {
            _writer.write("Processing URL: " + url + "\n");
            _writer.flush();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
