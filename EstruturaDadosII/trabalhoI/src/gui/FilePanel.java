// src/gui/FilePanel.java

package gui;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * PAINEL DE SELEÇÃO DE ARQUIVO
 *
 * Este painel é responsável por permitir que o usuário
 * selecione um arquivo .txt para análise.
 *
 * Imagine como uma "gaveta" específica da nossa interface
 * que só cuida da seleção de arquivos.
 */
public class FilePanel extends JPanel {

    // ====== COMPONENTES VISUAIS ======
    private JButton selectButton; // Botão "Selecionar Arquivo"
    private JLabel fileNameLabel; // Label que mostra o nome do arquivo
    private JLabel fileSizeLabel; // Label que mostra o tamanho do arquivo

    // ====== DADOS ======
    private File selectedFile; // Arquivo atualmente selecionado
    private FileSelectionListener listener; // "Ouvinte" para avisar quando arquivo for selecionado

    /**
     * CONSTRUTOR
     * Monta o painel de seleção de arquivo
     */
    public FilePanel() {
        createComponents(); // Cria os componentes visuais
        layoutComponents(); // Organiza o layout
        setupEventHandlers(); // Configura os eventos
    }

    /**
     * CRIAR COMPONENTES
     * Aqui "fabricamos" cada botão, label, etc.
     */
    private void createComponents() {
        // Botão para selecionar arquivo
        selectButton = new JButton("📁 Selecionar Arquivo (.txt)");
        selectButton.setPreferredSize(new Dimension(200, 35));

        // Personalizar o botão
        selectButton.setBackground(new Color(0, 123, 255)); // Azul
        selectButton.setForeground(Color.WHITE); // Texto branco
        selectButton.setFocusPainted(false); // Sem borda de foco
        selectButton.setBorderPainted(false); // Sem borda
        selectButton.setOpaque(true); // Cor de fundo visível

        // Label para mostrar nome do arquivo
        fileNameLabel = new JLabel("Nenhum arquivo selecionado");
        fileNameLabel.setForeground(Color.GRAY);
        fileNameLabel.setFont(new Font(Font.SANS_SERIF, Font.ITALIC, 12));

        // Label para mostrar tamanho do arquivo
        fileSizeLabel = new JLabel("");
        fileSizeLabel.setForeground(Color.GRAY);
        fileSizeLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 11));
    }

    /**
     * ORGANIZAR COMPONENTES (LAYOUT)
     * Decidir onde cada componente fica dentro deste painel
     */
    private void layoutComponents() {
        // Usar FlowLayout - organiza componentes em linha
        setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5));

        // Adicionar uma borda com título
        setBorder(new TitledBorder("📄 Seleção de Arquivo"));

        // Adicionar os componentes na ordem
        add(selectButton);

        // Criar um painel vertical para as informações do arquivo
        JPanel infoPanel = createFileInfoPanel();
        add(infoPanel);
    }

    /**
     * CRIAR PAINEL DE INFORMAÇÕES DO ARQUIVO
     * Organiza nome e tamanho do arquivo verticalmente
     */
    private JPanel createFileInfoPanel() {
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS)); // Layout vertical

        infoPanel.add(fileNameLabel);
        infoPanel.add(fileSizeLabel);

        return infoPanel;
    }

    /**
     * CONFIGURAR EVENTOS
     * Definir o que acontece quando o usuário clica no botão
     */
    private void setupEventHandlers() {
        selectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Quando o botão é clicado, abrir seletor de arquivo
                openFileChooser();
            }
        });
    }

    /**
     * ABRIR SELETOR DE ARQUIVO
     * Mostra a janela padrão do sistema para selecionar arquivos
     */
    private void openFileChooser() {
        // Criar o seletor de arquivos
        JFileChooser fileChooser = new JFileChooser();

        // Configurar o seletor
        fileChooser.setDialogTitle("Selecione um arquivo de texto");
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home"))); // Começar na pasta do usuário

        // Filtro para mostrar apenas arquivos .txt
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "Arquivos de Texto (*.txt)", "txt");
        fileChooser.setFileFilter(filter);
        fileChooser.setAcceptAllFileFilterUsed(false); // Não permitir "todos os tipos"

        // Mostrar o diálogo e capturar a resposta
        int result = fileChooser.showOpenDialog(this);

        // Verificar se o usuário selecionou um arquivo
        if (result == JFileChooser.APPROVE_OPTION) {
            // Arquivo foi selecionado!
            File file = fileChooser.getSelectedFile();
            setSelectedFile(file);

            // Avisar o "ouvinte" (nossa classe principal)
            if (listener != null) {
                listener.onFileSelected(file);
            }
        } else {
            // Usuário cancelou
            if (listener != null) {
                listener.onFileSelectionCancelled();
            }
        }
    }

    /**
     * DEFINIR ARQUIVO SELECIONADO
     * Atualizar as informações mostradas na tela
     * 
     * @param file Arquivo selecionado (ou null para limpar seleção)
     */
    private void setSelectedFile(File file) {
        this.selectedFile = file;

        if (file != null) {
            // Atualizar o nome do arquivo
            fileNameLabel.setText("📄 " + file.getName());
            fileNameLabel.setForeground(new Color(40, 167, 69)); // Verde

            // Calcular e mostrar o tamanho
            long sizeInBytes = file.length();
            String sizeText = formatFileSize(sizeInBytes);
            fileSizeLabel.setText("📏 Tamanho: " + sizeText);
            fileSizeLabel.setForeground(Color.DARK_GRAY);
        } else {
            // Resetar para estado inicial
            fileNameLabel.setText("Nenhum arquivo selecionado");
            fileNameLabel.setForeground(Color.GRAY);
            fileSizeLabel.setText("");
        }

        // Redesenhar o painel
        repaint();
    }

    /**
     * FORMATAR TAMANHO DO ARQUIVO
     * Converter bytes para formato legível (KB, MB, etc.)
     * 
     * @param bytes Tamanho do arquivo em bytes
     * @return String formatada com unidade apropriada
     */
    private String formatFileSize(long bytes) {
        if (bytes < 1024) {
            return bytes + " bytes";
        } else if (bytes < 1024 * 1024) {
            return String.format("%.1f KB", bytes / 1024.0);
        } else {
            return String.format("%.1f MB", bytes / (1024.0 * 1024.0));
        }
    }

    /**
     * DEFINIR "OUVINTE" PARA SELEÇÃO DE ARQUIVO
     * Permite que outras classes sejam avisadas quando um arquivo é selecionado
     * 
     * @param listener Objeto que implementa FileSelectionListener
     */
    public void setFileSelectionListener(FileSelectionListener listener) {
        this.listener = listener;
    }

    /**
     * OBTER ARQUIVO SELECIONADO
     * Permite que outras classes consultem qual arquivo está selecionado
     * 
     * @return Arquivo selecionado ou null se nenhum estiver selecionado
     */
    public File getSelectedFile() {
        return selectedFile;
    }
}

/**
 * INTERFACE PARA "OUVIR" SELEÇÃO DE ARQUIVOS
 *
 * Esta interface define um "contrato" - quem quiser ser avisado
 * quando um arquivo for selecionado deve implementar estes métodos.
 *
 * É como um "telefone" entre o FilePanel e outras classes.
 */
interface FileSelectionListener {
    /**
     * Chamado quando um arquivo é selecionado com sucesso
     * 
     * @param file Arquivo selecionado
     */
    void onFileSelected(File file);

    /**
     * Chamado quando o usuário cancela a seleção
     */
    void onFileSelectionCancelled();
}