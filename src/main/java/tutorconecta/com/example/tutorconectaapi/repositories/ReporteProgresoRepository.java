package tutorconecta.com.example.tutorconectaapi.repositories;

import tutorconecta.com.example.tutorconectaapi.entities.ReporteProgreso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReporteProgresoRepository extends JpaRepository<ReporteProgreso, Long> {

    @Query("""
            SELECT r
            FROM ReporteProgreso r
            JOIN FETCH r.sesion s
            JOIN FETCH s.estudiante e
            WHERE e.idEstudiante = :idEstudiante
            ORDER BY r.fechaReporte DESC
            """)
    List<ReporteProgreso> findReportesByEstudianteId(@Param("idEstudiante") Long idEstudiante);
    // Buscar reportes por tutor
    @Query("""
            SELECT r
            FROM ReporteProgreso r
            JOIN FETCH r.sesion s
            JOIN FETCH s.tutor t
            WHERE t.idTutor = :idTutor
            ORDER BY r.fechaReporte DESC
            """)
    List<ReporteProgreso> findReportesByTutorId(@Param("idTutor") Long idTutor);
}
