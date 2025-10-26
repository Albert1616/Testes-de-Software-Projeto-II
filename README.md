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
