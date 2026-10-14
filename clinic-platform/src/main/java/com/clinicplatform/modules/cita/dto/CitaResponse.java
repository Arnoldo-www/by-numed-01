package com.clinicplatform.modules.cita.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record CitaResponse(
        UUID id,
        UUID pacienteId,
        String pacienteNombreCompleto,
        LocalDateTime fechaHora,
        String motivo,
        String estado,
        String modalidad,
        String salaUrl
) {}
