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

// Reporte: cantidad de sesiones (no canceladas) por materia
    @Query(value = """
            SELECT m.id_materia, m.nombre, m.nivel, COUNT(s.id_sesion) AS cantidad
            FROM materias m
            LEFT JOIN sesiones s ON s.id_materia = m.id_materia AND s.estado <> 'cancelada'
            GROUP BY m.id_materia, m.nombre, m.nivel
            ORDER BY cantidad DESC, m.nombre
            """, nativeQuery = true)
    List<Object[]> contarSesionesPorMateria();

    // Reporte: cantidad de tutores que dictan cada materia
    @Query(value = """
            SELECT m.id_materia, m.nombre, m.nivel, COUNT(tm.id_tutor) AS cantidad
            FROM materias m
            LEFT JOIN tutor_materias tm ON tm.id_materia = m.id_materia
            GROUP BY m.id_materia, m.nombre, m.nivel
            ORDER BY cantidad DESC, m.nombre
            """, nativeQuery = true)
    List<Object[]> contarTutoresPorMateria();
}
