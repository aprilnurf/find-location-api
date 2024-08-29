package com.location.find.model;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@Builder
public class BaseResponse<T> implements Serializable {
    private String code;
    private String message;
    private T data;
    private List<String> errors;
    private Date serverTime;
}
