package arvore;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe que implementa uma Árvore AVL (Árvore Binária de Busca Balanceada)
 * Mantém a propriedade de balanceamento para garantir altura log(n)
 * Inclui contadores para análise de desempenho e compatibilidade com GUI
 */
public class AVLTree {
    private Node raiz; // Raiz da árvore (para compatibilidade com GUI)
    private int comparacoes = 0; // Contador de comparações realizadas
    private int atribuicoes = 0; // Contador de atribuições realizadas
    private int rotacoes = 0; // Contador de rotações realizadas

    /**
     * Estrutura interna de nó para a AVL (evita referências circulares)
     * Mantida separada da estrutura Node usada na GUI
     */
    private static class AVLNode {
        String palavra; // Palavra armazenada no nó
        int frequencia; // Frequência da palavra
        AVLNode esquerda; // Filho esquerdo
        AVLNode direita; // Filho direito
        int altura; // Altura do nó na árvore

        /**
         * Construtor do nó AVL
         * 
         * @param palavra Palavra a ser armazenada
         */
        AVLNode(String palavra) {
            this.palavra = palavra;
            this.frequencia = 1; // Frequência inicializada com 1
            this.altura = 1; // Altura inicial de um nó folha
            this.esquerda = null;
            this.direita = null;
        }
    }

    private AVLNode raizAVL; // Raiz da árvore AVL interna

    /**
     * Método público para inserção na árvore AVL
     * 
     * @param palavra Palavra a ser inserida
     */
    public void insertAVL(String palavra) {
        raizAVL = insertAVLRec(raizAVL, palavra);
    }

    /**
     * Método recursivo para inserção na árvore AVL
     * 
     * @param node    Nó atual na recursão
     * @param palavra Palavra a ser inserida
     * @return Novo nó (ou nó atualizado) após inserção
     */
    private AVLNode insertAVLRec(AVLNode node, String palavra) {
        // Passo 1: Inserção normal BST
        if (node == null) {
            atribuicoes++;
            return new AVLNode(palavra);
        }

        comparacoes++;
        int cmp = palavra.compareTo(node.palavra);

        // Inserção na subárvore esquerda ou direita
        if (cmp < 0) {
            node.esquerda = insertAVLRec(node.esquerda, palavra);
        } else if (cmp > 0) {
            node.direita = insertAVLRec(node.direita, palavra);
        } else {
            // Palavra já existe, incrementa frequência
            atribuicoes++;
            node.frequencia++;
            return node; // Retorna sem rebalanceamento para palavras duplicadas
        }

        // Passo 2: Atualiza altura do nó atual
        node.altura = 1 + Math.max(getHeight(node.esquerda), getHeight(node.direita));

        // Passo 3: Calcula fator de balanceamento
        int balance = getBalance(node);

        // Passo 4: Realiza rotações se necessário
        // Caso Left Left (rotação simples à direita)
        if (balance > 1 && palavra.compareTo(node.esquerda.palavra) < 0) {
            rotacoes++;
            return rotateRight(node);
        }

        // Caso Right Right (rotação simples à esquerda)
        if (balance < -1 && palavra.compareTo(node.direita.palavra) > 0) {
            rotacoes++;
            return rotateLeft(node);
        }

        // Caso Left Right (rotação dupla: esquerda-direita)
        if (balance > 1 && palavra.compareTo(node.esquerda.palavra) > 0) {
            rotacoes += 2; // Rotação dupla
            node.esquerda = rotateLeft(node.esquerda);
            return rotateRight(node);
        }

        // Caso Right Left (rotação dupla: direita-esquerda)
        if (balance < -1 && palavra.compareTo(node.direita.palavra) < 0) {
            rotacoes += 2; // Rotação dupla
            node.direita = rotateRight(node.direita);
            return rotateLeft(node);
        }

        return node; // Retorna nó não modificado se balanceado
    }

    /**
     * Obtém altura de um nó (trata nulo como altura 0)
     * 
     * @param node Nó a ser verificado
     * @return Altura do nó
     */
    private int getHeight(AVLNode node) {
        return node == null ? 0 : node.altura;
    }

    /**
     * Calcula fator de balanceamento de um nó
     * 
     * @param node Nó a ser verificado
     * @return Fator de balanceamento (altura_esquerda - altura_direita)
     */
    private int getBalance(AVLNode node) {
        return node == null ? 0 : getHeight(node.esquerda) - getHeight(node.direita);
    }

    // Métodos de rotação para AVL

    /**
     * Rotação simples à direita
     * 
     * @param y Nó desbalanceado
     * @return Nova raiz da subárvore
     */
    private AVLNode rotateRight(AVLNode y) {
        AVLNode x = y.esquerda;
        AVLNode T2 = x.direita;

        // Executa rotação
        x.direita = y;
        y.esquerda = T2;

        // Atualiza alturas
        y.altura = Math.max(getHeight(y.esquerda), getHeight(y.direita)) + 1;
        x.altura = Math.max(getHeight(x.esquerda), getHeight(x.direita)) + 1;

        return x; // Retorna nova raiz
    }

    /**
     * Rotação simples à esquerda
     * 
     * @param x Nó desbalanceado
     * @return Nova raiz da subárvore
     */
    private AVLNode rotateLeft(AVLNode x) {
        AVLNode y = x.direita;
        AVLNode T2 = y.esquerda;

        // Executa rotação
        y.esquerda = x;
        x.direita = T2;

        // Atualiza alturas
        x.altura = Math.max(getHeight(x.esquerda), getHeight(x.direita)) + 1;
        y.altura = Math.max(getHeight(y.esquerda), getHeight(y.direita)) + 1;

        return y; // Retorna nova raiz
    }

    /**
     * Constrói a árvore a partir de um array de palavras e retorna estatísticas
     * 
     * @param palavras Array de palavras a serem inseridas
     * @return Estatísticas da construção da árvore
     */
    public TreeStats buildWithStats(String[] palavras) {
        resetAnalise(); // Reseta contadores
        long startTime = System.nanoTime(); // Inicia medição de tempo

        // Insere todas as palavras
        for (String palavra : palavras) {
            insertAVL(palavra);
        }

        long endTime = System.nanoTime();
        double tempo = (endTime - startTime) / 1_000_000.0; // Converte para milissegundos

        // Converte estrutura AVL interna para estrutura Node (compatibilidade GUI)
        convertToNodeStructure();

        return new TreeStats(comparacoes, atribuicoes, rotacoes, tempo, getAltura());
    }

    /**
     * Converte estrutura AVL interna para estrutura Node (para compatibilidade com
     * GUI)
     */
    private void convertToNodeStructure() {
        raiz = convertToNode(raizAVL, null);
    }

    /**
     * Método recursivo para converter AVLNode para Node
     * 
     * @param avlNode Nó AVL a ser convertido
     * @param parent  Nó pai na nova estrutura
     * @return Nó convertido
     */
    private Node convertToNode(AVLNode avlNode, Node parent) {
        if (avlNode == null)
            return null;

        // Cria novo nó com os mesmos dados
        Node node = new Node(avlNode.palavra);
        node.frequencia = avlNode.frequencia;
        node.altura = avlNode.altura;
        node.pai = parent;
        node.filhos = new ArrayList<>();

        // Converte filhos recursivamente
        Node leftChild = convertToNode(avlNode.esquerda, node);
        Node rightChild = convertToNode(avlNode.direita, node);

        // Adiciona filhos à lista
        if (leftChild != null) {
            node.filhos.add(leftChild);
        }
        if (rightChild != null) {
            node.filhos.add(rightChild);
        }

        return node;
    }

    // Métodos de acesso para estatísticas

    public int getComparacoes() {
        return comparacoes;
    }

    public int getAtribuicoes() {
        return atribuicoes;
    }

    public int getRotacoes() {
        return rotacoes;
    }

    /**
     * Obtém altura da árvore AVL
     * 
     * @return Altura da árvore
     */
    public int getAltura() {
        return getHeight(raizAVL);
    }

    /**
     * Reseta contadores de análise
     */
    public void resetAnalise() {
        comparacoes = 0;
        atribuicoes = 0;
        rotacoes = 0;
    }

    // Métodos para compatibilidade com GUI

    /**
     * Retorna lista de frequências para exibição
     * 
     * @return Lista de strings no formato "palavra -> frequência"
     */
    public List<String> getFrequenciesAsList() {
        List<String> result = new ArrayList<>();
        inOrderToList(raizAVL, result);
        return result;
    }

    /**
     * Percorre a árvore em ordem e adiciona à lista
     * 
     * @param node   Nó atual
     * @param result Lista de resultados
     */
    private void inOrderToList(AVLNode node, List<String> result) {
        if (node != null) {
            inOrderToList(node.esquerda, result);
            result.add(node.palavra + " -> " + node.frequencia);
            inOrderToList(node.direita, result);
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
            preencherListaComNivel(raiz, 0, lista);
        }
        return lista;
    }

    /**
     * Preenche lista com nós e seus níveis
     * 
     * @param node  Nó atual
     * @param nivel Nível atual
     * @param lista Lista a ser preenchida
     */
    private void preencherListaComNivel(Node node, int nivel, List<NodeInfo> lista) {
        if (node != null) {
            NodeInfo nodeInfo = new NodeInfo(node, nivel);
            lista.add(nodeInfo);

            // Processa filhos recursivamente
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