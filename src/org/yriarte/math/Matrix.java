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

/**
 * @author Luc Yriarte
 * created on 24 aout 2008
 */
public class Matrix {

	protected int nLines;
	protected int nCols;
	protected double cell[][];
    
    /**
     * @brief Creates an empty matrix with 0 lines and 0 columns
     */
    public Matrix() {
        super();
		nLines = 0;
		nCols = 0;
    }

    /**
     * @brief Creates a matrix filled with 0
     * @param lines
     * @param cols
     */
    public Matrix(int lines, int cols) {
        super();
        nLines = lines;
        nCols = cols;
        cell = new double[nLines][nCols];
        toZero();
    }
    
    /**
     * @brief Creates a matrix referencing the cell array without copy
     * @param lines
     * @param cols
     * @param cell
     */
    public Matrix(int lines, int cols, double[][] cell) {
        super();
        nLines = lines;
        nCols = cols;
        this.cell = cell;
    }
    
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		String str = "["; //$NON-NLS-1$
		int i, j;
		for (i = 0; i < nLines; i++) {
			if (i > 0)
				str = str.concat(","); //$NON-NLS-1$
			str = str.concat("["); //$NON-NLS-1$
			for (j = 0; j < nCols; j++) {
				if (j > 0)
					str = str.concat(","); //$NON-NLS-1$
				str = str.concat((new Double(cell[i][j])).toString());
			}
			str = str.concat("]"); //$NON-NLS-1$
		}
		str = str.concat("]"); //$NON-NLS-1$
		return str;
	}

	/**
	 * @return the cell array
	 */
	public double[][] getCell() {
		return cell;
	}

	/**
	 * @param cell the cell array
	 * @return this Matrix
	 */
	public Matrix setCell(double[][] cell) {
		this.cell=cell;
		return this;
	}

	/**
	 * @return the number of columns
	 */
	public int getNCols() {
		return nCols;
	}

	/**
	 * @return the number of lines
	 */
	public int getNLines() {
		return nLines;
	}

	/**
	 * @return true if this matrix is filled with 0
	 */
	public boolean isZero() {
		int i, j;
		for (i=0; i<this.nLines; i++) {
			for (j=0; j<this.nCols; j++) {
				if (this.cell[i][j] != 0) 
					return false;
			}
		}
		return true;
	}

	/**
	 * @brief fills this matrix with 0 
	 * @return this Matrix
	 */
	public Matrix toZero() {
		int i, j;
		for (i = 0; i < nLines; i++) {
			for (j = 0; j < nCols; j++) {
				cell[i][j] = 0;
			}
		}
		return this;
	}

	/**
	 * @return true iff this matrix is square, filled with 0
	 * and the diagonal is filled with 1
	 */
	public boolean isId() {
		int i, j;
		if (this.nLines != this.nCols)
			return false;
		for (i=0; i<this.nLines; i++) {
			for (j=0; j<this.nCols; j++) {
				if (this.cell[i][j] != 0 && !(i == j && this.cell[i][j] == 1))
					return false;
			}
		}
		return true;
	}

	/**
	 * @brief fills this matrix with 0 and the diagonal with 1
	 * @return this Matrix
	 */
	public Matrix toId() {
		int i;
		toZero();
		for (i = 0; i < nLines; i++) {
			cell[i][i] = 1;
		}
		return this;
	}

	/**
	 * @param aMatrix
	 * @return mResult = this * aMatrix
	 */
	public Matrix mul(Matrix aMatrix) {
		int i, j, k;
		Matrix mResult = new Matrix(this.nLines, aMatrix.nCols);
		for (i = 0; i < mResult.nLines; i++) {
			for (j = 0; j < mResult.nCols; j++) {
				mResult.cell[i][j] = 0;
				for (k = 0; k < this.nCols; k++) {
					mResult.cell[i][j] += this.cell[i][k] * aMatrix.cell[k][j];
				}
			}
		}
		return mResult;
	}

	/**
	 * @param aMatrix
	 * @return mResult = this + aMatrix
	 */
	public Matrix add(Matrix aMatrix) {
		int i, j;
		Matrix mResult = new Matrix(this.nLines, this.nCols);
		for (i = 0; i < mResult.nLines; i++) {
			for (j = 0; j < mResult.nCols; j++) {
				mResult.cell[i][j] = this.cell[i][j] + aMatrix.cell[i][j];
			}
		}
		return mResult;
	}

	/**
	 * @param aMatrix
	 * @return this = this + aMatrix
	 */
	public Matrix addThis(Matrix aMatrix) {
		int i, j;
		for (i = 0; i < this.nLines; i++) {
			for (j = 0; j < this.nCols; j++) {
				this.cell[i][j] = this.cell[i][j] + aMatrix.cell[i][j];
			}
		}
		return this;
	}

	/**
	 * @param aMatrix
	 * @return mResult = this - aMatrix
	 */
	public Matrix sub(Matrix aMatrix) {
		int i, j;
		Matrix mResult = new Matrix(this.nLines, this.nCols);
		for (i = 0; i < mResult.nLines; i++) {
			for (j = 0; j < mResult.nCols; j++) {
				mResult.cell[i][j] = this.cell[i][j] - aMatrix.cell[i][j];
			}
		}
		return mResult;
	}

	/**
	 * @param aMatrix
	 * @return this = this - aMatrix
	 */
	public Matrix subThis(Matrix aMatrix) {
		int i, j;
		for (i = 0; i < this.nLines; i++) {
			for (j = 0; j < this.nCols; j++) {
				this.cell[i][j] = this.cell[i][j] - aMatrix.cell[i][j];
			}
		}
		return this;
	}

	/**
	 * @param aNumber
	 * @return mResult = aNumber * this
	 */
	public Matrix mulNum(double aNumber) {
		int i, j;
		Matrix mResult = new Matrix(this.nLines, this.nCols);
		for (i = 0; i < mResult.nLines; i++) {
			for (j = 0; j < mResult.nCols; j++) {
				mResult.cell[i][j] = this.cell[i][j] * aNumber;
			}
		}
		return mResult;
	}

	/**
	 * @param aNumber
	 * @return mResult = aNumber + this
	 */
	public Matrix addNum(double aNumber) {
		int i, j;
		Matrix mResult = new Matrix(this.nLines, this.nCols);
		for (i = 0; i < mResult.nLines; i++) {
			for (j = 0; j < mResult.nCols; j++) {
				mResult.cell[i][j] = this.cell[i][j] + aNumber;
			}
		}
		return mResult;
	}

	/**
	 * @param aNumber
	 * @return this = aNumber * this
	 */
	public Matrix mulNumThis(double aNumber) {
		int i, j;
		for (i = 0; i < this.nLines; i++) {
			for (j = 0; j < this.nCols; j++) {
				this.cell[i][j] = this.cell[i][j] * aNumber;
			}
		}
		return this;
	}

	/**
	 * @param aNumber
	 * @return this = aNumber + this
	 */
	public Matrix addNumThis(double aNumber) {
		int i, j;
		for (i = 0; i < this.nLines; i++) {
			for (j = 0; j < this.nCols; j++) {
				this.cell[i][j] = this.cell[i][j] + aNumber;
			}
		}
		return this;
	}

	/**
	 * @param aNumber
	 * @return this += aNumber * aMatrix
	 */
	public Matrix addMulNumThis(Matrix aMatrix, double aNumber) {
		int i, j;
		for (i = 0; i < this.nLines; i++) {
			for (j = 0; j < this.nCols; j++) {
				this.cell[i][j] += aMatrix.cell[i][j] * aNumber;
			}
		}
		return this;
	}

	/**
	 * @brief Inverts lines and columns
	 * @return mResult = this matrix transposed
	 */
	public Matrix transpose() {
		int i, j;
		Matrix mResult = new Matrix(this.nCols, this.nLines);
		for (i = 0; i < mResult.nLines; i++) {
			for (j = 0; j < mResult.nCols; j++) {
				mResult.cell[i][j] = cell[j][i];
			}
		}
		return mResult;
	}

	/**
	 * @return mResult = this matrix minus line iM and column jM
	 */
	public Matrix minor(int iM, int jM) {
		int i, j;
		Matrix mResult = new Matrix(this.nLines - 1, this.nCols - 1);
		for (i = 0; i < iM; i++) {
			for (j = 0; j < jM; j++) {
				mResult.cell[i][j] = this.cell[i][j];
			}
			for (j = jM + 1; j < this.nCols; j++) {
				mResult.cell[i][j - 1] = this.cell[i][j];
			}
		}
		for (i = iM + 1; i < this.nLines; i++) {
			for (j = 0; j < jM; j++) {
				mResult.cell[i - 1][j] = this.cell[i][j];
			}
			for (j = jM + 1; j < this.nCols; j++) {
				mResult.cell[i - 1][j - 1] = this.cell[i][j];
			}
		}
		return mResult;
	}

	/**
	 * @return this matrix determinant
	 */
	public double det() {
		if (this.nLines == 2)
			return this.cell[0][0] * this.cell[1][1] - this.cell[0][1]
					* this.cell[1][0];
		if (this.nLines == 1)
			return this.cell[0][0];
		Matrix minorIJ;
		double detIJ, determinant = 0, sign = 1;
		int j;
		for (j = 0; j < this.nCols; j++) {
			minorIJ = this.minor(0, j);
			detIJ = minorIJ.det();
			determinant += sign * detIJ * this.cell[0][j];
			sign = -sign;
		}
		return determinant;
	}

	/**
	 * @return mResult = this matrix cofactors matrix
	 */
	public Matrix cofactors() {
		if (this.nLines == 1)
			return this;
		int i, j;
		double detIJ;
		Matrix minorIJ, mResult = new Matrix(this.nCols, this.nLines);
		for (i = 0; i < mResult.nLines; i++) {
			for (j = 0; j < mResult.nCols; j++) {
				minorIJ = this.minor(i, j);
				detIJ = minorIJ.det();
				mResult.cell[i][j] = detIJ * Math.pow(-1,i+j);
			}
		}
		return mResult;
	}

	/**
	 * @return mResult = this matrix inverted
	 */
	public Matrix inv() {
		double determinant = this.det();
		Matrix mResult = this.cofactors();
		mResult = mResult.transpose();
		mResult = mResult.mulNum(1 / determinant);
		return mResult;
	}
    
}
