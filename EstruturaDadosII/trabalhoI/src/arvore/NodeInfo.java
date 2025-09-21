// src/arvore/NodeInfo.java

package arvore;

/**
 * Classe auxiliar para passar informações de cada nó para a GUI
 * Age como um DTO (Data Transfer Object) entre a lógica da árvore e a interface
 * gráfica
 */
public class NodeInfo {
    public String palavra; // Palavra armazenada no nó
    public int frequencia; // Frequência da palavra
    public int nivel; // Profundidade do nó na árvore (0 para raiz)
    public Node node; // Referência para o nó real (para possível acesso direto)
    public Node pai; // Referência para o nó pai (para exibição de hierarquia)

    /**
     * Construtor que extrai informações de um nó existente
     * 
     * @param node  Nó da árvore do qual extrair informações
     * @param nivel Nível (profundidade) do nó na árvore
     */
    public NodeInfo(Node node, int nivel) {
        this.node = node;
        this.palavra = node.palavra; // Copia a palavra
        this.frequencia = node.frequencia; // Copia a frequência
        this.nivel = nivel; // Define o nível hierárquico
        this.pai = node.pai; // Copia referência do pai
    }
}