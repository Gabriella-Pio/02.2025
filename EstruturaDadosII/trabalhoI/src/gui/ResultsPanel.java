package gui;

import arvore.NodeInfo;
import arvore.TreeStats;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.List;

/**
 * PAINEL DE RESULTADOS
 * Atualizado para suportar análise de BST e AVL
 */
public class ResultsPanel extends JPanel {

    // ====== COMPONENTES VISUAIS ======
    private JTextArea textArea;
    private JScrollPane textScrollPane;
    private JScrollPane scrollPane;
    private JPanel contentPanel;
    private JButton clearButton;
    private JButton exportButton;
    private TreePanel treePanel;
    private JTabbedPane tabbedPane; // Para alternar entre texto e árvore

    /**
     * CONSTRUTOR
     */
    public ResultsPanel() {
        createComponents();
        layoutComponents();
        customizeComponents();
        setupEventHandlers();
    }

    /**
     * CRIAR COMPONENTES
     */
    private void createComponents() {
        // Área de texto principal
        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setText("🔍 Selecione um arquivo e clique em 'Analisar' para ver os resultados...\n");

        // JScrollPane só do texto
        textScrollPane = new JScrollPane(textArea);
        textScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        // Botões
        clearButton = new JButton("🗑️ Limpar Resultados");
        exportButton = new JButton("💾 Exportar Resultados");

        treePanel = new TreePanel();
        treePanel.setBackground(textArea.getBackground());

        // Painel com abas para alternar entre texto e árvore
        tabbedPane = new JTabbedPane();
        tabbedPane.addTab("📊 Resultados Textuais", textScrollPane);
        tabbedPane.addTab("🌳 Visualização da Árvore", treePanel);

        // ScrollPane principal
        scrollPane = new JScrollPane(tabbedPane);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    }

    /**
     * ORGANIZAR COMPONENTES (LAYOUT)
     */
    private void layoutComponents() {
        setLayout(new BorderLayout(5, 5));
        setBorder(new TitledBorder("📊 Resultados da Análise"));

        // Área com abas ocupa o centro
        add(scrollPane, BorderLayout.CENTER);

        // Painel de botões na parte inferior
        JPanel buttonPanel = createButtonPanel();
        add(buttonPanel, BorderLayout.SOUTH);
    }

    /**
     * CRIAR PAINEL DE BOTÕES
     */
    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        buttonPanel.add(clearButton);
        buttonPanel.add(exportButton);
        return buttonPanel;
    }

    /**
     * PERSONALIZAR COMPONENTES
     */
    private void customizeComponents() {
        // Personalizar área de texto
        textArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        textArea.setBackground(new Color(248, 249, 250));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);

        // Personalizar abas
        tabbedPane.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));

        // Personalizar botões
        customizeButton(clearButton, new Color(220, 53, 69), Color.WHITE);
        customizeButton(exportButton, new Color(0, 123, 255), Color.WHITE);
    }

    /**
     * PERSONALIZAR UM BOTÃO
     */
    private void customizeButton(JButton button, Color backgroundColor, Color textColor) {
        button.setBackground(backgroundColor);
        button.setForeground(textColor);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setPreferredSize(new Dimension(150, 30));
    }

    /**
     * CONFIGURAR EVENTOS
     */
    private void setupEventHandlers() {
        clearButton.addActionListener(e -> clearResults());
        exportButton.addActionListener(e -> exportResults());
    }

    /**
     * LIMPAR RESULTADOS
     */
    public void clearResults() {
        textArea.setText("🔍 Resultados limpos. Pronto para nova análise...\n");
        textArea.setCaretPosition(0);
        treePanel.setNodes(null);
    }

    /**
     * EXPORTAR RESULTADOS
     */
    private void exportResults() {
        String content = textArea.getText().trim();
        if (content.isEmpty() || content.contains("Selecione um arquivo")) {
            JOptionPane.showMessageDialog(this,
                    "❌ Não há resultados para exportar!",
                    "Erro", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Salvar Resultados da Análise");
        fileChooser.setSelectedFile(new java.io.File("analise_resultados.txt"));

        int result = fileChooser.showSaveDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            try {
                java.io.File file = fileChooser.getSelectedFile();
                java.io.FileWriter writer = new java.io.FileWriter(file);
                writer.write(content);
                writer.close();

                JOptionPane.showMessageDialog(this,
                        "✅ Resultados exportados com sucesso!\n📄 " + file.getName(),
                        "Exportação Concluída", JOptionPane.INFORMATION_MESSAGE);

            } catch (java.io.IOException e) {
                JOptionPane.showMessageDialog(this,
                        "❌ Erro ao salvar arquivo: " + e.getMessage(),
                        "Erro de Exportação", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * ADICIONAR RESULTADO
     */
    public void addResult(String text) {
        textArea.append(text + "\n");
        textArea.setCaretPosition(textArea.getDocument().getLength());
    }

    /**
     * MOSTRAR FREQUÊNCIAS DE PALAVRAS
     */
    public void showWordFrequencies(List<String> lines) {
        addHeader("Frequência de Palavras");
        addResult(String.format("%-20s %s", "PALAVRA", "FREQUÊNCIA"));
        addResult("─".repeat(30));
        
        for (String line : lines) {
            if (line.contains(" -> ")) {
                String[] parts = line.split(" -> ");
                if (parts.length == 2) {
                    addResult(String.format("%-20s %5s", parts[0], parts[1]));
                }
            }
        }
        addSeparator();
    }

    /**
     * MOSTRAR ÁRVORE
     */
    public void showTree(List<NodeInfo> nodes) {
        treePanel.setNodes(nodes);
        
        // Ajustar tamanho baseado na profundidade
        if (nodes != null && !nodes.isEmpty()) {
            int maxLevel = nodes.stream().mapToInt(n -> n.nivel).max().orElse(0);
            int prefHeight = 20 + (maxLevel + 2) * 80;
            treePanel.setPreferredSize(new Dimension(600, prefHeight));
        }
        
        treePanel.revalidate();
        treePanel.repaint();
        
        // Mudar para a aba da árvore automaticamente
        tabbedPane.setSelectedIndex(1);
    }

    /**
     * MOSTRAR ANÁLISE DETALHADA
     */
    public void showAnalysis(TreeStats stats, String structureType) {
        addHeader("Análise de Performance - " + structureType);
        
        addResult("• Comparações: " + stats.getComparacoes());
        addResult("• Atribuições: " + stats.getAtribuicoes());
        
        if (stats.getRotacoes() > 0) {
            addResult("• Rotações AVL: " + stats.getRotacoes());
        }
        
        addResult("• Tempo de execução: " + String.format("%.2f ms", stats.getTempoMilissegundos()));
        addResult("• Altura da estrutura: " + stats.getAltura());
        
        // Análise de eficiência
        addSeparator();
        addResult("📈 Análise de Eficiência:");
        
        if (structureType.equals("Vetor Dinâmico")) {
            addResult("• Tipo: Vetor Dinâmico (Busca Binária)");
            addResult("• Complexidade: O(n log n) para construção");
        } else if (structureType.equals("BST")) {
            addResult("• Tipo: Árvore Binária de Busca");
            addResult("• Complexidade: O(n²) no pior caso (árvore degenerada)");
            addResult("• Balanceamento: ❌ Não balanceada");
        } else if (structureType.equals("AVL")) {
            addResult("• Tipo: Árvore AVL Balanceada");
            addResult("• Complexidade: O(n log n) garantido");
            addResult("• Balanceamento: ✅ Balanceada (" + stats.getRotacoes() + " rotações)");
        }
        
        addSeparator();
    }

    /**
     * COMPARAR ESTRUTURAS
     */
    public void compareStructures(TreeStats vectorStats, TreeStats bstStats, TreeStats avlStats) {
        addHeader("🔍 Comparação entre Estruturas");
        
        addResult(String.format("%-15s %-12s %-12s %-12s %-10s", 
            "ESTRUTURA", "COMPARAÇÕES", "ATRIBUIÇÕES", "TEMPO (ms)", "ALTURA"));
        addResult("─".repeat(65));
        
        addResult(String.format("%-15s %-12d %-12d %-12.2f %-10d", 
            "Vetor", vectorStats.getComparacoes(), vectorStats.getAtribuicoes(), 
            vectorStats.getTempoMilissegundos(), vectorStats.getAltura()));
        
        addResult(String.format("%-15s %-12d %-12d %-12.2f %-10d", 
            "BST", bstStats.getComparacoes(), bstStats.getAtribuicoes(), 
            bstStats.getTempoMilissegundos(), bstStats.getAltura()));
        
        addResult(String.format("%-15s %-12d %-12d %-12.2f %-10d", 
            "AVL", avlStats.getComparacoes(), avlStats.getAtribuicoes(), 
            avlStats.getTempoMilissegundos(), avlStats.getAltura()));
        
        addSeparator();
        
        // Análise comparativa
        addResult("💡 Insights:");
        if (avlStats.getAltura() < bstStats.getAltura()) {
            addResult("• AVL é " + (bstStats.getAltura() - avlStats.getAltura()) + 
                     " níveis mais balanceada que BST");
        }
        
        if (avlStats.getTempoMilissegundos() < bstStats.getTempoMilissegundos()) {
            addResult("• AVL foi " + String.format("%.2f", bstStats.getTempoMilissegundos() - avlStats.getTempoMilissegundos()) + 
                     " ms mais rápida que BST");
        }
    }

    /**
     * DEFINIR CONTEÚDO COMPLETO
     */
    public void setResults(String content) {
        textArea.setText(content);
        textArea.setCaretPosition(0);
    }

    /**
     * ADICIONAR SEPARADOR VISUAL
     */
    public void addSeparator() {
        addResult("═".repeat(80));
    }

    /**
     * ADICIONAR CABEÇALHO
     */
    public void addHeader(String title) {
        addResult("");
        addResult("📋 " + title.toUpperCase());
        addResult("─".repeat(Math.min(80, title.length() + 10)));
    }

    /**
     * MOSTRAR MENSAGEM DE STATUS
     */
    public void showStatus(String message) {
        addResult("⏳ " + message);
    }

    /**
     * MOSTRAR MENSAGEM DE SUCESSO
     */
    public void showSuccess(String message) {
        addResult("✅ " + message);
    }

    /**
     * MOSTRAR MENSAGEM DE ERRO
     */
    public void showError(String message) {
        addResult("❌ " + message);
    }
}