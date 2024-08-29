package com.location.find.controller;

import com.location.find.library.ResponseHelper;
import com.location.find.model.*;
import com.location.find.service.FindService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping(ApiPath.FIND)
public class FindController extends ErrorHandlerController {

    private final FindService findService;

    @Autowired
    public FindController(FindService findService) {
        this.findService = findService;
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    Mono<BaseResponse<List<FindResponse>>> paymentStatus(@RequestBody @Valid LocationRequest locationRequest) {
        return findService.findLocation(locationRequest)
                .map(response -> ResponseHelper.constructResponse(
                        ResponseCode.SUCCESS.getCode(),
                        ResponseCode.SUCCESS.getMessage(),
                        null,
                        response));
    }
}
