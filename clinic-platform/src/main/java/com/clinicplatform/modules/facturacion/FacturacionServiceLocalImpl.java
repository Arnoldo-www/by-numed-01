package com.clinicplatform.modules.facturacion;

import com.clinicplatform.common.exception.AppException;
import com.clinicplatform.modules.clinica.ClinicaRepository;
import com.clinicplatform.modules.facturacion.dto.FacturaRequest;
import com.clinicplatform.modules.facturacion.dto.FacturaResponse;
import com.clinicplatform.modules.paciente.Paciente;
import com.clinicplatform.modules.paciente.PacienteRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public class FacturacionServiceLocalImpl implements FacturacionService {

    private final FacturaRepository facturaRepository;
    private final ClinicaRepository clinicaRepository;
    private final PacienteRepository pacienteRepository;

    public FacturacionServiceLocalImpl(FacturaRepository facturaRepository,
                                        ClinicaRepository clinicaRepository,
                                        PacienteRepository pacienteRepository) {
        this.facturaRepository = facturaRepository;
        this.clinicaRepository = clinicaRepository;
        this.pacienteRepository = pacienteRepository;
    }

    @Override
    @Transactional
    public FacturaResponse crear(UUID clinicaId, FacturaRequest request) {
        Paciente paciente = pacienteRepository.findByIdAndClinicaId(request.pacienteId(), clinicaId)
                .orElseThrow(() -> new AppException("Paciente no encontrado", HttpStatus.BAD_REQUEST));

        Factura factura = Factura.builder()
                .clinica(clinicaRepository.getReferenceById(clinicaId))
                .paciente(paciente)
                .estado("emitida")
                .total(BigDecimal.ZERO)
                .build();

        BigDecimal total = BigDecimal.ZERO;
        for (var itemReq : request.items()) {
            BigDecimal subtotal = itemReq.precioUnitario().multiply(itemReq.cantidad());
            total = total.add(subtotal);
            factura.addItem(FacturaItem.builder()
                    .descripcion(itemReq.descripcion())
                    .cantidad(itemReq.cantidad())
                    .precioUnitario(itemReq.precioUnitario())
                    .build());
        }
        factura.setTotal(total);

        Factura saved = facturaRepository.save(factura);
        return toResponse(saved);
    }

    @Override
    @Transactional
    public FacturaResponse anular(UUID clinicaId, UUID facturaId, String motivo) {
        Factura factura = facturaRepository.findByIdAndClinicaId(facturaId, clinicaId)
                .orElseThrow(() -> new AppException("Factura no encontrada", HttpStatus.NOT_FOUND));

        if ("anulada".equals(factura.getEstado())) {
            throw new AppException("La factura ya está anulada", HttpStatus.CONFLICT);
        }

        factura.setEstado("anulada");
        factura.setMotivoAnulacion(motivo);
        Factura saved = facturaRepository.save(factura);
        return toResponse(saved);
    }

    @Override
    public List<FacturaResponse> listar(UUID clinicaId) {
        return facturaRepository.findByClinicaIdOrderByCreatedAtDesc(clinicaId)
                .stream().map(this::toResponse).toList();
    }

    private FacturaResponse toResponse(Factura f) {
        return new FacturaResponse(f.getId(), f.getPaciente().getId(), f.getTotal(), f.getEstado(), f.getMotivoAnulacion());
    }
}
