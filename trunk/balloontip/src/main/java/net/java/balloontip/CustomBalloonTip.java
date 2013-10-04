/**
 * Copyright (c) 2011-2013 Bernhard Pauler, Tim Molderez.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the 3-Clause BSD License
 * which accompanies this distribution, and is available at
 * http://www.opensource.org/licenses/BSD-3-Clause
 */

package net.java.balloontip;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

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
	private Rectangle offset = null;
	
	// Repaint the balloon tip when clicking the attached component
	private MouseAdapter mouseListener = new MouseAdapter() {
		public void mouseReleased(MouseEvent e) {
			setOffset(offset);
		}
	};
	
	// Repaint the balloon tip when typing a key in the attached component
	private KeyAdapter keyListener = new KeyAdapter() {
		public void keyPressed(KeyEvent e) {
			setOffset(offset);
		}
	};

	/**
	 * @see net.java.balloontip.BalloonTip#BalloonTip(JComponent, JComponent, BalloonTipStyle, Orientation, AttachLocation, int, int, boolean)
	 * @param attachedComponent	the custom component to attach the balloon tip to (may not be null)
	 * @param offset			specifies a rectangle within the attached component; the balloon tip will attach to this rectangle. (may not be null)
	 * 							Do note that the coordinates should be relative to the attached component's top left corner.
	 */
	public CustomBalloonTip(JComponent attachedComponent, JComponent component, Rectangle offset, BalloonTipStyle style, Orientation orientation, AttachLocation attachLocation, int horizontalOffset, int verticalOffset, boolean useCloseButton) {
		super();
		this.offset=offset;
		setup(attachedComponent, component, style, setupPositioner(orientation, attachLocation, horizontalOffset, verticalOffset), 
				useCloseButton?getDefaultCloseButton():null);
		setup();
	}

	/**
	 * @see net.java.balloontip.BalloonTip#BalloonTip(JComponent, JComponent, BalloonTipStyle, BalloonTipPositioner, JButton)
	 * @param attachedComponent	the custom component to attach the balloon tip to (may not be null)
	 * @param offset			specifies a rectangle within the attached component; the balloon tip will attach to this rectangle. (may not be null)
	 * 							Do note that the coordinates should be relative to the attached component's top left corner.
	 */
	public CustomBalloonTip(JComponent attachedComponent, JComponent component, Rectangle offset, BalloonTipStyle style, BalloonTipPositioner positioner, JButton closeButton) {
		super();
		this.offset=offset;
		setup(attachedComponent, component, style, positioner, closeButton);
		setup();
	}

	/**
	 * Set the offset within the attached component
	 * @param offset 	specifies a rectangle within the attached component; the balloon tip will attach to this rectangle. (may not be null)
	 * 					Do note that the coordinates should be relative to the attached component's top left corner.
	 */
	public void setOffset(Rectangle offset) {
		this.offset = offset;
		notifyViewportListener();
		refreshLocation();
		
		/* Bug workaround: For some reason, parts of the attached component can be drawn on top of the balloon tip when
		 * a JTable, JTree or JList is modified.. (What's also strange is that this problem doesn't occur once there 
		 * are multiple balloon tips attached to the same top-level container..)
		 * The current workaround to this problem is to hide and reshow the balloon tip... */	
		visibilityControl.setCriterionAndUpdate("refresh", false);
		visibilityControl.setCriterionAndUpdate("refresh", true);
	}

	/**
	 * Get the offset within the attached component
	 * @return		the offset
	 */
	public Rectangle getOffset() {
		return offset;
	}

	public Rectangle getAttachedRectangle() {
		Point location = SwingUtilities.convertPoint(attachedComponent, getLocation(), this);
		return new Rectangle(location.x + offset.x, location.y + offset.y, offset.width, offset.height);
	}
	
	public void closeBalloon() {
		attachedComponent.removeMouseListener(mouseListener);
		attachedComponent.removeKeyListener(keyListener);
		super.closeBalloon();
	}
	
	/*
	 * A helper method needed when constructing a CustomBalloonTip instance
	 */
	private void setup() {
		attachedComponent.addMouseListener(mouseListener);
		attachedComponent.addKeyListener(keyListener);
	}
	
	private static final long serialVersionUID = 2956673369456562797L;
}
