package tutorconecta.com.example.tutorconectaapi.repositories;

import tutorconecta.com.example.tutorconectaapi.entities.Sesion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SesionRepository extends JpaRepository<Sesion, Long> {

    // Query Simple
    List<Sesion> findByEstado(String estado);

    // Query con JOIN explícito: Obtener sesiones de un tutor específico por nombre de materia
    @Query("SELECT s FROM Sesion s JOIN s.materia m WHERE s.tutor.idTutor = :idTutor AND m.nombre = :nombreMateria")
    List<Sesion> findSesionesByTutorAndMateria(@Param("idTutor") Long idTutor, @Param("nombreMateria") String nombreMateria);

    // Query con JOIN: Obtener todas las sesiones agendadas de un estudiante, trayendo los datos del tutor
    @Query("SELECT s FROM Sesion s JOIN FETCH s.tutor t JOIN FETCH t.usuario u WHERE s.estudiante.idEstudiante = :idEstudiante AND s.estado = 'agendada'")
    List<Sesion> findSesionesAgendadasEstudiante(@Param("idEstudiante") Long idEstudiante);

    @Query (value = "SELECT * FROM sesiones WHERE estado = :estado", nativeQuery = true)
    List<Sesion> buscarPorEstadoNativo(@Param("estado") String estado);
}
