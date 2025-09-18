package gui;

import arvore.Node;
import arvore.NodeInfo;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class TreePanel extends JPanel {
    private List<NodeInfo> nodes;
    private int nodeRadius = 20;
    private int verticalSpacing = 80;
    private int topMargin = 20;
    private int horizontalPadding = 20;
    private int horizontalScale = 50; // Scale factor for x coordinates

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
        if (nodes == null || nodes.isEmpty())
            return;

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Find root node
        Node root = findRoot(nodes);
        if (root == null)
            return;

        // Apply Reingold-Tilford algorithm
        DrawTree drawTree = TreeLayout.layout(root);
        Map<Node, Point> coords = TreeLayout.getCoordinates(drawTree);

        // Scale and position coordinates for display
        scaleCoordinates(coords);

        // Draw connections
        drawConnections(g2, coords);

        // Draw nodes
        drawNodes(g2, coords);

        g2.dispose();
    }

    private Node findRoot(List<NodeInfo> nodes) {
        for (NodeInfo ni : nodes) {
            if (ni.nivel == 0) {
                return ni.node;
            }
        }
        return null;
    }

    private void scaleCoordinates(Map<Node, Point> coords) {
        // Find min and max x values for scaling
        int minX = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        int maxY = 0;

        for (Point p : coords.values()) {
            minX = Math.min(minX, p.x);
            maxX = Math.max(maxX, p.x);
            maxY = Math.max(maxY, p.y);
        }

        // Scale and position coordinates
        int panelWidth = Math.max(getWidth(), 600);
        int xRange = Math.max(1, maxX - minX);

        for (Map.Entry<Node, Point> entry : coords.entrySet()) {
            Point p = entry.getValue();
            int scaledX = horizontalPadding + (int) ((p.x - minX) / (double) xRange *
                    (panelWidth - 2 * horizontalPadding));
            int scaledY = topMargin + p.y * verticalSpacing;
            entry.setValue(new Point(scaledX, scaledY));
        }

        // Update preferred size
        int prefHeight = topMargin + (maxY + 2) * verticalSpacing;
        setPreferredSize(new Dimension(panelWidth, prefHeight));
    }

    private void drawConnections(Graphics2D g2, Map<Node, Point> coords) {
        g2.setColor(Color.GRAY);
        g2.setStroke(new BasicStroke(1.5f));

        for (NodeInfo ni : nodes) {
            Node node = ni.node;
            Point p = coords.get(node);
            if (p == null)
                continue;

            // Draw lines to all children
            if (node.filhos != null) {
                for (Node child : node.filhos) {
                    Point c = coords.get(child);
                    if (c != null) {
                        g2.drawLine(p.x, p.y, c.x, c.y);
                    }
                }
            }
        }
    }

    private void drawNodes(Graphics2D g2, Map<Node, Point> coords) {
        g2.setFont(new Font("SansSerif", Font.PLAIN, 12));

        for (NodeInfo ni : nodes) {
            Point p = coords.get(ni.node);
            if (p == null)
                continue;

            int x = p.x;
            int y = p.y;

            // Draw node circle
            g2.setColor(new Color(200, 230, 255));
            g2.fillOval(x - nodeRadius, y - nodeRadius, nodeRadius * 2, nodeRadius * 2);
            g2.setColor(Color.BLACK);
            g2.drawOval(x - nodeRadius, y - nodeRadius, nodeRadius * 2, nodeRadius * 2);

            // Draw label
            String label = ni.palavra + "(" + ni.frequencia + ")";
            FontMetrics fm = g2.getFontMetrics();
            int textWidth = fm.stringWidth(label);
            int textHeight = fm.getAscent();
            g2.drawString(label, x - textWidth / 2, y + textHeight / 2 - 2);
        }
    }
}