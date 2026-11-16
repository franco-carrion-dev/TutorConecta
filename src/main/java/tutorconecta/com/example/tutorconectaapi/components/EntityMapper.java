package tutorconecta.com.example.tutorconectaapi.components;

import tutorconecta.com.example.tutorconectaapi.dtos.SesionRequestDTO;
import tutorconecta.com.example.tutorconectaapi.dtos.SesionResponseDTO;
import tutorconecta.com.example.tutorconectaapi.dtos.PagoRequestDTO;
import tutorconecta.com.example.tutorconectaapi.dtos.PagoResponseDTO;
import tutorconecta.com.example.tutorconectaapi.dtos.CalificacionRequestDTO;
import tutorconecta.com.example.tutorconectaapi.dtos.CalificacionResponseDTO;
import tutorconecta.com.example.tutorconectaapi.dtos.DisponibilidadHorariaRequestDTO;
import tutorconecta.com.example.tutorconectaapi.dtos.DisponibilidadHorariaResponseDTO;
import tutorconecta.com.example.tutorconectaapi.dtos.ReporteProgresoRequestDTO;
import tutorconecta.com.example.tutorconectaapi.dtos.ReporteProgresoResponseDTO;
import tutorconecta.com.example.tutorconectaapi.dtos.EstudianteRequestDTO;
import tutorconecta.com.example.tutorconectaapi.dtos.EstudianteResponseDTO;
import tutorconecta.com.example.tutorconectaapi.dtos.MateriaRequestDTO;
import tutorconecta.com.example.tutorconectaapi.dtos.MateriaResponseDTO;
import tutorconecta.com.example.tutorconectaapi.dtos.TutorRequestDTO;
import tutorconecta.com.example.tutorconectaapi.dtos.TutorResponseDTO;
import tutorconecta.com.example.tutorconectaapi.dtos.UsuarioRequestDTO;
import tutorconecta.com.example.tutorconectaapi.dtos.UsuarioResponseDTO;
import tutorconecta.com.example.tutorconectaapi.entities.Estudiante;
import tutorconecta.com.example.tutorconectaapi.entities.Calificacion;
import tutorconecta.com.example.tutorconectaapi.entities.DisponibilidadHoraria;
import tutorconecta.com.example.tutorconectaapi.entities.Materia;
import tutorconecta.com.example.tutorconectaapi.entities.Pago;
import tutorconecta.com.example.tutorconectaapi.entities.ReporteProgreso;
import tutorconecta.com.example.tutorconectaapi.entities.Role;
import tutorconecta.com.example.tutorconectaapi.entities.Sesion;
import tutorconecta.com.example.tutorconectaapi.entities.Tutor;
import tutorconecta.com.example.tutorconectaapi.entities.Usuario;
import org.springframework.stereotype.Component;

@Component
public class EntityMapper {

    public UsuarioResponseDTO toUsuarioResponseDTO(Usuario usuario) {
        if (usuario == null) {
            return null;
        }

        UsuarioResponseDTO response = new UsuarioResponseDTO();
        response.setIdUsuario(usuario.getIdUsuario());
        response.setNombre(usuario.getNombre());
        response.setApellido(usuario.getApellido());
        response.setEmail(usuario.getEmail());
        response.setActivo(usuario.getActivo());
        response.setFechaRegistro(usuario.getFechaRegistro());
        response.setIdRol(usuario.getRol() != null ? usuario.getRol().getIdRol() : null);
        return response;
    }

    public Usuario toUsuarioEntity(UsuarioRequestDTO request) {
        if (request == null) {
            return null;
        }

        Usuario usuario = new Usuario();
        usuario.setNombre(request.getNombre());
        usuario.setApellido(request.getApellido());
        usuario.setEmail(request.getEmail());
        usuario.setPasswordHash(request.getPasswordHash());
        usuario.setActivo(request.getActivo());
        usuario.setFechaRegistro(request.getFechaRegistro());
        usuario.setRol(toRoleReference(request.getIdRol()));
        return usuario;
    }

    public TutorResponseDTO toTutorResponseDTO(Tutor tutor) {
        if (tutor == null) {
            return null;
        }

        TutorResponseDTO response = new TutorResponseDTO();
        response.setIdTutor(tutor.getIdTutor());
        response.setIdUsuario(tutor.getUsuario() != null ? tutor.getUsuario().getIdUsuario() : null);
        response.setBiografia(tutor.getBiografia());
        response.setTarifaPorHora(tutor.getTarifaPorHora());
        response.setVerificado(tutor.getVerificado());
        response.setCalificacionPromedio(tutor.getCalificacionPromedio());
        response.setIdMaterias(tutor.getMaterias() == null ? java.util.List.of()
                : tutor.getMaterias().stream().map(Materia::getIdMateria).sorted().toList());
        return response;
    }

    public Tutor toTutorEntity(TutorRequestDTO request) {
        if (request == null) {
            return null;
        }

        Tutor tutor = new Tutor();
        tutor.setIdTutor(request.getIdUsuario());
        tutor.setUsuario(toUsuarioReference(request.getIdUsuario()));
        tutor.setBiografia(request.getBiografia());
        tutor.setTarifaPorHora(request.getTarifaPorHora());
        tutor.setVerificado(request.getVerificado());
        tutor.setCalificacionPromedio(request.getCalificacionPromedio());
        java.util.Set<Materia> materias = new java.util.HashSet<>();
        if (request.getIdMaterias() != null) {
            request.getIdMaterias().forEach(id -> materias.add(toMateriaReference(id)));
        }
        tutor.setMaterias(materias);
        return tutor;
    }

    public EstudianteResponseDTO toEstudianteResponseDTO(Estudiante estudiante) {
        if (estudiante == null) {
            return null;
        }

        EstudianteResponseDTO response = new EstudianteResponseDTO();
        response.setIdEstudiante(estudiante.getIdEstudiante());
        response.setIdUsuario(estudiante.getUsuario() != null ? estudiante.getUsuario().getIdUsuario() : null);
        response.setPresupuestoMaximo(estudiante.getPresupuestoMaximo());
        return response;
    }

    public Estudiante toEstudianteEntity(EstudianteRequestDTO request) {
        if (request == null) {
            return null;
        }

        Estudiante estudiante = new Estudiante();
        estudiante.setIdEstudiante(request.getIdUsuario());
        estudiante.setUsuario(toUsuarioReference(request.getIdUsuario()));
        estudiante.setPresupuestoMaximo(request.getPresupuestoMaximo());
        return estudiante;
    }

    public MateriaResponseDTO toMateriaResponseDTO(Materia materia) {
        if (materia == null) {
            return null;
        }

        MateriaResponseDTO response = new MateriaResponseDTO();
        response.setIdMateria(materia.getIdMateria());
        response.setNombre(materia.getNombre());
        response.setDescripcion(materia.getDescripcion());
        response.setNivelEducativo(materia.getNivelEducativo());
        return response;
    }

    public Materia toMateriaEntity(MateriaRequestDTO request) {
        if (request == null) {
            return null;
        }

        Materia materia = new Materia();
        materia.setNombre(request.getNombre());
        materia.setDescripcion(request.getDescripcion());
        materia.setNivelEducativo(request.getNivelEducativo());
        return materia;
    }

    public SesionResponseDTO toSesionResponseDTO(Sesion sesion) {
        if (sesion == null) {
            return null;
        }

        SesionResponseDTO response = new SesionResponseDTO();
        response.setIdSesion(sesion.getIdSesion());
        response.setIdEstudiante(
                sesion.getEstudiante() != null ? sesion.getEstudiante().getIdEstudiante() : null);
        response.setIdTutor(sesion.getTutor() != null ? sesion.getTutor().getIdTutor() : null);
        response.setIdMateria(
                sesion.getMateria() != null ? sesion.getMateria().getIdMateria() : null);
        response.setFechaInicio(sesion.getFechaInicio());
        response.setFechaFin(sesion.getFechaFin());
        response.setEstado(sesion.getEstado());
        response.setEnlaceJitsi(sesion.getEnlaceJitsi());
        response.setFechaCreacion(sesion.getFechaCreacion());
        return response;
    }

    public Sesion toSesionEntity(SesionRequestDTO request) {
        if (request == null) {
            return null;
        }

        Sesion sesion = new Sesion();
        sesion.setEstudiante(toEstudianteReference(request.getIdEstudiante()));
        sesion.setTutor(toTutorReference(request.getIdTutor()));
        sesion.setMateria(toMateriaReference(request.getIdMateria()));
        sesion.setFechaInicio(request.getFechaInicio());
        sesion.setFechaFin(request.getFechaFin());
        sesion.setEstado(request.getEstado());
        sesion.setEnlaceJitsi(request.getEnlaceJitsi());
        sesion.setFechaCreacion(request.getFechaCreacion());
        return sesion;
    }

    public PagoResponseDTO toPagoResponseDTO(Pago pago) {
        PagoResponseDTO response = new PagoResponseDTO();
        response.setIdPago(pago.getIdPago());
        response.setIdSesion(pago.getSesion() != null ? pago.getSesion().getIdSesion() : null);
        response.setNombreTutor(nombreTutor(pago.getSesion() != null ? pago.getSesion().getTutor() : null));
        response.setMonto(pago.getMonto());
        response.setFechaPago(pago.getFechaPago());
        response.setEstado(pago.getEstado());
        response.setMetodoPago(pago.getMetodoPago());
        return response;
    }

    public Pago toPagoEntity(PagoRequestDTO request) {
        Pago pago = new Pago();
        pago.setSesion(toSesionReference(request.getIdSesion()));
        pago.setMonto(request.getMonto());
        pago.setFechaPago(request.getFechaPago());
        pago.setEstado(request.getEstado());
        pago.setMetodoPago(request.getMetodoPago());
        return pago;
    }

    public CalificacionResponseDTO toCalificacionResponseDTO(Calificacion calificacion) {
        CalificacionResponseDTO response = new CalificacionResponseDTO();
        response.setIdCalificacion(calificacion.getIdCalificacion());
        response.setIdSesion(calificacion.getSesion() != null ? calificacion.getSesion().getIdSesion() : null);
        response.setIdTutor(calificacion.getTutor() != null ? calificacion.getTutor().getIdTutor() : null);
        response.setNombreTutor(nombreTutor(calificacion.getTutor()));
        response.setPuntuacion(calificacion.getPuntuacion());
        response.setComentario(calificacion.getComentario());
        response.setFechaCalificacion(calificacion.getFechaCalificacion());
        return response;
    }

    public Calificacion toCalificacionEntity(CalificacionRequestDTO request) {
        Calificacion calificacion = new Calificacion();
        calificacion.setSesion(toSesionReference(request.getIdSesion()));
        calificacion.setTutor(toTutorReference(request.getIdTutor()));
        calificacion.setPuntuacion(request.getPuntuacion());
        calificacion.setComentario(request.getComentario());
        calificacion.setFechaCalificacion(request.getFechaCalificacion());
        return calificacion;
    }

    public DisponibilidadHorariaResponseDTO toDisponibilidadResponseDTO(DisponibilidadHoraria disponibilidad) {
        DisponibilidadHorariaResponseDTO response = new DisponibilidadHorariaResponseDTO();
        response.setIdDisponibilidad(disponibilidad.getIdDisponibilidad());
        response.setIdTutor(disponibilidad.getTutor() != null ? disponibilidad.getTutor().getIdTutor() : null);
        response.setNombreTutor(nombreTutor(disponibilidad.getTutor()));
        response.setDiaSemana(disponibilidad.getDiaSemana());
        response.setHoraInicio(disponibilidad.getHoraInicio());
        response.setHoraFin(disponibilidad.getHoraFin());
        response.setDisponible(disponibilidad.getDisponible());
        return response;
    }

    public DisponibilidadHoraria toDisponibilidadEntity(DisponibilidadHorariaRequestDTO request) {
        DisponibilidadHoraria disponibilidad = new DisponibilidadHoraria();
        disponibilidad.setTutor(toTutorReference(request.getIdTutor()));
        disponibilidad.setDiaSemana(request.getDiaSemana());
        disponibilidad.setHoraInicio(request.getHoraInicio());
        disponibilidad.setHoraFin(request.getHoraFin());
        disponibilidad.setDisponible(request.getDisponible());
        return disponibilidad;
    }

    public ReporteProgresoResponseDTO toReporteResponseDTO(ReporteProgreso reporte) {
        ReporteProgresoResponseDTO response = new ReporteProgresoResponseDTO();
        response.setIdReporte(reporte.getIdReporte());
        response.setIdSesion(reporte.getSesion() != null ? reporte.getSesion().getIdSesion() : null);
        response.setIdTutor(reporte.getTutor() != null ? reporte.getTutor().getIdTutor() : null);
        response.setNombreTutor(nombreTutor(reporte.getTutor()));
        response.setFechaReporte(reporte.getFechaReporte());
        response.setProgreso(reporte.getProgreso());
        response.setObservaciones(reporte.getObservaciones());
        return response;
    }

    public ReporteProgreso toReporteEntity(ReporteProgresoRequestDTO request) {
        ReporteProgreso reporte = new ReporteProgreso();
        reporte.setSesion(toSesionReference(request.getIdSesion()));
        reporte.setTutor(toTutorReference(request.getIdTutor()));
        reporte.setFechaReporte(request.getFechaReporte());
        reporte.setProgreso(request.getProgreso());
        reporte.setObservaciones(request.getObservaciones());
        return reporte;
    }

    private Role toRoleReference(Long idRol) {
        if (idRol == null) {
            return null;
        }
        Role role = new Role();
        role.setIdRol(idRol);
        return role;
    }

    private Usuario toUsuarioReference(Long idUsuario) {
        if (idUsuario == null) {
            return null;
        }
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(idUsuario);
        return usuario;
    }

    private Estudiante toEstudianteReference(Long idEstudiante) {
        if (idEstudiante == null) {
            return null;
        }
        Estudiante estudiante = new Estudiante();
        estudiante.setIdEstudiante(idEstudiante);
        return estudiante;
    }

    private Tutor toTutorReference(Long idTutor) {
        if (idTutor == null) {
            return null;
        }
        Tutor tutor = new Tutor();
        tutor.setIdTutor(idTutor);
        return tutor;
    }

    private Materia toMateriaReference(Long idMateria) {
        if (idMateria == null) {
            return null;
        }
        Materia materia = new Materia();
        materia.setIdMateria(idMateria);
        return materia;
    }

    private Sesion toSesionReference(Long idSesion) {
        if (idSesion == null) {
            return null;
        }
        Sesion sesion = new Sesion();
        sesion.setIdSesion(idSesion);
        return sesion;
    }

    private String nombreTutor(Tutor tutor) {
        if (tutor == null || tutor.getUsuario() == null) {
            return null;
        }
        return tutor.getUsuario().getNombre() + " " + tutor.getUsuario().getApellido();
    }
}
