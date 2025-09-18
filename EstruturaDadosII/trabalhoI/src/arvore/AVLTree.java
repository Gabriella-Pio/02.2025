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

    // Inserção pública que delega para o método recursivo
    public void insertAVL(String palavra) {
        raiz = insertAVLRec(raiz, palavra, null);
    }

    private Node insertAVLRec(Node node, String palavra, Node parent) {
        if (node == null) {
            atribuicoes++;
            Node newNode = new Node(palavra);
            newNode.pai = parent;

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
            Node leftChild = findOrCreateChildPosition(node, palavra, true);
            leftChild = insertAVLRec(leftChild, palavra, node);
            updateChildrenList(node, leftChild);
        } else if (cmp > 0) {
            Node rightChild = findOrCreateChildPosition(node, palavra, false);
            rightChild = insertAVLRec(rightChild, palavra, node);
            updateChildrenList(node, rightChild);
        } else {
            atribuicoes++;
            node.frequencia++;
            return node;
        }

        // Update height
        node.altura = 1 + Math.max(getHeight(getLeftChild(node)), getHeight(getRightChild(node)));

        // Check balance
        int balance = getBalance(node);

        // AVL Rotations - COUNT THESE!
        if (balance > 1 && palavra.compareTo(getLeftChild(node).palavra) < 0) {
            rotacoes++; // Count rotation
            return rotateRight(node, parent);
        }
        if (balance < -1 && palavra.compareTo(getRightChild(node).palavra) > 0) {
            rotacoes++; // Count rotation
            return rotateLeft(node, parent);
        }
        if (balance > 1 && palavra.compareTo(getLeftChild(node).palavra) > 0) {
            rotacoes += 2; // Count double rotation
            Node leftChild = getLeftChild(node);
            leftChild = rotateLeft(leftChild, node);
            updateChildrenList(node, leftChild);
            return rotateRight(node, parent);
        }
        if (balance < -1 && palavra.compareTo(getRightChild(node).palavra) < 0) {
            rotacoes += 2; // Count double rotation
            Node rightChild = getRightChild(node);
            rightChild = rotateRight(rightChild, node);
            updateChildrenList(node, rightChild);
            return rotateLeft(node, parent);
        }

        return node;
    }

    // findOrCreateChildPosition implementation
    private Node findOrCreateChildPosition(Node parent, String palavra, boolean isLeft) {
        if (parent.filhos == null) {
            return null; // Will be created in recursion
        }

        // Look for existing child that should be in this position
        for (Node child : parent.filhos) {
            int cmp = palavra.compareTo(child.palavra);
            boolean isCorrectPosition = (isLeft && cmp < 0 && child.palavra.compareTo(parent.palavra) < 0) ||
                    (!isLeft && cmp > 0 && child.palavra.compareTo(parent.palavra) > 0);

            if (isCorrectPosition) {
                return child;
            }
        }

        return null; // No suitable child found, will be created in recursion
    }

    // Helper methods for AVL
    private int getHeight(Node node) {
        return node == null ? 0 : node.altura;
    }

    private int getBalance(Node node) {
        if (node == null)
            return 0;
        return getHeight(getLeftChild(node)) - getHeight(getRightChild(node));
    }

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
    private Node rotateRight(Node y, Node parent) {
        Node x = getLeftChild(y);
        if (x == null)
            return y;

        Node T2 = getRightChild(x);

        // Perform rotation
        updateChildrenRelations(x, y, false); // x becomes parent of y
        if (T2 != null)
            updateChildrenRelations(y, T2, true); // y gets T2 as left child

        // Update heights
        y.altura = Math.max(getHeight(getLeftChild(y)), getHeight(getRightChild(y))) + 1;
        x.altura = Math.max(getHeight(getLeftChild(x)), getHeight(getRightChild(x))) + 1;

        // Update parent relationship
        x.pai = parent;
        if (parent != null) {
            updateChildrenList(parent, x);
        }

        return x;
    }

    private Node rotateLeft(Node x, Node parent) {
        Node y = getRightChild(x);
        if (y == null)
            return x;

        Node T2 = getLeftChild(y);

        // Perform rotation
        updateChildrenRelations(y, x, true); // y becomes parent of x
        if (T2 != null)
            updateChildrenRelations(x, T2, false); // x gets T2 as right child

        // Update heights
        x.altura = Math.max(getHeight(getLeftChild(x)), getHeight(getRightChild(x))) + 1;
        y.altura = Math.max(getHeight(getLeftChild(y)), getHeight(getRightChild(y))) + 1;

        // Update parent relationship
        y.pai = parent;
        if (parent != null) {
            updateChildrenList(parent, y);
        }

        return y;
    }

    public java.util.List<String> getFrequenciesAsList() {
        java.util.List<String> result = new java.util.ArrayList<>();
        inOrderToList(raiz, result);
        return result;
    }

    private void inOrderToList(Node node, java.util.List<String> result) {
        if (node != null) {
            // Process left subtree (children with smaller values)
            Node leftChild = getLeftChild(node);
            if (leftChild != null) {
                inOrderToList(leftChild, result);
            }

            // Process current node
            result.add(node.palavra + " -> " + node.frequencia);

            // Process right subtree (children with larger values)
            Node rightChild = getRightChild(node);
            if (rightChild != null) {
                inOrderToList(rightChild, result);
            }
        }
    }

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
            raiz = insertAVLRec(raiz, palavra, null);
        }

        long endTime = System.nanoTime();
        double tempo = (endTime - startTime) / 1_000_000.0; // ms

        return new TreeStats(comparacoes, atribuicoes, rotacoes, tempo, getAltura());
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

    public void resetAnalise() {
        comparacoes = 0;
        atribuicoes = 0;
        rotacoes = 0;
    }

    public int getAltura() {
        return getAltura(raiz);
    }

    private int getAltura(Node node) {
        if (node == null)
            return 0;
        return node.altura;
    }

    public List<NodeInfo> getNodesWithLevel() {
        List<NodeInfo> lista = new ArrayList<>();
        preencherListaComPai(raiz, null, 0, lista);
        return lista;
    }

    private void preencherListaComPai(Node node, Node parent, int nivel, List<NodeInfo> lista) {
        if (node != null) {
            NodeInfo nodeInfo = new NodeInfo(node, nivel);
            nodeInfo.pai = parent; // Set parent reference
            lista.add(nodeInfo);

            if (node.filhos != null) {
                for (Node child : node.filhos) {
                    preencherListaComPai(child, node, nivel + 1, lista);
                }
            }
        }
    }
}