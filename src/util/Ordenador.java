package util;

import java.util.Comparator;
import java.util.List;

/**
 * Algoritmos de ordenación implementados a mano.
 */
public final class Ordenador {
    
    private Ordenador() {
    }
    
    /**
     * Ordenación por inserción: recorre la lista y va insertando cada elemento
     * en su posición correcta dentro de la parte que ya quedó ordenada.
     * El criterio de orden lo define el Comparator recibido.
     */
    public static <T> void insercion(List<T> lista, Comparator<? super T> criterio) {
        for (int i = 1; i < lista.size(); i++) {
            T actual = lista.get(i);
            int j = i - 1;
            while (j >= 0 && criterio.compare(lista.get(i), actual) > 0) {
                lista.set(j + 1, lista.get(j));
                j--;
            }
            lista.set(j + 1, actual);
        }
    }
}
