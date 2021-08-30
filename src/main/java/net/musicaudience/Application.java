package net.musicaudience;

import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import static net.musicaudience.util.ArgsValidator.validateArgs;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        validateArgs(new DefaultApplicationArguments(args));
        SpringApplication.run(Application.class, args);
    }

}
