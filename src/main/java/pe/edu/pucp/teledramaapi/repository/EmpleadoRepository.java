package pe.edu.pucp.teledramaapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pe.edu.pucp.teledramaapi.entity.Empleado;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmpleadoRepository extends JpaRepository<Empleado,Integer> {


    @Query(value = "SELECT * FROM teledrama.empleado where rol = 'operador' order by id desc" , nativeQuery = true)
    List<Empleado> operadorList();

    @Query(value = "SELECT * FROM teledrama.empleado where rol = 'operador' and enabled='1' order by id desc", nativeQuery = true)
    List<Empleado> operadorListActivo();

    @Query(value = "SELECT * FROM teledrama.empleado where rol = 'operador' and enabled='0' order by id desc", nativeQuery = true)
    List<Empleado> operadorListRetirado();

    @Query(value = "SELECT * FROM teledrama.empleado \n" +
            "where rol = 'operador' and  (nombre like ?1% or dni like ?1% or apellido like ?1%) \n" +
            "order by id desc", nativeQuery = true)
    List<Empleado> operadorListBuscar(String buscar);

    @Query(value = "SELECT te.nombre FROM teledrama.empleado em\n" +
            "inner join teatro_empleado teEm on teEm.idempleado=em.id\n" +
            "inner join teatro te on te.id=teEm.idteatro\n" +
            "where em.id= ?1 limit 4;", nativeQuery = true)
    List<String> top4TeatroOperador(String id);

    Optional<Empleado> findByCorreo(String correo);
    Optional<Empleado> findByRsttokenAndRol(String token, String rol);


}
