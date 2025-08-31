package EstruturaDadosII.collections.atividade.menus;

import java.util.Scanner;

import EstruturaDadosII.collections.atividade.models.Telefone;
import EstruturaDadosII.collections.atividade.services.GerenciadorTelefone;

public class MenuTelefone {
  private Scanner scanner;
  private GerenciadorTelefone gerenciadorTelefone;

  public MenuTelefone(Scanner scanner, GerenciadorTelefone gerenciadorTelefone) {
    this.scanner = scanner;
    this.gerenciadorTelefone = gerenciadorTelefone;
  }

  public void exibirMenu() {
    int opcao;

    do {
      System.out.println("\n=== GERENCIAR TELEFONES ===");
      System.out.println("1. Adicionar Telefone");
      System.out.println("2. Listar Telefones");
      System.out.println("3. Remover Telefone");
      System.out.println("0. Voltar ao Menu Principal");
      System.out.print("Escolha uma opção: ");

      opcao = scanner.nextInt();
      scanner.nextLine(); // Limpar buffer
      System.out.println('\n');

      switch (opcao) {
        case 1:
          adicionarTelefone();
          break;
        case 2:
          listarTelefones();
          break;
        case 3:
          removerTelefone();
          break;
        case 0:
          System.out.println("Voltando ao menu principal...");
          break;
        default:
          System.out.println("Opção inválida! Tente novamente.");
      }
    } while (opcao != 0);
  }

  private void adicionarTelefone() {
    System.out.println("Digite o ID do telefone: ");
    Integer id = scanner.nextInt();
    scanner.nextLine(); // Limpar buffer

    if (id < 0) {
      System.out.println("ID inválido! Tente novamente.");
      return;
    }

    if (gerenciadorTelefone.telefoneExiste(id)) {
      System.out.println("ID já cadastrado!");
      return;
    }

    String ddd;
    do {
      System.out.println("Digite o DDD do telefone (xx): ");
      ddd = scanner.nextLine();
      if (!Telefone.validarDDD(ddd)) {
        System.out.println("DDD inválido! Tente novamente.");
      }
    } while (!Telefone.validarDDD(ddd));

    String numero;
    do {
      System.out.println("Digite o número do telefone (xxxxx-xxxx): ");
      numero = scanner.nextLine();
      if (!Telefone.validarNumero(numero)) {
        System.out.println("Número inválido! Tente novamente.");
      }
    } while (!Telefone.validarNumero(numero));

    gerenciadorTelefone.adicionarTelefone(id, ddd, numero);
    System.out.println("Telefone adicionado com sucesso!");
  }

  private void listarTelefones() {
    gerenciadorTelefone.listarTelefones();
  }

  private void removerTelefone() {
    gerenciadorTelefone.listarTelefones();

    if (gerenciadorTelefone.getTelefones().isEmpty()) {
      return;
    }

    System.out.println("Digite o id do telefone que deseja remover: ");
    Integer id = scanner.nextInt();
    scanner.nextLine(); // Limpar buffer

    if (gerenciadorTelefone.removerTelefone(id)) {
      System.out.println("Telefone removido com sucesso!");
    } else {
      System.out.println("Telefone não encontrado!");
    }
  }
}
