package SapatoFacil.TIS.dto;

import SapatoFacil.TIS.enuns.RoleEnum;
import SapatoFacil.TIS.model.UsuarioModel;
import lombok.Data;

@Data
public class UsuarioDTO {
    private Long id;
    private String cpf;
    private String nome;
    private String email;
    private RoleEnum role;
    private CarrinhoDTO carrinho;

    public static UsuarioDTO fromModel(UsuarioModel usuario) {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setId(usuario.getId());
        dto.setCpf(usuario.getCpf());
        dto.setNome(usuario.getNome());
        dto.setEmail(usuario.getEmail());
        dto.setRole(usuario.getRole());
        if (usuario.getCarrinho() != null) {
            dto.setCarrinho(CarrinhoDTO.fromModel(usuario.getCarrinho()));
        }
        return dto;
    }
} 