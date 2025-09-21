// src/gui/ConfigPanel.java

package gui;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;

/**
 * PAINEL DE CONFIGURAÇÕES
 *
 * Este painel permite ao usuário:
 * 1. Escolher qual estrutura usar para mostrar as frequências
 * 2. Iniciar a análise do texto
 * 3. Configurar modo passo a passo (para árvores)
 *
 * É como o "painel de controle" da nossa aplicação.
 */
public class ConfigPanel extends JPanel {

    // ====== COMPONENTES VISUAIS ======
    private JComboBox<String> structureComboBox; // Lista suspensa para escolher estrutura
    private JButton analyzeButton; // Botão para iniciar análise
    private JLabel instructionLabel; // Label com instruções

    // Componentes para modo passo a passo
    private JCheckBox stepByStepCheckBox; // Checkbox para ativar modo passo a passo
    private JLabel speedLabel; // Label para controle de velocidade
    private JSlider speedSlider; // Slider para ajustar velocidade

    // Controles de execução passo a passo
    private JButton playButton; // Botão play
    private JButton pauseButton; // Botão pause
    private JButton nextButton; // Botão next (próximo passo)
    private JButton stopButton; // Botão stop

    private JPanel stepPanel; // Painel para opções de passo a passo
    private JPanel controlPanel; // Painel para controles de execução

    /**
     * CONSTRUTOR
     * Monta o painel de configurações
     */
    public ConfigPanel() {
        createComponents(); // Cria os componentes
        layoutComponents(); // Organiza o layout
        customizeComponents(); // Personaliza aparência
        setupInteractions(); // Configura interações
    }

    /**
     * CRIAR COMPONENTES
     * "Fabricar" cada elemento do painel
     */
    private void createComponents() {
        // Label com instruções
        instructionLabel = new JLabel("Escolha a estrutura para exibir as frequências:");
        instructionLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));

        // ComboBox (lista suspensa) com as opções de estrutura
        String[] structures = {
                "🔍 Busca Binária (Vetor Dinâmico)",
                "🌳 Árvore Binária Simples (BST)",
                "⚖️ Árvore AVL (Balanceada)"
        };
        structureComboBox = new JComboBox<>(structures);
        structureComboBox.setSelectedIndex(0); // Selecionar a primeira opção por padrão

        // Botão para iniciar análise
        analyzeButton = new JButton("🚀 Analisar Texto");
        analyzeButton.setEnabled(false); // Inicialmente desabilitado (sem arquivo selecionado)

        // Componentes para modo passo a passo (inicialmente não visíveis)
        stepByStepCheckBox = new JCheckBox("Montar passo a passo");
        // speedLabel = new JLabel("Velocidade (ms):");

        // Controles de velocidade (comentados para versão simplificada)
        // speedSlider = new JSlider(50, 2000, 500); // 50–2000 ms
        // speedSlider.setPaintTicks(true);
        // speedSlider.setPaintLabels(true);
        // speedSlider.setMajorTickSpacing(500);
        // speedSlider.setMinorTickSpacing(50);
        // speedSlider.setEnabled(false);

        // Botões de controle de execução
        playButton = new JButton("▶ Play");
        pauseButton = new JButton("⏸ Pause");
        nextButton = new JButton("⏭ Next");
        stopButton = new JButton("⏹ Stop");

        // Inicialmente desabilitados
        playButton.setEnabled(false);
        pauseButton.setEnabled(false);
        nextButton.setEnabled(false);
        stopButton.setEnabled(false);

        // Painéis para agrupar componentes
        stepPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
    }

    /**
     * ORGANIZAR COMPONENTES (LAYOUT)
     * Decidir onde cada componente fica dentro deste painel
     */
    private void layoutComponents() {
        // Usar FlowLayout para organizar em linha
        setLayout(new FlowLayout(FlowLayout.LEFT, 12, 8));

        // Adicionar borda com título
        setBorder(new TitledBorder("⚙️ Configurações de Análise"));

        // Adicionar componentes na ordem
        add(instructionLabel);
        add(structureComboBox);

        // Adicionar componentes de passo a passo ao painel
        stepPanel.add(stepByStepCheckBox);
        // stepPanel.add(speedLabel);
        // stepPanel.add(speedSlider);
        add(stepPanel);

        // Adicionar botões de controle ao painel
        controlPanel.add(playButton);
        controlPanel.add(pauseButton);
        controlPanel.add(nextButton);
        controlPanel.add(stopButton);
        add(controlPanel);

        // Adicionar botão principal de análise
        add(analyzeButton);

        // Inicialmente ocultar painéis de controle
        stepPanel.setVisible(false);
        controlPanel.setVisible(false);
    }

    /**
     * PERSONALIZAR COMPONENTES
     * Ajustar cores, fontes, tamanhos, etc.
     */
    private void customizeComponents() {
        // Configurar combobox
        structureComboBox.setPreferredSize(new Dimension(300, 30));
        structureComboBox.setBackground(Color.WHITE);

        // Configurar botão de análise
        analyzeButton.setPreferredSize(new Dimension(160, 36));
        analyzeButton.setBackground(new Color(40, 167, 69)); // Verde
        analyzeButton.setForeground(Color.WHITE);
        analyzeButton.setFocusPainted(false);
        analyzeButton.setBorderPainted(false);
        analyzeButton.setOpaque(true);
        analyzeButton.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));

        // Configurações de slider (comentadas)
        // speedSlider.setPreferredSize(new Dimension(250, 50));
    }

    /**
     * CONFIGURAR INTERAÇÕES
     * Definir comportamentos para eventos dos componentes
     */
    private void setupInteractions() {
        // Exibir ou ocultar painel de passo-a-passo dependendo da estrutura
        structureComboBox.addItemListener(e -> {
            if (getSelectedStructureIndex() == 0) { // Vetor (não suporta passo a passo)
                stepPanel.setVisible(false);
                controlPanel.setVisible(false);
                stepByStepCheckBox.setSelected(false);
            } else { // Árvores (suportam passo a passo)
                stepPanel.setVisible(true);
                controlPanel.setVisible(stepByStepCheckBox.isSelected());
            }
            revalidate();
            repaint();
        });

        // Mostrar/ocultar controles quando checkbox for alterado
        stepByStepCheckBox.addItemListener(e -> {
            boolean on = e.getStateChange() == ItemEvent.SELECTED;
            // speedSlider.setEnabled(on);
            controlPanel.setVisible(on);
        });
    }

    // ===== MÉTODOS PÚBLICOS PARA CONTROLE EXTERNO =====

    /**
     * Define listener para o botão de análise
     * 
     * @param listener ActionListener para o botão
     */
    public void setAnalyzeButtonListener(ActionListener listener) {
        for (ActionListener l : analyzeButton.getActionListeners())
            analyzeButton.removeActionListener(l);
        analyzeButton.addActionListener(listener);
    }

    /**
     * Define listeners para os botões de controle
     */
    public void setPlayListener(ActionListener l) {
        playButton.addActionListener(l);
    }

    public void setPauseListener(ActionListener l) {
        pauseButton.addActionListener(l);
    }

    public void setNextListener(ActionListener l) {
        nextButton.addActionListener(l);
    }

    public void setStopListener(ActionListener l) {
        stopButton.addActionListener(l);
    }

    /**
     * Habilita/desabilita botões de controle
     * 
     * @param enable true para habilitar, false para desabilitar
     */
    public void enableControlButtons(boolean enable) {
        playButton.setEnabled(enable);
        pauseButton.setEnabled(enable);
        nextButton.setEnabled(enable);
        stopButton.setEnabled(enable);
    }

    /**
     * Habilita/desabilita botão de análise com feedback visual
     * 
     * @param enabled true para habilitar, false para desabilitar
     */
    public void setAnalyzeButtonEnabled(boolean enabled) {
        analyzeButton.setEnabled(enabled);
        if (enabled) {
            analyzeButton.setBackground(new Color(40, 167, 69)); // Verde
            analyzeButton.setText("🚀 Analisar Texto");
        } else {
            analyzeButton.setBackground(Color.GRAY); // Cinza
            analyzeButton.setText("⏳ Selecione um arquivo primeiro");
        }
    }

    /**
     * Obtém índice da estrutura selecionada
     * 
     * @return 0=Vetor, 1=BST, 2=AVL
     */
    public int getSelectedStructureIndex() {
        return structureComboBox.getSelectedIndex();
    }

    /**
     * Verifica se modo passo a passo está habilitado
     * 
     * @return true se habilitado, false caso contrário
     */
    public boolean isStepByStepEnabled() {
        return stepByStepCheckBox.isSelected();
    }

    /**
     * Habilita/desabilita toda a configuração durante análise
     * 
     * @param enabled true para habilitar, false para desabilitar
     */
    public void setConfigurationEnabled(boolean enabled) {
        structureComboBox.setEnabled(enabled);
        stepByStepCheckBox.setEnabled(enabled);
        // speedSlider.setEnabled(enabled && stepByStepCheckBox.isSelected());
        enableControlButtons(false);

        if (!enabled) {
            analyzeButton.setText("⏳ Analisando...");
            analyzeButton.setBackground(Color.ORANGE); // Laranja durante análise
        } else if (analyzeButton.isEnabled()) {
            analyzeButton.setText("🚀 Analisar Texto");
            analyzeButton.setBackground(new Color(40, 167, 69)); // Verde
        }
    }
}