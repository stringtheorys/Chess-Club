package Database;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

public class Hash {

    //Attributes
    private static final int iterations = 200 * 1000;
    private static final int saltLen = 32;
    private static final int desiredKeyLen = 256;

    //Computes the PBKDF2 algorithm which hashes a string
    public static String getSaltedHash(String password) throws Exception {
        byte[] salt = SecureRandom.getInstance("SHA1PRNG").generateSeed(saltLen);
        String saltedHash = Base64.encodeBase64String(salt) + "$" + hash(password, salt);
        return saltedHash;
    }

    //Checks if plaintext password equals stored salted hash of password
    public static boolean checkPassword(String password, String storedPassword) throws IllegalArgumentException {
        String[] SaltPass = storedPassword.split("\\$");
        if (SaltPass.length != 2) {
            throw new IllegalArgumentException("The stored password is illegal");
        }
        String PasswordHash = hash(password, Base64.decodeBase64(SaltPass[0]));
        return (PasswordHash.equals(SaltPass[1]));
    }

    //Hashes the password
    private static String hash(String password, byte[] salt) throws IllegalArgumentException {
        if (password == null || password.length() == 0) {
            throw new IllegalArgumentException("Illegal password argument");
        }
        try {
            SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            SecretKey key = f.generateSecret(new PBEKeySpec(
                    password.toCharArray(), salt, iterations, desiredKeyLen));
            String encodedPassword = Base64.encodeBase64String(key.getEncoded());
            return encodedPassword;
        } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
            ex.printStackTrace();
            return "ERROR";
        }
    }
}
