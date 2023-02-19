import java.math.BigInteger;

public class RSADriver {

    /* RSA encryption is m^e mod n */
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
        BigInteger carmTotient = findCarmTotient(p,q);
        BigInteger privateKey = publicKey.modInverse(carmTotient);

        if (!n.gcd(publicKey).equals(BigInteger.ONE)){
            throw new IllegalArgumentException("the public key must be coprime with n");
        }
        return new BigInteger[]{n, privateKey};
    }

    /* Carmichael's totient function of p&q is the LCM of p-1 and q-1, algo uses euclidean algo to calculate*/
    static public BigInteger findCarmTotient(BigInteger p, BigInteger q){
        p = p.subtract(BigInteger.ONE);
        q = q.subtract(BigInteger.ONE);
        return (p.multiply(q)).divide(p.gcd(q));
    }

    /*RSA decryption is c^d mod n*/
    static public BigInteger decrypt(BigInteger ciphertext, BigInteger n, BigInteger privateKey){
        return ciphertext.modPow(privateKey,n);
    }

    public static void main(String[] args){
        BigInteger p = new BigInteger("272115014291069423254174604747900283529589045946766925871434156490304672813332584327196238727346231707628524220591180278992669261885315541831665061427317232576855964638091138515271096575740045191406721441877386137238277044997897924289431318051601821531557067755924971635813394788750219100628475528939");
        BigInteger q = new BigInteger("939990218327997619341180359788579855500244920988439573136463013130588178054670474694146131976342461085653599808759685352209582282921223722955566198990820133439949264720928883468091806121120668896708772405642613688560634187375646055779965805796743925516161502207044309886301061164298370997403426209");
        BigInteger message = new BigInteger("11111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111");
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
