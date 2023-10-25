package com.dzaitsev.marshmallows;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MarshmallowsApplication {

    public static void main(String[] args) {
        SpringApplication.run(MarshmallowsApplication.class, args);
    }

}
