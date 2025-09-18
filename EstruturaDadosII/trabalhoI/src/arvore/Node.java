package arvore;

import java.util.ArrayList;

// Classe que representa um nó da árvore (BST ou AVL)
public class Node {
    public String palavra; // palavra armazenada no nó
    public int frequencia; // número de ocorrências da palavra
    public Node pai; // referência para o nó pai
    public ArrayList<Node> filhos; // referência para a subárvore abaixo (filhos)
    public int altura; // usado apenas na AVL para balanceamento

    // Construtor inicializa a palavra, frequência e altura
    public Node(String palavra) {
        this.palavra = palavra;
        this.frequencia = 1; // primeira ocorrência
        this.pai = null;
        this.filhos = null;
        this.altura = 1; // altura inicial para AVL
    }
}
