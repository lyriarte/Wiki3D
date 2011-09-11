/*
 * Copyright (c) 2008, Luc Yriarte
 * All rights reserved.
 * 
 * @file Object3D.java
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

import org.yriarte.graphics.Color;
import org.yriarte.graphics.Graphics;
import org.yriarte.graphics.Polygon;
import org.yriarte.math.Matrix;

/**
 * Object3D : Base class for all 3D Objects.
 * 
 * Left handed coordinates.
 * Every 3D object has its own referential as a transformation matrix 
 * relative to its parent in the composition hierarchy.
 */
public class Object3D {
	
	/** 
	 * Object3D class constants 
	 */
	/** Draw mode: wireframe or facets facets */
	public static final int DRAW_WIREFRAME = 0;
	public static final int DRAW_FACETS = 1; 
	/** Color shading per object */
	public static final int BRIGHT_SCALE = 255;
	
	/** 
	 * Object3D class variables 
	 */
	/** Default Draw mode */ 
	public static int DRAW_DEFAULT = DRAW_FACETS;
	/** Number of segments to approximate a circle */
	public static int N_DEFAULT = 12; 
	/** Default object color */
	public static Color COLOR_DEFAULT = new Color(127,127,127);
	
	/** 
	 * Object3D instance variables 
	 */
	
	/** Color shading */
	protected Color color = null;
	protected int drawMode = DRAW_DEFAULT;
	protected float brightIncLocal = 0;
	protected int brightDelta = 0;

	/** Position, Orientation and combined transformation on mesh vertices */
	protected Matrix position, orientation, transformation;
	protected Matrix updatedTransform = null;
	protected Mesh mesh;
	
	/** Object component tree */
	protected int nChild = 0, nFacet = 0, nFacetTotal = 0;
	protected Object3D[] child = null;
	protected Object3D parent = null;
	/** This object's facets */
	protected PolygonEdgeIndexes[] facet = null;
	/** This object's facets and its children's*/
	protected PolygonEdgeIndexes[] facetTotal = null;
	/** Select a vertex on this object or one of its child's mesh */
	protected int selectedVertex = -1;
	protected Object3D selectedObject = null;
	
	public Object3D() {
		mesh = new Mesh();
		position = new Matrix3D();
		orientation = new Matrix3D();
		transformation = new Matrix3D();
	}

	public Object3D reset() {
		return reset(0,0,0,0,0,0,0,0,0,0,0,0);
	}
	
	/**
	 * @brief sets the object's coordinate system relative to its parents, 
	 * and every vertex of the mesh relative to the object's oordinate system
	 * 
	 * @param trnX translation on the x axis
	 * @param trnY translation on the y axis
	 * @param trnZ translation on the z axis
	 * @param trn0X translate the mesh on the x axis
	 * @param trn0Y translate the mesh on the y axis
	 * @param trn0Z translate the mesh on the z axis
	 * @param rotX rotation on the x axis, in radians
	 * @param rotY rotation on the y axis, in radians
	 * @param rotZ rotation on the z axis, in radians
	 * @param rot0X rotate the mesh on the x axis, in radians
	 * @param rot0Y rotate the mesh on the y axis, in radians
	 * @param rot0Z rotate the mesh on the z axis, in radians
	 * 
	 * @return this Object3D
	 */
	public Object3D reset(double trnX, double trnY, double trnZ, 
			double trn0X, double trn0Y, double trn0Z,
			double rotX, double rotY, double rotZ, 
			double rot0X, double rot0Y, double rot0Z) {
		selectedVertex = -1;
		selectedObject = null;
		if (rot0X != 0)
			mesh.transformThis(Matrix3D.rotationX(rot0X));
		if (rot0Y != 0)
			mesh.transformThis(Matrix3D.rotationY(rot0Y));
		if (rot0Z != 0)
			mesh.transformThis(Matrix3D.rotationZ(rot0Z));
		if (trn0X != 0 || trn0Y != 0 || trn0Z != 0)
			mesh.transformThis(Matrix3D.translation(trn0X, trn0Y, trn0Z));
		if (rotX != 0)
			orientation = orientation.mul(Matrix3D.rotationX(rotX));
		if (rotY != 0)
			orientation = orientation.mul(Matrix3D.rotationY(rotY));
		if (rotZ != 0)
			orientation = orientation.mul(Matrix3D.rotationZ(rotZ));
		if (trnX != 0 || trnY != 0 || trnZ != 0)
			position = position.mul(Matrix3D.translation(trnX, trnY, trnZ));
		transformation = position.mul(orientation);
		updatedTransform = null;
		if (color == null) {
			if (parent != null)
				color = parent.color;
			else
				color = COLOR_DEFAULT;
		}
		// shading colors
		brightIncLocal = nFacet != 0 ? BRIGHT_SCALE / nFacet : 0;
		nFacetTotal = nFacet;
		for (int i = 0; i < nChild; i++) {
			child[i].reset();
			nFacetTotal += child[i].nFacetTotal;
		}
		// only build the facet list if this is the toplevel object
		if (parent == null && nFacetTotal > 0) {
			facetTotal = new PolygonEdgeIndexes[nFacetTotal];
			buildFacetList(facetTotal, 0);
		}
		if (nFacetTotal == 0 && mesh != null)
			drawMode = DRAW_WIREFRAME;
		return this;
	}
	
	/**
	 * @brief Scales the mesh on the 3 axes
	 * @param scaleX
	 * @param scaleY
	 * @param scaleZ
	 * @return this Object3D
	 */
	public Object3D resetScale(double scaleX, double scaleY, double scaleZ) {
		if (scaleX != 1 || scaleY != 1 || scaleZ != 1)
			mesh.transformThis(Matrix3D.scale(scaleX, scaleY, scaleZ));
		return this;
	}

	/**
	 * @return the color
	 */
	public Color getColor() {
		return color;
	}
	
	/**
	 * @param color 
	 */
	public void setColor(Color color) {
		this.color = color;
	}
	
	/**
	 * @return the current transformation matrix
	 */
	public Matrix getTransformation() {
		return transformation;
	}

	/**
	 * @return the orientation matrix
	 */
	public Matrix getOrientation() {
		return orientation;
	}

	public Object3D setOrientation(Matrix orientation) {
		this.orientation = orientation;
		transformation = position.mul(orientation);
		updatedTransform = null;
		return this;
	}

	/**
	 * @return the position matrix
	 */
	public Matrix getPosition() {
		return position;
	}

	public Object3D setPosition(Matrix position) {
		this.position = position;
		transformation = position.mul(orientation);
		updatedTransform = null;
		return this;
	}

	/**
	 * @return the mesh
	 */
	public Mesh getMesh() {
		return mesh;
	}

	public Object3D setMesh(Mesh mesh) {
		this.mesh = mesh;
		return this;
	}

    public PolygonEdgeIndexes[] getFacet() {
        return facet;
    }
    
    public void setFacet(PolygonEdgeIndexes[] facet, int nFacet) {
        this.facet = facet;
        this.nFacet = nFacet;
    }
    
    public int getNFacet() {
        return nFacet;
    }
    
    /**
     * @brief apply a transformation matrix to the object's coordinate system 
     * @param trans a transformation matrix
     * @return this Object3D
     */
	public Object3D transform(Matrix trans) {
		transformation = transformation.mul(trans);
		updatedTransform = null;
		return this;
	}

    /**
     * @brief apply a transformation matrix to the all the object's mesh vertices 
     * @param trans a transformation matrix
     * @return this Object3D
     */
	public Object3D transformMesh(Matrix trans) {
		mesh.transformThis(trans);
		return this;
	}

    /**
     * @brief apply a transformation matrix to the all the object's mesh vertices,
     * and recursively to the object's children
     * @param trans a transformation matrix
     * @return this Object3D
     */
	public Object3D transformMeshes(Matrix trans) {
	    int i;
	    transformMesh(trans);
		for (i = 0; i < nChild; i++) {
			child[i].transformMeshes(trans);
		}
		return this;
	}

	/**
	 * 
	 * @param obj an Object3D
	 * @return this Object3D
	 */
	public Object3D addChild(Object3D obj) {
		int i;
		Object3D[] prevChild = child;
		child = new Object3D[nChild + 1];
		for (i = 0; i < nChild; i++) {
			child[i] = prevChild[i];
		}
		child[nChild] = obj;
		obj.parent = this;
		if (obj.color == null)
			obj.color = this.color;
		nChild++;
		return this;
	}

	public Object3D getChild(int i) {
		if (child == null)
			return null;
		return child[i];
	}

	/**
	 * @brief Builds a set of facets attached to the toplevel object,
	 * recursively descends on the children.
	 * 
	 * @param facetList the global facet list
	 * @param startIndex index in the list where to add the current object facets
	 * @return index where the next object will add its facets
	 */
	private int buildFacetList(PolygonEdgeIndexes[] facetList, int startIndex) {
		int i;
		for (i = 0; i < nFacet; i++) {
			facetList[startIndex++] = facet[i];
		}
		for (i = 0; i < nChild; i++) {
			startIndex = child[i].buildFacetList(facetList, startIndex);
		}
		// if this was a toplevel object at some point, but not anymore,  
		// drop the previous global facet list.
		if (parent != null)
			facetTotal = null;
		return startIndex;
	}

	private void sortFacetTotal() {
		int i, n;
		PolygonEdgeIndexes tmpFacet;
		n = nFacetTotal;
		do {
			tmpFacet = null;
			for (i = 1; i < n; i++) {
				if (facetTotal[i - 1].z < facetTotal[i].z) {
					tmpFacet = facetTotal[i];
					facetTotal[i] = facetTotal[i - 1];
					facetTotal[i - 1] = tmpFacet;
				}
			}
			n--;
		} while (tmpFacet != null);
	}

	private int updateWireframe(int focal, int screenX, int screenY) {
		int i, nEdge;
		nEdge = 0;
		if (parent != null && parent.updatedTransform != null)
			updatedTransform = parent.updatedTransform.mul(transformation);
		else
			updatedTransform = transformation;
		for (i = 0; i < nChild; i++) {
			nEdge += child[i].updateWireframe(focal, screenX, screenY);
		}
		if (mesh.getNEdge() > 0)
			mesh.setWireframe(mesh.transform(updatedTransform).updateWireframe(focal, screenX, screenY));
		return nEdge + mesh.getNEdge();
	}

	private void updateFacets(int focal, int screenX, int screenY) {
		int i;
		for (i = 0; i < nChild; i++) {
			child[i].updateFacets(focal, screenX, screenY);
		}
		for (i = 0; i < nFacet; i++) {
			facet[i].updatePolygon();
		}
	}

	public void update(int focal, int screenX, int screenY) {
		updateWireframe(focal, screenX, screenY);
		if (drawMode == DRAW_FACETS) {
			updateFacets(focal, screenX, screenY);
			if (facetTotal != null)
				sortFacetTotal();
		}
	}

	private Color colorDelta(Color baseColor, int brightDelta) {
		int rgb, r, g, b;
		rgb = baseColor.getRGB();
		r = Math.max(Math.min(((rgb & 0x00FF0000) >> 16) + brightDelta,255),0);
		g = Math.max(Math.min(((rgb & 0x0000FF00) >> 8) + brightDelta,255),0);
		b = Math.max(Math.min(((rgb & 0x000000FF)) + brightDelta,255),0);
		return new Color(r, g, b);
	}
	
	public void paint(Graphics graphics) {
		int i;
		brightDelta = -(BRIGHT_SCALE / 2);
		for (i = 0; i < nChild; i++)
			child[i].paint(graphics);
		if (drawMode == DRAW_FACETS && facetTotal != null) {
			for (i = 0; i < nFacetTotal; i++) {
				graphics.setColor(colorDelta(facetTotal[i].obj.color,facetTotal[i].obj.brightDelta));
				facetTotal[i].obj.brightDelta += facetTotal[i].obj.brightIncLocal;
				if (facetTotal[i].z < 0) {
					break;
				}
				facetTotal[i].paint(graphics);
			}
		} else if (drawMode == DRAW_WIREFRAME) {
			graphics.setColor(color);
			int nEdge = mesh.getNEdge();
			int lines[][] = mesh.getLines();
			for (i = 0; i < nEdge; i++) {
				if (lines[i][4] >= 0)
					graphics.drawLine(lines[i][0], lines[i][1],
							lines[i][2], lines[i][3]);
			}
		}
	}

	/**
	 * @param d draw mode: DRAW_WIREFRAME or DRAW_FACETS
	 * If there are no facets on this object, draw mode is forced to wireframe.
	 */
	public void setDrawMode(int d) {
		int i;
		drawMode = d;
		if (nFacetTotal == 0 && mesh != null)
			drawMode = DRAW_WIREFRAME;
		for (i = 0; i < nChild; i++)
			child[i].setDrawMode(d);
	}

	/**
	 * @brief Selects the first vertex where xy coordinates in the mesh
	 * wireframe are closer than r to xy parameters.
	 * @param x
	 * @param y
	 * @param r
	 * @return index of the selected vertex in the selected object
	 */
	public int selectVertex(int x, int y, int r) {
		int i;
		this.selectedVertex=-1;
		this.selectedObject=null;
//		 select vertex on this object mesh
		if (mesh != null) { 
			this.selectedVertex=mesh.selectVertex(x,y,r);
			if (this.selectedVertex!=-1) {
				this.selectedObject=this;
				return this.selectedVertex;
			}
		}
//		 look for a vertex on children's meshes
		for (i = 0; i < nChild; i++) {
			this.selectedVertex=child[i].selectVertex(x,y,r);
			if (this.selectedVertex!=-1) {
				this.selectedObject=child[i].selectedObject;					
				return this.selectedVertex;
			}
		}
		return this.selectedVertex;
	}
	
	public org.yriarte.mini3D.Vector3D getSelectedVertex() {
		if (this.selectedVertex!=-1)
			return this.selectedObject.getMesh().getVertex()[this.selectedVertex];
		return null;
	}
	
	/**
	 * @return [[xmin,xmax],[ymin,ymax],[zmin,zmax]]
	 */
	public Matrix calculateBounds() {
		Matrix mxXyYzZ = mesh.calculateBounds();
		for (int i = 0; i < nChild; i++) {
			Matrix mChildBounds = child[i].calculateBounds();
			for (int j=0; j<3; j++) {
				mxXyYzZ.getCell()[j][0]=Math.min(mxXyYzZ.getCell()[j][0],mChildBounds.getCell()[j][0]);
				mxXyYzZ.getCell()[j][1]=Math.max(mxXyYzZ.getCell()[j][1],mChildBounds.getCell()[j][1]);
			}
		}
		return mxXyYzZ;
	}
	
	
	/**
	 * @brief The awt Polygon links the last point to the first.
	 */
	public class PolygonEdgeIndexes {

		private Object3D obj;

		private int z = 0;
		private int nEdge = 0;
		public int edge[] = null;

		private Polygon polygon;

		public PolygonEdgeIndexes(Object3D o, int n) {
			obj = o;
			nEdge = n;
			edge = new int[n];
			z = 0;
		}

		public void updatePolygon() {
			int i,zMin,zMax;
			int xP[] = new int[nEdge];
			int yP[] = new int[nEdge];
			int lines[][] = obj.mesh.getLines();
			zMin = zMax = lines[edge[0]][4];
			for (i = 0; i < nEdge; i++) {
				xP[i] = lines[edge[i]][0];
				yP[i] = lines[edge[i]][1];
				if (zMin > lines[edge[i]][4])
					zMin = lines[edge[i]][4];
				if (zMax < lines[edge[i]][4])
					zMax = lines[edge[i]][4];
			}
			polygon = new Polygon(xP, yP, nEdge);
			// the midpoint of the z range
			z = (zMax + zMin) >> 1;
		}

		public void updatePolygon(int xP[], int yP[]) {
			polygon = new Polygon(xP, yP, nEdge);
		}

		public void paint(Graphics g) {
			g.fillPolygon(polygon);
		}

	}
	
}
