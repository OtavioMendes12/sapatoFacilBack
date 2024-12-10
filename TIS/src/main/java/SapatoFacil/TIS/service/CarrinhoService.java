package SapatoFacil.TIS.service;

import SapatoFacil.TIS.model.CarrinhoModel;
import SapatoFacil.TIS.model.ProdutoModel;
import SapatoFacil.TIS.model.UsuarioModel;
import SapatoFacil.TIS.repository.CarrinhoRepository;
import SapatoFacil.TIS.repository.ProdutoRepository;
import SapatoFacil.TIS.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



import java.util.List;

@Service
public class CarrinhoService {
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private ProdutoRepository produtoRepository;
    @Autowired
    private CarrinhoRepository carrinhoRepository;
    public void adicionarProdutoAoCarrinho(Long id, Long produtoId) {
        UsuarioModel usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));
        ProdutoModel produto = produtoRepository.findById(produtoId)
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
        }

        List<ProdutoModel> produtos = carrinho.getProdutos();
        produtos.add(produto);
        carrinho.setProdutos(produtos);

        carrinhoRepository.save(carrinho);
    }

    public List<ProdutoModel> listarCarrinhoPorCpf(String cpfUsuario) {
        UsuarioModel usuario = usuarioRepository.findByCpf(cpfUsuario)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        CarrinhoModel carrinho = usuario.getCarrinho();
        List<ProdutoModel> produtos = carrinho.getProdutos();
        if (carrinho == null) {
            throw new IllegalArgumentException("Carrinho não encontrado");
        }

        return produtos;
    }

    public void removerProdutoDoCarrinho(String cpfUsuario, Long produtoId) {
        UsuarioModel usuario = usuarioRepository.findByCpf(cpfUsuario)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        CarrinhoModel carrinho = usuario.getCarrinho();
        if (carrinho == null) {
            throw new IllegalArgumentException("Carrinho não encontrado");
        }

        ProdutoModel produto = produtoRepository.findById(produtoId)
                .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado"));


        List<ProdutoModel> produtos = carrinho.getProdutos();
        if (produtos.contains(produto)) {
            produtos.remove(produto);
            carrinho.setProdutos(produtos);
            carrinhoRepository.save(carrinho);
            produto.setQuantidadeEstoque(produto.getQuantidadeEstoque() + 1);
            produtoRepository.save(produto);
        } else {
            throw new IllegalArgumentException("Produto não está no carrinho");
        }
    }
}