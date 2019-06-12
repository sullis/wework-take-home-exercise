package io.github.sullis.httpclient.example;

import java.net.URL;

public interface Task {
    public void processHttpResponse(URL url, int statusCode, String responseBody);
}
