package EstruturaDadosII.collections;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TestePessoa {
  public static void main(String[] args) {
    Scanner entrada = new Scanner(System.in);
    
    // Lista para armazenar objetos do tipo Pessoa
    List<Pessoa> pessoas = new ArrayList<>();

    int opc;

    do {
      System.out.println("Informe seu nome: ");
      String nome = entrada.nextLine();
      
      System.out.println("Informe seu cpf: ");
      String cpf = entrada.nextLine();
      
      System.out.println("Informe sua idade: ");
      Integer idade = entrada.nextInt(); // Use nextInt() para ler um número
      entrada.nextLine(); // Consome a quebra de linha

      System.out.println("Informe seu salario: ");
      Double salario = entrada.nextDouble(); // Use nextDouble() para ler um double
      entrada.nextLine(); // Consome a quebra de linha

      // Instancia um novo objeto Pessoa com os dados lidos
      Pessoa p = new Pessoa(nome, cpf, idade, salario);

      // Adiciona o objeto 'p' à lista 'pessoas'
      pessoas.add(p);

      System.out.println("Deseja continuar? (1 - sim, 2 - nao)");
      opc = entrada.nextInt(); // Lê a opção do usuário
      entrada.nextLine(); // Consome a quebra de linha
    } while (opc == 1);

    // Imprime a lista de pessoas
    System.out.println(pessoas);
    
    // Fechar o scanner para evitar vazamento de recursos
    entrada.close();
  }
}