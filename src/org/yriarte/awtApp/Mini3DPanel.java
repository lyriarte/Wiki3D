/*
 * Copyright (c) 2008, Luc Yriarte
 * All rights reserved.
 * 
 * @file Mini3DPanel.java
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
package org.yriarte.awtApp;

import java.awt.Button;
import java.awt.Checkbox;
import java.awt.Choice;
import java.awt.Event;
import java.awt.Graphics;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Observable;
import java.util.Observer;

import org.yriarte.awtGraphics.AwtGraphics;
import org.yriarte.awtGraphics.AwtImage;
import org.yriarte.math.Matrix;
import org.yriarte.mini3D.Buffered3DGraphics;
import org.yriarte.mini3D.Matrix3D;
import org.yriarte.mini3D.Object3D;

public class Mini3DPanel extends Panel 
	implements ActionListener, ComponentListener, ItemListener, Observer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final static int NAV_ROTATION = 0, NAV_TRANSLATION = 1, NAV_ZOOM = 2, NAV_FOCAL = 3;

	private Button toggleControls = null;
	private Checkbox drawFacets = null;
	private Choice navMenu = null;
	private TextField focalField = null, zoomField = null;
	private Label focalLabel = null, zoomLabel = null;

	private Object3D model = null;
	private Buffered3DGraphics graphics3D = null;
	
	private int width, height, dragX, dragY;
	
	private int focal=0;
	private double zoom=0;
	private boolean controlsVisible = true;
	private boolean controlsRepaint = true;
	
	private Matrix startPosition, startOrientation;
	
	public Mini3DPanel() {
		super();
		
		toggleControls = new Button("...");
		toggleControls.addActionListener(this);
		
		drawFacets = new Checkbox("Facets", true);
		drawFacets.addItemListener(this);

		navMenu = new Choice();
		navMenu.addItemListener(this);
		navMenu.addItem("Rotate");
		navMenu.addItem("Translate");
		navMenu.addItem("Zoom");
		navMenu.addItem("Focal");
		
		focalLabel = new Label(" Focal:");
		focalField = new TextField(4);
		focalField.addActionListener(this);
		
		zoomLabel = new Label(" Zoom:");
		zoomField = new TextField(4);
		zoomField.addActionListener(this);
	}

	public void showControls(boolean show) {
	    navMenu.setVisible(show);
	    drawFacets.setVisible(show);
	    focalLabel.setVisible(show);
	    focalField.setVisible(show);
	    zoomLabel.setVisible(show);
	    zoomField.setVisible(show);
	}
	
    /**
     * @return Returns the model.
     */
    public Object3D getModel() {
        return model;
    }

    public Buffered3DGraphics getGraphics3D() {
		return graphics3D;
	}

    public void init(Object3D scene, int focalArg, double zoomFactor, Matrix position, Matrix orientation) {
		width = this.getSize().width;
		height = this.getSize().height;
		
		model = scene;
		Matrix bounds = model.calculateBounds();

		focal = focalArg;
		if (focal == 0)
			focal = Math.max(width, height) / 2;
		
		zoom = zoomFactor;
		if (zoom == 0)
		    zoom = (bounds.getCell()[2][1]-bounds.getCell()[2][0]) / Math.max(width, height);
		
		add(toggleControls);
		add(navMenu);
		add(drawFacets);
		add(focalLabel); add(focalField);
		add(zoomLabel); add(zoomField);
		
		org.yriarte.graphics.Image img = new AwtImage(this.createImage(width,height));
		if (graphics3D != null)
			graphics3D.init(model, img, width, height, focal);
		else
			graphics3D = new Buffered3DGraphics(model, img, width, height, focal);
		graphics3D.setDrawMode(drawFacets.getState() ? Object3D.DRAW_FACETS : Object3D.DRAW_WIREFRAME);

		model.reset();
		
		// ---- ---- set point of view out of the bounding box on x axis
		startPosition = position;
		if (position == null)
			position = Matrix3D.translation(0, 0, (bounds.getCell()[0][1]-bounds.getCell()[0][0])/zoom);
		model.setPosition(position);
		startOrientation = orientation;
		if (orientation != null)
			model.setOrientation(orientation);
		
		// ---- ---- update and paint
		graphics3D.updateModel();
		repaint();
	}
	
    public void init(Object3D scene) {
        init(scene,0,0,null,null);
    }
    
	public void paint(Graphics g) {
		org.yriarte.graphics.Graphics graph = new AwtGraphics(g);
		if (graphics3D!=null)
			graphics3D.updateView(graph);		
	}

	public void update(Graphics g) {
		focalField.setText(Integer.toString(graphics3D.getFocal()));
		zoomField.setText(Double.toString(zoom));
		if (controlsRepaint) {
			showControls(controlsVisible);
			controlsRepaint = false;
		}
		this.paint(g);
	}

	public boolean mouseDown(Event arg0, int arg1, int arg2) {
		dragX = arg1;
		dragY = arg2;
		return true;
	}

	public boolean mouseDrag(Event arg0, int arg1, int arg2) {
		double deltaX, deltaY, deltaZ;
		int navcmd = navMenu.getSelectedIndex();
		switch (navcmd) {
		case NAV_ROTATION: 
			deltaX = (arg1 - dragX) * 2 * Math.PI / width;
			deltaY = (dragY - arg2) * 2 * Math.PI / height;
			model.setOrientation(model.getOrientation().mul(
							Matrix3D.rotationY(deltaX).mul(
									Matrix3D.rotationX(deltaY))));
			break;
		case NAV_TRANSLATION:
			deltaX = zoom * (arg1 - dragX);
			deltaY = zoom * (dragY - arg2);
			model.setPosition(model.getPosition().mul(
					Matrix3D.translation(deltaX, deltaY, 0)));
			break;
		case NAV_FOCAL:
			graphics3D.setFocal(graphics3D.getFocal() + 100 * (arg2 - dragY) / height);
			break;
		case NAV_ZOOM:
			deltaZ = zoom * (arg2 - dragY);
			model.setPosition(model.getPosition().mul(
					Matrix3D.translation(0, 0, deltaZ)));
			break;
		}
		graphics3D.updateModel();
		repaint();
		dragX = arg1;
		dragY = arg2;
		return true;
	}

	public boolean mouseUp(Event arg0, int arg1, int arg2) {
		dragX = dragY = 0;
		return true;
	}

	public void itemStateChanged(ItemEvent arg0) {
		Object src = arg0.getSource();
		if (src == drawFacets) {
			graphics3D.setDrawMode(drawFacets.getState() ? Object3D.DRAW_FACETS : Object3D.DRAW_WIREFRAME);
			graphics3D.updateModel();
			repaint();
		}
	}

	public void actionPerformed(ActionEvent arg0) {
		Object src = arg0.getSource();
		if (src == toggleControls) {
		    controlsVisible = !controlsVisible;
		    controlsRepaint = true;
		    repaint();
		}
		else if (src == focalField) {
		    graphics3D.setFocal(Integer.valueOf(focalField.getText()).intValue());
			graphics3D.updateModel();
			repaint();
		}
		else if (src == zoomField) {
		    zoom = Double.valueOf(zoomField.getText()).doubleValue();		    
		}
	}

	public void update(Observable arg0, Object arg1) {
		repaint();
	}

	public void componentHidden(ComponentEvent e) {
	}

	public void componentMoved(ComponentEvent e) {
	}

	public void componentResized(ComponentEvent e) {
		if (model != null)
			init(model, 0, 0, startPosition, startOrientation);
	}

	public void componentShown(ComponentEvent e) {
	}

}
