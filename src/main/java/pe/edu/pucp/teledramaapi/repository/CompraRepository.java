package pe.edu.pucp.teledramaapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pe.edu.pucp.teledramaapi.dto.ComprasPorClienteDto;
import pe.edu.pucp.teledramaapi.dto.TicketsVendidosPorFuncionDto;
import pe.edu.pucp.teledramaapi.entity.Compra;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompraRepository extends JpaRepository<Compra,Integer> {
    @Query(value="SELECT * FROM teledrama.compra where estado = 'vigente' and idcliente = ?1",nativeQuery = true)
    List<Compra> comprasVigentesList(int id);

    @Query(value = "select id as idfuncion, sum(cantidadtickets) as ticketsvendidos from compra " +
            "where idfuncion = ? group by idfuncion", nativeQuery = true)
    Optional<TicketsVendidosPorFuncionDto> ticketsVendidosPorFuncion(Integer idFuncion);

    @Query(value="select c.id as idcompra,f.id, o.id as idobra,c.fechacompra as fechacompra, c.fechacambio ,o.nombre as nombreobra,\n" +
            " c.cantidadtickets as cantidaddetickets, f.costoticket,c.estado,f.fechahora as fechafuncion, t.nombre as nombreteatro, s.nombre as nombresala, o.duracion\n" +
            " from compra c\n" +
            " inner join funcion f on c.idfuncion = f.id\n" +
            " inner join obra o on f.idobra = o.id\n" +
            " inner join sala s on s.id=f.idsala\n" +
            " inner join teatro t on t.id=s.idteatro\n" +
            " where c.idcliente = (?1)\n" +
            " order by c.fechacompra desc;",nativeQuery = true)
    List<ComprasPorClienteDto> comprasClient(int id);

    @Query(value="select c.id as idcompra,f.id, o.id as idobra,c.fechacompra as fechacompra, c.fechacambio ,o.nombre as nombreobra,\n" +
            " c.cantidadtickets as cantidaddetickets, f.costoticket,c.estado,f.fechahora as fechafuncion, t.nombre as nombreteatro, s.nombre as nombresala, o.duracion\n" +
            " from compra c\n" +
            " inner join funcion f on c.idfuncion = f.id\n" +
            " inner join obra o on f.idobra = o.id\n" +
            " inner join sala s on s.id=f.idsala\n" +
            " inner join teatro t on t.id=s.idteatro\n" +
            " where c.id=(?1) and f.id=(?2);",nativeQuery = true)
    ComprasPorClienteDto detalleCompra(int idc, int idf);

}
