package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication // Spring Boot'un otomatik yapılandırma ve bileşen tarama özelliklerini etkinleştirir
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args); // Spring Boot uygulamasını başlatır
    }
}