package com.clinicplatform.modules.portal;

import com.clinicplatform.common.exception.AppException;
import com.clinicplatform.modules.paciente.Paciente;
import com.clinicplatform.modules.paciente.PacienteRepository;
import com.clinicplatform.modules.portal.dto.PortalLoginRequest;
import com.clinicplatform.modules.portal.dto.PortalLoginResponse;
import com.clinicplatform.security.JwtService;
import com.clinicplatform.security.Role;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PortalAuthService {

    private final PacienteRepository pacienteRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public PortalAuthService(PacienteRepository pacienteRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.pacienteRepository = pacienteRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public PortalLoginResponse login(PortalLoginRequest request) {
        Paciente paciente = pacienteRepository.findByEmail(request.email())
                .orElseThrow(() -> new AppException("Credenciales inválidas", HttpStatus.UNAUTHORIZED));

        if (!Boolean.TRUE.equals(paciente.getPortalActivo()) || paciente.getPasswordHash() == null) {
            throw new AppException("El portal no está activado para este paciente", HttpStatus.FORBIDDEN);
        }

        if (!passwordEncoder.matches(request.password(), paciente.getPasswordHash())) {
            throw new AppException("Credenciales inválidas", HttpStatus.UNAUTHORIZED);
        }

        String token = jwtService.generateToken(paciente.getId(), paciente.getClinica().getId(), Role.PACIENTE);
        return new PortalLoginResponse(token);
    }
}
