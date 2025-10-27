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

        // Lista de itens inválida
        @Test
        void CalcularDescontoPorTipoDeProdutoDeveLancarExcecaoParaListaDeItensInvalida() {
                carrinho.setItens(null);
                assertThrows(IllegalArgumentException.class, () -> {
                        compraService.subTotalComDesconto(carrinho);
                        ;
                }, "Lista de itens inválida.");
        }

        // Carrinho com cliente inválido
        @Test
        void CalcularDescontoPorTipoDeProdutoDeveLancarExcecaoParaClienteInvalido() {
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
                carrinho.setCliente(null);
                assertThrows(IllegalArgumentException.class, () -> {
                        compraService.subTotalComDesconto(carrinho);
                        ;
                }, "O cliente informado no carrinho é inválido.");
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

                assertThat(subTotal).isEqualByComparingTo("430.00").as("Sem desconto para nenhum item do mesmo tipo");
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

                assertThat(subTotal).isEqualByComparingTo("485.50")
                                .as("Desconto de 5% para mais de 3 a 4 items do mesmo tipo");
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

                assertThat(subTotal).isEqualByComparingTo("454.00")
                                .as("Desconto de 10% para mais de 5 a 7 items do mesmo tipo");
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

                assertThat(subTotal).isEqualByComparingTo("485.00")
                                .as("Desconto de 15% para mais de 8 items do mesmo tipo");
        }

        // P5 - Subtotal negativo
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

                assertThat(subTotal).isEqualByComparingTo("410.00")
                                .as("Sem desconto para total do carrinho menor que R$500,00");
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

                assertThat(subTotal).isEqualByComparingTo("459.00")
                                .as("Desconto de 10% para total do carrinho entre R$500,00 e R$1000,00");
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

                assertThat(subTotal).isEqualByComparingTo("968.00")
                                .as("Desconto de 20% para total do carrinho maior que R$1000,00");
        }

        // P9 - Peso total negativo
        @Test
        void CalcularFretePorPesoTotalDeveLancarExcecaoParaPesoTotalNegativo() {
                items = Arrays.asList(
                                new ItemCompra(1L, new Produto(1L, "Smartphone X1", "Smartphone top de linha",
                                                BigDecimal.valueOf(10), BigDecimal.valueOf(-0.3),
                                                BigDecimal.valueOf(15), BigDecimal.valueOf(-7), BigDecimal.valueOf(0.8),
                                                false, TipoProduto.ELETRONICO), 1L));
                carrinho.setItens(items);

                assertThrows(IllegalArgumentException.class, () -> {
                        compraService.freteComDesconto(carrinho, Regiao.SUDESTE, TipoCliente.BRONZE);
                }, "Peso total inválido para cálculo de frete.");
        }

        // P10
        @Test
        void CalcularFretePorPesoTotal_ParaPesoTotalEntre0E5_EntaoFreteInsento() {
                Regiao regiao = Regiao.SUDESTE;
                TipoCliente tipoCliente = TipoCliente.BRONZE;

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
                BigDecimal frete = compraService.freteComDesconto(carrinho, regiao, tipoCliente);

                assertThat(frete).isEqualByComparingTo("0.00")
                                .as("Frete isento para peso total entre 0 e 5 kg");
        }

        // P11
        @Test
        void CalcularFretePorPesoTotal_ParaPesoTotalEntre5E10_EntaoFreteDe2PorKG() {
                Regiao regiao = Regiao.SUDESTE;
                TipoCliente tipoCliente = TipoCliente.BRONZE;

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
                BigDecimal frete = compraService.freteComDesconto(carrinho, regiao, tipoCliente);

                assertThat(frete).isEqualByComparingTo("32.00")
                                .as("Frete de R$2,00 por kg para peso total entre 5 e 10 kg");
        }

        // P12
        @Test
        void CalcularFretePorPesoTotal_ParaPesoTotalEntre10E50_EntaoFreteDe4PorKG() {
                Regiao regiao = Regiao.SUDESTE;
                TipoCliente tipoCliente = TipoCliente.BRONZE;

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
                BigDecimal frete = compraService.freteComDesconto(carrinho, regiao, tipoCliente);

                assertThat(frete).isEqualByComparingTo("132.00")
                                .as("Frete de R$4,00 por kg para peso total entre 10 e 50 kg");
        }

        // P13
        @Test
        void CalcularFretePorPesoTotal_ParaPesoTotalMaiorQue50_EntaoFreteDe7PorKG() {
                Regiao regiao = Regiao.SUDESTE;
                TipoCliente tipoCliente = TipoCliente.BRONZE;

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
                BigDecimal frete = compraService.freteComDesconto(carrinho, regiao, tipoCliente);

                assertThat(frete).isEqualByComparingTo("432.00")
                                .as("Frete de R$7,00 por kg para peso total maior que 50 kg");
        }

        // P14
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
                BigDecimal frete = compraService.freteComDesconto(carrinho, regiao, tipoCliente);

                assertThat(frete).isEqualByComparingTo("92.00")
                                .as("Sem taxa de manuseio para nenhum item com fragilidade");
        }

        // P14
        @Test
        void CalcularFretePorPesoTotalComFragilidade_ParaItemsComFragilidade_EntaoTaxaDeManuseioDe5XQuantidade() {
                Regiao regiao = Regiao.SUDESTE;
                TipoCliente tipoCliente = TipoCliente.BRONZE;

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
                BigDecimal frete = compraService.freteComDesconto(carrinho, regiao, tipoCliente);

                assertThat(frete).isEqualByComparingTo("102.00");
        }

        // P16
        @Test
        void CalcularFreteComDescontoPorPesoTipoDeCliente_ParaTipoDeClienteOuro_EntaoFreteInsento() {
                Regiao regiao = Regiao.SUDESTE;
                TipoCliente tipoCliente = TipoCliente.OURO;

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
                BigDecimal frete = compraService.freteComDesconto(carrinho, regiao, tipoCliente);

                assertThat(frete).isEqualByComparingTo("0.00")
                                .as("Frete isento para tipo de cliente Ouro");
        }

        // P17
        @Test
        void CalcularFreteComDescontoPorPesoTipoDeCliente_ParaTipoDeClientePrata_EntaoFreteComDescontoDe50Porcento() {
                Regiao regiao = Regiao.SUDESTE;
                TipoCliente tipoCliente = TipoCliente.PRATA;

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
                BigDecimal frete = compraService.freteComDesconto(carrinho, regiao, tipoCliente);

                assertThat(frete).isEqualByComparingTo("51.00")
                                .as("Frete com desconto de 50% para tipo de cliente Prata");
        }

        // P18 - Tipo de cliente nulo
        @Test
        void CalcularFreteComDescontoPorPesoTipoDeClienteDeveRetornarExcecaoParaTipoDeClienteInvalido() {
                Regiao regiao = Regiao.SUDESTE;

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

                assertThrows(IllegalArgumentException.class, () -> {
                        compraService.freteComDesconto(carrinho, regiao, null);
                }, "Tipo de cliente inválido para calculo de desconto do frete.");
        }

        // P19, P20, P21, P22, P23, P24
        @ParameterizedTest
        @CsvSource({
                        "NORTE, 41.60",
                        "NORDESTE, 35.20",
                        "SUDESTE, 32.00",
                        "SUL, 33.60",
                        "CENTRO_OESTE, 38.40"
        })
        void CalcularFretePorRegiao_ParaDiferentesRegioes_EntaoFreteCorreto(Regiao regiao, String valorEsperado) {
                TipoCliente tipoCliente = TipoCliente.BRONZE;

                items = Arrays.asList(
                                new ItemCompra(1L, new Produto(1L, "Smartphone X1", "Smartphone top de linha",
                                                BigDecimal.valueOf(10), BigDecimal.valueOf(10),
                                                BigDecimal.valueOf(1), BigDecimal.valueOf(1), BigDecimal.valueOf(0.8),
                                                false, TipoProduto.ELETRONICO), 1L));

                carrinho.setItens(items);
                BigDecimal frete = compraService.freteComDesconto(carrinho, regiao, tipoCliente);

                assertThat(frete).isEqualByComparingTo(valorEsperado)
                                .as("Frete correto para a região " + regiao);
        }

        // P25 - Região nula
        @Test
        void CalcularFretePorRegiaoDeveRetornarExcecaoParaRegiaoInvalida() {
                TipoCliente tipoCliente = TipoCliente.BRONZE;

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

                assertThrows(IllegalArgumentException.class, () -> {
                        compraService.freteComDesconto(carrinho, null, tipoCliente);
                }, "Regiao inválida para calculo do frete.");
        }
}
