package EstruturaDadosII.collections.atividade.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import EstruturaDadosII.collections.atividade.models.Cliente;

public class GerenciadorCliente {
  private Scanner scanner = new Scanner(System.in);

  private List<Cliente> clientes;

  public GerenciadorCliente() {
    this.clientes = new ArrayList<>();
  }

  public void adicionarCliente(Integer id, String nome, Integer idade, String telefone) {
    Cliente cliente = new Cliente(id, nome, idade, telefone);
    clientes.add(cliente);
  }

  public boolean clienteExiste(Integer id) {
    for (Cliente cliente : clientes) {
      if (cliente.getId().equals(id)) {
        return true;
      }
    }
    return false;
  }

  public void listarClientes() {
    if (clientes.isEmpty()) {
      System.out.println("Nenhum cliente cadastrado.");
      return;
    }

    System.out.println("\n=== LISTA DE CLIENTES ===");
    for (Cliente cliente : clientes) {
      System.out.println(cliente.toString());
    }
  }

  public List<Cliente> getClientes() {
    return clientes;
  }
}
