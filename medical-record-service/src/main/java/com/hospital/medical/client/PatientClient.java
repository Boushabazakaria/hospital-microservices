package com.hospital.medical.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

/**
 * Client Feign vers le Patient Service - pour vérifier l'existence des patients.
 */
@FeignClient(name = "patient-service", path = "/api/patients", fallback = PatientClientFallback.class)
public interface PatientClient {

    @GetMapping("/{id}/exists")
    Map<String, Boolean> checkPatientExists(@PathVariable("id") Long id);
}

/**
 * Fallback : appelé quand le Patient Service est indisponible.
 */
@Component
@Slf4j
class PatientClientFallback implements PatientClient {

    @Override
    public Map<String, Boolean> checkPatientExists(Long id) {
        log.warn("[FALLBACK] Patient Service indisponible pour vérifier patient ID: {}", id);
        return Map.of("exists", false);
    }
}
