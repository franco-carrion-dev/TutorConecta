package tutorconecta.com.example.tutorconectaapi.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tutorconecta.com.example.tutorconectaapi.entities.Pago;
import tutorconecta.com.example.tutorconectaapi.entities.Sesion;
import tutorconecta.com.example.tutorconectaapi.entities.Usuario;
import tutorconecta.com.example.tutorconectaapi.repositories.PagoRepository;
import tutorconecta.com.example.tutorconectaapi.repositories.SesionRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Notificaciones por correo (JavaMail + SMTP Gmail) y registro de auditoría.
 * Si MAIL_USERNAME no está configurado solo deja el log de auditoría.
 */
@Service
public class NotificacionService {
    private static final Logger AUDIT = LoggerFactory.getLogger("AUDITORIA");
    private static final DateTimeFormatter F = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private final ObjectProvider<JavaMailSender> mailSender;
    private final SesionRepository sesionRepository;
    private final PagoRepository pagoRepository;

    /** Correo que aparece como remitente. En Brevo debe ser un remitente verificado (app.mail.from). */
    @Value("${app.mail.from:${spring.mail.username:}}")
    private String remitente;

    public NotificacionService(ObjectProvider<JavaMailSender> mailSender,
                               SesionRepository sesionRepository, PagoRepository pagoRepository) {
        this.mailSender = mailSender;
        this.sesionRepository = sesionRepository;
        this.pagoRepository = pagoRepository;
    }

    @Async
    @Transactional(readOnly = true)
    public void sesionCambio(Long idSesion, String accion) {
        try {
            Sesion s = sesionRepository.findById(idSesion).orElse(null);
            if (s == null) return;
            AUDIT.info("SESION id={} accion={} estado={}", idSesion, accion, s.getEstado());
            Usuario est = s.getEstudiante() != null ? s.getEstudiante().getUsuario() : null;
            Usuario tut = s.getTutor() != null ? s.getTutor().getUsuario() : null;
            String materia = s.getMateria() != null ? s.getMateria().getNombre() : "";
            String texto = "Sesión de " + materia + " el " + s.getFechaInicio().format(F)
                    + "\nEstado: " + s.getEstado()
                    + (s.getEnlaceJitsi() != null ? "\nEnlace de videollamada: " + s.getEnlaceJitsi() : "");
            if (est != null) enviar(est.getEmail(), "TutorConecta - Sesión " + accion, "Hola " + est.getNombre() + ",\n\n" + texto);
            if (tut != null) enviar(tut.getEmail(), "TutorConecta - Sesión " + accion, "Hola " + tut.getNombre() + ",\n\n" + texto);
        } catch (Exception e) {
            AUDIT.warn("No se pudo notificar la sesion {}: {}", idSesion, e.getMessage());
        }
    }

    @Async
    @Transactional(readOnly = true)
    public void pagoCambio(Long idPago, String accion) {
        try {
            Pago p = pagoRepository.findById(idPago).orElse(null);
            if (p == null) return;
            AUDIT.info("PAGO id={} accion={} sesion={} monto={} estado={} metodo={}", idPago, accion,
                    p.getSesion() != null ? p.getSesion().getIdSesion() : null, p.getMonto(), p.getEstado(), p.getMetodoPago());
            Usuario est = p.getSesion() != null && p.getSesion().getEstudiante() != null
                    ? p.getSesion().getEstudiante().getUsuario() : null;
            if (est != null) {
                enviar(est.getEmail(), "TutorConecta - Pago " + accion,
                        "Hola " + est.getNombre() + ",\n\nTu pago de S/ " + p.getMonto() + " está en estado: " + p.getEstado()
                                + "\nSesión #" + p.getSesion().getIdSesion());
            }
        } catch (Exception e) {
            AUDIT.warn("No se pudo notificar el pago {}: {}", idPago, e.getMessage());
        }
    }

    /** Recordatorio: cada hora avisa de las sesiones agendadas que empiezan dentro de 23-24 h. */
    @Scheduled(cron = "0 0 * * * *")
    @Transactional(readOnly = true)
    public void recordatorios() {
        LocalDateTime desde = LocalDateTime.now().plusHours(23);
        LocalDateTime hasta = desde.plusHours(1);
        sesionRepository.findByEstado("agendada").stream()
                .filter(s -> s.getFechaInicio() != null
                        && !s.getFechaInicio().isBefore(desde) && s.getFechaInicio().isBefore(hasta))
                .forEach(s -> sesionCambio(s.getIdSesion(), "mañana (recordatorio)"));
    }

    private void enviar(String para, String asunto, String texto) {
        JavaMailSender sender = mailSender.getIfAvailable();
        if (sender == null || remitente == null || remitente.isBlank() || para == null) return;
        try {
            SimpleMailMessage m = new SimpleMailMessage();
            m.setFrom(remitente);
            m.setTo(para);
            m.setSubject(asunto);
            m.setText(texto + "\n\n— TutorConecta");
            sender.send(m);
        } catch (Exception e) {
            AUDIT.warn("Fallo envío de correo a {}: {}", para, e.getMessage());
        }
    }
}
