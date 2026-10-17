package util;

/**
 *
 * 
 */
public class GenerarHash {
    public static void main(String[] args) {
        String hash = Seguridad.hasher("123456");
        System.out.println(hash);
    }
}
