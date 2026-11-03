package tutorconecta.com.example.tutorconectaapi.servicesimplements;

import tutorconecta.com.example.tutorconectaapi.servicesinterfaces.PagoService;

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

    public PagoServiceImpl(PagoRepository repository, EntityMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public PagoResponseDTO crear(PagoRequestDTO request) {
        return mapper.toPagoResponseDTO(repository.save(mapper.toPagoEntity(request)));
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
        return mapper.toPagoResponseDTO(repository.save(pago));
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
}
