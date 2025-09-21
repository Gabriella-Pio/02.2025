// src/gui/TreePanel.java

package gui;

import arvore.Node;
import arvore.NodeInfo;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.*;
import java.util.List;

/**
 * Painel para visualização de árvore binária
 * Responsável por desenhar a estrutura da árvore com nós e conexões
 */
public class TreePanel extends JPanel {
    // Lista de nós da árvore
    private List<NodeInfo> nodes;

    // Configurações de layout e visualização
    private int nodeRadius = 30; // Raio dos nós circulares
    private int verticalSpacing = 80; // Espaçamento vertical entre níveis
    private int horizontalSpacing = 70; // Espaçamento horizontal entre nós
    private int topMargin = 70; // Margem superior
    private int sideMargin = 90; // Margem lateral

    // Cores utilizadas na visualização
    private final Color NODE_COLOR = new Color(173, 216, 230); // Azul claro para nós
    private final Color NODE_BORDER = new Color(70, 130, 180); // Azul escuro para bordas
    private final Color TEXT_COLOR = Color.BLACK; // Preto para texto
    private final Color LEFT_LINE_COLOR = new Color(70, 130, 180); // Azul para conexões à esquerda
    private final Color RIGHT_LINE_COLOR = new Color(220, 53, 69); // Vermelho para conexões à direita

    /**
     * Construtor - inicializa o painel com fundo branco
     */
    public TreePanel() {
        setBackground(Color.WHITE);
    }

    /**
     * Define os nós a serem visualizados e atualiza o painel
     * 
     * @param nodes Lista de nós da árvore
     */
    public void setNodes(List<NodeInfo> nodes) {
        this.nodes = (nodes == null) ? Collections.emptyList() : new ArrayList<>(nodes);
        revalidate();
        repaint();
    }

    /**
     * Método principal de pintura do componente
     * 
     * @param g Contexto gráfico
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Verifica se há nós para desenhar
        if (nodes == null || nodes.isEmpty()) {
            drawNoTreeMessage(g);
            return;
        }

        // Cria contexto Graphics2D com anti-aliasing para melhor qualidade
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        try {
            // Encontra a raiz da árvore
            Node root = findRoot(nodes);
            if (root == null) {
                drawErrorMessage(g2, "Root not found");
                return;
            }

            // Calcula as posições de todos os nós na árvore
            Map<Node, Point> positions = calculateTreeLayout(root);

            // Desenha as conexões entre os nós (linhas)
            drawBinaryTreeConnections(g2, positions, root);

            // Desenha os nós (círculos com texto)
            drawNodes(g2, positions);

        } catch (Exception e) {
            // Trata erros durante o desenho
            drawErrorMessage(g2, "Error drawing tree: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Libera recursos gráficos
            g2.dispose();
        }
    }

    /**
     * Exibe mensagem quando não há árvore para visualizar
     * 
     * @param g Contexto gráfico
     */
    private void drawNoTreeMessage(Graphics g) {
        g.setColor(Color.GRAY);
        g.setFont(new Font("SansSerif", Font.ITALIC, 16));
        FontMetrics fm = g.getFontMetrics();
        String message = "No tree to display";
        int x = (getWidth() - fm.stringWidth(message)) / 2;
        int y = getHeight() / 2;
        g.drawString(message, x, y);
    }

    /**
     * Exibe mensagem de erro
     * 
     * @param g2      Contexto gráfico 2D
     * @param message Mensagem de erro
     */
    private void drawErrorMessage(Graphics2D g2, String message) {
        g2.setColor(Color.RED);
        g2.setFont(new Font("SansSerif", Font.PLAIN, 14));
        g2.drawString(message, 20, 30);
    }

    /**
     * Encontra o nó raiz da árvore
     * 
     * @param nodes Lista de nós
     * @return Nó raiz ou null se não encontrado
     */
    private Node findRoot(List<NodeInfo> nodes) {
        // Primeiro busca por nível 0
        for (NodeInfo ni : nodes) {
            if (ni.nivel == 0) {
                return ni.node;
            }
        }
        // Depois busca por nó sem pai
        for (NodeInfo ni : nodes) {
            if (ni.pai == null) {
                return ni.node;
            }
        }
        // Retorna o primeiro nó se não encontrar raiz específica
        return nodes.isEmpty() ? null : nodes.get(0).node;
    }

    /**
     * Calcula o layout completo da árvore
     * 
     * @param root Nó raiz
     * @return Mapa com posições de todos os nós
     */
    private Map<Node, Point> calculateTreeLayout(Node root) {
        Map<Node, Point> positions = new HashMap<>();
        Map<Node, Rectangle> boundingBoxes = new HashMap<>();

        // Calcula layout considerando bounding boxes para evitar sobreposição
        calculateLayout(root, 0, 0, positions, boundingBoxes);

        // Centraliza a árvore no painel
        centerTree(positions);

        return positions;
    }

    /**
     * Calcula recursivamente o layout da árvore
     * 
     * @param node          Nó atual
     * @param x             Posição horizontal inicial
     * @param depth         Profundidade atual
     * @param positions     Mapa para armazenar posições
     * @param boundingBoxes Mapa para armazenar bounding boxes
     * @return Retângulo representando a área ocupada pela subárvore
     */
    private Rectangle calculateLayout(Node node, int x, int depth,
            Map<Node, Point> positions, Map<Node, Rectangle> boundingBoxes) {
        // Caso base: nó nulo
        if (node == null) {
            return new Rectangle(x, depth * verticalSpacing, 0, 0);
        }

        // Obtém filhos esquerdo e direito
        Node left = getLeftChild(node);
        Node right = getRightChild(node);

        // Processa recursivamente as subárvores
        Rectangle leftBox = calculateLayout(left, x, depth + 1, positions, boundingBoxes);
        int rightStartX = (left != null) ? leftBox.x + leftBox.width + horizontalSpacing : x + horizontalSpacing;
        Rectangle rightBox = calculateLayout(right, rightStartX, depth + 1, positions, boundingBoxes);

        // Calcula posição do nó atual baseado nas subárvores
        int nodeX;
        if (left != null && right != null) {
            // Centraliza entre os filhos
            nodeX = (leftBox.x + leftBox.width + rightBox.x) / 2;
        } else if (left != null) {
            // Alinha com filho esquerdo
            nodeX = leftBox.x + leftBox.width / 2;
        } else if (right != null) {
            // Alinha com filho direito
            nodeX = rightBox.x;
        } else {
            // Nó folha
            nodeX = x;
        }

        // Calcula posição Y baseada na profundidade
        int nodeY = depth * verticalSpacing;
        Point nodePos = new Point(nodeX, nodeY);
        positions.put(node, nodePos);

        // Calcula bounding box da subárvore (área ocupada)
        int minX = nodeX - nodeRadius;
        int maxX = nodeX + nodeRadius;
        int minY = nodeY - nodeRadius;
        int maxY = nodeY + nodeRadius;

        // Expande a bounding box para incluir subárvores
        if (left != null) {
            minX = Math.min(minX, leftBox.x);
            maxX = Math.max(maxX, leftBox.x + leftBox.width);
            maxY = Math.max(maxY, leftBox.y + leftBox.height);
        }

        if (right != null) {
            minX = Math.min(minX, rightBox.x);
            maxX = Math.max(maxX, rightBox.x + rightBox.width);
            maxY = Math.max(maxY, rightBox.y + rightBox.height);
        }

        // Armazena e retorna a bounding box
        Rectangle bbox = new Rectangle(minX, minY, maxX - minX, maxY - minY);
        boundingBoxes.put(node, bbox);

        return bbox;
    }

    /**
     * Centraliza a árvore no painel
     * 
     * @param positions Mapa com posições dos nós
     */
    private void centerTree(Map<Node, Point> positions) {
        if (positions.isEmpty())
            return;

        // Encontra os limites atuais da árvore
        int minX = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        int minY = Integer.MAX_VALUE;
        int maxY = Integer.MIN_VALUE;

        for (Point p : positions.values()) {
            minX = Math.min(minX, p.x);
            maxX = Math.max(maxX, p.x);
            minY = Math.min(minY, p.y);
            maxY = Math.max(maxY, p.y);
        }

        // Calcula deslocamento necessário para centralizar
        int shiftX = (getWidth() - (maxX - minX)) / 2 - minX;
        int shiftY = topMargin - minY;

        // Aplica deslocamento a todos os nós
        for (Point p : positions.values()) {
            p.x += shiftX;
            p.y += shiftY;
        }

        // Ajusta tamanho preferido do painel para caber toda a árvore
        int panelWidth = Math.max(getWidth(), (maxX - minX) + 2 * sideMargin);
        int panelHeight = topMargin + (maxY - minY) + verticalSpacing + 100;
        setPreferredSize(new Dimension(panelWidth, panelHeight));
    }

    /**
     * Obtém o filho esquerdo de um nó (menor valor)
     * 
     * @param node Nó pai
     * @return Filho esquerdo ou null se não existir
     */
    private Node getLeftChild(Node node) {
        if (node.filhos == null || node.filhos.isEmpty())
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

    /**
     * Obtém o filho direito de um nó (maior valor)
     * 
     * @param node Nó pai
     * @return Filho direito ou null se não existir
     */
    private Node getRightChild(Node node) {
        if (node.filhos == null || node.filhos.isEmpty())
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

    /**
     * Desenha recursivamente as conexões da árvore
     * 
     * @param g2        Contexto gráfico 2D
     * @param positions Mapa com posições dos nós
     * @param node      Nó atual
     */
    private void drawBinaryTreeConnections(Graphics2D g2, Map<Node, Point> positions, Node node) {
        if (node == null)
            return;

        Point nodePos = positions.get(node);
        if (nodePos == null)
            return;

        // Obtém filhos
        Node leftChild = getLeftChild(node);
        Node rightChild = getRightChild(node);

        // Configura linha mais grossa para conexões
        g2.setStroke(new BasicStroke(2.5f));

        // Desenha conexão com filho esquerdo
        if (leftChild != null) {
            Point childPos = positions.get(leftChild);
            if (childPos != null) {
                g2.setColor(LEFT_LINE_COLOR);
                g2.drawLine(nodePos.x, nodePos.y, childPos.x, childPos.y);
                g2.setFont(new Font("SansSerif", Font.BOLD, 10));
                int midX = (nodePos.x + childPos.x) / 2 - 10;
                int midY = (nodePos.y + childPos.y) / 2;
                g2.drawString("L", midX, midY); // Marca como conexão esquerda
                drawBinaryTreeConnections(g2, positions, leftChild); // Continua recursivamente
            }
        }

        // Desenha conexão com filho direito
        if (rightChild != null) {
            Point childPos = positions.get(rightChild);
            if (childPos != null) {
                g2.setColor(RIGHT_LINE_COLOR);
                g2.drawLine(nodePos.x, nodePos.y, childPos.x, childPos.y);
                g2.setFont(new Font("SansSerif", Font.BOLD, 10));
                int midX = (nodePos.x + childPos.x) / 2 + 5;
                int midY = (nodePos.y + childPos.y) / 2;
                g2.drawString("R", midX, midY); // Marca como conexão direita
                drawBinaryTreeConnections(g2, positions, rightChild); // Continua recursivamente
            }
        }
    }

    /**
     * Desenha todos os nós da árvore
     * 
     * @param g2        Contexto gráfico 2D
     * @param positions Mapa com posições dos nós
     */
    private void drawNodes(Graphics2D g2, Map<Node, Point> positions) {
        for (Map.Entry<Node, Point> entry : positions.entrySet()) {
            drawStyledNode(g2, entry.getKey(), entry.getValue());
        }
    }

    /**
     * Desenha um nó estilizado com sombra e borda
     * 
     * @param g2   Contexto gráfico 2D
     * @param node Nó a ser desenhado
     * @param pos  Posição do nó
     */
    private void drawStyledNode(Graphics2D g2, Node node, Point pos) {
        int x = pos.x;
        int y = pos.y;

        // Desenha sombra do nó
        g2.setColor(new Color(0, 0, 0, 30));
        g2.fillOval(x - nodeRadius + 2, y - nodeRadius + 2, nodeRadius * 2, nodeRadius * 2);

        // Desenha preenchimento do nó
        g2.setColor(NODE_COLOR);
        g2.fillOval(x - nodeRadius, y - nodeRadius, nodeRadius * 2, nodeRadius * 2);

        // Desenha borda do nó
        g2.setColor(NODE_BORDER);
        g2.setStroke(new BasicStroke(2.0f));
        g2.drawOval(x - nodeRadius, y - nodeRadius, nodeRadius * 2, nodeRadius * 2);

        // Desenha texto dentro do nó
        drawNodeText(g2, node, pos);
    }

    /**
     * Desenha o texto dentro de um nó (palavra e frequência)
     * 
     * @param g2   Contexto gráfico 2D
     * @param node Nó contendo o texto
     * @param pos  Posição do nó
     */
    private void drawNodeText(Graphics2D g2, Node node, Point pos) {
        g2.setColor(TEXT_COLOR);

        // Configura fonte para o texto
        Font font = new Font("SansSerif", Font.BOLD, 10);
        g2.setFont(font);
        FontMetrics fm = g2.getFontMetrics();

        String word = node.palavra;
        String freq = "(" + node.frequencia + ")";

        // Quebra o texto em múltiplas linhas se necessário
        List<String> wordLines = breakTextIntoLines(word, fm, nodeRadius * 1.8);

        // Calcula posição vertical para centralizar o texto
        int lineHeight = fm.getHeight();
        int startY = pos.y - (wordLines.size() * lineHeight) / 2 + lineHeight / 3;

        // Desenha cada linha do texto
        for (int i = 0; i < wordLines.size(); i++) {
            String line = wordLines.get(i);
            Rectangle2D lineBounds = fm.getStringBounds(line, g2);
            int lineX = pos.x - (int) (lineBounds.getWidth() / 2);
            g2.drawString(line, lineX, startY + i * lineHeight);
        }

        // Desenha a frequência abaixo da palavra
        Font smallFont = new Font("SansSerif", Font.PLAIN, 8);
        g2.setFont(smallFont);
        g2.setColor(new Color(80, 80, 80));
        Rectangle2D freqBounds = smallFont.getStringBounds(freq, g2.getFontRenderContext());
        int freqX = pos.x - (int) (freqBounds.getWidth() / 2);
        g2.drawString(freq, freqX, pos.y + nodeRadius - 5);
    }

    /**
     * Quebra texto em múltiplas linhas baseado na largura máxima
     * 
     * @param text     Texto a ser quebrado
     * @param fm       Métricas da fonte
     * @param maxWidth Largura máxima permitida
     * @return Lista de linhas de texto
     */
    private List<String> breakTextIntoLines(String text, FontMetrics fm, double maxWidth) {
        List<String> lines = new ArrayList<>();

        // Se o texto couber em uma linha, retorna como está
        if (fm.stringWidth(text) <= maxWidth) {
            lines.add(text);
            return lines;
        }

        // Quebra o texto em palavras
        String[] words = text.split("\\s+");
        StringBuilder currentLine = new StringBuilder();

        for (String word : words) {
            // Testa se a palavra cabe na linha atual
            String testLine = currentLine.length() > 0 ? currentLine.toString() + " " + word : word;

            if (fm.stringWidth(testLine) <= maxWidth) {
                currentLine = new StringBuilder(testLine);
            } else {
                // Adiciona linha atual ao resultado
                if (currentLine.length() > 0) {
                    lines.add(currentLine.toString());
                }

                // Se a palavra for muito longa, quebra a palavra
                if (fm.stringWidth(word) > maxWidth) {
                    List<String> brokenWord = breakLongWord(word, fm, maxWidth);
                    lines.addAll(brokenWord);
                    currentLine = new StringBuilder();
                } else {
                    currentLine = new StringBuilder(word);
                }
            }
        }

        // Adiciona a última linha se não estiver vazia
        if (currentLine.length() > 0) {
            lines.add(currentLine.toString());
        }

        return lines;
    }

    /**
     * Quebra palavras muito longas em partes menores
     * 
     * @param word     Palavra a ser quebrada
     * @param fm       Métricas da fonte
     * @param maxWidth Largura máxima permitida
     * @return Lista de partes da palavra
     */
    private List<String> breakLongWord(String word, FontMetrics fm, double maxWidth) {
        List<String> parts = new ArrayList<>();
        StringBuilder currentPart = new StringBuilder();

        // Quebra a palavra caractere por caractere
        for (char c : word.toCharArray()) {
            String testPart = currentPart.toString() + c;
            if (fm.stringWidth(testPart) <= maxWidth) {
                currentPart.append(c);
            } else {
                // Adiciona parte atual e começa nova parte
                if (currentPart.length() > 0) {
                    parts.add(currentPart.toString());
                }
                currentPart = new StringBuilder(String.valueOf(c));
            }
        }

        // Adiciona a última parte
        if (currentPart.length() > 0) {
            parts.add(currentPart.toString());
        }

        return parts;
    }
}