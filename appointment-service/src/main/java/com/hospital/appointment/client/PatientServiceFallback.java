package com.hospital.appointment.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Fallback du Patient Service Client.
 *
 * Cette classe est appelée automatiquement par Resilience4j quand :
 * - Le Patient Service est indisponible
 * - Le circuit est ouvert (trop d'erreurs récentes)
 * - Un timeout est atteint
 *
 * Permet une dégradation contrôlée du système sans arrêt complet.
 */
@Component
@Slf4j
public class PatientServiceFallback implements PatientServiceClient {

    /**
     * En cas d'indisponibilité du Patient Service, on retourne "inconnu"
     * plutôt que de faire planter tout le système.
     * Le service appelant peut alors décider comment gérer ce cas.
     */
    @Override
    public Map<String, Boolean> checkPatientExists(Long id) {
        log.warn("[FALLBACK] Patient Service indisponible - impossible de vérifier l'existence du patient ID: {}", id);
        // On retourne false pour refuser la création du RDV sans confirmation du patient
        return Map.of("exists", false);
    }
}
