/*
 * Copyright (c) 2010, Luc Yriarte
 * All rights reserved.
 * 
 * @file Color.java
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

public class Color {
	private byte r,g,b;

	/**
	 * @param r red
	 * @param g green
	 * @param b blue
	 */
	public Color(int r, int g, int b) {
		super();
		this.r = (byte)r;
		this.g = (byte)g;
		this.b = (byte)b;
	}
	
	/**
	 * @param r red
	 * @param g green
	 * @param b blue
	 */
	public Color(float r, float g, float b) {
		super();
		this.r = (byte)( 255 * r);
		this.g = (byte)( 255 * g);
		this.b = (byte)( 255 * b);
	}
	
	public int getRGB() {
		return (0x000000ff & ((int) b)) 
			| (0x0000ff00 & (((int) g) << 8)) 
			| (0x00ff0000 & (((int) r) << 16));
	}
}
