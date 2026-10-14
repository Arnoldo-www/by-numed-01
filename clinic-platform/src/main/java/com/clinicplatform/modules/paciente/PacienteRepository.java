package com.clinicplatform.modules.paciente;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PacienteRepository extends JpaRepository<Paciente, UUID> {
    List<Paciente> findByClinicaIdOrderByApellido(UUID clinicaId);
    Optional<Paciente> findByIdAndClinicaId(UUID id, UUID clinicaId);
    Optional<Paciente> findByEmail(String email);
}
