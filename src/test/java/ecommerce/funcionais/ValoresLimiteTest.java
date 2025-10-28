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

        // V1, V2, V3, V4, V5, V6, V7, V8, V9 - CT23, CT24, CT25, CT26, CT27, CT28, CT29 e CT30
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

        // V10, V11, V12, V13, V14, V15, V16, V17, V18 e V19 - CT31, CT32, CT33, CT34, CT35, CT36, CT37, CT38, CT39 e CT40
        @ParameterizedTest
        @CsvSource({
                        "0, 0, 0.00", 
                        "0, 0.01, 0.01",
                        "100, 150, 250.00", 
                        "499.98, 0, 499.98", 
                        "499.99, 0, 499.99", 
                        "250.00, 250.00, 500.00", 
                        "250.01, 250.00, 450.009", 
                        "500.00, 250.00, 675.00", 
                        "500.00, 500.00, 900.00", 
                        "500.01, 500.00, 800.008" 
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

        // V21, V22, V23, V24, V25, V26, V27, V28, V29, V30, V31, V32, V33, V34, V35 e V36 - CT41, CT42, CT43, CT44, CT45, CT46, CT47, CT48, CT49, CT50, CT51, CT52, CT53, CT54, CT55, CT56 
        @ParameterizedTest
        @CsvSource({
                        "0, 0, 0.00", 
                        "0, 0.01, 0.00",
                        "2.5, 0, 0.00", 
                        "4.99, 0, 0.00", 
                        "5.00, 0, 0.00", 
                        "5.01, 0, 22.02",
                        "5.02, 0, 22.04", 
                        "7.50, 0, 27.00", 
                        "9.99, 0, 31.98", 
                        "10.00, 0, 32.00", 
                        "10.01, 0, 52.04", 
                        "10.02, 0, 52.08", 
                        "49.99, 0, 211.96", 
                        "50.00, 0, 212.00",
                        "50.01, 0, 362.07", 
                        "50.02, 0, 362.14" 
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
