package arvore;

import java.util.ArrayList;
import java.util.List;

// Classe que implementa uma Árvore Binária de Busca (BST) simples
// Armazena palavras e suas frequências, além de contar comparações e atribuições

public class BSTree {
    private Node raiz; // raiz da árvore
    private long comparacoes = 0; // contador de comparações de chaves
    private long atribuicoes = 0; // contador de atribuições (inserções e incremento de frequência)

    // Inserção pública que delega para o método recursivo
    public void insert(String palavra) {
        raiz = insertRec(raiz, palavra);
    }

    // Método recursivo de inserção na BST
    private Node insertRec(Node node, String palavra) {
        if (node == null) { // se a posição está vazia, cria um novo nó
            atribuicoes++; // conta como atribuição
            return new Node(palavra);
        }

        comparacoes++; // conta a comparação com o nó atual
        int cmp = palavra.compareTo(node.palavra); // compara a palavra atual com a do nó

        if (cmp < 0) { // palavra menor: vai para a subárvore esquerda
            node.esquerda = insertRec(node.esquerda, palavra);
        } else if (cmp > 0) { // palavra maior: vai para a subárvore direita
            node.direita = insertRec(node.direita, palavra);
        } else { // palavra igual: incrementa a frequência
            atribuicoes++;
            node.frequencia++;
        }

        return node;
    }

    // Imprime a árvore em ordem alfabética (in-order traversal)
    public void inOrderTraversal() {
        inOrderRec(raiz);
    }

    private void inOrderRec(Node node) {
        if (node != null) {
            inOrderRec(node.esquerda); // percorre a subárvore esquerda
            System.out.println(node.palavra + " -> " + node.frequencia); // imprime palavra e frequência
            inOrderRec(node.direita); // percorre a subárvore direita
        }
    }

    // Constrói a árvore a partir de um array de palavras e retorna estatísticas
    public TreeStats buildWithStats(String[] palavras) {
        long inicio = System.nanoTime(); // tempo inicial (ns)
        for (String p : palavras)
            insert(p); // insere todas as palavras
        long fim = System.nanoTime(); // tempo final
        long tempoExecucao = (fim - inicio); // converte para milissegundos
        return new TreeStats(comparacoes, atribuicoes, tempoExecucao);
    }

    // -------------------------
    // Métodos para GUI
    // -------------------------

    public java.util.List<String> getFrequenciesAsList() {
        java.util.List<String> result = new java.util.ArrayList<>();
        inOrderToList(raiz, result);
        return result;
    }

    private void inOrderToList(Node node, java.util.List<String> result) {
        if (node != null) {
            inOrderToList(node.esquerda, result);
            result.add(node.palavra + " -> " + node.frequencia);
            inOrderToList(node.direita, result);
        }
    }

    // Retorna a raiz da árvore
    public Node getRaiz() {
        return raiz;
    }

    // Gera lista de nós com nível (para desenhar na GUI)
    public List<NodeInfo> getNodesWithLevel() {
        List<NodeInfo> lista = new ArrayList<>();
        preencherLista(raiz, 0, lista);
        return lista;
    }

    private void preencherLista(Node node, int nivel, List<NodeInfo> lista) {
        if (node != null) {
            lista.add(new NodeInfo(node, nivel));
            preencherLista(node.esquerda, nivel + 1, lista);
            preencherLista(node.direita, nivel + 1, lista);
        }
    }
}