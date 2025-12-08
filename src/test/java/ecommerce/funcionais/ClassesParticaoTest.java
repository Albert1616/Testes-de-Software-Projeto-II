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
        void subTotalComDesconto_deveLancarExcecao_paraCarrinhoSemCliente() {
                CarrinhoDeCompras carrinhoSemCliente = new CarrinhoDeCompras();
                carrinhoSemCliente.setCliente(null);

                assertThrows(IllegalArgumentException.class, () -> {
                        compraService.subTotalComDesconto(carrinhoSemCliente);
                }, "O carrinho informado não possui um cliente válido.");
        }

        @Test
        void subTotalComDesconto_deveLancarExcecao_paraCarrinhoSemItens() {
                CarrinhoDeCompras carrinhoSemItens = new CarrinhoDeCompras();
                carrinhoSemItens.setItens(null);

                assertThrows(IllegalArgumentException.class, () -> {
                        compraService.subTotalComDesconto(carrinhoSemItens);
                }, "Lista de itens inválida.");
        }

        // P5 - Subtotal negativo - CT07
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

        // P6 - CT08
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

                assertThat(subTotal).isEqualByComparingTo("410.00")
                                .as("Sem desconto para total do carrinho menor que R$500,00");
        }

        // P7 - CT09
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

                assertThat(subTotal).isEqualByComparingTo("459.00")
                                .as("Desconto de 10% para total do carrinho entre R$500,00 e R$1000,00");
        }

        // P8 - CT10
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

                assertThat(subTotal).isEqualByComparingTo("968.00")
                                .as("Desconto de 20% para total do carrinho maior que R$1000,00");
        }

        @Test
        void CalcularFretePorPesoTotalDeveLancarExcecaoParaPesoTotalNulo() {
                carrinho.setItens(null);

                assertThrows(IllegalArgumentException.class, () -> {
                        compraService.frete(carrinho);
                }, "Lista de itens inválida.");
        }

        @Test
        void CalcularFretePorPesoTotalDeveLancarExcecaoParaPesoTotalNegativo() {
                items = Arrays.asList(
                                new ItemCompra(1L, new Produto(1L, "Smartphone X1", "Smartphone top de linha",
                                                BigDecimal.valueOf(10), BigDecimal.valueOf(-0.3),
                                                BigDecimal.valueOf(15), BigDecimal.valueOf(-7), BigDecimal.valueOf(0.8),
                                                false, TipoProduto.ELETRONICO), 1L));
                carrinho.setItens(items);

                assertThrows(IllegalArgumentException.class, () -> {
                        compraService.frete(carrinho);
                }, "Peso total inválido para cálculo de frete.");
        }

        @Test
        void calcularFrete_deveSerZero_paraPesoMenorQueCinco() {
                List<ItemCompra> items = Arrays.asList(
                                new ItemCompra(1L,
                                                new Produto(1L, "Produto leve", "", BigDecimal.valueOf(2),
                                                                BigDecimal.valueOf(2),
                                                                null, null, null, false, TipoProduto.ALIMENTO),
                                                1L));

                BigDecimal frete = compraService.calcularFrete(items);
                assertThat(frete).isEqualByComparingTo(BigDecimal.ZERO);
        }

        @Test
        void calcularFrete_deveMultiplicarPorDois_paraPesoEntre5e10() {
                List<ItemCompra> items = Arrays.asList(
                                new ItemCompra(1L,
                                                new Produto(1L, "Produto médio", "", BigDecimal.valueOf(7),
                                                                BigDecimal.valueOf(6),
                                                                null, null, null, false, TipoProduto.ROUPA),
                                                1L));

                BigDecimal frete = compraService.calcularFrete(items);
                assertThat(frete).isEqualByComparingTo(BigDecimal.valueOf(12)); // 7*2
        }

        @Test
        void calcularFrete_deveMultiplicarPorQuatro_paraPesoEntre10e50() {
                List<ItemCompra> items = Arrays.asList(
                                new ItemCompra(1L,
                                                new Produto(1L, "Produto pesado", "", BigDecimal.valueOf(20),
                                                                BigDecimal.valueOf(20),
                                                                null, null, null, false, TipoProduto.ELETRONICO),
                                                1L));

                BigDecimal frete = compraService.calcularFrete(items);
                assertThat(frete).isEqualByComparingTo(BigDecimal.valueOf(80)); // 20*4
        }

        @Test
        void calcularFrete_deveMultiplicarPorSete_paraPesoMaiorQue50() {
                List<ItemCompra> items = Arrays.asList(
                                new ItemCompra(1L,
                                                new Produto(1L, "Produto gigante", "", BigDecimal.valueOf(60),
                                                                BigDecimal.valueOf(70),
                                                                null, null, null, false, TipoProduto.LIVRO),
                                                1L));

                BigDecimal frete = compraService.calcularFrete(items);
                assertThat(frete).isEqualByComparingTo(BigDecimal.valueOf(490)); // 60*7
        }

        // P10 - CT12
        @Test
        void CalcularFretePorPesoTotal_ParaPesoTotalEntre0E5_EntaoFreteInsento() {
                items = Arrays.asList(
                                new ItemCompra(1L, new Produto(1L, "Smartphone X1", "Smartphone top de linha",
                                                BigDecimal.valueOf(100), BigDecimal.valueOf(0.9),
                                                BigDecimal.valueOf(1), BigDecimal.valueOf(1), BigDecimal.valueOf(0.8),
                                                false, TipoProduto.ELETRONICO), 2L),

                                new ItemCompra(2L, new Produto(2L, "Camisa Polo", "Camisa social masculina",
                                                BigDecimal.valueOf(500), BigDecimal.valueOf(0.85),
                                                BigDecimal.valueOf(1), BigDecimal.valueOf(2), BigDecimal.valueOf(1),
                                                false, TipoProduto.ROUPA), 2L));

                carrinho.setItens(items);
                BigDecimal frete = compraService.frete(carrinho);

                assertThat(frete).isEqualByComparingTo("0.00")
                                .as("Frete isento para peso total entre 0 e 5 kg");
        }

        // P11 - CT13
        @Test
        void CalcularFretePorPesoTotal_ParaPesoTotalEntre5E10_EntaoFreteDe2PorKG() {
                items = Arrays.asList(
                                new ItemCompra(1L, new Produto(1L, "Smartphone X1", "Smartphone top de linha",
                                                BigDecimal.valueOf(100), BigDecimal.valueOf(5),
                                                BigDecimal.valueOf(1), BigDecimal.valueOf(1), BigDecimal.valueOf(0.8),
                                                false, TipoProduto.ELETRONICO), 1L),

                                new ItemCompra(2L, new Produto(2L, "Camisa Polo", "Camisa social masculina",
                                                BigDecimal.valueOf(500), BigDecimal.valueOf(5),
                                                BigDecimal.valueOf(1), BigDecimal.valueOf(2), BigDecimal.valueOf(1),
                                                false, TipoProduto.ROUPA), 1L));

                carrinho.setItens(items);
                BigDecimal frete = compraService.frete(carrinho);

                assertThat(frete).isEqualByComparingTo("20.00")
                                .as("Frete de R$2,00 por kg para peso total entre 5 e 10 kg");
        }

        // P12 - CT14
        @Test
        void CalcularFretePorPesoTotal_ParaPesoTotalEntre10E50_EntaoFreteDe4PorKG() {
                items = Arrays.asList(
                                new ItemCompra(1L, new Produto(1L, "Smartphone X1", "Smartphone top de linha",
                                                BigDecimal.valueOf(100), BigDecimal.valueOf(10),
                                                BigDecimal.valueOf(1), BigDecimal.valueOf(1), BigDecimal.valueOf(0.8),
                                                false, TipoProduto.ELETRONICO), 1L),

                                new ItemCompra(2L, new Produto(2L, "Camisa Polo", "Camisa social masculina",
                                                BigDecimal.valueOf(500), BigDecimal.valueOf(20),
                                                BigDecimal.valueOf(1), BigDecimal.valueOf(2), BigDecimal.valueOf(1),
                                                false, TipoProduto.ROUPA), 1L));

                carrinho.setItens(items);
                BigDecimal frete = compraService.frete(carrinho);

                assertThat(frete).isEqualByComparingTo("120.00")
                                .as("Frete de R$4,00 por kg para peso total entre 10 e 50 kg");
        }

        // P13 - CT15
        @Test
        void CalcularFretePorPesoTotal_ParaPesoTotalMaiorQue50_EntaoFreteDe7PorKG() {
                items = Arrays.asList(
                                new ItemCompra(1L, new Produto(1L, "Smartphone X1", "Smartphone top de linha",
                                                BigDecimal.valueOf(100), BigDecimal.valueOf(30),
                                                BigDecimal.valueOf(1), BigDecimal.valueOf(1), BigDecimal.valueOf(0.8),
                                                false, TipoProduto.ELETRONICO), 1L),

                                new ItemCompra(2L, new Produto(2L, "Camisa Polo", "Camisa social masculina",
                                                BigDecimal.valueOf(500), BigDecimal.valueOf(30),
                                                BigDecimal.valueOf(1), BigDecimal.valueOf(2), BigDecimal.valueOf(1),
                                                false, TipoProduto.ROUPA), 1L));

                carrinho.setItens(items);
                BigDecimal frete = compraService.frete(carrinho);

                assertThat(frete).isEqualByComparingTo("420.00")
                                .as("Frete de R$7,00 por kg para peso total maior que 50 kg");
        }

        // P15 - CT16
        @Test
        void CalcularFretePorPesoTotalSemFragilidade_ParaNenhumItemComFragilidade_EntaoSemTaxaDeManuseio() {
                Regiao regiao = Regiao.SUDESTE;
                TipoCliente tipoCliente = TipoCliente.BRONZE;

                items = Arrays.asList(
                                new ItemCompra(1L, new Produto(1L, "Smartphone X1", "Smartphone top de linha",
                                                BigDecimal.valueOf(100), BigDecimal.valueOf(10),
                                                BigDecimal.valueOf(1), BigDecimal.valueOf(1), BigDecimal.valueOf(0.8),
                                                false, TipoProduto.ELETRONICO), 1L),

                                new ItemCompra(2L, new Produto(2L, "Camisa Polo", "Camisa social masculina",
                                                BigDecimal.valueOf(500), BigDecimal.valueOf(10),
                                                BigDecimal.valueOf(1), BigDecimal.valueOf(2), BigDecimal.valueOf(1),
                                                false, TipoProduto.ROUPA), 1L));

                carrinho.setItens(items);
                BigDecimal frete = compraService.frete(carrinho);

                assertThat(frete).isEqualByComparingTo("80.00")
                                .as("Sem taxa de manuseio para nenhum item com fragilidade");
        }

        // P14 - CT17
        @Test
        void CalcularFretePorPesoTotalComFragilidade_ParaItemsComFragilidade_EntaoTaxaDeManuseioDe5XQuantidade() {
                items = Arrays.asList(
                                new ItemCompra(1L, new Produto(1L, "Smartphone X1", "Smartphone top de linha",
                                                BigDecimal.valueOf(100), BigDecimal.valueOf(10),
                                                BigDecimal.valueOf(1), BigDecimal.valueOf(1), BigDecimal.valueOf(0.8),
                                                true, TipoProduto.ELETRONICO), 1L),

                                new ItemCompra(2L, new Produto(2L, "Camisa Polo", "Camisa social masculina",
                                                BigDecimal.valueOf(500), BigDecimal.valueOf(10),
                                                BigDecimal.valueOf(1), BigDecimal.valueOf(2), BigDecimal.valueOf(1),
                                                true, TipoProduto.ROUPA), 1L));

                carrinho.setItens(items);
                BigDecimal frete = compraService.frete(carrinho);

                assertThat(frete).isEqualByComparingTo("90.00");
        }

        @Test
        void CarrinhoNuloEmSubTotalComDesconto() {
                TipoCliente tipoCliente = TipoCliente.BRONZE;

                carrinho = null;

                assertThrows(IllegalArgumentException.class, () -> {
                        compraService.subTotalComDesconto(carrinho);
                }, "O carrinho informado não é válido.");
        }

        @Test
        void CarrinhoNuloEmFreteComDesconto() {
                TipoCliente tipoCliente = TipoCliente.BRONZE;
                Regiao regiao = Regiao.NORTE;

                carrinho = null;

                assertThrows(IllegalArgumentException.class, () -> {
                        compraService.frete(carrinho);
                }, "O carrinho informado não é válido.");
        }

        @Test
        void ItensNuloEmFreteComDesconto() {
                TipoCliente tipoCliente = TipoCliente.BRONZE;
                Regiao regiao = Regiao.NORTE;

                carrinho.setItens(null);
                ;

                assertThrows(IllegalArgumentException.class, () -> {
                        compraService.frete(carrinho);
                }, "Lista de itens inválida.");
        }

        @Test
        void ClienteNuloEmFreteComDesconto() {
                TipoCliente tipoCliente = TipoCliente.BRONZE;
                Regiao regiao = Regiao.NORTE;

                carrinho.setCliente(null);
                ;

                assertThrows(IllegalArgumentException.class, () -> {
                        compraService.frete(carrinho);
                }, "O carrinho informado não possui um cliente válido.");
        }

        @Test
        void CalcularCustoTotalComSucesso() {
                items = Arrays.asList(
                                new ItemCompra(1L, new Produto(1L, "Smartphone X1", "Smartphone top de linha",
                                                BigDecimal.valueOf(100), BigDecimal.valueOf(10),
                                                BigDecimal.valueOf(1), BigDecimal.valueOf(1), BigDecimal.valueOf(0.8),
                                                true, TipoProduto.ELETRONICO), 1L),

                                new ItemCompra(2L, new Produto(2L, "Camisa Polo", "Camisa social masculina",
                                                BigDecimal.valueOf(500), BigDecimal.valueOf(10),
                                                BigDecimal.valueOf(1), BigDecimal.valueOf(2), BigDecimal.valueOf(1),
                                                true, TipoProduto.ROUPA), 1L));

                carrinho.setItens(items);
                BigDecimal custoTotal = compraService.calcularCustoTotal(carrinho);

                assertThat(custoTotal).isEqualByComparingTo("630.00")
                                .as("Cálculo de custo total");
        }
}
