package pe.edu.pucp.teledramaapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.edu.pucp.teledramaapi.dto.*;
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


    @Query(value = "select o.id as idobra, o.nombre, o.fotoprincipal,\n" +
            "f.fechahora,\n" +
            "s.id as idsala, s.nombre as sala,\n" +
            "t.id as idteatro, t.nombre as teatro,\n" +
            "sum(c.cantidadtickets) as `asistencia` \n" +
            "from compra c\t\t\t\t\t\t\t\n" +
            "inner join funcion f on c.idfuncion = f.id\n" +
            "inner join sala s on s.id = f.idsala\n" +
            "inner join teatro t on t.id = s.idteatro\n" +
            "inner join obra o on f.idobra = o.id\n" +
            "where f.estado = \"inactivo\"\n" +
            "group by f.id order by asistencia,fechahora desc limit 1", nativeQuery = true)
    Optional<FuncionMasMenosVistaDto> funcionMenosVista();

    @Query(value = "select o.id as idobra, o.nombre, o.fotoprincipal,\n" +
            "f.fechahora,\n" +
            "s.id as idsala, s.nombre as sala,\n" +
            "t.id as idteatro, t.nombre as teatro,\n" +
            "sum(c.cantidadtickets) as `asistencia` \n" +
            "from compra c\n" +
            "inner join funcion f on c.idfuncion = f.id\n" +
            "inner join sala s on s.id = f.idsala\n" +
            "inner join teatro t on t.id = s.idteatro\n" +
            "inner join obra o on f.idobra = o.id\n" +
            "where f.estado = \"inactivo\"\n" +
            "group by f.id order by asistencia desc,fechahora desc limit 1;", nativeQuery = true)
    Optional<FuncionMasMenosVistaDto> funcionMasVista();

    @Query(nativeQuery = true, value = "select o.id as idobra, o.nombre, \n" +
            "f.id as idfuncion, f.fechahora,\n" +
            "s.id as idsala, s.nombre as sala,\n" +
            "t.id as idteatro, t.nombre as teatro,\n" +
            "c.precioticket,\n" +
            "sum(c.cantidadtickets) as `asistencia`, \n" +
            "round((sum(c.cantidadtickets)/f.aforofuncion)*100,2) as `porcentaje`,\n" +
            "round((c.precioticket *sum(c.cantidadtickets)),2) as monto\n" +
            "from compra c \n" +
            "inner join funcion f on c.idfuncion = f.id\n" +
            "inner join sala s on s.id = f.idsala\n" +
            "inner join teatro t on t.id = s.idteatro\n" +
            "inner join obra o on f.idobra = o.id\n" +
            "where f.estado = 'inactivo' and  (:idobra is null or o.id = :idobra) " +
            "and (:idteatro is null or t.id = :idteatro) " +
            "and (:fechafin is not null or date(f.fechahora) = :fechainicio)  " +
            "and (:fechafin is null or (date(f.fechahora) >= :fechainicio and date(f.fechahora) <= :fechafin))  " +
            "group by f.id order by o.nombre, f.fechahora")
    List<ReporteDto> generarReporte(@Param("fechainicio") Date fechainicio,
                                    @Param("fechafin") Date fechafin,
                                    @Param("idobra") Integer idobra,
                                    @Param("idteatro") Integer idteatro);

    // min porcentaje asistencia
    @Query(nativeQuery = true,value = "select idobra, nombreobra, idfuncion,\n" +
            "    round(100*min(pctasistencia), 1) as asistencia \n" +
            "from (select c.idfuncion as idfuncion, \n" +
            "\t\to.id as idobra, o.nombre as nombreobra,\n" +
            "\t\tsum(c.cantidadtickets)/f.aforofuncion as pctasistencia\n" +
            "\tfrom compra c\n" +
            "\tinner join funcion f on c.idfuncion = f.id\n" +
            "\tinner join obra o on f.idobra = o.id\n" +
            "    inner join sala s on f.idsala = s.id\n" +
            "    inner join teatro t on s.idteatro = t.id\n" +
            "\twhere c.estado = 'asistido' \n" +
            "and (:idteatro is null or t.id = :idteatro) " +
            "and (:fechafin is not null or date(f.fechahora) = :fechainicio)  " +
            "and (:fechafin is null or (date(f.fechahora) >= :fechainicio and date(f.fechahora) <= :fechafin))  " +
            "\tgroup by c.idfuncion\n" +
            "\torder by idobra asc, pctasistencia asc) pct\n" +
            "group by pct.idobra;")
    List<PorcentajeAsistenciaObraDto> funcionesMenosVistasPorcentaje(@Param("fechainicio") Date fechainicio, @Param("fechafin") Date fechafin,
                                                                     @Param("idteatro") Integer idteatro);

    // max porcentaje asistencia
    @Query(nativeQuery = true,value = "select idobra, nombreobra, idfuncion,\n" +
            "    round(100*max(pctasistencia), 1) as asistencia \n" +
            "from (select c.idfuncion as idfuncion,\n" +
            "\t\to.id as idobra, o.nombre as nombreobra,\n" +
            "\t\tsum(c.cantidadtickets)/f.aforofuncion as pctasistencia\n" +
            "\tfrom compra c\n" +
            "\tinner join funcion f on c.idfuncion = f.id\n" +
            "\tinner join obra o on f.idobra = o.id\n" +
            "    inner join sala s on f.idsala = s.id\n" +
            "    inner join teatro t on s.idteatro = t.id\n" +
            "\twhere c.estado = 'asistido' \n" +
            "and (:idteatro is null or t.id = :idteatro) " +
            "and (:fechafin is not null or date(f.fechahora) = :fechainicio)  " +
            "and (:fechafin is null or (date(f.fechahora) >= :fechainicio and date(f.fechahora) <= :fechafin))  " +
            "\tgroup by c.idfuncion\n" +
            "\torder by idobra asc, pctasistencia desc \n" +
            "\t) pct\n" +
            "group by pct.idobra;")
    List<PorcentajeAsistenciaObraDto> funcionesMasVistasPorcentaje(@Param("fechainicio") Date fechainicio, @Param("fechafin") Date fechafin,
                                                                   @Param("idteatro") Integer idteatro);



    @Query(value ="select f.id as idfuncion,time_format(f.fechahora,'%H:%i') as time,o.id as idobra from funcion f\n" +
            "inner join obra o on (o.id=f.idobra)\n" +
            "inner join sala s on (s.id=f.idsala)\n" +
            "inner join teatro t on (t.id=s.idteatro)\n" +
            "where date(f.fechahora)= ?1 and o.id=?2 and t.id=?3 and f.estado='activo';", nativeQuery = true)
    Optional<List<HorasFuncionDto>> horasFuncionesPorTeatro(Date fecha, Integer idobra, Integer idteatro);


    @Query(value="select f.id as idfuncion, o.id as idobra,o.nombre as nombreobra, date(f.fechahora) as fechafuncion, time_format(f.fechahora,'%H:%i') as horafuncion, t.nombre as nombreteatro, f.aforofuncion as aforo\n" +
            "from funcion f \n" +
            "inner join obra o on f.idobra = o.id\n" +
            "inner join sala s on s.id=f.idsala\n" +
            "inner join teatro t on t.id=s.idteatro\n" +
            "where f.id=?1",nativeQuery = true)
    Optional<FuncionDatosDto> detalleFuncion(Integer idfuncion);

}
