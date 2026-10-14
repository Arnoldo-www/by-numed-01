package com.clinicplatform.modules.facturacion;

import com.clinicplatform.modules.facturacion.dto.FacturaRequest;
import com.clinicplatform.modules.facturacion.dto.FacturaResponse;

import java.util.List;
import java.util.UUID;

/**
 * Contrato de facturación. Hoy lo implementa FacturacionServiceLocalImpl,
 * que escribe directamente en la base de datos con transacciones y bloqueo
 * optimista. El día que exista una API de facturación centralizada (por
 * ejemplo, si SALUS expone la suya propia más adelante), se crea una
 * FacturacionServiceApiImpl que implemente esta misma interfaz llamando a
 * esa API por HTTP, se activa (por perfil de Spring o @Primary), y no se
 * toca ningún controller ni ninguna otra parte del sistema.
 */
public interface FacturacionService {
    FacturaResponse crear(UUID clinicaId, FacturaRequest request);
    FacturaResponse anular(UUID clinicaId, UUID facturaId, String motivo);
    List<FacturaResponse> listar(UUID clinicaId);
}
