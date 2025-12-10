## Autores

- Matheus Albert da Silva Araújo
- Paulo Daniel Carvalho de Souza

### Pré-requisítos

   1. Java
   2. Maven
   3. Recomendável alguma IDE, como VS Code ou Intellij

### Como executar

   1. Clone este repositório no seu computador

      ```bash
      git clone https://github.com/Albert1616/Testes-de-Software-Projeto-II.git
      ```

   3. Abra a pasta do projeto no terminal ou na IDE
   4. Para executar o projeto digite o seguinte comando no terminal:

      ```bash
      mvn clean compile
      ```

   6. Para executar os testes, digite o seguinte comando no terminal:

      ```bash
       mvn test
      ```

   8. Para ver a cobertura de testes do projeto, digite o seguinte comando no terminal:

       ```bash
         mvn verify
       ```

### Testes de mutação com PITEST

1. Comando usado para executar a verificação:

    ```bash
    mvn test-compile org.pitest:pitest-maven:mutationCoverage
    ```

2. Como verificar que não restaram mutantes sobreviventes em `calcularCustoTotal()`;
   1. Ao executar o comando já citado acima, vai ser gerador uma pasta `target/pit-repots`;
   2. Abra num browser o arquivo `index.html` dessa pasta;
   3. Clique em `ecommerce.service`, depois em `CompraService.java`;
   4. Dessa forma, conseguirá verificar que os métodos usados com `calcularCustoTotal()` estão todos com os mutantes mortos.

3. Ao usar o comando pela primeira vez, foi encontrado apenas 3 mutantes não mortos:

    ```
    removed conditional - replaced equality check with false → SURVIVED
    changed conditional boundary → SURVIVED
    changed conditional boundary → SURVIVED
    ```

    Esses mutantes foram mortos com uma refatoração no código:
      - Antes

          ```java
          public BigDecimal calcularFrete(List<ItemCompra> items) {
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
                          .multiply(BigDecimal.valueOf(2));
              } else if (pesoTotal.compareTo(BigDecimal.valueOf(10)) > 0
                      && pesoTotal.compareTo(BigDecimal.valueOf(50)) <= 0) {
                  valorFrete = pesoTotal
                          .multiply(BigDecimal.valueOf(4));
              } else if (pesoTotal.compareTo(BigDecimal.valueOf(50)) > 0) {
                  valorFrete = pesoTotal
                          .multiply(BigDecimal.valueOf(7));
              }
    
              // Adicional para produtos frágeis
              for (ItemCompra item : items) {
                  if (item.getProduto().isFragil()) {
                      valorFrete = valorFrete.add(BigDecimal.valueOf(5).multiply(BigDecimal.valueOf(item.getQuantidade())));
                  }
              }
    
              return valorFrete.setScale(2, RoundingMode.HALF_UP);
          }
         ```

     - Depois

         ```java
         private BigDecimal calcularValorBaseFrete(BigDecimal pesoTotal) {
           if (pesoTotal.compareTo(BigDecimal.valueOf(5)) < 1) {
               return BigDecimal.ZERO;
           }

           if (pesoTotal.compareTo(BigDecimal.valueOf(10)) <= 0) {
               return pesoTotal.multiply(BigDecimal.valueOf(2));
           }

           if (pesoTotal.compareTo(BigDecimal.valueOf(50)) <= 0) {
               return pesoTotal.multiply(BigDecimal.valueOf(4));
           }

           return pesoTotal.multiply(BigDecimal.valueOf(7));
          }
    
       public BigDecimal calcularFrete(List<ItemCompra> items) {
           BigDecimal pesoTotal = calcularPesoTotal(items);
            BigDecimal valorFrete = BigDecimal.ZERO;
         
            if (pesoTotal.compareTo(BigDecimal.ZERO) < 0) {
            t   hrow new IllegalArgumentException("Peso total inválido para cálculo de frete.");
            }
            
            valorFrete = calcularValorBaseFrete(pesoTotal);
            
            // Adicional para produtos frágeis
            for (ItemCompra item : items) {
               if (item.getProduto().isFragil()) {
                   valorFrete = valorFrete.add(BigDecimal.valueOf(5).multiply(BigDecimal.valueOf(item.getQuantidade())));
               }
            }
         
            return valorFrete.setScale(2, RoundingMode.HALF_UP);
         }
