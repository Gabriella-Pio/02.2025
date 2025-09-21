// src/arvore/BSTree.java

package arvore;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe que implementa uma Árvore Binária de Busca (BST) simples
 * Armazena palavras e suas frequências, além de contar comparações e
 * atribuições
 */
public class BSTree {

    /**
     * Estrutura interna de nó para a BST
     * Mantém palavra, frequência e referências para filhos esquerdo e direito
     */
    private static class BSTNode {
        String palavra; // Palavra armazenada no nó
        int frequencia; // Frequência da palavra
        BSTNode esquerda; // Referência para filho esquerdo
        BSTNode direita; // Referência para filho direito

        /**
         * Construtor do nó BST
         * 
         * @param palavra Palavra a ser armazenada no nó
         */
        BSTNode(String palavra) {
            this.palavra = palavra;
            this.frequencia = 1; // Frequência inicializada com 1
            this.esquerda = null; // Inicialmente sem filho esquerdo
            this.direita = null; // Inicialmente sem filho direito
        }
    }

    private BSTNode raizBST; // Raiz da árvore BST interna
    private Node raiz; // Raiz da árvore (para compatibilidade com GUI)
    private int comparacoes = 0; // Contador de comparações de chaves realizadas
    private int atribuicoes = 0; // Contador de atribuições (inserções e incrementos de frequência)

    /**
     * Método público para inserção na árvore BST
     * 
     * @param palavra Palavra a ser inserida
     */
    public void insertBST(String palavra) {
        raizBST = insertBSTRec(raizBST, palavra);
    }

    /**
     * Método recursivo para inserção na árvore BST
     * 
     * @param node    Nó atual na recursão
     * @param palavra Palavra a ser inserida
     * @return Novo nó (ou nó atualizado) após inserção
     */
    private BSTNode insertBSTRec(BSTNode node, String palavra) {
        // Caso base: encontrou posição vazia para inserção
        if (node == null) {
            atribuicoes++; // Conta a criação de novo nó
            return new BSTNode(palavra); // Cria e retorna novo nó
        }

        // Compara a palavra com a palavra do nó atual
        comparacoes++;
        int cmp = palavra.compareTo(node.palavra);

        // Decide em qual subárvore inserir com base na comparação
        if (cmp < 0) {
            // Insere na subárvore esquerda (palavra menor)
            node.esquerda = insertBSTRec(node.esquerda, palavra);
        } else if (cmp > 0) {
            // Insere na subárvore direita (palavra maior)
            node.direita = insertBSTRec(node.direita, palavra);
        } else {
            // Palavra já existe - incrementa frequência
            atribuicoes++;
            node.frequencia++;
        }

        return node; // Retorna o nó (possivelmente com estrutura modificada)
    }

    /**
     * Constrói a árvore a partir de um array de palavras e retorna estatísticas
     * 
     * @param palavras Array de palavras a serem inseridas
     * @return Estatísticas da construção da árvore
     */
    public TreeStats buildWithStats(String[] palavras) {
        resetAnalise(); // Reseta contadores de análise

        // Mede tempo de execução
        long inicio = System.nanoTime();
        for (String palavra : palavras) {
            insertBST(palavra); // Insere cada palavra
        }
        long fim = System.nanoTime();
        double tempoExecucao = (fim - inicio) / 1_000_000.0; // Converte para milissegundos

        // Converte estrutura BST interna para estrutura Node (compatibilidade GUI)
        convertToNodeStructure();

        // Retorna estatísticas (0 rotações pois BST não faz rotações)
        return new TreeStats(comparacoes, atribuicoes, 0, tempoExecucao, getAltura());
    }

    /**
     * Converte estrutura BST interna para estrutura Node (para compatibilidade com
     * GUI)
     */
    private void convertToNodeStructure() {
        raiz = convertToNode(raizBST, null);
    }

    /**
     * Método recursivo para converter BSTNode para Node
     * 
     * @param bstNode Nó BST a ser convertido
     * @param parent  Nó pai na nova estrutura
     * @return Nó convertido
     */
    private Node convertToNode(BSTNode bstNode, Node parent) {
        if (bstNode == null)
            return null;

        // Cria novo nó com os mesmos dados
        Node node = new Node(bstNode.palavra);
        node.frequencia = bstNode.frequencia;
        node.pai = parent;
        node.filhos = new ArrayList<>();

        // Converte filhos recursivamente
        Node leftChild = convertToNode(bstNode.esquerda, node);
        Node rightChild = convertToNode(bstNode.direita, node);

        // Adiciona filhos à lista (se existirem)
        if (leftChild != null) {
            node.filhos.add(leftChild);
        }
        if (rightChild != null) {
            node.filhos.add(rightChild);
        }

        return node;
    }

    /**
     * Reseta contadores de análise
     */
    public void resetAnalise() {
        comparacoes = 0;
        atribuicoes = 0;
    }

    /**
     * Obtém altura da árvore BST
     * 
     * @return Altura da árvore
     */
    public int getAltura() {
        return getAlturaBST(raizBST);
    }

    /**
     * Método recursivo para calcular altura da árvore BST
     * 
     * @param node Nó atual
     * @return Altura da subárvore
     */
    private int getAlturaBST(BSTNode node) {
        if (node == null)
            return 0; // Árvore vazia tem altura 0
        // Altura é 1 + altura da maior subárvore
        return 1 + Math.max(getAlturaBST(node.esquerda), getAlturaBST(node.direita));
    }

    // -------------------------
    // Métodos para GUI
    // -------------------------

    /**
     * Retorna lista de frequências para exibição
     * 
     * @return Lista de strings no formato "palavra -> frequência"
     */
    public List<String> getFrequenciesAsList() {
        List<String> result = new ArrayList<>();
        inOrderToList(raizBST, result); // Percorre em ordem para lista ordenada
        return result;
    }

    /**
     * Percorre a árvore em ordem (esquerda-raiz-direita) e adiciona à lista
     * 
     * @param node   Nó atual
     * @param result Lista de resultados
     */
    private void inOrderToList(BSTNode node, List<String> result) {
        if (node != null) {
            inOrderToList(node.esquerda, result); // Visita subárvore esquerda
            result.add(node.palavra + " -> " + node.frequencia); // Adiciona nó atual
            inOrderToList(node.direita, result); // Visita subárvore direita
        }
    }

    /**
     * Obtém lista de nós com informações de nível (para GUI)
     * 
     * @return Lista de NodeInfo
     */
    public List<NodeInfo> getNodesWithLevel() {
        List<NodeInfo> lista = new ArrayList<>();
        if (raiz != null) {
            preencherListaComNivel(raiz, 0, lista); // Começa da raiz com nível 0
        }
        return lista;
    }

    /**
     * Preenche lista com nós e seus níveis (percurso em profundidade)
     * 
     * @param node  Nó atual
     * @param nivel Nível atual na árvore
     * @param lista Lista a ser preenchida
     */
    private void preencherListaComNivel(Node node, int nivel, List<NodeInfo> lista) {
        if (node != null) {
            NodeInfo nodeInfo = new NodeInfo(node, nivel);
            lista.add(nodeInfo); // Adiciona nó atual à lista

            // Processa filhos recursivamente (aumentando o nível)
            if (node.filhos != null) {
                for (Node child : node.filhos) {
                    preencherListaComNivel(child, nivel + 1, lista);
                }
            }
        }
    }

    /**
     * Obtém raiz da árvore (para GUI)
     * 
     * @return Nó raiz
     */
    public Node getRaiz() {
        return raiz;
    }
}