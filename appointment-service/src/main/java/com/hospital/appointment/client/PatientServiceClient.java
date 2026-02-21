package com.hospital.appointment.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

/**
 * Client Feign pour communiquer avec le Patient Service.
 *
 * @FeignClient : Spring génère automatiquement l'implémentation de cette interface.
 * - name : doit correspondre au spring.application.name du Patient Service
 * - path : préfixe des URLs
 * - fallback : classe utilisée en cas d'indisponibilité du service (Circuit Breaker)
 *
 * Grâce à Eureka, Feign résout automatiquement l'adresse de "patient-service"
 * sans avoir à coder l'URL en dur.
 */
@FeignClient(
    name = "patient-service",
    path = "/api/patients",
    fallback = PatientServiceFallback.class  // Activé quand le circuit est ouvert
)
public interface PatientServiceClient {

    /**
     * Vérifie si un patient existe dans le Patient Service.
     * Appel REST : GET http://patient-service/api/patients/{id}/exists
     */
    @GetMapping("/{id}/exists")
    Map<String, Boolean> checkPatientExists(@PathVariable("id") Long id);
}
