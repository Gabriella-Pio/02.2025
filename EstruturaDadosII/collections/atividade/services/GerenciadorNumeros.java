package EstruturaDadosII.collections.atividade.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class GerenciadorNumeros {
    private List<Integer> numeros;
    private Random random;

    public GerenciadorNumeros() {
        this.numeros = new ArrayList<>();
        this.random = new Random();
    }

    /**
     * Gera números aleatórios distintos entre 1 e 100
     * @param quantidade Quantidade de números a serem gerados
     * @return true se gerado com sucesso, false se quantidade inválida
     */
    public boolean gerarNumerosAleatorios(int quantidade) {
        if (quantidade <= 0 || quantidade > 100) {
            return false;
        }

        numeros.clear();
        Set<Integer> numerosUnicos = new HashSet<>();
        
        while (numerosUnicos.size() < quantidade) {
            int numero = random.nextInt(100) + 1; // Gera entre 1 e 100
            numerosUnicos.add(numero);
        }
        
        numeros.addAll(numerosUnicos);
        return true;
    }

    /**
     * Ordena a lista em ordem crescente
     */
    public void ordenarCrescente() {
        Collections.sort(numeros);
    }

    /**
     * Ordena a lista em ordem decrescente
     */
    public void ordenarDecrescente() {
        Collections.sort(numeros);
        Collections.reverse(numeros);
    }

    /**
     * Exibe a lista atual
     */
    public void exibirLista(String mensagem) {
        System.out.println("\n" + mensagem);
        System.out.println("Quantidade: " + numeros.size());
        System.out.println("Números: " + numeros);
    }

    /**
     * Exibe todas as ordens solicitadas pelo exercício
     */
    public void exibirTodasOrdens() {
        // Ordem original
        exibirLista("=== ORDEM ORIGINAL ===");
        
        // Ordem crescente
        List<Integer> copiaCrescente = new ArrayList<>(numeros);
        Collections.sort(copiaCrescente);
        System.out.println("\n=== ORDEM CRESCENTE ===");
        System.out.println("Números: " + copiaCrescente);
        
        // Ordem decrescente
        List<Integer> copiaDecrescente = new ArrayList<>(numeros);
        Collections.sort(copiaDecrescente);
        Collections.reverse(copiaDecrescente);
        System.out.println("\n=== ORDEM DECRESCENTE ===");
        System.out.println("Números: " + copiaDecrescente);
    }

    public List<Integer> getNumeros() {
        return numeros;
    }

    public boolean isListaVazia() {
        return numeros.isEmpty();
    }
}