package tutorconecta.com.example.tutorconectaapi.repositories;

import tutorconecta.com.example.tutorconectaapi.entities.DisponibilidadHoraria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DisponibilidadHorariaRepository extends JpaRepository<DisponibilidadHoraria, Long> {

    @Query("""
            SELECT d
            FROM DisponibilidadHoraria d
            JOIN FETCH d.tutor t
            WHERE t.idTutor = :idTutor
            ORDER BY d.diaSemana, d.horaInicio
            """)
    List<DisponibilidadHoraria> findDisponibilidadByTutorId(@Param("idTutor") Long idTutor);
    //Buscar disponibilidad por dia
    @Query("""
            SELECT d
            FROM DisponibilidadHoraria d
            JOIN FETCH d.tutor t
            WHERE LOWER(d.diaSemana) = LOWER(:diaSemana)
            """)
    List<DisponibilidadHoraria> findByDiaSemana(@Param("diaSemana") String diaSemana);
}
