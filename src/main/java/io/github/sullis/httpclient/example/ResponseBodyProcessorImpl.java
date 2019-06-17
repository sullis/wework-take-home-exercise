package io.github.sullis.httpclient.example;

import java.io.IOException;
import java.net.URL;
import java.util.regex.Pattern;

public class ResponseBodyProcessorImpl implements ResponseBodyProcessor {
    private final OutputFileWriter _writer;
    private final Pattern _regexPattern;

    public ResponseBodyProcessorImpl(OutputFileWriter writer, Pattern regexPattern) {
        _writer = writer;
        _regexPattern = regexPattern;
    }

    @Override
    public void processHttpResponse(URL url, int statusCode, String responseBody) {
        try {
            if (_regexPattern.matcher(responseBody).find()) {
                _writer.writeLine(url, true);
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}

