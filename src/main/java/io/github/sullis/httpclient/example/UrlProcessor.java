package io.github.sullis.httpclient.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URISyntaxException;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Iterator;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

public final class UrlProcessor {
    private final Logger _logger = LoggerFactory.getLogger(UrlProcessor.class);
    private final HttpClient _httpClient;
    private final Optional<UrlProcessorListener> _listener;
    private final int _maxConcurrentHttpRequests;
    private final Stream<URL> _urls;
    private final ResponseBodyProcessor _responseBodyProcessor;

    public UrlProcessor(HttpClient client,
                        Optional<UrlProcessorListener> listener,
                        Stream<URL> urls,
                        int maxConcurrentHttpRequests,
                        ResponseBodyProcessor responseBodyProcessor) {
        this._httpClient = client;
        this._listener = listener;
        this._maxConcurrentHttpRequests = maxConcurrentHttpRequests;
        this._urls = urls;
        this._responseBodyProcessor = responseBodyProcessor;
    }

    public CompletableFuture<UrlProcessorResult> execute() {
        final Semaphore httpSemaphore = new Semaphore(_maxConcurrentHttpRequests);
        final Iterator<URL> iter = _urls.iterator();
        final AtomicLong processedCount = new AtomicLong(0);
        final AtomicLong failureCount = new AtomicLong(0);
        while (iter.hasNext()) {
            try {
                final URL url = iter.next();
                _logger.debug("PERMIT NEEDED [" + url + "]");
                httpSemaphore.acquireUninterruptibly();
                _logger.debug("PERMIT ACQUIRED [" + url + "]");
                _logger.debug("httpSemaphore: availablePermits="
                        + httpSemaphore.availablePermits());
                CompletableFuture<HttpResponse<String>> responseFuture = processUrl(url);
                responseFuture.whenCompleteAsync((httpResponse, throwable) -> {
                    if (httpResponse != null) {
                        processedCount.incrementAndGet();
                    }
                    if (throwable != null) {
                        failureCount.incrementAndGet();
                    }
                    httpSemaphore.release();
                    _logger.debug("PERMIT RELEASED [" + url + "]. throwable=" + throwable);
                });
            } catch (Exception ex) {
                return CompletableFuture.failedFuture(ex);
            }
        }
        if (_logger.isDebugEnabled()) {
            _logger.debug("Calling [acquireUninterruptibly]. "
                    + "availablePermits="
                    + httpSemaphore.availablePermits());
        }
        httpSemaphore.acquireUninterruptibly(_maxConcurrentHttpRequests);
        return CompletableFuture.completedFuture(UrlProcessorResult.create(processedCount.get(), failureCount.get()));
    }

    protected CompletableFuture<HttpResponse<String>> processUrl(URL url) {
        statusProcessingUrl(url);
        try {
            HttpRequest request = buildRequest(url);
            return _httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApplyAsync(httpResp -> {
                        if (_logger.isDebugEnabled()) {
                            _logger.debug("[" + url + "] statusCode=" + httpResp.statusCode());
                        }
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
                .timeout(Duration.ofMillis(60000))
                .header("User-Agent", HttpClientUtil.USER_AGENT)
                .header("Accept-Charset", CharSetUtil.DEFAULT_CHARSET.name())
                .build();
    }

}
