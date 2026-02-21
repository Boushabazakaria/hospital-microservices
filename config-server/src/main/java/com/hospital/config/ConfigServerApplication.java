package com.hospital.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Serveur de Configuration Centralisée.
 *
 * Ce serveur distribue la configuration à tous les microservices.
 * Avantage : modifier une propriété ici suffit pour tous les services.
 * Dans ce projet, nous utilisons le mode "native" (fichiers locaux).
 * En production, on utiliserait un dépôt Git.
 */
@SpringBootApplication
@EnableConfigServer    // Active le serveur de configuration
@EnableDiscoveryClient
public class ConfigServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ConfigServerApplication.class, args);
    }
}
