package main;

import model.ArvoreAVL;
import model.ArvoreBinariaBusca;
import model.ArvoreRubroNegraLLRB;
import util.ConsoleUtils;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int opcaoPrincipal = -1;

        while (opcaoPrincipal != 0) {
            System.out.println("\n==================================================");
            System.out.println("      SISTEMA DE BENCHMARKING DE ÁRVORES          ");
            System.out.println("==================================================");
            System.out.println("1 - Trabalhar com Árvore AVL");
            System.out.println("2 - Trabalhar com Árvore Rubro-Negra (LLRB)");
            System.out.println("3 - Executar Bateria Comparativa Completa (200k a 1M)");
            System.out.println("0 - Sair");

            opcaoPrincipal = ConsoleUtils.lerInteiro(scanner, "Escolha uma opção: ");

            switch (opcaoPrincipal) {
                case 1:
                    menuOperacoes(scanner, new ArvoreAVL());
                    break;
                case 2:
                    menuOperacoes(scanner, new ArvoreRubroNegraLLRB());
                    break;
                case 3:
                    ConsoleUtils.executarBateriaAutomatica();
                    break;
                case 0:
                    System.out.println("Encerrando o programa...");
                    break;
                default:
                    System.out.println("Opção inválida! Tente novamente.");
            }
        }
        scanner.close();
    }

    private static void menuOperacoes(Scanner scanner, ArvoreBinariaBusca arvore) {
        int opcao = -1;
        while (opcao != 0) {
            ConsoleUtils.exibirMenuOperacoes();
            opcao = ConsoleUtils.lerInteiro(scanner, "Escolha uma opção: ");

            switch (opcao) {
                case 1:
                    int valInserir = ConsoleUtils.lerInteiro(scanner, "Digite o valor inteiro para inserir: ");
                    arvore.inserir(valInserir);
                    break;
                case 2:
                    int valRemover = ConsoleUtils.lerInteiro(scanner, "Digite o valor inteiro para remover: ");
                    arvore.remover(valRemover);
                    break;
                case 3:
                    int valBuscar = ConsoleUtils.lerInteiro(scanner, "Digite o valor inteiro para buscar: ");
                    arvore.buscar(valBuscar);
                    break;
                case 4:
                    System.out.println("\n--- Percurso em Ordem ---");
                    arvore.emOrdem();
                    System.out.println();
                    break;
                case 5:
                    System.out.println("\n--- Estrutura Visual da Árvore ---");
                    arvore.exibirEstrutura();
                    break;
                case 6:
                    System.out.println("Altura da árvore: " + arvore.getAltura());
                    break;
                case 7:
                    System.out.println("Fator de Balanceamento da Raiz: " + arvore.getFatorBalanceamentoRaiz());
                    break;
                case 8:
                    System.out.println("Total de Comparações Realizadas: " + arvore.getContadorComparacoes());
                    break;
                case 9:
                    arvore.exibirDetalhamentoBalanceamentos();
                    break;
                case 10:
                    arvore.exibirTemposMedios();
                    break;
                case 11:
                    String arqComandos = ConsoleUtils.lerTexto(scanner, "Digite o caminho do arquivo de comandos (.txt): ");
                    ConsoleUtils.processarArquivoComandos(arqComandos, arvore);
                    break;
                case 12:
                    String arqSaida = ConsoleUtils.lerTexto(scanner, "Digite o caminho para exportar o relatório (.txt): ");
                    ConsoleUtils.exportarSaidaParaArquivo(arqSaida, arvore);
                    break;
                case 0:
                    System.out.println("Retornando ao menu principal...");
                    break;
                default:
                    System.out.println("Opção inválida! Tente novamente.");
            }
        }
    }
}