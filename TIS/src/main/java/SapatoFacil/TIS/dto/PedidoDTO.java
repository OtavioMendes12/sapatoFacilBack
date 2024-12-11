package SapatoFacil.TIS.dto;

import SapatoFacil.TIS.model.PedidoModel;
import lombok.Data;
import org.hibernate.Hibernate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class PedidoDTO {
    private Long id;
    private String clienteCpf;
    private String clienteNome;
    private List<ProdutoDTO> produtos;
    private EntregaDTO entrega;
    private LocalDateTime dataPedido;
    private LocalDateTime dataVenda;
    private Double total;
    private String status;

    public static PedidoDTO fromModel(PedidoModel pedido) {
        if (pedido == null) return null;
        
        PedidoDTO dto = new PedidoDTO();
        dto.setId(pedido.getId());
        if (pedido.getCliente() != null) {
            dto.setClienteCpf(pedido.getCliente().getCpf());
            dto.setClienteNome(pedido.getCliente().getNome());
        }
        
        // Safely handle produtos collection
        if (pedido.getProdutos() != null && Hibernate.isInitialized(pedido.getProdutos())) {
            dto.setProdutos(pedido.getProdutos().stream()
                    .filter(produto -> produto != null)
                    .map(ProdutoDTO::fromModel)
                    .collect(Collectors.toList()));
        } else {
            dto.setProdutos(new ArrayList<>());
        }
        
        dto.setEntrega(pedido.getEntrega() != null ? EntregaDTO.fromModel(pedido.getEntrega()) : null);
        dto.setDataPedido(pedido.getDataPedido());
        dto.setDataVenda(pedido.getDataVenda());
        dto.setTotal(pedido.getTotal());
        dto.setStatus(pedido.getStatus());
        return dto;
    }
} 