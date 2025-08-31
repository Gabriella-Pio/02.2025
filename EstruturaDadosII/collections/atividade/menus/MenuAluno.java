package EstruturaDadosII.collections.atividade.menus;

import java.util.Scanner;

import EstruturaDadosII.collections.atividade.models.Aluno;
import EstruturaDadosII.collections.atividade.services.GerenciadorAluno;

public class MenuAluno {
  private Scanner scanner;
  private GerenciadorAluno gerenciadorAluno;

  public MenuAluno(Scanner scanner, GerenciadorAluno gerenciadorAluno) {
    this.scanner = scanner;
    this.gerenciadorAluno = gerenciadorAluno;
  }

  public void exibirMenu() {
    int opcao;

    do {
      System.out.println("\n=== GERENCIAR ALUNOS (EXERCÍCIO 3) ===");
      System.out.println("1. Adicionar Aluno");
      System.out.println("2. Listar Alunos e Status Individual");
      System.out.println("3. Exibir Estatísticas da Turma");
      System.out.println("0. Voltar ao Menu Principal");
      System.out.print("Escolha uma opção: ");

      opcao = scanner.nextInt();
      scanner.nextLine(); // Limpar buffer
      System.out.println('\n');

      switch (opcao) {
        case 1:
          adicionarAluno();
          break;
        case 2:
          exibirStatusIndividual();
          break;
        case 3:
          exibirEstatisticasTurma();
          break;
        case 0:
          System.out.println("Voltando...");
          break;
        default:
          System.out.println("Opção inválida! Tente novamente.");
      }
    } while (opcao != 0);
  }

  private void adicionarAluno() {
    int adicionarOutro;

    do {
      System.out.println("--------- ADICIONAR ALUNO ---------");
      System.out.println("Digite a matrícula do aluno: ");
      Integer matricula = scanner.nextInt();
      scanner.nextLine(); // Limpar buffer

      if (matricula < 0) {
        System.out.println("Matrícula inválida! Tente novamente.");
        return;
      }

      if (gerenciadorAluno.alunoExiste(matricula)) {
        System.out.println("Matrícula já cadastrada!");
        return;
      }

      System.out.println("Digite o nome do aluno: ");
      String nome = scanner.nextLine();

      Double nb1;
      do {
        System.out.println("Digite a nota da NB1: ");
        nb1 = scanner.nextDouble();

        if (!Aluno.validarNota(nb1)) {
          System.out.println("Nota inválida! A nota deve estar entre 0.0 e 10.0. Tente novamente.");
        }
      } while (!Aluno.validarNota(nb1));

      Double nb2;
      do {
        System.out.println("Digite a nota da NB2: ");
        nb2 = scanner.nextDouble();

        if (!Aluno.validarNota(nb2)) {
          System.out.println("Nota inválida! A nota deve estar entre 0.0 e 10.0. Tente novamente.");
        }
      } while (!Aluno.validarNota(nb2));

      scanner.nextLine(); // Limpar buffer

      // Cria e adiciona o aluno
      Aluno novoAluno = new Aluno(matricula, nome, nb1, nb2);
      gerenciadorAluno.adicionarDadosAluno(matricula, nome, nb1, nb2);

      System.out.println("Aluno adicionado com sucesso!");

      // Mostra o status do aluno adicionado
      gerenciadorAluno.exibirStatusAluno(novoAluno);

      System.out.println('\n');
      System.out.print("Deseja adicionar outro aluno? (1 - Sim, 0 - Não) ");
      adicionarOutro = scanner.nextInt();
      scanner.nextLine(); // Limpar buffer
      System.out.println('\n');

    } while (adicionarOutro == 1);

  }

  private void exibirStatusIndividual() {
    System.out.println("--------- STATUS INDIVIDUAL DA TURMA ---------");
    gerenciadorAluno.exibirStatusIndividual();
  }

  private void exibirEstatisticasTurma() {
    System.out.println("--------- ESTATÍSTICAS DA TURMA ---------");
    gerenciadorAluno.exibirEstatisticasTurma();
  }
}
