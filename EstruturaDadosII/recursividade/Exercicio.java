package EstruturaDadosII.recursividade;

import java.lang.Math;

public class Exercicio {
  public static void main(String[] args) {
    // ----------Exercício 1---------- //

    System.out.println(potencia(2, 5)); // Resultado: 32

    // ----------Exercício 2---------- //
    String s1 = "ABCABCABC";
    String s2 = "BABACBAB";

    char[] X = s1.toCharArray();
    char[] Y = s2.toCharArray();

    int m = X.length;
    int n = Y.length;

    int lcsLength = lcsRecursivo(X, Y, m, n);
    int lcsDinamicoLength = lcsDinamico(X, Y);
    System.out.println("O lcs - recursivo - é: " + lcsLength);
    System.out.println("O lcs - dinâmico - é: " + lcsDinamicoLength);
  }

  // Exercício 1

  // Crie uma função recursiva que calcula a potência de um número, ou seja, X^Z:
  public static int potencia(int x, int n) {
    // Qual a condição de parada?
    // Condição de parada: se o expoente for 0
    if (n == 0) {
      return 1;
    } else {
      // Como escrever a função para o termo n em função do termo anterior?
      // Se expoente for != 0, calcula x * x ^ n - 1, para isso, chama a função recursivamente
      // Ex: 2 ^ 5 -> 2 * (2 ^ 4)
      return x * potencia(x, n - 1);
    }
    // Qual a complexidade desta função? O(n), tamanho do expoente
  }

  // Exercício 2 – Longest Common Subsequence

  /**
   * Calcula o comprimento da subsequência comum mais longa (LCS)
   * de duas sequências (strings ou arrays de caracteres) de forma recursiva.
   *
   * @param X A primeira sequência de caracteres.
   * @param Y A segunda sequência de caracteres.
   * @param m O comprimento atual da primeira sequência.
   * @param n O comprimento atual da segunda sequência.
   * @return O comprimento da LCS.
   */
  public static int lcsRecursivo(char[] X, char[] Y, int m, int n) {

    // Condição de parada: se uma das sequências estiver vazia, não há subsequência comum.
    if (m == 0 || n == 0) {
      System.out.println("Condição de parada");
      return 0;
    }

    // Se os últimos caracteres forem iguais, eles fazem parte da LCS.
    if (X[m - 1] == Y[n - 1]) {
      System.out.println("Recursivo");
      return 1 + lcsRecursivo(X, Y, m - 1, n - 1);
    } else {
      // Se os últimos caracteres forem diferentes, consideramos duas possibilidades e escolhemos o maior resultado.
      System.out.println("Recursivo");
      System.out.println("Recursivo");
      return Math.max(lcsRecursivo(X, Y, m, n - 1), lcsRecursivo(X, Y, m - 1, n));
    }
  }

  // Este algoritmo recursivo é eficiente para resolver este problema? Justifique sua resposta.
  // Não, pois a cada caractere diferente, é feita duas novas recursões para identificar a lcs. Utiliza muita memória mesmo para casos pequenos, facilmente não teria memória de máquina suficiente para casos de grande complexidade - complexidade exponencial.

  /**
   * Calcula o comprimento da subsequência comum mais longa (LCS)
   * de duas sequências usando programação dinâmica.
   *
   * @param X A primeira sequência de caracteres.
   * @param Y A segunda sequência de caracteres.
   * @return O comprimento da LCS.
   */
  public static int lcsDinamico(char[] X, char[] Y) {
    int m = X.length;
    int n = Y.length;

    // Cria uma matriz para armazenar os resultados dos subproblemas.
    int[][] resultado = new int[m + 1][n + 1];

    // Preenche a tabela em um processo de "bottom-up".
    for (int i = 0; i <= m; i++) {
      for (int j = 0; j <= n; j++) {
        // Se uma das strings for vazia, a LCS é 0.
        if (i == 0 || j == 0) {
          resultado[i][j] = 0;
        }
        // Se os caracteres atuais coincidirem, adiciona 1 ao resultado do subproblema anterior.
        else if (X[i - 1] == Y[j - 1]) {
          resultado[i][j] = 1 + resultado[i - 1][j - 1];
        }
        // Se os caracteres não coincidirem, pega o maior valor entre os subproblemas anteriores.
        else {
          resultado[i][j] = Math.max(resultado[i - 1][j], resultado[i][j - 1]);
        }
      }
    }

//         ""  B   A   B   A   C   B   A   B
// ""      0   0   0   0   0   0   0   0   0
// A       0   0   1   1   1   1   1   1   1
// B       0   1   1   2   2   2   2   2   2
// C       0   1   1   2   2   3   3   3   3
// A       0   1   2   2   3   3   3   4   4
// B       0   1   2   3   3   3   4   4   5
// C       0   1   2   3   3   4   4   4   5
// A       0   1   2   3   4   4   4   5   5
// B       0   1   2   3   4   4   5   5   6
// C       0   1   2   3   4   5   5   5   6

    // Retorna o valor na última célula, que contém o comprimento da LCS completa.
    return resultado[m][n];
  }
}