package com.chc.coindesk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;



@SpringBootApplication
@EnableScheduling
public class CoindeskApplication {
    public static void main(String[] args) {
        SpringApplication.run(CoindeskApplication.class, args);
    }

}
