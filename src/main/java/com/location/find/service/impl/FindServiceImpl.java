package com.location.find.service.impl;

import com.location.find.model.FindResponse;
import com.location.find.model.LocationRequest;
import com.location.find.model.Position;
import com.location.find.model.outbound.BaseContact;
import com.location.find.model.outbound.Contact;
import com.location.find.model.outbound.Item;
import com.location.find.model.outbound.OpeningHour;
import com.location.find.outbound.LocationOutbound;
import com.location.find.service.FindService;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FindServiceImpl implements FindService {

    private final LocationOutbound locationOutbound;

    @Autowired
    public FindServiceImpl(LocationOutbound locationOutbound) {
        this.locationOutbound = locationOutbound;
    }

    @Override
    public Mono<List<FindResponse>> findLocation(LocationRequest locationRequest) {
        return Mono.defer(() -> {
            if (StringUtils.isNotBlank(locationRequest.getLocation())) {
                return locationOutbound.getGeocode(locationRequest.getLocation())
                        .flatMap(geoLocation -> {
                            if (geoLocation == null || CollectionUtils.isEmpty(geoLocation.getItems())) {
                                return Mono.empty();
                            }
                            Item item = geoLocation.getItems().get(0);
                            Position position = item.getPosition();
                            locationRequest.setLongitude(position.getLng());
                            locationRequest.setLatitude(position.getLat());
                            return getDiscover(locationRequest);
                        });
            }
            return getDiscover(locationRequest);
        });
    }

    private Mono<List<FindResponse>> getDiscover(LocationRequest locationRequest) {
        return locationOutbound.getDiscover(locationRequest)
                .flatMap(discover -> Flux.fromIterable(discover.getItems())
                        .map(discoverItem -> FindResponse.builder()
                                .name(discoverItem.getTitle())
                                .address(discoverItem.getAddress())
                                .payment(discoverItem.getPayment())
                                .distance(discoverItem.getDistance())
                                .position(discoverItem.getPosition())
                                .contact(getContact(discoverItem.getContacts()))
                                .openingHours(CollectionUtils.isEmpty(discoverItem.getOpeningHours()) ?
                                        Collections.emptyList() : discoverItem.getOpeningHours()
                                        .stream()
                                        .map(OpeningHour::getText)
                                        .flatMap(List::stream)
                                        .toList())
                                .build())
                        .sort(Comparator.comparingDouble(FindResponse::getDistance))
                        .collectList());
    }

    private String getContact(List<Contact> contacts) {
        if (CollectionUtils.isEmpty(contacts)) {
            return "";
        }
        return contacts
                .stream()
                .map(contact -> {
                    if (contact == null || CollectionUtils.isEmpty(contact.getPhone())) {
                        return "";
                    }
                    return contact.getPhone()
                            .stream()
                            .map(BaseContact::getValue)
                            .collect(Collectors.joining(","));
                })
                .collect(Collectors.joining(","));
    }
}
