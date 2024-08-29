package com.location.find.service;

import com.location.find.model.FindResponse;
import com.location.find.model.LocationRequest;
import reactor.core.publisher.Mono;

import java.util.List;

public interface FindService {

    Mono<List<FindResponse>> findLocation(LocationRequest locationRequest);
}
