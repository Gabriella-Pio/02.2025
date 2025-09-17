package gui;

import arvore.NodeInfo;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.List;

/**
 * PAINEL DE RESULTADOS
 *
 * Este painel é responsável por mostrar os resultados da análise:
 * - Performance das estruturas de dados
 * - Frequência das palavras
 * - Mensagens de status
 *
 * É como a "tela de TV" da nossa aplicação onde tudo é exibido.
 */
public class ResultsPanel extends JPanel {

    // ====== COMPONENTES VISUAIS ======
    private JTextArea textArea; // Área de texto para mostrar resultados
    private JScrollPane textScrollPane; // scroll apenas do texto
    private JScrollPane scrollPane; // Painel com barra de rolagem
    private JPanel contentPanel; // container que fica dentro do scrollPane (vertical)
    private JButton clearButton; // Botão para limpar resultados
    private JButton exportButton; // Botão para exportar resultados
    private TreePanel treePanel;

    /**
     * CONSTRUTOR
     * Monta o painel de resultados
     */
    public ResultsPanel() {
        createComponents();
        layoutComponents();
        customizeComponents();
        setupEventHandlers();
    }

    /**
     * CRIAR COMPONENTES
     * "Fabricar" cada elemento do painel
     */
    private void createComponents() {
        // Área de texto principal
        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setText("🔍 Selecione um arquivo e clique em 'Analisar' para ver os resultados...\n");

        // JScrollPane só do texto
        textScrollPane = new JScrollPane(textArea);
        textScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        textScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        // Botões
        clearButton = new JButton("🗑️ Limpar Resultados");
        exportButton = new JButton("💾 Exportar Resultados");

        treePanel = new TreePanel();
        treePanel.setBackground(textArea.getBackground());

        // Painel que empilha texto + árvore
        contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.add(textScrollPane);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        contentPanel.add(treePanel);

        // ScrollPane principal que engloba tudo
        scrollPane = new JScrollPane(contentPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
    }

    /**
     * ORGANIZAR COMPONENTES (LAYOUT)
     * Decidir onde cada componente fica dentro deste painel
     */
    private void layoutComponents() {
        // Usar BorderLayout para organizar
        setLayout(new BorderLayout(5, 5));

        // Adicionar borda com título
        setBorder(new TitledBorder("📊 Resultados da Análise"));

        // Área de texto ocupa o centro (maior espaço)
        add(scrollPane, BorderLayout.CENTER);

        // Painel de botões na parte inferior
        JPanel buttonPanel = createButtonPanel();
        add(buttonPanel, BorderLayout.SOUTH);
    }

    /**
     * CRIAR PAINEL DE BOTÕES
     * Organizar os botões horizontalmente
     */
    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));

        // Adicionar botões
        buttonPanel.add(clearButton);
        buttonPanel.add(exportButton);

        return buttonPanel;
    }

    /**
     * PERSONALIZAR COMPONENTES
     * Ajustar aparência, cores, fontes, etc.
     */
    private void customizeComponents() {
        // Personalizar área de texto
        textArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12)); // Fonte monoespaçada (como terminal)
        textArea.setBackground(new Color(248, 249, 250)); // Cinza muito claro
        textArea.setForeground(Color.BLACK);
        textArea.setMargin(new Insets(10, 10, 10, 10)); // Margem interna
        textArea.setLineWrap(true); // Quebrar linhas longas
        textArea.setWrapStyleWord(true); // Quebrar por palavras, não por caracteres

        // dar um tamanho inicial para a área de texto e para painel de árvore
        textScrollPane.setPreferredSize(new Dimension(0, 300));
        treePanel.setPreferredSize(new Dimension(0, 300));

        // Personalizar barra de rolagem
        scrollPane.setPreferredSize(new Dimension(0, 400)); // Altura mínima
        scrollPane.getVerticalScrollBar().setUnitIncrement(16); // Rolagem mais suave

        // Personalizar botões
        customizeButton(clearButton, new Color(220, 53, 69), Color.WHITE); // Vermelho
        customizeButton(exportButton, new Color(0, 123, 255), Color.WHITE); // Azul
    }

    /**
     * PERSONALIZAR UM BOTÃO
     * Método auxiliar para aplicar estilo aos botões
     */
    private void customizeButton(JButton button, Color backgroundColor, Color textColor) {
        button.setBackground(backgroundColor);
        button.setForeground(textColor);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setPreferredSize(new Dimension(150, 30));
        button.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 11));

        // Adicionar efeito hover
        addButtonHoverEffect(button, backgroundColor);
    }

    /**
     * ADICIONAR EFEITO HOVER AOS BOTÕES
     * Botões ficam mais escuros quando o mouse passa sobre eles
     */
    private void addButtonHoverEffect(JButton button, Color originalColor) {
        // Calcular cor mais escura (diminui RGB em 20%)
        Color hoverColor = new Color(
                Math.max(0, (int) (originalColor.getRed() * 0.8)),
                Math.max(0, (int) (originalColor.getGreen() * 0.8)),
                Math.max(0, (int) (originalColor.getBlue() * 0.8)));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(hoverColor);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(originalColor);
            }
        });
    }

    /**
     * CONFIGURAR EVENTOS
     * Definir o que acontece quando os botões são clicados
     */
    private void setupEventHandlers() {
        // Evento do botão "Limpar"
        clearButton.addActionListener(e -> clearResults());

        // Evento do botão "Exportar"
        exportButton.addActionListener(e -> exportResults());
    }

    /**
     * LIMPAR RESULTADOS
     * Apagar todo o conteúdo da área de texto
     */
    public void clearResults() {
        textArea.setText("🔍 Resultados limpos. Pronto para nova análise...\n");

        // Rolar para o topo
        textArea.setCaretPosition(0);

        // limpar árvore também
        treePanel.setNodes(null);
    }

    /**
     * EXPORTAR RESULTADOS
     * Salvar o conteúdo em um arquivo
     */
    private void exportResults() {
        // Verificar se há conteúdo para exportar
        String content = textArea.getText().trim();
        if (content.isEmpty() || content.contains("Selecione um arquivo")) {
            JOptionPane.showMessageDialog(this,
                    "❌ Não há resultados para exportar!",
                    "Erro", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Abrir diálogo para salvar arquivo
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
     * Método para outras classes adicionarem texto aos resultados
     */
    public void addResult(String text) {
        textArea.append(text + "\n");

        // Auto-scroll para o final
        textArea.setCaretPosition(textArea.getDocument().getLength());
    }

    /**
     * ADICIONAR MÚLTIPLAS LINHAS
     * Adicionar várias linhas de uma vez
     */
    public void addResults(String[] lines) {
        for (String line : lines) {
            addResult(line);
        }
    }

    // Mostrar lista de palavras e frequências
    public void showWordFrequencies(java.util.List<String> lines) {
        // addHeader("Frequência de Palavras");
        for (String line : lines) {
            addResult(line);
        }
    }

    /**
     * Exibe a árvore dentro do mesmo painel rolável.
     * 
     * @param nodes lista de NodeInfo retornada por BST/AVL.getNodesWithLevel()
     */
    public void showTree(java.util.List<NodeInfo> nodes) {
        treePanel.setNodes(nodes);

        // ajustar preferências de tamanho do treePanel com base na profundidade
        // (opcional)
        int maxLevel = (nodes == null || nodes.isEmpty()) ? 0 : nodes.stream().mapToInt(n -> n.nivel).max().orElse(0);
        int prefHeight = 20 + (maxLevel + 2) * 80;
        treePanel.setPreferredSize(new Dimension(Math.max(600, scrollPane.getViewport().getWidth()), prefHeight));

        // atualizar visualizações
        treePanel.revalidate();
        treePanel.repaint();

        // garantir que a barra de rolagem atualize
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    /**
     * DEFINIR CONTEÚDO COMPLETO
     * Substituir todo o conteúdo atual
     */
    public void setResults(String content) {
        textArea.setText(content);
        textArea.setCaretPosition(0); // Rolar para o topo
    }

    /**
     * OBTER CONTEÚDO ATUAL
     * Permitir que outras classes leiam o conteúdo
     */
    public String getResults() {
        return textArea.getText();
    }

    /**
     * VERIFICAR SE HÁ RESULTADOS
     * Útil para validações
     */
    public boolean hasResults() {
        String content = textArea.getText().trim();
        return !content.isEmpty() && !content.contains("Selecione um arquivo");
    }

    /**
     * ADICIONAR SEPARADOR VISUAL
     * Método auxiliar para adicionar linhas divisórias
     */
    public void addSeparator() {
        addResult("═".repeat(80));
    }

    /**
     * ADICIONAR CABEÇALHO
     * Método auxiliar para adicionar títulos formatados
     */
    public void addHeader(String title) {
        addResult("");
        addResult("📋 " + title.toUpperCase());
        addResult("─".repeat(Math.min(80, title.length() + 10)));
    }

    /**
     * MOSTRAR MENSAGEM DE STATUS
     * Para mostrar progresso durante a análise
     */
    public void showStatus(String message) {
        addResult("⏳ " + message);
    }

    /**
     * MOSTRAR MENSAGEM DE SUCESSO
     * Para mostrar quando algo deu certo
     */
    public void showSuccess(String message) {
        addResult("✅ " + message);
    }

    /**
     * MOSTRAR MENSAGEM DE ERRO
     * Para mostrar quando algo deu errado
     */
    public void showError(String message) {
        addResult("❌ " + message);
    }
}