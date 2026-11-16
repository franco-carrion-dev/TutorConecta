package tutorconecta.com.example.tutorconectaapi.services;

import tutorconecta.com.example.tutorconectaapi.components.EntityMapper;
import tutorconecta.com.example.tutorconectaapi.dtos.MateriaRequestDTO;
import tutorconecta.com.example.tutorconectaapi.dtos.MateriaResponseDTO;
import tutorconecta.com.example.tutorconectaapi.entities.Materia;
import tutorconecta.com.example.tutorconectaapi.repositories.MateriaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MateriaServiceImpl implements MateriaService {
    private final MateriaRepository repository;
    private final EntityMapper mapper;

    public MateriaServiceImpl(MateriaRepository repository, EntityMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public MateriaResponseDTO crear(MateriaRequestDTO request) {
        return mapper.toMateriaResponseDTO(repository.save(mapper.toMateriaEntity(request)));
    }

    @Override
    public List<MateriaResponseDTO> listarTodas() {
        return repository.findAll().stream().map(mapper::toMateriaResponseDTO).toList();
    }

    @Override
    public MateriaResponseDTO buscarPorId(Long id) {
        return repository.findById(id).map(mapper::toMateriaResponseDTO)
                .orElseThrow(() -> new EntityNotFoundException("No existe una materia con ID: " + id));
    }

    @Override
    public MateriaResponseDTO actualizar(Long id, MateriaRequestDTO request) {
        buscarPorId(id);
        Materia materia = mapper.toMateriaEntity(request);
        materia.setIdMateria(id);
        return mapper.toMateriaResponseDTO(repository.save(materia));
    }

    @Override
    public void eliminar(Long id) {
        Materia materia = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No existe una materia con ID: " + id));
        repository.delete(materia);
    }
}
