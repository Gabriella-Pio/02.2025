// src/gui/TreePanel.java

package gui;

import arvore.Node;
import arvore.NodeInfo;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.*;
import java.util.List;

public class TreePanel extends JPanel {
    private List<NodeInfo> nodes;
    private int nodeRadius = 30;
    private int verticalSpacing = 80;
    private int minHorizontalSpacing = 60;
    private int topMargin = 70;
    private int sideMargin = 90;

    // Colors
    private final Color NODE_COLOR = new Color(173, 216, 230);
    private final Color NODE_BORDER = new Color(70, 130, 180);
    private final Color TEXT_COLOR = Color.BLACK;
    private final Color LEFT_LINE_COLOR = new Color(70, 130, 180); // Blue for left children
    private final Color RIGHT_LINE_COLOR = new Color(220, 53, 69); // Red for right children

    public TreePanel() {
        setBackground(Color.WHITE);
    }

    public void setNodes(List<NodeInfo> nodes) {
        this.nodes = (nodes == null) ? Collections.emptyList() : new ArrayList<>(nodes);
        revalidate();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (nodes == null || nodes.isEmpty()) {
            drawNoTreeMessage(g);
            return;
        }

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        try {
            Node root = findRoot(nodes);
            if (root == null) {
                drawErrorMessage(g2, "Root not found");
                return;
            }

            // calcula posições com novo layout
            Map<Node, Point> positions = calculateBinaryTreeLayout(root);

            // desenha conexões primeiro
            drawBinaryTreeConnections(g2, positions, root);

            // depois desenha os nós
            drawNodes(g2, positions);

        } catch (Exception e) {
            drawErrorMessage(g2, "Error drawing tree: " + e.getMessage());
            e.printStackTrace();
        } finally {
            g2.dispose();
        }
    }

    private void drawNoTreeMessage(Graphics g) {
        g.setColor(Color.GRAY);
        g.setFont(new Font("SansSerif", Font.ITALIC, 16));
        FontMetrics fm = g.getFontMetrics();
        String message = "No tree to display";
        int x = (getWidth() - fm.stringWidth(message)) / 2;
        int y = getHeight() / 2;
        g.drawString(message, x, y);
    }

    private void drawErrorMessage(Graphics2D g2, String message) {
        g2.setColor(Color.RED);
        g2.setFont(new Font("SansSerif", Font.PLAIN, 14));
        g2.drawString(message, 20, 30);
    }

    private Node findRoot(List<NodeInfo> nodes) {
        for (NodeInfo ni : nodes) {
            if (ni.nivel == 0) {
                return ni.node;
            }
        }
        for (NodeInfo ni : nodes) {
            if (ni.pai == null) {
                return ni.node;
            }
        }
        return nodes.isEmpty() ? null : nodes.get(0).node;
    }

    private Map<Node, Point> calculateBinaryTreeLayout(Node root) {
        Map<Node, Point> positions = new HashMap<>();
        Map<Node, Integer> subtreeWidths = new HashMap<>();

        // calcular largura das subárvores
        computeSubtreeWidths(root, subtreeWidths);

        // atribuir posições
        assignPositions(root, 0, 0, subtreeWidths, positions);

        // normalizar coordenadas (para não ficarem negativas)
        int minX = positions.values().stream().mapToInt(p -> p.x).min().orElse(0);
        int maxX = positions.values().stream().mapToInt(p -> p.x).max().orElse(0);

        int shift = sideMargin - minX + nodeRadius;
        for (Point p : positions.values()) {
            p.x += shift;
            p.y += topMargin;
        }

        // ajustar tamanho do painel
        int panelWidth = (maxX - minX) + 2 * sideMargin;
        int panelHeight = topMargin + getTreeHeight(root) * verticalSpacing + 100;
        setPreferredSize(new Dimension(panelWidth, panelHeight));
        revalidate();

        return positions;
    }

    private int computeSubtreeWidths(Node node, Map<Node, Integer> widths) {
        if (node == null)
            return 0;
        int left = computeSubtreeWidths(getLeftChild(node), widths);
        int right = computeSubtreeWidths(getRightChild(node), widths);
        int width = Math.max(1, left + right); // pelo menos 1
        widths.put(node, width);
        return width;
    }

    private void assignPositions(Node node, int x, int depth,
            Map<Node, Integer> widths, Map<Node, Point> positions) {
        if (node == null)
            return;

        Node left = getLeftChild(node);
        Node right = getRightChild(node);

        int nodeY = depth * verticalSpacing;

        if (left == null && right == null) {
            // nó folha
            positions.put(node, new Point(x, nodeY));
            return;
        }

        int leftX = x;
        int rightX = x;

        if (left != null) {
            assignPositions(left, x, depth + 1, widths, positions);
            leftX = positions.get(left).x;
        }

        if (right != null) {
            int rightStartX = (left != null)
                    ? leftX + widths.get(left) * minHorizontalSpacing
                    : x + minHorizontalSpacing;
            assignPositions(right, rightStartX, depth + 1, widths, positions);
            rightX = positions.get(right).x;
        }

        // centraliza entre os filhos ou alinha com o único filho
        int nodeX;
        if (left != null && right != null) {
            nodeX = (leftX + rightX) / 2;
        } else if (left != null) {
            nodeX = leftX;
        } else {
            nodeX = rightX;
        }

        positions.put(node, new Point(nodeX, nodeY));
    }

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

    private int getTreeHeight(Node node) {
        if (node == null)
            return 0;
        return 1 + Math.max(getTreeHeight(getLeftChild(node)), getTreeHeight(getRightChild(node)));
    }

    private void drawBinaryTreeConnections(Graphics2D g2, Map<Node, Point> positions, Node node) {
        if (node == null)
            return;

        Point nodePos = positions.get(node);
        if (nodePos == null)
            return;

        Node leftChild = getLeftChild(node);
        Node rightChild = getRightChild(node);

        g2.setStroke(new BasicStroke(2.5f));

        if (leftChild != null) {
            Point childPos = positions.get(leftChild);
            if (childPos != null) {
                g2.setColor(LEFT_LINE_COLOR);
                g2.drawLine(nodePos.x, nodePos.y, childPos.x, childPos.y);
                g2.setFont(new Font("SansSerif", Font.BOLD, 10));
                int midX = (nodePos.x + childPos.x) / 2 - 10;
                int midY = (nodePos.y + childPos.y) / 2;
                g2.drawString("L", midX, midY);
                drawBinaryTreeConnections(g2, positions, leftChild);
            }
        }

        if (rightChild != null) {
            Point childPos = positions.get(rightChild);
            if (childPos != null) {
                g2.setColor(RIGHT_LINE_COLOR);
                g2.drawLine(nodePos.x, nodePos.y, childPos.x, childPos.y);
                g2.setFont(new Font("SansSerif", Font.BOLD, 10));
                int midX = (nodePos.x + childPos.x) / 2 + 5;
                int midY = (nodePos.y + childPos.y) / 2;
                g2.drawString("R", midX, midY);
                drawBinaryTreeConnections(g2, positions, rightChild);
            }
        }
    }

    private void drawNodes(Graphics2D g2, Map<Node, Point> positions) {
        for (Map.Entry<Node, Point> entry : positions.entrySet()) {
            drawStyledNode(g2, entry.getKey(), entry.getValue());
        }
    }

    private void drawStyledNode(Graphics2D g2, Node node, Point pos) {
        int x = pos.x;
        int y = pos.y;

        g2.setColor(new Color(0, 0, 0, 30));
        g2.fillOval(x - nodeRadius + 2, y - nodeRadius + 2, nodeRadius * 2, nodeRadius * 2);

        g2.setColor(NODE_COLOR);
        g2.fillOval(x - nodeRadius, y - nodeRadius, nodeRadius * 2, nodeRadius * 2);

        g2.setColor(NODE_BORDER);
        g2.setStroke(new BasicStroke(2.0f));
        g2.drawOval(x - nodeRadius, y - nodeRadius, nodeRadius * 2, nodeRadius * 2);

        drawNodeText(g2, node, pos);
    }

    private void drawNodeText(Graphics2D g2, Node node, Point pos) {
        g2.setColor(TEXT_COLOR);

        Font font = new Font("SansSerif", Font.BOLD, 10);
        g2.setFont(font);
        FontMetrics fm = g2.getFontMetrics();

        String word = node.palavra;
        String freq = "(" + node.frequencia + ")";

        if (word.length() > 7) {
            word = word.substring(0, 5) + "..";
        }

        Rectangle2D wordBounds = fm.getStringBounds(word, g2);
        Rectangle2D freqBounds = fm.getStringBounds(freq, g2);

        int wordX = pos.x - (int) (wordBounds.getWidth() / 2);
        int freqX = pos.x - (int) (freqBounds.getWidth() / 2);

        g2.drawString(word, wordX, pos.y - 3);

        Font smallFont = new Font("SansSerif", Font.PLAIN, 8);
        g2.setFont(smallFont);
        g2.setColor(new Color(80, 80, 80));
        g2.drawString(freq, freqX, pos.y + 10);
    }
}
