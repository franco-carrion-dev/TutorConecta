package tutorconecta.com.example.tutorconectaapi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tutorconecta.com.example.tutorconectaapi.entities.Materia;

import java.util.List;

public interface MateriaRepository extends JpaRepository<Materia, Long> {
    // Query Simple
    List<Materia> findByNivelEducativo(String nivelEducativo);
    @Query(value = "SELECT * FROM materias WHERE UPPER(nombre) LIKE UPPER(CONCAT('%', :texto, '%'))", nativeQuery = true)
    List<Materia> buscarPorNombreParecido(@Param("texto") String texto);
}