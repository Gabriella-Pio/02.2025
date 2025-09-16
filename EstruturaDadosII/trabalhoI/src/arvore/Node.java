package arvore;

// Classe que representa um nó da árvore (BST ou AVL)
public class Node {
    public String palavra;    // palavra armazenada no nó
    public int frequencia;    // número de ocorrências da palavra
    public Node esquerda;     // referência para a subárvore esquerda
    public Node direita;      // referência para a subárvore direita
    public int altura;        // usado apenas na AVL para balanceamento

    // Construtor inicializa a palavra, frequência e altura
    public Node(String palavra) {
        this.palavra = palavra;
        this.frequencia = 1;  // primeira ocorrência
        this.esquerda = null;
        this.direita = null;
        this.altura = 1;      // altura inicial para AVL
    }
}
