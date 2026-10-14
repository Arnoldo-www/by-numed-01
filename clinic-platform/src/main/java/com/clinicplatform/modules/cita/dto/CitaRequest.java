package com.clinicplatform.modules.cita.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.UUID;

public record CitaRequest(
        @NotNull UUID pacienteId,
        @NotNull LocalDateTime fechaHora,
        String motivo,
        // "presencial" (default) o "virtual"
        String modalidad
) {}
