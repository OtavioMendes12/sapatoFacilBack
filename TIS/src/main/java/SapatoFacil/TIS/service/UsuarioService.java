package SapatoFacil.TIS.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import SapatoFacil.TIS.dto.RecuperacaoSenhaRequestDTO;
import SapatoFacil.TIS.dto.RedefinirSenhaRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import SapatoFacil.TIS.model.UsuarioModel;
import SapatoFacil.TIS.repository.UsuarioRepository;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository userRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;

    public UsuarioModel salvarUsuario(UsuarioModel usuario) {
        if (!isCpfValido(usuario.getCpf())) {
            throw new IllegalArgumentException("O CPF deve conter exatamente 11 dígitos.");
        }
        return userRepository.save(usuario);
    }

    public List<UsuarioModel> listarUsuarios() {
        return userRepository.findAll();
    }

    public UsuarioModel buscarPorId(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public void deletarUsuario(Long id) {
        userRepository.deleteById(id);
    }

    private boolean isCpfValido(String cpf) {
        return cpf != null && cpf.matches("\\d{11}");
    }

    public boolean loginUsuario(String cpf, String password) {
        Optional<UsuarioModel> usuarioOptional = userRepository.findByCpf(cpf);
        if (usuarioOptional.isPresent()) {
            UsuarioModel usuario = usuarioOptional.get();
            return usuario.getSenha().equals(password);
        }
        return false;
    }
    public UsuarioModel atualizarUsuario(String cpf, UsuarioModel usuarioAtualizado) {

        UsuarioModel usuarioExistente = userRepository.findByCpf(cpf)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));
        usuarioExistente.setNome(usuarioAtualizado.getNome());
        usuarioExistente.setEmail(usuarioAtualizado.getEmail());
        usuarioExistente.setSenha(usuarioAtualizado.getSenha());
        return userRepository.save(usuarioExistente);
    }
    public String verificarCpfEmail(RecuperacaoSenhaRequestDTO request) {
        UsuarioModel usuario = usuarioRepository.findByCpfAndEmail(request.getCpf(), request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("CPF ou e-mail não encontrado"));


        String token = UUID.randomUUID().toString();
        usuario.setTokenDeRecuperacao(token);
        usuarioRepository.save(usuario);

        return token;
    }

    public void redefinirSenha(RedefinirSenhaRequestDTO request) {
        UsuarioModel usuario = usuarioRepository.findByTokenDeRecuperacao(request.getToken())
                .orElseThrow(() -> new IllegalArgumentException("Token inválido ou expirado"));

        // Atualizar a senha do usuário
        usuario.setSenha(request.getNovaSenha());
        usuario.setTokenDeRecuperacao(null); // Remove o token após o uso
        usuarioRepository.save(usuario);
    }
}