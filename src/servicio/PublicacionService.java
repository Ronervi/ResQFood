package servicio;

import dao.PublicacionDAO;
import dao.ReservaDAO;
import excepcion.DatoInvalidoException;
import excepcion.OperacionNoPermitidaException;
import excepcion.RecursoNoEncontradoException;
import excepcion.ResQFoodException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import modelo.Establecimiento;
import modelo.Estados;
import modelo.Publicacion;
import modelo.PublicacionDescuento;
import modelo.PublicacionGratuita;
import util.Buscador;
import util.Fechas;
import util.Ordenador;
import util.Validador;

/**
 * Reglas de negocio de las publicaciones de alimentos excedentes.
 */
public class PublicacionService {
    
    private final PublicacionDAO publicacionDAO = new PublicacionDAO();
    private final ReservaDAO reservaDAO = new ReservaDAO();
    
    // ---- Establecimiento ----
    
    public Publicacion publicar(Establecimiento establecimiento, String nombre, String descripcion,
            int cantidad, boolean gratuita, double precio, double descuento,
            String fechaVencimiento, String horarioRetiro) throws ResQFoodException {
        
        Validador.enteroPositivo(cantidad, "cantidad");
        Validador.horario(horarioRetiro);
        LocalDate vencimiento = Validador.fecha(fechaVencimiento, "fechaVencimiento");
        if (vencimiento.isBefore(LocalDate.now())) {
            throw new DatoInvalidoException("La fecha de vencimiento no puede ser anterior a hoy.");
        }
        
        Publicacion publicacion;
        if (gratuita) {
            publicacion = new PublicacionGratuita(0, establecimiento.getId(), nombre, descripcion,
            cantidad, fechaVencimiento, Fechas.hoy(), horarioRetiro, Estados.DISPONIBLE);
        } else {
            if (precio <= 0) {
                throw new DatoInvalidoException("El precio original debe ser mayor que cero.");
            }
            publicacion = new PublicacionDescuento(0, establecimiento.getId(), nombre, descripcion,
            cantidad, precio, descuento, fechaVencimiento, Fechas.hoy(), horarioRetiro,
            Estados.DISPONIBLE);
        }
        publicacion.setIdPublicacion(publicacionDAO.insertar(publicacion));
        publicacion.setNombreEstablecimiento(establecimiento.getNombre());
        return publicacion;
    }
    
    public List<Publicacion> listarPropias(Establecimiento establecimiento) throws ResQFoodException {
        return publicacionDAO.listarPorEstablecimiento(establecimiento.getId());
    }
    
    /** Modifica cantidad y horario (y precio/descuento si NO es gratuita). */
    public void modificar(Establecimiento establecimiento, int idPublicacion, int cantidad,
            double precio, double descuento, String horarioRetiro) throws ResQFoodException {
        
        Publicacion publicacion = obtenerPropia(establecimiento, idPublicacion);
        
        publicacion.setCantidad(cantidad);
        publicacion.setHorarioRetiro(Validador.horario(horarioRetiro));
        if (!(publicacion instanceof PublicacionGratuita)) {
            if (precio <= 0) {
                throw new DatoInvalidoException("El precio original debe ser mayor que cero.");
            }
            publicacion.setPrecio(precio);
            publicacion.setDescuento(descuento);
        }
        publicacion.setEstado(cantidad == 0 ? Estados.AGOTADA : Estados.DISPONIBLE);
        publicacionDAO.actualizar(publicacion);
    }
    
   public void eliminar(Establecimiento establecimiento, int idPublicacion) throws ResQFoodException {
       obtenerPropia(establecimiento, idPublicacion);
       eliminarSinControlDePropietario(idPublicacion);
   }
   
   /** Uso del administrador: elimina cualquier publicacion que no tenga reservas. */
   public void eliminarComoAdministrador(int idPublicacion) throws ResQFoodException {
       if (publicacionDAO.buscarPorId(idPublicacion) == null) {
           throw new RecursoNoEncontradoException("La publicación #" + idPublicacion + " no existe.");
       }
       eliminarSinControlDePropietario(idPublicacion);
   }
   
   private void eliminarSinControlDePropietario(int idPublicacion) throws ResQFoodException {
       if (reservaDAO.contarPorPublicacion(idPublicacion) > 0) {
           throw new OperacionNoPermitidaException("La publicación tiene reservas asociadas y no se puede "
           + "eliminar. Podés ponerle cantidad 0 para que deje de mostrarse.");
       }
       publicacionDAO.eliminar(idPublicacion);
   }
   
   private Publicacion obtenerPropia(Establecimiento establecimiento, int idPublicacion)
           throws ResQFoodException {
       Publicacion publicacion = publicacionDAO.buscarPorId(idPublicacion);
       if (publicacion == null) {
           throw new RecursoNoEncontradoException("La publicación #" + idPublicacion + " no existe.");
       }
       if (publicacion.getIdEstablecimiento() != establecimiento.getId()) {
           throw new OperacionNoPermitidaException("Solo podés gestionar tus propias publicaciones.");
       }
       return publicacion;
   }
   
   // ---- Usuario: consulta, búsqueda y orden ----
   
   public List<Publicacion> listarDisponibles() throws ResQFoodException {
       return publicacionDAO.listarDisponibles();
   }
   
   /** Búsqueda lineal por nombre sobre las publicaciones disponibles. */
   public List<Publicacion> buscarPorNombre(String texto) throws ResQFoodException {
       Validador.texto(texto, "texto de búsqueda", 100);
       return Buscador.porNombre(publicacionDAO.listarDisponibles(), texto);
   }
   
   /** Búsqueda binaria por id sobre las publicaciones disponibles (ya ordenadas por id). */
   public Publicacion buscarDisponiblePorId(int idPublicacion) throws ResQFoodException {
       Publicacion encontrada = Buscador.binariaPorId(publicacionDAO.listarDisponibles(), idPublicacion);
       if (encontrada == null) {
           throw new RecursoNoEncontradoException(
           "No hay una publicacion disponible con el id #" + idPublicacion + ".");
       }
       return encontrada;
   }
   
   /** Ordena de menor a mayor precio final usando ordenación por inserción. */
   public List<Publicacion> listarOrdenadasPorPrecio() throws ResQFoodException {
       List<Publicacion> lista = new ArrayList<>(publicacionDAO.listarDisponibles());
       Ordenador.insercion(lista, new Comparator<Publicacion>() {
           @Override
           public int compare(Publicacion a, Publicacion b) {
               return Double.compare(a.calcularPrecioFinal(), b.calcularPrecioFinal());
           }
       });
       return lista;
   }
   
   /** Ordena por fecha de vencimiento (las que vencen antes, primero). */
   public List<Publicacion> listarOrdenadasPorVencimiento() throws ResQFoodException {
       List<Publicacion> lista = new ArrayList<>(publicacionDAO.listarDisponibles());
       Ordenador.insercion(lista, new Comparator<Publicacion>() {
           @Override
           public int compare(Publicacion a, Publicacion b) {
               return a.getFechaVencimiento().compareTo(b.getFechaVencimiento());
           }
       });
       return lista;
   }
   
   // ---- Administrador ----
   
   public List<Publicacion> listarTodas() throws ResQFoodException {
       return publicacionDAO.listarTodas();
   }
}
