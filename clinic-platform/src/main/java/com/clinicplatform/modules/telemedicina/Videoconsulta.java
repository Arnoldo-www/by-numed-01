package com.clinicplatform.modules.telemedicina;

import com.clinicplatform.modules.cita.Cita;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "videoconsultas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Videoconsulta {

    @Id
    @GeneratedValue
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cita_id", nullable = false, unique = true)
    private Cita cita;

    @Column(nullable = false)
    private String proveedor;

    @Column(name = "sala_id", nullable = false)
    private String salaId;

    @Column(nullable = false, length = 500)
    private String url;

    @Builder.Default
    private String estado = "programada";

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}
