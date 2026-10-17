package vista;

import excepcion.ResQFoodException;
import java.util.List;
import modelo.Calificacion;
import modelo.Publicacion;
import modelo.Reserva;
import modelo.Usuario;
import servicio.CalificacionService;
import servicio.PublicacionService;
import servicio.ReservaService;

/**
 * Munú del rol Usuario: consultar, buscar, reservar, cancelar y calificar.
 */
public class MenuUsuario extends Menu {
    
    private final Usuario usuario;
    private final PublicacionService publicacionService = new PublicacionService();
    private final ReservaService reservaService = new ReservaService();
    private final CalificacionService calificacionService = new CalificacionService();
    
    public MenuUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
    
    @Override
    protected String titulo() {
        return "MENÚ USUARIO - " + usuario.getNombreCompleto();
    }
    
    @Override
    protected void mostrarOpciones() {
        System.out.println("1. Ver alimentos disponibles");
        System.out.println("2. Buscar alimentos por nombre");
        System.out.println("3. Ver alimentos ordenados por precio final");
        System.out.println("4. Ver alimentos ordenados por fecha de vencimiento");
        System.out.println("5. Ver detalle de un alimento por ID");
        System.out.println("6. Reservar un alimento");
        System.out.println("7. Cancelar una reserva");
        System.out.println("8. Mi historial de reservas");
        System.out.println("9. Calificar un establecimiento");
        System.out.println("10. Ver calificaciones de un establecimiento");
        System.out.println("0. Cerrar sesión");
    }
    
    @Override
    protected boolean procesarOpcion(int opcion) throws ResQFoodException {
        switch (opcion) {
            case 1:
                mostrarPublicaciones(publicacionService.listarDisponibles());
                break;
            case 2:
                String texto = Consola.leerTexto("Nombre a buscar: ");
                mostrarPublicaciones(publicacionService.buscarPorNombre(texto));
                break;
            case 3:
                mostrarPublicaciones(publicacionService.listarOrdenadasPorPrecio());
                break;
            case 4:
                mostrarPublicaciones(publicacionService.listarOrdenadasPorVencimiento());
                break;
            case 5:
                int idDetalle = Consola.leerEntero("ID del alimento: ");
                Publicacion detalle = publicacionService.buscarDisponiblePorId(idDetalle);
                System.out.println(detalle.resumen());
                if (!detalle.getDescripcion().isEmpty()) {
                    System.out.println("    Descripción: " + detalle.getDescripcion());
                }
                break;
            case 6:
                reservar();
                break;
            case 7:
                cancelar();
                break;
            case 8:
                mostrarReservas(reservaService.historial(usuario));
                break;
            case 9:
                calificar();
                break;
            case 10:
                verCalificaciones();
                break;
            case 0:
                System.out.println("Sesión cerrada.");
                return false;
            default:
                opcionInvalida();
        }
        return false;
    }
    
    private void mostrarPublicaciones(List<Publicacion> lista) {
        if (lista.isEmpty()) {
            System.out.println("No hay alimentos para mostrar.");
            return;
        }
        for (Publicacion p : lista) {
            System.out.println(p.resumen());
        }
    }
    
    private void mostrarReservas(List<Reserva> lista) {
        if (lista.isEmpty()) {
            System.out.println("No hay reservas para mostrar.");
            return;
        }
        for (Reserva r : lista) {
            System.out.println(r.resumen());
        }
    }
    
    private void reservar() throws ResQFoodException {
        mostrarPublicaciones(publicacionService.listarDisponibles());
        int idPublicacion = Consola.leerEntero("ID del alimento a reservar (0 para volver): ");
        if (idPublicacion == 0) {
            return;
        }
        int cantidad = Consola.leerEntero("Cantidad: ");
        Reserva reserva = reservaService.reservar(usuario, idPublicacion, cantidad);
        System.out.println("Reserva realizada con éxito.");
        System.out.println(reserva.resumen());
    }
    
    private void cancelar() throws ResQFoodException {
        mostrarReservas(reservaService.historial(usuario));
        int idReserva = Consola.leerEntero("ID de la reserva a cancelar (0 para volver): ");
        if (idReserva == 0) {
            return;
        }
        if (Consola.confirmar("¿Confirmás la cancelación?")) {
            reservaService.cancelar(usuario, idReserva);
            System.out.println("Reserva cancelada. Las unidades volvieron a estar disponibles.");
        }
    }
    
    private void calificar() throws ResQFoodException {
        mostrarReservas(reservaService.historial(usuario));
        int idReserva = Consola.leerEntero("ID de la reseerva retirada que querés calificar (0 para volver): ");
        if (idReserva == 0) {
            return;
        }
        int puntuacion = Consola.leerEntero("Puntuacion (1 a 5): ");
        String comentario = Consola.leerTexto("Comentario (opcional): ");
        calificacionService.calificar(usuario, idReserva, puntuacion, comentario);
        System.out.println("¡Gracias por tu calificación!");
    }
    
    private void verCalificaciones() throws ResQFoodException {
        int idEstablecimiento = Consola.leerEntero("ID del establecimiento: ");
        List<Calificacion> lista = calificacionService.listarDe(idEstablecimiento);
        if (lista.isEmpty()) {
            System.out.println("Ese establecimiento todavía no tiene calificaciones.");
            return;
        }
        System.out.printf("Promedio: %.1f / 5 (%d calificaciones)%n",
                calificacionService.promedioDe(idEstablecimiento), lista.size());
        for (Calificacion c : lista) {
            System.out.println(" - " + c);
        }
    }   
}
