/*
 * Copyright (c) 2008, Luc Yriarte
 * All rights reserved.
 * 
 * @file Vector.java
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

/**
 * Column vector
 */
public class Vector extends Matrix {

	/**
	 * @brief Casts a Matrix as a Vector, cells are not copied
	 * @param aMatrix single column matrix
	 * @return a vector
	 */
	static public Vector MatrixAsVector(Matrix aMatrix) {
		return new Vector(aMatrix.nLines, aMatrix.cell);
	}
	
	public Vector() {
		super();
	}

	public Vector(int lines) {
		super(lines, 1);
	}

	public Vector(int lines, double[][] cell) {
		super(lines, 1, cell);
	}

	/**
	 * @brief creates a vector the matrix first column
	 * @param aMatrix
	 */
	public Vector(Matrix aMatrix) {
		super(aMatrix.nLines, 1);
		int i;
		for (i = 0; i < nLines; i++) {
			this.cell[i][0] = aMatrix.cell[i][0];
		}
	}
	
	/**
	 * @param aMatrix
	 * @param column
	 */
	public Vector(Matrix aMatrix, int column) {
		super(aMatrix.nLines, 1);
		int i;
		for (i = 0; i < nLines; i++) {
			this.cell[i][0] = aMatrix.cell[i][column];
		}
	}
	
	/**
	 * @brief vector norm, or magnitude, noted "|a|"
	 * @return square root of the sum of squares
	 */
	public double norm() {
		double n = 0;
		int i;
		for (i = 0; i < this.nLines; i++) {
			n += this.cell[i][0] * this.cell[i][0];
		}
		n = Math.sqrt(n);
		return n;
	}

	/**
	 * @brief noted "a . b"
	 * @param aVector
	 * @return result = this vector transposed multiplied by a vector
	 */
	public double scalProd(Vector aVector) {
/*		Matrix mResult = this.transpose();
		mResult = mResult.mul(aVector);
		return mResult.getCell()[0][0];
*/
		double result = 0;
		int i;
		for (i = 0; i < nLines; i++) {
			result += this.cell[i][0] * aVector.cell[i][0];
		}
		return result;		
	}

	/**
	 * @brief noted "‰"
	 * @return |‰|=1 unit vector in this vector direction 
	 */
	public Vector unitVector() {
		double n = this.norm();
		int i;
		Vector vResult = new Vector(nLines);
		if (n != 0) {
			for (i = 0; i < nLines; i++) {
				vResult.cell[i][0] = this.cell[i][0] / n;
			}
		}
		return vResult;
	}

	/**
	 * @param aVector
	 * @return this vector angle with aVector
	 */
	public double vectAngle(Vector aVector) {
		double prodNorm, prodScalar;
		prodScalar = this.scalProd(aVector);
		prodNorm = this.norm() * aVector.norm();
		return Math.acos(prodScalar/prodNorm);
	}

	/**
	 * @brief noted "a x b"
	 * @param aVector
	 * @return vector product
	 */
	public Vector vectProd(Vector aVector) {
		Vector vResult = new Vector(aVector.nLines);
		int i,ia,ib;
		for (i = 0; i < nLines; i++) {
			ia = (i + 1) % nLines;
			ib = (ia+ 1) % nLines;
			vResult.cell[i][0] = this.cell[ia][0] * aVector.cell[ib][0] - this.cell[ib][0] * aVector.cell[ia][0];
		}
		return vResult;
	}

	/**
	 * @brief noted "a / b"
	 * @param aVector
	 * @return vector division
	 */
	public Vector vectDiv(Vector aVector) {
		Vector vResult = new Vector();
		if (this.isZero()) {
			if (aVector.isZero())
				return vResult;
			return null;
		}
		double norma = this.norm();
		vResult = aVector.vectProd(this);
		vResult = new Vector(vResult.mulNum(1/(norma*norma)));
		return vResult;
	}

}

