/*
 * Copyright (c) 2008, Luc Yriarte
 * All rights reserved.
 * 
 * @file Matrix3D.java
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

package org.yriarte.mini3D;

import org.yriarte.math.Matrix;

public class Matrix3D extends Matrix {

	static public Matrix3D MatrixAsMatrix3D(Matrix aMatrix) {
		return new Matrix3D(aMatrix.getCell());
	}
	
	/**
	 * @brief Identity
	 */
	public Matrix3D() {
		super(4, 4);
		toId();
	}

	public Matrix3D(double[][] cell) {
		nLines = 4;
		nCols = 4;
		this.cell = cell;
	}

	/**
	 * @param x translation units on the x axis
	 * @param y translation units on the y axis
	 * @param z translation units on the z axis
	 * @return a translated copy of this matrix.
	 */
	static public Matrix3D translation(double x, double y, double z) {
		Matrix3D result = new Matrix3D();
		result.cell[0][3] = x;
		result.cell[1][3] = y;
		result.cell[2][3] = z;
		return result;
	}

	/**
	 * @param x scale factor on the x axis 
	 * @param y scale factor on the y axis 
	 * @param z scale factor on the z axis 
	 * @return a scaled copy of this matrix.
	 */
	static public Matrix3D scale(double x, double y, double z) {
		Matrix3D result = new Matrix3D();
		result.cell[0][0] = x;
		result.cell[1][1] = y;
		result.cell[2][2] = z;
		return result;
	}

	/**
	 * @param teta angle in radians
	 * @return a copy of this matrix rotated by teta radians on the x axis
	 */
	static public Matrix3D rotationX(double teta) {
		Matrix3D result = new Matrix3D();
		result.cell[1][1] = Math.cos(teta);
		result.cell[1][2] = Math.sin(teta);
		result.cell[2][1] = -Math.sin(teta);
		result.cell[2][2] = Math.cos(teta);
		return result;
	}

	/**
	 * @param teta angle in radians
	 * @return a copy of this matrix rotated by teta radians on the y axis
	 */
	static public Matrix3D rotationY(double teta) {
		Matrix3D result = new Matrix3D();
		result.cell[0][0] = Math.cos(teta);
		result.cell[0][2] = -Math.sin(teta);
		result.cell[2][0] = Math.sin(teta);
		result.cell[2][2] = Math.cos(teta);
		return result;
	}

	/**
	 * @param teta angle in radians
	 * @return a copy of this matrix rotated by teta radians on the z axis
	 */
	static public Matrix3D rotationZ(double teta) {
		Matrix3D result = new Matrix3D();
		result.cell[0][0] = Math.cos(teta);
		result.cell[0][1] = Math.sin(teta);
		result.cell[1][0] = -Math.sin(teta);
		result.cell[1][1] = Math.cos(teta);
		return result;
	}

}
