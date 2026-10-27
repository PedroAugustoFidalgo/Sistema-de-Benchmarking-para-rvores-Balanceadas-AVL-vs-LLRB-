package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ArvoreAVL extends ArvoreBinariaBusca {

    private static class No {
        int chave;
        int altura;
        No esquerda;
        No direita;

        No(int chave) {
            this.chave = chave;
            this.altura = 1;
        }
    }

    private No raiz;

    @Override
    public boolean buscar(int chave) {
        long inicio = System.nanoTime();
        No atual = raiz;
        
        while (atual != null) {
            incrementarComparacoes();
            if (chave == atual.chave) {
                registrarTempoBusca(System.nanoTime() - inicio);
                return true;
            } else if (chave < atual.chave) {
                atual = atual.esquerda;
            } else {
                atual = atual.direita;
            }
        }
        
        registrarTempoBusca(System.nanoTime() - inicio);
        return false;
    }

    @Override
    public void inserir(int chave) {
        long inicio = System.nanoTime();
        raiz = inserirRec(raiz, chave);
        registrarTempoInsercao(System.nanoTime() - inicio);
    }

    private No inserirRec(No no, int chave) {
        if (no == null) return new No(chave);

        incrementarComparacoes();
        if (chave < no.chave) {
            no.esquerda = inserirRec(no.esquerda, chave);
        } else if (chave > no.chave) {
            no.direita = inserirRec(no.direita, chave);
        } else {
            return no;
        }

        atualizarAltura(no);
        return balancear(no);
    }

    @Override
    public void remover(int chave) {
        long inicio = System.nanoTime();
        raiz = removerRec(raiz, chave);
        registrarTempoRemocao(System.nanoTime() - inicio);
    }

    private No removerRec(No no, int chave) {
        if (no == null) return null;

        incrementarComparacoes();
        if (chave < no.chave) {
            no.esquerda = removerRec(no.esquerda, chave);
        } else if (chave > no.chave) {
            no.direita = removerRec(no.direita, chave);
        } else {
            if (no.esquerda == null || no.direita == null) {
                no = (no.esquerda != null) ? no.esquerda : no.direita;
            } else {
                No menorNo = obterMenorNo(no.direita);
                no.chave = menorNo.chave;
                no.direita = removerRec(no.direita, menorNo.chave);
            }
        }

        if (no == null) return null;

        atualizarAltura(no);
        return balancear(no);
    }

    private No obterMenorNo(No no) {
        No atual = no;
        while (atual.esquerda != null) {
            atual = atual.esquerda;
        }
        return atual;
    }

    private int altura(No no) {
        return (no == null) ? 0 : no.altura;
    }

    private void atualizarAltura(No no) {
        no.altura = 1 + Math.max(altura(no.esquerda), altura(no.direita));
    }

    private int fatorBalanceamento(No no) {
        return (no == null) ? 0 : altura(no.esquerda) - altura(no.direita);
    }

    private No balancear(No no) {
        int fb = fatorBalanceamento(no);

        if (fb > 1) {
            if (fatorBalanceamento(no.esquerda) < 0) {
                no.esquerda = rotacionarEsquerda(no.esquerda);
                registrarRotacaoDupla();
            } else {
                registrarRotacaoSimples();
            }
            return rotacionarDireita(no);
        }

        if (fb < -1) {
            if (fatorBalanceamento(no.direita) > 0) {
                no.direita = rotacionarDireita(no.direita);
                registrarRotacaoDupla();
            } else {
                registrarRotacaoSimples();
            }
            return rotacionarEsquerda(no);
        }

        return no;
    }

    private No rotacionarDireita(No y) {
        No x = y.esquerda;
        No T2 = x.direita;

        x.direita = y;
        y.esquerda = T2;

        atualizarAltura(y);
        atualizarAltura(x);

        return x;
    }

    private No rotacionarEsquerda(No x) {
        No y = x.direita;
        No T2 = y.esquerda;

        y.esquerda = x;
        x.direita = T2;

        atualizarAltura(x);
        atualizarAltura(y);

        return y;
    }

    @Override
    public int getAltura() {
        return altura(raiz);
    }

    @Override
    public int getFatorBalanceamentoRaiz() {
        return fatorBalanceamento(raiz);
    }

    @Override
    public void emOrdem() {
        emOrdemRec(raiz);
    }

    private void emOrdemRec(No no) {
        if (no != null) {
            emOrdemRec(no.esquerda);
            System.out.print(no.chave + " ");
            emOrdemRec(no.direita);
        }
    }

    @Override
    public void exibirEstrutura() {
        if (raiz == null) {
            System.out.println("(árvore vazia)");
            return;
        }
        desenharArvoreVertical();
    }

    private void desenharArvoreVertical() {
        List<No> ordem = new ArrayList<>();
        mapearEmOrdem(raiz, ordem);

        Map<No, Integer> mapaColunas = new HashMap<>();
        int espacamentoColuna = 8;
        for (int i = 0; i < ordem.size(); i++) {
            mapaColunas.put(ordem.get(i), i * espacamentoColuna + 3);
        }

        int h = getAltura();
        List<List<No>> niveis = new ArrayList<>();
        for (int i = 0; i < h; i++) niveis.add(new ArrayList<>());
        preencherNiveis(raiz, 0, niveis);

        int larguraTotal = ordem.size() * espacamentoColuna + 8;

        for (int i = 0; i < h; i++) {
            char[] linhaNos = new char[larguraTotal];
            char[] linhaHastes = new char[larguraTotal];
            for (int k = 0; k < larguraTotal; k++) {
                linhaNos[k] = ' ';
                linhaHastes[k] = ' ';
            }

            List<No> nivel = niveis.get(i);
            for (No no : nivel) {
                int colCentro = mapaColunas.get(no);
                String strVal = "(" + no.chave + ")";
                int posInicio = colCentro - (strVal.length() / 2);

                for (int c = 0; c < strVal.length(); c++) {
                    if (posInicio + c >= 0 && posInicio + c < larguraTotal) {
                        linhaNos[posInicio + c] = strVal.charAt(c);
                    }
                }

                if (no.esquerda != null) {
                    int colEsq = mapaColunas.get(no.esquerda);
                    for (int c = colEsq + 1; c < colCentro; c++) linhaHastes[c] = '─';
                    linhaHastes[colEsq] = '┌';
                    linhaHastes[colCentro] = '┴';
                }
                if (no.direita != null) {
                    int colDir = mapaColunas.get(no.direita);
                    for (int c = colCentro + 1; c < colDir; c++) linhaHastes[c] = '─';
                    linhaHastes[colCentro] = '┴';
                    linhaHastes[colDir] = '┐';
                }
            }

            System.out.println(new String(linhaNos).replaceAll("\\s+$", ""));
            if (i < h - 1) {
                String hastesStr = new String(linhaHastes).replaceAll("\\s+$", "");
                if (!hastesStr.trim().isEmpty()) {
                    System.out.println(hastesStr);
                }
            }
        }
    }

    private void mapearEmOrdem(No no, List<No> lista) {
        if (no == null) return;
        mapearEmOrdem(no.esquerda, lista);
        lista.add(no);
        mapearEmOrdem(no.direita, lista);
    }

    private void preencherNiveis(No no, int prof, List<List<No>> niveis) {
        if (no == null || prof >= niveis.size()) return;
        niveis.get(prof).add(no);
        preencherNiveis(no.esquerda, prof + 1, niveis);
        preencherNiveis(no.direita, prof + 1, niveis);
    }
}