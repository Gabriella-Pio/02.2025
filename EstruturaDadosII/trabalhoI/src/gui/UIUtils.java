// src/gui/UIUtils.java

package gui;
import javax.swing.*;
import java.awt.*;

/**
 * UTILITÁRIOS DE INTERFACE (UIUtils)
 *
 * Esta classe contém métodos auxiliares reutilizáveis para
 * melhorar a aparência e funcionalidade da interface.
 *
 * É como uma "caixa de ferramentas" com funções úteis
 * que podem ser usadas em qualquer parte da aplicação.
 */
public class UIUtils {

    // ====== CORES PADRÃO DA APLICAÇÃO ======
    public static final Color PRIMARY_COLOR = new Color(0, 123, 255);     // Azul principal
    public static final Color SUCCESS_COLOR = new Color(40, 167, 69);     // Verde sucesso
    public static final Color DANGER_COLOR = new Color(220, 53, 69);      // Vermelho erro
    public static final Color WARNING_COLOR = new Color(255, 193, 7);     // Amarelo aviso
    public static final Color LIGHT_GRAY = new Color(248, 249, 250);      // Cinza claro
    public static final Color DARK_GRAY = new Color(108, 117, 125);       // Cinza escuro

    // ====== FONTES PADRÃO ======
    public static final Font TITLE_FONT = new Font(Font.SANS_SERIF, Font.BOLD, 16);
    public static final Font NORMAL_FONT = new Font(Font.SANS_SERIF, Font.PLAIN, 12);
    public static final Font SMALL_FONT = new Font(Font.SANS_SERIF, Font.PLAIN, 10);
    public static final Font MONO_FONT = new Font(Font.MONOSPACED, Font.PLAIN, 12);

    /**
     * CONFIGURAR LOOK AND FEEL DO SISTEMA
     * Faz a aplicação usar a aparência nativa do sistema operacional
     */
    public static void setupSystemLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

            // Configurações globais adicionais
            UIManager.put("Button.focus", new Color(0, 0, 0, 0)); // Remove foco visual dos botões
            UIManager.put("ScrollPane.border", BorderFactory.createEmptyBorder()); // Remove borda dos scroll panes

        } catch (Exception e) {
            System.err.println("Não foi possível definir Look and Feel do sistema: " + e.getMessage());
            // Continua com o Look and Feel padrão
        }
    }

    /**
     * CRIAR BOTÃO ESTILIZADO
     * Cria um botão com aparência moderna e consistente
     */
    public static JButton createStyledButton(String text, Color backgroundColor, Color textColor) {
        JButton button = new JButton(text);

        // Configurar aparência
        button.setBackground(backgroundColor);
        button.setForeground(textColor);
        button.setFocusPainted(false);      // Remove borda de foco
        button.setBorderPainted(false);     // Remove borda padrão
        button.setOpaque(true);             // Permitir cor de fundo
        button.setFont(NORMAL_FONT);

        // Configurar tamanho
        button.setPreferredSize(new Dimension(120, 35));

        // Adicionar efeito hover
        addHoverEffect(button, backgroundColor);

        return button;
    }

    /**
     * CRIAR BOTÃO PRIMÁRIO (AZUL)
     * Atalho para criar botão com cor principal
     */
    public static JButton createPrimaryButton(String text) {
        return createStyledButton(text, PRIMARY_COLOR, Color.WHITE);
    }

    /**
     * CRIAR BOTÃO DE SUCESSO (VERDE)
     * Atalho para criar botão verde
     */
    public static JButton createSuccessButton(String text) {
        return createStyledButton(text, SUCCESS_COLOR, Color.WHITE);
    }

    /**
     * CRIAR BOTÃO DE PERIGO (VERMELHO)
     * Atalho para criar botão vermelho
     */
    public static JButton createDangerButton(String text) {
        return createStyledButton(text, DANGER_COLOR, Color.WHITE);
    }

    /**
     * ADICIONAR EFEITO HOVER A UM BOTÃO
     * Faz o botão escurecer quando o mouse passa sobre ele
     */
    public static void addHoverEffect(JButton button, Color originalColor) {
        // Calcular cor mais escura (reduzir brilho em 20%)
        Color hoverColor = darkenColor(originalColor, 0.8f);

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (button.isEnabled()) {
                    button.setBackground(hoverColor);
                }
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (button.isEnabled()) {
                    button.setBackground(originalColor);
                }
            }
        });
    }

    /**
     * ESCURECER UMA COR
     * Reduz o brilho de uma cor por um fator
     */
    public static Color darkenColor(Color color, float factor) {
        return new Color(
                Math.max(0, (int)(color.getRed() * factor)),
                Math.max(0, (int)(color.getGreen() * factor)),
                Math.max(0, (int)(color.getBlue() * factor))
        );
    }

    /**
     * CLAREAR UMA COR
     * Aumenta o brilho de uma cor
     */
    public static Color lightenColor(Color color, float factor) {
        return new Color(
                Math.min(255, (int)(color.getRed() * factor)),
                Math.min(255, (int)(color.getGreen() * factor)),
                Math.min(255, (int)(color.getBlue() * factor))
        );
    }

    /**
     * CRIAR LABEL ESTILIZADO
     * Cria um JLabel com aparência consistente
     */
    public static JLabel createStyledLabel(String text, Font font, Color color) {
        JLabel label = new JLabel(text);
        label.setFont(font);
        label.setForeground(color);
        return label;
    }

    /**
     * CRIAR TÍTULO
     * Cria um label para títulos de seções
     */
    public static JLabel createTitleLabel(String text) {
        return createStyledLabel(text, TITLE_FONT, Color.BLACK);
    }

    /**
     * CRIAR LABEL DE STATUS
     * Cria um label para mensagens de status
     */
    public static JLabel createStatusLabel(String text) {
        return createStyledLabel(text, SMALL_FONT, DARK_GRAY);
    }

    /**
     * CRIAR PAINEL COM BORDA
     * Cria um JPanel com borda e título
     */
    public static JPanel createTitledPanel(String title, LayoutManager layout) {
        JPanel panel = new JPanel(layout);
        panel.setBorder(BorderFactory.createTitledBorder(title));
        return panel;
    }

    /**
     * CRIAR PAINEL COM PADDING
     * Cria um JPanel com espaçamento interno
     */
    public static JPanel createPaddedPanel(LayoutManager layout, int padding) {
        JPanel panel = new JPanel(layout);
        panel.setBorder(BorderFactory.createEmptyBorder(padding, padding, padding, padding));
        return panel;
    }

    /**
     * MOSTRAR DIÁLOGO DE CONFIRMAÇÃO
     * Exibe uma janela perguntando "Tem certeza?"
     */
    public static boolean showConfirmDialog(Component parent, String message, String title) {
        int option = JOptionPane.showConfirmDialog(
                parent,
                message,
                title,
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        return option == JOptionPane.YES_OPTION;
    }

    /**
     * MOSTRAR MENSAGEM DE INFORMAÇÃO
     * Exibe uma janela com informação para o usuário
     */
    public static void showInfoMessage(Component parent, String message, String title) {
        JOptionPane.showMessageDialog(parent, message, title, JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * MOSTRAR MENSAGEM DE ERRO
     * Exibe uma janela com erro para o usuário
     */
    public static void showErrorMessage(Component parent, String message, String title) {
        JOptionPane.showMessageDialog(parent, message, title, JOptionPane.ERROR_MESSAGE);
    }

    /**
     * MOSTRAR MENSAGEM DE SUCESSO
     * Exibe uma janela com mensagem de sucesso
     */
    public static void showSuccessMessage(Component parent, String message) {
        showInfoMessage(parent, message, "✅ Sucesso");
    }

    /**
     * CENTRALIZAR JANELA NA TELA
     * Posiciona uma janela no centro da tela
     */
    public static void centerWindow(Window window) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension windowSize = window.getSize();

        int x = (screenSize.width - windowSize.width) / 2;
        int y = (screenSize.height - windowSize.height) / 2;

        window.setLocation(x, y);
    }

    /**
     * FORMATAR TAMANHO DE ARQUIVO
     * Converte bytes para formato legível (KB, MB, GB)
     */
    public static String formatFileSize(long bytes) {
        if (bytes < 1024) {
            return bytes + " bytes";
        } else if (bytes < 1024 * 1024) {
            return String.format("%.1f KB", bytes / 1024.0);
        } else if (bytes < 1024 * 1024 * 1024) {
            return String.format("%.1f MB", bytes / (1024.0 * 1024.0));
        } else {
            return String.format("%.1f GB", bytes / (1024.0 * 1024.0 * 1024.0));
        }
    }

    /**
     * FORMATAR TEMPO DE EXECUÇÃO
     * Converte milissegundos para formato legível
     */
    public static String formatExecutionTime(long milliseconds) {
        if (milliseconds < 1000) {
            return milliseconds + " ms";
        } else if (milliseconds < 60000) {
            return String.format("%.1f s", milliseconds / 1000.0);
        } else {
            long minutes = milliseconds / 60000;
            long seconds = (milliseconds % 60000) / 1000;
            return String.format("%d min %d s", minutes, seconds);
        }
    }

    /**
     * CRIAR SEPARADOR VISUAL
     * Cria uma linha horizontal para separar seções
     */
    public static JSeparator createSeparator() {
        JSeparator separator = new JSeparator(JSeparator.HORIZONTAL);
        separator.setPreferredSize(new Dimension(0, 1));
        return separator;
    }

    /**
     * ANIMAR COMPONENTE (FADE IN/OUT)
     * Faz um componente aparecer/desaparecer gradualmente
     * (Implementação simplificada)
     */
    public static void fadeComponent(JComponent component, boolean fadeIn, int duration) {
        Timer timer = new Timer(50, null);
        float[] opacity = {fadeIn ? 0.0f : 1.0f};
        float step = 1.0f / (duration / 50);

        timer.addActionListener(e -> {
            if (fadeIn) {
                opacity[0] += step;
                if (opacity[0] >= 1.0f) {
                    opacity[0] = 1.0f;
                    timer.stop();
                }
            } else {
                opacity[0] -= step;
                if (opacity[0] <= 0.0f) {
                    opacity[0] = 0.0f;
                    timer.stop();
                    component.setVisible(false);
                }
            }

            // Aplicar transparência (simplificado)
            component.setVisible(opacity[0] > 0.1f);
            component.repaint();
        });

        if (fadeIn) component.setVisible(true);
        timer.start();
    }
}