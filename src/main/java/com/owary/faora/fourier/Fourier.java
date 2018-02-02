/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.owary.faora.fourier;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author OwaryLtd
 */
public class Fourier {

    // DFT - Discrete Fourier Transform
    public static Complex[] fft(Complex[] c) {
        int n = c.length;
        if (n == 1) {
            return c;
        }

        Complex[] EVEN = new Complex[n / 2];
        Complex[] ODD = new Complex[n / 2];

        for (int j = 0; j < n / 2; j++) {
            EVEN[j] = c[2 * j];
            ODD[j] = c[2 * j + 1];
        }

        Complex[] cEVEN = fft(EVEN);
        Complex[] cODD = fft(ODD);

        double param = -2 * Math.PI / n;
        Complex omegaN = new Complex(Math.cos(param), Math.sin(param));
        Complex omega = new Complex(1, 0);

        Complex[] result = new Complex[n];

        for (int j = 0; j < n / 2; j++) {
            result[j] = cEVEN[j].add(omega.multiply(cODD[j]));
            result[j + n / 2] = cEVEN[j].subtract(omega.multiply(cODD[j]));
            omega = omega.multiply(omegaN);
        }

        return result;
    }

    // IDFT - Inverse Fourier Transform
    private static Complex[] ifft(Complex[] c) {
        int n = c.length;
        if (n == 1) {
            return c;
        }

        Complex[] EVEN = new Complex[n / 2];
        Complex[] ODD = new Complex[n / 2];

        for (int j = 0; j < n / 2; j++) {
            EVEN[j] = c[2 * j];
            ODD[j] = c[2 * j + 1];
        }

        Complex[] cEVEN = ifft(EVEN);
        Complex[] cODD = ifft(ODD);

        double param = 2 * Math.PI / n;
        Complex omegaN = new Complex(Math.cos(param), -Math.sin(param)).reciprocal();
        Complex omega = new Complex(1, 0);

        Complex[] result = new Complex[n];

        for (int j = 0; j < n / 2; j++) {
            result[j] = cEVEN[j].add(omega.multiply(cODD[j]));
            result[j + n / 2] = cEVEN[j].subtract(omega.multiply(cODD[j]));
            omega = omega.multiply(omegaN);
        }

        return result;
    }

    // IDFT - Inverse Fourier Transform - Main Method.
    /*
    * Since we need to divide each term by N, I did it with sepatate method
     */
    public static Complex[] invfft(Complex[] c) {
        int N = c.length;
        Complex[] tmp = ifft(c);
        Complex[] result = new Complex[N];
        for (int i = 0; i < N; i++) {
            result[i] = tmp[i].divideByNumber(N);
        }
        return result;
    }

    // Another working Inverse FFT 
    public static Complex[] inverseFFT(Complex[] c) {
        int n = c.length;
        if (n == 1) {
            return c;
        }

        Complex[] cEVEN = new Complex[n / 2];
        Complex[] cODD = new Complex[n / 2];

        for (int j = 0; j < n / 2; j++) {
            cEVEN[j] = c[2 * j];
            cODD[j] = c[2 * j + 1];
        }

        Complex[] EVEN = inverseFFT(cEVEN);
        Complex[] ODD = inverseFFT(cODD);

        double param = -2 * Math.PI / n;
        Complex conjOmegaN = new Complex(Math.cos(param), Math.sin(param)).conjugate();

        Complex omega = new Complex(1, 0);

        Complex[] result = new Complex[n];
        for (int j = 0; j < n / 2; j++) {
            Complex tmp = omega.multiply(ODD[j]);
            Complex two = new Complex(1, 0);

            result[j] = two.multiply(EVEN[j].add(tmp));
            result[j + n / 2] = two.multiply(EVEN[j].subtract(tmp));

            omega = omega.multiply(conjOmegaN);
        }

        return result;
    }

    // Fast Fourier Multiply
    public static Complex[] fftMultiply(Complex[] p, Complex[] q) {
        int n = p.length;
        int m = q.length;
        
        // we're making the polynomials equally sized
        List<Complex[]> list = makeThemEqual(p, q);

        // two new p and qs of the same size;
        Complex[] tmpP = list.get(0);
        Complex[] tmpQ = list.get(1);

        // apply discrete fourier transform
        Complex[] pStar = fft(tmpP);
        Complex[] qStar = fft(tmpQ);

        // determining the size of the result array
        double l = Math.ceil(logBase2(p.length + q.length));
        int size = (int) Math.pow(2, l);

        // result array
        Complex[] rStar = new Complex[size];
        for (int i = 0; i < size; i++) {
            rStar[i] = pStar[i].multiply(qStar[i]);
        }
        // switch back to the normal polynomial form
        return invfft(rStar);
    }

    // make 2 polynomials equally sized
    private static List<Complex[]> makeThemEqual(Complex[] p, Complex[] q) {
        List<Complex[]> result = new ArrayList<>();
        int n = p.length;
        int m = q.length;

        // find the next power of 2
        double l = Math.ceil(logBase2(n + m));
        int size = (int) Math.pow(2, l);
        
        // create 2 temporary arrays of the size of next power of 2
        Complex[] tmpP = new Complex[size];
        Complex[] tmpQ = new Complex[size];

        // copy the existing ones to the corresponding new arrays
        System.arraycopy(p, 0, tmpP, 0, n);
        System.arraycopy(q, 0, tmpQ, 0, m);

        // make the ones after the size of the older array, zero
        for (int j = n; j < size; j++) {
            tmpP[j] = new Complex(0, 0);
        }

        for (int j = m; j < size; j++) {
            tmpQ[j] = new Complex(0, 0);
        }

        result.add(tmpP);
        result.add(tmpQ);

        return result;
    }

    private static boolean isPowerOfTwo(int n) {
        return (n & (n - 1)) == 0;
        // 10000 and 01111 = 00000
    }

    private static double logBase2(int k) {
        return Math.log(k) / Math.log(2);
    }

    public static void show(Complex[] x, String title) {
        System.out.println(title);
        System.out.println("-------------------");
        for (int i = 0; i < x.length; i++) {
            System.out.println(x[i]);
        }
        System.out.println();
    }

    public static void main(String[] args) {
        
        Complex[] p1 = new Complex[4];
        p1[0] = new Complex(3, 0);
        p1[1] = new Complex(2, 0);
        p1[2] = new Complex(1, 0);
        p1[3] = new Complex(2, 0);
        
        Complex[] p2 = new Complex[4];
        p2[0] = new Complex(4, 0);
        p2[1] = new Complex(3, 0);
        p2[2] = new Complex(2, 0);
        p2[3] = new Complex(4, 0);
        
        Complex[] re = fftMultiply(p1, p2);

        show(re, "MULT");
    }

}
