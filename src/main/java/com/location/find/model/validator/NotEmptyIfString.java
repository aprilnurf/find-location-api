package com.location.find.model.validator;

import jakarta.validation.*;
import org.apache.commons.lang3.StringUtils;

import java.lang.annotation.*;
import java.lang.reflect.Field;
import java.util.Optional;

/**
 * Validate that a field cannot be empty or null
 * if another field matches to the given String condition.
 * Note: Put this upon the class
 * Default message: {field} cannot be empty or null if {dependentField} is {condition} {conditionValue}
 * Copy or override this message in your messages.properties and/or messages_[lang].properties
 */

@Documented
@Constraint(validatedBy = NotEmptyIfString.NotEmptyIfStringValidator.class)
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(NotEmptyIfString.List.class)
public @interface NotEmptyIfString {

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  String message() default "{hotel.common.constraints.NotEmptyIfString.message}";

  String field();

  String dependentField();

  Condition condition();

  String conditionValue() default "";

  Class fieldType();

  @Target({ElementType.TYPE})
  @Retention(RetentionPolicy.RUNTIME)
  @Documented
  @interface List {

    NotEmptyIfString[] value();
  }


  class NotEmptyIfStringValidator implements ConstraintValidator<NotEmptyIfString, Object> {

    private NotEmptyIfString constraintAnnotation;

    @Override
    public void initialize(NotEmptyIfString constraintAnnotation) {
      this.constraintAnnotation = constraintAnnotation;
    }

    @Override
    public boolean isValid(Object object, ConstraintValidatorContext context) {

      try {

        Field field = object.getClass().getDeclaredField(constraintAnnotation.field());
        field.setAccessible(true);  // To access private fields
        String fieldA = Optional.ofNullable(field.get(object)).orElse("").toString();

        Field fieldB = object.getClass().getDeclaredField(constraintAnnotation.dependentField());
        fieldB.setAccessible(true);  // To access private fields
        String dependentField = Optional.ofNullable(fieldB.get(object)).orElse("").toString();
//
//        String field = getProperty(object, constraintAnnotation.field());
//        String dependentField = BeanUtils
//            .getProperty(object, constraintAnnotation.dependentField());

        boolean isMatchCondition;

        switch (constraintAnnotation.condition()) {
          case EMPTY:
            isMatchCondition = StringUtils.isBlank(dependentField);
            break;
          case NOT_EMPTY:
            isMatchCondition = StringUtils.isNotBlank(dependentField);
            break;
          default:
            isMatchCondition = constraintAnnotation.conditionValue()
                .equalsIgnoreCase(dependentField);
            break;
        }

        return !isMatchCondition || ValidatorHelper
            .isNotSoEmpty(fieldA, constraintAnnotation.fieldType());
      } catch (Exception e) {
        throw new ValidationException(e);
      }
    }
  }

  enum Condition {
    EQUAL, EMPTY, NOT_EMPTY
  }
}

