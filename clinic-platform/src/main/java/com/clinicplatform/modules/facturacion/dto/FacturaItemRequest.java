package com.clinicplatform.modules.facturacion.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record FacturaItemRequest(
        @NotBlank String descripcion,
        @NotNull @Positive BigDecimal cantidad,
        @NotNull @Positive BigDecimal precioUnitario
) {}
