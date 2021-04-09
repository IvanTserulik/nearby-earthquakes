package edu.itserulik.earthquakes.validation;

import dagger.Module;
import dagger.Provides;
import edu.itserulik.earthquakes.validation.impl.ConsoleValidatorImpl;
import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;

import javax.validation.Validation;
import javax.validation.Validator;

@Module
public class ValidationModule {

    @Provides
    public Validator validator() {
        return Validation.byDefaultProvider()
                .configure()
                .messageInterpolator(new ParameterMessageInterpolator())
                .buildValidatorFactory()
                .getValidator();
    }

    @Provides
    public ConsoleValidator consoleValidator(ConsoleValidatorImpl impl) {
        return impl;
    }

}
