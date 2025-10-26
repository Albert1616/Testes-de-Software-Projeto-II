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

public class ClassesParticaoTest {
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

    @Test
    void CalcularDescontoPorTipoDeProdutoDeveLancarExcecaoParaListaDeItensInvalida() {
        carrinho.setItens(null);
        assertThrows(IllegalArgumentException.class, () -> {
            compraService.subTotalComDesconto(carrinho);
            ;
        }, "Lista de itens inválida.");
    }

    // P1
    @Test
    void CalcularDescontoPorTipoDeProduto_ParaNenhumItemDoMesmoTipo_EntaoDescontoZero() {
        items = Arrays.asList(
                new ItemCompra(1L, new Produto(1L, "Smartphone X1", "Smartphone top de linha",
                        BigDecimal.valueOf(100), BigDecimal.valueOf(0.3),
                        BigDecimal.valueOf(15), BigDecimal.valueOf(7), BigDecimal.valueOf(0.8),
                        false, TipoProduto.ELETRONICO), 1L),

                new ItemCompra(2L, new Produto(2L, "Camisa Polo", "Camisa social masculina",
                        BigDecimal.valueOf(150), BigDecimal.valueOf(0.25),
                        BigDecimal.valueOf(30), BigDecimal.valueOf(20), BigDecimal.valueOf(1),
                        false, TipoProduto.ROUPA), 2L),

                new ItemCompra(3L, new Produto(3L, "Chocolate Ao Leite", "Caixa com 500g",
                        BigDecimal.valueOf(30), BigDecimal.valueOf(0.5),
                        BigDecimal.valueOf(10), BigDecimal.valueOf(5), BigDecimal.valueOf(4),
                        false, TipoProduto.ALIMENTO), 1L));

        carrinho.setItens(items);
        BigDecimal subTotal = compraService.subTotalComDesconto(carrinho);

        assertThat(subTotal).isEqualByComparingTo("430.00");
    }

    // P2
    @Test
    void CalcularDescontoPorTipoDeProduto_ParaMaisDeTresAQuatroItemsDoMesmoTipo_EntaoDescontoDe5Porcento() {
        items = Arrays.asList(
                new ItemCompra(1L, new Produto(1L, "Smartphone X1", "Smartphone top de linha",
                        BigDecimal.valueOf(100), BigDecimal.valueOf(0.3),
                        BigDecimal.valueOf(15), BigDecimal.valueOf(7), BigDecimal.valueOf(0.8),
                        false, TipoProduto.ELETRONICO), 1L),

                new ItemCompra(2L, new Produto(2L, "Camisa Polo", "Camisa social masculina",
                        BigDecimal.valueOf(150), BigDecimal.valueOf(0.25),
                        BigDecimal.valueOf(30), BigDecimal.valueOf(20), BigDecimal.valueOf(1),
                        false, TipoProduto.ROUPA), 2L),

                new ItemCompra(3L, new Produto(3L, "Chocolate Ao Leite", "Caixa com 500g",
                        BigDecimal.valueOf(30), BigDecimal.valueOf(0.5),
                        BigDecimal.valueOf(10), BigDecimal.valueOf(5), BigDecimal.valueOf(4),
                        false, TipoProduto.ALIMENTO), 3L));

        carrinho.setItens(items);
        BigDecimal subTotal = compraService.subTotalComDesconto(carrinho);

        assertThat(subTotal).isEqualByComparingTo("485.50");
    }

    // P3
    @Test
    void CalcularDescontoPorTipoDeProduto_ParaMaisDeCincoASeteItemsDoMesmoTipo_EntaoDescontoDe10Porcento() {
        items = Arrays.asList(
                new ItemCompra(1L, new Produto(1L, "Smartphone X1", "Smartphone top de linha",
                        BigDecimal.valueOf(100), BigDecimal.valueOf(0.3),
                        BigDecimal.valueOf(15), BigDecimal.valueOf(7), BigDecimal.valueOf(0.8),
                        false, TipoProduto.ELETRONICO), 1L),

                new ItemCompra(2L, new Produto(2L, "Camisa Polo", "Camisa social masculina",
                        BigDecimal.valueOf(150), BigDecimal.valueOf(0.25),
                        BigDecimal.valueOf(30), BigDecimal.valueOf(20), BigDecimal.valueOf(1),
                        false, TipoProduto.ROUPA), 2L),

                new ItemCompra(3L, new Produto(3L, "Chocolate Ao Leite", "Caixa com 500g",
                        BigDecimal.valueOf(10), BigDecimal.valueOf(0.5),
                        BigDecimal.valueOf(10), BigDecimal.valueOf(5), BigDecimal.valueOf(4),
                        false, TipoProduto.ALIMENTO), 6L));

        carrinho.setItens(items);
        BigDecimal subTotal = compraService.subTotalComDesconto(carrinho);

        assertThat(subTotal).isEqualByComparingTo("454.00");
    }

    // P4
    @Test
    void CalcularDescontoPorTipoDeProduto_ParaMaisDeOitoItemsDoMesmoTipo_EntaoDescontoDe15Porcento() {
        items = Arrays.asList(
                new ItemCompra(1L, new Produto(1L, "Smartphone X1", "Smartphone top de linha",
                        BigDecimal.valueOf(100), BigDecimal.valueOf(0.3),
                        BigDecimal.valueOf(15), BigDecimal.valueOf(7), BigDecimal.valueOf(0.8),
                        false, TipoProduto.ELETRONICO), 1L),

                new ItemCompra(2L, new Produto(2L, "Camisa Polo", "Camisa social masculina",
                        BigDecimal.valueOf(150), BigDecimal.valueOf(0.25),
                        BigDecimal.valueOf(30), BigDecimal.valueOf(20), BigDecimal.valueOf(1),
                        false, TipoProduto.ROUPA), 2L),

                new ItemCompra(3L, new Produto(3L, "Chocolate Ao Leite", "Caixa com 500g",
                        BigDecimal.valueOf(10), BigDecimal.valueOf(0.5),
                        BigDecimal.valueOf(10), BigDecimal.valueOf(5), BigDecimal.valueOf(4),
                        false, TipoProduto.ALIMENTO), 10L));

        carrinho.setItens(items);
        BigDecimal subTotal = compraService.subTotalComDesconto(carrinho);

        assertThat(subTotal).isEqualByComparingTo("485.00");
    }

    // P5
    @Test
    void CalcularDescontoPorValorTotalDeCarrinhoDeveLancarExcecaoParaSubTotalNegativo() {
        items = Arrays.asList(
                new ItemCompra(1L, new Produto(1L, "Smartphone X1", "Smartphone top de linha",
                        BigDecimal.valueOf(-10), BigDecimal.valueOf(0.3),
                        BigDecimal.valueOf(15), BigDecimal.valueOf(7), BigDecimal.valueOf(0.8),
                        false, TipoProduto.ELETRONICO), 1L));
        carrinho.setItens(items);

        assertThrows(IllegalArgumentException.class, () -> {
            compraService.subTotalComDesconto(carrinho);
        }, "Subtotal negatívo inválido para cálculo de desconto.");
    }

    // P6
    @Test
    void CalcularDescontoPorValorTotalDoCarrinho_ParaTotalMenorQue500_EntaoDescontoDe0Porcento() {
        items = Arrays.asList(
                new ItemCompra(1L, new Produto(1L, "Smartphone X1", "Smartphone top de linha",
                        BigDecimal.valueOf(100), BigDecimal.valueOf(0.3),
                        BigDecimal.valueOf(15), BigDecimal.valueOf(7), BigDecimal.valueOf(0.8),
                        false, TipoProduto.ELETRONICO), 1L),

                new ItemCompra(2L, new Produto(2L, "Camisa Polo", "Camisa social masculina",
                        BigDecimal.valueOf(150), BigDecimal.valueOf(0.25),
                        BigDecimal.valueOf(30), BigDecimal.valueOf(20), BigDecimal.valueOf(1),
                        false, TipoProduto.ROUPA), 2L),

                new ItemCompra(3L, new Produto(3L, "Chocolate Ao Leite", "Caixa com 500g",
                        BigDecimal.valueOf(10), BigDecimal.valueOf(0.5),
                        BigDecimal.valueOf(10), BigDecimal.valueOf(5), BigDecimal.valueOf(4),
                        false, TipoProduto.ALIMENTO), 1L));

        carrinho.setItens(items);
        BigDecimal subTotal = compraService.subTotalComDesconto(carrinho);

        assertThat(subTotal).isEqualByComparingTo("410.00");
    }

    // P7
    @Test
    void CalcularDescontoPorValorTotalDoCarrinho_ParaTotalEntre500e1000_EntaoDescontoDe10Porcento() {
        items = Arrays.asList(
                new ItemCompra(1L, new Produto(1L, "Smartphone X1", "Smartphone top de linha",
                        BigDecimal.valueOf(100), BigDecimal.valueOf(0.3),
                        BigDecimal.valueOf(15), BigDecimal.valueOf(7), BigDecimal.valueOf(0.8),
                        false, TipoProduto.ELETRONICO), 2L),

                new ItemCompra(2L, new Produto(2L, "Camisa Polo", "Camisa social masculina",
                        BigDecimal.valueOf(150), BigDecimal.valueOf(0.25),
                        BigDecimal.valueOf(30), BigDecimal.valueOf(20), BigDecimal.valueOf(1),
                        false, TipoProduto.ROUPA), 2L),

                new ItemCompra(3L, new Produto(3L, "Chocolate Ao Leite", "Caixa com 500g",
                        BigDecimal.valueOf(10), BigDecimal.valueOf(0.5),
                        BigDecimal.valueOf(10), BigDecimal.valueOf(5), BigDecimal.valueOf(4),
                        false, TipoProduto.ALIMENTO), 1L));

        carrinho.setItens(items);
        BigDecimal subTotal = compraService.subTotalComDesconto(carrinho);

        assertThat(subTotal).isEqualByComparingTo("459.00");
    }

    // P8
    @Test
    void CalcularDescontoPorValorTotalDoCarrinho_ParaTotalMaiorQue1000_EntaoDescontoDe20Porcento() {
        items = Arrays.asList(
                new ItemCompra(1L, new Produto(1L, "Smartphone X1", "Smartphone top de linha",
                        BigDecimal.valueOf(100), BigDecimal.valueOf(0.3),
                        BigDecimal.valueOf(15), BigDecimal.valueOf(7), BigDecimal.valueOf(0.8),
                        false, TipoProduto.ELETRONICO), 2L),

                new ItemCompra(2L, new Produto(2L, "Camisa Polo", "Camisa social masculina",
                        BigDecimal.valueOf(500), BigDecimal.valueOf(0.25),
                        BigDecimal.valueOf(30), BigDecimal.valueOf(20), BigDecimal.valueOf(1),
                        false, TipoProduto.ROUPA), 2L),

                new ItemCompra(3L, new Produto(3L, "Chocolate Ao Leite", "Caixa com 500g",
                        BigDecimal.valueOf(10), BigDecimal.valueOf(0.5),
                        BigDecimal.valueOf(10), BigDecimal.valueOf(5), BigDecimal.valueOf(4),
                        false, TipoProduto.ALIMENTO), 1L));

        carrinho.setItens(items);
        BigDecimal subTotal = compraService.subTotalComDesconto(carrinho);

        assertThat(subTotal).isEqualByComparingTo("968.00");
    }
}
