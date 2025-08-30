package EstruturaDadosII.collections;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TestePessoa {
  public static void main(String[] args) {
    // TODO Auto-generated method stub

    Scanner entrada = new Scanner(System.in);
    List<Pessoa> pessoas = new ArrayList<Pessoa>();
    int opc;

    pessoas.add(new Pessoa("Carlos", "05678977732", 67, 5000.0));
    pessoas.add(new Pessoa("Henrique", "11122233344", 34, 10000.0));

    do {
      System.out.println("Informe seu nome: ");
      String nome = entrada.nextLine();
      System.out.println(nome + ", informe seu CPF: ");
      String cpf = entrada.nextLine();
      System.out.println(nome + ", informe sua idade: ");
      Integer idade = entrada.nextInt();
      System.out.println(nome + ", informe seu salário (em R$): ");
      Double salario = entrada.nextDouble();

      Pessoa pessoa = new Pessoa(nome, cpf, idade, salario);
      pessoas.add(pessoa);

      System.out.println("Deseja continuar? (1- sim; 0- nao): ");
      opc = entrada.nextInt();

      entrada.nextLine();

    } while (opc == 1);

    // a) Buscar uma pessoa pelo CPF
    System.out.println("Informe o CPF a ser buscado");
    String cpfProcurado = entrada.nextLine();

    boolean achou = false;
    Pessoa pCpfEncontrado = null;
    Double somaIdades = 0.0;
    Double somaSalarios = 0.0;
    for (Pessoa p : pessoas) {
      if (p.getCpf().equalsIgnoreCase(cpfProcurado)) {
        achou = true;
        pCpfEncontrado = p;
      }

      somaIdades += p.getIdade();
      somaSalarios += p.getSalario();
    }
    if (!achou) {
      System.out.println(cpfProcurado + ", não existe na lista de pessoas");
    } else {
      System.out.println(cpfProcurado + ", existe na lista de pessoas");
    }

    // b) qual posição meu elemento está armazenado?

    System.out.println(pessoas.indexOf(pCpfEncontrado));

    // c) remover o elemento do cpf encontrado

    pessoas.remove(pessoas.indexOf(pCpfEncontrado));

    // d) qual a média de Idades e Salarios das pessoas na lista?

    Double mediaIdades = somaIdades / pessoas.size();
    Double mediaSalarios = somaSalarios / pessoas.size();

  }

}