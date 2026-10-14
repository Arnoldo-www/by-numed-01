package com.clinicplatform.modules.facturacion;

import com.clinicplatform.modules.facturacion.dto.AnularFacturaRequest;
import com.clinicplatform.modules.facturacion.dto.FacturaRequest;
import com.clinicplatform.modules.facturacion.dto.FacturaResponse;
import com.clinicplatform.security.CurrentUser;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/facturas")
public class FacturacionController {

    private final FacturacionService facturacionService;

    public FacturacionController(FacturacionService facturacionService) {
        this.facturacionService = facturacionService;
    }

    @PostMapping
    public ResponseEntity<FacturaResponse> crear(@AuthenticationPrincipal CurrentUser user,
                                                    @Valid @RequestBody FacturaRequest request) {
        return ResponseEntity.status(201).body(facturacionService.crear(user.getClinicaId(), request));
    }

    @PostMapping("/{id}/anular")
    public FacturaResponse anular(@AuthenticationPrincipal CurrentUser user,
                                    @PathVariable UUID id,
                                    @Valid @RequestBody AnularFacturaRequest request) {
        return facturacionService.anular(user.getClinicaId(), id, request.motivo());
    }

    @GetMapping
    public List<FacturaResponse> listar(@AuthenticationPrincipal CurrentUser user) {
        return facturacionService.listar(user.getClinicaId());
    }
}
