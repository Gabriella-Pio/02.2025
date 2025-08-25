package EstruturaDadosII.collections;

import java.util.ArrayList;
import java.util.List;

public class ArrayListTeste {
  public static void main(String[] args) {
    List<String> nomes = new ArrayList<String>();

    nomes.add("Gabriella");
    nomes.add("Joyce");

    System.out.println(nomes);

    nomes.add(1, "Luiz");
    System.out.println(nomes);

    nomes.remove("Gabriella");
    System.out.println(nomes);

    nomes.remove(0);
    System.out.println(nomes);

    System.out.println(nomes.isEmpty());
    
    nomes.add("Carlos");
    nomes.add("Henrique");
    nomes.add("Eduardo");

    System.out.println(nomes);

    String nomeProcurado = "Luiz";

    if (nomes.isEmpty()) {
      System.out.println("Lista vazia");
    } else {
      boolean achou = false;
      for(String s : nomes) {
      System.out.println(s);
      if (s.equalsIgnoreCase(nomeProcurado)) {
        achou = true;
      }
    }
    if (achou == true) {
      System.out.println(nomeProcurado + " esta na lista");      
    } else {
      System.out.println(nomeProcurado + " n esta na lista");
    }
    }
    
  }
}
