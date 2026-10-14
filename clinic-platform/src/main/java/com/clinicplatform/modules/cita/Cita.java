package com.clinicplatform.modules.cita;

import com.clinicplatform.modules.clinica.Clinica;
import com.clinicplatform.modules.paciente.Paciente;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "citas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cita {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "clinica_id", nullable = false)
    private Clinica clinica;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paciente_id", nullable = false)
    private Paciente paciente;

    @Column(name = "fecha_hora", nullable = false)
    private LocalDateTime fechaHora;

    private String motivo;

    @Builder.Default
    private String estado = "pendiente";

    // "presencial" o "virtual". Si es virtual, CitaService pide a
    // VideoConsultaService que genere una sala al crear la cita.
    @Builder.Default
    @Column(nullable = false)
    private String modalidad = "presencial";

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}
