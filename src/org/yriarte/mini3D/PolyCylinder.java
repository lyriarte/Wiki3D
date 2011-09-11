/*
 * Copyright (c) 2008, Luc Yriarte
 * All rights reserved.
 * 
 * @file PolyCylinder.java
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

/**
 * @brief Vertical Cylinder, height h on the y axis, disks of radius r approximated
 * as polygons of n sides.
 */

public class PolyCylinder extends Object3D {

	/**
	 * @param nTotal segments for base circle approximation
	 * @param n segments to draw a partial base
	 * @param h height on the y axis
	 * @param r base radius
	 */
	private void init(int nTotal, int n, double h, double r) {
		if (n < 3)
			n = 3;
		nFacet = n + 2;
		facet = new PolygonEdgeIndexes[nFacet];
		// bottom polygon
		facet[n] = new PolygonEdgeIndexes(this, n);
		// top polygon
		facet[n + 1] = new PolygonEdgeIndexes(this, n);
		double dh = h / 2;
		double step = 2 * Math.PI / nTotal;
		Vector3D v[] = new Vector3D[n * 2];
		int[][] e = new int[n * 3][2];
		int i;
		for (i = 0; i < n; i++) {
			// bottom polygon
			v[i] = new Vector3D(r * Math.cos(i * step), -dh, r
					* Math.sin(i * step));
			e[i][0] = i;
			e[i][1] = (i + 1) % n;
			// bottom polygon facet
			facet[n].edge[i] = i;
			// top polygon
			v[i + n] = new Vector3D(r * Math.cos(i * step), dh, r
					* Math.sin(i * step));
			e[i + n][0] = i + n;
			e[i + n][1] = ((i + 1) % n) + n;
			// top polygon facet
			facet[n + 1].edge[i] = i + n;
			// vertical lines
			e[i + n * 2][0] = i;
			e[i + n * 2][1] = i + n;
			// vertical rectangle facets
			facet[i] = new PolygonEdgeIndexes(this, 4);
			facet[i].edge[0] = i;
			facet[i].edge[1] = (i + 1) % n;
			facet[i].edge[2] = ((i + 1) % n) + n;
			facet[i].edge[3] = i + n;
		}
		mesh = new Mesh(n * 2, n * 3, v, e);
	}

	public PolyCylinder(int nTotal, int n, double h, double r) {
		super();
		init(nTotal, n, h, r);
	}

	public PolyCylinder(int n, double h, double r) {
		super();
		init(n, n, h, r);
	}

	public PolyCylinder(double h, double r) {
		super();
		init(N_DEFAULT, N_DEFAULT, h, r);
	}

}
