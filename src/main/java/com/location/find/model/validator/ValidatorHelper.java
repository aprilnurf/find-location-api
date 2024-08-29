package com.location.find.model.validator;

import io.micrometer.common.util.StringUtils;

import java.util.Objects;

public class ValidatorHelper {

  private ValidatorHelper() {
  }

  public static boolean isNotSoEmpty(String s, Class clazz) {
    if (clazz == String.class) {
      return StringUtils.isNotBlank(s);
    } else {
      return Objects.nonNull(s);
    }
  }
}
