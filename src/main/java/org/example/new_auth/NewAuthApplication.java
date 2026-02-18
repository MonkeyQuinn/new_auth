package org.example.new_auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class NewAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(NewAuthApplication.class, args);
    }

}
