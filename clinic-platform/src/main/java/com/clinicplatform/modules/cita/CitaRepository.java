package com.clinicplatform.modules.cita;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CitaRepository extends JpaRepository<Cita, UUID> {
    List<Cita> findByClinicaIdOrderByFechaHora(UUID clinicaId);
    List<Cita> findByPacienteIdOrderByFechaHora(UUID pacienteId);
}
