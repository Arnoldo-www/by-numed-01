package com.clinicplatform.modules.facturacion;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FacturaRepository extends JpaRepository<Factura, UUID> {
    List<Factura> findByClinicaIdOrderByCreatedAtDesc(UUID clinicaId);
    List<Factura> findByPacienteIdOrderByCreatedAtDesc(UUID pacienteId);
    Optional<Factura> findByIdAndClinicaId(UUID id, UUID clinicaId);
}
