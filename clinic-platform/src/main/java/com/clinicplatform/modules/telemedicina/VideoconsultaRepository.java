package com.clinicplatform.modules.telemedicina;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface VideoconsultaRepository extends JpaRepository<Videoconsulta, UUID> {
    Optional<Videoconsulta> findByCitaId(UUID citaId);
}
