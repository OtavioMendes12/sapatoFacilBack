package SapatoFacil.TIS.service;

import SapatoFacil.TIS.model.ProdutoModel;
import SapatoFacil.TIS.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Transactional
    public ProdutoModel salvarProduto(ProdutoModel produto) {
        return produtoRepository.save(produto);
    }

    @Transactional(readOnly = true)
    public List<ProdutoModel> listarProdutos() {
        return produtoRepository.findAllWithoutFoto();
    }

    @Transactional(readOnly = true)
    public ProdutoModel buscarPorId(Long id) {
        return produtoRepository.findByIdWithoutFoto(id).orElse(null);
    }

    @Transactional(readOnly = true)
    public byte[] buscarFotoPorId(Long id) {
        return produtoRepository.findFotoById(id).orElse(null);
    }

    @Transactional
    public void deletarProduto(Long id) {
        produtoRepository.deleteById(id);
    }

    @Transactional
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

    @Transactional
    public ProdutoModel salvarFoto(Long id, byte[] foto) {
        ProdutoModel produto = produtoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado"));
        produto.setFoto(foto);
        return produtoRepository.save(produto);
    }

    @Transactional(readOnly = true)
    public List<ProdutoModel> filtrarProdutos(String nome, String genero, String tamanho, Double precoMin, Double precoMax) {
        if (nome != null) {
            nome = nome.trim();
        }
        return produtoRepository.filtrarProdutos(nome, genero, tamanho, precoMin, precoMax);
    }
}