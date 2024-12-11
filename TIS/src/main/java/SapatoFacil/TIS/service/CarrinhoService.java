package SapatoFacil.TIS.service;

import SapatoFacil.TIS.model.CarrinhoModel;
import SapatoFacil.TIS.model.ProdutoModel;
import SapatoFacil.TIS.model.UsuarioModel;
import SapatoFacil.TIS.repository.CarrinhoRepository;
import SapatoFacil.TIS.repository.ProdutoRepository;
import SapatoFacil.TIS.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class CarrinhoService {
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private ProdutoRepository produtoRepository;
    @Autowired
    private CarrinhoRepository carrinhoRepository;

    @Transactional
    public void adicionarProdutoAoCarrinho(Long id, Long produtoId) {
        UsuarioModel usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));
        ProdutoModel produto = produtoRepository.findByIdWithoutFoto(produtoId)
                .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado"));
        
        if (produto.getQuantidadeEstoque() <= 0) {
            throw new IllegalArgumentException("Produto sem estoque");
        }
        
        produto.setQuantidadeEstoque(produto.getQuantidadeEstoque() - 1);
        produtoRepository.save(produto);
        
        CarrinhoModel carrinho = usuario.getCarrinho();
        if (carrinho == null) {
            carrinho = new CarrinhoModel();
            carrinho.setUsuario(usuario);
            carrinho.setProdutos(new ArrayList<>());
        }

        carrinho.getProdutos().add(produto);
        carrinhoRepository.save(carrinho);
    }

    @Transactional(readOnly = true)
    public List<ProdutoModel> listarCarrinhoPorCpf(String cpfUsuario) {
        CarrinhoModel carrinho = carrinhoRepository.findByUsuarioCpf(cpfUsuario)
                .orElseThrow(() -> new IllegalArgumentException("Carrinho não encontrado"));
        
        return carrinho.getProdutos();
    }

    @Transactional
    public void removerProdutoDoCarrinho(String cpfUsuario, Long produtoId) {
        CarrinhoModel carrinho = carrinhoRepository.findByUsuarioCpf(cpfUsuario)
                .orElseThrow(() -> new IllegalArgumentException("Carrinho não encontrado"));

        ProdutoModel produto = produtoRepository.findByIdWithoutFoto(produtoId)
                .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado"));

        if (carrinho.getProdutos().remove(produto)) {
            carrinhoRepository.save(carrinho);
            produto.setQuantidadeEstoque(produto.getQuantidadeEstoque() + 1);
            produtoRepository.save(produto);
        } else {
            throw new IllegalArgumentException("Produto não está no carrinho");
        }
    }
}