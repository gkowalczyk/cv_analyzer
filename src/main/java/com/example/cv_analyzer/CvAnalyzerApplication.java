package com.example.cv_analyzer;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class CvAnalyzerApplication {

    public static void main(String[] args) {
        SpringApplication.run(CvAnalyzerApplication.class, args);
    }


}
