package com.clinicplatform.modules.paciente;

import com.clinicplatform.modules.paciente.dto.ActivarPortalResponse;
import com.clinicplatform.modules.paciente.dto.PacienteRequest;
import com.clinicplatform.modules.paciente.dto.PacienteResponse;
import com.clinicplatform.security.CurrentUser;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/pacientes")
public class PacienteController {

    private final PacienteService pacienteService;

    public PacienteController(PacienteService pacienteService) {
        this.pacienteService = pacienteService;
    }

    @PostMapping
    public ResponseEntity<PacienteResponse> crear(@AuthenticationPrincipal CurrentUser user,
                                                    @Valid @RequestBody PacienteRequest request) {
        return ResponseEntity.status(201).body(pacienteService.crear(user.getClinicaId(), request));
    }

    @GetMapping
    public List<PacienteResponse> listar(@AuthenticationPrincipal CurrentUser user) {
        return pacienteService.listar(user.getClinicaId());
    }

    @GetMapping("/{id}")
    public PacienteResponse obtener(@AuthenticationPrincipal CurrentUser user, @PathVariable UUID id) {
        return pacienteService.obtener(user.getClinicaId(), id);
    }

    @PostMapping("/{id}/portal/activar")
    public ActivarPortalResponse activarPortal(@AuthenticationPrincipal CurrentUser user, @PathVariable UUID id) {
        return pacienteService.activarPortal(user.getClinicaId(), id);
    }
}
