package SapatoFacil.TIS.model;

import SapatoFacil.TIS.enuns.RoleEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Entity
public class UsuarioModel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true)
	private String cpf;

	@NotBlank(message = "O nome de usuário não pode ser vazio")
	private String nome;

	@NotBlank(message = "O email do usuário não pode ser vazio")
	@Column(unique = true)
	private String email;

	@NotBlank(message = "A senha do usuário não pode ser vazio")
	@Basic(fetch = FetchType.LAZY)
	private String senha;


	@Enumerated(EnumType.STRING)
	private RoleEnum role;

	@OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private CarrinhoModel carrinho;

	private String tokenDeRecuperacao;

}
