## Classes de partição

| Partição | Quantidade de itens do mesmo tipo  (Q)|
|-------|----------------------------|
| P1    | Q < 3 |
| P2    | 3 <= Q <= 4|
| P3    | 5 <= Q <= 7 |
| P4    | Q >= 8 |

| Partição | Subtotal do carrinho  (T)|
|-----------|---------------|
| P5        | T < `R$ 0,00` |
| P6        | `R$ 0,00` <= T < `R$ 500,00` |
| P7        | `R$ 500,00` <= T <= `R$ 1000,00`|
| P8        | T > `R$ 1000,00` |

| Partição | Peso (frete) (P)|
|-----------|---------------|
| P9        | P < 0,00 |
| P10        | 0,00 <= P <= 5,00|
| P11        | 5,00 < P <= 10,00 |
| P12        | 10,00 < P <= 50,00 |
| P13        | P > 50,00 |

| Partição | Fragilidade do item (frete) (F)|
|-----------|---------------|
| P14        | `Frágil` |
| P15        | `Não é frágil`|

| Partição | Nível do cliente (N)|
|-----------|---------------|
| P16        | `Ouro` |
| P17        | `Prata` |
| P18        | `Bronze` |
| P19        | `Outro` |

| Partição    | Região (R)
|-----------|---------------|
| P20        | `Sudeste` |
| P21        | `Sul` |
| P22        | `Nordeste` |
| P23        | `Centro-Oeste` |
| P24        | `Norte` |
| P25        | `Outro` |

## Valores limite

| ID  | Valor Limite | Partições cobertas |
|-----|--------------|------------------|
| V1  | Q = 2        | P2(min-)         |
| V2  | Q = 3        | P2(min)(max-)    |
| V3  | Q = 4        | P2(min+)(max) / P3(min-) |
| V4  | Q = 5        | P2(max+) / P3(min) |
| V5  | Q = 6        | P3(min+)(max-)   |
| V6  | Q = 7        | P3(max) / P4(min-) |
| V7  | Q = 8        | P3(max+) / P4(min) |
| V8  | Q = 9        | P4(min+)         |

| ID  | Valor Limite | Partições cobertas |
|-----|--------------|------------------|
| V9  | T = -0,01    | P6(min-)         |
| V10 | T = 0,00     | P6(min)          |
| V11 | T = 0,01     | P6(min+)         |
| V12 | T = 250,00   | P6               |
| V13 | T = 499,98   | P6(max-)         |
| V14 | T = 499,99   | P6(max) / P7(min-) |
| V15 | T = 500,00   | P7(min) / P6(max+) |
| V16 | T = 500,01   | P7(min+)         |
| V17 | T = 750,00   | P7               |
| V18 | T = 1000,00  | P7(max) / P8(min-) |
| V19 | T = 1000,01  | P7(max+) / P8(min) |

| ID  | Valor Limite | Partições cobertas |
|-----|--------------|------------------|
| V20 | P = -0,01    | P10(min-)        |
| V21 | P = 0,00     | P10(min)         |
| V22 | P = 0,01     | P10(min+)        |
| V23 | P = 2,50     | P10              |
| V24 | P = 4,99     | P10(max-)        |
| V25 | P = 5,00     | P10(max) / P11(min-) |
| V26 | P = 5,01     | P10(max+) / P11(min) |
| V27 | P = 5,02     | P11(min+)        |
| V28 | P = 7,50     | P11              |
| V29 | P = 9,99     | P11(max-)        |
| V30 | P = 10,00    | P11(max) / P12(min-) |
| V31 | P = 10,01    | P12(min)         |
| V32 | P = 10,02    | P12(min+)        |
| V33 | P = 49,99    | P12(max-)        |
| V34 | P = 50,00    | P12(max) / P13(min-) |
| V35 | P = 50,01    | P12(max+) / P13(min) |
| V36 | P = 50,02    | P13(min+)        |

## Tabela de decisão

| **Condição** | **R1** | **R2** | **R3** | **R4** | **R5** | **R6** | **R7** | **R8** | **R9** | **R10** | **R11** | **R12** |
|:-------------|:------:|:------:|:------:|:------:|:------:|:------:|:------:|:------:|:------:|:-------:|:-------:|:-------:|
| **Q = quantidade de itens do mesmo tipo** | 1–2 | 3–4 | 5–7 | ≥8 | 1–2 | 3–4 | 5–7 | ≥8 | 1–2 | 3–4 | 5–7 | ≥8 |
| **T = subtotal (R$)** | ≤500 | ≤500 | ≤500 | ≤500 | 500 < T ≤ 1000 | 500 < T ≤ 1000 | 500 < T ≤ 1000 | 500 < T ≤ 1000 | >1000 | >1000 | >1000 | >1000 |

## Casos de teste

| ID | Entrada | Resultado Esperado | Critério Coberto |
|----|---------|------------------|-----------------|
| CT01 | Carrinho com lista de itens = null | `IllegalArgumentException` | Validação / Robustez |
| CT02 | Carrinho com cliente = null | `IllegalArgumentException` | Validação / Robustez |
| CT03 | Nenhum item do mesmo tipo | Subtotal = 430.00 | Partição P1 |
| CT04 | 3 a 4 itens do mesmo tipo | Subtotal = 485.50 | Partição P2 |
| CT05 | 5 a 7 itens do mesmo tipo | Subtotal = 454.00 | Partição P3 |
| CT06 | 8 ou mais itens do mesmo tipo | Subtotal = 485.00 | Partição P4 |
| CT07 | Subtotal negativo | `IllegalArgumentException` | Validação / Robustez |
| CT08 | Subtotal < 500 | Subtotal = 410.00 | Partição P6 |
| CT09 | Subtotal entre 500 e 1000 | Subtotal = 459.00 | Partição P7 |
| CT10 | Subtotal > 1000 | Subtotal = 968.00 | Partição P8 |
| CT11 | Peso total negativo | `IllegalArgumentException` | Validação / Robustez |
| CT12 | Peso total entre 0 e 5 kg | Frete = 0.00 | Partição P10 |
| CT13 | Peso total entre 5 e 10 kg | Frete = 32.00 | Partição P11 |
| CT14 | Peso total entre 10 e 50 kg | Frete = 132.00 | Partição P12 |
| CT15 | Peso total > 50 kg | Frete = 432.00 | Partição P13 |
| CT16 | Itens não frágeis | Frete = 92.00 | Partição P15 |
| CT17 | Itens frágeis | Frete = 102.00 | Partição P14 |
| CT18 | Tipo de cliente Ouro | Frete = 0.00 | Partição P16 |
| CT19 | Tipo de cliente Prata | Frete = 51.00 | Partição P17 |
| CT20 | Tipo de cliente inválido | `IllegalArgumentException` | Robustez / Validação |
| CT21 | Diferentes regiões | Frete conforme multiplicador da região | Partições P20, P21, P22, P23 e P24 |
| CT22 | Região inválida | `IllegalArgumentException` | Robustez / Validação |
| CT23 | Quantidade de itens do mesmo tipo = 2   | Subtotal = 20.00 | Valor limite V1         |
| CT24 | Quantidade de itens do mesmo tipo = 3   | Subtotal = 28.50 | Valor limite V2    |
| CT25 | Quantidade de itens do mesmo tipo = 4   | Subtotal = 38.00 | Valor limite V3 |
| CT26 | Quantidade de itens do mesmo tipo = 5   | Subtotal = 45.00 | Valor limte V4 |
| CT27 | Quantidade de itens do mesmo tipo = 6   | Subtotal = 54.00 | Valor limite V5 |
| CT28 | Quantidade de itens do mesmo tipo = 7   | Subtotal = 63.00 | Valor limite V6 |
| CT29 | Quantidade de itens do mesmo tipo = 8   | Subtotal = 68.00 | Valor limite V7 |
| CT30 | Quantidade de itens do mesmo tipo = 9   | Subtotal = 76.50 | Valor limite V8         |
| CT31 | Subtotal do carrinho = 0.00   | Subtotal = 0.00  | Valor limite V9|
| CT32 | Subtotal do carrinho = 0.01   | Subtotal = 0.01  | Valor limite V10 |
| CT33 | Subtotal do carrinho = 100.00 | Subtotal = 250.00| Valor limite V11 |
| CT34 | Subtotal do carrinho = 499.98 | Subtotal = 499.98| Valor limite V12        |
| CT35 | Subtotal do carrinho = 499.99 | Subtotal = 499.99| Valor limite V13 |
| CT36 | Subtotal do carrinho = 500.00 | Subtotal = 500.00| Valor limite V14 |
| CT37 | Subtotal do carrinho = 500.01 | Subtotal = 450.009| Valor limite V15        |
| CT38 | Subtotal do carrinho = 750.00 | Subtotal = 675.00| Valor limite V16 |
| CT39 | Subtotal do carrinho = 1000.0 | Subtotal = 900.00| Valor limite V17 |
| CT40 | Subtotal do carrinho = 1000.0 | Subtotal = 800.008| Valor limite V18 |
| CT41 | Peso total = 0.00   | Frete = 0.00     | Valor limite V19         |
| CT42 | Peso total = 0.01   | Frete = 0.00     | Valor limite V20        |
| CT43 | Peso total = 2.50   | Frete = 0.00     | Valor limite V21               |
| CT44 | Peso total = 4.99   | Frete = 0.00     | Valor limite V22        |
| CT45 | Peso total = 5.00   | Frete = 0.00     | Valor limite V23 |
| CT46 | Peso total = 5.01   | Frete = 22.02    | Valor limite V24 |
| CT47 | Peso total = 5.02   | Frete = 22.04    | Valor limite V25        |
| CT48 | Peso total = 7.50   | Frete = 27.00    | Valor limite V26               |
| CT49 | Peso total = 9.99   | Frete = 31.98    | Valor limite V27        |
| CT50 | Peso total = 10.00  | Frete = 32.00    | Valor limite V28 |
| CT51 | Peso total = 10.01  | Frete = 52.04    | Valor limite V29         |
| CT52 | Peso total = 10.02  | Frete = 52.08    | Valor limite V30        |
| CT53 | Peso total = 49.99  | Frete = 211.96   | Valor limite V31       |
| CT54 | Peso total = 50.00  | Frete = 212.00   | Valor limite V32 |
| CT55 | Peso total = 50.01  | Frete = 362.07   | Valor limite V34 |
| CT56 | Peso total = 50.02  | Frete = 362.14   | Valor limite V35        |
| CT57 | Diferentes quantidades de itens do mesmo tipo  | Subtotal correspondente   | Decisões A1, A2, A3 e A4        |
| CT58 | Diferentes quantidades de itens do mesmo tipo e preços variados | Subtotal correspondente   | Decisões A5, A6, A7 e A8        |

## Cobertura MC/DC

Decisão mais complexa no nosso código:
`if (pesoTotal.compareTo(BigDecimal.valueOf(5)) > 0 && pesoTotal.compareTo(BigDecimal.valueOf(10)) <= 0)`

| Id | Condição          |
|----|-------------------|
| C1 | `pesoTotal > 5`   |
| C2 | `pesoTotal <= 10` |

| C1      | C2      | Saída   | Caso de teste |
|---------|---------|---------|---------------|
| `True`  | `True`  | `True`  | CalcularFretePorPesoTotal_ParaPesoTotalEntre5E10_EntaoFreteDe2PorKG              |
| `False` | `True`  | `False` | CalcularFretePorPesoTotal_ParaPesoTotalEntre0E5_EntaoFreteIsento              |
| `True`  | `False` | `False` | CalcularFretePorPesoTotal_ParaPesoTotalEntre10E50_EntaoFreteDe4PorKG              |


## Grafo de fluxo de controle (GFC)

Como nós dividimos a implementação de `CompraService` em vários métodos menores, vamos fazer um GFC para cada médoto.

| Método | GFC          |
|----|-------------------|
| ![](/img/subTotalComDesconto.png) | ![](/img/subTotalComDesconto-gfc.png)   |
| ![](/img/calcularSubTotal.png) | ![](/img/calcularSubTotal-gfc.png) |
| ![](/img/calculaDescontoPorTipo.png) | ![](/img/calculaDescontoPorTipo-gfc.png) |
| ![](/img/calcularDescontoPorValorCarrinho.png) | ![](/img/calcularDescontoPorValorCarrinho-gfc.png) |
| ![](/img/freteComDesconto.png) | ![](/img/freteComDesconto-gfc.png) |
| ![](/img/calcularFrete.png) | ![](/img/calcularFrete-gfc.png) |
| ![](/img/calcularPesoTotal.png) | ![](/img/calcularPesoTotal-gfc.png) |
| ![](/img/calculaDescontoFrete.png) | ![](/img/calculaDescontoFrete-gfc.png) |