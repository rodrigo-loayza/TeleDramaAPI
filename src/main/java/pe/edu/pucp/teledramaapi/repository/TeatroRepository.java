package pe.edu.pucp.teledramaapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pe.edu.pucp.teledramaapi.entity.Teatro;

import java.util.List;

@Repository
public interface TeatroRepository extends JpaRepository<Teatro, Integer> {
    @Query(value = "SELECT * FROM teledrama.teatro where estado = 'activo'", nativeQuery = true)
    List<Teatro> teatrosActivos();

    @Query(value = "SELECT * FROM teledrama.teatro t\n" +
            "inner join distrito d on (d.id = t.iddistrito)\n" +
            "where LOWER(CONCAT(t.nombre, t.direccion, t.coordenadas, t.cantidadsalas, t.estado, d.nombre)) like '%(?1)%'", nativeQuery = true)
    List<Teatro> buscadorTeatro(String search);

    @Query(value = "SELECT distinct t.nombre FROM teledrama.funcion f\n" +
            "inner join sala s on (f.idsala=s.id)\n" +
            "inner join teatro t on (s.idteatro=t.id) \n" +
            "where idobra=?1;", nativeQuery = true)
    List<Teatro> teatroPorObra(Integer idobra);

    @Query(value = "SELECT distinct t.nombre FROM teledrama.funcion f\n" +
            "inner join sala s on (f.idsala=s.id)\n" +
            "inner join teatro t on (s.idteatro=t.id) \n" +
            "where idobra=?1;", nativeQuery = true)
    List<Teatro> teatroPorFechaObra(Integer idobra);

    @Query(value = "SELECT distinct t.* FROM teledrama.funcion f\n" +
            "inner join sala s on (f.idsala=s.id and s.estado=\"activo\")\n" +
            "inner join teatro t on (s.idteatro=t.id and t.estado=\"activo\")\n" +
            "where f.estado=\"activo\" order by fechahora ASC;\n", nativeQuery = true)
    List<Teatro> teatrosConFuncion();

    @Query(value = "Select distinct t.* FROM teledrama.funcion f\n" +
            "inner join sala s on (f.idsala=s.id and s.estado='activo')\n" +
            "inner join teatro t on (s.idteatro=t.id and t.estado='activo')\n" +
            "inner join distrito d on (t.iddistrito=d.id)\n" +
            "where f.estado='activo' and d.id=?1 order by fechahora ASC;", nativeQuery = true)
    List<Teatro> teatrosPorDistrito(Integer distrito);

    @Query(value ="select distinct t.* from funcion f\n" +
            "inner join obra o on (o.id=f.idobra)\n" +
            "inner join sala s on (s.id=f.idsala)\n" +
            "inner join teatro t on (t.id=s.idteatro)\n" +
            "where date(f.fechahora)=?1 and o.id=?2 and f.estado='activo'", nativeQuery = true)
    Optional<List<Teatro>> teatrosPorFecha(Date fecha, Integer idObra);

    @Query(nativeQuery = true, value = "select idobra,nombre,idteatro,teatro,sum(monto) as montorecaudado from (select o.id as idobra, o.nombre, \n" +
            "t.id as idteatro, t.nombre as teatro,\n" +
            "round(c.precioticket*sum(c.cantidadtickets),2) as monto\n" +
            "from compra c \n" +
            "inner join funcion f on c.idfuncion = f.id\n" +
            "inner join sala s on s.id = f.idsala\n" +
            "inner join teatro t on t.id = s.idteatro\n" +
            "inner join obra o on f.idobra = o.id\n" +
            "where f.estado = 'inactivo' " +
            "and (:idteatro is null or t.id = :idteatro)" +
            "and (:fechafin is not null or date(f.fechahora) = :fechainicio)  " +
            "and (:fechafin is null or (date(f.fechahora) >= :fechainicio and date(f.fechahora) <= :fechafin))  " +
            "group by f.id order by o.nombre, f.fechahora) \n" +
            "as reporte group by idobra order by nombre;")
    List<MontoObraReporteDto> montoRecaudadoPorObra(@Param("fechainicio") String fechainicio, @Param("fechafin") String fechafin,
                                                    @Param("idteatro") Integer idteatro);
}
