package vista;

import excepcion.ResQFoodException;
import java.util.List;
import modelo.Administrador;
import modelo.Establecimiento;
import modelo.Publicacion;
import modelo.Usuario;
import servicio.AdministracionService;
import servicio.PublicacionService;

/**
 * Menú del rol Administrador: moderación de establecimientos, usuarios y publicaciones.
 */
public class MenuAdministrador extends Menu {
    
    private final Administrador administrador;
    private final AdministracionService administracionService = new AdministracionService();
    private final PublicacionService publicacionService = new PublicacionService();
    
    public MenuAdministrador(Administrador administrador) {
        this.administrador = administrador;
    }
    
    @Override
    protected String titulo() {
        return "MENÚ ADMINISTRADOR - " + administrador.getNombre();
    }
    
    @Override
    protected void mostrarOpciones() {
        System.out.println("1. Revisar establecimientos pendientes (aprobar / rechazar)");
        System.out.println("2. Listar usuario");
        System.out.println("3. Bloquear un usuario");
        System.out.println("4. Reactivar un usuario");
        System.out.println("5. Listar todas las publicaciones");
        System.out.println("6. Eliminar una publicación");
        System.out.println("0. Cerrar sesión");
    }
    
    @Override
    protected boolean procesarOpcion(int opcion) throws ResQFoodException {
        switch (opcion) {
            case 1:
                revisarPendientes();
                break;
            case 2:
                for (Usuario u : administracionService.listarUsuarios()) {
                    System.out.println(u);
                }
                break;
            case 3:
                administracionService.bloquearUsuario(Consola.leerEntero("ID del usuario a bloquear: "));
                System.out.println("Usuario bloqueado.");
                break;
            case 4:
                administracionService.activarUsuario(Consola.leerEntero("ID del usuario a reactivar: "));
                System.out.println("Usuario reactivado.");
                break;
            case 5:
                List<Publicacion> todas = publicacionService.listarTodas();
                if (todas.isEmpty()) {
                    System.out.println("No hay publicaciones.");
                }
                for (Publicacion p : todas) {
                    System.out.println(p.resumen());
                }
                break;
            case 6:
                int id = Consola.leerEntero("ID de la publicación a eliminar: ");
                if (Consola.confirmar("¿Seguro que querés eliminarla?")) {
                    publicacionService.eliminarComoAdministrador(id);
                    System.out.println("Publicacion eliminada.");
                }
                break;
            case 0:
                System.out.println("Sesión cerrada.");
                return false;
            default:
                opcionInvalida();
        }
        return true;
    }
    
    private void revisarPendientes() throws ResQFoodException {
        List<Establecimiento> pendientes = administracionService.listarPendientes();
        if (pendientes.isEmpty()) {
            System.out.println("No hay establecimientos pendientes de aprobación.");
            return;
        }
        for (Establecimiento e : pendientes){
            System.out.println(e);
        }
        int id = Consola.leerEntero("ID des establecimiento a evaluar (0 para volver): ");
        if (id == 0) {
            return;
        }
        String decision = Consola.leerTexto("¿Aprobar (A) o rechazar (R): ").toUpperCase();
        if (decision.equals("A")) {
            administracionService.aprobarEstablecimiento(id);
            System.out.println("Establecimiento aprobado.");
        } else if (decision.equals("R")) {
            administracionService.rechazarEstablecimiento(id);
            System.out.println("Establecimiento rechazado.");
        } else {
            System.out.println("[!] Decisión inválida. No se realizaron cambios.");
        }
    }
}
