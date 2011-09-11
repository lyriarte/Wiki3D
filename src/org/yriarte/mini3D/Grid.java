/*
 * Copyright (c) 2008, Luc Yriarte
 * All rights reserved.
 * 
 * @file Grid.java
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

public class Grid extends Object3D {

	/**
	 * @brief Grid on the XY plane, n steps of one unit.
	 */

	public static int DRAW_DEFAULT = DRAW_WIREFRAME;
	
	double unit;
	int steps;

	public Grid(double u, int n) {
		super();
		int i;
		unit = u;
		steps = n;
		Vector3D[] v = new Vector3D[n * 4 + 1];
		int[][] e = new int[n * 2 + 2][2];
		for (i = 0; i < n; i++) {
			v[i] = new Vector3D(i * unit, 0, 0);
			v[i + n] = new Vector3D(i * unit, 0, n * unit);
			e[i][0] = i;
			e[i][1] = i + n;
			v[i + 2 * n] = new Vector3D(0, 0, i * unit);
			v[i + 3 * n] = new Vector3D(n * unit, 0, i * unit);
			e[i + n][0] = i + 2 * n;
			e[i + n][1] = i + 3 * n;
		}
		v[n * 4] = new Vector3D(n * unit, 0, n * unit);
		e[2 * n][0] = 3 * n;
		e[2 * n][1] = 4 * n;
		e[2 * n + 1][0] = n;
		e[2 * n + 1][1] = 4 * n;
		mesh = new Mesh(n * 4 + 1, n * 2 + 2, v, e);
	}

}
