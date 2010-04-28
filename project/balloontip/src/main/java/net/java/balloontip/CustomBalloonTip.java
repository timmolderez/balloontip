/**
 * Balloontip - Balloon tips for Java Swing applications
 * Copyright 2007, 2008 Bernhard Pauler, Tim Molderez
 * 
 * This file is part of Balloontip.
 * 
 * Balloontip is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Balloontip is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Balloontip.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.java.balloontip;

import java.awt.Point;
import java.awt.Rectangle;

import javax.swing.JComponent;
import javax.swing.JViewport;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.java.balloontip.positioners.BalloonTipPositioner;
import net.java.balloontip.styles.BalloonTipStyle;


/**
 * Provides the same functionality as a BalloonTip, but you can add a certain offset, 
 * which can come in handy if attached to custom components.
 * Also, if the attached component is part of a JScrollPane, the balloon tip can be set such that it will
 * only be visible if the chosen offset point is visible. 
 * @author Tim Molderez
 */
public class CustomBalloonTip extends BalloonTip {

	// A rectangular shape within the custom component; the balloon tip will attach to this rectangle
	protected Rectangle offset = null;
	// If the custom component is located in a viewport, we'll need it to determine when the balloon tip should hide itself
	protected JViewport viewport = null;

	// If the viewport changes, so should the balloon tip
	private final ChangeListener viewportListener = new ChangeListener() {
		public void stateChanged(ChangeEvent e) {
			if (attachedComponent.isShowing()) {
				refreshLocation();
			}
		}
	};

	/**
	 * @see net.java.balloontip.BalloonTip#BalloonTip(JComponent, String, BalloonTipStyle, Orientation, AttachLocation, int, int, boolean)
	 * @param attachedComponent	The custom component to attach the balloon tip to
	 * @param offset			Specifies a rectangle within the attached component; the balloon tip will attach to this rectangle.
	 * 							Do note that the coordinates should be relative to the attached component's top left corner.
	 * 							If offset is null, the balloon tip will attach to the whole component.
	 */
	public CustomBalloonTip(JComponent attachedComponent, String text, Rectangle offset, BalloonTipStyle style, Orientation alignment, AttachLocation attachLocation, int horizontalOffset, int verticalOffset, boolean useCloseButton) {
		super(attachedComponent, text, style, alignment, attachLocation, horizontalOffset, verticalOffset, useCloseButton);
		this.offset = offset;
		refreshLocation();
	}

	/**
	 * @see net.java.balloontip.BalloonTip#BalloonTip(JComponent, String, BalloonTipStyle, BalloonTipPositioner, boolean)
	 * @param attachedComponent	The custom component to attach the balloon tip to
	 * @param offset			Specifies a rectangle within the attached component; the balloon tip will attach to this rectangle.
	 * 							Do note that the coordinates should be relative to the attached component's top left corner.
	 * 							If offset is null, the balloon tip will attach to the whole component.
	 */
	public CustomBalloonTip(JComponent attachedComponent, String text, Rectangle offset, BalloonTipStyle style, BalloonTipPositioner positioner, boolean useCloseButton) {
		super(attachedComponent, text, style, positioner, useCloseButton);
		this.offset = offset;
		refreshLocation();
	}

	/**
	 * Set the offset within the attached component
	 * @param offset
	 */
	public void setOffset(Rectangle offset) {
		this.offset = offset;
		refreshLocation();
	}

	/**
	 * Get the offset within the attached component
	 * @return The offset
	 */
	public Rectangle getOffset() {
		return offset;
	}

	public void closeBalloon() {
		if (viewport != null) {
			viewport.removeChangeListener(viewportListener);
		}
		super.closeBalloon();
	}

	/**
	 * Sets up the balloon tip such that it will only be shown if
	 * the table cell we're attached to is visible within this viewport.
	 * This is very useful if, for example, the JTable with this balloon tip is inside a JScrollpane.
	 * (You can also remove the viewport by calling setViewport(null).)
	 * @param viewport
	 */
	public void setViewport(JViewport viewport) {
		if (this.viewport != null) {
			this.viewport.removeChangeListener(viewportListener);
		}
		this.viewport = viewport;
		if (this.viewport != null) {
			this.viewport.addChangeListener(viewportListener);
		}
	}

	/**
	 * Retrieve the viewport that this balloon tip is monitoring, such that the balloon tip will hide itself 
	 * once the balloon's tip is outside of this viewport.
	 * @return The viewport this balloon tip is located in
	 */
	public JViewport getViewport() {
		return viewport;
	}

	public void refreshLocation() {
		Point location = SwingUtilities.convertPoint(attachedComponent, getLocation(), this);
		try {
			Rectangle attached = offset != null
			? new Rectangle(location.x + offset.x, location.y + offset.y, offset.width, offset.height)
			: new Rectangle(location.x, location.y, attachedComponent.getWidth(), attachedComponent.getHeight());
			// refreshLocation() should *at least* set the new location of the balloon tip.
			positioner.determineAndSetLocation(attached);

			// This method should not change the visibility of the balloon tip if it's called with
			// an "attachedComponent" that is not showing on screen, because there's no reason to call
			// setVisible(true) to make a balloon tip visible when it's "attachedComponent" is not showing on screen.
			// NB : "showing on screen" means here that the "attachedComponent" simply must be on a "showing" view port.
			if (viewport != null && viewport.isShowing()) {
				// Determine whether the point that visually connects the balloon and the table cell still is visible...
				Rectangle view = new Rectangle(SwingUtilities.convertPoint(viewport, viewport.getLocation(), getTopLevelContainer()), viewport.getSize());
				Point tipLocation = positioner.getTipLocation();
				if (tipLocation.y >= view.y-1 // -1 because we still want to allow balloons that are attached to the very top...
						&& tipLocation.y <= (view.y + view.height)
						&& (tipLocation.x) >= view.x
						&& (tipLocation.x) <= (view.x + view.width)) {
					setVisible(true);
				} else {
					setVisible(false);
				}
			}
		} catch (NullPointerException exc) {}
	}
}