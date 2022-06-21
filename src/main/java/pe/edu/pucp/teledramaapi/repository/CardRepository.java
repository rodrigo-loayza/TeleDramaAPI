package pe.edu.pucp.teledramaapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.pucp.teledramaapi.entity.Cardcredential;

@Repository
public interface CardRepository  extends JpaRepository<Cardcredential,Integer> {
}