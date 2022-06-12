package pe.edu.pucp.teledramaapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pe.edu.pucp.teledramaapi.entity.Elenco;

import java.util.List;
import java.util.Optional;

@Repository
public interface ElencoRepository extends JpaRepository<Elenco, Integer> {

    List<Elenco> findElencoByRol(String rol);

    Elenco findElencoByCorreo(String correo);

    @Query(nativeQuery = true, value = "SELECT * FROM teledrama.elenco limit 290,16")
    List<Elenco> listaElenco16();

    @Query(nativeQuery = true, value = "SELECT * FROM teledrama.elenco where rol = 'actor'")
    List<Elenco> listaElencoActores();

    @Query(nativeQuery = true, value = "SELECT * FROM teledrama.elenco where rol = 'director'")
    List<Elenco> listaElencoDirectores();

    @Query(nativeQuery = true, value = "select e.id, e.foto, e.nombre, e.apellido, e.rol,\n" +
            "round(sum(ca.estrellas)/count(ca.estrellas),2) as calificacion, count(idelenco) as votos\n" +
            "from elenco e inner join calificacion ca on e.id = ca.idelenco\n" +
            "where idelenco is not null and e.rol = ?1\n" +
            "group by e.id\n" +
            "having votos>3 order by calificacion desc limit 3")
    Optional<Elenco> elencoMejorCalificado(String rol);

}

