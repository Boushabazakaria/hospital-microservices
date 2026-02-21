package com.hospital.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * API Gateway - Porte d'entrée unique vers tous les microservices.
 *
 * Rôles principaux :
 * 1. Routing : redirige les requêtes vers le bon microservice
 * 2. Load Balancing : distribue les appels si plusieurs instances
 * 3. Résolution : trouve les services via Eureka (lb://service-name)
 * 4. Résilience : Circuit Breaker sur les routes
 */
@SpringBootApplication
@EnableDiscoveryClient
public class ApiGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args);
    }
}
