// src/gui/TextAnalyzerGUI.java

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
import java.util.Arrays;
import java.util.List;

/**
 * CLASSE PRINCIPAL DA INTERFACE GRÁFICA
 *
 * Esta é a "janela mãe" que coordena todos os componentes.
 * Pense nela como o "maestro" de uma orquestra - ela organiza
 * e conecta todos os outros componentes da interface.
 */
public class TextAnalyzerGUI extends JFrame {

    // ====== COMPONENTES ======
    private FilePanel filePanel;
    private ConfigPanel configPanel;
    private ResultsPanel resultsPanel;
    private JProgressBar progressBar;

    // ====== DADOS ======
    private File selectedFile;

    // ====== CONTROLE PASSO-A-PASSO ======
    private Timer stepTimer;
    private int currentStep = 0;
    private String[] palavrasStep;
    private int totalSteps = 0;
    private int delayMs = 500;
    private int estruturaSelecionada = -1;

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
        setSize(1440, 900);

        // Centralizar na tela
        setLocationRelativeTo(null);

        // Permitir redimensionamento
        setResizable(true);

        // Definir tamanho mínimo
        setMinimumSize(new Dimension(1200, 800));
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

        // Criar la barra de progresso
        progressBar = new JProgressBar();
        progressBar.setStringPainted(true); // Mostrar texto na barra
        progressBar.setVisible(false); // Inicialmente invisível
    }

    /**
     * ORGANIZAÇÃO DOS COMPONENTES (LAYOUT)
     * Aqui decidimos ONDE cada componente fica na tela
     */
    private void layoutComponents() {
        // Usar BorderLayout para organizar
        setLayout(new BorderLayout(10, 10)); // 10px de espaço entre componentes
        JPanel topPanel = new JPanel(new BorderLayout(5, 5));
        topPanel.add(filePanel, BorderLayout.NORTH);
        topPanel.add(configPanel, BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);
        add(resultsPanel, BorderLayout.CENTER);
        add(progressBar, BorderLayout.SOUTH);

        // Adicionar uma "moldura" interna de 15px
        ((JComponent) getContentPane()).setBorder(
                BorderFactory.createEmptyBorder(15, 15, 15, 15));
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
                    selectedFile = file;
                    configPanel.setAnalyzeButtonEnabled(true);
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
        configPanel.setAnalyzeButtonListener(e -> startAnalysis());

        // Play / Pause / Next / Stop
        configPanel.setPlayListener(e -> {
            if (stepTimer != null)
                stepTimer.start();
        });
        configPanel.setPauseListener(e -> {
            if (stepTimer != null)
                stepTimer.stop();
        });
        configPanel.setNextListener(e -> {
            if (stepTimer != null)
                stepTimer.stop();
            runOneStep();
        });
        configPanel.setStopListener(e -> stopStepByStep());
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
        configPanel.setAnalyzeButtonEnabled(false);
        resultsPanel.clearResults();

        // Executar análise em thread separada para não travar a interface
        new SwingWorker<Void, String>() {
            @Override
            protected Void doInBackground() {
                try {
                    TextTokenizer tokenizer = new TextTokenizer("src/resources/stopwords.txt");
                    tokenizer.loadTextFile(selectedFile.getAbsolutePath());
                    String[] palavras = tokenizer.tokenizeToArray(tokenizer.TEXT);

                    estruturaSelecionada = configPanel.getSelectedStructureIndex();
                    boolean passoAPasso = configPanel.isStepByStepEnabled();
                    delayMs = 400; // fixo em 400 ms

                    if (!passoAPasso || estruturaSelecionada == 0) {
                        executarNormal(palavras, estruturaSelecionada);
                    } else {
                        executarPassoAPasso(palavras);
                    }
                } catch (Exception e) {
                    publish("❌ Erro: " + e.getMessage());
                }
                return null;
            }

            @Override
            protected void process(java.util.List<String> chunks) {
                for (String msg : chunks)
                    resultsPanel.addResult(msg);
            }

            @Override
            protected void done() {
                progressBar.setVisible(false);
                configPanel.setAnalyzeButtonEnabled(true);
            }
        }.execute();
    }

    /**
     * EXECUTAR ANÁLISE NORMAL (SEM PASSO-A-PASSO)
     */
    private void executarNormal(String[] palavras, int escolha) {
        if (escolha == 0) {
            // Vetor Dinâmico
            DynamicWordFrequencyVector vetor = new DynamicWordFrequencyVector();
            TreeStats stats = vetor.buildWithStats(palavras);
            SwingUtilities.invokeLater(() -> {
                resultsPanel.addHeader("Resultados - Vetor Dinâmico");
                resultsPanel.showWordFrequencies(vetor.getFrequenciesAsList());
                resultsPanel.showAnalysis(stats, "Vetor Dinâmico");
            });
        } else if (escolha == 1) {
            // BST
            BSTree bst = new BSTree();
            TreeStats stats = bst.buildWithStats(palavras);
            SwingUtilities.invokeLater(() -> {
                resultsPanel.addHeader("Resultados - BST");
                resultsPanel.showWordFrequencies(bst.getFrequenciesAsList());
                resultsPanel.showAnalysis(stats, "BST");
                resultsPanel.showTree(bst.getNodesWithLevel());
            });
        } else if (escolha == 2) {
            // AVL
            AVLTree avl = new AVLTree();
            TreeStats stats = avl.buildWithStats(palavras);
            SwingUtilities.invokeLater(() -> {
                resultsPanel.addHeader("Resultados - AVL");
                resultsPanel.showWordFrequencies(avl.getFrequenciesAsList());
                resultsPanel.showAnalysis(stats, "AVL");
                resultsPanel.showTree(avl.getNodesWithLevel());
            });
        }
    }

    /**
     * EXECUTAR ANÁLISE PASSO-A-PASSO
     */
    private void executarPassoAPasso(String[] palavras) {
        palavrasStep = palavras;
        totalSteps = palavras.length;
        currentStep = 0;

        SwingUtilities.invokeLater(() -> {
            progressBar.setIndeterminate(false);
            progressBar.setMinimum(0);
            progressBar.setMaximum(totalSteps);
            progressBar.setValue(0);
            configPanel.enableControlButtons(true);
        });

        stepTimer = new Timer(delayMs, e -> runOneStep());
        stepTimer.start();
    }

    /**
     * EXECUTAR UM PASSO DA ANÁLISE
     */
    private void runOneStep() {
        if (currentStep >= totalSteps) {
            if (stepTimer != null)
                stepTimer.stop();
            mostrarResultadosFinais();
            return;
        }

        currentStep++;
        String[] prefix = Arrays.copyOfRange(palavrasStep, 0, currentStep);

        if (estruturaSelecionada == 1) {
            // BST passo-a-passo
            BSTree bst = new BSTree();
            TreeStats stats = bst.buildWithStats(prefix);
            List<NodeInfo> nodes = bst.getNodesWithLevel();
            String palavra = prefix[currentStep - 1];
            SwingUtilities.invokeLater(() -> {
                resultsPanel.showTree(nodes);
                resultsPanel.addResult("Inserido (" + currentStep + "/" + totalSteps + "): " + palavra);
                progressBar.setValue(currentStep);
                progressBar.setString("Inserindo: " + currentStep + " / " + totalSteps);
            });
        } else if (estruturaSelecionada == 2) {
            // AVL passo-a-passo
            AVLTree avl = new AVLTree();
            TreeStats stats = avl.buildWithStats(prefix);
            List<NodeInfo> nodes = avl.getNodesWithLevel();
            String palavra = prefix[currentStep - 1];
            SwingUtilities.invokeLater(() -> {
                resultsPanel.showTree(nodes);
                resultsPanel.addResult("Inserido (" + currentStep + "/" + totalSteps + "): " + palavra);
                progressBar.setValue(currentStep);
                progressBar.setString("Inserindo: " + currentStep + " / " + totalSteps);
            });
        }
    }

    /**
     * MOSTRAR RESULTADOS FINAIS
     */
    private void mostrarResultadosFinais() {
        if (estruturaSelecionada == 1) {
            BSTree bst = new BSTree();
            TreeStats stats = bst.buildWithStats(palavrasStep);
            SwingUtilities.invokeLater(() -> {
                resultsPanel.addHeader("Resultados - BST (final)");
                resultsPanel.showWordFrequencies(bst.getFrequenciesAsList());
                resultsPanel.showAnalysis(stats, "BST");
            });
        } else if (estruturaSelecionada == 2) {
            AVLTree avl = new AVLTree();
            TreeStats stats = avl.buildWithStats(palavrasStep);
            SwingUtilities.invokeLater(() -> {
                resultsPanel.addHeader("Resultados - AVL (final)");
                resultsPanel.showWordFrequencies(avl.getFrequenciesAsList());
                resultsPanel.showAnalysis(stats, "AVL");
            });
        }
    }

    /**
     * PARAR EXECUÇÃO PASSO-A-PASSO
     */
    private void stopStepByStep() {
        if (stepTimer != null)
            stepTimer.stop();
        currentStep = totalSteps;
        resultsPanel.addResult("⏹ Execução interrompida.");
        configPanel.enableControlButtons(false);
    }

    /**
     * MOSTRAR MENSAGEM DE INFORMAÇÃO
     */
    private void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Informação", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * MOSTRAR MENSAGEM DE ERRO
     */
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Erro", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * MÉTODO PRINCIPAL
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new TextAnalyzerGUI().setVisible(true);
        });
    }
}