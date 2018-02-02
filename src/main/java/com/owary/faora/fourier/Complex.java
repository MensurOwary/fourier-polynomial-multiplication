/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.owary.faora.fourier;

/**
 *
 * @author OwaryLtd
 */
public class Complex {

    private double real;
    private double imaginary;

    public Complex(double real, double imaginary) {
        this.real = real;
        this.imaginary = imaginary;
    }

    public Complex add(Complex c) {
        double real_ = this.getReal() + c.getReal();
        double imaginary_ = this.getImaginary() + c.getImaginary();
        return new Complex(real_, imaginary_);
    }

    public Complex subtract(Complex c) {
        double real_ = this.getReal() - c.getReal();
        double imaginary_ = this.getImaginary() - c.getImaginary();
        return new Complex(real_, imaginary_);
    }
    
    public Complex multiply(Complex c){
        double real_ = this.getReal()*c.getReal() - this.getImaginary()*c.getImaginary();
        double imaginary_ = this.getReal()*c.getImaginary() + this.getImaginary()*c.getReal();
        return new Complex(real_, imaginary_);
    }
    
    public Complex reciprocal(){
        double a = this.getReal();
        double b = this.getImaginary();
        double denom = a*a + b*b;
        double real = a/denom;
        double imag = b/denom;
        Complex c = new Complex(real,-imag);
        return c;
    }
    
    public Complex divideByNumber(int n){
        double real = this.getReal()/n;
        double imag = this.getImaginary()/n;
        return new Complex(real, imag);
    }
    
    public Complex conjugate(){
        return new Complex(this.getReal(),-this.getImaginary());
    }
    
    public double getReal() {
        return real;
    }

    public void setReal(double real) {
        this.real = real;
    }

    public double getImaginary() {
        return imaginary;
    }

    public void setImaginary(double imaginary) {
        this.imaginary = imaginary;
    }

    @Override
    public String toString() {
        String result = "";
        double real = this.real;
        double imag = this.imaginary;
        if (real!=0){
            result += real;
        }       
        if(imag<0){
            result+=imag+"i";
        }else if(imag>0){
            result+="+"+imag+"i";
        }
        return result;
    }

}
