package ecommerce.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ecommerce.dto.CompraDTO;
import ecommerce.dto.DisponibilidadeDTO;
import ecommerce.dto.EstoqueBaixaDTO;
import ecommerce.dto.PagamentoDTO;
import ecommerce.entity.CarrinhoDeCompras;
import ecommerce.entity.Cliente;
import ecommerce.entity.ItemCompra;
import ecommerce.entity.Produto;
import ecommerce.entity.Regiao;
import ecommerce.entity.TipoCliente;
import ecommerce.entity.TipoProduto;
import ecommerce.external.IEstoqueExternal;
import ecommerce.external.IPagamentoExternal;
import jakarta.transaction.Transactional;

@Service
public class CompraService {

	private final CarrinhoDeComprasService carrinhoService;
	private final ClienteService clienteService;

	private final IEstoqueExternal estoqueExternal;
	private final IPagamentoExternal pagamentoExternal;

	@Autowired
	public CompraService(CarrinhoDeComprasService carrinhoService, ClienteService clienteService,
			IEstoqueExternal estoqueExternal, IPagamentoExternal pagamentoExternal) {
		this.carrinhoService = carrinhoService;
		this.clienteService = clienteService;

		this.estoqueExternal = estoqueExternal;
		this.pagamentoExternal = pagamentoExternal;
	}

	@Transactional
	public CompraDTO finalizarCompra(Long carrinhoId, Long clienteId) {
		Cliente cliente = clienteService.buscarPorId(clienteId);
		CarrinhoDeCompras carrinho = carrinhoService.buscarPorCarrinhoIdEClienteId(carrinhoId, cliente);

		List<Long> produtosIds = carrinho.getItens().stream().map(i -> i.getProduto().getId())
				.collect(Collectors.toList());
		List<Long> produtosQtds = carrinho.getItens().stream().map(i -> i.getQuantidade()).collect(Collectors.toList());

		DisponibilidadeDTO disponibilidade = estoqueExternal.verificarDisponibilidade(produtosIds, produtosQtds);

		if (!disponibilidade.disponivel()) {
			throw new IllegalStateException("Itens fora de estoque.");
		}

		BigDecimal custoTotal = calcularCustoTotal(carrinho, cliente.getRegiao(), cliente.getTipo());

		PagamentoDTO pagamento = pagamentoExternal.autorizarPagamento(cliente.getId(), custoTotal.doubleValue());

		if (!pagamento.autorizado()) {
			throw new IllegalStateException("Pagamento não autorizado.");
		}

		EstoqueBaixaDTO baixaDTO = estoqueExternal.darBaixa(produtosIds, produtosQtds);

		if (!baixaDTO.sucesso()) {
			pagamentoExternal.cancelarPagamento(cliente.getId(), pagamento.transacaoId());
			throw new IllegalStateException("Erro ao dar baixa no estoque.");
		}

		CompraDTO compraDTO = new CompraDTO(true, pagamento.transacaoId(), "Compra finalizada com sucesso.");

		return compraDTO;
	}

	public BigDecimal calcularSubTotal(List<ItemCompra> itens) {
		BigDecimal total = BigDecimal.ZERO;

		for (ItemCompra item : itens) {
			BigDecimal produtoPreco = item.getProduto().getPreco();
			BigDecimal quantidade = BigDecimal.valueOf(item.getQuantidade());
			total = total.add(quantidade.multiply(produtoPreco));
		}

		return total;
	}

	public BigDecimal calculaDescontoPorTipo(List<ItemCompra> itens) {
		BigDecimal descontoTotal = BigDecimal.ZERO;

		// Map para acumular quantidade e subtotal por tipo de produto
		Map<TipoProduto, BigDecimal> subtotalPorTipo = new HashMap<>();
		Map<TipoProduto, Long> quantidadePorTipo = new HashMap<>();

		for (ItemCompra item : itens) {
			TipoProduto tipo = item.getProduto().getTipo();
			BigDecimal precoTotal = item.getProduto().getPreco()
					.multiply(BigDecimal.valueOf(item.getQuantidade()));

			subtotalPorTipo.put(tipo, subtotalPorTipo.getOrDefault(tipo, BigDecimal.ZERO).add(precoTotal));
			quantidadePorTipo.put(tipo, quantidadePorTipo.getOrDefault(tipo, 0L) + item.getQuantidade());
		}

		// Calcula desconto por tipo
		for (TipoProduto tipo : subtotalPorTipo.keySet()) {
			Long quantidade = quantidadePorTipo.get(tipo);
			BigDecimal subtotal = subtotalPorTipo.get(tipo);

			BigDecimal desconto = BigDecimal.ZERO;
			if (quantidade >= 3 && quantidade <= 4) {
				desconto = subtotal.multiply(BigDecimal.valueOf(0.05));
			} else if (quantidade >= 5 && quantidade <= 7) {
				desconto = subtotal.multiply(BigDecimal.valueOf(0.10));
			} else if (quantidade >= 8) {
				desconto = subtotal.multiply(BigDecimal.valueOf(0.15));
			}

			descontoTotal = descontoTotal.add(desconto);
		}

		return descontoTotal.setScale(2, RoundingMode.HALF_UP);
	}

	public BigDecimal calcularDescontoPorValorCarrinho(BigDecimal subTotal) {
		if (subTotal.compareTo(BigDecimal.ZERO) < 0) {
			throw new IllegalArgumentException("Subtotal inválido para cálculo de desconto.");
		}

		if (subTotal.compareTo(BigDecimal.valueOf(1000)) > 0) {
			return subTotal.multiply(BigDecimal.valueOf(0.2));
		} else if (subTotal.compareTo(BigDecimal.valueOf(500)) > 0) {
			return subTotal.multiply(BigDecimal.valueOf(0.1));
		}

		return BigDecimal.ZERO;
	}

	public BigDecimal calcularPesoTotal(List<ItemCompra> itens) {
		BigDecimal pesoTotal = BigDecimal.ZERO;

		for (ItemCompra item : itens) {
			BigDecimal produtoPeso = item.getProduto().getPesoFisico();
			BigDecimal pesoCubico = item.getProduto().getComprimento()
					.multiply(item.getProduto().getLargura())
					.multiply(item.getProduto().getAltura())
					.divide(BigDecimal.valueOf(6000), 2, RoundingMode.HALF_UP);

			BigDecimal quantidade = BigDecimal.valueOf(item.getQuantidade());
			pesoTotal = pesoTotal.add(quantidade.multiply(produtoPeso.max(pesoCubico))).setScale(2,
					RoundingMode.HALF_UP);
		}

		return pesoTotal;
	}

	public BigDecimal calcularFrete(Regiao regiao, List<ItemCompra> items) {
		if (regiao == null) {
			throw new IllegalArgumentException("Região inválida para cálculo de frete.");
		}

		BigDecimal pesoTotal = calcularPesoTotal(items);
		BigDecimal valorFrete = BigDecimal.ZERO;

		if (pesoTotal.compareTo(BigDecimal.ZERO) < 0) {
			throw new IllegalArgumentException("Peso total inválido para cálculo de frete.");
		}

		// Cálculo do frete baseado no peso total
		if (pesoTotal.compareTo(BigDecimal.valueOf(5)) == -1) {
			valorFrete = BigDecimal.ZERO;
		} else if (pesoTotal.compareTo(BigDecimal.valueOf(5)) > 0 && pesoTotal.compareTo(BigDecimal.valueOf(10)) <= 0) {
			valorFrete = pesoTotal
					.multiply(BigDecimal.valueOf(2))
					.add(BigDecimal.valueOf(12));
		} else if (pesoTotal.compareTo(BigDecimal.valueOf(10)) > 0
				&& pesoTotal.compareTo(BigDecimal.valueOf(50)) <= 0) {
			valorFrete = pesoTotal
					.multiply(BigDecimal.valueOf(4))
					.add(BigDecimal.valueOf(12));
		} else if (pesoTotal.compareTo(BigDecimal.valueOf(50)) > 0) {
			valorFrete = pesoTotal
					.multiply(BigDecimal.valueOf(7))
					.add(BigDecimal.valueOf(12));
		}

		// Adicional para produtos frágeis
		for (ItemCompra item : items) {
			if (item.getProduto().isFragil()) {
				valorFrete = valorFrete.add(BigDecimal.valueOf(5).multiply(BigDecimal.valueOf(item.getQuantidade())));
			}
		}

		// Valor do frete multiplicado pelo fator da região
		switch (regiao) {
			case SUL:
				valorFrete = valorFrete.multiply(BigDecimal.valueOf(1.05));
				break;
			case CENTRO_OESTE:
				valorFrete = valorFrete.multiply(BigDecimal.valueOf(1.2));
				break;
			case NORDESTE:
				valorFrete = valorFrete.multiply(BigDecimal.valueOf(1.1));
				break;
			case NORTE:
				valorFrete = valorFrete.multiply(BigDecimal.valueOf(1.3));
				break;
			default:
				break;
		}

		return valorFrete.setScale(2, RoundingMode.HALF_UP);
	}

	public BigDecimal calcularDescontoFrete(BigDecimal valorFrete, TipoCliente tipoCliente) {
		if (tipoCliente == null) {
			throw new IllegalArgumentException("Tipo de cliente inválido para cálculo de desconto no frete.");
		}

		if (tipoCliente == TipoCliente.OURO) {
			valorFrete = BigDecimal.ZERO;
		} else if (tipoCliente == TipoCliente.PRATA) {
			valorFrete = valorFrete.multiply(BigDecimal.valueOf(0.5)).setScale(2, RoundingMode.HALF_UP);
		}

		return valorFrete;
	}

	public BigDecimal subTotalComDesconto(CarrinhoDeCompras carrinho) {
		if (carrinho.getCliente() == null) {
			throw new IllegalArgumentException("O carrinho informado não possui um cliente válido.");
		}

		if (carrinho == null) {
			throw new IllegalArgumentException("O carrinho informado não é válido.");
		}

		// Lista de ItemProduto do carrinho
		List<ItemCompra> produtos = carrinho.getItens();

		if (produtos == null || produtos.isEmpty()) {
			throw new IllegalArgumentException("Lista de itens inválida.");
		}

		// Subtotal dos itens
		BigDecimal subTotal = calcularSubTotal(produtos);

		// Desconto: items do mesmo tipo
		BigDecimal descontoItem = calculaDescontoPorTipo(produtos);

		// Aplicação do desconto no subtotal
		subTotal = subTotal.subtract(descontoItem);

		// Desconto: valor do carrinho
		BigDecimal descontoCarrinho = calcularDescontoPorValorCarrinho(subTotal);

		// Aplicação do desconto no subtotal
		subTotal = subTotal.subtract(descontoCarrinho);

		return subTotal;
	}

	public BigDecimal freteComDesconto(CarrinhoDeCompras carrinho, Regiao regiao, TipoCliente tipoCliente) {
		if (carrinho.getCliente() == null) {
			throw new IllegalArgumentException("O carrinho informado não possui um cliente válido.");
		}

		if (carrinho == null) {
			throw new IllegalArgumentException("O carrinho informado não é válido.");
		}

		List<ItemCompra> produtos = carrinho.getItens();

		if (produtos == null) {
			throw new IllegalArgumentException("Lista de itens inválida.");
		}

		// Calcular frete com base nas regras
		BigDecimal frete = calcularFrete(regiao, produtos);

		// Aplicar desconto ou não ao frete com base nas regras de tipo de cliente
		frete = calcularDescontoFrete(frete, tipoCliente);

		return frete;
	}

	public BigDecimal calcularCustoTotal(CarrinhoDeCompras carrinho, Regiao regiao, TipoCliente tipoCliente) {
		// SubTotal com desconto
		BigDecimal subTotal = subTotalComDesconto(carrinho);
		// frete com desconto de nivel de cliente
		BigDecimal frete = freteComDesconto(carrinho, regiao, tipoCliente);

		BigDecimal valorTotalCarrinho = subTotal.add(frete)
				.setScale(2, RoundingMode.HALF_UP);

		return valorTotalCarrinho;
	}
}
