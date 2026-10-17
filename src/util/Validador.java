package util;

import excepcion.DatoInvalidoException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.regex.Pattern;

/**
 * Validaciones reutilizables.
 * Lanzan DatoInvalidoException si el dato no es válido.
 */
public final class Validador {
    
    private static final Pattern EMAIL =
            Pattern.compile("^[\\w.+-]+@[\\w-]+(\\.[\\w-]+)+$");
    private static final Pattern HORARIO =
            Pattern.compile("^([01]\\d|2[0-3]):[0-5]\\d-([01]\\d|2[0-3]):[0-5]\\d$");
    
    private Validador() {
    }
    
    /** Exige texto no vacío y con una longitud máxima. Devuelve el texto sin espacios sobrantes. */
    public static String texto(String valor, String campo, int max) {
        if (valor == null || valor.trim().isEmpty()) {
            throw new DatoInvalidoException("El campo '" + campo + "' es obligatorio.");
        }
        String limpio = valor.trim();
        if (limpio.length() > max) {
            throw new DatoInvalidoException("El campo '" + campo
            + "' no puede superar los " + max + "caracteres.");
        }
        return limpio;
    }
    
    /* Texto opcional: acepta null o vacío, pero respeta la longitud máxima. */
    public static String textoopcional(String valor, String campo, int max) {
        if (valor == null) {
            return "";
        }
        String limpio = valor.trim();
        if (limpio.length() > max) {
            throw new DatoInvalidoException("El campo '" + campo
            + "' no puede superar los " + max + " caracteres.");
        }
        return limpio;
    }
    
    public static String email(String valor) {
        String limpio = texto(valor, "email", 100);
        if (!EMAIL.matcher(limpio).matches()) {
            throw new DatoInvalidoException("El email ingresado no tiene un formato válido.");
        }
        return limpio.toLowerCase();
    }
    
    /** Valida la contraseña EN TEXTO PLANO antes de hashesarla. */
    public static String contrasena(String valor) {
        if (valor == null || valor.length() < 6) {
            throw new DatoInvalidoException("La contraseña debe tener al menos 6 caracteres.");
        }
        if (valor.length() > 60)  {
            throw new DatoInvalidoException("La contraseña no puede superar los 60 caracteres.");
        }
        return valor;
    }
    
    public static int enteroPositivo(int valor, String campo) {
        if (valor <= 0) {
            throw new DatoInvalidoException("El campo '" + campo + "' debe ser mayor que cero.");
        }
        return valor;
    }
    
    public static int enteroNoNegativo(int valor, String campo) {
        if (valor < 0) {
            throw new DatoInvalidoException("El campo '" + campo + "' no puede ser negativo.");
        }
        return valor;
    }
    
    public static double decimalNoNegativo(double valor, String campo) {
        if (valor < 0 || Double.isNaN(valor)) {
            throw new DatoInvalidoException("El campo '" + campo + "' no puede ser negativo.");
        }
        return valor;
    }
    
    public static double porcentaje(double valor, String campo) {
        if (valor < 0 || valor > 100 || Double.isNaN(valor)) {
            throw new DatoInvalidoException("El campo '" + campo + "' debe estar entre 0 y 10.");
        }
        return valor;
    }
    
    public static int rango(int valor, int min, int max, String campo) {
        if (valor < min || valor > max) {
            throw new DatoInvalidoException("El campo '" + campo 
            + "' debe estar entre " + min + " y " + max + ".");
        }
        return valor;
    }
    
    /**Valida una fecha con formato yyyy-mm-dd (por ejemplo 2026-10-25). */
    public static LocalDate fecha(String valor, String campo) {
        if (valor == null || valor.trim().isEmpty()) {
            throw new DatoInvalidoException("El campo '" + campo + "' es obligatorio.");
        }
        try {
            return LocalDate.parse(valor.trim());
        } catch (DateTimeParseException e) {
            throw new DatoInvalidoException("El campo '" + campo
             + "' debe tener el formato AAAA-MM-DD (por ejemplo: 2026-10-25).");
        }
    }
    
    /** Valida un horario con formato HH:mm-HH:mm (por ejemplo 18:00-20:00). */
    public static String horario(String valor) {
        String limpio = texto(valor, "horario de retiro", 50);
        if (!HORARIO.matcher(limpio).matches()) {
            throw new DatoInvalidoException(
            "El horario de retiro debe tener el formato HH:MM-HH:MM (por ejemplo: 18:00-20:00).");
        }
        return limpio;
    }
    
    /** Devuelve el valor, o el valor por defecto si viene null o vacío (útil al leer de la BD): */
    public static String valorOPorDefecto(String valor, String porDefecto) {
        return (valor == null || valor.trim().isEmpty()) ? porDefecto : valor;
    }
}
