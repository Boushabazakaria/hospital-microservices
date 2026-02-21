package com.hospital.patient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Microservice Patient.
 * @EnableDiscoveryClient : s'enregistre automatiquement auprès d'Eureka au démarrage.
 */
@SpringBootApplication
@EnableDiscoveryClient
public class PatientServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(PatientServiceApplication.class, args);
    }
}
