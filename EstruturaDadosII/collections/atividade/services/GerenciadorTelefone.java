package EstruturaDadosII.collections.atividade.services;

import java.util.ArrayList;
import java.util.List;

import EstruturaDadosII.collections.atividade.models.Telefone;

public class GerenciadorTelefone {

  private List<Telefone> telefones;

  public GerenciadorTelefone() {
    this.telefones = new ArrayList<>();
  }

  public void adicionarTelefone(Integer id, String ddd, String numero) {
    Telefone telefone = new Telefone(id, ddd, numero);
    telefones.add(telefone);
  }

  public boolean telefoneExiste(Integer id) {
    for (Telefone telefone : telefones) {
      if (telefone.getId().equals(id)) {
        return true;
      }
    }
    return false;
  }

  public Telefone buscarTelefonePorId(Integer id) {
    for (Telefone telefone : telefones) {
      if (telefone.getId().equals(id)) {
        return telefone;
      }
    }
    return null;
  }

  public void listarTelefones() {
    if (telefones.isEmpty()) {
      System.out.println("Nenhum telefone cadastrado.");
      return;
    }

    System.out.println("\n=== LISTA DE TELEFONES ===");
    for (Telefone telefone : telefones) {
      System.out.println(telefone.toString());
    }
  }

  public boolean removerTelefone(Integer id) {
    Telefone telefone = buscarTelefonePorId(id);
    if (telefone != null) {
      telefones.remove(telefone);
      return true;
    }
    return false;
  }

  public List<Telefone> getTelefones() {
    return telefones;
  }
}
