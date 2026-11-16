package tutorconecta.com.example.tutorconectaapi.services;

import tutorconecta.com.example.tutorconectaapi.dtos.UsuarioRequestDTO;
import tutorconecta.com.example.tutorconectaapi.dtos.UsuarioResponseDTO;
import tutorconecta.com.example.tutorconectaapi.entities.Usuario;

import java.util.List;

public interface UsuarioService {

    UsuarioResponseDTO crear(UsuarioRequestDTO request);

    List<UsuarioResponseDTO> listarTodos();

    UsuarioResponseDTO buscarPorId(Long id);

    UsuarioResponseDTO actualizar(Long id, UsuarioRequestDTO request);

    void eliminar(Long id);

    List<UsuarioResponseDTO> buscarTutorPorNombre(String nombre);

}
