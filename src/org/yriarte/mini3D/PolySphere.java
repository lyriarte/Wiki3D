/*
 * Copyright (c) 2008, Luc Yriarte
 * All rights reserved.
 * 
 * @file PolySphere.java
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
 * @brief Sphere with meridans and parallels approximated as polygons of n sides.
 * frad factor on parallel radius spheric if 1
 */

public class PolySphere extends Object3D {

	/**
	 * @param nTotal segments for equatorial circle approximation
	 * @param n segments to draw a partial sphere
	 * @param r equatorial radius
	 * @param frad equatorial radius divided by polar axis length
	 * @param full draw half sqare facets if false
	 */
	private void init(int nTotal, int n, double r, double frad, boolean full) {
		if (n < 3)
			n = 3;
		if (nTotal < n)
			nTotal = n;
		nFacet = n * (n - 1);
		facet = new PolygonEdgeIndexes[nFacet];
		double step = 2 * Math.PI / nTotal;
		double rLat, y;
		int nV = n * (n - 2) + 2;
		Vector3D v[] = new Vector3D[nV];
		int nE = 2 * (nV - 2) + n;
		int[][] e = new int[nE][2];
		int i, j, f;
		// facet counter
		f = 0;
		// top
		v[nV - 1] = new Vector3D(0, r, 0);
		// bottom
		v[nV - 2] = new Vector3D(0, -r, 0);
		// lines from top
		for (i = 0; i < n; i++) {
			e[nE - (i + 1)][0] = nV - 1;
			e[nE - (i + 1)][1] = i;
			// top triangle facets
			facet[f] = new PolygonEdgeIndexes(this, 3);
			facet[f].edge[0] = nE - (i + 1);
			facet[f].edge[1] = i;
			facet[f].edge[2] = (i + 1) % n;
			f++;
		}
		// layers top down
		for (i = 0; i < n - 2; i++) {
			// top down
			rLat = frad * r * Math.sin(Math.PI * (i + 1) / (n - 1));
			y = r * Math.cos(Math.PI * (i + 1) / (n - 1));
			// polygon
			for (j = 0; j < n; j++) {
				int iV = i * n + j;
				v[iV] = new Vector3D(rLat * Math.cos(j * step), y, rLat
						* Math.sin(j * step));
				// line to next
				e[iV][0] = iV;
				e[iV][1] = i * n + ((j + 1) % n);
				// vertical lines to next below
				e[n * (n - 2) + iV][1] = iV;
				if (i < n - 3) {
					e[n * (n - 2) + iV][0] = iV + n;
					if (full) { // square facet
						facet[f] = new PolygonEdgeIndexes(this, 4);
						facet[f].edge[2] = (i + 1) * n + (j + 1) % n;
						facet[f].edge[3] = (i + 1) * n + j;
					}
					else { // hollow sphere with triangle facets
						facet[f] = new PolygonEdgeIndexes(this, 3);
						facet[f].edge[2] = (i + 1) * n + (j + 1) % n;
					}
				} else {
					e[n * (n - 2) + iV][0] = nV - 2;
					// bottom triangle facet
					facet[f] = new PolygonEdgeIndexes(this, 3);
					facet[f].edge[2] = n * (n - 2) + iV;
				}
				facet[f].edge[0] = iV;
				facet[f].edge[1] = i * n + (j + 1) % n;
				f++;
			}
		}
		mesh = new Mesh(nV, nE, v, e);
	}

	public PolySphere(int nTotal, int n, double r, double frad, boolean full) {
		super();
		init(nTotal, n, r, frad, full);
	}

	public PolySphere(int nTotal, int n, double r, double frad) {
		super();
		init(nTotal, n, r, frad, true);
	}

	public PolySphere(int n, double r, double frad) {
		super();
		init(n, n, r, frad, true);
	}

	public PolySphere(int n, double r) {
		super();
		init(n, n, r, 1, true);
	}

	public PolySphere(double r, double frad) {
		super();
		init(N_DEFAULT, N_DEFAULT, r, frad, true);
	}

	public PolySphere(double r) {
		super();
		init(N_DEFAULT, N_DEFAULT, r, 1, true);
	}

}
