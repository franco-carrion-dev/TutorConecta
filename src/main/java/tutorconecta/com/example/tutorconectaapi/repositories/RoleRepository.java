package tutorconecta.com.example.tutorconectaapi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tutorconecta.com.example.tutorconectaapi.entities.Role;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByDenominacionIgnoreCase(String denominacion);
}
