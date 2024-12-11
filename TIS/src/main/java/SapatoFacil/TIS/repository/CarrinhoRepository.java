package SapatoFacil.TIS.repository;

import SapatoFacil.TIS.model.CarrinhoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CarrinhoRepository extends JpaRepository<CarrinhoModel, Long> {
    @Query("SELECT DISTINCT c FROM CarrinhoModel c " +
           "LEFT JOIN FETCH c.usuario u " +
           "LEFT JOIN FETCH c.produtos " +
           "WHERE c.id = :id")
    Optional<CarrinhoModel> findById(@Param("id") Long id);

    @Query("SELECT DISTINCT c FROM CarrinhoModel c " +
           "LEFT JOIN FETCH c.usuario u " +
           "LEFT JOIN FETCH c.produtos " +
           "WHERE u.cpf = :cpf")
    Optional<CarrinhoModel> findByUsuarioCpf(@Param("cpf") String cpf);
}