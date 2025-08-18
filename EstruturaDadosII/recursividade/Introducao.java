package EstruturaDadosII.recursividade;

public class Introducao {
    public static void main(String[] args) {
        olaMundo(5);
    }
    
    public static void olaMundo(int i) {
        if(i>=1) {
            System.out.println("Hello world");
            olaMundo(i-1);
        }
    }
}
