package pe.edu.pucp.teledramawebservices.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.pucp.teledramawebservices.entity.TeatroEmpleado;

import java.util.List;

@Repository
public interface TeatroEmpleadoRepository extends JpaRepository<TeatroEmpleado,Integer> {
//    List<TeatroEmpleado> findAllByIdempleado(Empleado id);
  List<TeatroEmpleado> findAllByIdempleadoIdAndIdteatroEstado(int id, String estado);
}
