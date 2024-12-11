package SapatoFacil.TIS.dto;

import SapatoFacil.TIS.model.CarrinhoModel;
import lombok.Data;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class CarrinhoDTO {
    private Long id;
    private String usuarioCpf;
    private List<ProdutoDTO> produtos;

    public static CarrinhoDTO fromModel(CarrinhoModel carrinho) {
        CarrinhoDTO dto = new CarrinhoDTO();
        dto.setId(carrinho.getId());
        dto.setUsuarioCpf(carrinho.getUsuario() != null ? carrinho.getUsuario().getCpf() : null);
        dto.setProdutos(carrinho.getProdutos().stream()
                .map(ProdutoDTO::fromModel)
                .collect(Collectors.toList()));
        return dto;
    }
} 