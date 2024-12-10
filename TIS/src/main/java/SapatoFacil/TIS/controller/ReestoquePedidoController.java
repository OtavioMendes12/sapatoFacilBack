package SapatoFacil.TIS.controller;


import SapatoFacil.TIS.dto.ReestoqueRequest;
import SapatoFacil.TIS.model.ReestoquePedido;
import SapatoFacil.TIS.service.ReestoquePedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/public/reestoque")
public class ReestoquePedidoController {

    @Autowired
    private ReestoquePedidoService reestoquePedidoService;

    @PostMapping("/solicitar")
    public ResponseEntity<ReestoquePedido> solicitarReestoque(@RequestBody ReestoqueRequest reestoqueRequest) {
        ReestoquePedido pedido = reestoquePedidoService.solicitarReestoque(
                reestoqueRequest.getProdutoId(),
                reestoqueRequest.getQuantidade()
        );
        return new ResponseEntity<>(pedido, HttpStatus.CREATED);
    }

    @GetMapping("/listar")
    public ResponseEntity<List<ReestoquePedido>> listarPedidos() {
        List<ReestoquePedido> pedidos = reestoquePedidoService.listarReestoquePedidos();
        return ResponseEntity.ok(pedidos);
    }
}