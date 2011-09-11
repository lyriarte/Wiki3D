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

import java.util.Observable;

import org.yriarte.math.Matrix;
import org.yriarte.threads.ManagedRunnable;

public class Animation extends Observable implements ManagedRunnable {
	
	private int nObjects, millisec;
	private Object3D object[];
	private TransformList trans[];
	private Buffered3DGraphics objectGraphics;
	/** run state */
	protected boolean running = true;
	
	public void init(Buffered3DGraphics g, int n, 
			Object3D o[], TransformList tr[], int ms) {
		objectGraphics = g;
		nObjects = n;
		object = o;
		trans = tr;
		millisec = ms;
	}
	
	public Animation() {
		super();
	}
	
	public Animation(Buffered3DGraphics g, Object3D obj, int ms) {
		super();
		Object3D o[] = new Object3D[1];
		o[0] = obj;
		TransformList tr[] = new TransformList[1];
		tr[0] = null;
		init(g,1,o,tr,ms);
	}

	public Animation merge(Animation a) {
		Object3D obj[];
		TransformList trl[];
		double fms = millisec / a.millisec;
		int n = a.nObjects + nObjects;
		obj = new Object3D[n];
		trl = new TransformList[n];
		int i;
		for (i = 0; i < nObjects; i++) {
			obj[i] = object[i];
			trl[i] = trans[i];
		}
		for (i = nObjects; i < n; i++) {
			obj[i] = a.object[nObjects - i];
			trl[i] = a.trans[nObjects - i];
			trl[i].setnStep((int)(trl[i].getnStep()*fms));
		}
		init(objectGraphics, n, obj, trl, millisec);
		return this;
	}

	public Animation setGraphics(Buffered3DGraphics g) {
		objectGraphics = g;
		return this;
	}
	
	public Animation setObjectLoop(Object3D obj, boolean loop) {
		int i = 0;
		TransformList currTrl;
		while (i < nObjects) {
			if (object[i] == obj) {
				currTrl = trans[i];
				if(currTrl != null) {
					while (currTrl.getNext() != null && currTrl.getNext() != trans[i])
						currTrl = currTrl.getNext();
					currTrl.setNext(loop ? trans[i] : null);
				}
				break;
			}
		}
		return this;
	}
	
	public Animation addObjectTransformation(Object3D obj, Matrix transform, int nStep) {
		int i = 0;
		TransformList currTrl, newTrl;
		while (i < nObjects) {
			if (object[i] == obj) {
				newTrl = new TransformList(transform, nStep);
				currTrl = trans[i];
				if(currTrl == null)
					trans[i] = newTrl;
				else {
					while (currTrl.getNext() != null && currTrl.getNext() != trans[i])
						currTrl = currTrl.getNext();
					if (currTrl.getNext() == trans[i])
						newTrl.setNext(trans[i]);
					currTrl.setNext(newTrl);
				}
				break;
			}
		}
		return this;
	}
	
	public void run() {
		int i;
		boolean changed = true;
		while (running && changed && nObjects != 0) {
			changed = false;
			for (i = 0; i < nObjects; i++) {
				if (trans[i] != null) {
					changed = true;
					object[i].transform(trans[i].getTransform());
					trans[i] = trans[i].next();
				}
			}
			if (changed) {
				objectGraphics.updateModel();
				setChanged();
				notifyObservers(objectGraphics);
			}
			try {
				Thread.sleep(millisec);
			} catch (Exception e) {
				System.out.println("Animation exception " + e.toString());
			}
		}
	}

	/**
	 * Implement requestStop method
	 */
	public void requestStop() {
		running = false;
	}

	
}
