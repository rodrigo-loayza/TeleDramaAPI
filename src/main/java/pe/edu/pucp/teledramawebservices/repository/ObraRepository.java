package pe.edu.pucp.teledramawebservices.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pe.edu.pucp.teledramawebservices.dto.*;
import pe.edu.pucp.teledramawebservices.entity.Obra;

import java.util.List;

@Repository
public interface ObraRepository extends JpaRepository<Obra, Integer> {

    @Query(value = "select f.id as idfuncion,\n" +
            "f.costoticket as costoticket,\n" +
            "f.fechahora as fechahora,\n" +
            "sum(cantidadtickets) as cantidadtickets,\n" +
            "f.aforofuncion as aforo,\n" +
            "t.nombre as nombreteatro,\n" +
            "s.id as idsala,\n" +
            "s.nombre as nombresala,\n" +
            "f.estado as estado\n" +
            "from compra c\n" +
            "right join funcion f on c.idfuncion = f.id\n" +
            "inner join obra o on f.idobra = o.id\n" +
            "inner join sala s on f.idsala = s.id\n" +
            "inner join teatro t on s.idteatro = t.id\n" +
            "where o.id=?1 group by f.id order by nombreteatro asc, fechahora desc", nativeQuery = true)
    List<FuncionesPorObraDto> funcionesPorObra(Integer id);

    @Query(nativeQuery = true, value = "select ob.id  as 'obraid', ob.nombre as nombreobra,  el.* , round(avg(c.estrellas), 0) as estrellas,  count(el.id) as votaciones \n" +
            "from obra ob \n" +
            "inner join obra_elenco obel on obel.idobra = ob.id \n" +
            "inner join elenco el on el.id = obel.idelenco\n" +
            "inner join calificacion c on el.id = c.idelenco and obel.idobra = c.idobra\n" +
            "where ob.id = ?1 group by el.id")
    List<ElencoPorObraDto> elencoPorObra(Integer id);

    @Query(nativeQuery = true, value = "select ob.id as 'obraid',\n" +
            "       ob.nombre                  as nombreobra,\n" +
            "       el.*,\n" +
            "       round(avg(c.estrellas), 0) as estrellas,\n" +
            "       count(el.id)               as votaciones\n" +
            "from obra ob\n" +
            "         inner join obra_elenco obel on obel.idobra = ob.id\n" +
            "         inner join elenco el on el.id = obel.idelenco\n" +
            "         inner join calificacion c on el.id = c.idelenco and obel.idobra = c.idobra\n" +
            "where ob.id = ?1\n" +
            "  and el.rol = ?2\n" +
            "group by el.id order by estrellas DESC limit 1")
    ElencoPorObraDto elencoDestacadoPorObra(Integer id, String rol);

    @Query(nativeQuery = true, value = "select round(sum(costoticket*cantidadtickets),2) as montototal , sum(cantidadtickets) as totaltickets from (select f.id as idfuncion,\n" +
            "                      f.costoticket        as costoticket,\n" +
            "                      sum(cantidadtickets) as cantidadtickets\n" +
            "               from compra c\n" +
            "                        inner join funcion f on c.idfuncion = f.id\n" +
            "                        inner join obra o on f.idobra = o.id\n" +
            "                        inner join sala s on f.idsala = s.id\n" +
            "                        inner join teatro t on s.idteatro = t.id\n" +
            "               where o.id = ?1\n" +
            "               group by f.id) as funcionObra")
    MontoTotalPorObraDto montoTotalPorObra(Integer id);

    @Query(nativeQuery = true, value = "SELECT distinct o.nombre, o.id, o.fotoprincipal FROM teledrama.funcion f\n" +
            "inner join obra o on (f.idobra=o.id)\n" +
            "where f.estado=\"activo\";")
    List<ObrasFuncionActiva> obrasFuncionActiva();

    @Query(nativeQuery = true, value = "select o.*\n" +
            "from funcion f\n" +
            "inner join obra o on f.idobra = o.id\n" +
            "where f.estado = \"activo\"\n" +
            "\tand f.fechahora > now()\n" +
            "group by f.idobra order by fechahora ASC limit 8;")
    List<Obra> obrasMasPróximas();

    @Query(nativeQuery = true, value = "select o.*,\n" +
            "\tsum(c.cantidadtickets) as vistas\n" +
            "from compra c\n" +
            "inner join funcion f on c.idfuncion = f.id\n" +
            "inner join obra o on f.idobra = o.id\n" +
            "where f.estado = \"activo\"\n" +
            "group by f.idobra\n" +
            "order by vistas desc\n" +
            "limit 8;")
    List<ObrasMasVistasDto> obrasMasVistas();

    @Query(nativeQuery = true, value = "select o.id,o.nombre,o.fotoprincipal,\n" +
            "\tsum(ca.estrellas)/count(ca.estrellas) as calificacion \n" +
            "from compra c\n" +
            "inner join funcion f on c.idfuncion = f.id\n" +
            "inner join obra o on f.idobra = o.id\n" +
            "inner join calificacion ca on ca.idobra=o.id\n" +
            "where f.estado = \"activo\"\n" +
            "group by f.idobra\n" +
            "order by calificacion desc\n" +
            "limit 8;")
    List<ObrasMejorPuntuadasDto> obrasMejorPuntuadas();

    @Query(nativeQuery = true, value = "select ob.id as idobra , \n" +
            "\tob.nombre as nombreobra, \n" +
            "\ttruncate(sum(ca.estrellas)/count(ca.estrellas),1) as calificacion \n" +
            "from obra as ob\n" +
            "inner join calificacion ca on ca.idobra=ob.id\n" +
            "where ob.id=?1\n" +
            "group by ob.nombre;")
    CalificacionPorObraDto calificacionPorObra(Integer id);

    @Query(nativeQuery = true, value = "Select distinct o.* FROM obra o\n" +
            "inner join funcion f on (o.id=f.idobra)\n" +
            "inner join genero_obra go on (go.idobra=o.id)\n" +
            "inner join genero g on (g.id=go.idgenero)\n" +
            "where f.estado='activo' and  g.id=?1 order by g.nombre ASC;")
    List<Obra> obraPorGenero(Integer id);

    @Query(nativeQuery = true, value = "Select distinct o.* FROM obra o\n" +
            "inner join funcion f on (o.id=f.idobra)\n" +
            "inner join sala s on (f.idsala=s.id and s.estado='activo')\n" +
            "inner join teatro t on (s.idteatro=t.id and t.estado='activo')\n" +
            "where f.estado='activo' and  t.id=?1 order by o.nombre ASC;")
    List<Obra> obraPorTeatro(Integer id);

    @Query(nativeQuery = true, value = "Select distinct o.* FROM obra o\n" +
            "inner join funcion f on (o.id=f.idobra)\n" +
            "where f.estado='activo' and f.fechahora=?1 order by o.nombre ASC;")
    List<Obra> obraPorFecha(String fecha);

    // obras por teatro Gestion
    @Query(nativeQuery = true, value = "Select distinct o.* FROM obra o\n" +
            "            inner join funcion f on (o.id=f.idobra)\n" +
            "            inner join sala s on (f.idsala=s.id)\n" +
            "            inner join teatro t on (s.idteatro=t.id)\n" +
            "            inner join teatro_empleado te on (te.idteatro = t.id)\n" +
            "            where t.id=?1 and te.idempleado = ?2 order by o.nombre ASC")
    List<Obra> obraPorTeatroGestion(Integer idTeatro, Integer idOperador, Pageable pageable);

}
