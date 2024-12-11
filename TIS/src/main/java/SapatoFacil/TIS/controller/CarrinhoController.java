package SapatoFacil.TIS.controller;

import SapatoFacil.TIS.dto.CarrinhoDTO;
import SapatoFacil.TIS.dto.ProdutoDTO;
import SapatoFacil.TIS.model.CarrinhoModel;
import SapatoFacil.TIS.model.ProdutoModel;
import SapatoFacil.TIS.service.CarrinhoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/private/carrinho")
public class CarrinhoController {

    @Autowired
    private CarrinhoService carrinhoService;

    @PostMapping("/adicionar/{id}/{produtoId}")
    public ResponseEntity<String> adicionarProdutoAoCarrinho(@PathVariable Long id, @PathVariable Long produtoId) {
        try {
            carrinhoService.adicionarProdutoAoCarrinho(id, produtoId);
            return new ResponseEntity<>("Produto adicionado ao carrinho com sucesso", HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao adicionar produto - Mensagem: " + e.getMessage());
        }
    }

    @GetMapping("/listar/{cpf}")
    @Transactional(readOnly = true)
    public ResponseEntity<List<ProdutoDTO>> listarCarrinho(@PathVariable String cpf) {
        try {
            List<ProdutoModel> produtos = carrinhoService.listarCarrinhoPorCpf(cpf);
            List<ProdutoDTO> produtosDTO = produtos.stream()
                    .map(ProdutoDTO::fromModel)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(produtosDTO, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/remover/{cpf}/{produtoId}")
    public ResponseEntity<String> removerProdutoDoCarrinho(@PathVariable String cpf, @PathVariable Long produtoId) {
        try {
            carrinhoService.removerProdutoDoCarrinho(cpf, produtoId);
            return new ResponseEntity<>("Produto removido do carrinho com sucesso", HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao remover produto - Mensagem: " + e.getMessage());
        }
    }
}