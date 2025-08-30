package EstruturaDadosII.collections.atividade.models;

public class Cliente {
  private Integer id;
  private String nome;
  private Integer idade;
  private String telefone;

  public Cliente(Integer id, String nome, Integer idade, String telefone) {
    this.id = id;
    this.nome = nome;
    this.idade = idade;
    this.telefone = telefone;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getNome() {
    return nome;
  }

  public void setNome(String nome) {
    this.nome = nome;
  }

  public Integer getIdade() {
    return idade;
  }

  public void setIdade(Integer idade) {
    this.idade = idade;
  }

  public String getTelefone() {
    return telefone;
  }

  public void setTelefone(String telefone) {
    this.telefone = telefone;
  }

  public String toString() {
    return "ID: " + this.id + ", Nome: " + this.nome + ", Idade: " + this.idade + ", Telefone: " + this.telefone + "\n";
  }
}
