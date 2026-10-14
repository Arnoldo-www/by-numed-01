package com.clinicplatform.modules.portal;

import com.clinicplatform.common.exception.AppException;
import com.clinicplatform.modules.cita.CitaRepository;
import com.clinicplatform.modules.cita.dto.CitaResponse;
import com.clinicplatform.modules.facturacion.FacturaRepository;
import com.clinicplatform.modules.facturacion.dto.FacturaResponse;
import com.clinicplatform.modules.telemedicina.Videoconsulta;
import com.clinicplatform.modules.telemedicina.VideoconsultaRepository;
import com.clinicplatform.modules.telemedicina.dto.VideoconsultaEnlaceResponse;
import com.clinicplatform.security.CurrentUser;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

// Todo lo que hay aquí solo devuelve datos del PROPIO paciente autenticado
// (user.getId() = paciente.id para tokens con role PACIENTE). Nunca recibe
// ni confía en un id de paciente que venga del cliente.
@RestController
@RequestMapping("/api/portal")
public class PortalController {

    private final CitaRepository citaRepository;
    private final FacturaRepository facturaRepository;
    private final VideoconsultaRepository videoconsultaRepository;

    public PortalController(CitaRepository citaRepository, FacturaRepository facturaRepository,
                             VideoconsultaRepository videoconsultaRepository) {
        this.citaRepository = citaRepository;
        this.facturaRepository = facturaRepository;
        this.videoconsultaRepository = videoconsultaRepository;
    }

    @GetMapping("/citas")
    public List<CitaResponse> misCitas(@AuthenticationPrincipal CurrentUser user) {
        return citaRepository.findByPacienteIdOrderByFechaHora(user.getId()).stream()
                .map(c -> new CitaResponse(
                        c.getId(),
                        c.getPaciente().getId(),
                        c.getPaciente().getNombre() + " " + c.getPaciente().getApellido(),
                        c.getFechaHora(),
                        c.getMotivo(),
                        c.getEstado(),
                        c.getModalidad(),
                        videoconsultaRepository.findByCitaId(c.getId()).map(Videoconsulta::getUrl).orElse(null)
                ))
                .toList();
    }

    @GetMapping("/facturas")
    public List<FacturaResponse> misFacturas(@AuthenticationPrincipal CurrentUser user) {
        return facturaRepository.findByPacienteIdOrderByCreatedAtDesc(user.getId()).stream()
                .map(f -> new FacturaResponse(f.getId(), f.getPaciente().getId(), f.getTotal(), f.getEstado(), f.getMotivoAnulacion()))
                .toList();
    }

    @GetMapping("/citas/{id}/videoconsulta")
    public VideoconsultaEnlaceResponse enlaceVideoconsulta(@AuthenticationPrincipal CurrentUser user, @PathVariable UUID id) {
        var cita = citaRepository.findById(id)
                .orElseThrow(() -> new AppException("Cita no encontrada", HttpStatus.NOT_FOUND));

        if (!cita.getPaciente().getId().equals(user.getId())) {
            throw new AppException("No autorizado", HttpStatus.FORBIDDEN);
        }

        String url = videoconsultaRepository.findByCitaId(id)
                .map(Videoconsulta::getUrl)
                .orElseThrow(() -> new AppException("Esta cita no tiene videoconsulta programada", HttpStatus.NOT_FOUND));

        return new VideoconsultaEnlaceResponse(url);
    }
}
