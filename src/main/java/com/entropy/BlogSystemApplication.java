package com.entropy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling //允许开启定时器
public class BlogSystemApplication {
    public static void main(String[] args) {
        SpringApplication.run(BlogSystemApplication.class);
    }
}
