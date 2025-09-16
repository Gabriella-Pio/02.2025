package arvore;

// Classe para armazenar estatísticas de desempenho da árvore
public class TreeStats {
    private long comparacoes;       // contador de comparações de chaves
    private long atribuicoes;       // contador de atribuições (novos nós ou incrementos)
    private long tempoExecucao;     // tempo de execução em milissegundos

    // Construtor inicializa os valores
    public TreeStats(long comparacoes, long atribuicoes, long tempoExecucao) {
        this.comparacoes = comparacoes;
        this.atribuicoes = atribuicoes;
        this.tempoExecucao = tempoExecucao;
    }

    public long getComparacoes() { return comparacoes; }
    public long getAtribuicoes() { return atribuicoes; }
    public long getTempoExecucao() { return tempoExecucao; }

    // Método toString para imprimir facilmente as estatísticas
    @Override
    public String toString() {
        return "Comparações: " + comparacoes +
               " | Atribuições: " + atribuicoes +
               " | Tempo (ns): " + tempoExecucao;
    }
}
