package com.clinicplatform.modules.facturacion;

import com.clinicplatform.modules.clinica.Clinica;
import com.clinicplatform.modules.paciente.Paciente;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "facturas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Factura {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "clinica_id", nullable = false)
    private Clinica clinica;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paciente_id", nullable = false)
    private Paciente paciente;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal total;

    @Builder.Default
    private String estado = "emitida";

    @Column(name = "motivo_anulacion")
    private String motivoAnulacion;

    @OneToMany(mappedBy = "factura", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<FacturaItem> items = new ArrayList<>();

    // Bloqueo optimista: si dos usuarios intentan modificar la misma factura
    // al mismo tiempo, la segunda escritura falla con una excepción en vez
    // de sobrescribir en silencio los datos del primero.
    @Version
    private Long version;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public void addItem(FacturaItem item) {
        item.setFactura(this);
        items.add(item);
    }
}
