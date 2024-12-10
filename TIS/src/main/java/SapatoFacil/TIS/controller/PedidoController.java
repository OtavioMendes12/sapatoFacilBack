package SapatoFacil.TIS.controller;

import SapatoFacil.TIS.dto.ComprovanteRequestDTO;
import SapatoFacil.TIS.model.EntregaModel;
import SapatoFacil.TIS.model.PedidoModel;
import SapatoFacil.TIS.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/v1/private/pedidos")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;


    @PostMapping("/criar/{idCliente}")
    public ResponseEntity<PedidoModel> criarPedido(@PathVariable Long idCliente, @RequestBody EntregaModel entregaModel) {
        PedidoModel pedido = pedidoService.criarPedido(idCliente,entregaModel);
        return new ResponseEntity<>(pedido, HttpStatus.CREATED);
    }

    @GetMapping("/relatorio-vendas-finalizadas")
    public ResponseEntity<List<PedidoModel>> gerarRelatorioVendasFinalizadas(
            @RequestParam("inicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam("fim") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {

        List<PedidoModel> relatorioVendasFinalizadas = pedidoService.gerarRelatorioVendasFinalizadas(inicio, fim);
        return new ResponseEntity<>(relatorioVendasFinalizadas, HttpStatus.OK);
    }


    @GetMapping("/listar/{cpf}")
    public ResponseEntity<List<PedidoModel>> listarPedidos(@PathVariable String cpf) {
        List<PedidoModel> pedidos = pedidoService.listarPedidosPorCpf(cpf);
        return new ResponseEntity<>(pedidos, HttpStatus.OK);
    }

    @PostMapping("/alterarEntrega/{pedidoId}")
    public ResponseEntity<String> alterarEntrega(@PathVariable Long pedidoId, @RequestBody EntregaModel entregaModel) {
        try{
           pedidoService.alterarEntrega(entregaModel, pedidoId);
            return new ResponseEntity<>("Entrega do pedido alterada com sucesso", HttpStatus.OK);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao setar comprovante");

        }

    }
    @PostMapping("/finalizar/{pedidoId}")
    public ResponseEntity<PedidoModel> finalizarPedido(@PathVariable Long pedidoId) {
        PedidoModel pedidoFinalizado = pedidoService.finalizarPedido(pedidoId);
        return new ResponseEntity<>(pedidoFinalizado, HttpStatus.OK);
    }

    @PostMapping("/colocarComprovante/{pedidoId}")
    public ResponseEntity<String> colocarComprovante(@PathVariable Long pedidoId, @RequestBody ComprovanteRequestDTO comprovanteRequestDTO) {
        try{
            PedidoModel pedidoModel = pedidoService.receberComprovante(comprovanteRequestDTO.getComprovante(), pedidoId);
            return new ResponseEntity<>(pedidoModel.toString(), HttpStatus.OK);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao setar comprovante");
        }

    }
}