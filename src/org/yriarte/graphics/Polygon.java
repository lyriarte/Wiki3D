/*
 * Copyright (c) 2010, Luc Yriarte
 * All rights reserved.
 * 
 * @file Polygon.java
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
 
package org.yriarte.graphics;

public class Polygon {

	private int[] xP;
	private int[] yP;
	private int nEdge;
	
	/**
	 * @param xP
	 * @param yP
	 * @param nEdge
	 */
	public Polygon(int[] xP, int[] yP, int nEdge) {
		super();
		this.xP = xP;
		this.yP = yP;
		this.nEdge = nEdge;
	}

	/**
	 * @return the xP
	 */
	public int[] getxP() {
		return xP;
	}

	/**
	 * @return the yP
	 */
	public int[] getyP() {
		return yP;
	}

	/**
	 * @return the nEdge
	 */
	public int getnEdge() {
		return nEdge;
	}
	
}