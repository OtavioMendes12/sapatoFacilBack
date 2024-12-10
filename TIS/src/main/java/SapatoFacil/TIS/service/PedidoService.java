package SapatoFacil.TIS.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import SapatoFacil.TIS.model.CarrinhoModel;
import SapatoFacil.TIS.model.EntregaModel;
import SapatoFacil.TIS.model.NotificacaoModel;
import SapatoFacil.TIS.model.PedidoModel;
import SapatoFacil.TIS.model.ProdutoModel;
import SapatoFacil.TIS.model.UsuarioModel;
import SapatoFacil.TIS.repository.CarrinhoRepository;
import SapatoFacil.TIS.repository.NotificacaoRepository;
import SapatoFacil.TIS.repository.PedidoRepository;
import SapatoFacil.TIS.repository.UsuarioRepository;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private CarrinhoRepository carrinhoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private NotificacaoRepository notificacaoRepository;

    public PedidoModel criarPedido(Long idCliente, EntregaModel entregaModel) {

        UsuarioModel cliente = usuarioRepository.findById(idCliente)
                .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado"));
        CarrinhoModel carrinho = cliente.getCarrinho();
        if (carrinho == null || carrinho.getProdutos().isEmpty()) {
            throw new IllegalArgumentException("Carrinho vazio");
        }
        PedidoModel pedido = new PedidoModel();
        pedido.setCliente(cliente);
        pedido.setProdutos(new ArrayList<>(carrinho.getProdutos()));
        pedido.setDataPedido(LocalDateTime.now());
        pedido.setStatus("Pendente");
        pedido.setEntrega(entregaModel);



        Double total = carrinho.getProdutos().stream()
                .mapToDouble(ProdutoModel::getValor)
                .sum();
        pedido.setTotal(total);


        PedidoModel pedidoSalvo = pedidoRepository.save(pedido);

        String mensagem = "Novo pedido gerado pelo cliente: " + cliente.getNome();
        NotificacaoModel notificacao = new NotificacaoModel();
        notificacao.setMensagem(mensagem);
        notificacaoRepository.save(notificacao);


        //emailService.enviarNotificacao("admin@exemplo.com", "Nova solicitação de pedido Pedido", mensagem);


        carrinho.getProdutos().clear();
        carrinhoRepository.save(carrinho);

        return pedidoSalvo;
    }

    public List<PedidoModel> listarPedidosPorCpf(String cpfCliente) {
        return pedidoRepository.findByClienteCpf(cpfCliente);
    }

    public PedidoModel alterarEntrega(EntregaModel entregaAtualizada, Long pedidoId) {
        try{
            PedidoModel pedidoExistente = pedidoRepository.findById(pedidoId)
                    .orElseThrow(() -> new RuntimeException("Pedido não encontrado com ID: " + pedidoId));
            EntregaModel existenteEntrega = pedidoExistente.getEntrega();
            existenteEntrega.setCep(entregaAtualizada.getCep());
            existenteEntrega.setRua(entregaAtualizada.getRua());
            existenteEntrega.setCidade(entregaAtualizada.getCidade());
            existenteEntrega.setComplemento(entregaAtualizada.getComplemento());
            existenteEntrega.setTelefoneContato(entregaAtualizada.getTelefoneContato());
            existenteEntrega.setEstado(entregaAtualizada.getEstado());
            return pedidoRepository.save(pedidoExistente);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }


    }
    public List<PedidoModel> gerarRelatorioVendasFinalizadas(LocalDate inicio, LocalDate fim) {
        LocalDateTime dataInicio = inicio.atStartOfDay();
        LocalDateTime dataFim = fim.atTime(23, 59, 59);

        return pedidoRepository.findAllByDataVendaBetweenAndStatus(dataInicio, dataFim, "FINALIZADO");
    }

    public PedidoModel finalizarPedido(Long pedidoId) {
        PedidoModel pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado com ID: " + pedidoId));

        if (!"FINALIZADO".equals(pedido.getStatus())) {
            pedido.setStatus("FINALIZADO");
            pedido.setDataVenda(LocalDateTime.now());
            pedidoRepository.save(pedido);
        }

        return pedido;
    }

    public PedidoModel receberComprovante(byte[] comprovante, Long pedidoId) {
        try {
            PedidoModel pedido = pedidoRepository.findById(pedidoId)
                    .orElseThrow(() -> new RuntimeException("Pedido não encontrato com ID: " + pedidoId));

            pedido.setComprovante(comprovante);
            pedidoRepository.save(pedido);
            return pedido;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

    }


}