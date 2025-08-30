package EstruturaDadosII.collections.atividade.models;

public class Telefone {
  private Integer id;
  private String ddd;
  private String numero;

  public Telefone(Integer id, String ddd, String numero) {
    this.id = id;
    this.ddd = ddd;
    this.numero = numero;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getDdd() {
    return ddd;
  }

  public void setDdd(String ddd) {
    this.ddd = ddd;
  }

  public String getNumero() {
    return numero;
  }

  public void setNumero(String numero) {
    this.numero = numero;
  }

  // Método para validar DDD
  public static boolean validarDDD(String ddd) {
    return ddd != null && ddd.matches("\\d{2}");
  }

  // Método para validar número de telefone
  public static boolean validarNumero(String numero) {
    return numero != null && numero.matches("\\d{4,5}-?\\d{4}");
  }

  public String toString() {
    return this.id + " - (" + this.ddd + ") " + this.numero;
  }
}