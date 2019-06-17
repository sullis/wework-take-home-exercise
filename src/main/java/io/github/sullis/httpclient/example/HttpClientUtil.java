package io.github.sullis.httpclient.example;

import java.net.http.HttpClient;
import java.time.Duration;

public class HttpClientUtil {
    static public final String USER_AGENT = HttpClientUtil.class.getName();

    private HttpClientUtil() { /* empty */ }

    static public HttpClient build() {
        return HttpClient.newBuilder()
                .connectTimeout(Duration.ofMillis(2000))
                .followRedirects(HttpClient.Redirect.NORMAL)
                .version(HttpClient.Version.HTTP_1_1)
                .build();
    }
}
