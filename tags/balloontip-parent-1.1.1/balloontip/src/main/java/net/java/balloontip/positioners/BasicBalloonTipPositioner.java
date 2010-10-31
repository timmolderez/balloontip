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

/**
 * Provides the interface for a basic positioner
 */
public abstract class BasicBalloonTipPositioner extends BalloonTipPositioner {

	protected int x = 0;							// Current position
	protected int y = 0;
	protected int hOffset = 0;						// Current horizontal offset
	protected boolean flipX = false;				// Current orientation
	protected boolean flipY = false;

	protected int preferredHorizontalOffset;		// The preferred value of the horizontal offset
	protected int preferredVerticalOffset;			// The preferred value of the vertical offset
	protected int minimumHorizontalOffset;			// The horizontal offset may not become smaller than this value		

	protected boolean offsetCorrection = true;		// If true, a balloon tip should adjust its horizontal offset when the left/right side collides with the window's border
	protected boolean orientationCorrection = true;	// If true, a balloon tip should flip/mirror itself if otherwise it would become invisible 

	protected boolean fixedAttachLocation = false;	// If true, attachLocationX and attachLocationY should be used to determine the position of the tip

	protected float attachLocationX = 0.0f;			// A percentage that determines the X-location of the tip on the attached object
	protected float attachLocationY = 0.0f;			// A percentage that determines the Y-location of the tip on the attached object
													// For example, if attachLocationX and Y are both 0.5, then the tip is centered on the attached object
	
	/**
	 * Constructor
	 * @param hO	Preferred horizontal offset
	 * @param vO	Preferred vertical offset
	 */
	public BasicBalloonTipPositioner(int hO, int vO) {
		super();
		preferredHorizontalOffset = hO;
		preferredVerticalOffset = vO;
	}
	
	public void onStyleChange() {
		balloonTip.getStyle().setHorizontalOffset(preferredHorizontalOffset);
		balloonTip.getStyle().setVerticalOffset(preferredVerticalOffset);
		minimumHorizontalOffset = balloonTip.getStyle().getMinimalHorizontalOffset();
	}

	/**
	 * Retrieve the preferred horizontal offset
	 * @return Preferred horizontal offset
	 */
	public int getPreferredHorizontalOffset() {
		return preferredHorizontalOffset;
	}

	/**
	 * Set the preferred horizontal offset
	 * @param preferredHorizontalOffset
	 */
	public void setPreferredHorizontalOffset(int preferredHorizontalOffset) {
		this.preferredHorizontalOffset = preferredHorizontalOffset;
		balloonTip.getStyle().setHorizontalOffset(preferredHorizontalOffset);
		balloonTip.repaint();
	}

	/**
	 * Retrieve the preferred vertical offset
	 * @return Preferred vertical offset
	 */
	public int getPreferredVerticalOffset() {
		return preferredVerticalOffset;
	}

	/**
	 * Set the preferred horizontal offset
	 * @param preferredVerticalOffset
	 */
	public void setPreferredVerticalOffset(int preferredVerticalOffset) {
		this.preferredVerticalOffset = preferredVerticalOffset;
		this.minimumHorizontalOffset = 2 * preferredVerticalOffset;
		balloonTip.getStyle().setVerticalOffset(preferredVerticalOffset);
	}

	/**
	 * Is offset correction enabled?
	 * @return True if offset correction is enabled
	 */
	public boolean isOffsetCorrected() {
		return offsetCorrection;
	}

	/**
	 * Set offset correction
	 * @param offsetCorrection	enabled if true
	 */
	public void enableOffsetCorrection(boolean offsetCorrection) {
		this.offsetCorrection = offsetCorrection;
	}

	/**
	 * Is orientation correction enabled?
	 * @return True if orientation correction is enabled
	 */
	public boolean isOrientationCorrected() {
		return orientationCorrection;
	}

	/**
	 * Set orientation correction
	 * @param orientationCorrection		enabled if true
	 */
	public void enableOrientationCorrection(boolean orientationCorrection) {
		this.orientationCorrection = orientationCorrection;
	}

	/**
	 * Does the tip have a fixed location?
	 * @return True if the balloon has a fixed attaching location
	 */
	public boolean isFixedAttachLocation() {
		return fixedAttachLocation;
	}

	/**
	 * Set whether the tip should have a fixed location
	 * @param fixedAttachLocation	the tip has a fixed location if true
	 */
	public void enableFixedAttachLocation(boolean fixedAttachLocation) {
		this.fixedAttachLocation = fixedAttachLocation;
	}

	/**
	 * Returns the percentage that determines the X-coordinate of the tip within the attached component
	 * (whereas 0.0 is the left side and 1.0 is the right side)
	 * @return The percentage that determines the X-coordinate of the attaching location
	 */
	public float getAttachLocationX() {
		return attachLocationX;
	}

	/**
	 * Returns the percentage that determines the Y-coordinate of the tip within the attached component
	 * (whereas 0.0 is the top and 1.0 is the bottom)
	 * @return The percentage that determines the Y-coordinate of the attaching location
	 */
	public float getAttachLocationY() {
		return attachLocationY;
	}

	/**
	 * Set where the tip should be located, relative to the component the balloon is attached to.
	 * @param attachLocationX	a number from 0.0 to 1.0 (whereas 0 is the left side; 1 is the right side)
	 * @param attachLocationY	a number from 0.0 to 1.0 (whereas 0 is the top; 1 is the bottom)
	 */
	public void setAttachLocation(float attachLocationX, float attachLocationY) {
		this.attachLocationX = attachLocationX;
		this.attachLocationY = attachLocationY;
	}

	public Point getTipLocation() {
		int tipX = x + hOffset;
		int tipY = y + balloonTip.getHeight();

		if (flipX) {
			tipX = x + hOffset;
		}
		if (flipY) {
			tipY = y;
		}

		return new Point(tipX, tipY);
	}

	/*
	 * Applies offset correction to the current position of the balloon tip
	 */
	protected void applyOffsetCorrection() {
		// Check collision with the left side of the window
		int overflow = -x;
		int balloonWidth = balloonTip.getWidth();

		if (overflow > 0) {
			x += overflow;
			hOffset -= overflow;
			// Take into account the minimum horizontal offset
			if (hOffset < minimumHorizontalOffset) {
				hOffset = minimumHorizontalOffset;
				if (flipX) {
					x += -overflow +  (balloonWidth - preferredHorizontalOffset) - minimumHorizontalOffset;
				}else {
					x += -overflow +  preferredHorizontalOffset - minimumHorizontalOffset;
				}
			}
		}

		// Check collision with the right side of the window
		overflow = (x+balloonWidth) - balloonTip.getTopLevelContainer().getWidth();
		if (overflow > 0) {
			x -= overflow;
			hOffset += overflow;

			// Take into account the minimum horizontal offset
			if (hOffset > balloonWidth - minimumHorizontalOffset) {
				hOffset = balloonWidth - minimumHorizontalOffset;
				if (flipX) {
					x += overflow + preferredHorizontalOffset + minimumHorizontalOffset;
				}else {
					x += overflow -  (balloonWidth - preferredHorizontalOffset) + minimumHorizontalOffset;
				}
			}
		}		
	}
}