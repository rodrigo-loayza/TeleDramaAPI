package pe.edu.pucp.teledramaapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pe.edu.pucp.teledramaapi.entity.Cliente;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Integer> {

    @Query(value = "SELECT * FROM teledrama.cliente \n" +
            "where rol = 'cliente' and  (nombre like ?1% or apellido like ?1% or dni like ?1%) \n" +
            "order by id desc", nativeQuery = true)
    List<Cliente> clienteListBuscar(String buscar);

    Optional<Cliente> findByCorreo(String correo);
    Optional<Cliente> findByDni(String dni);
    Optional<Cliente> findByRsttoken(String token);


}
