package vista;

import excepcion.DatoInvalidoException;
import excepcion.ResQFoodException;

/**
 * ABSTRACCIÓN + HERENCIA: define cómo funciona cualquier menú del sistema
 * (mostrar opciones, leer una, ejecutarla y repetir hasta salir).
 * Cada rol implementa sus propias opciones en una subclase (POLIMORFISMO).
 */
public abstract class Menu {
    
    protected abstract String titulo();
    
    protected abstract void mostrarOpciones();
    
    /** Ejecuta la opción elegida. Devuelve false cuando hay que salir del menú. */
    protected abstract boolean procesarOpcion(int opcion) throws ResQFoodException;
    
    /** Bucle principal del menú. Atiende los errores sin cerrar el programa. */
    public final void ejecutar() {
        boolean continuar = true;
        while (continuar) {
            Consola.titulo(titulo());
            mostrarOpciones();
            int opcion = Consola.leerEntero("Elegí una opción: ");
            try {
                continuar = procesarOpcion(opcion);
            } catch (DatoInvalidoException e) {
                System.out.println("[!] Dato inválido: " + e.getMessage());
            } catch (ResQFoodException e) {
                System.out.println("[!] " + e.getMessage());
            }
        }
    }
    
    protected void opcionInvalida() {
        System.out.println("[!] Opción inexistente. Elegí una de la lista.");
    }
    
}
