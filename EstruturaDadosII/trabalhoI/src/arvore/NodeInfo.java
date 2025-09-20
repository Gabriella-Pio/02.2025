// src/arvore/NodeInfo.java

package arvore;

// Classe auxiliar para passar informações de cada nó para a GUI
public class NodeInfo {
    public String palavra;    // palavra armazenada
    public int frequencia;    // frequência da palavra
    public int nivel;         // profundidade do nó na árvore
    public Node node;         // referência para o nó real (opcional)
    public Node pai;

    public NodeInfo(Node node, int nivel) {
        this.node = node;
        this.palavra = node.palavra;
        this.frequencia = node.frequencia;
        this.nivel = nivel;
        this.pai = node.pai;
    }
}
