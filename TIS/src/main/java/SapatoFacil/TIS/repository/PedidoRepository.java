package SapatoFacil.TIS.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import SapatoFacil.TIS.model.PedidoModel;

public interface PedidoRepository extends JpaRepository<PedidoModel, Long> {

    List<PedidoModel> findByClienteCpf(String cpf);

    PedidoModel findByClienteId(Long id);

    Optional<PedidoModel> findById(Long id);

    List<PedidoModel> findAllByDataVendaBetweenAndStatus(LocalDateTime inicio, LocalDateTime fim, String status);

    }