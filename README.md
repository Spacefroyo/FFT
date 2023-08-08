# FFT

## Fourier Transforms
 - The fourier transform is a transform which finds n points which lie on a degree n-1 polynomial, thereby completely defining the polynomial
 - The fast fourier transform uses the fact that squares of roots of unity are also roots of unity, allowing for a logilinear runtime
 - This project contains iterative implementations for the fast fourier and inverse fast fourier transforms, as well as a multiplication method for BigIntegers that uses these transforms

## The cmult method
The cmult method can be roughly summarized by the following steps:
1. Represent each number as a polynomial (i.e. 121 -> 1x<sup>2</sup> + 2x + 1, x = 10)
2. Evaluate the two polynomials at the 2<sup>n</sup> roots of unity
3. Multiply the values at each evaluated position
4. Convert the new (position, value) pairs back into a polynomial
5. Evaluate this new polynomial at x = 10 to get the final product

Java's default multiplication method for BigIntegers has a polynomial time complexity.

By contrast, the cmult method has a O(n log(n) log(log(n))) time complexity.

This disparity in time complexity results in the cmult method being increasingly faster than the default multiplication method for larger values.

## Results
When the two multiplicands are around 1MB in size, the cmult method is ~30% - 40% faster than the default multiplication method.
