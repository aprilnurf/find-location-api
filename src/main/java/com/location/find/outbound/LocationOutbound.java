package com.location.find.outbound;

import com.location.find.model.LocationRequest;
import com.location.find.model.outbound.LocationOutboundResponse;
import reactor.core.publisher.Mono;

public interface LocationOutbound {
    Mono<LocationOutboundResponse> getGeocode(String location);
    Mono<LocationOutboundResponse> getDiscover(LocationRequest locationRequest);
}
