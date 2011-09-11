package org.yriarte.awtGraphics;

import org.yriarte.graphics.Graphics;
import org.yriarte.graphics.Image;

public class AwtImage implements Image {

	private java.awt.Image awtImage;
	
	public AwtImage(java.awt.Image awtImage) {
		super();
		this.awtImage = awtImage;
	}

	/**
	 * @return the awtImage
	 */
	protected java.awt.Image getAwtImage() {
		return awtImage;
	}

	/**
	 * @param awtImage the awtImage to set
	 */
	protected void setAwtImage(java.awt.Image awtImage) {
		this.awtImage = awtImage;
	}

	public Graphics getGraphics() {
		return new AwtGraphics(awtImage.getGraphics());
	}

}
