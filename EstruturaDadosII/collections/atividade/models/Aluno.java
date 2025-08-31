package EstruturaDadosII.collections.atividade.models;

public class Aluno {
  private Integer matricula;
  private String nome;
  private Double nb1;
  private Double nb2;

  public Aluno (Integer matricula, String nome, Double nb1, Double nb2) {
    this.matricula = matricula;
    this.nome = nome;
    this.nb1 = nb1;
    this.nb2 = nb2;
  }

  public Integer getMatricula() {
    return matricula;
  }

  public void setMatricula(Integer matricula) {
    this.matricula = matricula;
  }

  public String getNome() {
    return nome;
  }

  public void setNome(String nome) {
    this.nome = nome;
  }

  public Double getNb1() {
    return nb1;
  }

  public void setNb1(Double nb1) {
    this.nb1 = nb1;
  }

  public Double getNb2() {
    return nb2;
  }

  public void setNb2(Double nb2) {
    this.nb2 = nb2;
  }

  public static boolean validarNota(Double nota) {
    return nota != null && nota >= 0.0 && nota <= 10.0;
  }

  public String toString() {
    return "Matrícula: " + this.matricula + ", Nome: " + this.nome + ", NB1: " + this.nb1 + ", NB2: " + this.nb2 + "\n";
  }
}
