package EstruturaDadosII.collections.atividade;

import java.util.Scanner;
import EstruturaDadosII.collections.atividade.menus.MenuPrincipal;

public class main {
  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    MenuPrincipal menuPrincipal = new MenuPrincipal(scanner);

    System.out.println("=== SISTEMA DE GERENCIAMENTO ===");
    menuPrincipal.executar();

    scanner.close();
  }
}
