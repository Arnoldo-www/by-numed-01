# Clinic Platform — Starter (Java / Spring Boot)

Producto clínico nuevo, independiente de SALUS. MVP con autenticación de personal,
pacientes, citas (presenciales y virtuales), facturación básica y **portal de paciente
con telemedicina**.

## Toda la lógica vive en Java, no en SQL

Las migraciones (`V1__init.sql`, `V2__portal_telemedicina.sql`) solo definen tablas,
tipos y restricciones de integridad (`NOT NULL`, `UNIQUE`, `FOREIGN KEY`). No hay triggers,
funciones ni stored procedures. Restricciones de integridad no son lógica de negocio —
son una red de seguridad para que el código no pueda insertar datos inválidos aunque
tenga un bug. Todo cálculo, toda regla, todo flujo (totales de factura, estados de cita,
activación de portal, generación de salas de video) vive en las clases `*Service`.

## Dos tipos de usuario, dos niveles de acceso

- **STAFF**: personal de la clínica. Acceso a `/api/pacientes`, `/api/citas`, `/api/facturas`.
- **PACIENTE**: paciente con portal activado. Acceso *solo* a `/api/portal/**`, y solo
  a sus propios datos — nunca a los de otro paciente ni a operaciones de otra clínica.

Cada JWT lleva un claim `role` (`STAFF` o `PACIENTE`). `SecurityConfig` separa ambos
mundos a nivel de rutas; `PortalController` además valida por código que cada recurso
pertenece al paciente autenticado, no solo confía en la ruta.

## Flujo de telemedicina

1. Personal crea una cita con `modalidad: "virtual"`.
2. `CitaService` le pide a `VideoConsultaService` (interfaz) que genere una sala.
3. `VideoConsultaServiceJitsiImpl` crea un enlace contra la instancia pública de Jitsi
   Meet — sin credenciales, suficiente para probar el flujo end-to-end.
4. Personal y paciente ven el mismo enlace (`salaUrl` / `GET /api/portal/citas/{id}/videoconsulta`).

**Antes de usar esto con pacientes reales:** meet.jit.si es un servicio público sin
garantías contractuales de privacidad. Evaluar Jitsi autoalojado, Daily.co, Twilio Video
o Doxy.me antes de ir a producción con datos de salud reales. El cambio de proveedor es
una nueva clase que implementa `VideoConsultaService` — no toca el resto del sistema.

## Flujo de activación del portal

1. Personal crea el paciente con su correo (`PacienteRequest.email`).
2. Personal llama `POST /api/pacientes/{id}/portal/activar` → genera una contraseña
   temporal y activa el portal.
3. **Pendiente real (no incluido):** enviar esa contraseña por correo. Hoy se devuelve
   en la respuesta de la API únicamente porque este starter no tiene proveedor de correo
   configurado — no debe quedar así en producción.
4. Paciente hace `POST /api/portal/auth/login` con su correo y esa contraseña.

## Arquitectura

```
modules/
  auth/           login/registro de personal de clínica (STAFF)
  clinica/        entidad base multi-tenant
  usuario/        personal por clínica
  paciente/       CRUD de pacientes + activación de portal
  cita/           agenda (presencial/virtual)
  facturacion/    emisión y anulación de facturas (interfaz aislada, ver README anterior)
  telemedicina/   generación de salas de videoconsulta (interfaz aislada)
  portal/         login y endpoints de autoservicio para PACIENTE
```

## Cómo correr

```bash
docker-compose up --build
```

Flyway aplica `V1` y `V2` automáticamente. Backend en `http://localhost:8080`.

**Primer paso manual:** insertar una clínica (no hay onboarding todavía):
```sql
INSERT INTO clinicas (nombre) VALUES ('Clínica Demo');
```

## Endpoints

**Personal (STAFF):**
- `POST /api/auth/register` · `POST /api/auth/login`
- `POST /api/pacientes` · `GET /api/pacientes` · `GET /api/pacientes/{id}`
- `POST /api/pacientes/{id}/portal/activar`
- `POST /api/citas` (acepta `modalidad: "presencial"|"virtual"`) · `GET /api/citas`
- `POST /api/facturas` · `GET /api/facturas` · `POST /api/facturas/{id}/anular`

**Paciente (PACIENTE), vía portal:**
- `POST /api/portal/auth/login`
- `GET /api/portal/citas` · `GET /api/portal/facturas`
- `GET /api/portal/citas/{id}/videoconsulta`

## Importante — límites de este starter

- Código escrito siguiendo convenciones estándar de Spring Boot 3 / Java 17, pero **no
  compilado en este entorno** (sin acceso a Maven Central desde este sandbox). Corran
  `mvn clean compile` como primer paso real, no como detalle opcional.
- Sin tests automatizados, sin envío real de correo, sin rate limiting.
- Telemedicina sobre Jitsi público: válido para probar el flujo, no para pacientes reales
  (ver sección de telemedicina arriba).
