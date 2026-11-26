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

// Reporte: cantidad y monto recaudado por método de pago (solo COMPLETADO)
    @Query(value = """
            SELECT COALESCE(p.metodo_pago, 'Sin método') AS metodo,
                   COUNT(p.id_pago) AS cantidad,
                   COALESCE(SUM(p.monto), 0) AS monto_total
            FROM pagos p
            WHERE p.estado = 'COMPLETADO'
            GROUP BY metodo
            ORDER BY monto_total DESC
            """, nativeQuery = true)
    List<Object[]> montoPorMetodoPago();

    // Reporte: cantidad y monto recaudado por mes en un año (solo COMPLETADO)
    @Query(value = """
            SELECT CAST(EXTRACT(MONTH FROM p.fecha_pago) AS INTEGER) AS mes,
                   COUNT(p.id_pago) AS cantidad,
                   COALESCE(SUM(p.monto), 0) AS monto_total
            FROM pagos p
            WHERE p.estado = 'COMPLETADO' AND EXTRACT(YEAR FROM p.fecha_pago) = :anio
            GROUP BY mes
            ORDER BY mes
            """, nativeQuery = true)
    List<Object[]> montoPorMes(@Param("anio") int anio);
}
