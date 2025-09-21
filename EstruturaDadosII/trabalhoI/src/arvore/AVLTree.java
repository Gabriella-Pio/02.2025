package arvore;

import java.util.ArrayList;
import java.util.List;

// Classe que implementa uma Árvore AVL (BST balanceada)
// Mantém a propriedade de balanceamento para garantir altura log(n)
public class AVLTree {
    private Node raiz; // raiz da árvore
    private int comparacoes = 0; // contador de comparações
    private int atribuicoes = 0; // contador de atribuições
    private int rotacoes = 0; // contador de atribuições

    // Simplified Node structure for AVL - avoid circular references
    private static class AVLNode {
        String palavra;
        int frequencia;
        AVLNode esquerda, direita;
        int altura;

        AVLNode(String palavra) {
            this.palavra = palavra;
            this.frequencia = 1;
            this.altura = 1;
            this.esquerda = this.direita = null;
        }
    }

    private AVLNode raizAVL;

    // Inserção pública que delega para o método recursivo
    public void insertAVL(String palavra) {
        raizAVL = insertAVLRec(raizAVL, palavra);
        // raiz = insertAVLRec(raiz, palavra, null);
    }

    private AVLNode insertAVLRec(AVLNode node, String palavra) {
        // Step 1: Perform normal BST insertion
        if (node == null) {
            atribuicoes++;
            return new AVLNode(palavra);
        }

        comparacoes++;
        int cmp = palavra.compareTo(node.palavra);

        if (cmp < 0) {
            node.esquerda = insertAVLRec(node.esquerda, palavra);
        } else if (cmp > 0) {
            node.direita = insertAVLRec(node.direita, palavra);
        } else {
            // Word already exists, increment frequency
            atribuicoes++;
            node.frequencia++;
            return node;
        }

        // Step 2: Update height of current node
        node.altura = 1 + Math.max(getHeight(node.esquerda), getHeight(node.direita));

        // Step 3: Get balance factor
        int balance = getBalance(node);

        // Step 4: Perform rotations if unbalanced
        // Left Left Case
        if (balance > 1 && palavra.compareTo(node.esquerda.palavra) < 0) {
            rotacoes++;
            return rotateRight(node);
        }

        // Right Right Case
        if (balance < -1 && palavra.compareTo(node.direita.palavra) > 0) {
            rotacoes++;
            return rotateLeft(node);
        }

        // Left Right Case
        if (balance > 1 && palavra.compareTo(node.esquerda.palavra) > 0) {
            rotacoes += 2; // Double rotation
            node.esquerda = rotateLeft(node.esquerda);
            return rotateRight(node);
        }

        // Right Left Case
        if (balance < -1 && palavra.compareTo(node.direita.palavra) < 0) {
            rotacoes += 2; // Double rotation
            node.direita = rotateRight(node.direita);
            return rotateLeft(node);
        }

        return node;
    }

    // private Node insertAVLRec(Node node, String palavra, Node parent) {
    // if (node == null) {
    // atribuicoes++;
    // Node newNode = new Node(palavra);
    // newNode.pai = parent;

    // if (parent != null) {
    // if (parent.filhos == null) {
    // parent.filhos = new ArrayList<>();
    // }
    // parent.filhos.add(newNode);
    // }
    // return newNode;
    // }

    // comparacoes++;
    // int cmp = palavra.compareTo(node.palavra);

    // if (cmp < 0) {
    // Node leftChild = findOrCreateChildPosition(node, palavra, true);
    // leftChild = insertAVLRec(leftChild, palavra, node);
    // updateChildrenList(node, leftChild);
    // } else if (cmp > 0) {
    // Node rightChild = findOrCreateChildPosition(node, palavra, false);
    // rightChild = insertAVLRec(rightChild, palavra, node);
    // updateChildrenList(node, rightChild);
    // } else {
    // atribuicoes++;
    // node.frequencia++;
    // return node;
    // }

    // // Update height
    // node.altura = 1 + Math.max(getHeight(getLeftChild(node)),
    // getHeight(getRightChild(node)));

    // // Check balance
    // int balance = getBalance(node);

    // // AVL Rotations - COUNT THESE!
    // if (balance > 1 && palavra.compareTo(getLeftChild(node).palavra) < 0) {
    // rotacoes++; // Count rotation
    // return rotateRight(node, parent);
    // }
    // if (balance < -1 && palavra.compareTo(getRightChild(node).palavra) > 0) {
    // rotacoes++; // Count rotation
    // return rotateLeft(node, parent);
    // }
    // if (balance > 1 && palavra.compareTo(getLeftChild(node).palavra) > 0) {
    // rotacoes += 2; // Count double rotation
    // Node leftChild = getLeftChild(node);
    // leftChild = rotateLeft(leftChild, node);
    // updateChildrenList(node, leftChild);
    // return rotateRight(node, parent);
    // }
    // if (balance < -1 && palavra.compareTo(getRightChild(node).palavra) < 0) {
    // rotacoes += 2; // Count double rotation
    // Node rightChild = getRightChild(node);
    // rightChild = rotateRight(rightChild, node);
    // updateChildrenList(node, rightChild);
    // return rotateLeft(node, parent);
    // }

    // return node;
    // }

    // findOrCreateChildPosition implementation

    // private Node findOrCreateChildPosition(Node parent, String palavra, boolean
    // isLeft) {
    // if (parent.filhos == null) {
    // return null; // Will be created in recursion
    // }

    // // Look for existing child that should be in this position
    // for (Node child : parent.filhos) {
    // int cmp = palavra.compareTo(child.palavra);
    // boolean isCorrectPosition = (isLeft && cmp < 0 &&
    // child.palavra.compareTo(parent.palavra) < 0) ||
    // (!isLeft && cmp > 0 && child.palavra.compareTo(parent.palavra) > 0);

    // if (isCorrectPosition) {
    // return child;
    // }
    // }

    // return null; // No suitable child found, will be created in recursion
    // }

    private int getHeight(AVLNode node) {
        return node == null ? 0 : node.altura;
    }

    private int getBalance(AVLNode node) {
        return node == null ? 0 : getHeight(node.esquerda) - getHeight(node.direita);
    }

    // // Helper methods for AVL
    // private int getHeight(Node node) {
    // return node == null ? 0 : node.altura;
    // }

    // private int getBalance(Node node) {
    // if (node == null)
    // return 0;
    // return getHeight(getLeftChild(node)) - getHeight(getRightChild(node));
    // }

    private Node getLeftChild(Node node) {
        if (node == null || node.filhos == null)
            return null;

        Node leftChild = null;
        for (Node child : node.filhos) {
            if (child.palavra.compareTo(node.palavra) < 0) {
                if (leftChild == null || child.palavra.compareTo(leftChild.palavra) > 0) {
                    leftChild = child;
                }
            }
        }
        return leftChild;
    }

    private Node getRightChild(Node node) {
        if (node == null || node.filhos == null)
            return null;

        Node rightChild = null;
        for (Node child : node.filhos) {
            if (child.palavra.compareTo(node.palavra) > 0) {
                if (rightChild == null || child.palavra.compareTo(rightChild.palavra) < 0) {
                    rightChild = child;
                }
            }
        }
        return rightChild;
    }

    private void updateChildrenList(Node parent, Node child) {
        if (parent.filhos == null) {
            parent.filhos = new ArrayList<>();
        }
        if (!parent.filhos.contains(child)) {
            parent.filhos.add(child);
        }
        child.pai = parent;
    }

    // Rotation methods for AVL

    private AVLNode rotateRight(AVLNode y) {
        AVLNode x = y.esquerda;
        AVLNode T2 = x.direita;

        // Perform rotation
        x.direita = y;
        y.esquerda = T2;

        // Update heights
        y.altura = Math.max(getHeight(y.esquerda), getHeight(y.direita)) + 1;
        x.altura = Math.max(getHeight(x.esquerda), getHeight(x.direita)) + 1;

        return x;
    }

    // private Node rotateRight(Node y, Node parent) {
    // Node x = getLeftChild(y);
    // if (x == null)
    // return y;

    // Node T2 = getRightChild(x);

    // // Perform rotation
    // updateChildrenRelations(x, y, false); // x becomes parent of y
    // if (T2 != null)
    // updateChildrenRelations(y, T2, true); // y gets T2 as left child

    // // Update heights
    // y.altura = Math.max(getHeight(getLeftChild(y)), getHeight(getRightChild(y)))
    // + 1;
    // x.altura = Math.max(getHeight(getLeftChild(x)), getHeight(getRightChild(x)))
    // + 1;

    // // Update parent relationship
    // x.pai = parent;
    // if (parent != null) {
    // updateChildrenList(parent, x);
    // }

    // return x;
    // }

    private AVLNode rotateLeft(AVLNode x) {
        AVLNode y = x.direita;
        AVLNode T2 = y.esquerda;

        // Perform rotation
        y.esquerda = x;
        x.direita = T2;

        // Update heights
        x.altura = Math.max(getHeight(x.esquerda), getHeight(x.direita)) + 1;
        y.altura = Math.max(getHeight(y.esquerda), getHeight(y.direita)) + 1;

        return y;
    }

    // private Node rotateLeft(Node x, Node parent) {
    // Node y = getRightChild(x);
    // if (y == null)
    // return x;

    // Node T2 = getLeftChild(y);

    // // Perform rotation
    // updateChildrenRelations(y, x, true); // y becomes parent of x
    // if (T2 != null)
    // updateChildrenRelations(x, T2, false); // x gets T2 as right child

    // // Update heights
    // x.altura = Math.max(getHeight(getLeftChild(x)), getHeight(getRightChild(x)))
    // + 1;
    // y.altura = Math.max(getHeight(getLeftChild(y)), getHeight(getRightChild(y)))
    // + 1;

    // // Update parent relationship
    // y.pai = parent;
    // if (parent != null) {
    // updateChildrenList(parent, y);
    // }

    // return y;
    // }

    private void updateChildrenRelations(Node newParent, Node newChild, boolean asLeftChild) {
        // Remove newChild from its current parent's children list
        if (newChild.pai != null && newChild.pai.filhos != null) {
            newChild.pai.filhos.remove(newChild);
        }

        // Add newChild to newParent's children list
        if (newParent.filhos == null) {
            newParent.filhos = new ArrayList<>();
        }
        if (!newParent.filhos.contains(newChild)) {
            newParent.filhos.add(newChild);
        }
        newChild.pai = newParent;
    }

    public TreeStats buildWithStats(String[] palavras) {
        resetAnalise();
        long startTime = System.nanoTime();

        for (String palavra : palavras) {
            insertAVL(palavra);
        }

        long endTime = System.nanoTime();
        double tempo = (endTime - startTime) / 1_000_000.0;

        // Convert AVL structure to Node structure for GUI compatibility
        convertToNodeStructure();

        return new TreeStats(comparacoes, atribuicoes, rotacoes, tempo, getAltura());
    }

    // public TreeStats buildWithStats(String[] palavras) {
    // resetAnalise();
    // long startTime = System.nanoTime();

    // for (String palavra : palavras) {
    // raiz = insertAVLRec(raiz, palavra, null);
    // }

    // long endTime = System.nanoTime();
    // double tempo = (endTime - startTime) / 1_000_000.0; // ms

    // return new TreeStats(comparacoes, atribuicoes, rotacoes, tempo, getAltura());
    // }

    // Convert internal AVL structure to Node structure for GUI
    private void convertToNodeStructure() {
        raiz = convertToNode(raizAVL, null);
    }

    private Node convertToNode(AVLNode avlNode, Node parent) {
        if (avlNode == null)
            return null;

        Node node = new Node(avlNode.palavra);
        node.frequencia = avlNode.frequencia;
        node.altura = avlNode.altura;
        node.pai = parent;
        node.filhos = new ArrayList<>();

        // Convert children
        Node leftChild = convertToNode(avlNode.esquerda, node);
        Node rightChild = convertToNode(avlNode.direita, node);

        if (leftChild != null) {
            node.filhos.add(leftChild);
        }
        if (rightChild != null) {
            node.filhos.add(rightChild);
        }

        return node;
    }

    public int getComparacoes() {
        return comparacoes;
    }

    public int getAtribuicoes() {
        return atribuicoes;
    }

    public int getRotacoes() {
        return rotacoes;
    }

    public int getAltura() {
        return getHeight(raizAVL);
    }

    public void resetAnalise() {
        comparacoes = 0;
        atribuicoes = 0;
        rotacoes = 0;
    }

    // public int getAltura() {
    // return getAltura(raiz);
    // }

    // private int getAltura(Node node) {
    // if (node == null)
    // return 0;
    // return node.altura;
    // }

    // GUI compatibility methods
    public List<String> getFrequenciesAsList() {
        List<String> result = new ArrayList<>();
        inOrderToList(raizAVL, result);
        return result;
    }

    // public java.util.List<String> getFrequenciesAsList() {
    // java.util.List<String> result = new java.util.ArrayList<>();
    // inOrderToList(raiz, result);
    // return result;
    // }

    private void inOrderToList(AVLNode node, List<String> result) {
        if (node != null) {
            inOrderToList(node.esquerda, result);
            result.add(node.palavra + " -> " + node.frequencia);
            inOrderToList(node.direita, result);
        }
    }

    // private void inOrderToList(Node node, java.util.List<String> result) {
    // if (node != null) {
    // // Process left subtree (children with smaller values)
    // Node leftChild = getLeftChild(node);
    // if (leftChild != null) {
    // inOrderToList(leftChild, result);
    // }

    // // Process current node
    // result.add(node.palavra + " -> " + node.frequencia);

    // // Process right subtree (children with larger values)
    // Node rightChild = getRightChild(node);
    // if (rightChild != null) {
    // inOrderToList(rightChild, result);
    // }
    // }
    // }

    public List<NodeInfo> getNodesWithLevel() {
        List<NodeInfo> lista = new ArrayList<>();
        if (raiz != null) {
            preencherListaComNivel(raiz, 0, lista);
        }
        return lista;
    }

    // public List<NodeInfo> getNodesWithLevel() {
    // List<NodeInfo> lista = new ArrayList<>();
    // preencherListaComPai(raiz, null, 0, lista);
    // return lista;
    // }

    private void preencherListaComNivel(Node node, int nivel, List<NodeInfo> lista) {
        if (node != null) {
            NodeInfo nodeInfo = new NodeInfo(node, nivel);
            lista.add(nodeInfo);

            if (node.filhos != null) {
                for (Node child : node.filhos) {
                    preencherListaComNivel(child, nivel + 1, lista);
                }
            }
        }
    }

    public Node getRaiz() {
        return raiz;
    }

    // private void preencherListaComPai(Node node, Node parent, int nivel,
    // List<NodeInfo> lista) {
    // if (node != null) {
    // NodeInfo nodeInfo = new NodeInfo(node, nivel);
    // nodeInfo.pai = parent; // Set parent reference
    // lista.add(nodeInfo);

    // if (node.filhos != null) {
    // for (Node child : node.filhos) {
    // preencherListaComPai(child, node, nivel + 1, lista);
    // }
    // }
    // }
    // }
}