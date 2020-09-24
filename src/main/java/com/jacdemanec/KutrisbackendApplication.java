package com.jacdemanec;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;

@Configuration
@SpringBootApplication
@EnableAutoConfiguration
public class KutrisbackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(KutrisbackendApplication.class, args);
    }

}
