package com.location.find.controller;

import com.location.find.library.CustomException;
import com.location.find.library.ResponseHelper;
import com.location.find.model.BaseResponse;
import com.location.find.model.ResponseCode;
import jakarta.validation.ValidationException;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.support.WebExchangeBindException;

import java.util.Collections;
import java.util.List;

@Log4j2
public class ErrorHandlerController {

    @ExceptionHandler(BindException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public BaseResponse handleError(BindException ex) {
        log.error("Error during validation", ex);
        List<FieldError> fieldErrors = ex.getFieldErrors();
        List<String> errors = fieldErrors.stream().map(e-> e.getField() + " " +e.getDefaultMessage()).toList();
        return ResponseHelper.constructResponse(
                ResponseCode.BIND_ERROR.getCode(),
                ResponseCode.BIND_ERROR.getMessage(),
                errors, null);
    }

    @ExceptionHandler(WebExchangeBindException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public BaseResponse handleError(WebExchangeBindException ex) {
        log.error("Error during validation", ex);
        List<ObjectError> fieldErrors = ex.getBindingResult().getAllErrors();
        List<String> errors = fieldErrors.stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toList();
        return ResponseHelper.constructResponse(
                ResponseCode.BIND_ERROR.getCode(),
                ResponseCode.BIND_ERROR.getMessage(),
                errors, null);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public BaseResponse exception(Exception e) {
        log.error("Exception = {}", e.getMessage(), e);
        return ResponseHelper.constructResponse(ResponseCode.SYSTEM_ERROR.getCode(),
                ResponseCode.SYSTEM_ERROR.getMessage(), null, null);
    }

    @ExceptionHandler(CustomException.class)
    @ResponseStatus(value = HttpStatus.OK)
    public BaseResponse businessLogicException(CustomException ce) {
        log.error("BusinessLogicException = {}", ce.getMessage(), ce);
        return ResponseHelper.constructResponse(ce.getCode(), ce.getMessage(), null, null);
    }
}
