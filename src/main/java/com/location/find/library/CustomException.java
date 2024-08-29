package com.location.find.library;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
@AllArgsConstructor
public class CustomException extends RuntimeException {

  private String code;
  private String message;

}
