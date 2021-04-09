package edu.itserulik.earthquakes.validation;

import javax.validation.ConstraintViolation;
import java.util.Set;

public interface ConsoleValidator {

    <T> Set<ConstraintViolation<T>> validate(T t);

    <T> Set<String> constraints(T t);

}
