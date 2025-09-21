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
 *
 * É como o "painel de controle" da nossa aplicação.
 */
public class ConfigPanel extends JPanel {

    // ====== COMPONENTES VISUAIS ======
    private JComboBox<String> structureComboBox; // Lista suspensa para escolher estrutura
    private JButton analyzeButton; // Botão para iniciar análise
    private JLabel instructionLabel; // Label com instruções

    // Passo-a-passo
    private JCheckBox stepByStepCheckBox;
    private JLabel speedLabel;
    private JSlider speedSlider;

    // Controles extras
    private JButton playButton;
    private JButton pauseButton;
    private JButton nextButton;
    private JButton stopButton;

    private JPanel stepPanel;
    private JPanel controlPanel;

    /**
     * CONSTRUTOR
     * Monta o painel de configurações
     */
    public ConfigPanel() {
        createComponents();
        layoutComponents();
        customizeComponents();
        setupInteractions();
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

        // speedSlider = new JSlider(50, 2000, 500); // 50–2000 ms
        // speedSlider.setPaintTicks(true);
        // speedSlider.setPaintLabels(true);
        // speedSlider.setMajorTickSpacing(500);
        // speedSlider.setMinorTickSpacing(50);
        // speedSlider.setEnabled(false);

        playButton = new JButton("▶ Play");
        pauseButton = new JButton("⏸ Pause");
        nextButton = new JButton("⏭ Next");
        stopButton = new JButton("⏹ Stop");

        playButton.setEnabled(false);
        pauseButton.setEnabled(false);
        nextButton.setEnabled(false);
        stopButton.setEnabled(false);

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

        stepPanel.add(stepByStepCheckBox);
        // stepPanel.add(speedLabel);
        // stepPanel.add(speedSlider);
        add(stepPanel);

        controlPanel.add(playButton);
        controlPanel.add(pauseButton);
        controlPanel.add(nextButton);
        controlPanel.add(stopButton);
        add(controlPanel);

        add(analyzeButton);

        stepPanel.setVisible(false);
        controlPanel.setVisible(false);
    }

    /**
     * PERSONALIZAR COMPONENTES
     * Ajustar cores, fontes, tamanhos, etc.
     */

    private void customizeComponents() {
        structureComboBox.setPreferredSize(new Dimension(300, 30));
        structureComboBox.setBackground(Color.WHITE);

        analyzeButton.setPreferredSize(new Dimension(160, 36));
        analyzeButton.setBackground(new Color(40, 167, 69));
        analyzeButton.setForeground(Color.WHITE);
        analyzeButton.setFocusPainted(false);
        analyzeButton.setBorderPainted(false);
        analyzeButton.setOpaque(true);
        analyzeButton.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));

        // speedSlider.setPreferredSize(new Dimension(250, 50));
    }

    private void setupInteractions() {
        // Exibir ou ocultar painel de passo-a-passo dependendo da estrutura
        structureComboBox.addItemListener(e -> {
            if (getSelectedStructureIndex() == 0) { // Vetor
                stepPanel.setVisible(false);
                controlPanel.setVisible(false);
                stepByStepCheckBox.setSelected(false);
            } else {
                stepPanel.setVisible(true);
                controlPanel.setVisible(stepByStepCheckBox.isSelected());
            }
            revalidate();
            repaint();
        });

        stepByStepCheckBox.addItemListener(e -> {
            boolean on = e.getStateChange() == ItemEvent.SELECTED;
            // speedSlider.setEnabled(on);
            controlPanel.setVisible(on);
        });
    }

    // ===== Métodos públicos =====

    public void setAnalyzeButtonListener(ActionListener listener) {
        for (ActionListener l : analyzeButton.getActionListeners())
            analyzeButton.removeActionListener(l);
        analyzeButton.addActionListener(listener);
    }

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

    public void enableControlButtons(boolean enable) {
        playButton.setEnabled(enable);
        pauseButton.setEnabled(enable);
        nextButton.setEnabled(enable);
        stopButton.setEnabled(enable);
    }

    public void setAnalyzeButtonEnabled(boolean enabled) {
        analyzeButton.setEnabled(enabled);
        if (enabled) {
            analyzeButton.setBackground(new Color(40, 167, 69));
            analyzeButton.setText("🚀 Analisar Texto");
        } else {
            analyzeButton.setBackground(Color.GRAY);
            analyzeButton.setText("⏳ Selecione um arquivo primeiro");
        }
    }

    public int getSelectedStructureIndex() {
        return structureComboBox.getSelectedIndex();
    }

    public boolean isStepByStepEnabled() {
        return stepByStepCheckBox.isSelected();
    }

    // public int getStepDelayMillis() {
    //     return speedSlider.getValue();
    // }

    public void setConfigurationEnabled(boolean enabled) {
        structureComboBox.setEnabled(enabled);
        stepByStepCheckBox.setEnabled(enabled);
        // speedSlider.setEnabled(enabled && stepByStepCheckBox.isSelected());
        enableControlButtons(false);

        if (!enabled) {
            analyzeButton.setText("⏳ Analisando...");
            analyzeButton.setBackground(Color.ORANGE);
        } else if (analyzeButton.isEnabled()) {
            analyzeButton.setText("🚀 Analisar Texto");
            analyzeButton.setBackground(new Color(40, 167, 69));
        }
    }
}