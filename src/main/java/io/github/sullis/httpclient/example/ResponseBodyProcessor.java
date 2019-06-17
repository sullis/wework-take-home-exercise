package io.github.sullis.httpclient.example;

import java.net.URL;

public interface ResponseBodyProcessor {
    void processHttpResponse(URL url, int statusCode, String responseBody);
}
