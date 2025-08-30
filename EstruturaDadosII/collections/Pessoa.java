package EstruturaDadosII.collections;

public class Pessoa {

  private String nome;
  private String cpf;
  private Integer idade;
  private Double salario;

  public Pessoa() {

  }

  public String getNome() {
    return nome;
  }

  public String getCpf() {
    return cpf;
  }

  public Integer getIdade() {
    return idade;
  }

  public Double getSalario() {
    return salario;
  }

  public void setNome(String nome) {
    this.nome = nome;
  }

  public void setCpf(String cpf) {
    this.cpf = cpf;
  }

  public void setIdade(Integer idade) {
    this.idade = idade;
  }

  public void setSalario(Double salario) {
    this.salario = salario;
  }

  public String toString() {
    return "Nome: " + this.nome + ", CPF: " + this.cpf + ", Idade: " + this.idade + ", Salario: " + this.salario + "\n";
  }
}
