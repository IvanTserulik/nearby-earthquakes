package edu.itserulik.earthquakes.validation.impl;

import edu.itserulik.earthquakes.validation.ConsoleValidator;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Set;
import java.util.stream.Collectors;

@Singleton
public class ConsoleValidatorImpl implements ConsoleValidator {

    private Validator validator;

    @Inject
    public ConsoleValidatorImpl(Validator validator) {
        this.validator = validator;
    }

    @Override
    public <T> Set<ConstraintViolation<T>> validate(T t) {
        return validator.validate(t);
    }

    @Override
    public <T> Set<String> constraints(T t) {
        return validate(t).stream()
                .map(constraint -> String.join(" ",
                        constraint.getPropertyPath().toString(), constraint.getMessage()))
                .collect(Collectors.toSet());
    }
}
