import java.math.BigInteger;

public class Main {
    public static void main(String[] args) {
        // Number of bytes
        int n = 1000000;

        // Generate 2 BigIntegers (n bytes each)
        byte[] arr1 = new byte[n];
        byte[] arr2 = new byte[n];
        for (int i = 0; i < n; i++) {
            arr1[i] = (byte) (Math.random() * 128);
            arr2[i] = (byte) (Math.random() * 128);
        }
        BigInteger a = new BigInteger(arr1);
        BigInteger b = new BigInteger(arr2);

        // Standard Java BigInteger multiplication
        long start = System.currentTimeMillis();
        a.multiply(b);
        System.out.println("Java: " + (System.currentTimeMillis() - start) + "ms");

        // BigInteger multiplication using the fast fourier transform
        start = System.currentTimeMillis();
        FourierTransforms.cmult(a, b);
        System.out.println("FFT: " + (System.currentTimeMillis() - start) + "ms");

        // At n = 10e6, cmult is ~30%-40% faster
    }
}