/*
 * Copyright (c) 2010, Luc Yriarte
 * All rights reserved.
 * 
 * @file Object3DFactory.java
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
package org.yriarte.wiki3D;

import java.util.StringTokenizer;

import org.yriarte.graphics.Color;
import org.yriarte.mini3D.Animation;
import org.yriarte.mini3D.Box;
import org.yriarte.mini3D.Grid;
import org.yriarte.mini3D.Object3D;
import org.yriarte.mini3D.PolyCone;
import org.yriarte.mini3D.PolyCylinder;
import org.yriarte.mini3D.PolySphere;
import org.yriarte.mini3D.TransformList;

public class Object3DFactory {

	static Animation parsedAnimation;
	static int currStep, currLoop, DEFAULT_MS = 83; // 12 fps;

	static public Animation getAnimation() {
		return parsedAnimation;
	}
	
	static public void resetAnimation() {
		parsedAnimation = null;
	}
	
	static public Object3D NewObject3D(String str) {
		parsedAnimation = null;
		StringTokenizer st = new StringTokenizer(str, " \t\n(),=");
		Object3D obj = Object3DFactory.parseTokens(st);
		obj.reset();
		return obj;
	}

	static public Object3D parseTokens(StringTokenizer st) {
		boolean skip = false;
		String tok = "";
		Object3D obj = new Object3D();
		TransformList trl = null;
		Animation anime = null;
		double degToRad= 2 * Math.PI / 360;
		double trnX, trnY, trnZ, trn0X,  trn0Y,  trn0Z, rotX, rotY, rotZ, rot0X,  rot0Y, rot0Z;
		double a, b, c;
		int n0, n;
		trnX= trnY= trnZ= trn0X=  trn0Y=  trn0Z= rotX= rotY= rotZ= rot0X=  rot0Y= rot0Z= 0;
		a = b = c = -1;
		n0 = -1;
		n = Object3D.N_DEFAULT;
		while (st.hasMoreTokens()) {
			if (!skip)
				tok = st.nextToken();
			skip = false;
			if (tok.equals("{")) {
				obj.addChild(Object3DFactory.parseTokens(st));
			} else if (tok.equals("}")) {
				obj.reset(trnX, trnY, trnZ, trn0X, trn0Y, trn0Z, rotX, rotY, rotZ, rot0X, rot0Y, rot0Z);
				return obj;
			} else if (tok.equals("]")) {
				obj.reset(trnX, trnY, trnZ, trn0X, trn0Y, trn0Z, rotX, rotY, rotZ, rot0X, rot0Y, rot0Z);
				return obj;
			} else if (tok.equals("[")) {
				currStep = -1;
				currLoop = 0 ;
				// parse a temporary object to get the transformation matrix
				Object3D tmpobj = Object3DFactory.parseTokens(st);
				TransformList tmptrl = new TransformList(tmpobj.getTransformation(),currStep);
				TransformList itrl ;
				// append to the current animation's transform list
				if (trl == null) {
					trl = tmptrl;
				}
				else {
					itrl = trl;
					while (itrl.getNext() != null)
						itrl = itrl.getNext();
					itrl.setNext(tmptrl);
				}
				// goto 1-based list item index
				if (currLoop > 0) {
					itrl = trl;
					for (int i = 1; i < currLoop; i++)
						itrl = itrl.getNext();
					tmptrl.setNext(itrl);
				}
				// new animation
				if (anime == null) {
					Object3D tabobj[] = new Object3D[1];
					tabobj[0] = obj;
					TransformList tabtrl[] = new TransformList[1];
					tabtrl[0] = trl;
					anime = new Animation();
					anime.init(null,1,tabobj,tabtrl,DEFAULT_MS);
					// if an animation is already created append
					if (parsedAnimation != null)
						parsedAnimation = parsedAnimation.merge(anime);
				}
				// first animation
				if (parsedAnimation == null)
					parsedAnimation = anime;
			} else if (tok.equals("step")) {
				tok = st.nextToken();
				currStep = new Integer(tok).intValue();
			} else if (tok.equals("goto")) {
				tok = st.nextToken();
				currLoop = new Integer(tok).intValue();
			} else if (tok.equals("color")) {
				tok = st.nextToken();
				a = new Integer(tok).intValue();
				tok = st.nextToken();
				b = new Integer(tok).intValue();
				tok = st.nextToken();
				c = new Integer(tok).intValue();
				obj.setColor(new Color((int) a, (int) b, (int) c));
			} else if (tok.equals("trn")) {
				tok = st.nextToken();
				trnX = new Double(tok).doubleValue();
				tok = st.nextToken();
				trnY = new Double(tok).doubleValue();
				tok = st.nextToken();
				trnZ = new Double(tok).doubleValue();
			} else if (tok.equals("trn0")) {
				tok = st.nextToken();
				trn0X = new Double(tok).doubleValue();
				tok = st.nextToken();
				trn0Y = new Double(tok).doubleValue();
				tok = st.nextToken();
				trn0Z = new Double(tok).doubleValue();
			} else if (tok.equals("rot")) {
				tok = st.nextToken();
				rotX = new Float(tok).floatValue()*degToRad;
				tok = st.nextToken();
				rotY = new Float(tok).floatValue()*degToRad;
				tok = st.nextToken();
				rotZ = new Float(tok).floatValue()*degToRad;
			} else if (tok.equals("rot0")) {
				tok = st.nextToken();
				rot0X = new Float(tok).floatValue()*degToRad;
				tok = st.nextToken();
				rot0Y = new Float(tok).floatValue()*degToRad;
				tok = st.nextToken();
				rot0Z = new Float(tok).floatValue()*degToRad;
			} else if (tok.equals("cylinder")) {
				for (int i = 0; i < 2; i++) {
					tok = st.nextToken();
					if (tok.equals("h")) {
						tok = st.nextToken();
						a = new Double(tok).doubleValue();
					} else if (tok.equals("r")) {
						tok = st.nextToken();
						b = new Double(tok).doubleValue();
					} else if (tok.equals("n")) {
						tok = st.nextToken();
						n = new Integer(tok).intValue();
						i--;
					} else if (tok.equals("p")) {
						tok = st.nextToken();
						n0 = new Integer(tok).intValue();
						i--;
					}
				}
				obj = new PolyCylinder(n0 != -1 ? n0 : n, n, a, b);
			} else if (tok.equals("cone")) {
				for (int i = 0; i < 2; i++) {
					tok = st.nextToken();
					if (tok.equals("h")) {
						tok = st.nextToken();
						a = new Double(tok).doubleValue();
					} else if (tok.equals("r")) {
						tok = st.nextToken();
						b = new Double(tok).doubleValue();
					} else if (tok.equals("n")) {
						tok = st.nextToken();
						n = new Integer(tok).intValue();
						i--;
					} else if (tok.equals("p")) {
						tok = st.nextToken();
						n0 = new Integer(tok).intValue();
						i--;
					}
				}
				obj = new PolyCone(n0 != -1 ? n0 : n, n, a, b);
			} else if (tok.equals("sphere")) {
				b = 1;
				boolean full = true;
				for (int i = 0; i < 2; i++) {
					tok = st.nextToken();
					if (!(tok.equals("r") || tok.equals("f") || tok.equals("n") || tok.equals("p") || tok.equals("hollow"))) {
						skip = true;
						break;
					}
					if (tok.equals("r")) {
						tok = st.nextToken();
						a = new Double(tok).doubleValue();
					}
					if (tok.equals("f")) {
						tok = st.nextToken();
						b = new Double(tok).doubleValue();
					}
					if (tok.equals("n")) {
						tok = st.nextToken();
						n = new Integer(tok).intValue();
						i--;
					}
					if (tok.equals("p")) {
						tok = st.nextToken();
						n0 = new Integer(tok).intValue();
						i--;
					}
					if (tok.equals("hollow")) {
						full = false;
						i--;
					}
				}
				obj = new PolySphere(n0 != -1 ? n0 : n, n, a, b, full);
			} else if (tok.equals("box")) {
				n = 1;
				for (int i = 0; i < 3; i++) {
					tok = st.nextToken();
					if (tok.equals("w")) {
						tok = st.nextToken();
						a = new Double(tok).doubleValue();
					} else if (tok.equals("h")) {
						tok = st.nextToken();
						b = new Double(tok).doubleValue();
					} else if (tok.equals("l")) {
						tok = st.nextToken();
						c = new Double(tok).doubleValue();
					} else if (tok.equals("f")) {
						tok = st.nextToken();
						n = new Integer(tok).intValue();
						i--;
					}
				}
				obj = new Box(a, b, c, n);
			} else if (tok.equals("grid")) {
				for (int i = 0; i < 2; i++) {
					tok = st.nextToken();
					if (tok.equals("n")) {
						tok = st.nextToken();
						n = new Integer(tok).intValue();
					} else if (tok.equals("u")) {
						tok = st.nextToken();
						a = new Double(tok).doubleValue();
					}
				}
				obj = new Grid(a, n);
			} else
				System.err.println("??" + tok);
		}
		return obj;
	}

}