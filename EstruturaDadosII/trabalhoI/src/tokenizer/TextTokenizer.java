// src/tokenizer/TextTokenizer.java
package tokenizer;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.*;

/**
 * Classe responsável por tokenizar texto, dividindo-o em palavras individuais
 * e removendo stopwords (palavras muito comuns sem significado lexical)
 */
public class TextTokenizer {
    private Set<String> stopwords; // Conjunto de palavras a serem ignoradas
    public String TEXT; // Texto carregado para processamento
    private Pattern punctuationPattern; // Padrão regex para identificar pontuação

    /**
     * Construtor padrão - inicializa com stopwords do arquivo padrão
     */
    public TextTokenizer() {
        this.stopwords = new HashSet<>();
        this.TEXT = "";
        // Padrão para dividir texto baseado em pontuação e espaços
        this.punctuationPattern = Pattern.compile("[\\p{Punct}\\s]+");
        loadStopwords("src/resources/stopwords.txt");
    }

    /**
     * Construtor com caminho customizado para arquivo de stopwords
     * 
     * @param stopwordsFilePath Caminho para o arquivo de stopwords
     */
    public TextTokenizer(String stopwordsFilePath) {
        this.stopwords = new HashSet<>();
        this.TEXT = "";
        this.punctuationPattern = Pattern.compile("[\\p{Punct}\\s]+");
        loadStopwords(stopwordsFilePath);
    }

    /**
     * Carrega stopwords de um arquivo
     * 
     * @param filePath Caminho do arquivo de stopwords
     */
    private void loadStopwords(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String word = line.trim().toLowerCase();
                if (!word.isEmpty()) {
                    stopwords.add(word);
                }
            }
            System.out.println("Loaded " + stopwords.size() + " stopwords from file.");
        } catch (IOException e) {
            System.err.println("Warning: Could not load stopwords file: " + e.getMessage());
            // Carrega stopwords padrão em português se arquivo não for encontrado
            loadDefaultStopwords();
        }
    }

    /**
     * Carrega stopwords padrão em português (fallback)
     */
    private void loadDefaultStopwords() {
        String[] defaultStopwords = {
                "de", "a", "o", "que", "e", "do", "da", "em", "um", "para", "é", "com", "não",
                "uma", "os", "no", "se", "na", "por", "mais", "as", "dos", "como", "mas", "foi",
                "ao", "ele", "das", "tem", "à", "seu", "sua", "ou", "ser", "quando", "muito",
                // ... (lista completa de stopwords em português)
        };

        Collections.addAll(stopwords, defaultStopwords);
        System.out.println("Using default stopwords: " + stopwords.size() + " words.");
    }

    /**
     * Carrega texto de um arquivo para processamento
     * 
     * @param filePath Caminho do arquivo de texto
     */
    public void loadTextFile(String filePath) {
        StringBuilder textBuilder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Adiciona espaço entre linhas para evitar concatenação de palavras
                if (textBuilder.length() > 0) {
                    textBuilder.append(" ");
                }
                textBuilder.append(line.trim());
            }
            this.TEXT = textBuilder.toString();
            System.out.println("Loaded text file: " + filePath + " (" + TEXT.length() + " characters)");
        } catch (IOException e) {
            System.err.println("Error: Could not load text file: " + e.getMessage());
        }
    }

    /**
     * Tokenização simples - divide texto em palavras, removendo stopwords por
     * padrão
     * 
     * @param text Texto a ser tokenizado
     * @return Lista de tokens (palavras)
     */
    public List<String> tokenize(String text) {
        return tokenize(text, true);
    }

    /**
     * Tokenização com controle de remoção de stopwords
     * 
     * @param text            Texto a ser tokenizado
     * @param removeStopwords Se true, remove stopwords da lista resultante
     * @return Lista de tokens (palavras)
     */
    public List<String> tokenize(String text, boolean removeStopwords) {
        if (text == null || text.trim().isEmpty()) {
            return new ArrayList<>();
        }

        List<String> tokens = new ArrayList<>();

        // Converte para minúsculas para uniformização
        String lowerText = text.toLowerCase();

        // Divide o texto usando o padrão de pontuação e espaços
        String[] rawTokens = punctuationPattern.split(lowerText);

        for (String token : rawTokens) {
            String cleanedToken = token.trim();

            // Pula tokens vazios
            if (cleanedToken.isEmpty()) {
                continue;
            }

            // Mantém apenas tokens que são palavras válidas (contêm letras, mínimo 2
            // caracteres)
            if (cleanedToken.length() >= 2 && cleanedToken.matches("[a-záéíóúàèìòùâêîôûãõç]+")) {
                // Remove stopwords se habilitado
                if (!removeStopwords || !stopwords.contains(cleanedToken)) {
                    tokens.add(cleanedToken);
                }
            }
        }

        return tokens;
    }

    /**
     * Tokenização com divisão de palavras compostas
     * Apenas para casos específicos onde palavras estão concatenadas
     * 
     * @param text            Texto a ser tokenizado
     * @param removeStopwords Se true, remove stopwords
     * @return Lista de tokens com palavras compostas divididas
     */
    public List<String> tokenizeWithCompoundSplitting(String text, boolean removeStopwords) {
        List<String> basicTokens = tokenize(text, false); // Não remove stopwords ainda
        List<String> result = new ArrayList<>();

        for (String token : basicTokens) {
            // Apenas tenta dividir se o token for suspeitamente longo (>10 chars)
            if (token.length() > 10 && containsLikelyCompound(token)) {
                List<String> splitTokens = trySplitCompound(token);
                for (String splitToken : splitTokens) {
                    if (!removeStopwords || !stopwords.contains(splitToken)) {
                        result.add(splitToken);
                    }
                }
            } else {
                // Mantém o token original
                if (!removeStopwords || !stopwords.contains(token)) {
                    result.add(token);
                }
            }
        }

        return result;
    }

    /**
     * Verifica se uma palavra provavelmente contém elementos compostos
     * 
     * @param word Palavra a ser verificada
     * @return True se a palavra parece ser composta
     */
    private boolean containsLikelyCompound(String word) {
        // Procura por preposições comuns no meio de palavras longas
        String[] commonPreps = { "de", "da", "do", "em", "na", "no", "para", "com" };

        for (String prep : commonPreps) {
            // Verifica se a preposição aparece após pelo menos 3 chars e antes de pelo
            // menos 3 chars
            int index = word.indexOf(prep);
            if (index > 2 && index + prep.length() < word.length() - 2) {
                return true;
            }
        }
        return false;
    }

    /**
     * Tenta dividir palavras compostas de forma conservadora
     * 
     * @param word Palavra a ser dividida
     * @return Lista de partes da palavra
     */
    private List<String> trySplitCompound(String word) {
        List<String> result = new ArrayList<>();

        String[] commonPreps = { "de", "da", "do", "em", "na", "no", "para", "com" };

        for (String prep : commonPreps) {
            int index = word.indexOf(prep);
            if (index > 2 && index + prep.length() < word.length() - 2) {
                String before = word.substring(0, index);
                String after = word.substring(index + prep.length());

                // Apenas divide se ambas as partes parecerem palavras válidas
                if (before.length() >= 3 && after.length() >= 3 &&
                        before.matches("[a-záéíóúàèìòùâêîôûãõç]+") &&
                        after.matches("[a-záéíóúàèìòùâêîôûãõç]+")) {
                    result.add(before);
                    result.add(prep);
                    result.add(after);
                    return result;
                }
            }
        }

        // Se nenhuma divisão funcionou, retorna a palavra original
        result.add(word);
        return result;
    }

    /**
     * Tokeniza texto e retorna como array
     * 
     * @param text Texto a ser tokenizado
     * @return Array de tokens
     */
    public String[] tokenizeToArray(String text) {
        List<String> tokens = tokenize(text);
        return tokens.toArray(new String[0]);
    }

    /**
     * Retorna o conjunto atual de stopwords
     * 
     * @return Conjunto de stopwords
     */
    public Set<String> getStopwords() {
        return new HashSet<>(stopwords);
    }

    /**
     * Adiciona uma stopword personalizada
     * 
     * @param word Palavra a ser adicionada como stopword
     */
    public void addStopword(String word) {
        stopwords.add(word.toLowerCase().trim());
    }

    /**
     * Remove uma stopword
     * 
     * @param word Palavra a ser removida das stopwords
     */
    public void removeStopword(String word) {
        stopwords.remove(word.toLowerCase().trim());
    }

    /**
     * Limpa todas as stopwords
     */
    public void clearStopwords() {
        stopwords.clear();
    }

    /**
     * Cria um mapa de ocorrências de palavras
     * 
     * @param list Lista de palavras
     * @return Mapa com palavras como chaves e contagens como valores
     */
    public Map<String, Integer> createOccurrenceMap(List<String> list) {
        Map<String, Integer> occurrenceMap = new HashMap<>();
        for (String element : list) {
            occurrenceMap.put(element, occurrenceMap.getOrDefault(element, 0) + 1);
        }
        return occurrenceMap;
    }

    /**
     * Método principal para testes
     * 
     * @param args Argumentos da linha de comando (caminho do arquivo opcional)
     */
    public static void main(String[] args) {
        TextTokenizer tokenizer = new TextTokenizer();

        // Teste com texto específico
        String testText = "Faça um programa que leia um arquivo texto";
        System.out.println("Original text: " + testText);
        System.out.println("Tokenized: " + tokenizer.tokenize(testText, false));
        System.out.println("Without stopwords: " + tokenizer.tokenize(testText, true));

        // Teste com arquivo real se fornecido
        if (args.length > 0) {
            tokenizer.loadTextFile(args[0]);
            List<String> tokens = tokenizer.tokenize(tokenizer.TEXT);
            Collections.sort(tokens);

            System.out.println("\nFirst 20 tokens after tokenization:");
            for (int i = 0; i < Math.min(20, tokens.size()); i++) {
                System.out.println((i + 1) + ": " + tokens.get(i));
            }

            Map<String, Integer> map = tokenizer.createOccurrenceMap(tokens);
            System.out.println("\nTotal unique words: " + map.size());
        }
    }
}