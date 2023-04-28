package cmu.edu.ruidic;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;


/**
 *  RSA Algorithm from CLR
 *
 * 1. Select at random two large prime numbers p and q.
 * 2. Compute n by the equation n = p * q.
 * 3. Compute phi(n)=  (p - 1) * ( q - 1)
 * 4. Select a small odd integer e that is relatively prime to phi(n).
 * 5. Compute d as the multiplicative inverse of e modulo phi(n). A theorem in
 *    number theory asserts that d exists and is uniquely defined.
 * 6. Publish the pair P = (e,n) as the RSA public key.
 * 7. Keep secret the pair S = (d,n) as the RSA secret key.
 * 8. To encrypt a message M compute C = M^e (mod n)
 * 9. To decrypt a message C compute M = C^d (mod n)
 */


public class RSA {
    // Each public and private key consists of an exponent and a modulus
    BigInteger n; // n is the modulus for both the private and public keys
    BigInteger e; // e is the exponent of the public key
    BigInteger d; // d is the exponent of the private key

    // Reference from 95702 RSAExample.java
    public RSA(){
        Random rnd = new Random();

        // Step 1: Generate two large random primes.
        // We use 400 bits here, but best practice for security is 2048 bits.
        // Change 400 to 2048, recompile, and run the program again and you will
        // notice it takes much longer to do the math with that many bits.
        BigInteger p = new BigInteger(400, 100, rnd);
        BigInteger q = new BigInteger(400, 100, rnd);

        // Step 2: Compute n by the equation n = p * q.
        n = p.multiply(q);

        // Step 3: Compute phi(n) = (p-1) * (q-1)
        BigInteger phi = (p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE));

        // Step 4: Select a small odd integer e that is relatively prime to phi(n).
        // By convention the prime 65537 is used as the public exponent.
        e = new BigInteger("65537");

        // Step 5: Compute d as the multiplicative inverse of e modulo phi(n).
        d = e.modInverse(phi);
    }
    public BigInteger getE() {
        return e;
    }

    public BigInteger getD() {
        return d;
    }

    public BigInteger getN(){
        return n;
    }

    public static String getID(BigInteger e, BigInteger n) throws NoSuchAlgorithmException {
        String id = e.toString()+n.toString();
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(id.getBytes());
        id = bytesToHex(md.digest());
        return id.substring(id.length()-20);
    }

    private static String bytesToHex(byte[] data) {
        char[] hexCode = "0123456789ABCDEF".toCharArray();

        StringBuilder r = new StringBuilder(data.length * 2);
        byte[] var2 = data;
        int var3 = data.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            byte b = var2[var4];
            r.append(hexCode[b >> 4 & 15]);
            r.append(hexCode[b & 15]);
        }
        return r.toString();
    }

    // reference from 95702 ShortMessageSign.java
    public String sign(String message) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        // compute the digest with SHA-256
        byte[] bytesOfMessage = message.getBytes("UTF-8");
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] bigDigest = md.digest(bytesOfMessage);

        byte[] messageDigest = new byte[bigDigest.length+1];
        messageDigest[0] = 0;
        System.arraycopy(bigDigest, 0, messageDigest,1, bigDigest.length);

        // From the digest, create a BigInteger
        BigInteger m = new BigInteger(messageDigest);

        // encrypt the digest with the private key
        BigInteger c = m.modPow(d, n);

        return c.toString();
    }

    public static Boolean verify(String message, String encryptedHashStr, BigInteger key, BigInteger n) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        // Take the encrypted string and make it a big integer
        BigInteger encryptedHash = new BigInteger(encryptedHashStr);
        // Decrypt it
        BigInteger decryptedHash = encryptedHash.modPow(key, n);

        // Get the bytes from messageToCheck
        byte[] bytesOfMessageToCheck = message.getBytes("UTF-8");

        // compute the digest of the message with SHA-256
        MessageDigest md = MessageDigest.getInstance("SHA-256");

        byte[] messageToCheckDigest = md.digest(bytesOfMessageToCheck);

        // messageToCheckDigest is a full SHA-256 digest and add a zero byte
        byte[] extraByte = new byte[messageToCheckDigest.length+1];
        extraByte[0] = 0;
        System.arraycopy(messageToCheckDigest, 0, extraByte,1, messageToCheckDigest.length);// copy the rest of the bytes

        // Make it a big int
        BigInteger bigIntegerToCheck = new BigInteger(extraByte);
        // inform the client on how the two compare
        if(bigIntegerToCheck.compareTo(decryptedHash) == 0) {
            return true;
        } else {
            return false;
        }
    }

}
