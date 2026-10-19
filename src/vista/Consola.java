package vista;

import java.util.Scanner;

/**
 * Utilidades para leer datos por consola de forma segura (sin que el programa se cierre ante errores).
 */
public final class Consola {
    
    private static final Scanner ENTRADA = new Scanner(System.in);
    
    private Consola(){
    }
    
    public static String leerTexto(String mensaje) {
        System.out.print(mensaje);
        return ENTRADA.nextLine().trim();
    }
    
    /** Repite la pregunta hasta que se ingrese un número entero válido. */
    public static int leerEntero(String mensaje) {
        while (true) {
            String texto = leerTexto(mensaje);
            try {
                return Integer.parseInt(texto);
            } catch (NumberFormatException e) {
                System.out.println("[!] Ingresá un número entero válido.");
            }
        }
    }
    
    /** Repite la pregunta hasta que se ingrese un número decimal válido (acepta coma o punto). */
    public static double leerDecimal(String mensaje) {
        while (true) {
            String texto = leerTexto(mensaje).replace(',', '.');
            try {
                return Double.parseDouble(texto);
            } catch (NumberFormatException e) {
                System.out.println("[!] Ingresá un número válido (por ejemplo 1500 o 12.5).");
            }
        }
    }
    
    public static boolean confirmar(String mensaje) {
        while (true) {
            String respuesta = leerTexto(mensaje + " (s/n): ").toLowerCase();
            if (respuesta.equals("s") || respuesta.equals("si") || respuesta.equals("sí")) {
                return true;
            }
            if (respuesta.equals("n") || respuesta.equals("no")) {
                return false;
            }
            System.out.println("[!] Respondé con 's' o 'n'.");
        }
    }
    
    public static void titulo(String texto) {
        System.out.println();
        System.out.println("=== " + texto + " ===");
    }
}
