/*
 * Copyright (c) 2008, Luc Yriarte
 * All rights reserved.
 * 
 * @file Matrix.java
 * @author Luc Yriarte
 * 
 * License: BSD <http://www.opensource.org/licenses/bsd-license.php>
 * 
 * Redistribution and use in source and binary forms, with or without modification, are permitted 
 * provided that the following conditions are met:
 * 
 *     * Redistributions of source code must retain the above copyright notice, this 
 * list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright notice, this 
 * list of conditions and the following disclaimer in the documentation and/or other materials 
 * provided with the distribution.
 *     * The name of Luc Yriarte may not be used to endorse or promote products derived 
 * from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR 
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND 
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. 
 * IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, 
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE 
 * GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY 
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. 
 * 
 */

package org.yriarte.math;


public class Complex {
	/** @brief complex number z = a + i.b */
	public double a,b,mod;
	
	/**
	 * @brief module of a complex number, same thing as hypothenuse length
	 * @param a
	 * @param b
	 * @return square root of a square plus b square
	 */
	static public double module(double a, double b) {
		return Math.sqrt(a*a+b*b);
	}

	public Complex() {
		a=b=mod=0;
	}

	/**
	 * @brief creates a complex number z = a + i.b 
	 * from its real (a) and imaginary (b) parts and computes the module
	 * @param real
	 * @param imaginary
	 */
	public Complex(double real, double imaginary) {
		a=real;
		b=imaginary;
		mod = Complex.module(a,b);
//System.out.println(" z=a+ib : " + mod + " = " + a + " + i * " + b);
	}

	/**
	 * @brief creates a complex number from a column vector where first line is
	 * the real and second line is the imaginary part
	 * @param m a column vector
	 */
	public Complex(Matrix m) {
		a=m.cell[0][0];
		b=m.cell[0][1];
		mod = Complex.module(a,b);
//System.out.println(" z=a+ib : " + mod + " = " + a + " + i * " + b);
	}

	/**
	 * @brief addition and multiplication on complex numbers are equivalent to matrix algebra
	 * where a complex number z = a + i.b is represented by a square [[a,-b],[b,a]] matrix
	 * @return a square [[a,-b],[b,a]] matrix
	 */
	public Matrix matrix() {
		Matrix mResult = new Matrix(2,2);
		mResult.cell[0][0] = a;
		mResult.cell[0][1] = b;
		mResult.cell[1][0] =-b;
		mResult.cell[1][1] = a;
		return mResult;
	}

	/**
	 * @brief Adds complex numbers
	 * @param z a complex number
	 * @return new Complex(this.matrix().add(z.matrix()));
	 */
	public Complex add(Complex z) {
		return new Complex(a+z.a, b+z.b);
	}
	
	/**
	 * @brief Multiplies complex numbers
	 * @param z a complex number
	 * @return new Complex(this.matrix().mul(z.matrix()));
	 */
	public Complex mul(Complex z) {
		return new Complex(a*z.a-b*z.b, a*z.b+b*z.a);
	}
}
