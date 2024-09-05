package com.location.find.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResponseCode {
    SUCCESS("SUCCESS", "SUCCESS"),
    SERVICE_UNAVAILABLE("SERVICE_UNAVAILABLE", "Service unavailable"),
    BIND_ERROR("BIND_ERROR", "Please fill in mandatory parameter"),
    SYSTEM_ERROR("SYSTEM_ERROR", "System error"),
    FAILED("FAILED", "Failed error"),
    DATA_NOT_EXIST("DATA_NOT_EXIST", "Data Not Exist"),
    THIRD_PARTY_ERROR("THIRD_PARTY_ERROR", "Third party error"),
    TYPE_DATA_NOT_MATCH("TYPE_DATA_NOT_MATCH", "Type data not match");

    private String code;
    private String message;

}
