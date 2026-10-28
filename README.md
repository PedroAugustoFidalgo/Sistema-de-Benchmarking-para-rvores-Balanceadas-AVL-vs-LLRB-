# Sistema de Benchmarking para Árvores Balanceadas (AVL vs LLRB)

Este projeto consiste em uma aplicação interativa desenvolvida em Java para criação, manipulação, análise e benchmarking comparativo entre duas estruturas de dados auto-balanceadas: **Árvore AVL** e **Árvore Rubro-Negra Caída para a Esquerda (Left-Leaning Red-Black Tree - LLRB)**.

---

## Sumário
1. [Estruturas de Dados Suportadas](#-estruturas-de-dados-suportadas)
2. [Dependências e Requisitos](#-dependências-e-requisitos)
3. [Instruções de Compilação e Execução](#-instruções-de-compilação-e-execução)
4. [Organização dos Arquivos do Projeto](#-organização-dos-arquivos-do-projeto)
5. [Manual de Funcionalidades do Menu](#-manual-de-funcionalidades-do-menu)
6. [Sintaxe para Arquivos de Script de Comandos](#-sintaxe-para-arquivos-de-script-de-comandos)

---

## Estruturas de Dados Suportadas

### 1. Árvore AVL
* **Princípio:** Balanceamento estrito baseado na altura das subárvores.
* **Invariante:** Para todo nó, a diferença entre as alturas das subárvores esquerda e direita (fator de balanceamento) deve pertencer ao conjunto $\{-1, 0, 1\}$.
* **Rebalanceamento:** Realizado via Rotações Simples (LL, RR) e Rotações Duplas (LR, RL).
* **Características:** Garante altura mínima extremamente otimizada, favorecendo operações de busca com custo $O(\log n)$.

### 2. Árvore Rubro-Negra (LLRB - Sedgewick)
* **Princípio:** Isomorfismo a uma Árvore 2-3 representado como uma árvore binária de busca com arcos (links) coloridos de vermelho ou preto.
* **Invariante:** Links vermelhos penderão sempre para a esquerda; nenhum nó possui dois links vermelhos incidentes.
* **Rebalanceamento:** Utiliza Rotações à Esquerda, Rotações à Direita e Inversões de Cor (*Flip Color*).
* **Características:** Operações de inversão de cor são atualizações de atributos $O(1)$ que evitam reestruturações pesadas de ponteiros, garantindo excelente eficiência em inserções e remoções volumosas.

---

## Dependências e Requisitos

* **JDK (Java Development Kit):** Versão 11 ou superior.
* **Terminal/CLI:** Suporte a caracteres ANSI para renderização de cores na visualização gráfica da árvore.

---

## Instruções de Compilação e Execução

### Opção A: Executando diretamente pelo arquivo `.jar` (Recomendado)
Se você já possui o arquivo executável empacotado, não é necessário compilar. Para baterias com até 1.000.000 de elementos, recomenda-se alocar memória heap adequada para a JVM (ex: `-Xmx2g`):

```bash
java -Xmx2g -jar ArvoresBenchmark.jar
```

### Opção B: Compilando e gerando o `.jar` do zero

1. **Compilar as classes Java:**
   ```bash
   javac -d bin src/model/*.java src/util/*.java src/main/*.java
   ```

2. **Criar o manifesto e empacotar o executável JAR:**
   ```bash
   echo "Main-Class: main.Main" > manifest.mf
   jar cvfm ArvoresBenchmark.jar manifest.mf -C bin .
   rm manifest.mf
   ```

3. **Executar a aplicação:**
   ```bash
   java -Xmx2g -jar ArvoresBenchmark.jar
   ```

---

## Organização dos Arquivos do Projeto

A aplicação organiza e consome automaticamente arquivos nas pastas localizadas na raiz do projeto:

```text
arvores/
├── ArvoresBenchmark.jar
├── bin/
├── comandos/            <-- Scripts de comandos (.txt) gerados e lidos pelo sistema
│   ├── comandos_200000.txt
│   ├── comandos_400000.txt
│   ├── comandos_600000.txt
│   ├── comandos_800000.txt
│   └── comandos_1000000.txt
├── relatorios/          <-- Relatórios de estatísticas exportados (.txt)
│   ├── relatorio_ArvoreAVL_200000.txt
│   ├── relatorio_ArvoreRubroNegraLLRB_200000.txt
│   └── ...
└── src/
    ├── main/
    ├── model/
    └── util/
```

---

## Manual de Funcionalidades do Menu

### Menu Principal
```text
1 - Trabalhar com Árvore AVL
2 - Trabalhar com Árvore Rubro-Negra (LLRB)
3 - Executar Bateria Comparativa Completa (200k a 1M)
0 - Sair
```

* **Opção 3 (Bateria Comparativa):** Executa automaticamente os testes comparativos aplicando **os mesmos conjuntos de dados idênticos** nas duas estruturas para os tamanhos $N \in \{200000, 400000, 600000, 800000, 1000000\}$.

### Submenu de Operações Individuais (Opções 1 e 2)
| Opção | Funcionalidade | Descrição / Exemplo de Uso |
| :---: | :--- | :--- |
| **1** | **Inserir elemento** | Insere um valor inteiro único na árvore. |
| **2** | **Remover elemento** | Remove uma chave existente, rebalanceando a árvore. |
| **3** | **Buscar elemento** | Consulta se um elemento está presente na árvore. |
| **4** | **Percurso em ordem** | Imprime todos os elementos ordenados crescentemente. |
| **5** | **Visualizar estrutura** | Renderiza a árvore visualmente no terminal com cores. |
| **6** | **Visualizar altura** | Exibe a altura total/profundidade máxima atual da estrutura. |
| **7** | **Visualizar FB da raiz** | Exibe o fator de balanceamento específico do nó raiz. |
| **8** | **Total de comparações** | Retorna a quantidade acumulada de comparações realizadas. |
| **9** | **Total de rotações** | Exibe o detalhamento de rotações simples, duplas e inversões de cor. |
| **10**| **Tempos médios** | Exibe o tempo médio de execução ($ns$ e $ms$) por tipo de operação. |
| **11**| **Processar arquivo** | Lê um script `.txt` e executa a sequência de instruções sequencialmente. |
| **12**| **Exportar relatório** | Grava um relatório completo da árvore atual (com estrutura visual e percurso). |
| **0** | **Voltar** | Retorna ao menu principal. |

---

## Sintaxe para Arquivos de Script de Comandos

O parser do sistema reconhece arquivos `.txt` contendo instruções linha a linha:

```text
# Script Exemplo
INSERIR 50
INSERIR 30
INSERIR 70
BUSCAR 30
REMOVER 50

EXIBIR_ALTURA
EXIBIR_COMPARACOES
EXIBIR_TEMPOS
```

## Autor

Pedro Augusto Carvalho de Freitas Gouveia Fidalgo

## Declaração do Uso de Inteligência Artificical

O código foi **criado com auxílio de inteligência artificial**, bem como o README.

Modelo: **Gemini 3.6 Flash**.
