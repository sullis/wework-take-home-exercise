package io.github.sullis.httpclient.example;

import java.net.http.HttpClient;
import java.time.Duration;

public class HttpClientUtil {
    private HttpClientUtil() { /* empty */ }

    static public HttpClient build() {
        return HttpClient.newBuilder()
                .connectTimeout(Duration.ofMillis(10))
                .followRedirects(HttpClient.Redirect.ALWAYS)
                .build();
    }
}
