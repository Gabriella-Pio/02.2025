import arvore.AVLTree;
import arvore.BSTree;
import arvore.TreeStats;
import tokenizer.TextTokenizer;
import vetor.DynamicWordFrequencyVector;

public class Principal {
    public static void main(String[] args) {
        TextTokenizer tokenizer = new TextTokenizer();
        tokenizer.loadTextFile("src/resources/atv.txt");
        String[] palavrasTeste = tokenizer.tokenizeToArray(tokenizer.TEXT);

        // -------------------------------
        // Vetor Dinâmico
        // -------------------------------
        System.out.println("=== VETOR DINÂMICO (BUSCA BINÁRIA) ===");
        DynamicWordFrequencyVector vector = new DynamicWordFrequencyVector();
        TreeStats statsVetor = vector.buildWithStats(palavrasTeste);
        vector.displayWordFrequencies();
        System.out.println(statsVetor);

        // -------------------------------
        // Árvore Binária de Busca (BST)
        // -------------------------------
        System.out.println("\n=== BST ===");
        BSTree bst = new BSTree();
        TreeStats statsBST = bst.buildWithStats(palavrasTeste);
        System.out.println(statsBST);

        // -------------------------------
        // Árvore AVL (Balanceada)
        // -------------------------------
        System.out.println("\n=== AVL ===");
        AVLTree avl = new AVLTree();
        TreeStats statsAVL = avl.buildWithStats(palavrasTeste);
        System.out.println(statsAVL);
    }
}
