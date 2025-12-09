package ecommerce.integracao;

import ecommerce.dto.DisponibilidadeDTO;
import ecommerce.dto.EstoqueBaixaDTO;
import ecommerce.external.IEstoqueExternal;
import ecommerce.external.IPagamentoExternal;
import ecommerce.service.CarrinhoDeComprasService;
import ecommerce.service.ClienteService;
import ecommerce.service.CompraService;
import ecommerce.dto.PagamentoDTO;
import ecommerce.entity.CarrinhoDeCompras;
import ecommerce.entity.Cliente;
import ecommerce.entity.ItemCompra;
import ecommerce.entity.Produto;
import ecommerce.entity.Regiao;
import ecommerce.entity.TipoCliente;
import ecommerce.entity.TipoProduto;
import ecommerce.dto.CompraDTO;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class cenario1Test {

    // =============================================================
    // FAKE DO ESTOQUE EXTERNAL
    // =============================================================
    class EstoqueFake implements IEstoqueExternal {
        public boolean disponibilidade = true;
        public boolean baixaOk = true;

        @Override
        public EstoqueBaixaDTO darBaixa(List<Long> ids, List<Long> qtds) {
            return new EstoqueBaixaDTO(baixaOk);
        }

        @Override
        public DisponibilidadeDTO verificarDisponibilidade(List<Long> ids, List<Long> qtds) {
            return new DisponibilidadeDTO(disponibilidade, null);
        }
    }

    // =============================================================
    // FAKE DO PAGAMENTO EXTERNAL
    // =============================================================
    class PagamentoFake implements IPagamentoExternal {
        public boolean pagamentoOk = true;
        public Long transacao = 999L;
        public boolean cancelou = false;

        @Override
        public PagamentoDTO autorizarPagamento(Long clienteId, Double valor) {
            return new PagamentoDTO(pagamentoOk, transacao);
        }

        @Override
        public void cancelarPagamento(Long clienteId, Long pagamentoTransacaoId) {
            cancelou = true;
        }
    }

    EstoqueFake estoque;
    PagamentoFake pagamento;

    @Mock
    CarrinhoDeComprasService carrinhoService;

    @Mock
    ClienteService clienteService;

    Cliente cliente;
    Produto produto;
    CarrinhoDeCompras carrinho;

    CompraService compraService;

    @BeforeEach
    void setup() {
        estoque = new EstoqueFake();
        pagamento = new PagamentoFake();

        cliente = new Cliente(1L, "Matheus", Regiao.NORTE, TipoCliente.BRONZE);
        produto = new Produto(
                1L, "Produto A", "Descricao A",
                new BigDecimal("100.00"),
                new BigDecimal("1.5"),
                new BigDecimal("10"),
                new BigDecimal("5"),
                new BigDecimal("2"),
                false,
                TipoProduto.ELETRONICO);

        carrinho = new CarrinhoDeCompras(
                1L,
                cliente,
                List.of(new ItemCompra(1L, produto, 2L)),
                null);

        compraService = new CompraService(carrinhoService, clienteService, estoque, pagamento);
    }

    // =============================================================
    // 1) SUCESSO
    // =============================================================
    @Test
    void deveFinalizarCompraComSucesso() {

        estoque.disponibilidade = true;
        estoque.baixaOk = true;

        pagamento.pagamentoOk = true;
        pagamento.transacao = 1234L;

        when(clienteService.buscarPorId(1L)).thenReturn(cliente);
        when(carrinhoService.buscarPorCarrinhoIdEClienteId(1L, cliente)).thenReturn(carrinho);

        CompraDTO dto = compraService.finalizarCompra(1L, 1L);

        Assertions.assertTrue(dto.sucesso());
        Assertions.assertEquals(1234L, dto.transacaoPagamentoId());
    }

    // =============================================================
    // 2) FALHA POR ESTOQUE INDISPONÍVEL
    // =============================================================
    // @Test
    // void deveFalharQuandoEstoqueIndisponivel() {
    // assertThrows(IllegalStateException.class, () -> {
    // compraService.finalizarCompra(1L, 1L);
    // });
    // }

    // =============================================================
    // 3) FALHA POR PAGAMENTO NEGADO
    // =============================================================
    @Test
    void deveFalharQuandoPagamentoNegado() {

        when(clienteService.buscarPorId(1L)).thenReturn(cliente);
        when(carrinhoService.buscarPorCarrinhoIdEClienteId(1L, cliente)).thenReturn(carrinho);

        estoque.disponibilidade = true;
        pagamento.pagamentoOk = false; // pagamento negado

        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                () -> compraService.finalizarCompra(1L, 1L));

        Assertions.assertEquals("Pagamento não autorizado.", ex.getMessage());
    }

    // =============================================================
    // 4) FALHA NA BAIXA → PAGAMENTO CANCELADO
    // =============================================================
    @Test
    void deveCancelarPagamentoQuandoBaixaNoEstoqueFalhar() {

        when(clienteService.buscarPorId(1L)).thenReturn(cliente);
        when(carrinhoService.buscarPorCarrinhoIdEClienteId(1L, cliente)).thenReturn(carrinho);

        estoque.disponibilidade = true;
        estoque.baixaOk = false;

        pagamento.pagamentoOk = true;
        pagamento.transacao = 777L;

        IllegalStateException ex = Assertions.assertThrows(
                IllegalStateException.class,
                () -> compraService.finalizarCompra(1L, 1L));

        Assertions.assertEquals("Erro ao dar baixa no estoque.", ex.getMessage());

        // valida que o fake realmente cancelou
        Assertions.assertTrue(pagamento.cancelou);
    }
}
