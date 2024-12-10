package SapatoFacil.TIS.repository;

import SapatoFacil.TIS.model.ProdutoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProdutoRepository extends JpaRepository<ProdutoModel, Long> {
    Optional<ProdutoModel> findById(Long id);
    @Query("SELECT p FROM ProdutoModel p WHERE " +
            "(:nome IS NULL OR LOWER(p.nome) LIKE LOWER(CONCAT('%', :nome, '%'))) AND " +
            "(:genero IS NULL OR p.genero = :genero) AND " +
            "(:tamanho IS NULL OR p.tamanho = :tamanho) AND " +
            "(:precoMin IS NULL OR p.valor >= :precoMin) AND " +
            "(:precoMax IS NULL OR p.valor <= :precoMax)")
    List<ProdutoModel> filtrarProdutos(String nome, String genero, String tamanho, Double precoMin, Double precoMax);
}