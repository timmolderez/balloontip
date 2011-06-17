/**
 * Copyright (c) 2011 Bernhard Pauler, Tim Molderez.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the 3-Clause BSD License
 * which accompanies this distribution, and is available at
 * http://www.opensource.org/licenses/BSD-3-Clause
 */

package net.java.balloontip;

import java.awt.Point;
import java.awt.Rectangle;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;

import net.java.balloontip.positioners.BalloonTipPositioner;
import net.java.balloontip.styles.BalloonTipStyle;

/**
 * Provides the same functionality as a BalloonTip, but you can add a certain offset to its position (, which can come in handy if attached to custom components)
 * @author Tim Molderez
 */
public class CustomBalloonTip extends BalloonTip {

	// A rectangular shape within the custom component; the balloon tip will attach to this rectangle
	protected Rectangle offset = null;

	/**
	 * @see net.java.balloontip.BalloonTip#BalloonTip(JComponent, JComponent, BalloonTipStyle, Orientation, AttachLocation, int, int, boolean)
	 * @param attachedComponent	The custom component to attach the balloon tip to (may not be null)
	 * @param offset			Specifies a rectangle within the attached component; the balloon tip will attach to this rectangle.
	 * 							Do note that the coordinates should be relative to the attached component's top left corner.
	 * 							If offset is null, the balloon tip will attach to the whole component.
	 */
	public CustomBalloonTip(JComponent attachedComponent, JComponent component, Rectangle offset, BalloonTipStyle style, Orientation alignment, AttachLocation attachLocation, int horizontalOffset, int verticalOffset, boolean useCloseButton) {
		super(attachedComponent, component, style, alignment, attachLocation, horizontalOffset, verticalOffset, useCloseButton);
		setOffset(offset);
	}

	/**
	 * @see net.java.balloontip.BalloonTip#BalloonTip(JComponent, JComponent, BalloonTipStyle, BalloonTipPositioner, JButton)
	 * @param attachedComponent	The custom component to attach the balloon tip to (may not be null)
	 * @param offset			Specifies a rectangle within the attached component; the balloon tip will attach to this rectangle.
	 * 							Do note that the coordinates should be relative to the attached component's top left corner.
	 * 							If offset is null, the balloon tip will attach to the whole component.
	 */
	public CustomBalloonTip(JComponent attachedComponent, JComponent component, Rectangle offset, BalloonTipStyle style, BalloonTipPositioner positioner, JButton closeButton) {
		super(attachedComponent, component, style, positioner, closeButton);
		setOffset(offset);
	}

	/**
	 * Set the offset within the attached component
	 * @param offset (if null, the balloon tip will attach to the whole component)
	 */
	public void setOffset(Rectangle offset) {
		this.offset = offset;
		refreshLocation();
	}

	/**
	 * Get the offset within the attached component
	 * @return The offset (if null, the balloon tip is attached to the whole component)
	 */
	public Rectangle getOffset() {
		return offset;
	}

	public Rectangle getAttachedRectangle() {
		Point location = SwingUtilities.convertPoint(attachedComponent, getLocation(), this);
		return topLevelContainer!=null && offset!=null ?
				new Rectangle(location.x + offset.x, location.y + offset.y, offset.width, offset.height) :
					super.getAttachedRectangle();
	}
}
