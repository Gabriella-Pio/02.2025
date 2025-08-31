package EstruturaDadosII.collections.atividade.menus;

import java.util.Scanner;
import EstruturaDadosII.collections.atividade.services.GerenciadorCliente;
import EstruturaDadosII.collections.atividade.services.GerenciadorTelefone;
import EstruturaDadosII.collections.atividade.services.GerenciadorAluno;

public class MenuPrincipal {
  private Scanner scanner;
  private GerenciadorCliente gerenciadorCliente;
  private GerenciadorTelefone gerenciadorTelefone;
  private GerenciadorAluno gerenciadorAluno;
  private MenuCliente menuCliente;
  private MenuTelefone menuTelefone;
  private MenuAluno menuAluno;

  public MenuPrincipal(Scanner scanner) {
    this.scanner = scanner;
    this.gerenciadorCliente = new GerenciadorCliente();
    this.gerenciadorTelefone = new GerenciadorTelefone();
    this.gerenciadorAluno = new GerenciadorAluno();
    this.menuCliente = new MenuCliente(scanner, gerenciadorCliente);
    this.menuTelefone = new MenuTelefone(scanner, gerenciadorTelefone);
    this.menuAluno = new MenuAluno(scanner, gerenciadorAluno);
  }

  public void executar() {
    int opcao;

    do {
      System.out.println("\n=== SISTEMA PRINCIPAL ===");
      System.out.println("1 - Gerenciar Clientes");
      System.out.println("2 - Gerenciar Telefones");
      System.out.println("3 - Gerenciar Alunos");
      System.out.println("0 - Sair");
      System.out.print("Opção: ");

      opcao = scanner.nextInt();
      scanner.nextLine();

      switch (opcao) {
        case 1:
          menuCliente.exibirMenu();
          break;
        case 2:
          menuTelefone.exibirMenu();
          break;
        case 3:
          menuAluno.exibirMenu();
          break;
        case 0:
          System.out.println("Encerrando sistema...");
          break;
        default:
          System.out.println("Opção inválida!");
      }
    } while (opcao != 0);
  }
}