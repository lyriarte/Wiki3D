/*
 * Copyright (c) 2008, Luc Yriarte
 * All rights reserved.
 * 
 * @file Vector3D.java
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
import org.yriarte.math.Vector;

public class Vector3D extends Vector implements SpaceCoordinate {

	static public Vector3D MatrixAsVector3D(Matrix aMatrix) {
		return new Vector3D(aMatrix.getCell());
	}
	
	/**
	 * [0,0,0,1]
	 */
	public Vector3D() {
		nLines = 4;
		nCols = 1;
		cell = new double[nLines][nCols];
		cell[0][0] = 0;
		cell[1][0] = 0;
		cell[2][0] = 0;
		cell[3][0] = 1;
	}

	/**
	 * [x,y,z,1]
	 */
	public Vector3D(double x, double y, double z) {
		nLines = 4;
		nCols = 1;
		cell = new double[nLines][nCols];
		cell[0][0] = x;
		cell[1][0] = y;
		cell[2][0] = z;
		cell[3][0] = 1;
	}
	
	public Vector3D(double[][] cell) {
		nLines = 4;
		nCols = 1;
		this.cell = cell;
	}

	public Vector3D(Matrix aMatrix) {
		nLines = 4;
		nCols = 1;
		cell = new double[nLines][nCols];
		cell[0][0] = aMatrix.getCell()[0][0];
		cell[1][0] = aMatrix.getCell()[1][0];
		cell[2][0] = aMatrix.getCell()[2][0];
		cell[3][0] = 1;
	}

	public Vector3D(Matrix aMatrix, int column) {
		nLines = 4;
		nCols = 1;
		cell = new double[nLines][nCols];
		cell[0][0] = aMatrix.getCell()[0][column];
		cell[1][0] = aMatrix.getCell()[1][column];
		cell[2][0] = aMatrix.getCell()[2][column];
		cell[3][0] = 1;
	}

	public Matrix3D positionMatrix() {
		return Matrix3D.translation(this.cell[0][0],this.cell[1][0],this.cell[2][0]);
	}
	
	/**
	 * @param aMatrix
	 * @return this = aMatrix * this
	 */
	public Vector3D transformThis(Matrix aMatrix) {
		double x,y,z;
		double mCell[][] = aMatrix.getCell();
		x = (mCell[0][0] * this.cell[0][0])
		  + (mCell[0][1] * this.cell[1][0])
		  + (mCell[0][2] * this.cell[2][0])
		  +  mCell[0][3];
		y = (mCell[1][0] * this.cell[0][0])
		  + (mCell[1][1] * this.cell[1][0])
		  + (mCell[1][2] * this.cell[2][0])	
		  +  mCell[1][3];
		z = (mCell[2][0] * this.cell[0][0])
		  + (mCell[2][1] * this.cell[1][0])
		  + (mCell[2][2] * this.cell[2][0])
		  +  mCell[2][3];
		this.cell[0][0] = x;
		this.cell[1][0] = y;
		this.cell[2][0] = z;
		return this;
	}
	
	public double getX() {
		return cell[0][0];
	}
	
	public double getY() {
		return cell[1][0];
	}
	
	public double getZ() {
		return cell[2][0];
	}
	
	public void setX(double aDouble) {
		cell[0][0] = aDouble;
	}
	
	public void setY(double aDouble) {
		cell[1][0] = aDouble;
	}
	
	public void setZ(double aDouble) {
		cell[2][0] = aDouble;
	}
	
}
