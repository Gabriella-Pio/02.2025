package EstruturaDadosII.collections.atividade.ex_5;

import java.util.List;

public class Agenda {
  List<Contato> contatos;

  public Agenda(List<Contato> contatos) {
    this.contatos = contatos;
  }

  public List<Contato> getContatos() {
    return contatos;
  }

  public void setContatos(List<Contato> contatos) {
    this.contatos = contatos;
  }

  public void adicionarContato(Contato contato) {
    this.contatos.add(contato);
  }

  public boolean contatoExistente(String nome) {
    for (Contato contato : contatos) {
      if (contato.getNome().equalsIgnoreCase(nome)) {
        return true;
      }
    }
    return false;
  }

  public void listarContatos() {
    if (contatos.isEmpty()) {
      System.out.println("Nenhum contato cadastrado.");
      return;
    }

    System.out.println("\n=== LISTA DE CONTATOS ===");
    for (Contato contato : contatos) {
      System.out.println(contato.toString());
    }
  }

  public Contato buscarContatoPorNome(String nome) {
    for (Contato contato : contatos) {
      if (contato.getNome().equalsIgnoreCase(nome)) {
        return contato;
      }
    }
    return null;
  }

  public boolean removerContatoPorNome(String nome) {
    Contato contato = buscarContatoPorNome(nome);
    if (contato != null) {
      contatos.remove(contato);
      return true;
    }
    return false;
  }

}
