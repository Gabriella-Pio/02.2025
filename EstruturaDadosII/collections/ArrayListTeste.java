package EstruturaDadosII.collections;

import java.util.ArrayList;
import java.util.List;

public class ArrayListTeste {
  public static void main(String args[]) {

    List<String> nomes = new ArrayList<String>();

    nomes.add("Eduardo");
    nomes.add("Joyce");
    nomes.add("Carlos");
    nomes.add("Henrique");

    String nomeProcurado = "Rita";

    if (nomes.isEmpty()) {
      System.out.println("A lista está vazia!");
    } else {
      System.out.println("A lista tem tamanho: " + nomes.size());
      // boolean achou = false;
      // for(String s : nomes) {
      // if(s.equalsIgnoreCase(nomeProcurado)) {
      // achou = true;
      // }
      // }
      // if(achou == true) {
      // System.out.println(nomeProcurado + " está na lista");
      // }
      // else {
      // System.out.println(nomeProcurado + " não está na lista.");
      // }

      if (nomes.contains(nomeProcurado)) {
        System.out.println(nomeProcurado + " está na lista");
      } else {
        System.out.println(nomeProcurado + " não está na lista.");
      }

      nomes.set(1, "Marcos");

    }

  }
}
