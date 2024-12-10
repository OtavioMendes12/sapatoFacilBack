package SapatoFacil.TIS.service;

import SapatoFacil.TIS.model.ProdutoModel;
import SapatoFacil.TIS.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    public ProdutoModel salvarProduto(ProdutoModel produto) {
        return produtoRepository.save(produto);
    }

    public List<ProdutoModel> listarProdutos() {
        return produtoRepository.findAll();
    }

    public ProdutoModel buscarPorId(Long id) {
        return produtoRepository.findById(id).orElse(null);
    }

    public void deletarProduto(Long id) {
        produtoRepository.deleteById(id);
    }

    public ProdutoModel atualizarProduto(Long id, ProdutoModel produtoAtualizado) {

        ProdutoModel produtoExistente = produtoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado"));
        produtoExistente.setNome(produtoAtualizado.getNome());
        produtoExistente.setValor(produtoAtualizado.getValor());
        produtoExistente.setQuantidadeEstoque(produtoAtualizado.getQuantidadeEstoque());
        produtoExistente.setTamanho(produtoAtualizado.getTamanho());
        produtoExistente.setGenero(produtoAtualizado.getGenero());
        produtoExistente.setCor(produtoAtualizado.getCor());

        return produtoRepository.save(produtoExistente);
    }
    public List<ProdutoModel> filtrarProdutos(String nome, String genero, String tamanho, Double precoMin, Double precoMax) {
        if (nome != null) {
            nome = nome.trim();
        }
        return produtoRepository.filtrarProdutos(nome, genero, tamanho, precoMin, precoMax);
    }
}