// src/arvore/BSTree.java

package arvore;

import java.util.ArrayList;
import java.util.List;

// Classe que implementa uma Árvore Binária de Busca (BST) simples
// Armazena palavras e suas frequências, além de contar comparações e atribuições

public class BSTree {
    private Node raiz; // raiz da árvore
    private int comparacoes = 0; // contador de comparações de chaves
    private int atribuicoes = 0; // contador de atribuições (inserções e incremento de frequência)

    // Inserção pública que delega para o método recursivo
    public void insertBST(String palavra) {
        raiz = insertBSTRec(raiz, palavra, null, 0);
    }

    private Node insertBSTRec(Node node, String palavra, Node parent, int depth) {
        if (node == null) {
            atribuicoes++;
            Node newNode = new Node(palavra);
            newNode.pai = parent;

            // Add to parent's children list if parent exists
            if (parent != null) {
                if (parent.filhos == null) {
                    parent.filhos = new ArrayList<>();
                }
                parent.filhos.add(newNode);
            }
            return newNode;
        }

        comparacoes++;
        int cmp = palavra.compareTo(node.palavra);

        if (cmp < 0) {
            // Find or create left child position
            Node leftChild = findOrCreateChildPosition(node, palavra, true);
            insertBSTRec(leftChild, palavra, node, depth + 1);
        } else if (cmp > 0) {
            // Find or create right child position
            Node rightChild = findOrCreateChildPosition(node, palavra, false);
            insertBSTRec(rightChild, palavra, node, depth + 1);
        } else {
            atribuicoes++;
            node.frequencia++;
        }

        return node;
    }

    private Node findOrCreateChildPosition(Node parent, String palavra, boolean isLeft) {
        if (parent.filhos == null) {
            parent.filhos = new ArrayList<>();
            return null;
        }

        // Look for existing child in the correct position
        for (Node child : parent.filhos) {
            int cmp = palavra.compareTo(child.palavra);
            if ((isLeft && cmp < 0) || (!isLeft && cmp > 0)) {
                return child;
            }
        }

        return null; // No suitable child found, will be created in recursion
    }

    // Constrói a árvore a partir de um array de palavras e retorna estatísticas
    public TreeStats buildWithStats(String[] palavras) {
        // RESET counters before starting
        resetAnalise();
        
        long inicio = System.nanoTime();
        for (String p : palavras) {
            insertBST(p); // insere todas as palavras
        }
        long fim = System.nanoTime();
        double tempoExecucao = (fim - inicio) / 1_000_000.0; // convert to milliseconds
        
        return new TreeStats(comparacoes, atribuicoes, 0, tempoExecucao, getAltura());
    }
    
    public void resetAnalise() {
        comparacoes = 0;
        atribuicoes = 0;
    }
    
    public int getAltura() {
        return getAltura(raiz);
    }
    
    private int getAltura(Node node) {
        if (node == null) return 0;
        int alturaEsq = getAltura(getLeftChild(node));
        int alturaDir = getAltura(getRightChild(node));
        return 1 + Math.max(alturaEsq, alturaDir);
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
            // Primeiro processa todos os filhos menores (esquerda)
            if(node.filhos != null) {
                for (Node child : node.filhos) {
                    if (child.palavra.compareTo(node.palavra) < 0) {
                        inOrderToList(child, result);
                    }
                }
            }

            // Processa o nó atual
            result.add(node.palavra + " -> " + node.frequencia);

            // Depois processa todos os filhos maiores (direita)
            if(node.filhos != null) {
                for (Node child : node.filhos) {
                    if (child.palavra.compareTo(node.palavra) > 0) {
                        inOrderToList(child, result);
                    }
                }
            }
        }
    }

    // private void inOrderToList(Node node, java.util.List<String> result) {
    //     if (node != null) {
    //         // Get left subtree (children with smaller values)
    //         Node leftChild = getLeftChild(node);
    //         if (leftChild != null) {
    //             inOrderToList(leftChild, result);
    //         }

    //         // Process current node
    //         result.add(node.palavra + " -> " + node.frequencia);

    //         // Get right subtree (children with larger values)
    //         Node rightChild = getRightChild(node);
    //         if (rightChild != null) {
    //             inOrderToList(rightChild, result);
    //         }
    //     }
    // }

    // Helper method to get left child (child with value < current node)
    private Node getLeftChild(Node node) {
        if (node.filhos == null)
            return null;

        for (Node child : node.filhos) {
            if (child.palavra.compareTo(node.palavra) < 0) {
                return child;
            }
        }
        return null;
    }

    // Helper method to get right child (child with value > current node)
    private Node getRightChild(Node node) {
        if (node.filhos == null)
            return null;

        for (Node child : node.filhos) {
            if (child.palavra.compareTo(node.palavra) > 0) {
                return child;
            }
        }
        return null;
    }

    public List<NodeInfo> getNodesWithLevel() {
        List<NodeInfo> lista = new ArrayList<>();
        preencherListaComNivel(raiz, 0, lista);
        return lista;
    }

    private void preencherListaComNivel(Node node, int nivel, List<NodeInfo> lista) {
        if (node != null) {
            // Add current node
            lista.add(new NodeInfo(node, nivel));

            // Recursively process all children
            if (node.filhos != null) {
                for (Node child : node.filhos) {
                    preencherListaComNivel(child, nivel + 1, lista);
                }
            }
        }
    }

    // Retorna a raiz da árvore
    public Node getRaiz() {
        return raiz;
    }

}