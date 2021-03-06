package pe.edu.pucp.teledramaapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.pucp.teledramaapi.entity.TeatroEmpleado;

import java.util.List;

@Repository
public interface TeatroEmpleadoRepository extends JpaRepository<TeatroEmpleado,Integer> {
//    List<TeatroEmpleado> findAllByIdempleado(Empleado id);
  List<TeatroEmpleado> findAllByIdempleadoIdAndIdteatroEstado(int id, String estado);
}
