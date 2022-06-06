package pe.edu.pucp.teledramawebservices.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pe.edu.pucp.teledramawebservices.dto.contenidoSalaDto;
import pe.edu.pucp.teledramawebservices.entity.Sala;
import pe.edu.pucp.teledramawebservices.entity.Teatro;

import java.util.List;
import java.util.Optional;

public interface SalaRepository extends JpaRepository<Sala, Integer> {


    @Query(value = "SELECT * FROM sala WHERE idteatro = ?1 and estado = ?2", nativeQuery = true)
    Optional<List<Sala>> salasPorIdteatroYEstado(int idteatro, String estado);

    @Query(value = "select * from sala where idteatro = ?1",nativeQuery = true)
    List<Sala> listaSala(Integer idteatro);

    @Query(value = "SELECT fu.id as idfuncion,ob.nombre as nombreobra, el.nombre as director, fu.fechahora as fechayhora,fu.costoticket as costoticker,co.cantidadtickets as tickescomprados, fu.estado FROM teledrama.funcion fu\n" +
            "inner join obra ob on ob.id=fu.idobra\n" +
            "inner join obra_elenco obel on obel.idobra=ob.id\n" +
            "inner join elenco el on el.id=obel.idelenco\n" +
            "inner join compra co on co.idfuncion=fu.id\n" +
            "where el.rol='director' and fu.idsala= ?1",nativeQuery = true)
    List<contenidoSalaDto> listaInfoSala(Integer idSala);

    Optional<Sala> findAllByTeatro(Integer id);




}
