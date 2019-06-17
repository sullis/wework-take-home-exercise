package io.github.sullis.httpclient.example;

import java.net.URISyntaxException;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Iterator;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

public final class UrlProcessor {
    private final HttpClient _httpClient;
    private final Optional<UrlProcessorListener> _listener;
    private final int _maxConcurrency;
    private final Stream<URL> _urls;
    private final ResponseBodyProcessor _responseBodyProcessor;

    public UrlProcessor(HttpClient client,
                        Optional<UrlProcessorListener> listener,
                        Stream<URL> urls,
                        int maxConcurrency,
                        ResponseBodyProcessor responseBodyProcessor) {
        this._httpClient = client;
        this._listener = listener;
        this._maxConcurrency = maxConcurrency;
        this._urls = urls;
        this._responseBodyProcessor = responseBodyProcessor;
    }

    public CompletableFuture<UrlProcessorResult> execute() {
        Iterator<URL> iter = _urls.iterator();
        AtomicLong processedCount = new AtomicLong(0);
        AtomicLong failureCount = new AtomicLong(0);
        while(iter.hasNext()) {
            URL url = iter.next();
            CompletableFuture<HttpResponse<String>> responseFuture = processUrl(url);
            try {
                responseFuture.get(10, TimeUnit.SECONDS);
                processedCount.incrementAndGet();
            } catch (Exception ex) {
                failureCount.incrementAndGet();
            }
        }
        return CompletableFuture.completedFuture(UrlProcessorResult.create(processedCount.get(), failureCount.get()));
    }

    protected CompletableFuture<HttpResponse<String>> processUrl(URL url) {
        statusProcessingUrl(url);
        try {
            HttpRequest request = buildRequest(url);
            return _httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(httpResp -> {
                        _responseBodyProcessor.processHttpResponse(url, httpResp.statusCode(), httpResp.body());
                        return httpResp;
                    });
        }
        catch (URISyntaxException ex) {
            return CompletableFuture.failedFuture(ex);
        }
    }


    protected void statusProcessingUrl(URL url) {
        try {
            _listener.ifPresent(l -> l.statusProcessingUrl(url));
        }
        catch (Exception ex) {
            // ignore
        }
    }

    protected HttpRequest buildRequest(URL url) throws URISyntaxException {
        return HttpRequest.newBuilder(url.toURI())
                .GET()
                .header("User-Agent", HttpClientUtil.USER_AGENT)
                .header("Accept-Charset", CharSetUtil.DEFAULT_CHARSET.name())
                .build();
    }

}
