package SapatoFacil.TIS.controller;

import SapatoFacil.TIS.model.ProdutoModel;
import SapatoFacil.TIS.repository.ProdutoRepository;
import SapatoFacil.TIS.service.ProdutoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ExpressionException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/v1/public/produtos")
public class ProdutoController {
    @Autowired
    private ProdutoService produtoService;
    @Autowired
    private ProdutoRepository produtoRepository;

    @Operation(description = "Cadastra um produto")
    @ApiResponses(
            value ={
                    @ApiResponse(responseCode = "200", description = "Produto cadastrado com sucesso"),
                    @ApiResponse(responseCode = "400", description = "Erro ao cadastrar produto")
            }
    )
    @PostMapping("/cadastrar")
    public ResponseEntity<ProdutoModel> cadastrarProduto(@RequestBody ProdutoModel produto) {
        try {
            ProdutoModel novoProduto = produtoService.salvarProduto(produto);
            return new ResponseEntity<>(novoProduto, HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @Operation(description = "Lista todos os produtos cadastrados no banco")
    @ApiResponses(
            value ={
                    @ApiResponse(responseCode = "200", description = "Retorna todos os produtos"),
                    @ApiResponse(responseCode = "400", description = "Não possui produtos cadastrados")
            }
    )
    @GetMapping("/listar")
    public ResponseEntity<List<ProdutoModel>> listarProdutos() {
        List<ProdutoModel> produtos = produtoService.listarProdutos();
        return new ResponseEntity<>(produtos, HttpStatus.OK);
    }
    @Operation(description = "Busca o produto pelo ID")
    @ApiResponses(
            value ={
                    @ApiResponse(responseCode = "200", description = "Retorna o produto pela busca do ID"),
                    @ApiResponse(responseCode = "400", description = "Não possui o produto com este ID")
            }
    )
    @GetMapping("/buscar/{id}")
    public ResponseEntity<ProdutoModel> buscarProduto(@PathVariable Long id) {
        ProdutoModel produto = produtoService.buscarPorId(id);
        return produto != null ? ResponseEntity.ok(produto) : ResponseEntity.notFound().build();
    }

    @Operation(description = "Deleta o produto pelo ID")
    @ApiResponses(
            value ={
                    @ApiResponse(responseCode = "200", description = "Deleta o produto com sucesso"),
                    @ApiResponse(responseCode = "400", description = "Não possui o produto com este ID para deletar")
            }
    )
    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<Void> deletarProduto(@PathVariable Long id) {
        produtoService.deletarProduto(id);
        return ResponseEntity.noContent().build();
    }
    @PutMapping("/atualizar/{id}")
    public ResponseEntity<ProdutoModel> atualizarProduto(
            @PathVariable Long id, @RequestBody ProdutoModel produtoAtualizado) {
        ProdutoModel produtoAtualizadoResponse = produtoService.atualizarProduto(id, produtoAtualizado);
        return new ResponseEntity<>(produtoAtualizadoResponse, HttpStatus.OK);
    }

    @PostMapping("/{id}/upload-imagem")
    public ResponseEntity<String> uploadImagem(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        try {
            byte[] fileBytes = file.getBytes();
            ProdutoModel produto = produtoRepository.findById(id).get();
            produto.setFoto(fileBytes);
            produtoRepository.save(produto);

            return ResponseEntity.ok("Imagem enviada e associada com sucesso ao produto de ID " + id);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao processar imagem");
        }
    }
    @GetMapping("/{id}/foto")
    public ResponseEntity<byte[]> getFoto(@PathVariable Long id) {
        ProdutoModel produto = produtoRepository.findById(id).orElseThrow(() -> new ExpressionException("Produto não encontrado"));
        byte[] foto = produto.getFoto();

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(foto);
    }

    @GetMapping("/filtrar")
    public ResponseEntity<List<ProdutoModel>> filtrarProdutos(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String genero,
            @RequestParam(required = false) String tamanho,
            @RequestParam(required = false) Double precoMin,
            @RequestParam(required = false) Double precoMax) {

        List<ProdutoModel> produtos = produtoService.filtrarProdutos(nome, genero, tamanho, precoMin, precoMax);
        return ResponseEntity.ok(produtos);
    }
}