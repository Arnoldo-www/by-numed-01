package com.clinicplatform.modules.telemedicina;

import com.clinicplatform.common.exception.AppException;
import com.clinicplatform.modules.cita.Cita;
import com.clinicplatform.modules.cita.CitaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class VideoConsultaServiceJitsiImpl implements VideoConsultaService {

    private final VideoconsultaRepository videoconsultaRepository;
    private final CitaRepository citaRepository;

    public VideoConsultaServiceJitsiImpl(VideoconsultaRepository videoconsultaRepository, CitaRepository citaRepository) {
        this.videoconsultaRepository = videoconsultaRepository;
        this.citaRepository = citaRepository;
    }

    @Override
    public Videoconsulta crearSala(UUID citaId) {
        Cita cita = citaRepository.findById(citaId)
                .orElseThrow(() -> new AppException("Cita no encontrada", HttpStatus.BAD_REQUEST));

        String salaId = "clinicplatform-" + citaId;
        String url = "https://meet.jit.si/" + salaId;

        Videoconsulta videoconsulta = Videoconsulta.builder()
                .cita(cita)
                .proveedor("jitsi")
                .salaId(salaId)
                .url(url)
                .estado("programada")
                .build();

        return videoconsultaRepository.save(videoconsulta);
    }
}
