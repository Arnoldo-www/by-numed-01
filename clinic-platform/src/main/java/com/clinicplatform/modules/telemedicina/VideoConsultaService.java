package com.clinicplatform.modules.telemedicina;

import java.util.UUID;

/**
 * Mismo patrón que FacturacionService: hoy VideoConsultaServiceJitsiImpl
 * genera salas contra la instancia PÚBLICA de Jitsi Meet (meet.jit.si), sin
 * necesidad de credenciales, suficiente para probar el flujo completo del
 * MVP. Cambiar de proveedor (Jitsi autoalojado, Daily.co, Twilio Video,
 * Doxy.me) implica una nueva clase que implemente esta interfaz — no se
 * toca CitaService, PortalController ni ningún otro módulo.
 *
 * IMPORTANTE: meet.jit.si es un servicio público sin garantías contractuales
 * de privacidad. No usarlo con datos reales de pacientes hasta evaluar un
 * proveedor con acuerdo de confidencialidad/tratamiento de datos de salud.
 */
public interface VideoConsultaService {
    Videoconsulta crearSala(UUID citaId);
}
