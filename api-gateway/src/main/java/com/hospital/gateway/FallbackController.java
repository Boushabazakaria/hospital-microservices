package com.hospital.gateway;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Contrôleur de Fallback de la Gateway.
 * Renvoie un message clair quand un service est indisponible.
 * Les Circuit Breakers de la Gateway redirigent ici en cas de panne.
 */
@RestController
@RequestMapping("/fallback")
public class FallbackController {

    @GetMapping("/patient")
    public ResponseEntity<Map<String, String>> patientFallback() {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(
            Map.of(
                "erreur", "Service Patient temporairement indisponible",
                "message", "Veuillez réessayer dans quelques instants"
            )
        );
    }

    @GetMapping("/appointment")
    public ResponseEntity<Map<String, String>> appointmentFallback() {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(
            Map.of(
                "erreur", "Service Rendez-vous temporairement indisponible",
                "message", "Veuillez réessayer dans quelques instants"
            )
        );
    }

    @GetMapping("/medical")
    public ResponseEntity<Map<String, String>> medicalFallback() {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(
            Map.of(
                "erreur", "Service Dossiers Médicaux temporairement indisponible",
                "message", "Veuillez réessayer dans quelques instants"
            )
        );
    }
}
