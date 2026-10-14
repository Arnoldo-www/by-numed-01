package com.clinicplatform.modules.facturacion.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record FacturaRequest(
        @NotNull UUID pacienteId,
        @NotEmpty @Valid List<FacturaItemRequest> items
) {}
