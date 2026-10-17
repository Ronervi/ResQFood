package vista;

import excepcion.ResQFoodException;
import java.util.NoSuchElementException;
import modelo.Administrador;
import modelo.Cuenta;
import modelo.Establecimiento;
import modelo.Rol;
import modelo.Usuario;
import servicio.AutenticacionService;

/**
 * Punto de entrada del programa: registro e inicio de sesión. 
 */
public class MenuPrincipal {
    
    private static final AutenticacionService AUTENTICACION = new AutenticacionService();
    
    public static void main(String[] args) {
        System.out.println("******************************");
        System.out.println("   ResQFood - Alimentos que   ");
        System.out.println("   valen la pena rescatar   ");
        System.out.println("******************************");
        
        boolean salir = false;
        try {
            while (!salir) {
                Consola.titulo("MENÚ PRINCIPAL");
                System.out.println("1. Iniciar sesión");
                System.out.println("2. Registrarme como usuario");
                System.out.println("3. Registrar mi establecimiento");
                System.out.println("0. Salir");
                int opcion = Consola.leerEntero("Elegí una opción: ");
                
                try {
                    switch (opcion) {
                        case 1:
                            iniciarSesion();
                            break;
                        case 2:
                            registrarUsuario();
                            break;
                        case 3:
                            registrarEstablecimiento();
                            break;
                        case 0:
                            salir = true;
                            break;
                        default:
                            System.out.println("[!] Opción inexistente. Elegí una de la lista.");
                    }
                } catch (IllegalArgumentException e) {
                    System.out.println("[!] Dato inválido: " + e.getMessage());
                } catch (ResQFoodException e) {
                    System.out.println("[!] " + e.getMessage());
                }
            }
        } catch (NoSuchElementException e) {
            System.out.println();
            System.out.println("Se cerró la entrada de datos.");
        }
        System.out.println("¡Hasta pronto!");
    }
    
    private static void iniciarSesion() throws ResQFoodException {
        System.out.println("Ingresar como:");
        System.out.println("1. Usuario");
        System.out.println("2. Establecimiento");
        System.out.println("3. Administrador");
        int opcionRol = Consola.leerEntero("Elegí una opción: ");
        
        Rol rol;
        switch (opcionRol) {
            case 1:
                rol = Rol.USUARIO;
                break;
            case 2:
                rol = Rol.ESTABLECIMIENTO;
                break;
            case 3:
                rol = Rol.ADMINISTRADOR;
                break;
            default:
                System.out.println("[!] Opción inexistente.");
                return;
        }
        
        String email = Consola.leerTexto("Email: ");
        String contraseña = Consola.leerTexto("Contraseña: ");
        Cuenta cuenta = AUTENTICACION.iniciarSesion(rol, email, contraseña);
        System.out.println("¡Bienvenido/a, " + cuenta.getNombreCompleto() + "!");
        
        // POLIMORFISMO: según el tipo real de la cuenta se abre el menú que le corresponde.
        Menu menu;
        switch (cuenta.getRol()) {
            case USUARIO:
                menu = new MenuUsuario((Usuario) cuenta);
                break;
            case ESTABLECIMIENTO:
                menu = new MenuEstablecimiento((Establecimiento) cuenta);
                break;
            default:
                menu = new MenuAdministrador((Administrador) cuenta);
        }
        menu.ejecutar();
    }
    
    private static void registrarUsuario() throws ResQFoodException {
        Consola.titulo("REGISTRO DE USUARIO");
        String nombre = Consola.leerTexto("Nombre: ");
        String apellido = Consola.leerTexto("Apellido: ");
        String email = Consola.leerTexto("Email: ");
        String telefono = Consola.leerTexto("Telefono (opcional): ");
        String contraseña = Consola.leerTexto("Contraseña (mínimo 6 caracteres): ");
        
        Usuario nuevo = AUTENTICACION.registrarUsuario(nombre, apellido, email, contraseña, telefono);
        System.out.println("Registro exitoso. Ya podés iniciar sesión, " + nuevo.getNombre() + ".");
    }
    
    private static void registrarEstablecimiento() throws ResQFoodException {
        Consola.titulo("REGISTRO DE ESTABLECIMIENTO");
        String nombre = Consola.leerTexto("Nombre del establecimiento: ");
        String direccion = Consola.leerTexto("Dirección: ");
        String ubicacion = Consola.leerTexto("Ubicacion / referencia (opcional): ");
        String telefono = Consola.leerTexto("Teléfono (opcional): ");
        String email = Consola.leerTexto("Email: ");
        String contraseña = Consola.leerTexto("Contraseña (mínimo 6 caracteres): ");
        
        AUTENTICACION.registrarEstablecimiento(nombre, direccion, telefono, email, contraseña, ubicacion);
        System.out.println("Solicitud enviada. Podrás ingresar cuando el administrador apruebe tu establecimiento.");
    }
}
