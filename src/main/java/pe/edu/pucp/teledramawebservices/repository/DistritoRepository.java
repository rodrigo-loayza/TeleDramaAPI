package pe.edu.pucp.teledramawebservices.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.pucp.teledramawebservices.entity.Distrito;

@Repository
public interface DistritoRepository extends JpaRepository<Distrito,Integer> {
}
