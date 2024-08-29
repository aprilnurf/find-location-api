package com.location.find.outbound.impl;

import com.location.find.configuration.WebClientUtils;
import com.location.find.model.LocationRequest;
import com.location.find.model.OutboundPath;
import com.location.find.model.outbound.LocationOutboundResponse;
import com.location.find.outbound.LocationOutbound;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Slf4j
@Service
public class LocationOutboundImpl implements LocationOutbound {

    private final WebClientUtils webClientUtils;
    private final WebClient webClientGeoLocation;
    private final WebClient webClientDiscover;
    @Value("${location.here.apiKey}")
    private String apiKey;
    @Value("${location.here.radius}")
    private int defaultRadius;

    @Autowired
    public LocationOutboundImpl(WebClientUtils webClientUtils, WebClient webClientGeoLocation, WebClient webClientDiscover) {
        this.webClientUtils = webClientUtils;
        this.webClientGeoLocation = webClientGeoLocation;
        this.webClientDiscover = webClientDiscover;
    }

    @Override
    public Mono<LocationOutboundResponse> getGeocode(String location) {
        MultiValueMap<String, String> queryParam = new LinkedMultiValueMap<>();
        queryParam.add("apiKey", apiKey);
        queryParam.add("q", location);
        return webClientUtils.get(webClientGeoLocation, OutboundPath.BASE_PATH_GEOCODE, queryParam, LocationOutboundResponse.class)
                .map(ResponseEntity::getBody)
                .doOnError(e -> {
                    log.error("Error getGeocode {} :  ", location, e);
                });
    }

    @Override
    public Mono<LocationOutboundResponse> getDiscover(LocationRequest locationRequest) {
        MultiValueMap<String, String> queryParam = new LinkedMultiValueMap<>();
        queryParam.add("in", buildCircleReq(locationRequest));
        queryParam.add("apiKey", apiKey);
        queryParam.add("q", locationRequest.getType().name());
        return webClientUtils.get(webClientDiscover, OutboundPath.BASE_PATH_DISCOVER, queryParam, LocationOutboundResponse.class)
                .map(ResponseEntity::getBody)
                .doOnError(e -> {
                    log.error("Error getDiscover {} :  ", locationRequest, e);
                });
    }

    private String buildCircleReq(LocationRequest locationRequest) {
        return "circle:" + locationRequest.getLatitude() + "," + locationRequest.getLongitude() +
                ";r=" + Optional.ofNullable(locationRequest.getRadius()).orElse(defaultRadius);
    }
}
