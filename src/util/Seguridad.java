package util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 * Almacenamiento seguro de contraseñas (RNF04).
 * Usa PBKDF2 con HMAC-SHC256 y un "salt" aleatorio distinto por contraseña.
 * El resultado se guarda como iteraciones:salt:hash (todo en Base64):
 */
public final class Seguridad {
    
    private static final int ITERACIONES = 65536;
    private static final int BITS_CLAVE = 256;
    private static final int BYTES_SALT = 16;
    
    private Seguridad() {
    }
    
    public static String hasher(String clave) {
        byte[] salt = new byte[BYTES_SALT];
        new SecureRandom().nextBytes(salt);
        byte[] hash = derivar(clave, salt, ITERACIONES);
        return ITERACIONES + ":"
                + Base64.getEncoder().encodeToString(salt) + ":"
                + Base64.getEncoder().encodeToString(hash);
    }
    
    public static boolean verificar(String clave, String almacenado) {
        if (clave == null || almacenado == null) {
            return false;
        }
        String[] partes = almacenado.split(":");
        if (partes.length != 3) {
            return false;
        }
        try {
            int iteraciones = Integer.parseInt(partes[0]);
            byte[] salt = Base64.getDecoder().decode(partes[1]);
            byte[] esperado = Base64.getDecoder().decode(partes[2]);
            byte[] calculado = derivar(clave, salt, iteraciones);
            return MessageDigest.isEqual(esperado, calculado);
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
    
    private static byte[] derivar(String clave, byte[] salt, int iteraciones) {
        try {
            PBEKeySpec spec = new PBEKeySpec(clave.toCharArray(), salt, iteraciones, BITS_CLAVE);
            return SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
                    .generateSecret(spec).getEncoded();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new IllegalStateException("No se pudo calcular el hash de la contraseña.", e);
        }
    }
}
