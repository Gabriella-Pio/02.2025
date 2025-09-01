package EstruturaDadosII.collections.atividade.menus;

import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import EstruturaDadosII.collections.atividade.services.GerenciadorNumeros;

public class MenuNumeros {
  private Scanner scanner;
  private GerenciadorNumeros gerenciadorNumeros;

  public MenuNumeros(Scanner scanner, GerenciadorNumeros gerenciadorNumeros) {
    this.scanner = scanner;
    this.gerenciadorNumeros = gerenciadorNumeros;
  }

  public void exibirMenu() {
    int opcao;

    do {
      System.out.println("\n=== GERENCIAR NÚMEROS (EXERCÍCIO 4) ===");
      System.out.println("1 - Gerar números aleatórios");
      System.out.println("2 - Exibir ordem original");
      System.out.println("3 - Exibir ordem crescente");
      System.out.println("4 - Exibir ordem decrescente");
      System.out.println("5 - Exibir todas as ordens (Exercício completo)");
      System.out.println("0 - Voltar ao Menu Principal");
      System.out.print("Opção: ");

      opcao = scanner.nextInt();
      scanner.nextLine();

      switch (opcao) {
        case 1:
          gerarNumerosAleatorios();
          break;
        case 2:
          exibirOrdemOriginal();
          break;
        case 3:
          exibirOrdemCrescente();
          break;
        case 4:
          exibirOrdemDecrescente();
          break;
        case 5:
          executarExercicioCompleto();
          break;
        case 0:
          System.out.println("Voltando ao menu principal...");
          break;
        default:
          System.out.println("Opção inválida!");
      }
    } while (opcao != 0);
  }

  private void gerarNumerosAleatorios() {
    System.out.println("\n--- GERAR NÚMEROS ALEATÓRIOS ---");
    System.out.print("Digite a quantidade de números (1-100): ");
    int quantidade = scanner.nextInt();
    scanner.nextLine();

    if (gerenciadorNumeros.gerarNumerosAleatorios(quantidade)) {
      System.out.println("Números gerados com sucesso!");
      gerenciadorNumeros.exibirLista("Lista gerada:");
    } else {
      System.out.println("Quantidade inválida! Digite entre 1 e 100.");
    }
  }

  private void exibirOrdemOriginal() {
    if (gerenciadorNumeros.isListaVazia()) {
      System.out.println("Nenhum número gerado. Use a opção 1 primeiro.");
      return;
    }
    gerenciadorNumeros.exibirLista("=== ORDEM ORIGINAL ===");
  }

  private void exibirOrdemCrescente() {
    if (gerenciadorNumeros.isListaVazia()) {
      System.out.println("Nenhum número gerado. Use a opção 1 primeiro.");
      return;
    }

    gerenciadorNumeros.ordenarCrescente();
    gerenciadorNumeros.exibirLista("=== ORDEM CRESCENTE ===");
  }

  private void exibirOrdemDecrescente() {
    if (gerenciadorNumeros.isListaVazia()) {
      System.out.println("Nenhum número gerado. Use a opção 1 primeiro.");
      return;
    }

    gerenciadorNumeros.ordenarDecrescente();
    gerenciadorNumeros.exibirLista("=== ORDEM DECRESCENTE ===");
  }

  private void executarExercicioCompleto() {
    System.out.println("\n=== EXECUTANDO EXERCÍCIO 4 COMPLETO ===");

    System.out.print("Digite a quantidade de números a serem gerados: ");
    int quantidade = scanner.nextInt();
    scanner.nextLine();

    if (!gerenciadorNumeros.gerarNumerosAleatorios(quantidade)) {
      System.out.println("Quantidade inválida! Digite entre 1 e 100.");
      return;
    }

    System.out.println("\n" + "=".repeat(50));
    System.out.println("RESULTADOS (EXERCÍCIO 4):");
    System.out.println("=".repeat(50));

    gerenciadorNumeros.exibirTodasOrdens();

    // DESAFIO: Verificar se todos são distintos
    System.out.println("\n=== DESAFIO - VERIFICAÇÃO ===");
    verificarNumerosDistintos();
  }

  private void verificarNumerosDistintos() {
    List<Integer> numeros = gerenciadorNumeros.getNumeros();
    Set<Integer> conjunto = new HashSet<>(numeros);

    if (numeros.size() == conjunto.size()) {
      System.out.println("Os números são diferentes!");
    } else {
      System.out.println("Existem números repetidos!");
    }
    System.out.println("Total de números: " + numeros.size());
    System.out.println("Números únicos: " + conjunto.size());
  }
}