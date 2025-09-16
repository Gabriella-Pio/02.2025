package gui;
    import javax.swing.*;
    import javax.swing.border.TitledBorder;
    import java.awt.*;

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
        private JTextArea textArea;           // Área de texto para mostrar resultados
        private JScrollPane scrollPane;       // Painel com barra de rolagem
        private JButton clearButton;          // Botão para limpar resultados
        private JButton exportButton;         // Botão para exportar resultados

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
            textArea.setEditable(false); // Usuário não pode editar
            textArea.setText("🔍 Selecione um arquivo e clique em 'Analisar' para ver os resultados...\n");

            // Painel com barra de rolagem
            scrollPane = new JScrollPane(textArea);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

            // Botões de ação
            clearButton = new JButton("🗑️ Limpar Resultados");
            exportButton = new JButton("💾 Exportar Resultados");
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
            textArea.setLineWrap(true);      // Quebrar linhas longas
            textArea.setWrapStyleWord(true); // Quebrar por palavras, não por caracteres

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
                    Math.max(0, (int)(originalColor.getRed() * 0.8)),
                    Math.max(0, (int)(originalColor.getGreen() * 0.8)),
                    Math.max(0, (int)(originalColor.getBlue() * 0.8))
            );

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