package io.github.sullis.httpclient.example;

import java.io.IOException;
import java.net.URL;

public class ResponseBodyProcessorImpl implements ResponseBodyProcessor {
    private final OutputFileWriter _writer;
    public ResponseBodyProcessorImpl(OutputFileWriter writer) {
        _writer = writer;
    }

    @Override
    public void processHttpResponse(URL url, int statusCode, String responseBody) {
        try {
            _writer.writeLine(url, true);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}

