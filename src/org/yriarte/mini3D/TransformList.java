/*
 * Copyright (c) 2008, Luc Yriarte
 * All rights reserved.
 * 
 * @file Animation.java
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


public class TransformList {

	private TransformList next;
	private Matrix transform;
	private int nStep, current;

	public TransformList() {
		next = null;
		transform = new Matrix3D();
		nStep = current = -1;
	}

	public TransformList(Matrix transform, int nStep) {
		next = null;
		this.transform = transform;
		this.nStep = current = nStep - 1;
	}

	public TransformList(Matrix transform, int nStep, TransformList next) {
		this.next = next;
		this.transform = transform;
		this.nStep = current = nStep - 1;
	}

	/**
	 * @return the nStep
	 */
	public int getnStep() {
		return nStep;
	}

	/**
	 * @param nStep the nStep to set
	 */
	public void setnStep(int nStep) {
		this.nStep = nStep;
	}

	/**
	 * @return the next
	 */
	public TransformList getNext() {
		return next;
	}

	/**
	 * @param next the next to set
	 */
	public void setNext(TransformList next) {
		this.next = next;
	}

	/**
	 * @return the transform
	 */
	public Matrix getTransform() {
		return transform;
	}

	public TransformList next() {
		if (current < 0)
			return this;
		if (current == 0) {
			current = nStep;
			return next;
		}
		current--;
		return this;
	}
}
