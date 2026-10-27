package util;

import model.ArvoreAVL;
import model.ArvoreBinariaBusca;
import model.ArvoreRubroNegraLLRB;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class ConsoleUtils {

    public static int lerInteiro(Scanner scanner, String mensagem) {
        System.out.print(mensagem);
        while (!scanner.hasNextInt()) {
            System.out.print("Entrada inválida. Digite um número inteiro válido: ");
            scanner.next();
        }
        return scanner.nextInt();
    }

    public static String lerTexto(Scanner scanner, String mensagem) {
        System.out.print(mensagem);
        return scanner.next();
    }

    public static void exibirMenuOperacoes() {
        System.out.println("\n---------------- MENU DE OPERAÇÕES ----------------");
        System.out.println("1  - Inserir elemento");
        System.out.println("2  - Remover elemento");
        System.out.println("3  - Buscar elemento");
        System.out.println("4  - Percurso em ordem (ordem crescente)");
        System.out.println("5  - Visualizar estrutura da árvore");
        System.out.println("6  - Visualizar altura final da árvore");
        System.out.println("7  - Visualizar fator de balanceamento da raiz");
        System.out.println("8  - Visualizar total de comparações realizadas");
        System.out.println("9  - Visualizar total de rotações e balanceamentos");
        System.out.println("10 - Visualizar tempo médio das operações (1 a 3)");
        System.out.println("11 - Processar arquivo de comandos (.txt)");
        System.out.println("12 - Exportar estado e relatório para arquivo (.txt)");
        System.out.println("0  - Voltar ao menu principal");
    }

    public static void executarBateriaAutomatica() {
        int[] tamanhos = {200000, 400000, 600000, 800000, 1000000};
        String[] cenarios = {"Crescente", "Decrescente", "Aleatorio"};

        File dirComandos = new File("comandos");
        File dirRelatorios = new File("relatorios");

        if (!dirComandos.exists()) dirComandos.mkdirs();
        if (!dirRelatorios.exists()) dirRelatorios.mkdirs();

        System.out.println("\n=========================================================");
        System.out.println(" GERANDO E EXECUTANDO BATERIA DE BENCHMARK (200k A 1M)   ");
        System.out.println(" DIVISÃO EM 3 CENÁRIOS: CRESCENTE, DECRESCENTE E ALEATÓRIO");
        System.out.println("=========================================================");

        for (int n : tamanhos) {
            for (String cenario : cenarios) {
                String nomeScript = "comandos" + File.separator + "comandos_" + n + "_" + cenario + ".txt";
                gerarScriptEspecifico(nomeScript, n, cenario);

                // Benchmark para Árvore AVL
                ArvoreBinariaBusca avl = new ArvoreAVL();
                System.out.println(">>> Executando [" + cenario + "] no modelo AVL (N = " + n + ")...");
                processarArquivoComandos(nomeScript, avl);
                String relatorioAVL = "relatorios" + File.separator + "relatorio_ArvoreAVL_" + n + "_" + cenario + ".txt";
                exportarSaidaResumidaParaArquivo(relatorioAVL, avl);

                // Benchmark para Árvore Rubro-Negra (LLRB)
                ArvoreBinariaBusca llrb = new ArvoreRubroNegraLLRB();
                System.out.println(">>> Executando [" + cenario + "] no modelo LLRB (N = " + n + ")...");
                processarArquivoComandos(nomeScript, llrb);
                String relatorioLLRB = "relatorios" + File.separator + "relatorio_ArvoreRubroNegraLLRB_" + n + "_" + cenario + ".txt";
                exportarSaidaResumidaParaArquivo(relatorioLLRB, llrb);
            }
        }

        System.out.println("✔ Bateria comparativa completa concluída!");
        System.out.println("✔ Todos os relatórios foram salvos individualmente na pasta ./relatorios/\n");
    }

    private static void gerarScriptEspecifico(String caminhoArquivo, int n, String cenario) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(caminhoArquivo))) {
            writer.println("# SCRIPT AUTOMÁTICO DE BENCHMARK - N = " + n + " | CENÁRIO: " + cenario.toUpperCase() + "\n");

            writer.println("# --- INSERÇÕES (PADRÃO " + cenario.toUpperCase() + ") ---");
            if (cenario.equalsIgnoreCase("Crescente")) {
                for (int i = 1; i <= n; i++) {
                    writer.println("INSERIR " + i);
                }
            } else if (cenario.equalsIgnoreCase("Decrescente")) {
                for (int i = n; i >= 1; i--) {
                    writer.println("INSERIR " + i);
                }
            } else if (cenario.equalsIgnoreCase("Aleatorio")) {
                List<Integer> lista = new ArrayList<>(n);
                for (int i = 1; i <= n; i++) {
                    lista.add(i);
                }
                Collections.shuffle(lista);
                for (int val : lista) {
                    writer.println("INSERIR " + val);
                }
            }

            int qtdOperacoesAmostragem = Math.max(10, (int) (n * 0.10));

            writer.println("\n# --- BATERIA DE BUSCAS (" + qtdOperacoesAmostragem + " operações) ---");
            writer.println("BUSCAR -1");
            writer.println("BUSCAR " + (n + 9999));
            int passoBusca = Math.max(1, n / qtdOperacoesAmostragem);
            for (int i = 1; i <= n; i += passoBusca) {
                writer.println("BUSCAR " + i);
            }

            writer.println("\n# --- BATERIA DE REMOÇÕES (" + qtdOperacoesAmostragem + " operações) ---");
            writer.println("REMOVER -1");
            writer.println("REMOVER " + (n + 9999));
            int passoRemocao = Math.max(1, n / qtdOperacoesAmostragem);
            for (int i = 1; i <= n; i += passoRemocao) {
                writer.println("REMOVER " + i);
            }

            writer.println("\n# --- CONSOLIDAÇÃO DE MÉTRICAS ---");
            writer.println("EXIBIR_ALTURA");
            writer.println("EXIBIR_FB_RAIZ");
            writer.println("EXIBIR_COMPARACOES");
            writer.println("EXIBIR_BALANCEAMENTOS");
            writer.println("EXIBIR_TEMPOS");

        } catch (IOException e) {
            System.err.println("Erro ao gerar script " + caminhoArquivo + ": " + e.getMessage());
        }
    }

    public static void processarArquivoComandos(String caminhoArquivo, ArvoreBinariaBusca arvore) {
        long inicio = System.currentTimeMillis();
        long linhasProcessadas = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(caminhoArquivo))) {
            String linha;

            while ((linha = reader.readLine()) != null) {
                linha = linha.trim();
                if (linha.isEmpty() || linha.startsWith("#")) continue;

                String[] partes = linha.split("\\s+");
                String comando = partes[0].toUpperCase();
                linhasProcessadas++;

                switch (comando) {
                    case "INSERIR":
                        if (partes.length > 1) arvore.inserir(Integer.parseInt(partes[1]));
                        break;
                    case "REMOVER":
                        if (partes.length > 1) arvore.remover(Integer.parseInt(partes[1]));
                        break;
                    case "BUSCAR":
                        if (partes.length > 1) arvore.buscar(Integer.parseInt(partes[1]));
                        break;
                    case "EXIBIR_ALTURA":
                        System.out.println("Altura Final da Árvore: " + arvore.getAltura());
                        break;
                    case "EXIBIR_FB_RAIZ":
                        System.out.println("Fator de Balanceamento da Raiz: " + arvore.getFatorBalanceamentoRaiz());
                        break;
                    case "EXIBIR_COMPARACOES":
                        System.out.println("Total de Comparações Realizadas: " + arvore.getContadorComparacoes());
                        break;
                    case "EXIBIR_BALANCEAMENTOS":
                        System.out.println("Total de Rotações/Balanceamentos: " + arvore.getContadorBalanceamentos());
                        break;
                    case "EXIBIR_TEMPOS":
                        arvore.exibirTemposMedios();
                        break;
                    case "EM_ORDEM":
                        System.out.print("Percurso em Ordem: ");
                        arvore.emOrdem();
                        System.out.println();
                        break;
                    case "EXIBIR_ESTRUTURA":
                        System.out.println("--- ESTRUTURA VISUAL DA ÁRVORE ---");
                        arvore.exibirEstrutura();
                        break;
                    default:
                        System.out.println("⚠️ Comando não reconhecido no script: " + comando);
                        break;
                }
            }

            long tempoTotal = System.currentTimeMillis() - inicio;
            System.out.println("  ↳ Concluído: " + linhasProcessadas + " comandos processados em " + tempoTotal + " ms.");

        } catch (IOException e) {
            System.out.println("✖ Erro ao ler o arquivo de comandos: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("✖ Erro de formatação numérica no arquivo: " + e.getMessage());
        }
    }

    public static void exportarSaidaResumidaParaArquivo(String caminhoArquivo, ArvoreBinariaBusca arvore) {
        File arquivoSaida = new File(caminhoArquivo);
        if (arquivoSaida.getParentFile() != null && !arquivoSaida.getParentFile().exists()) {
            arquivoSaida.getParentFile().mkdirs();
        }

        ArvoreRubroNegraLLRB.setModoTerminalAnsi(false);

        try (PrintWriter writer = new PrintWriter(new FileWriter(arquivoSaida))) {
            String tipoArvore = arvore.getClass().getSimpleName().equals("ArvoreAVL") 
                    ? "Árvore AVL" 
                    : "Árvore Rubro-Negra (LLRB)";

            java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
            java.io.PrintStream psOriginal = System.out;
            System.setOut(new java.io.PrintStream(baos));
            
            arvore.exibirTemposMedios();
            System.setOut(psOriginal);

            List<String[]> temposLista = new ArrayList<>();
            String[] linhasTempos = baos.toString().trim().split("\n");
            for (String lt : linhasTempos) {
                if (lt.startsWith("1.") || lt.startsWith("2.") || lt.startsWith("3.")) {
                    String[] partes = lt.split(":", 2);
                    if (partes.length == 2) {
                        temposLista.add(new String[]{partes[0].trim(), partes[1].trim()});
                    }
                }
            }

            baos.reset();
            System.setOut(new java.io.PrintStream(baos));
            arvore.exibirDetalhamentoBalanceamentos();
            System.setOut(psOriginal);

            List<String[]> balanceamentoLista = new ArrayList<>();
            String[] linhasBal = baos.toString().trim().split("\n");
            for (String lb : linhasBal) {
                if (lb.startsWith("•")) {
                    String[] partes = lb.replace("•", "").split(":", 2);
                    if (partes.length == 2) {
                        balanceamentoLista.add(new String[]{partes[0].trim(), partes[1].trim()});
                    }
                }
            }

            int larguraColunaEsquerda = 49;
            int larguraColunaDireita = 22;

            larguraColunaDireita = Math.max(larguraColunaDireita, String.valueOf(arvore.getAltura()).length());
            larguraColunaDireita = Math.max(larguraColunaDireita, String.valueOf(arvore.getFatorBalanceamentoRaiz()).length());
            larguraColunaDireita = Math.max(larguraColunaDireita, String.valueOf(arvore.getContadorComparacoes()).length());
            larguraColunaDireita = Math.max(larguraColunaDireita, String.valueOf(arvore.getContadorBalanceamentos()).length());

            for (String[] t : temposLista) larguraColunaDireita = Math.max(larguraColunaDireita, t[1].length());
            for (String[] b : balanceamentoLista) larguraColunaDireita = Math.max(larguraColunaDireita, b[1].length());

            larguraColunaDireita += 2;

            String bordaSuave = "+" + "-".repeat(larguraColunaEsquerda + 2) + "+" + "-".repeat(larguraColunaDireita + 2) + "+";
            String fmtGenerico = "| %-" + larguraColunaEsquerda + "s | %-" + larguraColunaDireita + "s |\n";
            String fmtInteiro = "| %-" + larguraColunaEsquerda + "s | %-" + larguraColunaDireita + "d |\n";

            int larguraTotal = larguraColunaEsquerda + larguraColunaDireita + 7;
            writer.println("=".repeat(larguraTotal));
            writer.println(" ".repeat(Math.max(0, (larguraTotal - 37) / 2)) + "RELATÓRIO CONSOLIDADO DE ESTATÍSTICAS");
            writer.println("=".repeat(larguraTotal));
            writer.println("Estrutura Utilizada: " + tipoArvore);
            writer.println();

            writer.println(bordaSuave);
            writer.printf(fmtGenerico, "MÉTRICA / INDICADOR", "VALOR / DETALHAMENTO");
            writer.println(bordaSuave);
            writer.printf(fmtInteiro, "Altura Final da Árvore", arvore.getAltura());
            writer.printf(fmtInteiro, "Fator de Balanceamento da Raiz", arvore.getFatorBalanceamentoRaiz());
            writer.printf(fmtInteiro, "Total de Comparações Realizadas", arvore.getContadorComparacoes());
            writer.printf(fmtInteiro, "Total de Operações de Balanceamento/Rotações", arvore.getContadorBalanceamentos());
            writer.println(bordaSuave);

            writer.printf(fmtGenerico, "DESEMPENHO E TEMPOS MÉDIOS DE EXECUÇÃO", "");
            writer.println(bordaSuave);
            for (String[] t : temposLista) {
                writer.printf(fmtGenerico, t[0], t[1]);
            }
            writer.println(bordaSuave);

            writer.printf(fmtGenerico, "DETALHAMENTO DAS OPERAÇÕES DE BALANCEAMENTO", "");
            writer.println(bordaSuave);
            for (String[] b : balanceamentoLista) {
                writer.printf(fmtGenerico, b[0], b[1]);
            }
            writer.println(bordaSuave);

            System.out.println("  ↳ Relatório exportado: " + caminhoArquivo);

        } catch (IOException e) {
            System.out.println("✖ Erro ao gravar o arquivo de saída: " + e.getMessage());
        } finally {
            ArvoreRubroNegraLLRB.setModoTerminalAnsi(true);
        }
    }

    public static void exportarSaidaParaArquivo(String caminhoArquivo, ArvoreBinariaBusca arvore) {
        File arquivoSaida = new File(caminhoArquivo);
        if (arquivoSaida.getParentFile() != null && !arquivoSaida.getParentFile().exists()) {
            arquivoSaida.getParentFile().mkdirs();
        }

        ArvoreRubroNegraLLRB.setModoTerminalAnsi(false);

        try (PrintWriter writer = new PrintWriter(new FileWriter(arquivoSaida))) {
            String tipoArvore = arvore.getClass().getSimpleName().equals("ArvoreAVL") 
                    ? "Árvore AVL" 
                    : "Árvore Rubro-Negra (LLRB)";

            java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
            java.io.PrintStream psOriginal = System.out;
            System.setOut(new java.io.PrintStream(baos));
            
            arvore.exibirTemposMedios();
            System.setOut(psOriginal);

            List<String[]> temposLista = new ArrayList<>();
            String[] linhasTempos = baos.toString().trim().split("\n");
            for (String lt : linhasTempos) {
                if (lt.startsWith("1.") || lt.startsWith("2.") || lt.startsWith("3.")) {
                    String[] partes = lt.split(":", 2);
                    if (partes.length == 2) {
                        temposLista.add(new String[]{partes[0].trim(), partes[1].trim()});
                    }
                }
            }

            baos.reset();
            System.setOut(new java.io.PrintStream(baos));
            arvore.exibirDetalhamentoBalanceamentos();
            System.setOut(psOriginal);

            List<String[]> balanceamentoLista = new ArrayList<>();
            String[] linhasBal = baos.toString().trim().split("\n");
            for (String lb : linhasBal) {
                if (lb.startsWith("•")) {
                    String[] partes = lb.replace("•", "").split(":", 2);
                    if (partes.length == 2) {
                        balanceamentoLista.add(new String[]{partes[0].trim(), partes[1].trim()});
                    }
                }
            }

            int larguraColunaEsquerda = 49;
            int larguraColunaDireita = 22;

            larguraColunaDireita = Math.max(larguraColunaDireita, String.valueOf(arvore.getAltura()).length());
            larguraColunaDireita = Math.max(larguraColunaDireita, String.valueOf(arvore.getFatorBalanceamentoRaiz()).length());
            larguraColunaDireita = Math.max(larguraColunaDireita, String.valueOf(arvore.getContadorComparacoes()).length());
            larguraColunaDireita = Math.max(larguraColunaDireita, String.valueOf(arvore.getContadorBalanceamentos()).length());

            for (String[] t : temposLista) larguraColunaDireita = Math.max(larguraColunaDireita, t[1].length());
            for (String[] b : balanceamentoLista) larguraColunaDireita = Math.max(larguraColunaDireita, b[1].length());

            larguraColunaDireita += 2;

            String bordaSuave = "+" + "-".repeat(larguraColunaEsquerda + 2) + "+" + "-".repeat(larguraColunaDireita + 2) + "+";
            String fmtGenerico = "| %-" + larguraColunaEsquerda + "s | %-" + larguraColunaDireita + "s |\n";
            String fmtInteiro = "| %-" + larguraColunaEsquerda + "s | %-" + larguraColunaDireita + "d |\n";

            int larguraTotal = larguraColunaEsquerda + larguraColunaDireita + 7;
            writer.println("=".repeat(larguraTotal));
            writer.println(" ".repeat(Math.max(0, (larguraTotal - 37) / 2)) + "RELATÓRIO CONSOLIDADO DE ESTATÍSTICAS");
            writer.println("=".repeat(larguraTotal));
            writer.println("Estrutura Utilizada: " + tipoArvore);
            writer.println();

            writer.println(bordaSuave);
            writer.printf(fmtGenerico, "MÉTRICA / INDICADOR", "VALOR / DETALHAMENTO");
            writer.println(bordaSuave);
            writer.printf(fmtInteiro, "Altura Final da Árvore", arvore.getAltura());
            writer.printf(fmtInteiro, "Fator de Balanceamento da Raiz", arvore.getFatorBalanceamentoRaiz());
            writer.printf(fmtInteiro, "Total de Comparações Realizadas", arvore.getContadorComparacoes());
            writer.printf(fmtInteiro, "Total de Operações de Balanceamento/Rotações", arvore.getContadorBalanceamentos());
            writer.println(bordaSuave);

            writer.printf(fmtGenerico, "DESEMPENHO E TEMPOS MÉDIOS DE EXECUÇÃO", "");
            writer.println(bordaSuave);
            for (String[] t : temposLista) {
                writer.printf(fmtGenerico, t[0], t[1]);
            }
            writer.println(bordaSuave);

            writer.printf(fmtGenerico, "DETALHAMENTO DAS OPERAÇÕES DE BALANCEAMENTO", "");
            writer.println(bordaSuave);
            for (String[] b : balanceamentoLista) {
                writer.printf(fmtGenerico, b[0], b[1]);
            }
            writer.println(bordaSuave);
            writer.println();

            writer.println("--- PERCURSO EM ORDEM (ELEMENTOS EM ORDEM CRESCENTE) ---");
            baos.reset();
            System.setOut(new java.io.PrintStream(baos));
            arvore.emOrdem();
            System.setOut(psOriginal);
            writer.println(baos.toString().trim());
            writer.println();

            writer.println("--- ESTRUTURA VISUAL DA ÁRVORE ---");
            baos.reset();
            System.setOut(new java.io.PrintStream(baos));
            arvore.exibirEstrutura();
            System.setOut(psOriginal);
            writer.println(baos.toString());

            System.out.println("✔ Relatório completo exportado com sucesso para: " + caminhoArquivo);

        } catch (IOException e) {
            System.out.println("✖ Erro ao gravar o arquivo de saída: " + e.getMessage());
        } finally {
            ArvoreRubroNegraLLRB.setModoTerminalAnsi(true);
        }
    }
}