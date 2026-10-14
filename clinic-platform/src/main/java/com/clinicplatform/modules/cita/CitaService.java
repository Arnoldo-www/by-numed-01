package com.clinicplatform.modules.cita;

import com.clinicplatform.common.exception.AppException;
import com.clinicplatform.modules.cita.dto.CitaRequest;
import com.clinicplatform.modules.cita.dto.CitaResponse;
import com.clinicplatform.modules.clinica.ClinicaRepository;
import com.clinicplatform.modules.paciente.PacienteRepository;
import com.clinicplatform.modules.telemedicina.VideoConsultaService;
import com.clinicplatform.modules.telemedicina.Videoconsulta;
import com.clinicplatform.modules.telemedicina.VideoconsultaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CitaService {

    private final CitaRepository citaRepository;
    private final ClinicaRepository clinicaRepository;
    private final PacienteRepository pacienteRepository;
    private final VideoConsultaService videoConsultaService;
    private final VideoconsultaRepository videoconsultaRepository;

    public CitaService(CitaRepository citaRepository,
                        ClinicaRepository clinicaRepository,
                        PacienteRepository pacienteRepository,
                        VideoConsultaService videoConsultaService,
                        VideoconsultaRepository videoconsultaRepository) {
        this.citaRepository = citaRepository;
        this.clinicaRepository = clinicaRepository;
        this.pacienteRepository = pacienteRepository;
        this.videoConsultaService = videoConsultaService;
        this.videoconsultaRepository = videoconsultaRepository;
    }

    public CitaResponse crear(UUID clinicaId, CitaRequest request) {
        var paciente = pacienteRepository.findByIdAndClinicaId(request.pacienteId(), clinicaId)
                .orElseThrow(() -> new AppException("Paciente no encontrado", HttpStatus.BAD_REQUEST));

        String modalidad = (request.modalidad() == null || request.modalidad().isBlank())
                ? "presencial" : request.modalidad();

        Cita cita = Cita.builder()
                .clinica(clinicaRepository.getReferenceById(clinicaId))
                .paciente(paciente)
                .fechaHora(request.fechaHora())
                .motivo(request.motivo())
                .estado("pendiente")
                .modalidad(modalidad)
                .build();

        Cita saved = citaRepository.save(cita);

        String salaUrl = null;
        if ("virtual".equals(modalidad)) {
            Videoconsulta videoconsulta = videoConsultaService.crearSala(saved.getId());
            salaUrl = videoconsulta.getUrl();
        }

        return toResponse(saved, salaUrl);
    }

    public List<CitaResponse> listar(UUID clinicaId) {
        return citaRepository.findByClinicaIdOrderByFechaHora(clinicaId)
                .stream()
                .map(c -> toResponse(c, videoconsultaRepository.findByCitaId(c.getId()).map(Videoconsulta::getUrl).orElse(null)))
                .toList();
    }

    private CitaResponse toResponse(Cita c, String salaUrl) {
        return new CitaResponse(
                c.getId(),
                c.getPaciente().getId(),
                c.getPaciente().getNombre() + " " + c.getPaciente().getApellido(),
                c.getFechaHora(),
                c.getMotivo(),
                c.getEstado(),
                c.getModalidad(),
                salaUrl
        );
    }
}
