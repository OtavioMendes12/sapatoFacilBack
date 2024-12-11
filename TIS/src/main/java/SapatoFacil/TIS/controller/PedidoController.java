package SapatoFacil.TIS.controller;

import SapatoFacil.TIS.dto.ComprovanteRequestDTO;
import SapatoFacil.TIS.dto.PedidoDTO;
import SapatoFacil.TIS.model.EntregaModel;
import SapatoFacil.TIS.model.PedidoModel;
import SapatoFacil.TIS.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/private/pedidos")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    @PostMapping("/criar/{idCliente}")
    public ResponseEntity<PedidoDTO> criarPedido(@PathVariable Long idCliente, @RequestBody EntregaModel entregaModel) {
        PedidoModel pedido = pedidoService.criarPedido(idCliente, entregaModel);
        return new ResponseEntity<>(PedidoDTO.fromModel(pedido), HttpStatus.CREATED);
    }

    @GetMapping("/relatorio-vendas-finalizadas")
    @Transactional(readOnly = true)
    public ResponseEntity<List<PedidoDTO>> gerarRelatorioVendasFinalizadas(
            @RequestParam("inicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam("fim") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {

        List<PedidoModel> relatorioVendasFinalizadas = pedidoService.gerarRelatorioVendasFinalizadas(inicio, fim);
        List<PedidoDTO> relatorioDTO = relatorioVendasFinalizadas.stream()
                .map(PedidoDTO::fromModel)
                .collect(Collectors.toList());
        return new ResponseEntity<>(relatorioDTO, HttpStatus.OK);
    }

    @GetMapping("/listar/{cpf}")
    @Transactional(readOnly = true)
    public ResponseEntity<List<PedidoDTO>> listarPedidos(@PathVariable String cpf) {
        List<PedidoModel> pedidos = pedidoService.listarPedidosPorCpf(cpf);
        List<PedidoDTO> pedidosDTO = pedidos.stream()
                .map(PedidoDTO::fromModel)
                .collect(Collectors.toList());
        return new ResponseEntity<>(pedidosDTO, HttpStatus.OK);
    }

    @PostMapping("/alterarEntrega/{pedidoId}")
    public ResponseEntity<String> alterarEntrega(@PathVariable Long pedidoId, @RequestBody EntregaModel entregaModel) {
        try {
            pedidoService.alterarEntrega(entregaModel, pedidoId);
            return new ResponseEntity<>("Entrega do pedido alterada com sucesso", HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao alterar entrega");
        }
    }

    @PostMapping("/finalizar/{pedidoId}")
    public ResponseEntity<PedidoDTO> finalizarPedido(@PathVariable Long pedidoId) {
        PedidoModel pedidoFinalizado = pedidoService.finalizarPedido(pedidoId);
        return new ResponseEntity<>(PedidoDTO.fromModel(pedidoFinalizado), HttpStatus.OK);
    }

    @PostMapping("/colocarComprovante/{pedidoId}")
    public ResponseEntity<String> colocarComprovante(@PathVariable Long pedidoId, @RequestBody ComprovanteRequestDTO comprovanteRequestDTO) {
        try {
            PedidoModel pedidoModel = pedidoService.receberComprovante(comprovanteRequestDTO.getComprovante(), pedidoId);
            return new ResponseEntity<>("Comprovante adicionado com sucesso", HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao setar comprovante");
        }
    }
}