package SapatoFacil.TIS.repository;

import java.util.Optional;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import SapatoFacil.TIS.model.UsuarioModel;

@Repository
public interface UsuarioRepository extends JpaRepository<UsuarioModel, Long> {

    @Query("SELECT DISTINCT u FROM UsuarioModel u " +
           "LEFT JOIN FETCH u.carrinho c " +
           "LEFT JOIN FETCH c.produtos p " +
           "WHERE u.cpf = :cpf")
    Optional<UsuarioModel> findByCpf(@Param("cpf") String cpf);

    @Query("SELECT DISTINCT u FROM UsuarioModel u " +
           "LEFT JOIN FETCH u.carrinho c " +
           "LEFT JOIN FETCH c.produtos p " +
           "WHERE u.id = :id")
    Optional<UsuarioModel> findById(@Param("id") Long id);

    @Query("SELECT DISTINCT u FROM UsuarioModel u " +
           "LEFT JOIN FETCH u.carrinho c " +
           "LEFT JOIN FETCH c.produtos p " +
           "WHERE u.cpf = :cpf AND u.email = :email")
    Optional<UsuarioModel> findByCpfAndEmail(@Param("cpf") String cpf, @Param("email") String email);

    Optional<UsuarioModel> findByTokenDeRecuperacao(String tokenDeRecuperacao);

    @Query("SELECT DISTINCT u FROM UsuarioModel u " +
           "LEFT JOIN FETCH u.carrinho c " +
           "LEFT JOIN FETCH c.produtos p")
    List<UsuarioModel> findAllWithCarrinho();

    @Override
    @Query("SELECT DISTINCT u FROM UsuarioModel u " +
           "LEFT JOIN FETCH u.carrinho c " +
           "LEFT JOIN FETCH c.produtos p")
    List<UsuarioModel> findAll();
}