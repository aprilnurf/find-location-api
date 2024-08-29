package com.location.find.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class WebClientUtils {

    public <T> Mono<ResponseEntity<T>> get(WebClient webClient, String uri,
                                           MultiValueMap queryParam, Class<T> targetClass) {
        return webClient.get()
                .uri(builder -> builder.path(uri).queryParams(queryParam).build())
                .exchange()
                .flatMap(clientResponse -> clientResponse.toEntity(targetClass)
                        .publishOn(Schedulers.boundedElastic())
                        .map(responseEntity -> {
                            log.info("Response {} ", responseEntity);

                            if (isValidHeaders(clientResponse)) {
                                return new ResponseEntity<>(
                                        responseEntity.getBody(),
                                        responseEntity.getHeaders(),
                                        responseEntity.getStatusCode());
                            }

                            return new ResponseEntity<>(
                                    newInstance(targetClass),
                                    responseEntity.getHeaders(),
                                    responseEntity.getStatusCode());
                        }))
                .onErrorResume(e -> {
                    log.error("Get queryParam {}, Error {}", queryParam, e.getMessage(), e);
                    return Mono.just(new ResponseEntity<>(newInstance(targetClass), null, HttpStatus.INTERNAL_SERVER_ERROR));
                });
    }


    private boolean isValidHeaders(ClientResponse clientResponse) {
        Optional<MediaType> contentTypeOptional = clientResponse.headers().contentType();
        if (contentTypeOptional.isPresent()) {
            MediaType mediaType = contentTypeOptional.get();
            return isJson(mediaType);
        }
        return false;
    }

    private boolean isJson(MediaType mediaType) {
        MediaType jsonUTF8 = new MediaType("application", "json", StandardCharsets.UTF_8);
        return MediaType.APPLICATION_JSON.equals(mediaType) || jsonUTF8
                .equals(mediaType);
    }


    private <T> T newInstance(Class<T> targetClass) {
        Map<Class<?>, Object> emptyObject = new ConcurrentHashMap<>();
        T object = (T) emptyObject.get(targetClass);

        if (Objects.nonNull(object)) {
            return object;
        }

        try {
            object = targetClass.getDeclaredConstructor().newInstance();
            emptyObject.put(targetClass, object);

            return object;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}