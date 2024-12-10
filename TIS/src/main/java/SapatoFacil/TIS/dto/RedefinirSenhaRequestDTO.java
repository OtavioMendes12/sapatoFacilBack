package SapatoFacil.TIS.dto;

import lombok.Data;

@Data
public class RedefinirSenhaRequestDTO {
    private String token;
    private String novaSenha;

}