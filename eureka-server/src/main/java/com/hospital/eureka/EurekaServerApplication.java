package com.hospital.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * Serveur Eureka - Centre de découverte de services.
 * Tous les microservices s'enregistrent ici au démarrage.
 * L'API Gateway interroge ce serveur pour trouver les adresses des services.
 */
@SpringBootApplication
@EnableEurekaServer  // Active le serveur Eureka
public class EurekaServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(EurekaServerApplication.class, args);
    }
}
