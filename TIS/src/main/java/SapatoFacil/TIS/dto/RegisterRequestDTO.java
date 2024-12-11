package SapatoFacil.TIS.dto;

import SapatoFacil.TIS.enuns.RoleEnum;

public record RegisterRequestDTO (String nome, String cpf, String email, String senha, RoleEnum role){
}
