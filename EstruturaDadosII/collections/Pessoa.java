package EstruturaDadosII.collections;

public class Pessoa {

  private String nome;
  private String cpf;
  private Integer idade;
  private Double salario;

  public Pessoa() {
  }

  public Pessoa(String nome, String cpf, Integer idade, Double salario) {
    super();
    this.nome = nome;
    this.cpf = cpf;
    this.idade = idade;
    this.salario = salario;
  }

  public String getNome() {
    return nome;
  }

  public void setNome(String nome) {
    this.nome = nome;
  }

  public String getCpf() {
    return cpf;
  }

  public void setCpf(String cpf) {
    this.cpf = cpf;
  }

  public Integer getIdade() {
    return idade;
  }

  public void setIdade(Integer idade) {
    this.idade = idade;
  }

  public Double getSalario() {
    return salario;
  }

  public void setSalario(Double salario) {
    this.salario = salario;
  }

  public String toString() {
    return "Nome: " + this.nome + ", CPF: " + this.cpf +
        ", Idade: " + this.idade + ", Salário: R$ " + this.salario + "\n";
  }

}
