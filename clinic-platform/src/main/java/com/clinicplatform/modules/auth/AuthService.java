package com.clinicplatform.modules.auth;

import com.clinicplatform.common.exception.AppException;
import com.clinicplatform.modules.auth.dto.LoginRequest;
import com.clinicplatform.modules.auth.dto.LoginResponse;
import com.clinicplatform.modules.auth.dto.RegisterRequest;
import com.clinicplatform.modules.clinica.Clinica;
import com.clinicplatform.modules.clinica.ClinicaRepository;
import com.clinicplatform.modules.usuario.Usuario;
import com.clinicplatform.modules.usuario.UsuarioRepository;
import com.clinicplatform.security.JwtService;
import com.clinicplatform.security.Role;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final ClinicaRepository clinicaRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UsuarioRepository usuarioRepository,
                        ClinicaRepository clinicaRepository,
                        PasswordEncoder passwordEncoder,
                        JwtService jwtService) {
        this.usuarioRepository = usuarioRepository;
        this.clinicaRepository = clinicaRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Transactional
    public void register(RegisterRequest request) {
        Clinica clinica = clinicaRepository.findById(request.clinicaId())
                .orElseThrow(() -> new AppException("Clínica no encontrada", HttpStatus.BAD_REQUEST));

        usuarioRepository.findByEmail(request.email()).ifPresent(u -> {
            throw new AppException("El correo ya está registrado", HttpStatus.CONFLICT);
        });

        Usuario usuario = Usuario.builder()
                .clinica(clinica)
                .email(request.email())
                .passwordHash(passwordEncoder.encode(request.password()))
                .build();

        usuarioRepository.save(usuario);
    }

    public LoginResponse login(LoginRequest request) {
        Usuario usuario = usuarioRepository.findByEmail(request.email())
                .orElseThrow(() -> new AppException("Credenciales inválidas", HttpStatus.UNAUTHORIZED));

        if (!passwordEncoder.matches(request.password(), usuario.getPasswordHash())) {
            throw new AppException("Credenciales inválidas", HttpStatus.UNAUTHORIZED);
        }

        String token = jwtService.generateToken(usuario.getId(), usuario.getClinica().getId(), Role.STAFF);
        return new LoginResponse(token);
    }
}
