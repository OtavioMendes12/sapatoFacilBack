package SapatoFacil.TIS.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import SapatoFacil.TIS.model.UsuarioModel;

@Repository
public interface UsuarioRepository extends JpaRepository<UsuarioModel, Long> {

    Optional<UsuarioModel> findByCpf(String cpf);

    Optional<UsuarioModel> findById(Long id);

    Optional<UsuarioModel> findByCpfAndEmail(String cpf, String email);

    Optional<UsuarioModel> findByTokenDeRecuperacao(String tokenDeRecuperacao);

}