package com.clinicplatform.modules.portal;

import com.clinicplatform.modules.portal.dto.PortalLoginRequest;
import com.clinicplatform.modules.portal.dto.PortalLoginResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/portal/auth")
public class PortalAuthController {

    private final PortalAuthService portalAuthService;

    public PortalAuthController(PortalAuthService portalAuthService) {
        this.portalAuthService = portalAuthService;
    }

    @PostMapping("/login")
    public PortalLoginResponse login(@Valid @RequestBody PortalLoginRequest request) {
        return portalAuthService.login(request);
    }
}
