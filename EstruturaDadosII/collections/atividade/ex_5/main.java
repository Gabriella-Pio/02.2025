package EstruturaDadosII.collections.atividade.ex_5;

import java.util.Scanner;

import EstruturaDadosII.collections.atividade.ex_5.Agenda;
import EstruturaDadosII.collections.atividade.ex_5.Contato;

import java.util.ArrayList;
import java.util.List;

public class main {
  private static Scanner scanner = new Scanner(System.in);
  private static Agenda agenda = new Agenda(new ArrayList<>());

  public static void main(String[] args) {
    exibirMenu();

  }

  public static void exibirMenu() {
    int opcao;

    do {
      System.out.println("\n=== AGENDA ELETRÔNICA (EXERCÍCIO 5) ===");
      System.out.println("[ 1 ] Incluir Contato");
      System.out.println("[ 2 ] Excluir Contato");
      System.out.println("[ 3 ] Listar Contatos");
      System.out.println("[ 4 ] Pesquisar Contato");
      System.out.println("[ 0 ] Encerrar o Programa");
      System.out.print("Opção: ");

      opcao = scanner.nextInt();
      scanner.nextLine();

      switch (opcao) {
        case 1:
          incluirContato();
          break;
        case 2:
          excluirContato();
          break;
        case 3:
          listarContatos();
          break;
        case 4:
          pesquisarContato();
          break;
        case 0:
          System.out.println("Encerrando agenda...");
          break;
        default:
          System.out.println("Opção inválida!");
      }
    } while (opcao != 0);
  }

  private static void incluirContato() {
    System.out.println("\n--- INCLUIR CONTATO ---");

    System.out.print("Informe o nome do contato: ");
    String nome = scanner.nextLine();

    if (agenda.contatoExistente(nome)) {
      System.out.println("Já existe um contato com este nome!");
      return;
    }

    System.out.print("Informe o telefone do contato: ");
    String telefone = scanner.nextLine();

    System.out.print("informe o email do contato: ");
    String email = scanner.nextLine();

    Contato novoContato = new Contato(nome, telefone, email);
    agenda.adicionarContato(novoContato);

    System.out.println("Contato adicionado com sucesso!");
  }

  private static void excluirContato() {
    System.out.println("\n--- EXCLUIR CONTATO ---");

    agenda.listarContatos();
    if (agenda.getContatos().isEmpty()) {
      return;
    }

    System.out.print("Digite o nome do contato a excluir: ");
    String nome = scanner.nextLine();

    if (agenda.removerContatoPorNome(nome)) {
      System.out.println("Contato removido com sucesso!");
    } else {
      System.out.println("Contato não encontrado!");
    }
  }

  private static void listarContatos() {
    System.out.println("\n--- LISTAR CONTATOS ---");
    agenda.listarContatos();
  }


  private static void pesquisarContato() {
    System.out.println("\n--- PESQUISAR CONTATO ---");

    if (agenda.getContatos().isEmpty()) {
      System.out.println("Nenhum contato cadastrado.");
      return;
    }

    System.out.print("Digite o nome do contato a pesquisar: ");
    String nome = scanner.nextLine();

    Contato contato = agenda.buscarContatoPorNome(nome);
    if (contato != null) {
      System.out.println("\nContato encontrado:");
      System.out.println(contato.toString());
    } else {
      System.out.println("Contato não encontrado!");
    }
  }
}
