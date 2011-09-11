package org.yriarte.awtGraphics;

import java.awt.image.ImageObserver;

import org.yriarte.graphics.Color;
import org.yriarte.graphics.Graphics;
import org.yriarte.graphics.Image;
import org.yriarte.graphics.Polygon;

public class AwtGraphics implements Graphics {

	private java.awt.Graphics awtGraphics;
	
	public AwtGraphics(java.awt.Graphics awtGraphics) {
		super();
		this.awtGraphics = awtGraphics;
	}

	/**
	 * @return the awtGraphics
	 */
	protected java.awt.Graphics getAwtGraphics() {
		return awtGraphics;
	}

	/**
	 * @param awtGraphics the awtGraphics to set
	 */
	protected void setAwtGraphics(java.awt.Graphics awtGraphics) {
		this.awtGraphics = awtGraphics;
	}

	public void setColor(Color color) {
		awtGraphics.setColor(color != null ? new java.awt.Color(color.getRGB()) : java.awt.Color.gray);
	}

	public void drawLine(int x1, int y1, int x2, int y2) {
		awtGraphics.drawLine(x1, y1, x2, y2);
	}

	public void drawImage(Image img, int x, int y, Object object) {
		awtGraphics.drawImage(((AwtImage)img).getAwtImage(), x, y, (ImageObserver) object);
	}

	public void clearRect(int x, int y, int width, int height) {
		awtGraphics.clearRect(x, y, width, height);
	}

	public void fillPolygon(Polygon polygon) {
		awtGraphics.fillPolygon(polygon.getxP(), polygon.getyP(), polygon.getnEdge());
	}

}
