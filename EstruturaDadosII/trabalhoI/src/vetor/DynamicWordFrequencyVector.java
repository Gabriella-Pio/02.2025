package vetor;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import arvore.TreeStats; // importando a mesma classe de estatísticas

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

public class DynamicWordFrequencyVector {
    private final List<WordFrequency> vector;
    private int comparacoes;
    private int atribuicoes;

    public DynamicWordFrequencyVector() {
        this.vector = new ArrayList<>();
        this.comparacoes = 0;
        this.atribuicoes = 0;
    }

    // Busca binária para encontrar uma palavra no vetor
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

    // Insere ou atualiza uma palavra no vetor mantendo a ordenação
    public void insertOrUpdate(String word) {
        // if(word.isEmpty() || isStopword(word)) return;

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

    // Insere uma nova palavra na posição ordenada correta
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

    // Processa um arquivo de texto e conta as palavras
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

        TreeStats stats = new TreeStats(comparacoes, atribuicoes, processingTime,0);
        System.out.println("\n=== ESTATÍSTICAS ===");
        System.out.println(stats);
    }

    public void displayWordFrequencies() {
        System.out.println("\n=== FREQUÊNCIA DE PALAVRAS (ORDEM ALFABÉTICA) ===");
        for (WordFrequency wf : vector)
            System.out.println(wf);
    }

    public int getWordFrequency(String word) {
        int index = binarySearch(word.toLowerCase());
        if (index != -1)
            return vector.get(index).getFrequency();
        return 0;
    }

    public int getTotalDistinctWords() {
        return vector.size();
    }

    public java.util.List<String> getFrequenciesAsList() {
        java.util.List<String> result = new java.util.ArrayList<>();
        for (WordFrequency wf : vector) {
            result.add(wf.getWord() + " -> " + wf.getFrequency());
        }
        return result;
    }

    // Constrói a busca a partir do vetor de palavras e retorna estatísticas
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
    
    public void resetAnalise() {
        comparacoes = 0;
        atribuicoes = 0;
    }
}