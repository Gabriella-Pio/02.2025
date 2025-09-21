// src/arvore/TreeStats.java

package arvore;

/**
 * Classe que armazena e formata estatísticas de performance da árvore
 * Utilizada para análise comparativa entre BST e AVL
 */
public class TreeStats {
    private int comparacoes; // Número total de comparações realizadas
    private int atribuicoes; // Número total de atribuições realizadas
    private int rotacoes; // Número total de rotações (apenas AVL)
    private double tempoMilissegundos; // Tempo de execução em milissegundos
    private int altura; // Altura final da árvore

    /**
     * Construtor para BST (sem rotações)
     */
    public TreeStats(int comparacoes, int atribuicoes, double tempoMilissegundos, int altura) {
        this(comparacoes, atribuicoes, 0, tempoMilissegundos, altura); // Rotações = 0 para BST
    }

    /**
     * Construtor completo para AVL (com rotações)
     */
    public TreeStats(int comparacoes, int atribuicoes, int rotacoes,
            double tempoMilissegundos, int altura) {
        this.comparacoes = comparacoes;
        this.atribuicoes = atribuicoes;
        this.rotacoes = rotacoes;
        this.tempoMilissegundos = tempoMilissegundos;
        this.altura = altura;
    }

    /**
     * Retorna representação formatada das estatísticas
     * 
     * @return String formatada com emojis para melhor visualização
     */
    @Override
    public String toString() {
        return String.format(
                "📊 ESTATÍSTICAS DA ESTRUTURA:\n" +
                        "• Comparações: %d\n" + // Operações de comparação
                        "• Atribuições: %d\n" + // Operações de atribuição
                        "• Rotações: %d\n" + // Rotações de balanceamento
                        "• Tempo de execução: %.2f ms\n" + // Tempo total
                        "• Altura da árvore: %d\n" + // Altura da estrutura
                        "• Balanceada: %s", // Indica se é balanceada
                comparacoes, atribuicoes, rotacoes, tempoMilissegundos, altura,
                (rotacoes > 0) ? "Sim (AVL)" : "Não" // Detecção automática do tipo
        );
    }

    // Métodos de acesso (getters) para os campos privados

    public int getComparacoes() {
        return comparacoes;
    }

    public int getAtribuicoes() {
        return atribuicoes;
    }

    public int getRotacoes() {
        return rotacoes;
    }

    public double getTempoMilissegundos() {
        return tempoMilissegundos;
    }

    public int getAltura() {
        return altura;
    }
}