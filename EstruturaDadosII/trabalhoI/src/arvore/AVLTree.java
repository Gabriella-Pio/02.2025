package arvore;

import java.util.ArrayList;
import java.util.List;

// Classe que implementa uma Árvore AVL (BST balanceada)
// Mantém a propriedade de balanceamento para garantir altura log(n)
public class AVLTree {
    private Node raiz; // raiz da árvore
    private long comparacoes = 0; // contador de comparações
    private long atribuicoes = 0; // contador de atribuições

    // Inserção pública que delega para o método recursivo
    public void insert(String palavra) {
        raiz = insertRec(raiz, palavra);
    }

    // Inserção recursiva na AVL
    private Node insertRec(Node node, String palavra) {
        if (node == null) { // se não existe nó, cria um novo
            atribuicoes++;
            return new Node(palavra);
        }

        comparacoes++; // conta comparação
        int cmp = palavra.compareTo(node.palavra);

        if (cmp < 0) { // vai para a esquerda
            node.esquerda = insertRec(node.esquerda, palavra);
        } else if (cmp > 0) { // vai para a direita
            node.direita = insertRec(node.direita, palavra);
        } else { // já existe, incrementa frequência
            atribuicoes++;
            node.frequencia++;
            return node; // retorna nó existente sem balancear
        }

        // Atualiza altura do nó
        node.altura = 1 + Math.max(altura(node.esquerda), altura(node.direita));

        // Calcula fator de balanceamento
        int balance = getBalance(node);

        // Rotação simples à direita
        if (balance > 1 && palavra.compareTo(node.esquerda.palavra) < 0)
            return rotateRight(node);

        // Rotação simples à esquerda
        if (balance < -1 && palavra.compareTo(node.direita.palavra) > 0)
            return rotateLeft(node);

        // Rotação dupla esquerda-direita
        if (balance > 1 && palavra.compareTo(node.esquerda.palavra) > 0) {
            node.esquerda = rotateLeft(node.esquerda);
            return rotateRight(node);
        }

        // Rotação dupla direita-esquerda
        if (balance < -1 && palavra.compareTo(node.direita.palavra) < 0) {
            node.direita = rotateRight(node.direita);
            return rotateLeft(node);
        }

        return node; // retorna o nó após balanceamento
    }

    // Retorna altura de um nó (0 se nulo)
    private int altura(Node n) {
        return (n == null) ? 0 : n.altura;
    }

    // Retorna fator de balanceamento (altura esquerda - altura direita)
    private int getBalance(Node n) {
        return (n == null) ? 0 : altura(n.esquerda) - altura(n.direita);
    }

    // Rotação simples à direita
    private Node rotateRight(Node y) {
        atribuicoes++;
        Node x = y.esquerda;
        Node T2 = x.direita;

        x.direita = y;
        y.esquerda = T2;

        y.altura = Math.max(altura(y.esquerda), altura(y.direita)) + 1;
        x.altura = Math.max(altura(x.esquerda), altura(x.direita)) + 1;

        return x; // novo nó raiz da subárvore
    }

    // Rotação simples à esquerda
    private Node rotateLeft(Node x) {
        atribuicoes++;
        Node y = x.direita;
        Node T2 = y.esquerda;

        y.esquerda = x;
        x.direita = T2;

        x.altura = Math.max(altura(x.esquerda), altura(x.direita)) + 1;
        y.altura = Math.max(altura(y.esquerda), altura(y.direita)) + 1;

        return y; // novo nó raiz da subárvore
    }

    // Impressão em ordem alfabética
    public void inOrderTraversal() {
        inOrderRec(raiz);
    }

    private void inOrderRec(Node node) {
        if (node != null) {
            inOrderRec(node.esquerda);
            System.out.println(node.palavra + " -> " + node.frequencia);
            inOrderRec(node.direita);
        }
    }

    // Constrói a árvore a partir do vetor de palavras e retorna estatísticas
    public TreeStats buildWithStats(String[] palavras) {
        long inicio = System.nanoTime();
        for (String p : palavras)
            insert(p);
        long fim = System.nanoTime();
        long tempoExecucao = (fim - inicio); // converte para ms
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

    public Node getRaiz() {
        return raiz;
    }

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