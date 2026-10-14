package com.clinicplatform.modules.paciente.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

public record PacienteRequest(
        @NotBlank String nombre,
        @NotBlank String apellido,
        String telefono,
        LocalDate fechaNacimiento,
        @Email String email
) {}
