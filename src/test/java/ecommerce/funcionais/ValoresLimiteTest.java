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

public class ValoresLimiteTest {
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

        // V1, V2, V3, V4, V5, V6, V7, V8, V9
        @ParameterizedTest
        @CsvSource({
                        "2, 0, 20.00", // Q = 2, sem desconto
                        "3, 0, 28.50", // Q = 3, 5% desconto
                        "4, 0, 38.00", // Q = 4, 5% desconto
                        "5, 0, 45.00", // Q = 5, 10% desconto
                        "6, 0, 54.00", // Q = 6, 10% desconto
                        "7, 0, 63.00", // Q = 7, 10% desconto
                        "8, 0, 68.00", // Q = 8, 15% desconto
                        "9, 0, 76.50" // Q = 9, 15% desconto
        })
        void CalcularDescontoPorTipoDeProduto_ParaDiferentesValoresLimites_EntaoCalculaDescontoCorreto(Long quantidade1,
                        Long quantidade2, String valorEsperado) {
                items = Arrays.asList(
                                new ItemCompra(1L, new Produto(1L, "Smartphone X1", "Smartphone top de linha",
                                                BigDecimal.valueOf(10), BigDecimal.valueOf(0.3),
                                                BigDecimal.valueOf(15), BigDecimal.valueOf(7), BigDecimal.valueOf(0.8),
                                                false, TipoProduto.ELETRONICO), quantidade1),

                                new ItemCompra(2L, new Produto(2L, "Camisa Polo", "Camisa social masculina",
                                                BigDecimal.valueOf(20), BigDecimal.valueOf(0.25),
                                                BigDecimal.valueOf(30), BigDecimal.valueOf(20), BigDecimal.valueOf(1),
                                                false, TipoProduto.ROUPA), quantidade2));

                carrinho.setItens(items);
                BigDecimal subTotal = compraService.subTotalComDesconto(carrinho);

                assertThat(subTotal).isEqualByComparingTo(valorEsperado)
                                .as("Desconto por tipo de produto para" + quantidade1 + " unidades do produto 1 e "
                                                + quantidade2 + " unidades do produto 2");
        }

        @ParameterizedTest
        @CsvSource({
                        "0, 0, 0.00", // V10
                        "0, 0.01, 0.01", // V11
                        "100, 150, 250.00", // V12
                        "499.98, 0, 499.98", // V13
                        "499.99, 0, 499.99", // V14
                        "250.00, 250.00, 500.00", // V15
                        "250.01, 250.00, 450.009", // V16
                        "500.00, 250.00, 675.00", // V17
                        "500.00, 500.00, 900.00", // V18
                        "500.01, 500.00, 800.008" // V19
        })
        void CalcularDescontoPorValorTotalDeCarrinho_ParaDiferentesValoresLimites_EntaoCalculaDescontoCorreto(
                        String preco1,
                        String preco2, String valorEsperado) {
                items = Arrays.asList(
                                new ItemCompra(1L, new Produto(1L, "Smartphone X1", "Smartphone top de linha",
                                                new BigDecimal(preco1), BigDecimal.valueOf(0.3),
                                                BigDecimal.valueOf(15), BigDecimal.valueOf(7), BigDecimal.valueOf(0.8),
                                                false, TipoProduto.ELETRONICO), 1L),

                                new ItemCompra(2L, new Produto(2L, "Camisa Polo", "Camisa social masculina",
                                                new BigDecimal(preco2), BigDecimal.valueOf(0.25),
                                                BigDecimal.valueOf(30), BigDecimal.valueOf(20), BigDecimal.valueOf(1),
                                                false, TipoProduto.ROUPA), 1L));

                carrinho.setItens(items);
                BigDecimal subTotal = compraService.subTotalComDesconto(carrinho);

                assertThat(subTotal).isEqualByComparingTo(valorEsperado)
                                .as("Desconto por valor total do carrinho para produtos com preços " + preco1 + " e "
                                                + preco2);
        }

        @ParameterizedTest
        @CsvSource({
                        "0, 0, 0.00", // V21
                        "0, 0.01, 0.00", // V22
                        "2.5, 0, 0.00", // V23
                        "4.99, 0, 0.00", // V24
                        "5.00, 0, 0.00", // V25
                        "5.01, 0, 22.02", // V26
                        "5.02, 0, 22.04", // V27
                        "7.50, 0, 27.00", // V28
                        "9.99, 0, 31.98", // V29
                        "10.00, 0, 32.00", // V30
                        "10.01, 0, 52.04", // V31
                        "10.02, 0, 52.08", // V32
                        "49.99, 0, 211.96", // V33
                        "50.00, 0, 212.00", // V34
                        "50.01, 0, 362.07", // V35
                        "50.02, 0, 362.14" // V36
        })
        void CalcularFretePorPesoTotal_ParaDiferentesValoresLimites_EntaoCalculaFreteCorreto(String peso1, String peso2,
                        String valorEsperado) {
                Regiao regiao = Regiao.SUDESTE;
                TipoCliente tipoCliente = TipoCliente.BRONZE;

                items = Arrays.asList(
                                new ItemCompra(1L, new Produto(1L, "Smartphone X1", "Smartphone top de linha",
                                                BigDecimal.valueOf(100), new BigDecimal(peso1),
                                                BigDecimal.valueOf(1), BigDecimal.valueOf(1), BigDecimal.valueOf(0.8),
                                                false, TipoProduto.ELETRONICO), 1L),

                                new ItemCompra(2L, new Produto(2L, "Camisa Polo", "Camisa social masculina",
                                                BigDecimal.valueOf(500), new BigDecimal(peso2),
                                                BigDecimal.valueOf(1), BigDecimal.valueOf(2), BigDecimal.valueOf(1),
                                                false, TipoProduto.ROUPA), 1L));

                carrinho.setItens(items);
                BigDecimal frete = compraService.freteComDesconto(carrinho, regiao, tipoCliente);

                assertThat(frete).isEqualByComparingTo(valorEsperado)
                                .as("Frete por peso total para produtos com pesos " + peso1 + " e " + peso2);
        }
}
