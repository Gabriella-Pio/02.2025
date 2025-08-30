package EstruturaDadosII.collections.atividade.menus;

import java.util.Scanner;
import EstruturaDadosII.collections.atividade.services.GerenciadorCliente;

public class MenuCliente {
  private Scanner scanner;
  private GerenciadorCliente gerenciadorCliente;

  public MenuCliente(Scanner scanner, GerenciadorCliente gerenciadorCliente) {
    this.scanner = scanner;
    this.gerenciadorCliente = gerenciadorCliente;
  }

  public void exibirMenu() {
    int opcao;

    do {
      System.out.println("\n=== GERENCIAR CLIENTES ===");
      System.out.println("1. Adicionar Cliente");
      System.out.println("2. Listar Clientes");
      System.out.println("0. Voltar ao Menu Principal");
      System.out.print("Escolha uma opção: ");

      opcao = scanner.nextInt();
      scanner.nextLine(); // Limpar buffer

      switch (opcao) {
        case 1:
          adicionarCliente();
          break;
        case 2:
          listarClientes();
          break;
        case 0:
          System.out.println("Voltando ao menu principal...");
          break;
        default:
          System.out.println("Opção inválida! Tente novamente.");
      }
    } while (opcao != 0);
  }

  private void adicionarCliente() {
    System.out.println("Digite o ID do cliente: ");
    Integer id = scanner.nextInt();
    scanner.nextLine(); // Limpar buffer

    if (id < 0) {
      System.out.println("ID inválido! Tente novamente.");
      return;
    }

    if (gerenciadorCliente.clienteExiste(id)) {
      System.out.println("ID já cadastrado!");
      return;
    }

    System.out.println("Digite o nome do cliente: ");
    String nome = scanner.nextLine();

    System.out.println("Digite a idade do cliente: ");
    Integer idade = scanner.nextInt();
    scanner.nextLine(); // Limpar buffer

    System.out.println("Digite o telefone do cliente: ");
    String telefone = scanner.nextLine();

    gerenciadorCliente.adicionarCliente(id, nome, idade, telefone);
    System.out.println("Cliente adicionado com sucesso!");
  }

  private void listarClientes() {
    gerenciadorCliente.listarClientes();
  }
}
