package tutorconecta.com.example.tutorconectaapi.services;

import java.math.BigDecimal;
import tutorconecta.com.example.tutorconectaapi.dtos.MontoPorMetodoDTO;
import tutorconecta.com.example.tutorconectaapi.dtos.MontoPorMesDTO;

import tutorconecta.com.example.tutorconectaapi.components.EntityMapper;
import tutorconecta.com.example.tutorconectaapi.dtos.PagoRequestDTO;
import tutorconecta.com.example.tutorconectaapi.dtos.PagoResponseDTO;
import tutorconecta.com.example.tutorconectaapi.entities.Pago;
import tutorconecta.com.example.tutorconectaapi.repositories.PagoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PagoServiceImpl implements PagoService {
    private final PagoRepository repository;
    private final EntityMapper mapper;
    private final NotificacionService notificacionService;

    public PagoServiceImpl(PagoRepository repository, EntityMapper mapper,
                           NotificacionService notificacionService) {
        this.repository = repository;
        this.mapper = mapper;
        this.notificacionService = notificacionService;
    }

    @Override
    public PagoResponseDTO crear(PagoRequestDTO request) {
        Pago guardado = repository.save(mapper.toPagoEntity(request));
        notificacionService.pagoCambio(guardado.getIdPago(), "registrado");
        return mapper.toPagoResponseDTO(guardado);
    }

    @Override
    public List<PagoResponseDTO> listarTodos() {
        return repository.findAll().stream().map(mapper::toPagoResponseDTO).toList();
    }

    @Override
    public PagoResponseDTO buscarPorId(Long id) {
        return repository.findById(id).map(mapper::toPagoResponseDTO)
                .orElseThrow(() -> new EntityNotFoundException("No existe un pago con ID: " + id));
    }

    @Override
    public PagoResponseDTO actualizar(Long id, PagoRequestDTO request) {
        buscarPorId(id);
        Pago pago = mapper.toPagoEntity(request);
        pago.setIdPago(id);
        Pago guardado = repository.save(pago);
        notificacionService.pagoCambio(id, "actualizado");
        return mapper.toPagoResponseDTO(guardado);
    }

    @Override
    public void eliminar(Long id) {
        Pago pago = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No existe un pago con ID: " + id));
        repository.delete(pago);
    }

    @Override
    public List<PagoResponseDTO> buscarPorEstado(String estado) {
        return repository.findByEstado(estado).stream()
                .map(mapper::toPagoResponseDTO)
                .toList();
    }

    @Override
    public List<PagoResponseDTO> buscarPorTutorYEstado(Long idTutor, String estado) {
        return repository.findPagosByTutorAndEstado(idTutor, estado).stream()
                .map(mapper::toPagoResponseDTO)
                .toList();
    }

@Override
    public List<MontoPorMetodoDTO> reportePorMetodo() {
        return repository.montoPorMetodoPago().stream()
                .map(f -> new MontoPorMetodoDTO((String) f[0], ((Number) f[1]).longValue(), new BigDecimal(f[2].toString())))
                .toList();
    }

    @Override
    public List<MontoPorMesDTO> reportePorMes(int anio) {
        return repository.montoPorMes(anio).stream()
                .map(f -> new MontoPorMesDTO(((Number) f[0]).intValue(), ((Number) f[1]).longValue(), new BigDecimal(f[2].toString())))
                .toList();
    }
}
