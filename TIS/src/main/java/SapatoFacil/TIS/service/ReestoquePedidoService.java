package SapatoFacil.TIS.service;


import SapatoFacil.TIS.model.ReestoquePedido;
import SapatoFacil.TIS.repository.ProdutoRepository;
import SapatoFacil.TIS.repository.ReestoquePedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReestoquePedidoService {

    @Autowired
    private ReestoquePedidoRepository reestoquePedidoRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    public ReestoquePedido solicitarReestoque(Long produtoId, Integer quantidade) {
        var produto = produtoRepository.findById(produtoId)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        ReestoquePedido pedido = new ReestoquePedido();
        pedido.setProduto(produto);
        pedido.setQuantidadeSolicitada(quantidade);

        return reestoquePedidoRepository.save(pedido);
    }

    public List<ReestoquePedido> listarReestoquePedidos() {
        return reestoquePedidoRepository.findAll();
    }
}