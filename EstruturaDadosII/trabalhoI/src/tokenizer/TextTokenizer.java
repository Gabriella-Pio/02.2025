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
        } catch (IOException e) {
            System.err.println("Warning: Could not load stopwords file: " + e.getMessage());
            // Use default stopwords if file not found
        }
    }
    public void loadTextFile(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String word = line.trim().toLowerCase();
                    this.TEXT = this.TEXT.concat(word);
            }
        } catch (IOException e) {
            System.err.println("Warning: Could not load text file: " + e.getMessage());
            // Use default stopwords if file not found
    }
    }
    
    /**
     * Default stopwords if file loading fails
     */
    
    /**
     * Tokenize text based on spaces and punctuation
     */
    public List<String> tokenize(String text) {
        return tokenize(text, true);
    }
    
    /**
     * Tokenize text with option to remove stopwords
     */
    public List<String> tokenize(String text, boolean removeStopwords) {
        if (text == null || text.trim().isEmpty()) {
            return new ArrayList<>();
        }
        
        // Split on punctuation and whitespace
        String[] rawTokens = punctuationPattern.split(text.toLowerCase());
        List<String> tokens = new ArrayList<>();
        
        for (String token : rawTokens) {
            String cleanedToken = token.trim();
            
            // Skip empty tokens
            if (cleanedToken.isEmpty()) {
                continue;
            }
            
            // Remove stopwords if enabled
            if (removeStopwords && stopwords.contains(cleanedToken)) {
                continue;
            }
            
            tokens.add(cleanedToken);
        }
        
        return tokens;
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

        tokenizer.loadTextFile("src/resources/atv.txt");
        List<String> txt = tokenizer.tokenize(tokenizer.TEXT);
        Collections.sort(txt);
        Map<String,Integer> map = tokenizer.createOccurrenceMap(txt);
        System.out.println("Original text: " + tokenizer.TEXT+"\n");
        System.out.println("Tokenized (with stopwords): " + tokenizer.tokenize(tokenizer.TEXT, false)+"\n");
        System.out.println("Tokenized ValueArray: " + txt+"\n");
        System.out.println(map);
        
    }
}