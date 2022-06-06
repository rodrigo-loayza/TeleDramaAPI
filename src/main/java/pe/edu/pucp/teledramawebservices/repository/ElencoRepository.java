package pe.edu.pucp.teledramawebservices.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pe.edu.pucp.teledramawebservices.entity.Elenco;

import java.util.List;

@Repository
public interface ElencoRepository extends JpaRepository<Elenco, Integer> {

    List<Elenco> findElencoByRol (String rol);
    Elenco findElencoByCorreo (String correo);


    @Query (nativeQuery = true, value = "SELECT * FROM teledrama.elenco limit 290,16")
    List<Elenco> listaElenco16();

    @Query (nativeQuery = true, value = "SELECT * FROM teledrama.elenco where rol = 'actor'")
    List<Elenco> listaElencoActores();

    @Query (nativeQuery = true, value = "SELECT * FROM teledrama.elenco where rol = 'director'")
    List<Elenco> listaElencoDirectores();
}

