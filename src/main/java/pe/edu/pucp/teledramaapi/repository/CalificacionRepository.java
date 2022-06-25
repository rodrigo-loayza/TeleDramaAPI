package pe.edu.pucp.teledramaapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.pucp.teledramaapi.entity.Calificacion;

public interface CalificacionRepository extends JpaRepository<Calificacion,Integer> {
}
