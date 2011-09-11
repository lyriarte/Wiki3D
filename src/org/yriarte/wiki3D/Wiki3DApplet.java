/*
 * Copyright (c) 2010, Luc Yriarte
 * All rights reserved.
 * 
 * @file Wiki3DApplet.java
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

import java.applet.Applet;
import java.awt.BorderLayout;

import org.yriarte.awtApp.Mini3DPanel;
import org.yriarte.mini3D.Animation;
import org.yriarte.mini3D.Object3D;
import org.yriarte.mini3D.PolySphere;

public class Wiki3DApplet extends Applet {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Mini3DPanel panel;

    public Wiki3DApplet() {
        super();
        panel = new Mini3DPanel();
        addComponentListener(panel);
    }

	public void init() {
		String str;
		Object3D obj = null;
		Animation anime = null;
		int focal=0;
		double zoom=0;
		
		str = getParameter("Z");
		if (str != null)
			zoom = Double.valueOf(str).doubleValue();
		
		str = getParameter("N");
		if (str != null)
			Object3D.N_DEFAULT = Integer.valueOf(str).intValue();
		
		str = getParameter("Focal");
		if (str != null)
			focal = Integer.valueOf(str).intValue();
		
		str = getParameter("Model");
		obj = (str == null) ? new PolySphere(100) : Object3DFactory.NewObject3D(str);

		// ---- ---- initialize 3D panel
		panel.setBounds(this.getBounds());
		this.setLayout(new BorderLayout());
		this.add(panel,BorderLayout.CENTER);
		panel.init(obj,focal,zoom,null,null);
		
		// ---- ---- parsed animation ?
		if (Object3DFactory.getAnimation() != null) {
			anime = Object3DFactory.getAnimation();
			Object3DFactory.resetAnimation();
			anime.addObserver(panel);
			anime.setGraphics(panel.getGraphics3D());
			Thread animeThread = new Thread(anime);
			animeThread.start();
		}		
		
	}
	
	
}
