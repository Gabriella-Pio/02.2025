package EstruturaDadosII.recursividade;

public class Fatorial {
    public static void main(String[] args) {
        System.out.println(fatRecursivo(5));
    }

    public static int fatIterativo(int  n) {
        int acum = 1;
        for(int i = 1; i <= n; i++) {
            acum *= i;
        }
        return acum;
    }

    public static int fatRecursivo(int n) {
        if(n <= 1) {
            return 1;
        }
        else {
            System.out.println(n);
            return n * fatRecursivo(n - 1);
        }
    }
}
