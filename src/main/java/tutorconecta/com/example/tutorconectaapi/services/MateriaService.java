package tutorconecta.com.example.tutorconectaapi.services;

import tutorconecta.com.example.tutorconectaapi.dtos.MateriaRequestDTO;
import tutorconecta.com.example.tutorconectaapi.dtos.MateriaResponseDTO;

import java.util.List;

public interface MateriaService {

    MateriaResponseDTO crear(MateriaRequestDTO request);

    List<MateriaResponseDTO> listarTodas();

    default List<MateriaResponseDTO> listarTodos() {
        return listarTodas();
    }

    MateriaResponseDTO buscarPorId(Long id);

    MateriaResponseDTO actualizar(Long id, MateriaRequestDTO request);

    void eliminar(Long id);
}
