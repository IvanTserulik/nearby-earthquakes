package edu.itserulik.earthquakes.console;

import dagger.Binds;
import dagger.Module;
import edu.itserulik.earthquakes.console.impl.ConsoleHandlerImpl;
import edu.itserulik.earthquakes.service.ServiceModule;
import edu.itserulik.earthquakes.validation.ValidationModule;

@Module(includes = {ServiceModule.class, ValidationModule.class})
public interface ConsoleModule {

    @Binds
    ConsoleHandler consoleHandler(ConsoleHandlerImpl impl);
}
