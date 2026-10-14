package com.clinicplatform.security;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class CurrentUser {
    // Para STAFF: id = usuario.id. Para PACIENTE: id = paciente.id.
    private final UUID id;
    private final UUID clinicaId;
    private final Role role;
}
