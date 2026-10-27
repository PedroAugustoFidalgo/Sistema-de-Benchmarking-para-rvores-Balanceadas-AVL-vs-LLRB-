package model;

public abstract class ArvoreBinariaBusca {

    protected long contadorComparacoes = 0;
    protected long contadorRotacoesSimples = 0;
    protected long contadorRotacoesDuplas = 0;
    protected long contadorInversoesCor = 0;

    protected long tempoTotalInsercaoNs = 0;
    protected long qtdInsercoes = 0;

    protected long tempoTotalRemocaoNs = 0;
    protected long qtdRemocoes = 0;

    protected long tempoTotalBuscaNs = 0;
    protected long qtdBuscas = 0;

    // Métodos abstratos que devem ser implementados pelas subclasses
    public abstract void inserir(int chave);
    public abstract void remover(int chave);
    public abstract boolean buscar(int chave);
    public abstract int getAltura();
    public abstract int getFatorBalanceamentoRaiz();
    public abstract void emOrdem();
    public abstract void exibirEstrutura();

    // Métodos utilitários de contagem e registro
    protected void incrementarComparacoes() {
        this.contadorComparacoes++;
    }

    protected void registrarRotacaoSimples() {
        this.contadorRotacoesSimples++;
    }

    protected void registrarRotacaoDupla() {
        this.contadorRotacoesDuplas++;
    }

    protected void registrarInversaoCor() {
        this.contadorInversoesCor++;
    }

    protected void registrarTempoInsercao(long nanosegundos) {
        this.tempoTotalInsercaoNs += nanosegundos;
        this.qtdInsercoes++;
    }

    protected void registrarTempoRemocao(long nanosegundos) {
        this.tempoTotalRemocaoNs += nanosegundos;
        this.qtdRemocoes++;
    }

    protected void registrarTempoBusca(long nanosegundos) {
        this.tempoTotalBuscaNs += nanosegundos;
        this.qtdBuscas++;
    }

    public long getContadorComparacoes() {
        return contadorComparacoes;
    }

    public long getContadorBalanceamentos() {
        return contadorRotacoesSimples + contadorRotacoesDuplas + contadorInversoesCor;
    }

    public void exibirDetalhamentoBalanceamentos() {
        System.out.println("\n--- Detalhamento das Operações de Balanceamento ---");
        System.out.println("• Rotações Simples: " + contadorRotacoesSimples);
        System.out.println("• Rotações Duplas:  " + contadorRotacoesDuplas);
        System.out.println("• Inversões de Cor: " + contadorInversoesCor);
        System.out.println("• Total de Reestruturações: " + getContadorBalanceamentos());
    }

    public void exibirTemposMedios() {
        System.out.println("\n--- Desempenho e Tempos Médios de Execução ---");
        
        double mediaInsNs = qtdInsercoes > 0 ? (double) tempoTotalInsercaoNs / qtdInsercoes : 0;
        double mediaRemNs = qtdRemocoes > 0 ? (double) tempoTotalRemocaoNs / qtdRemocoes : 0;
        double mediaBusNs = qtdBuscas > 0 ? (double) tempoTotalBuscaNs / qtdBuscas : 0;

        System.out.printf("1. Inserção : %.2f ns (%.6f ms) | Operações: %d\n", mediaInsNs, mediaInsNs / 1_000_000.0, qtdInsercoes);
        System.out.printf("2. Remoção  : %.2f ns (%.6f ms) | Operações: %d\n", mediaRemNs, mediaRemNs / 1_000_000.0, qtdRemocoes);
        System.out.printf("3. Busca    : %.2f ns (%.6f ms) | Operações: %d\n", mediaBusNs, mediaBusNs / 1_000_000.0, qtdBuscas);
    }
}