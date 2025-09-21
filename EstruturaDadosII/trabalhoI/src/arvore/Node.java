// src/arvore/Node.java

package arvore;

import java.util.ArrayList;

/**
 * Classe que representa um nó da árvore (BST ou AVL)
 * Utilizada para armazenar dados e manter a estrutura hierárquica da árvore
 */
public class Node {
    public String palavra; // Palavra armazenada no nó
    public int frequencia; // Número de ocorrências da palavra
    public Node pai; // Referência para o nó pai (hierarquia superior)
    public ArrayList<Node> filhos; // Lista de nós filhos (subárvores)
    public int altura; // Altura do nó (usada principalmente na AVL para balanceamento)

    /**
     * Construtor do nó - inicializa com valores padrão
     * 
     * @param palavra Palavra a ser armazenada no nó
     */
    public Node(String palavra) {
        this.palavra = palavra;
        this.frequencia = 1; // Primeira ocorrência da palavra
        this.pai = null; // Inicialmente sem pai (será raiz)
        this.filhos = null; // Inicialmente sem filhos (será folha)
        this.altura = 1; // Altura inicial para AVL (nó folha tem altura 1)
    }
}