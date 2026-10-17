package util;

import java.util.ArrayList;
import java.util.List;
import modelo.Publicacion;

/**
 * Algoritmos de búsqueda implementados a mano.
 */
public final class Buscador {
    
    private Buscador() {
    }
    
    /** Búsqueda lineal: recorre toda la lista y devuelve las publicaciones cuyo nombre contiene el texto. */
    public static List<Publicacion> porNombre(List<Publicacion> lista, String texto) {
        List<Publicacion> resultado = new ArrayList<>();
        String buscado = texto.trim().toLowerCase();
        for (Publicacion p : lista) {
            if (p.getNombre().toLowerCase().contains(buscado)) {
                resultado.add(p);
            }
        }
        return resultado;
    }
    
    /**
     * Búsqueda binaria por id. Requiere que la lista esté ORDENADA por idPublicacion
     * (la consulta SQL ya la devuelve así). Devuelve null si no lo encuentra.
     */
    public static Publicacion binariaPorId(List<Publicacion> listaOrdenada, int id) {
        int inicio = 0;
        int fin = listaOrdenada.size() - 1;
        while (inicio <= fin) {
            int medio = (inicio + fin) / 2;
            int idMedio = listaOrdenada.get(medio).getIdPublicacion();
            if (idMedio == id) {
                return listaOrdenada.get(medio);
            } else if (idMedio < id) {
                inicio = medio +1;
            } else {
                fin = medio -1;
            }
        }
        return null;
    }
}
