package com.location.find.configuration;

import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import java.util.concurrent.TimeUnit;

@Slf4j
@Configuration
public class WebClientConfiguration {

  @Bean("webClientGeoLocation")
  public WebClient webClient(GeoLocationConfiguration geoLocationConfiguration) {
    return getWebClient(geoLocationConfiguration);
  }

  @Bean("webClientDiscover")
  public WebClient webClientBooking(DiscoverApiConfiguration DIscoverApiConfiguration) {
    return getWebClient(DIscoverApiConfiguration);
  }

  private WebClient getWebClient(WebClientProperties webClientProperties) {

    ReactorClientHttpConnector connector = new ReactorClientHttpConnector(
        HttpClient
            .create()
            .doAfterRequest((httpClientRequest, connection) -> connection
                .addHandlerFirst(new ReadTimeoutHandler(webClientProperties.getReadTimeout(),
                    TimeUnit.MILLISECONDS))
                .addHandlerFirst(
                    new WriteTimeoutHandler(webClientProperties.getWriteTimeout(),
                        TimeUnit.MILLISECONDS)))
            .compress(true)
            .wiretap(true)
    );

    return WebClient.builder().clientConnector(connector).baseUrl(webClientProperties.getHost())
        .filter(logRequest())
        .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
        .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .defaultHeader(HttpHeaders.ACCEPT_ENCODING, "gzip")
        .build();
  }

  private static ExchangeFilterFunction logRequest() {
    return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
      log.info("Request: {} {} {}", clientRequest.method(), clientRequest.url(), clientRequest.headers());
      return Mono.just(clientRequest);
    });
  }
}