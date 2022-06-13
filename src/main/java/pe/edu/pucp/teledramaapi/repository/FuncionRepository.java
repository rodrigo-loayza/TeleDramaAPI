package pe.edu.pucp.teledramaapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pe.edu.pucp.teledramaapi.dto.FuncionesPorObraDto;
import pe.edu.pucp.teledramaapi.dto.FuncionesProximasDto;
import pe.edu.pucp.teledramaapi.dto.HorasFuncionDto;
import pe.edu.pucp.teledramaapi.dto.InicioFinFuncionDto;
import pe.edu.pucp.teledramaapi.entity.Funcion;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface FuncionRepository extends JpaRepository<Funcion, Integer> {
    /*
     * Query que valida que no se cruce la fechahora de la función a crear con otras ya creadas
     * En caso haya cruce, retornará el id, iniciofuncion, finfunción de la función con la que se cruza.
     * En caso no haya cruce con ninguna, no se retorna nada.
     * Además, se usa el idfuncion para no validar con su fecha misma.
     */
    @Query(value = "select f.id as idfuncion,\n" +
            "    f.fechahora as iniciofuncion,\n" +
            "            (f.fechahora + INTERVAL o.duracion MINUTE) as finfuncion\n" +
            "    from funcion f\n" +
            "    inner join obra o on (f.idobra = o.id)\n" +
            "    where f.idobra = ?1 and f.id != ?3\n" +
            "    and not (?2 < f.fechahora - INTERVAL (o.duracion + 30) MINUTE\n" +
            "       or ?2 > f.fechahora + INTERVAL (o.duracion + 30) MINUTE);", nativeQuery = true)
    List<InicioFinFuncionDto> validarFechaFuncion(Integer idobra, LocalDateTime fechahora, Integer idfuncion);

    @Query(value ="SELECT id, date(fechahora) as fecha, estado,idobra FROM teledrama.funcion where fechahora > NOW() and idobra=?1 order by fechahora ASC;", nativeQuery = true)
    Optional<List<FuncionesProximasDto>> funcionesProximasId(Integer idobra);

    @Query(value = "select f.id as idfuncion,\n" +
            "f.costoticket as costoticket,\n" +
            "f.fechahora as fechahora,\n" +
            "sum(cantidadtickets) as cantidadtickets,\n" +
            "f.aforofuncion as aforo,\n" +
            "t.nombre as nombreteatro,\n" +
            "s.nombre as nombresala,\n" +
            "f.estado as estado from compra c\n" +
            "right join funcion f on c.idfuncion = f.id\n" +
            "inner join obra o on f.idobra = o.id\n" +
            "inner join sala s on f.idsala = s.id\n" +
            "inner join teatro t on s.idteatro = t.id\n" +
            "where f.id=(?1);", nativeQuery = true)
    List<FuncionesPorObraDto> obradeFuncion(Integer idf);

    @Query(value ="select f.id as idfuncion,time(f.fechahora) as time,o.id as idobra from funcion f\n" +
            "inner join obra o on (o.id=f.idobra)\n" +
            "inner join sala s on (s.id=f.idsala)\n" +
            "inner join teatro t on (t.id=s.idteatro)\n" +
            "where date(f.fechahora)= ?1 and o.id=?2 and t.id=?3 and f.estado='activo';", nativeQuery = true)
    Optional<List<HorasFuncionDto>> horasFuncionesPorTeatro(Date fecha, Integer idobra, Integer idteatro);

}
