package SapatoFacil.TIS.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import SapatoFacil.TIS.dto.RecuperacaoSenhaRequestDTO;
import SapatoFacil.TIS.dto.RedefinirSenhaRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import SapatoFacil.TIS.model.UsuarioModel;
import SapatoFacil.TIS.repository.UsuarioRepository;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Transactional
    public UsuarioModel salvarUsuario(UsuarioModel usuario) {
        if (!isCpfValido(usuario.getCpf())) {
            throw new IllegalArgumentException("O CPF deve conter exatamente 11 dígitos.");
        }
        return usuarioRepository.save(usuario);
    }

    @Transactional(readOnly = true)
    public List<UsuarioModel> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    @Transactional(readOnly = true)
    public UsuarioModel buscarPorId(Long id) {
        return usuarioRepository.findById(id).orElse(null);
    }

    @Transactional
    public void deletarUsuario(Long id) {
        usuarioRepository.deleteById(id);
    }

    private boolean isCpfValido(String cpf) {
        return cpf != null && cpf.matches("\\d{11}");
    }

    @Transactional(readOnly = true)
    public boolean loginUsuario(String cpf, String password) {
        Optional<UsuarioModel> usuarioOptional = usuarioRepository.findByCpf(cpf);
        if (usuarioOptional.isPresent()) {
            UsuarioModel usuario = usuarioOptional.get();
            return usuario.getSenha().equals(password);
        }
        return false;
    }

    @Transactional
    public UsuarioModel atualizarUsuario(String cpf, UsuarioModel usuarioAtualizado) {
        UsuarioModel usuarioExistente = usuarioRepository.findByCpf(cpf)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));
        
        usuarioExistente.setNome(usuarioAtualizado.getNome());
        usuarioExistente.setEmail(usuarioAtualizado.getEmail());
        usuarioExistente.setSenha(usuarioAtualizado.getSenha());
        
        return usuarioRepository.save(usuarioExistente);
    }

    @Transactional
    public String verificarCpfEmail(RecuperacaoSenhaRequestDTO request) {
        UsuarioModel usuario = usuarioRepository.findByCpfAndEmail(request.getCpf(), request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("CPF ou e-mail não encontrado"));

        String token = UUID.randomUUID().toString();
        usuario.setTokenDeRecuperacao(token);
        usuarioRepository.save(usuario);

        return token;
    }

    @Transactional
    public void redefinirSenha(RedefinirSenhaRequestDTO request) {
        UsuarioModel usuario = usuarioRepository.findByTokenDeRecuperacao(request.getToken())
                .orElseThrow(() -> new IllegalArgumentException("Token inválido ou expirado"));

        usuario.setSenha(request.getNovaSenha());
        usuario.setTokenDeRecuperacao(null);
        usuarioRepository.save(usuario);
    }
}