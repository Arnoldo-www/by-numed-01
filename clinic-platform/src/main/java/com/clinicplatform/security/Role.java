package com.clinicplatform.security;

/**
 * STAFF: personal de la clínica (lo que ya existía).
 * PACIENTE: acceso al portal del paciente, mucho más restringido —
 * solo puede ver sus propias citas y facturas, nunca las de otro paciente
 * ni operaciones de otras clínicas.
 */
public enum Role {
    STAFF,
    PACIENTE
}
