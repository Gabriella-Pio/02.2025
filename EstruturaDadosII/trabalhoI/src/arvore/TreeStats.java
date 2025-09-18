package arvore;

public class TreeStats {
    private int comparacoes;
    private int atribuicoes;
    private int rotacoes;
    private double tempoMilissegundos;
    private int altura;
    
    public TreeStats(int comparacoes, int atribuicoes, double tempoMilissegundos, int altura) {
        this(comparacoes, atribuicoes, 0, tempoMilissegundos, altura);
    }
    
    public TreeStats(int comparacoes, int atribuicoes, int rotacoes, 
                    double tempoMilissegundos, int altura) {
        this.comparacoes = comparacoes;
        this.atribuicoes = atribuicoes;
        this.rotacoes = rotacoes;
        this.tempoMilissegundos = tempoMilissegundos;
        this.altura = altura;
    }
    
    @Override
    public String toString() {
        return String.format(
            "📊 ESTATÍSTICAS DA ESTRUTURA:\n" +
            "• Comparações: %d\n" +
            "• Atribuições: %d\n" +
            "• Rotações: %d\n" +
            "• Tempo de execução: %.2f ms\n" +
            "• Altura da árvore: %d\n" +
            "• Balanceada: %s",
            comparacoes, atribuicoes, rotacoes, tempoMilissegundos, altura,
            (rotacoes > 0) ? "Sim (AVL)" : "Não"
        );
    }
    
    // Getters
    public int getComparacoes() { return comparacoes; }
    public int getAtribuicoes() { return atribuicoes; }
    public int getRotacoes() { return rotacoes; }
    public double getTempoMilissegundos() { return tempoMilissegundos; }
    public int getAltura() { return altura; }
}