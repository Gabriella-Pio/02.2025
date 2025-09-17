package gui;

import arvore.Node;
import arvore.NodeInfo;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Painel que desenha a árvore a partir de uma lista de NodeInfo.
 * - Agrupa nós por nível (nivel em NodeInfo)
 * - Distribui nós de cada nível horizontalmente
 * - Desenha linhas entre pai e filhos (se houver)
 */
public class TreePanel extends JPanel {
    private List<NodeInfo> nodes; // lista enviada pela GUI
    private int nodeRadius = 20;
    private int verticalSpacing = 80;
    private int topMargin = 20;
    private int horizontalPadding = 20;

    public TreePanel() {
        setBackground(Color.WHITE);
    }

    public void setNodes(List<NodeInfo> nodes) {
        this.nodes = (nodes == null) ? Collections.emptyList() : new ArrayList<>(nodes);
        // atualizar preferências de tamanho com base na profundidade
        int maxLevel = nodes == null || nodes.isEmpty() ? 0 :
                nodes.stream().mapToInt(n -> n.nivel).max().orElse(0);
        int prefHeight = topMargin + (maxLevel + 2) * verticalSpacing;
        // prefWidth deixamos dinâmico; altura ajustada para permitir rolagem vertical
        setPreferredSize(new Dimension(Math.max(600, getWidth()), prefHeight));
        revalidate();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (nodes == null || nodes.isEmpty()) return;

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Agrupar por nível (Map ordenado por chave)
        Map<Integer, List<NodeInfo>> levels = new TreeMap<>();
        for (NodeInfo ni : nodes) {
            levels.computeIfAbsent(ni.nivel, k -> new ArrayList<>()).add(ni);
        }

        // mapa de posições: Node -> Point (x, y)
        Map<Node, Point> coords = new HashMap<>();
        int panelWidth = Math.max(getWidth(), 600);

        // Para cada nível, distribui nodes uniformemente na largura disponível
        for (Map.Entry<Integer, List<NodeInfo>> entry : levels.entrySet()) {
            int level = entry.getKey();
            List<NodeInfo> list = entry.getValue();
            int n = list.size();
            for (int i = 0; i < n; i++) {
                // distribuição: (i+1)/(n+1) espaço para evitar bordas
                int x = horizontalPadding + (int) ((long) (panelWidth - 2 * horizontalPadding) * (i + 1) / (n + 1.0));
                int y = topMargin + level * verticalSpacing;
                coords.put(list.get(i).node, new Point(x, y));
            }
        }

        // Desenhar ligações pai -> filho (linhas)
        g2.setColor(Color.GRAY);
        g2.setStroke(new BasicStroke(1.5f));
        for (NodeInfo ni : nodes) {
            Node node = ni.node;
            Point p = coords.get(node);
            if (p == null) continue;
            if (node.esquerda != null && coords.containsKey(node.esquerda)) {
                Point c = coords.get(node.esquerda);
                g2.drawLine(p.x, p.y, c.x, c.y);
            }
            if (node.direita != null && coords.containsKey(node.direita)) {
                Point c = coords.get(node.direita);
                g2.drawLine(p.x, p.y, c.x, c.y);
            }
        }

        // Desenhar nós (círculos e labels)
        g2.setFont(new Font("SansSerif", Font.PLAIN, 12));
        for (NodeInfo ni : nodes) {
            Point p = coords.get(ni.node);
            if (p == null) continue;

            int x = p.x;
            int y = p.y;

            // círculo preenchido
            g2.setColor(new Color(200, 230, 255));
            g2.fillOval(x - nodeRadius, y - nodeRadius, nodeRadius * 2, nodeRadius * 2);

            // contorno
            g2.setColor(Color.BLACK);
            // g2.drawOval(x - nodeRadius, y - nodeRadius, nodeRadius * 2, nodeRadius * 2);

            // texto (palavra(freq))
            String label = ni.palavra + "(" + ni.frequencia + ")";
            FontMetrics fm = g2.getFontMetrics();
            int textWidth = fm.stringWidth(label);
            int textHeight = fm.getAscent();
            g2.drawString(label, x - textWidth / 2, y + textHeight / 2 - 2);
        }

        g2.dispose();
    }
}
