package com.clinicplatform.modules.cita;

import com.clinicplatform.modules.cita.dto.CitaRequest;
import com.clinicplatform.modules.cita.dto.CitaResponse;
import com.clinicplatform.security.CurrentUser;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/citas")
public class CitaController {

    private final CitaService citaService;

    public CitaController(CitaService citaService) {
        this.citaService = citaService;
    }

    @PostMapping
    public ResponseEntity<CitaResponse> crear(@AuthenticationPrincipal CurrentUser user,
                                                @Valid @RequestBody CitaRequest request) {
        return ResponseEntity.status(201).body(citaService.crear(user.getClinicaId(), request));
    }

    @GetMapping
    public List<CitaResponse> listar(@AuthenticationPrincipal CurrentUser user) {
        return citaService.listar(user.getClinicaId());
    }
}
