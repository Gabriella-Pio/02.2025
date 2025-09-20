// src/gui/ConfigPanel.java

package gui;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionListener;

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
    private JComboBox<String> structureComboBox;  // Lista suspensa para escolher estrutura
    private JButton analyzeButton;                // Botão para iniciar análise
    private JLabel instructionLabel;              // Label com instruções

    /**
     * CONSTRUTOR
     * Monta o painel de configurações
     */
    public ConfigPanel() {
        createComponents();
        layoutComponents();
        customizeComponents();
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
    }

    /**
     * ORGANIZAR COMPONENTES (LAYOUT)
     * Decidir onde cada componente fica dentro deste painel
     */
    private void layoutComponents() {
        // Usar FlowLayout para organizar em linha
        setLayout(new FlowLayout(FlowLayout.LEFT, 15, 10));

        // Adicionar borda com título
        setBorder(new TitledBorder("⚙️ Configurações de Análise"));

        // Adicionar componentes na ordem
        add(instructionLabel);
        add(structureComboBox);
        add(analyzeButton);
    }

    /**
     * PERSONALIZAR COMPONENTES
     * Ajustar cores, fontes, tamanhos, etc.
     */
    private void customizeComponents() {
        // Personalizar ComboBox
        structureComboBox.setPreferredSize(new Dimension(280, 30));
        structureComboBox.setBackground(Color.WHITE);
        structureComboBox.setForeground(Color.BLACK);

        // Personalizar botão de análise
        analyzeButton.setPreferredSize(new Dimension(150, 35));
        analyzeButton.setBackground(new Color(40, 167, 69)); // Verde
        analyzeButton.setForeground(Color.WHITE);
        analyzeButton.setFocusPainted(false);
        analyzeButton.setBorderPainted(false);
        analyzeButton.setOpaque(true);
        analyzeButton.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));

        // Adicionar efeitos de hover (passar mouse sobre o botão)
        addHoverEffects();
    }

    /**
     * ADICIONAR EFEITOS DE HOVER
     * Fazer o botão mudar de cor quando o mouse passar sobre ele
     */
    private void addHoverEffects() {
        Color originalColor = analyzeButton.getBackground();
        Color hoverColor = new Color(34, 139, 59); // Verde mais escuro

        analyzeButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                // Mouse entrou no botão
                if (analyzeButton.isEnabled()) {
                    analyzeButton.setBackground(hoverColor);
                }
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                // Mouse saiu do botão
                if (analyzeButton.isEnabled()) {
                    analyzeButton.setBackground(originalColor);
                }
            }
        });
    }

    /**
     * DEFINIR "OUVINTE" PARA O BOTÃO DE ANÁLISE
     * Permite que outras classes sejam avisadas quando o botão for clicado
     */
    public void setAnalyzeButtonListener(ActionListener listener) {
        // Remove listeners antigos (se existirem)
        ActionListener[] listeners = analyzeButton.getActionListeners();
        for (ActionListener l : listeners) {
            analyzeButton.removeActionListener(l);
        }

        // Adiciona o novo listener
        analyzeButton.addActionListener(listener);
    }

    /**
     * HABILITAR/DESABILITAR BOTÃO DE ANÁLISE
     * Controlar quando o usuário pode ou não clicar no botão
     */
    public void setAnalyzeButtonEnabled(boolean enabled) {
        analyzeButton.setEnabled(enabled);

        // Ajustar aparência baseada no estado
        if (enabled) {
            analyzeButton.setBackground(new Color(40, 167, 69)); // Verde
            analyzeButton.setText("🚀 Analisar Texto");
        } else {
            analyzeButton.setBackground(Color.GRAY);
            analyzeButton.setText("⏳ Selecione um arquivo primeiro");
        }
    }

    /**
     * OBTER ESTRUTURA SELECIONADA
     * Retorna qual estrutura o usuário escolheu (0=Busca, 1=BST, 2=AVL)
     */
    public int getSelectedStructureIndex() {
        return structureComboBox.getSelectedIndex();
    }

    /**
     * OBTER NOME DA ESTRUTURA SELECIONADA
     * Retorna o texto completo da estrutura selecionada
     */
    public String getSelectedStructureName() {
        return (String) structureComboBox.getSelectedItem();
    }

    /**
     * DEFINIR ESTRUTURA SELECIONADA
     * Permite programaticamente escolher uma estrutura
     */
    public void setSelectedStructure(int index) {
        if (index >= 0 && index < structureComboBox.getItemCount()) {
            structureComboBox.setSelectedIndex(index);
        }
    }

    /**
     * BLOQUEAR/DESBLOQUEAR CONFIGURAÇÕES DURANTE ANÁLISE
     * Impedir que o usuário mude configurações enquanto está analisando
     */
    public void setConfigurationEnabled(boolean enabled) {
        structureComboBox.setEnabled(enabled);

        if (!enabled) {
            analyzeButton.setText("⏳ Analisando...");
            analyzeButton.setBackground(Color.ORANGE);
        } else if (analyzeButton.isEnabled()) {
            analyzeButton.setText("🚀 Analisar Texto");
            analyzeButton.setBackground(new Color(40, 167, 69));
        }
    }
}