package com.clinicplatform.modules.paciente;

import com.clinicplatform.common.exception.AppException;
import com.clinicplatform.common.util.PasswordGenerator;
import com.clinicplatform.modules.clinica.ClinicaRepository;
import com.clinicplatform.modules.paciente.dto.ActivarPortalResponse;
import com.clinicplatform.modules.paciente.dto.PacienteRequest;
import com.clinicplatform.modules.paciente.dto.PacienteResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PacienteService {

    private final PacienteRepository pacienteRepository;
    private final ClinicaRepository clinicaRepository;
    private final PasswordEncoder passwordEncoder;

    public PacienteService(PacienteRepository pacienteRepository,
                            ClinicaRepository clinicaRepository,
                            PasswordEncoder passwordEncoder) {
        this.pacienteRepository = pacienteRepository;
        this.clinicaRepository = clinicaRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public PacienteResponse crear(UUID clinicaId, PacienteRequest request) {
        Paciente paciente = Paciente.builder()
                .clinica(clinicaRepository.getReferenceById(clinicaId))
                .nombre(request.nombre())
                .apellido(request.apellido())
                .telefono(request.telefono())
                .fechaNacimiento(request.fechaNacimiento())
                .email(request.email())
                .build();
        Paciente saved = pacienteRepository.save(paciente);
        return toResponse(saved);
    }

    public List<PacienteResponse> listar(UUID clinicaId) {
        return pacienteRepository.findByClinicaIdOrderByApellido(clinicaId)
                .stream().map(this::toResponse).toList();
    }

    public PacienteResponse obtener(UUID clinicaId, UUID id) {
        Paciente paciente = pacienteRepository.findByIdAndClinicaId(id, clinicaId)
                .orElseThrow(() -> new AppException("Paciente no encontrado", HttpStatus.NOT_FOUND));
        return toResponse(paciente);
    }

    // Acción de personal de clínica: habilita el acceso al portal de un
    // paciente. Genera una contraseña temporal. En producción esa contraseña
    // se envía por correo al paciente y nunca se devuelve en la respuesta de
    // la API — aquí se devuelve porque este starter no incluye proveedor de
    // correo todavía.
    public ActivarPortalResponse activarPortal(UUID clinicaId, UUID pacienteId) {
        Paciente paciente = pacienteRepository.findByIdAndClinicaId(pacienteId, clinicaId)
                .orElseThrow(() -> new AppException("Paciente no encontrado", HttpStatus.NOT_FOUND));

        if (paciente.getEmail() == null || paciente.getEmail().isBlank()) {
            throw new AppException("El paciente necesita un correo registrado antes de activar el portal", HttpStatus.BAD_REQUEST);
        }

        String passwordTemporal = PasswordGenerator.generar(10);
        paciente.setPasswordHash(passwordEncoder.encode(passwordTemporal));
        paciente.setPortalActivo(true);
        pacienteRepository.save(paciente);

        return new ActivarPortalResponse(paciente.getEmail(), passwordTemporal);
    }

    private PacienteResponse toResponse(Paciente p) {
        return new PacienteResponse(p.getId(), p.getNombre(), p.getApellido(), p.getTelefono(),
                p.getFechaNacimiento(), p.getEmail(), p.getPortalActivo());
    }
}
