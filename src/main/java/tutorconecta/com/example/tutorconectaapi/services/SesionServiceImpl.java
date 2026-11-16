package tutorconecta.com.example.tutorconectaapi.services;

import tutorconecta.com.example.tutorconectaapi.components.EntityMapper;
import tutorconecta.com.example.tutorconectaapi.dtos.SesionRequestDTO;
import tutorconecta.com.example.tutorconectaapi.dtos.SesionResponseDTO;
import tutorconecta.com.example.tutorconectaapi.entities.Sesion;
import tutorconecta.com.example.tutorconectaapi.repositories.SesionRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SesionServiceImpl implements SesionService {
    private final SesionRepository sesionRepository;
    private final EntityMapper entityMapper;
    private final NotificacionService notificacionService;

    public SesionServiceImpl(SesionRepository sesionRepository, EntityMapper entityMapper,
                             NotificacionService notificacionService) {
        this.sesionRepository = sesionRepository;
        this.entityMapper = entityMapper;
        this.notificacionService = notificacionService;
    }

    @Override
    public SesionResponseDTO crear(SesionRequestDTO request) {
        Sesion sesion = entityMapper.toSesionEntity(request);
        if (sesion.getFechaCreacion() == null) sesion.setFechaCreacion(java.time.LocalDateTime.now());
        Sesion guardada = sesionRepository.save(sesion);
        notificacionService.sesionCambio(guardada.getIdSesion(), "registrada");
        return entityMapper.toSesionResponseDTO(guardada);
    }

    @Override
    public List<SesionResponseDTO> listarTodos() {
        return sesionRepository.findAll().stream()
                .map(entityMapper::toSesionResponseDTO)
                .toList();
    }

    @Override
    public SesionResponseDTO buscarPorId(Long id) {
        return sesionRepository.findById(id)
                .map(entityMapper::toSesionResponseDTO)
                .orElseThrow(() -> new EntityNotFoundException("No existe una sesion con ID: " + id));
    }

    @Override
    public SesionResponseDTO actualizar(Long id, SesionRequestDTO request) {
        buscarPorId(id);
        Sesion sesion = entityMapper.toSesionEntity(request);
        sesion.setIdSesion(id);
        return entityMapper.toSesionResponseDTO(sesionRepository.save(sesion));
    }

    @Override
    public void updateEstadoSesion(Long id, String estado) {
        Sesion sesion = sesionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No existe una sesion con ID: " + id));
        sesion.setEstado(estado);
        sesionRepository.save(sesion);
        notificacionService.sesionCambio(id, estado);
    }

    @Override
    public void eliminar(Long id) {
        if (!sesionRepository.existsById(id)) {
            throw new EntityNotFoundException("No existe una sesion con ID: " + id);
        }
        sesionRepository.deleteById(id);
    }

}
