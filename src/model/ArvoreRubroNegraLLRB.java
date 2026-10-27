package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ArvoreRubroNegraLLRB extends ArvoreBinariaBusca {

    private static final boolean VERMELHO = true;
    private static final boolean PRETO = false;

    private static final String ANSI_RED = "\u001B[31;1m";
    private static final String ANSI_RESET = "\u001B[0m";

    private static boolean modoTerminalAnsi = true;

    public static void setModoTerminalAnsi(boolean ativo) {
        modoTerminalAnsi = ativo;
    }

    private static class No {
        int chave;
        No esquerda, direita;
        boolean cor;

        No(int chave, boolean cor) {
            this.chave = chave;
            this.cor = cor;
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
        if (raiz != null) raiz.cor = PRETO;
        registrarTempoInsercao(System.nanoTime() - inicio);
    }

    private No inserirRec(No h, int chave) {
        if (h == null) return new No(chave, VERMELHO);

        incrementarComparacoes();
        if (chave < h.chave) {
            h.esquerda = inserirRec(h.esquerda, chave);
        } else if (chave > h.chave) {
            h.direita = inserirRec(h.direita, chave);
        } else {
            return h;
        }

        if (ehVermelho(h.direita) && !ehVermelho(h.esquerda)) {
            h = rotacionarEsquerda(h);
        }
        if (ehVermelho(h.esquerda) && ehVermelho(h.esquerda.esquerda)) {
            h = rotacionarDireita(h);
        }
        if (ehVermelho(h.esquerda) && ehVermelho(h.direita)) {
            inverterCores(h);
        }

        return h;
    }

    @Override
    public void remover(int chave) {
        long inicio = System.nanoTime();
        if (!buscarSemRegistro(chave)) {
            registrarTempoRemocao(System.nanoTime() - inicio);
            return;
        }

        if (raiz != null && !ehVermelho(raiz.esquerda) && !ehVermelho(raiz.direita)) {
            raiz.cor = VERMELHO;
        }

        raiz = removerRec(raiz, chave);
        if (raiz != null) raiz.cor = PRETO;

        registrarTempoRemocao(System.nanoTime() - inicio);
    }

    private boolean buscarSemRegistro(int chave) {
        No atual = raiz;
        while (atual != null) {
            if (chave == atual.chave) return true;
            atual = (chave < atual.chave) ? atual.esquerda : atual.direita;
        }
        return false;
    }

    private No removerRec(No h, int chave) {
        incrementarComparacoes();
        if (chave < h.chave) {
            if (h.esquerda != null && !ehVermelho(h.esquerda) && !ehVermelho(h.esquerda.esquerda)) {
                h = moverVermelhoEsquerda(h);
            }
            h.esquerda = removerRec(h.esquerda, chave);
        } else {
            if (ehVermelho(h.esquerda)) {
                h = rotacionarDireita(h);
            }
            if (chave == h.chave && (h.direita == null)) {
                return null;
            }
            if (h.direita != null && !ehVermelho(h.direita) && !ehVermelho(h.direita.esquerda)) {
                h = moverVermelhoDireita(h);
            }
            if (chave == h.chave) {
                No x = min(h.direita);
                h.chave = x.chave;
                h.direita = removerMin(h.direita);
            } else {
                h.direita = removerRec(h.direita, chave);
            }
        }
        return balancear(h);
    }

    private No min(No x) {
        while (x.esquerda != null) x = x.esquerda;
        return x;
    }

    private No removerMin(No h) {
        if (h.esquerda == null) return null;

        if (!ehVermelho(h.esquerda) && !ehVermelho(h.esquerda.esquerda)) {
            h = moverVermelhoEsquerda(h);
        }

        h.esquerda = removerMin(h.esquerda);
        return balancear(h);
    }

    private No moverVermelhoEsquerda(No h) {
        inverterCores(h);
        if (h.direita != null && ehVermelho(h.direita.esquerda)) {
            h.direita = rotacionarDireita(h.direita);
            h = rotacionarEsquerda(h);
            inverterCores(h);
        }
        return h;
    }

    private No moverVermelhoDireita(No h) {
        inverterCores(h);
        if (h.esquerda != null && ehVermelho(h.esquerda.esquerda)) {
            h = rotacionarDireita(h);
            inverterCores(h);
        }
        return h;
    }

    private No balancear(No h) {
        if (ehVermelho(h.direita)) h = rotacionarEsquerda(h);
        if (ehVermelho(h.esquerda) && ehVermelho(h.esquerda.esquerda)) h = rotacionarDireita(h);
        if (ehVermelho(h.esquerda) && ehVermelho(h.direita)) inverterCores(h);
        return h;
    }

    private boolean ehVermelho(No x) {
        return x != null && x.cor == VERMELHO;
    }

    private No rotacionarEsquerda(No h) {
        No x = h.direita;
        h.direita = x.esquerda;
        x.esquerda = h;
        x.cor = h.cor;
        h.cor = VERMELHO;
        registrarRotacaoSimples();
        return x;
    }

    private No rotacionarDireita(No h) {
        No x = h.esquerda;
        h.esquerda = x.direita;
        x.direita = h;
        x.cor = h.cor;
        h.cor = VERMELHO;
        registrarRotacaoSimples();
        return x;
    }

    private void inverterCores(No h) {
        h.cor = !h.cor;
        if (h.esquerda != null) h.esquerda.cor = !h.esquerda.cor;
        if (h.direita != null) h.direita.cor = !h.direita.cor;
        registrarInversaoCor();
    }

    @Override
    public int getAltura() {
        return alturaRec(raiz);
    }

    private int alturaRec(No no) {
        if (no == null) return 0;
        return 1 + Math.max(alturaRec(no.esquerda), alturaRec(no.direita));
    }

    @Override
    public int getFatorBalanceamentoRaiz() {
        if (raiz == null) return 0;
        return alturaRec(raiz.esquerda) - alturaRec(raiz.direita);
    }

    @Override
    public void emOrdem() {
        emOrdemRec(raiz);
    }

    private void emOrdemRec(No no) {
        if (no != null) {
            emOrdemRec(no.esquerda);
            if (no.cor == VERMELHO) {
                if (modoTerminalAnsi) {
                    System.out.print(ANSI_RED + no.chave + "[V]" + ANSI_RESET + " ");
                } else {
                    System.out.print(no.chave + "[V] ");
                }
            } else {
                System.out.print(no.chave + " ");
            }
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
        int espacamentoColuna = 9;
        for (int i = 0; i < ordem.size(); i++) {
            mapaColunas.put(ordem.get(i), i * espacamentoColuna + 4);
        }

        int h = getAltura();
        List<List<No>> niveis = new ArrayList<>();
        for (int i = 0; i < h; i++) niveis.add(new ArrayList<>());
        preencherNiveis(raiz, 0, niveis);

        int larguraTotal = ordem.size() * espacamentoColuna + 9;

        for (int i = 0; i < h; i++) {
            StringBuilder sbLinha = new StringBuilder(" ".repeat(larguraTotal));
            char[] linhaHastes = new char[larguraTotal];
            for (int k = 0; k < larguraTotal; k++) linhaHastes[k] = ' ';

            List<No> nivel = niveis.get(i);
            for (No no : nivel) {
                int colCentro = mapaColunas.get(no);
                String tagCor = (no.cor == VERMELHO) ? "[V]" : "";
                String strBase = "(" + no.chave + ")" + tagCor;

                String strExibicao = strBase;
                if (no.cor == VERMELHO && modoTerminalAnsi) {
                    strExibicao = ANSI_RED + strBase + ANSI_RESET;
                }

                int posInicio = colCentro - (strBase.length() / 2);
                if (posInicio >= 0 && posInicio + strExibicao.length() <= sbLinha.length()) {
                    sbLinha.replace(posInicio, posInicio + strExibicao.length(), strExibicao);
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

            System.out.println(sbLinha.toString().replaceAll("\\s+$", ""));
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