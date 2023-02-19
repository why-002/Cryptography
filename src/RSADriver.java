import java.math.BigInteger;

public class RSADriver {
    static public BigInteger encrypt(BigInteger n, BigInteger publicKey, BigInteger message){
        return message.modPow(publicKey,n);
    }
    static public BigInteger[] findValues(BigInteger p, BigInteger q, BigInteger publicKey){
        if (!p.isProbablePrime(1000000)){
            throw new IllegalArgumentException("p must be prime");
        }

        if (!q.isProbablePrime(1000000)){
            throw new IllegalArgumentException("q must be prime");
        }

        BigInteger n = p.multiply(q);
        BigInteger charTotient = chrt(p,q);
        BigInteger privateKey = publicKey.modInverse(charTotient);

        if (!n.gcd(publicKey).equals(BigInteger.ONE)){
            throw new IllegalArgumentException("the public key must be coprime with n");
        }
        return new BigInteger[]{n, privateKey};
    }

    static public BigInteger chrt(BigInteger p, BigInteger q){
        p = p.subtract(BigInteger.ONE);
        q = q.subtract(BigInteger.ONE);
        return (p.multiply(q)).divide(p.gcd(q));


    }
    static public BigInteger decrypt(BigInteger ciphertext, BigInteger n, BigInteger privateKey){
        return ciphertext.modPow(privateKey,n);
    }

    public static void main(String[] args){
        BigInteger p = new BigInteger("61");
        BigInteger q = new BigInteger("53");
        BigInteger message = new BigInteger("65");
        BigInteger public_key = new BigInteger("17");
        BigInteger[] vals = findValues(p,q,public_key);
        System.out.println(vals[0]);
        System.out.println(vals[1]);
        BigInteger ans = encrypt(vals[0],public_key, message);
        System.out.println(ans);
        BigInteger dec = decrypt(ans, vals[0], vals[1]);
        System.out.println(dec);
    }
}
