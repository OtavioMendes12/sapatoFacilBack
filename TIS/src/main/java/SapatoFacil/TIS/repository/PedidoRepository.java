package SapatoFacil.TIS.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import SapatoFacil.TIS.model.PedidoModel;

public interface PedidoRepository extends JpaRepository<PedidoModel, Long> {

    @Query("SELECT DISTINCT p FROM PedidoModel p " +
           "LEFT JOIN FETCH p.cliente c " +
           "LEFT JOIN FETCH p.entrega " +
           "LEFT JOIN FETCH p.produtos prod " +
           "WHERE c.cpf = :cpf")
    List<PedidoModel> findByClienteCpf(@Param("cpf") String cpf);

    @Query("SELECT DISTINCT p FROM PedidoModel p " +
           "LEFT JOIN FETCH p.cliente c " +
           "LEFT JOIN FETCH p.entrega " +
           "LEFT JOIN FETCH p.produtos prod " +
           "WHERE c.id = :id")
    PedidoModel findByClienteId(@Param("id") Long id);

    @Query("SELECT DISTINCT p FROM PedidoModel p " +
           "LEFT JOIN FETCH p.cliente " +
           "LEFT JOIN FETCH p.entrega " +
           "LEFT JOIN FETCH p.produtos " +
           "WHERE p.id = :id")
    Optional<PedidoModel> findById(@Param("id") Long id);

    @Query("SELECT DISTINCT p FROM PedidoModel p " +
           "LEFT JOIN FETCH p.cliente " +
           "LEFT JOIN FETCH p.entrega " +
           "LEFT JOIN FETCH p.produtos " +
           "WHERE p.dataVenda BETWEEN :inicio AND :fim " +
           "AND p.status = :status")
    List<PedidoModel> findAllByDataVendaBetweenAndStatus(
            @Param("inicio") LocalDateTime inicio,
            @Param("fim") LocalDateTime fim,
            @Param("status") String status);
}