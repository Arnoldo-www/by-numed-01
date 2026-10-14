package com.clinicplatform.modules.facturacion.dto;

import jakarta.validation.constraints.NotBlank;

public record AnularFacturaRequest(@NotBlank String motivo) {}
