/*
 * Copyright (c) 2008, Luc Yriarte
 * All rights reserved.
 * 
 * @file Wiki3DFileReader.java
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

import java.awt.Frame;
import java.io.FileInputStream;
import java.io.InputStream;

import org.yriarte.awtApp.Mini3DPanel;
import org.yriarte.mini3D.Animation;
import org.yriarte.mini3D.Object3D;

public class Wiki3DFileReader {

	static int BUFSIZE=4096;
	
	public static void main(String[] args) {
	    if (args.length != 1) {
	        System.out.println("No Wiki3D file to open, exiting");
	        return;
	    }
		try {
			InputStream is = new FileInputStream(args[0]);
			byte buffer[] = new byte[BUFSIZE];
			String str = "";
			int nb;
			while ((nb = is.read(buffer)) > 0) {
				str += new String(buffer,0,nb);
			}
			Object3D obj = Object3DFactory.NewObject3D(str);
			Animation anime = null;

			// ---- ---- initialize 3D panel
			Frame frm = new Frame();
			frm.setSize(600,600);
			frm.setTitle(args[0]);
			Mini3DPanel pnl = new Mini3DPanel();
			frm.add(pnl);
			frm.show();
			pnl.init(obj,0,0,null,null);
			frm.validate();
			// ---- ---- parsed animation ?
			if (Object3DFactory.getAnimation() != null) {
				anime = Object3DFactory.getAnimation();
				Object3DFactory.resetAnimation();
				anime.addObserver(pnl);
				anime.setGraphics(pnl.getGraphics3D());
				Thread animeThread = new Thread(anime);
				animeThread.start();
			}		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
