package SapatoFacil.TIS.repository;

import SapatoFacil.TIS.model.ProdutoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProdutoRepository extends JpaRepository<ProdutoModel, Long> {
    @Query("SELECT new ProdutoModel(p.id, p.nome, p.valor, p.quantidadeEstoque, p.tamanho, p.genero, p.cor) FROM ProdutoModel p")
    List<ProdutoModel> findAllWithoutFoto();

    @Query("SELECT new ProdutoModel(p.id, p.nome, p.valor, p.quantidadeEstoque, p.tamanho, p.genero, p.cor) FROM ProdutoModel p WHERE p.id = :id")
    Optional<ProdutoModel> findByIdWithoutFoto(@Param("id") Long id);

    @Query("SELECT p.foto FROM ProdutoModel p WHERE p.id = :id")
    Optional<byte[]> findFotoById(@Param("id") Long id);

    @Query("SELECT new ProdutoModel(p.id, p.nome, p.valor, p.quantidadeEstoque, p.tamanho, p.genero, p.cor) FROM ProdutoModel p WHERE " +
            "(:nome IS NULL OR LOWER(p.nome) LIKE LOWER(CONCAT('%', :nome, '%'))) AND " +
            "(:genero IS NULL OR p.genero = :genero) AND " +
            "(:tamanho IS NULL OR p.tamanho = :tamanho) AND " +
            "(:precoMin IS NULL OR p.valor >= :precoMin) AND " +
            "(:precoMax IS NULL OR p.valor <= :precoMax)")
    List<ProdutoModel> filtrarProdutos(
            @Param("nome") String nome, 
            @Param("genero") String genero, 
            @Param("tamanho") String tamanho, 
            @Param("precoMin") Double precoMin, 
            @Param("precoMax") Double precoMax);
}