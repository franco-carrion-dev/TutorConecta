package tutorconecta.com.example.tutorconectaapi.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tutorconecta.com.example.tutorconectaapi.entities.Pago;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PagoRepository extends JpaRepository<Pago, Long> {
    // Query Simple: Obtener los pagos por su estado (ej. "COMPLETADO")
    List<Pago> findByEstado(String estado);

    // Query con JOIN explícito: Obtener pagos de un tutor específico filtrados por el estado del pago
    @Query("SELECT p FROM Pago p JOIN p.sesion s WHERE s.tutor.idTutor = :idTutor AND p.estado = :estado")
    List<Pago> findPagosByTutorAndEstado(@Param("idTutor") Long idTutor, @Param("estado") String estado);
}

