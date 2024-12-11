package SapatoFacil.TIS.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
public class ProdutoModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O título do produto não pode ser vazio")
    @Size(max = 80, message = "O título do produto deve ter no máximo 80 caracteres")
    private String nome;

    @NotNull(message = "O valor do produto não pode ser vazio")
    @DecimalMin(value = "0.0", inclusive = false, message = "O valor do produto deve ser um número positivo")
    private Double valor;

    @NotNull(message = "A quantidade no estoque não pode ser vazia")
    @Min(value = 0, message = "A quantidade no estoque deve ser um número inteiro positivo")
    private Integer quantidadeEstoque;

    @NotNull(message = "O tamanho do produto não pode ser vazio")
    @Min(value = 1, message = "O tamanho do produto deve ser um número inteiro positivo")
    private Integer tamanho;

    @NotNull(message = "O gênero do produto não pode ser vazio")
    @Enumerated(EnumType.STRING)
    private Genero genero;

    @NotBlank(message = "A cor do produto não pode ser vazia")
    private String cor;

    @Basic(fetch = FetchType.LAZY)
    @Lob
    private byte[] foto;

    public enum Genero {
        MASCULINO, FEMININO, UNISSEX
    }

    public ProdutoModel(Long id, String nome, Double valor, Integer quantidadeEstoque, Integer tamanho, Genero genero, String cor) {
        this.id = id;
        this.nome = nome;
        this.valor = valor;
        this.quantidadeEstoque = quantidadeEstoque;
        this.tamanho = tamanho;
        this.genero = genero;
        this.cor = cor;
    }
}