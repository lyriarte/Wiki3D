/*
 * Copyright (c) 2008, Luc Yriarte
 * All rights reserved.
 * 
 * @file Buffered3DGraphics.java
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

import org.yriarte.graphics.Graphics;
import org.yriarte.graphics.Image;

public class Buffered3DGraphics {

	private int width, height, focal;
	
	private int drawMode;
	
	private Object3D model;
	private Image bufferedImage;
	

	public Buffered3DGraphics(Object3D o, Image i, int w, int h, int f) {
		super();
		init(o, i, w, h, f);
	}
	
	public void init(Object3D o, Image i, int w, int h, int f) {
		drawMode = Object3D.DRAW_DEFAULT;
		model = o;
		width = w;
		height = h;
		focal = f;
		bufferedImage = i;
	}

	public void updateModel() {
		model.setDrawMode(drawMode);
		model.update(focal, width, height);
		bufferedImage.getGraphics().clearRect(0,0,width,height);
		model.paint(bufferedImage.getGraphics());
	}

	public void updateView(Graphics g) {
		g.drawImage(bufferedImage,0,0,null);
	}

	public int getDrawMode() {
		return drawMode;
	}

	public void setDrawMode(int drawMode) {
		this.drawMode = drawMode;
	}

    public int getFocal() {
        return focal;
    }
    
    public void setFocal(int focal) {
        this.focal = focal;
    }
    
    public Object3D getModel() {
        return model;
    }
    
    public void setModel(Object3D model) {
        this.model = model;
    }
    
}
