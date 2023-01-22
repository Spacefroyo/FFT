import java.math.BigInteger;
import java.util.Arrays;

/**
 * The FourierTransforms class implements functions related to the fast fourier
 * transform
 */
public class FourierTransforms {

    // Number of bits in a byte
    public static final int BITS_IN_BYTE = 8;
    private FourierTransforms() {}

    /**
     * Finds n (complex) points that lie on a given degree n-1 polynomial using
     * an iterative implementation of the fast fourier transform
     *
     * @param arr The coefficients of the input polynomial
     * @return The n complex points formatted as {real comp, imag comp}
     */
    public static double[][] FFT(double[] arr) {
        int n = arr.length, n2 = n >> 1;
        double[][][] ans = new double[2][2][n];
        for (int i = 0; i < n; i++) {
            ans[0][0][i] = arr[i];
        }

        int iter = 0;
        for (int i = (int)Math.round(Math.log(n2)/Math.log(2)); i > -1; --i) {
            double theta = Math.PI / (n2 >> i);

            double[][] w = new double[2][n2>>i];
            for (int j = 0; j < w[0].length; ++j) {
                w[0][j] = Math.cos(theta * j);
                w[1][j] = Math.sin(theta * j);
            }

            double[] now0 = ans[1-iter][0], now1 = ans[1-iter][1],
                    last0 = ans[iter][0], last1 = ans[iter][1];
            for (int k = 0, k2 = n2; k < n2; ++k, ++k2) {
                int index = k>>i, from0 = (index<<i)+k, from1 = (1<<i)+from0;
                double from00 = last0[from0], from01 = last1[from0];
                double from10 = last0[from1], from11 = last1[from1];
                double w0 = w[0][index], w1 = w[1][index];
                double p0 = w0 * from10 - w1 * from11,
                        p1 = w0 * from11 + w1 * from10;

                now0[k] = from00 + p0;
                now1[k] = from01 + p1;

                now0[k2] = from00 - p0;
                now1[k2] = from01 - p1;

            }
            iter = 1 - iter;
        }
        return ans[iter];
    }

    /**
     * Finds the coefficients of the degree n-1 polynomial that goes through n
     * given (complex) points using an iterative implementation of the inverse
     * fast fourier transform
     *
     * @param arr The n complex points formatted as {real comp, imag comp}
     * @return The coefficients of the output polynomial
     */
    public static double[][] IFFT(double[][] arr) {
        int n = arr[0].length, n2 = n >> 1;
        double[][][] ans = new double[2][2][n];
        for (int i = 0; i < n; i++) {
            ans[0][0][i] = arr[0][i];
            ans[0][1][i] = arr[1][i];
        }

        int iter = 0;
        for (int i = n>>1; i > 0; i >>= 1) {
            double theta = -i * Math.PI / n2;
            double[][] w = new double[2][n2/i];
            for (int j = 0; j < w[0].length; j++) {
                w[0][j] = Math.cos(theta * j);
                w[1][j] = Math.sin(theta * j);
            }
            double[] now0 = ans[1-iter][0], now1 = ans[1-iter][1],
                    last0 = ans[iter][0], last1 = ans[iter][1];
            for (int k = 0, k2 = n2; k < n2; ++k, ++k2) {
                int index = k/i, from0 = index * i + k, from1 = from0+i;
                double from00 = last0[from0], from01 = last1[from0];
                double from10 = last0[from1], from11 = last1[from1];
                double w0 = w[0][index], w1 = w[1][index];
                double p0 = w0 * from10 - w1 * from11,
                        p1 = w0 * from11 + w1 * from10;

                now0[k] = from00 + p0;
                now1[k] = from01 + p1;

                now0[k2] = from00 - p0;
                now1[k2] = from01 - p1;

            }
            iter = 1 - iter;
        }

        for (int i = 0; i < n; ++i) {
            ans[iter][0][i] /= n;
            ans[iter][1][i] /= n;
        }
        return ans[iter];
    }

    /**
     * Finds the convolution of two given arrays
     *
     * @param arr1 A given array
     * @param arr2 A given array
     * @return The convolution of the two given arrays
     */
    public static double[] convolve(double[] arr1, double[] arr2) {
        int n = 1<<(int)Math.ceil(Math.log(arr1.length+arr2.length)/Math.log(2));
        double[] a = Arrays.copyOf(arr1, n), b = Arrays.copyOf(arr2, n);

        double[][] fft1 = FFT(a), fft2 = FFT(b);
        double[][] product = new double[2][n];
        for (int i = 0; i < n; ++i) {
            product[0][i] = fft1[0][i] * fft2[0][i] - fft1[1][i] * fft2[1][i];
            product[1][i] = fft1[0][i] * fft2[1][i] + fft1[1][i] * fft2[0][i];
        }

        double[][] ans = IFFT(product);
        double[] ret = new double[n];
        for (int i = 0; i < n; ++i) {
            ret[i] = ans[0][i];
        }

        return ret;
    }

    /**
     * Multiplies two BigIntegers using convolutions and the fast fourier
     * transform
     *
     * @param a One multiplicand (or multiplier)
     * @param b One multiplicand (or multiplier)
     * @return The product of the two input BigIntegers
     */
    public static BigInteger cmult(BigInteger a, BigInteger b) {
        byte[] barr1 = a.toByteArray();
        byte[] barr2 = b.toByteArray();
        double[] arr1 = new double[barr1.length];
        double[] arr2 = new double[barr2.length];
        for (int i = 0; i < barr1.length; i++) {
            arr1[arr1.length-1-i] = barr1[i];
        }
        for (int i = 0; i < barr2.length; i++) {
            arr2[arr2.length-1-i] = barr2[i];
        }
        double[] arr = FourierTransforms.convolve(arr1, arr2);
        byte[] ans = new byte[arr.length];
        for (int i = 0; i < arr.length-1; i++) {
            arr[i+1] += Math.round(arr[i]) >> BITS_IN_BYTE;
            ans[arr.length-1-i] = (byte)(Math.round(arr[i]) % (1<<BITS_IN_BYTE));
        }
        ans[0] = (byte)(Math.round(arr[arr.length-1]) % (1<<BITS_IN_BYTE));
        return new BigInteger(ans);
    }
}
