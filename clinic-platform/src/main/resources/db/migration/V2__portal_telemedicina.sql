-- Solo estructura e integridad. Ningún cálculo ni regla de negocio vive aquí.

ALTER TABLE pacientes ADD COLUMN email VARCHAR(255) UNIQUE;
ALTER TABLE pacientes ADD COLUMN password_hash VARCHAR(255);
ALTER TABLE pacientes ADD COLUMN portal_activo BOOLEAN NOT NULL DEFAULT false;

ALTER TABLE citas ADD COLUMN modalidad VARCHAR(20) NOT NULL DEFAULT 'presencial';

CREATE TABLE videoconsultas (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    cita_id UUID NOT NULL UNIQUE REFERENCES citas(id),
    proveedor VARCHAR(50) NOT NULL,
    sala_id VARCHAR(255) NOT NULL,
    url VARCHAR(500) NOT NULL,
    estado VARCHAR(50) NOT NULL DEFAULT 'programada',
    created_at TIMESTAMP NOT NULL DEFAULT now()
);

CREATE INDEX idx_videoconsultas_cita ON videoconsultas(cita_id);
