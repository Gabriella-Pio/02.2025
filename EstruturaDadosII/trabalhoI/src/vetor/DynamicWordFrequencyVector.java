// src/vetor/DynamicWordFrequencyVector.java

package vetor;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import arvore.TreeStats; // importando a mesma classe de estatísticas

/**
 * Classe que representa uma palavra e sua frequência
 */
class WordFrequency {
    private String word;
    private int frequency;

    public WordFrequency(String word) {
        this.word = word;
        this.frequency = 1;
    }

    public String getWord() {
        return word;
    }

    public int getFrequency() {
        return frequency;
    }

    public void incrementFrequency() {
        frequency++;
    }

    @Override
    public String toString() {
        return word + ": " + frequency;
    }
}

/**
 * Implementação de vetor dinâmico com busca binária para contagem de
 * frequências
 */
public class DynamicWordFrequencyVector {
    private final List<WordFrequency> vector;
    private int comparacoes;
    private int atribuicoes;

    public DynamicWordFrequencyVector() {
        this.vector = new ArrayList<>();
        this.comparacoes = 0;
        this.atribuicoes = 0;
    }

    /**
     * Busca binária para encontrar uma palavra no vetor
     * 
     * @param word Palavra a ser buscada
     * @return Índice da palavra ou -1 se não encontrada
     */
    private int binarySearch(String word) {
        int left = 0;
        int right = vector.size() - 1;

        while (left <= right) {
            int mid = left + (right - left) / 2;
            atribuicoes++; // Atribuição do mid

            int comparison = vector.get(mid).getWord().compareTo(word);
            comparacoes++; // Comparação de strings

            if (comparison == 0)
                return mid;
            else if (comparison < 0) {
                left = mid + 1;
                atribuicoes++; // Atribuição do left
            } else {
                right = mid - 1;
                atribuicoes++; // Atribuição do right
            }
        }
        return -1; // Palavra não encontrada
    }

    /**
     * Insere ou atualiza uma palavra no vetor mantendo a ordenação
     * 
     * @param word Palavra a ser inserida ou atualizada
     */
    public void insertOrUpdate(String word) {
        if (vector.isEmpty()) {
            vector.add(new WordFrequency(word));
            atribuicoes++; // Atribuição do novo objeto
            return;
        }

        int index = binarySearch(word);

        if (index != -1) {
            // Palavra já existe - incrementa frequência
            vector.get(index).incrementFrequency();
            atribuicoes++; // Atribuição do incremento
        } else {
            // Palavra não existe - insere na posição correta
            insertInOrder(word);
        }
    }

    /**
     * Insere uma nova palavra na posição ordenada correta
     * 
     * @param word Palavra a ser inserida
     */
    private void insertInOrder(String word) {
        int i = 0;
        while (i < vector.size()) {
            int comparison = vector.get(i).getWord().compareTo(word);
            comparacoes++; // Comparação de strings

            if (comparison > 0)
                break;
            i++;
            atribuicoes++; // Atribuição do i
        }

        vector.add(i, new WordFrequency(word));
        atribuicoes += 2; // Atribuição do novo objeto e do add na posição
    }

    /**
     * Processa um arquivo de texto e conta as palavras
     * 
     * @param filePath Caminho do arquivo
     */
    public void processFile(String filePath) {
        long startTime = System.currentTimeMillis();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] words = line.split("[\\s,.!?;:\"()]+");
                for (String rawWord : words) {
                    insertOrUpdate(rawWord.trim().toLowerCase());
                }
            }
        } catch (IOException e) {
            System.err.println("Erro ao ler o arquivo: " + e.getMessage());
        }

        long endTime = System.currentTimeMillis();
        long processingTime = endTime - startTime;

        TreeStats stats = new TreeStats(comparacoes, atribuicoes, processingTime, 0);
        System.out.println("\n=== ESTATÍSTICAS ===");
        System.out.println(stats);
    }

    /**
     * Exibe frequências de palavras no console
     */
    public void displayWordFrequencies() {
        System.out.println("\n=== FREQUÊNCIA DE PALAVRAS (ORDEM ALFABÉTICA) ===");
        for (WordFrequency wf : vector)
            System.out.println(wf);
    }

    /**
     * Obtém frequência de uma palavra específica
     * 
     * @param word Palavra a ser consultada
     * @return Frequência da palavra (0 se não encontrada)
     */
    public int getWordFrequency(String word) {
        int index = binarySearch(word.toLowerCase());
        if (index != -1)
            return vector.get(index).getFrequency();
        return 0;
    }

    /**
     * Obtém número total de palavras distintas
     * 
     * @return Tamanho do vetor
     */
    public int getTotalDistinctWords() {
        return vector.size();
    }

    /**
     * Obtém lista de frequências para exibição
     * 
     * @return Lista de strings no formato "palavra -> frequência"
     */
    public java.util.List<String> getFrequenciesAsList() {
        java.util.List<String> result = new java.util.ArrayList<>();
        for (WordFrequency wf : vector) {
            result.add(wf.getWord() + " -> " + wf.getFrequency());
        }
        return result;
    }

    /**
     * Constrói a busca a partir do vetor de palavras e retorna estatísticas
     * 
     * @param palavras Array de palavras a serem processadas
     * @return Estatísticas do processamento
     */
    public TreeStats buildWithStats(String[] palavras) {
        // RESET counters before starting
        resetAnalise();

        long inicio = System.nanoTime();
        for (String p : palavras) {
            insertOrUpdate(p);
        }
        long fim = System.nanoTime();
        double tempoExecucao = (fim - inicio) / 1_000_000.0;

        return new TreeStats(comparacoes, atribuicoes, 0, tempoExecucao, 0);
    }

    /**
     * Reseta contadores de análise
     */
    public void resetAnalise() {
        comparacoes = 0;
        atribuicoes = 0;
    }
}