package gui;

import javax.swing.*;

import arvore.AVLTree;
import arvore.BSTree;
import arvore.NodeInfo;
import arvore.TreeStats;
import tokenizer.TextTokenizer;
import vetor.DynamicWordFrequencyVector;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * CLASSE PRINCIPAL DA INTERFACE GRÁFICA
 *
 * Esta é a "janela mãe" que coordena todos os componentes.
 * Pense nela como o "maestro" de uma orquestra - ela organiza
 * e conecta todos os outros componentes da interface.
 */
public class TextAnalyzerGUI extends JFrame {

    // ====== COMPONENTES DA INTERFACE ======
    // Estes são os "painéis" que compõem nossa janela
    private FilePanel filePanel; // Painel para seleção de arquivo
    private ConfigPanel configPanel; // Painel de configurações
    private ResultsPanel resultsPanel; // Painel para mostrar resultados
    private JProgressBar progressBar; // Barra de progresso

    // ====== DADOS DA APLICAÇÃO ======
    private File selectedFile; // Arquivo selecionado pelo usuário

    /**
     * CONSTRUTOR - É executado quando criamos a janela
     * Aqui "montamos" nossa interface peça por peça
     */
    public TextAnalyzerGUI() {
        // Primeiro: configurar a janela principal
        setupWindow();

        // Segundo: criar todos os componentes
        createComponents();

        // Terceiro: organizar os componentes na tela
        layoutComponents();

        // Quarto: conectar os eventos (cliques, etc.)
        setupEventHandlers();
    }

    /**
     * CONFIGURAÇÃO DA JANELA PRINCIPAL
     * Aqui definimos título, tamanho, comportamento ao fechar, etc.
     */
    private void setupWindow() {
        // Título que aparece na barra superior
        setTitle("📊 Analisador de Texto - Árvores Binárias");

        // O que acontece quando o usuário clica no X
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Tamanho da janela (largura x altura)
        setSize(900, 700);

        // Centralizar na tela
        setLocationRelativeTo(null);

        // Permitir redimensionamento
        setResizable(true);

        // Definir tamanho mínimo
        setMinimumSize(new Dimension(700, 500));
    }

    /**
     * CRIAÇÃO DOS COMPONENTES
     * Aqui "fabricamos" cada peça da nossa interface
     */
    private void createComponents() {
        // Criar o painel de seleção de arquivo
        filePanel = new FilePanel();

        // Criar o painel de configurações
        configPanel = new ConfigPanel();

        // Criar o painel de resultados
        resultsPanel = new ResultsPanel();

        // Criar a barra de progresso
        progressBar = new JProgressBar();
        progressBar.setStringPainted(true); // Mostrar texto na barra
        progressBar.setVisible(false); // Inicialmente invisível
    }

    /**
     * ORGANIZAÇÃO DOS COMPONENTES (LAYOUT)
     * Aqui decidimos ONDE cada componente fica na tela
     *
     * Usamos BorderLayout - imagine a tela dividida em 5 regiões:
     * NORTE (NORTH) - parte superior
     * SUL (SOUTH) - parte inferior
     * LESTE (EAST) - lado direito
     * OESTE (WEST) - lado esquerdo
     * CENTRO (CENTER) - meio (pega o espaço restante)
     */
    private void layoutComponents() {
        // Usar BorderLayout para organizar
        setLayout(new BorderLayout(10, 10)); // 10px de espaço entre componentes

        // Painel superior - combina seleção de arquivo + configurações
        JPanel topPanel = createTopPanel();
        add(topPanel, BorderLayout.NORTH);

        // Centro - resultados (pega a maior parte do espaço)
        add(resultsPanel, BorderLayout.CENTER);

        // Parte inferior - barra de progresso
        add(progressBar, BorderLayout.SOUTH);

        // Adicionar uma "moldura" interna de 15px
        ((JComponent) getContentPane()).setBorder(
                BorderFactory.createEmptyBorder(15, 15, 15, 15));
    }

    /**
     * CRIAR PAINEL SUPERIOR
     * Combina o painel de arquivo + painel de configurações
     * em um só painel vertical
     */
    private JPanel createTopPanel() {
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout(5, 5));

        // Arquivo vai no topo
        topPanel.add(filePanel, BorderLayout.NORTH);

        // Configurações vão embaixo
        topPanel.add(configPanel, BorderLayout.CENTER);

        return topPanel;
    }

    /**
     * CONFIGURAÇÃO DOS EVENTOS
     * Aqui "ensinamos" o que fazer quando o usuário interage
     * com a interface (clica em botões, etc.)
     */
    private void setupEventHandlers() {

        // ====== EVENTO: Arquivo selecionado ======
        filePanel.setFileSelectionListener(new FileSelectionListener() {
            @Override
            public void onFileSelected(File file) {
                try {
                    TextTokenizer tokenizer = new TextTokenizer("src/resources/stopwords.txt");
                    tokenizer.loadTextFile(file.getAbsolutePath());

                    // String[] palavras = tokenizer.tokenizeToArray(tokenizer.TEXT);

                    // // Aqui você pode chamar os algoritmos
                    // DynamicWordFrequencyVector vetor = new DynamicWordFrequencyVector();
                    // TreeStats statsVetor = vetor.buildWithStats(palavras);

                    // E atualizar a ResultsPanel, por exemplo
                    // resultsPanel.showResults(vetor, statsVetor);

                    // Quando um arquivo é selecionado...
                    selectedFile = file;

                    // Habilitar o botão de análise
                    configPanel.setAnalyzeButtonEnabled(true);

                    // Mostrar mensagem de sucesso
                    showMessage("✅ Arquivo selecionado: " + file.getName());
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null,
                            "Erro ao processar arquivo: " + e.getMessage(),
                            "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }

            @Override
            public void onFileSelectionCancelled() {
                // Se o usuário cancelou a seleção...
                showMessage("❌ Seleção de arquivo cancelada");
            }
        });

        // ====== EVENTO: Botão de análise clicado ======
        configPanel.setAnalyzeButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Quando o botão "Analisar" é clicado...
                startAnalysis();
            }
        });
    }

    /**
     * INICIAR ANÁLISE
     * Este método seria chamado quando o usuário quer analisar o arquivo
     */
    private void startAnalysis() {
        if (selectedFile == null) {
            showError("❌ Por favor, selecione um arquivo primeiro!");
            return;
        }

        // Mostrar barra de progresso
        progressBar.setVisible(true);
        progressBar.setIndeterminate(true); // Animação contínua
        progressBar.setString("🔍 Analisando arquivo...");

        // Desabilitar botão durante análise
        configPanel.setAnalyzeButtonEnabled(false);

        // Limpar resultados anteriores
        resultsPanel.clearResults();

        // Aqui você conectaria com seu backend
        // Por enquanto, vamos simular uma análise
        // simulateAnalysis();

        SwingWorker<Void, String> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() {
                try {
                    // 1. Tokenizar texto
                    TextTokenizer tokenizer = new TextTokenizer("src/resources/stopwords.txt");
                    tokenizer.loadTextFile(selectedFile.getAbsolutePath());
                    String[] palavras = tokenizer.tokenizeToArray(tokenizer.TEXT);

                    int escolha = configPanel.getSelectedStructureIndex();

                    // 2. Executar estrutura escolhida
                    if (escolha == 0) { // Vetor dinâmico
                        // publish("Executando Busca Binária (Vetor Dinâmico)...");
                        DynamicWordFrequencyVector vetor = new DynamicWordFrequencyVector();
                        TreeStats stats = vetor.buildWithStats(palavras);
                        resultsPanel.addHeader("Resultados - Vetor Dinâmico");
                        vetor.displayWordFrequencies(); // no console
                        resultsPanel.showWordFrequencies(vetor.getFrequenciesAsList().stream()
                                .map(line -> String.format("%-20s %5s", line.split(" -> ")[0], line.split(" -> ")[1]))
                                .toList());
                        resultsPanel.addSeparator();
                        resultsPanel.addResult(stats.toString());

                    } else if (escolha == 1) { // BST
                        // publish("Executando Árvore BST...");
                        BSTree bst = new BSTree();
                        TreeStats stats = bst.buildWithStats(palavras);
                        resultsPanel.addHeader("Resultados - BST");
                        bst.inOrderTraversal(); // no console
                        resultsPanel.showWordFrequencies(bst.getFrequenciesAsList().stream()
                                .map(line -> String.format("%-20s %5s", line.split(" -> ")[0], line.split(" -> ")[1]))
                                .toList());
                        resultsPanel.addSeparator();
                        resultsPanel.addResult(stats.toString());
                        resultsPanel.addSeparator();
                        resultsPanel.showTree(bst.getNodesWithLevel());

                    } else if (escolha == 2) { // AVL
                        // publish("Executando Árvore AVL...");
                        AVLTree avl = new AVLTree();
                        TreeStats stats = avl.buildWithStats(palavras);
                        resultsPanel.addHeader("Resultados - AVL");
                        avl.inOrderTraversal(); // no console
                        resultsPanel.showWordFrequencies(avl.getFrequenciesAsList().stream()
                                .map(line -> String.format("%-20s %5s", line.split(" -> ")[0], line.split(" -> ")[1]))
                                .toList());
                        resultsPanel.addSeparator();
                        resultsPanel.addResult(stats.toString());
                        resultsPanel.addSeparator();
                        resultsPanel.showTree(avl.getNodesWithLevel());
                    }

                    publish("✅ Análise concluída!");
                } catch (Exception e) {
                    publish("❌ Erro durante análise: " + e.getMessage());
                }
                return null;
            }

            @Override
            protected void process(java.util.List<String> chunks) {
                for (String message : chunks) {
                    resultsPanel.addResult(message);
                }
            }

            @Override
            protected void done() {
                progressBar.setVisible(false);
                configPanel.setAnalyzeButtonEnabled(true);
            }
        };

        worker.execute();
    }

    /**
     * SIMULAÇÃO DE ANÁLISE
     * Simula o processamento para demonstrar a interface
     */
    private void simulateAnalysis() {
        // Criar uma tarefa em background para não travar a interface
        SwingWorker<Void, String> worker = new SwingWorker<Void, String>() {
            @Override
            protected Void doInBackground() throws Exception {
                // Simular processamento...
                publish("🚀 Iniciando análise do arquivo: " + selectedFile.getName());
                Thread.sleep(1000); // Simular 1 segundo de processamento

                publish("📊 Executando Busca Binária...");
                Thread.sleep(800);

                publish("🌳 Executando BST...");
                Thread.sleep(900);

                publish("⚖️ Executando AVL...");
                Thread.sleep(700);

                publish("✅ Análise concluída!");

                return null;
            }

            @Override
            protected void process(java.util.List<String> chunks) {
                // Atualizar a interface com mensagens
                for (String message : chunks) {
                    resultsPanel.addResult(message);
                }
            }

            @Override
            protected void done() {
                // Quando terminar...
                progressBar.setVisible(false);
                configPanel.setAnalyzeButtonEnabled(true);
                showMessage("🎉 Análise concluída com sucesso!");
            }
        };

        worker.execute();
    }

    /**
     * MÉTODOS AUXILIARES PARA MOSTRAR MENSAGENS
     */
    private void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Informação",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Erro",
                JOptionPane.ERROR_MESSAGE);
    }

    /**
     * MÉTODO MAIN - PONTO DE ENTRADA DO PROGRAMA
     * É aqui que tudo começa!
     */
    public static void main(String[] args) {
        // SwingUtilities.invokeLater garante que a interface
        // seja criada na thread correta (EDT - Event Dispatch Thread)
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    // Usar a aparência do sistema operacional
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // Criar e mostrar a janela
                new TextAnalyzerGUI().setVisible(true);
            }
        });
    }
}