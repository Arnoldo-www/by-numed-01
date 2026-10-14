package com.clinicplatform.modules.paciente.dto;

import java.time.LocalDate;
import java.util.UUID;

public record PacienteResponse(
        UUID id,
        String nombre,
        String apellido,
        String telefono,
        LocalDate fechaNacimiento,
        String email,
        Boolean portalActivo
) {}
