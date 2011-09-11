/*
 * Copyright (c) 2008, Luc Yriarte
 * All rights reserved.
 * 
 * @file Box.java
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
 * @brief Box with its length on z, width on x, height on y
 * factor fx != 1  makes a flat trapezoid
 */

public class Box extends Object3D {

	/**
	 * @param x width
	 * @param y height
	 * @param z length
	 */
	private void init(double x, double y, double z, double fx) {
		double dx = x / 2;
		double dy = y / 2;
		double dz = z / 2;
		double dbx = dx * fx;
		Vector3D v[] = new Vector3D[8];
		// bottom rectangle
		v[0] = new Vector3D(-dbx, -dy, -dz);
		v[1] = new Vector3D(-dbx, -dy, dz);
		v[2] = new Vector3D(dbx, -dy, dz);
		v[3] = new Vector3D(dbx, -dy, -dz);
		// top rectangle
		v[4] = new Vector3D(-dx, dy, -dz);
		v[5] = new Vector3D(-dx, dy, dz);
		v[6] = new Vector3D(dx, dy, dz);
		v[7] = new Vector3D(dx, dy, -dz);
		nFacet = 6;
		facet = new PolygonEdgeIndexes[nFacet];
		// bottom rectangle
		facet[4] = new PolygonEdgeIndexes(this, 4);
		// top rectangle
		facet[5] = new PolygonEdgeIndexes(this, 4);
		int[][] e = new int[12][2];
		int i;
		for (i = 0; i < 4; i++) {
			// bottom rectangle
			e[i][0] = i;
			e[i][1] = (i + 1) % 4;
			// bottom rectangle facet
			facet[4].edge[i] = i;
			// top rectangle
			e[i + 4][0] = i + 4;
			e[i + 4][1] = ((i + 1) % 4) + 4;
			// top rectangle facet
			facet[5].edge[i] = i + 4;
			// vertical lines
			e[i + 8][0] = i;
			e[i + 8][1] = i + 4;
			// vertical rectangle facets
			facet[i] = new PolygonEdgeIndexes(this, 4);
			facet[i].edge[0] = i;
			facet[i].edge[1] = (i + 1) % 4;
			facet[i].edge[2] = ((i + 1) % 4) + 4;
			facet[i].edge[3] = i + 4;
		}
		mesh = new Mesh(8, 12, v, e);
	}

	public Box(double x, double y, double z) {
		super();
		init(x, y, z, 1);
	}

	public Box(double x, double y, double z, double fx) {
		super();
		init(x, y, z, fx);
	}

}
