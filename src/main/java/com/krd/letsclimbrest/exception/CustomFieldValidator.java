package com.krd.letsclimbrest.exception;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.List;

public class CustomFieldValidator implements ConstraintValidator<ValidFieldValues, String> {

    private List<String> allowedValues;

    @Override
    public void initialize(ValidFieldValues constraintAnnotation) {
        allowedValues = List.of(constraintAnnotation.allowedValues());
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value == null || allowedValues.contains(value);
    }
}
