package com.devspods;

import com.devspods.util.ArgsValidator;
import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        ArgsValidator.validateArgs(new DefaultApplicationArguments(args));
        SpringApplication.run(Application.class, args);
    }

}
