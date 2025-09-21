// src/tokenizer/TextTokenizer.java
package tokenizer;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.*;

public class TextTokenizer {
    private Set<String> stopwords;
    public String TEXT;
    private Pattern punctuationPattern;

    public TextTokenizer() {
        this.stopwords = new HashSet<>();
        this.TEXT = "";
        // Simple punctuation and whitespace splitting - don't overcomplicate
        this.punctuationPattern = Pattern.compile("[\\p{Punct}\\s]+");
        loadStopwords("src/resources/stopwords.txt");
    }

    public TextTokenizer(String stopwordsFilePath) {
        this.stopwords = new HashSet<>();
        this.TEXT = "";
        this.punctuationPattern = Pattern.compile("[\\p{Punct}\\s]+");
        loadStopwords(stopwordsFilePath);
    }

    /**
     * Load stopwords from a file
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
            // Add basic Portuguese stopwords as fallback
            loadDefaultStopwords();
        }
    }

    /**
     * Load basic Portuguese stopwords if file not found
     */
    private void loadDefaultStopwords() {
        String[] defaultStopwords = { "de", "a", "o", "que", "e", "do", "da", "em", "um", "para", "é", "com", "não",
                "uma", "os", "no", "se", "na", "por", "mais", "as", "dos", "como", "mas", "foi", "ao", "ele", "das",
                "tem", "à", "seu", "sua", "ou", "ser", "quando", "muito", "há", "nos", "já", "está", "eu", "também",
                "só", "pelo", "pela", "até", "isso", "ela", "entre", "era", "depois", "sem", "mesmo", "aos", "ter",
                "seus", "quem", "nas", "me", "esse", "eles", "estão", "você", "tinha", "foram", "essa", "num", "nem",
                "suas", "meu", "às", "minha", "têm", "numa", "pelos", "elas", "havia", "seja", "qual", "será", "nós",
                "tenho", "lhe", "deles", "essas", "esses", "pelas", "este", "fosse", "dele", "tu", "te", "vocês", "vos",
                "lhes", "meus", "minhas", "teu", "tua", "teus", "tuas", "nosso", "nossa", "nossos", "nossas", "dela",
                "delas", "esta", "estes", "estas", "aquele", "aquela", "aqueles", "aquelas", "isto", "aquilo", "estou",
                "está", "estamos", "estão", "estive", "esteve", "estivemos", "estiveram", "estava", "estávamos",
                "estavam", "estivera", "estivéramos", "esteja", "estejamos", "estejam", "estivesse", "estivéssemos",
                "estivessem", "estiver", "estivermos", "estiverem", "hei", "há", "havemos", "hão", "houve", "houvemos",
                "houveram", "houvera", "houvéramos", "haja", "hajamos", "hajam", "houvesse", "houvéssemos", "houvessem",
                "houver", "houvermos", "houverem", "houverei", "houverá", "houveremos", "houverão", "houveria",
                "houveríamos", "houveriam", "sou", "somos", "são", "era", "éramos", "eram", "fui", "foi", "fomos",
                "foram", "fora", "fôramos", "seja", "sejamos", "sejam", "fosse", "fôssemos", "fossem", "for", "formos",
                "forem", "serei", "será", "seremos", "serão", "seria", "seríamos", "seriam", "tenho", "tem", "temos",
                "tém", "tinha", "tínhamos", "tinham", "tive", "teve", "tivemos", "tiveram", "tivera", "tivéramos",
                "tenha", "tenhamos", "tenham", "tivesse", "tivéssemos", "tivessem", "tiver", "tivermos", "tiverem",
                "terei", "terá", "teremos", "terão", "teria", "teríamos", "teriam"
        };

        Collections.addAll(stopwords, defaultStopwords);
        System.out.println("Using default stopwords: " + stopwords.size() + " words.");
    }

    public void loadTextFile(String filePath) {
        StringBuilder textBuilder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Add space between lines to avoid word concatenation
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
     * Simple, conservative tokenization - avoid over-splitting
     */
    public List<String> tokenize(String text) {
        return tokenize(text, true);
    }

    public List<String> tokenize(String text, boolean removeStopwords) {
        if (text == null || text.trim().isEmpty()) {
            return new ArrayList<>();
        }

        List<String> tokens = new ArrayList<>();

        // Convert to lowercase first
        String lowerText = text.toLowerCase();

        // Split ONLY on punctuation and whitespace - nothing else
        String[] rawTokens = punctuationPattern.split(lowerText);

        for (String token : rawTokens) {
            String cleanedToken = token.trim();

            // Skip empty tokens
            if (cleanedToken.isEmpty()) {
                continue;
            }

            // Only keep tokens that are actual words (contain letters, minimum length 2)
            if (cleanedToken.length() >= 2 && cleanedToken.matches("[a-záéíóúàèìòùâêîôûãõç]+")) {
                // Remove stopwords if enabled
                if (!removeStopwords || !stopwords.contains(cleanedToken)) {
                    tokens.add(cleanedToken);
                }
            }
        }

        return tokens;
    }

    /**
     * Special method to handle specific concatenated words if needed
     * Only call this if you specifically know there are concatenated words
     */
    public List<String> tokenizeWithCompoundSplitting(String text, boolean removeStopwords) {
        List<String> basicTokens = tokenize(text, false); // Don't remove stopwords yet
        List<String> result = new ArrayList<>();

        for (String token : basicTokens) {
            // Only try to split if token is suspiciously long (>10 chars) and looks like
            // concatenation
            if (token.length() > 10 && containsLikelyCompound(token)) {
                List<String> splitTokens = trySplitCompound(token);
                for (String splitToken : splitTokens) {
                    if (!removeStopwords || !stopwords.contains(splitToken)) {
                        result.add(splitToken);
                    }
                }
            } else {
                // Keep original token
                if (!removeStopwords || !stopwords.contains(token)) {
                    result.add(token);
                }
            }
        }

        return result;
    }

    /**
     * Check if a word likely contains compound elements
     */
    private boolean containsLikelyCompound(String word) {
        // Look for common Portuguese prepositions in the middle of long words
        String[] commonPreps = { "de", "da", "do", "em", "na", "no", "para", "com" };

        for (String prep : commonPreps) {
            // Check if prep appears after at least 3 chars and before at least 3 chars
            int index = word.indexOf(prep);
            if (index > 2 && index + prep.length() < word.length() - 2) {
                return true;
            }
        }
        return false;
    }

    /**
     * Conservative compound word splitting
     */
    private List<String> trySplitCompound(String word) {
        List<String> result = new ArrayList<>();

        String[] commonPreps = { "de", "da", "do", "em", "na", "no", "para", "com" };

        for (String prep : commonPreps) {
            int index = word.indexOf(prep);
            if (index > 2 && index + prep.length() < word.length() - 2) {
                String before = word.substring(0, index);
                String after = word.substring(index + prep.length());

                // Only split if both parts look like valid words
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

        // If no split worked, return original word
        result.add(word);
        return result;
    }

    /**
     * Tokenize and return as array
     */
    public String[] tokenizeToArray(String text) {
        List<String> tokens = tokenize(text);
        return tokens.toArray(new String[0]);
    }

    /**
     * Get the current set of stopwords
     */
    public Set<String> getStopwords() {
        return new HashSet<>(stopwords);
    }

    /**
     * Add a custom stopword
     */
    public void addStopword(String word) {
        stopwords.add(word.toLowerCase().trim());
    }

    /**
     * Remove a stopword
     */
    public void removeStopword(String word) {
        stopwords.remove(word.toLowerCase().trim());
    }

    /**
     * Clear all stopwords
     */
    public void clearStopwords() {
        stopwords.clear();
    }

    public <String> Map<String, Integer> createOccurrenceMap(List<String> list) {
        Map<String, Integer> occurrenceMap = new HashMap<>();
        for (String element : list) {
            occurrenceMap.put(element, occurrenceMap.getOrDefault(element, 0) + 1);
        }
        return occurrenceMap;
    }

    /**
     * Main method for testing
     */
    public static void main(String[] args) {
        TextTokenizer tokenizer = new TextTokenizer();

        // Test with your specific text
        String testText = "Faça um programa que leia um arquivo texto";
        System.out.println("Original text: " + testText);
        System.out.println("Tokenized: " + tokenizer.tokenize(testText, false));
        System.out.println("Without stopwords: " + tokenizer.tokenize(testText, true));

        // Test with the actual file if provided
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