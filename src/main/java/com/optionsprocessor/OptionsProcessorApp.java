package com.optionsprocessor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class OptionsProcessorApp {
    public static void main(String[] args) {
        SpringApplication.run(OptionsProcessorApp.class, args);
    }
}
