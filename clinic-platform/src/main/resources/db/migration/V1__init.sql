CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE clinicas (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    nombre VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT now()
);

CREATE TABLE usuarios (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    clinica_id UUID NOT NULL REFERENCES clinicas(id),
    email VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT now()
);

CREATE TABLE pacientes (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    clinica_id UUID NOT NULL REFERENCES clinicas(id),
    nombre VARCHAR(255) NOT NULL,
    apellido VARCHAR(255) NOT NULL,
    telefono VARCHAR(50),
    fecha_nacimiento DATE,
    created_at TIMESTAMP NOT NULL DEFAULT now()
);

CREATE TABLE citas (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    clinica_id UUID NOT NULL REFERENCES clinicas(id),
    paciente_id UUID NOT NULL REFERENCES pacientes(id),
    fecha_hora TIMESTAMP NOT NULL,
    motivo TEXT,
    estado VARCHAR(50) NOT NULL DEFAULT 'pendiente',
    created_at TIMESTAMP NOT NULL DEFAULT now()
);

CREATE TABLE facturas (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    clinica_id UUID NOT NULL REFERENCES clinicas(id),
    paciente_id UUID NOT NULL REFERENCES pacientes(id),
    total NUMERIC(12,2) NOT NULL,
    estado VARCHAR(50) NOT NULL DEFAULT 'emitida',
    motivo_anulacion TEXT,
    version BIGINT NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT now()
);

CREATE TABLE factura_items (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    factura_id UUID NOT NULL REFERENCES facturas(id),
    descripcion VARCHAR(255) NOT NULL,
    cantidad NUMERIC(10,2) NOT NULL,
    precio_unitario NUMERIC(12,2) NOT NULL
);

CREATE INDEX idx_pacientes_clinica ON pacientes(clinica_id);
CREATE INDEX idx_citas_clinica ON citas(clinica_id);
CREATE INDEX idx_facturas_clinica ON facturas(clinica_id);
CREATE INDEX idx_factura_items_factura ON factura_items(factura_id);
