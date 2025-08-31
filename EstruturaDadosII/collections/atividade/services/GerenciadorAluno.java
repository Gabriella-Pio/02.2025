package EstruturaDadosII.collections.atividade.services;

import java.util.ArrayList;
import java.util.List;

import EstruturaDadosII.collections.atividade.models.Aluno;

public class GerenciadorAluno {
  private List<Aluno> alunos;

  public GerenciadorAluno() {
    this.alunos = new ArrayList<>();
  }

  public void adicionarDadosAluno(Integer matricula, String nome, Double nb1, Double nb2) {
    Aluno aluno = new Aluno(matricula, nome, nb1, nb2);
    alunos.add(aluno);
  }

  public boolean alunoExiste(Integer matricula) {
    for (Aluno aluno : alunos) {
      if (aluno.getMatricula().equals(matricula)) {
        return true;
      }
    }
    return false;
  }

  public Double calcularMedia(Aluno aluno) {
    return (aluno.getNb1() + aluno.getNb2()) / 2;
  }

  public String determinarStatus(Double media) {
    if (media < 4.0) {
      return "REPROVADO";
    } else if (media >= 4.0 && media < 6.0) {
      return "EXAME";
    } else {
      return "APROVADO";
    }
  }

  public void exibirStatusAluno(Aluno aluno) {
    Double media = calcularMedia(aluno);
    String status = determinarStatus(media);

    System.out.println("-----------------------------------");
    System.out.printf("Aluno: %s\n", aluno.getNome());
    System.out.printf("Média: %.2f\n", media);
    System.out.printf("Status: %s\n", status);
    System.out.println("-----------------------------------");
  }

  public void exibirStatusIndividual() {
    for (Aluno aluno : alunos) {
      Double media = calcularMedia(aluno);
      String status = determinarStatus(media);

      System.out.printf("Aluno %s, Média: %.2f \n", aluno.getNome(), media);
      System.out.println("Status: " + status);
      System.out.println("--------------------------------------------");
    }
  }

  public void exibirEstatisticasTurma() {
    if (alunos.isEmpty()) {
      System.out.println("Nenhum aluno cadastrado.");
      return;
    }

    int aprovados = 0, reprovados = 0, exame = 0;
    Double somaMedias = 0.0;
    Aluno maiorMediaAluno = alunos.get(0);
    Aluno menorMediaAluno = alunos.get(0);
    Double maiorMedia = calcularMedia(maiorMediaAluno);
    Double menorMedia = calcularMedia(menorMediaAluno);

    for (Aluno aluno : alunos) {
      Double media = calcularMedia(aluno);
      String status = determinarStatus(media);

      switch (status) {
        case "APROVADO":
          aprovados++;
          break;
        case "REPROVADO":
          reprovados++;
          break;
        case "EXAME":
          exame++;
          break;
      }

      somaMedias += media;

      if (media > maiorMedia) {
        maiorMedia = media;
        maiorMediaAluno = aluno;
      }
      if (media < menorMedia) {
        menorMedia = media;
        menorMediaAluno = aluno;
      }
    }

    System.out.println("Total de alunos: " + alunos.size());
    System.out.println("-----------------------------------");
    System.out.println("Aprovados: " + aprovados);
    System.out.println("Reprovados: " + reprovados);
    System.out.println("Exame: " + exame);
    System.out.println("-----------------------------------");
    Double mediaTurma = somaMedias / alunos.size();
    System.out.printf("Média da turma: %.2f\n", mediaTurma);
    System.out.println("-----------------------------------");
    System.out.printf("Maior média: %.2f, aluno %s\n", maiorMedia, maiorMediaAluno.getNome());
    System.out.printf("Menor média: %.2f, aluno %s\n", menorMedia, menorMediaAluno.getNome());
    System.out.println("-----------------------------------");
  }

  public List<Aluno> getAlunos() {
    return alunos;
  }
}
