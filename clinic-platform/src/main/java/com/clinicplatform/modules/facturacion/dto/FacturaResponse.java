package com.clinicplatform.modules.facturacion.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record FacturaResponse(
        UUID id,
        UUID pacienteId,
        BigDecimal total,
        String estado,
        String motivoAnulacion
) {}
