package SapatoFacil.TIS.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import SapatoFacil.TIS.dto.*;
import SapatoFacil.TIS.infra.security.TokenService;
import SapatoFacil.TIS.model.CarrinhoModel;
import SapatoFacil.TIS.repository.CarrinhoRepository;
import SapatoFacil.TIS.repository.UsuarioRepository;
import SapatoFacil.TIS.service.CarrinhoService;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import SapatoFacil.TIS.model.UsuarioModel;
import SapatoFacil.TIS.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/v1/public/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private CarrinhoService carrinhoService;
    @Autowired
    private CarrinhoRepository carrinhoRepository;

    @Operation(description = "Cadastra um usuario")
    @ApiResponses(
            value ={
                    @ApiResponse(responseCode = "200", description = "usuario cadastrado com sucesso"),
                    @ApiResponse(responseCode = "400", description = "Erro ao cadastrar usuario")
            }
    )
    @PostMapping("/cadastrar")
    public ResponseEntity<ResponseDTO> cadastrarusuario(@Valid @RequestBody RegisterRequestDTO usuario) {
        Optional<UsuarioModel> user = this.usuarioRepository.findByCpf(usuario.cpf());
        if(user.isEmpty()) {
            UsuarioModel newUser = new UsuarioModel();
            newUser.setSenha(passwordEncoder.encode(usuario.senha()));
            newUser.setEmail(usuario.email());
            newUser.setNome(usuario.nome());
            newUser.setCpf(usuario.cpf());
            newUser.setRole(usuario.role());
            this.usuarioRepository.save(newUser);
            CarrinhoModel carrinho = new CarrinhoModel();
            carrinho.setUsuario(newUser);
            carrinhoRepository.save(carrinho);
            newUser.setCarrinho(carrinho);
            usuarioRepository.save(newUser);
            String token = this.tokenService.generateToken(newUser);
            return ResponseEntity.ok(new ResponseDTO(newUser.getNome(), token));
        }
        return ResponseEntity.badRequest().build();
    }

    @Operation(description = "Lista todos os usuarios cadastrados no banco")
    @ApiResponses(
            value ={
                    @ApiResponse(responseCode = "200", description = "Retorna todos os usuarios"),
                    @ApiResponse(responseCode = "400", description = "Não possui usuarios cadastrados")
            }
    )
    @GetMapping("/listar")
    public ResponseEntity<List<UsuarioDTO>> listarUsuarios() {
        List<UsuarioModel> usuarios = usuarioService.listarUsuarios();
        List<UsuarioDTO> usuariosDTO = usuarios.stream()
                .map(UsuarioDTO::fromModel)
                .collect(Collectors.toList());
        return new ResponseEntity<>(usuariosDTO, HttpStatus.OK);
    }

    @Operation(description = "Busca o usuario pelo ID")
    @ApiResponses(
            value ={
                    @ApiResponse(responseCode = "200", description = "Retorna o usuario pela busca do ID"),
                    @ApiResponse(responseCode = "400", description = "Não possui o usuario com este ID")
            }
    )
    @GetMapping("/buscar/{id}")
    public ResponseEntity<UsuarioDTO> buscarUsuario(@PathVariable Long id) {
        UsuarioModel usuario = usuarioService.buscarPorId(id);
        return usuario != null ? ResponseEntity.ok(UsuarioDTO.fromModel(usuario)) : ResponseEntity.notFound().build();
    }

    @Operation(description = "Deleta o usuario pelo ID")
    @ApiResponses(
            value ={
                    @ApiResponse(responseCode = "200", description = "Deleta o usuario com sucesso"),
                    @ApiResponse(responseCode = "400", description = "Não possui o usuario com este ID para deletar")
            }
    )
    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<Void> deletarUsuario(@PathVariable Long id) {
        usuarioService.deletarUsuario(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(description = "Login de usuario")
    @ApiResponses(
            value ={
                    @ApiResponse(responseCode = "200", description = "Login efetuado com sucesso"),
                    @ApiResponse(responseCode = "401", description = "Credenciais inválidas")
            }
    )
    @PostMapping("/login")
    public ResponseEntity<ResponseDTO> loginUsuario(@RequestBody LoginRequestDTO usuario) {
        UsuarioModel user = this.usuarioRepository.findByCpf(usuario.cpf()).orElseThrow(() -> new RuntimeException("User not found"));
        if(passwordEncoder.matches(usuario.senha(), user.getSenha())) {
            String token = this.tokenService.generateToken(user);
            return ResponseEntity.ok(new ResponseDTO(user.getNome(), token));
        }
        return ResponseEntity.badRequest().build();
    }

    @PutMapping("/atualizar")
    public ResponseEntity<UsuarioDTO> atualizarUsuario(@RequestBody UsuarioModel usuarioAtualizado, Authentication authentication) {
        String cpfUsuarioAutenticado = authentication.name();
        UsuarioModel usuarioAtualizadoResponse = usuarioService.atualizarUsuario(cpfUsuarioAutenticado, usuarioAtualizado);
        return new ResponseEntity<>(UsuarioDTO.fromModel(usuarioAtualizadoResponse), HttpStatus.OK);
    }

    @PostMapping("/recuperar-senha")
    public ResponseEntity<String> verificarCpfEmail(@RequestBody RecuperacaoSenhaRequestDTO request) {
        try {
            String token = usuarioService.verificarCpfEmail(request);
            return new ResponseEntity<>(token, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao recuperar senha - Mensagem: "+ e.getMessage());
        }
    }

    @PostMapping("/redefinir-senha")
    public ResponseEntity<String> redefinirSenha(@RequestBody RedefinirSenhaRequestDTO request) {
        try {
            usuarioService.redefinirSenha(request);
            return new ResponseEntity<>("Senha alterada com sucesso", HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao redefinir senha - Mensagem: "+ e.getMessage());
        }
    }
}