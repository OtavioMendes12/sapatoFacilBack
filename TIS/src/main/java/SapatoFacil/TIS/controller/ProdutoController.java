package SapatoFacil.TIS.controller;

import SapatoFacil.TIS.dto.ProdutoDTO;
import SapatoFacil.TIS.model.ProdutoModel;
import SapatoFacil.TIS.repository.ProdutoRepository;
import SapatoFacil.TIS.service.ProdutoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/public/produtos")
public class ProdutoController {
    @Autowired
    private ProdutoService produtoService;

    @Operation(description = "Cadastra um produto")
    @ApiResponses(
            value ={
                    @ApiResponse(responseCode = "200", description = "Produto cadastrado com sucesso"),
                    @ApiResponse(responseCode = "400", description = "Erro ao cadastrar produto")
            }
    )
    @PostMapping("/cadastrar")
    public ResponseEntity<ProdutoDTO> cadastrarProduto(@RequestBody ProdutoModel produto) {
        try {
            ProdutoModel novoProduto = produtoService.salvarProduto(produto);
            return new ResponseEntity<>(ProdutoDTO.fromModel(novoProduto), HttpStatus.CREATED);
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
    public ResponseEntity<List<ProdutoDTO>> listarProdutos() {
        List<ProdutoModel> produtos = produtoService.listarProdutos();
        List<ProdutoDTO> produtosDTO = produtos.stream()
                .map(ProdutoDTO::fromModel)
                .collect(Collectors.toList());
        return new ResponseEntity<>(produtosDTO, HttpStatus.OK);
    }

    @Operation(description = "Busca o produto pelo ID")
    @ApiResponses(
            value ={
                    @ApiResponse(responseCode = "200", description = "Retorna o produto pela busca do ID"),
                    @ApiResponse(responseCode = "400", description = "Não possui o produto com este ID")
            }
    )
    @GetMapping("/buscar/{id}")
    public ResponseEntity<ProdutoDTO> buscarProduto(@PathVariable Long id) {
        ProdutoModel produto = produtoService.buscarPorId(id);
        return produto != null ? ResponseEntity.ok(ProdutoDTO.fromModel(produto)) : ResponseEntity.notFound().build();
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
    public ResponseEntity<ProdutoDTO> atualizarProduto(
            @PathVariable Long id, @RequestBody ProdutoModel produtoAtualizado) {
        ProdutoModel produtoAtualizadoResponse = produtoService.atualizarProduto(id, produtoAtualizado);
        return new ResponseEntity<>(ProdutoDTO.fromModel(produtoAtualizadoResponse), HttpStatus.OK);
    }

    @PostMapping("/{id}/upload-imagem")
    public ResponseEntity<String> uploadImagem(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        try {
            produtoService.salvarFoto(id, file.getBytes());
            return ResponseEntity.ok("Imagem enviada e associada com sucesso ao produto de ID " + id);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao processar imagem");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/{id}/foto")
    public ResponseEntity<byte[]> getFoto(@PathVariable Long id) {
        byte[] foto = produtoService.buscarFotoPorId(id);
        if (foto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(foto);
    }

    @GetMapping("/filtrar")
    public ResponseEntity<List<ProdutoDTO>> filtrarProdutos(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String genero,
            @RequestParam(required = false) String tamanho,
            @RequestParam(required = false) Double precoMin,
            @RequestParam(required = false) Double precoMax) {

        List<ProdutoModel> produtos = produtoService.filtrarProdutos(nome, genero, tamanho, precoMin, precoMax);
        List<ProdutoDTO> produtosDTO = produtos.stream()
                .map(ProdutoDTO::fromModel)
                .collect(Collectors.toList());
        return ResponseEntity.ok(produtosDTO);
    }
}