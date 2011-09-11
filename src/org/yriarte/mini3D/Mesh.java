/*
 * Copyright (c) 2008, Luc Yriarte
 * All rights reserved.
 * 
 * @file Mesh.java
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

/**
 * @brief Mesh wireframe model. Edges are represented by the indexes of both ends
 * in the vertex table.
 */
public class Mesh {
	protected int nVertex, nEdge;
	/** vertex table */
	protected Vector3D[] vertex;
	/** an edge is a pair of indexes in the vertex table */
	protected int[][] edge;
	/** wireframe screen coordinates */
	protected int focal, screenX, screenY;
	/** vertices xy translated in screen coordinates */
	protected int scrX[], scrY[];
	/** wireframe lines as an array of edges quintuplets <x1 y1 x2 y2 (z1+z2)/2> */
	protected int lines[][];

	public Mesh() {
		nVertex = nEdge = 0;
		vertex = null;
		edge = null;
		lines = null;
		scrX = scrY = null;
	}

	public Mesh(int nV, int nE, Vector3D[] v, int[][] e) {
		nVertex = nV;
		nEdge = nE;
		vertex = v;
		edge = e;
		scrX = new int[nVertex];
		scrY = new int[nVertex];
		lines = new int[nEdge][5];		
	}

	/**
	 * @param aMesh references the mesh vertices and edges
	 */
	public Mesh(Mesh aMesh) {
		this(aMesh.nVertex, aMesh.nEdge, aMesh.vertex, aMesh.edge);
	}


	
	public int getNVertex() {
		return nVertex;
	}

	public int getNEdge() {
		return nEdge;
	}

	public Vector3D[] getVertex() {
		return vertex;
	}

	public int[][] getLines() {
		return lines;
	}

	/**
	 * Creates a new mesh with the transformed vertices and this meshes
	 * edges.
	 */
	public Mesh transform(Matrix trans) {
		if (nVertex == 0)
			return this;
		int i;
		Vector3D[] transVertex = new Vector3D[nVertex];
		for (i = 0; i < nVertex; i++) {
			transVertex[i] = Vector3D.MatrixAsVector3D(trans.mul(vertex[i]));
		}
		return new Mesh(nVertex, nEdge, transVertex, edge);
	}

	/**
	 * Transform this mesh
	 */
	public Mesh transformThis(Matrix trans) {
		if (nVertex == 0)
			return this;
		int i;
		for (i = 0; i < nVertex; i++) {
			vertex[i].transformThis(trans);
		}
		return this;
	}

	/**
	 * Converts the XYZ coordinates of each vertex in flat xy coordinates
	 *  
	 * @param foc: focal focal distance
	 * @param sX: screenX screen dimension x
	 * @param sY: screenY screen dimension y
	 * @return this
	 */
	public Mesh updateWireframe(int foc, int sX, int sY) {
		if (nEdge == 0)
			return null;
		int i, x0, y0;
		double z;
		focal = foc;
		screenX = sX;
		screenY = sY;
		// screen center
		x0 = screenX / 2;
		y0 = screenY / 2;
		// Translate vertices to screen coordinates
		for (i = 0; i < nVertex; i++) {
			z = vertex[i].getCell()[2][0] > 0 ? vertex[i].getCell()[2][0] : 0.001;
			scrX[i] = (int) (x0 + vertex[i].getCell()[0][0] * focal / z);
			scrY[i] = (int) (y0 - vertex[i].getCell()[1][0] * focal / z);
		}
		// Create lines from mesh edges
		for (i = 0; i < nEdge; i++) {
			lines[i][0] = scrX[edge[i][0]];
			lines[i][1] = scrY[edge[i][0]];
			lines[i][2] = scrX[edge[i][1]];
			lines[i][3] = scrY[edge[i][1]];
			// z is the middle of this edge's ends on the z-axis
			lines[i][4] = ((int) (vertex[edge[i][0]].getCell()[2][0] + vertex[edge[i][1]].getCell()[2][0])) >> 1;
		}
		return this;
	}
	
	public Mesh setWireframe(Mesh aMesh) {
		this.screenX = aMesh.screenX;
		this.screenY = aMesh.screenY;
		this.focal = aMesh.focal;
		this.scrX = aMesh.scrX;
		this.scrY = aMesh.scrY;
		this.lines = aMesh.lines;
		return this;
	}
	
	/**
	 * Selects the first vertex where xy coordinates in the mesh
	 * wireframe are closer than r to xy parameters.
	 * @param x
	 * @param y
	 * @param r
	 * @return index of the selected vertex, -1 if none
	 */
	public int selectVertex(int x, int y, int r) {
		int xmr,xpr,ymr,ypr;
		int i;
		i=-1;
		xmr=x-r;xpr=x+r;ymr=y-r;ypr=y+r;
		for (i = 0; i < nVertex; i++) {
			if (scrX[i] >= xmr && scrX[i] <= xpr && scrY[i] >= ymr && scrY[i] <= ypr)
				break;
		}
		if (i==nVertex)
			i=-1;
// System.out.println("select vertex [" + xmr + ".." + xpr + "][" + ymr + ".." + ypr + "] :" + i);		
		return i;
	}
	

	/**
	 * Calculates the mesh's bounding box
	 * @return [[xmin,xmax],[ymin,ymax],[zmin,zmax]]
	 */
	public Matrix calculateBounds() {
		Matrix mxXyYzZ = new Matrix(3,2);
		for (int j=0; j<3; j++) {
			mxXyYzZ.getCell()[j][0]=Integer.MAX_VALUE;
			mxXyYzZ.getCell()[j][1]=Integer.MIN_VALUE;
		}
		for (int i=0; i<nVertex; i++) {
			for (int j=0; j<3; j++) {
				mxXyYzZ.getCell()[j][0]=Math.min(mxXyYzZ.getCell()[j][0],vertex[i].getCell()[j][0]);
				mxXyYzZ.getCell()[j][1]=Math.max(mxXyYzZ.getCell()[j][1],vertex[i].getCell()[j][0]);
			}
		}
		return mxXyYzZ;
	}
	
}
