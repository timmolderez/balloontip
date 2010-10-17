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

package net.java.balloontip.positioners;

import java.awt.Point;
import java.awt.Rectangle;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import net.java.balloontip.BalloonTip;

/**
 * A BalloonTipPositioner is used to determine the position of a BalloonTip
 * Note: If you change a positioner's settings, the changes may not be visible until the balloon tip is redrawn.
 * @author Tim Molderez
 */
public abstract class BalloonTipPositioner {
	protected BalloonTip balloonTip = null;
	private PropertyChangeListener styleListener = new PropertyChangeListener() {
		public void propertyChange(PropertyChangeEvent evt) {
			onStyleChange();
		}
	};
	
	/**
	 * Default constructor
	 */
	public BalloonTipPositioner() {}
	
	
	/**
	 * Retrieve the balloon tip that uses this positioner
	 * @return The balloon tip that uses this positioner
	 */
	public final BalloonTip getBalloonTip() {
		return balloonTip;
	}
	
	/**
	 * This method is meant only to be used by BalloonTip!
	 * A BalloonTip must call this method at the end of its construction (or when it's swapping for a new BalloonTipPositioner).
	 * @param balloonTip	the balloon tip
	 */
	public final void setBalloonTip(final BalloonTip balloonTip) {
		this.balloonTip = balloonTip;
		this.balloonTip.addPropertyChangeListener("style", styleListener);
		onStyleChange();
	}
	
	/**
	 * Find the current location of the balloon's tip, relative to the top-level container
	 * @return The location of the tip
	 */
	 public abstract Point getTipLocation();

	/**
	 * Determine and set the current location of the balloon tip
	 * @param attached		the balloon tip is attached to this rectangle
	 */
	public abstract void determineAndSetLocation(Rectangle attached);
	
	/**
	 * This method is called whenever the balloon tip's style changes.
	 * The positioner will ensure the new style is set up properly.
	 */
	protected abstract void onStyleChange();
	
	protected void finalize() throws Throwable {
		if (balloonTip!=null) {
			balloonTip.removePropertyChangeListener("style", styleListener);
		}
		super.finalize();
	}
	
}