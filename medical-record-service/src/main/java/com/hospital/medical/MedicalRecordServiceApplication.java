package com.hospital.medical;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Microservice Dossiers Médicaux.
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class MedicalRecordServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(MedicalRecordServiceApplication.class, args);
    }
}
