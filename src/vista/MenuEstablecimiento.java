package vista;

import excepcion.ResQFoodException;
import java.util.List;
import java.util.Queue;
import modelo.Calificacion;
import modelo.Establecimiento;
import modelo.Publicacion;
import modelo.PublicacionDescuento;
import modelo.Reserva;
import servicio.CalificacionService;
import servicio.PublicacionService;
import servicio.ReservaService;

/**
 * Menú del rol Establecimiento: publicar alimentos y gestionar las reservas recibidas.
 */
public class MenuEstablecimiento extends Menu {
    
    private final Establecimiento establecimiento;
    private final PublicacionService publicacionService = new PublicacionService();
    private final ReservaService reservaService = new ReservaService();
    private final CalificacionService calificacionService = new CalificacionService();
    
    public MenuEstablecimiento(Establecimiento establecimiento) {
        this.establecimiento = establecimiento;
    }
    
    @Override
    protected String titulo() {
        return "MENÚ ESTABLECIMIENTO - " + establecimiento.getNombre();
    }
    
    @Override
    protected void mostrarOpciones() {
        System.out.println("1. Publicar alimento excedente");
        System.out.println("2. Ver mis publicaciones");
        System.out.println("3. Modificar una publicación");
        System.out.println("4. Eliminar una publicación");
        System.out.println("5. Ver reservas pendientes");
        System.out.println("6. Atender la siguiente reserva (cola por orden de llegada)");
        System.out.println("7. Confirmar retiro de reserva por ID");
        System.out.println("8. Ver mis calificaciones");
        System.out.println("0. Cerrar sesión");
    }
    
    @Override
    protected boolean procesarOpcion(int opcion) throws ResQFoodException {
        switch (opcion) {
            case 1:
                publicar();
                break;
            case 2:
                mostrarPublicaciones(publicacionService.listarPropias(establecimiento));
                break;
            case 3:
               modificar();
               break;
            case 4:
                eliminar();
                break;
            case 5:
                mostrarReservas(reservaService.pendientesDe(establecimiento));
                break;
            case 6:
                atenderSiguiente();
                break;
            case 7:
                int idReserva = Consola.leerEntero("ID de la reserva retirada: ");
                reservaService.confirmarRetiro(establecimiento, idReserva);
                System.out.println("Retiro confirmado.");
                break;
            case 8:
                verCalificaciones();
                break;
            case 0:
                System.out.println("Sesión cerrada.");
                return false;
            default:
                opcionInvalida();
        }
        return true;
    }
    
    private void mostrarPublicaciones(List<Publicacion> lista) {
        if (lista.isEmpty()) {
            System.out.println("No tenés publicaciones.");
            return;
        }
        for (Publicacion p : lista) {
            System.out.println(p.resumen());
        }
    }
    
    private void mostrarReservas(List<Reserva> lista) {
        if (lista.isEmpty()) {
            System.out.println("No hay reservas pendientes.");
            return;
        }
        for (Reserva r : lista) {
            System.out.println(r.resumen());
        }
    }
    
    private void publicar() throws ResQFoodException {
        String nombre = Consola.leerTexto("Nombre del alimento: ");
        String descripcion = Consola.leerTexto("Descripcion: ");
        int cantidad = Consola.leerEntero("Cantidad de unidades: ");
        boolean gratuita = Consola.confirmar("¿Es gratuito?");
        double precio = 0;
        double descuento = 100;
        if (!gratuita) {
            precio = Consola.leerDecimal("Precio original: $");
            descuento = Consola.leerDecimal("Descuenro (0 a 100%): ");
        }
        String vencimiento = Consola.leerTexto("Fecha de vencimiento (AAAA-MM-DD): ");
        String horario = Consola.leerTexto("Horario de retiro (HH:MM-HH:MM): ");
        
        Publicacion creada = publicacionService.publicar(establecimiento, nombre, descripcion,
                cantidad, gratuita, precio, descuento, vencimiento, horario);
        System.out.println("Publicación creada:");
        System.out.println(creada.resumen());
    }
    
    private void modificar() throws ResQFoodException {
        List<Publicacion> propias = publicacionService.listarPropias(establecimiento);
        mostrarPublicaciones(propias);
        if (propias.isEmpty()) {
            return;
        }
        int id = Consola.leerEntero("ID de la publicación a modificar (0 para volver): ");
        if (id == 0) {
            return;
        }
        Publicacion actual = null;
        for (Publicacion p : propias) {
            if (p.getIdPublicacion() == id) {
                actual = p;
            }
        }
        if (actual == null) {
            System.out.println("[!] Esa publicación no es tuya o no existe.");
            return;
        }
        int cantidad = Consola.leerEntero("Nueva cantidad (0 la marca como agotada): ");
        String horario = Consola.leerTexto("Nuevo horario de retiro (HH:MM-HH:MM): ");
        double precio = actual.getPrecio();
        double descuento = actual.getDescuento();
        if (actual instanceof PublicacionDescuento) {
            precio = Consola.leerDecimal("Nuevo precio original: $");
            descuento = Consola.leerDecimal("Nuevo descuento (0 a 100%): ");
        }
        publicacionService.modificar(establecimiento, id, cantidad, precio, descuento, horario);
        System.out.println("Publicación actualizada.");
    }
    
    private void eliminar() throws ResQFoodException {
        mostrarPublicaciones(publicacionService.listarPropias(establecimiento));
        int id = Consola.leerEntero("ID de la publicación a eliminar (0 para volver): ");
        if (id == 0) {
            return;
        }
        if (Consola.confirmar("¿Segura que querés eliminarla?")) {
            publicacionService.eliminar(establecimiento, id);
            System.out.println("Publicación eliminada.");
        }
    }
    
    /** Usa una cola: la reserva que llegó primero es la primera en atenderse. */
    private void atenderSiguiente() throws ResQFoodException {
        Queue<Reserva> cola = reservaService.colaDeRetiros(establecimiento);
        System.out.println("Reservas en cola: " + cola.size());
        Reserva siguiente = cola.peek();
        if (siguiente == null) {
            System.out.println("No hay reservas pendientes.");
            return;
        }
        System.out.println("Siguiente en la cola:");
        System.out.println(siguiente.resumen());
        if (Consola.confirmar("¿El cliente retiró el pedido?")) {
            reservaService.confirmarRetiro(establecimiento, siguiente.getIdReserva());
            cola.poll();
            System.out.println("Retiro confirmado. Quedan " + cola.size() + " reservas en cola.");
        }
    }
    
    private void verCalificaciones() throws ResQFoodException {
        List<Calificacion> lista = calificacionService.listarDe(establecimiento.getId());
        if (lista.isEmpty()) {
            System.out.println("Todavía no recibiste calificaciones.");
            return;
        }
        System.out.printf("Promedio: %.1f / 5 (%d calificaciones)%n",
                calificacionService.promedioDe(establecimiento.getId()), lista.size());
        for (Calificacion c : lista) {
            System.out.println(" - " + c);
        }
    }
}
