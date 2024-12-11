package SapatoFacil.TIS.dto;

import SapatoFacil.TIS.model.ProdutoModel;
import SapatoFacil.TIS.model.ProdutoModel.Genero;
import lombok.Data;

@Data
public class ProdutoDTO {
    private Long id;
    private String nome;
    private Double valor;
    private Integer quantidadeEstoque;
    private Integer tamanho;
    private Genero genero;
    private String cor;

    public static ProdutoDTO fromModel(ProdutoModel produto) {
        if (produto == null) return null;
        ProdutoDTO dto = new ProdutoDTO();
        dto.setId(produto.getId());
        dto.setNome(produto.getNome());
        dto.setValor(produto.getValor());
        dto.setQuantidadeEstoque(produto.getQuantidadeEstoque());
        dto.setTamanho(produto.getTamanho());
        dto.setGenero(produto.getGenero());
        dto.setCor(produto.getCor());
        return dto;
    }
} 