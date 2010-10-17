/**
 * Balloontip - Balloon tips for Java Swing applications
 * Copyright 2007-2010 Bernhard Pauler, Tim Molderez
 * 
 * This file is part of Balloontip.
 * 
 * Balloontip is free software: you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * Balloontip is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Balloontip. If not, see <http://www.gnu.org/licenses/>.
 */

package net.java.balloontip.styles;

import java.awt.Component;
import java.awt.Insets;

import javax.swing.border.Border;

/**
 * Implement this interface to create a BalloonTip style
 * @author Tim Molderez
 */
public abstract class BalloonTipStyle implements Border {
	protected int horizontalOffset = 0;
	protected int verticalOffset = 0;
	protected boolean flipX = false;
	protected boolean flipY = false;
	
	/**
	 * Sets a new value for the horizontal offset.
	 * @param px	horizontal offset (in pixels)
	 */
	public void setHorizontalOffset(int px) {
		horizontalOffset = px;
	}
	
	/**
	 * Sets a new value for the vertical offset.
	 * @param px	horizontal offset (in pixels)
	 */
	public void setVerticalOffset(int px) {
		verticalOffset = px;
	}
	
	/**
	 * Get the minimum value of the horizontal offset
	 * (Also useful as a maximum; maximum horizontaloffset = balloon tip width - minimum horizontal offset)
	 * @return Minimul horizontal offset
	 */
	public int getMinimalHorizontalOffset() {
		return verticalOffset;
	}
	
	/**
	 * Flip around the vertical axis
	 * @param flipX		if true, the balloon tip is flipped horizontally
	 */
	public void flipX(boolean flipX) {
		this.flipX = flipX;
	}
	
	/**
	 * Flip around the horizontal axis
	 * @param flipY		if true, the balloon tip is flipped vertically
	 */
	public void flipY(boolean flipY) {
		this.flipY = flipY;
	}
	
	/**
	 * Which mirror effect should be applied to the balloon tip
	 * @param flipX		if true, the balloon tip is flipped horizontally
	 * @param flipY		if true, the balloon tip is flipped vertically
	 */
	public void flip(boolean flipX, boolean flipY) {
		this.flipX = flipX;
		this.flipY = flipY;
	}
	
	/**
	 * Is this balloon tip opaque?
	 * @return True if opaque, false if the border uses transparency
	 */
	public boolean isBorderOpaque() {
		return true;
	}
	
	/**
	 * Retrieve the balloon tip's border insets
	 * @return The balloon tip's border insets
	 */
	public abstract Insets getBorderInsets(Component c);
}
