package ecommerce.funcionais;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import ecommerce.entity.*;
import ecommerce.service.CompraService;

import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TabelaDecicoesTest {
    private Cliente cliente;
    private CarrinhoDeCompras carrinho;
    private List<ItemCompra> items;
    private CompraService compraService;

    @BeforeEach
    public void definirClienteECarrinho() {
        cliente = new Cliente(1L, "Albert", Regiao.NORDESTE, TipoCliente.OURO);
        items = new ArrayList<>();
        carrinho = new CarrinhoDeCompras(1L, cliente, items, LocalDate.now());
        compraService = new CompraService(null, null, null, null);
    }

    // A1, A2, A3, A4
    @ParameterizedTest
    @CsvSource({
            "2, 20.00",
            "3, 28.50",
            "6, 54.00",
            "10, 85.00",
    })
    void CenarioTabelaDecicoes_DescontoPorTipoDeProduto_SemDescontoPorValorDeCarrinho(
            Long quantidade, String valorEsperado) {
        items = Arrays.asList(
                new ItemCompra(1L, new Produto(1L, "Smartphone X1", "Smartphone top de linha",
                        BigDecimal.valueOf(10), BigDecimal.valueOf(0.3),
                        BigDecimal.valueOf(15), BigDecimal.valueOf(7), BigDecimal.valueOf(0.8),
                        false, TipoProduto.ELETRONICO), quantidade));

        carrinho.setItens(items);
        BigDecimal subTotal = compraService.subTotalComDesconto(carrinho);
        assertThat(subTotal).isEqualByComparingTo(valorEsperado);
    }

    // A5, A6, A7, A8
    @ParameterizedTest
    @CsvSource({
            "2, 300.00, 540.00",
            "3, 200.00, 513.00",
            "5, 120.00, 486.00",
            "8, 80.00, 489.60",
            "2, 600.00, 960.00",
            "3, 400.00, 912.00",
            "5, 240.00, 864.00",
            "8, 160.00, 870.400"
    })
    void CenarioTabelaDecicoes_ComDescontoPorTipoDeProduto_ComDescontoPorValorDeCarrinho(
            Long quantidade, String preco, String valorEsperado) {
        items = Arrays.asList(
                new ItemCompra(1L, new Produto(1L, "Smartphone X1", "Smartphone top de linha",
                        new BigDecimal(preco), BigDecimal.valueOf(0.3),
                        BigDecimal.valueOf(15), BigDecimal.valueOf(7), BigDecimal.valueOf(0.8),
                        false, TipoProduto.ELETRONICO), quantidade));

        carrinho.setItens(items);
        BigDecimal subTotal = compraService.subTotalComDesconto(carrinho);
        assertThat(subTotal).isEqualByComparingTo(valorEsperado);
    }
}
