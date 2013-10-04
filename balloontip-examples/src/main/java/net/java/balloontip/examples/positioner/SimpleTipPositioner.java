/**
 * Copyright (c) 2011-2013 Bernhard Pauler, Tim Molderez.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the 3-Clause BSD License
 * which accompanies this distribution, and is available at
 * http://www.opensource.org/licenses/BSD-3-Clause
 */

package net.java.balloontip.examples.positioner;

import java.awt.Point;
import java.awt.Rectangle;

import net.java.balloontip.positioners.BalloonTipPositioner;

/**
 * A sample implementation of a BalloonTipPositioner
 * @author Tim Molderez
 */
public class SimpleTipPositioner extends BalloonTipPositioner {
	private int x = 0;	// Current position of the balloon tip
	private int y = 0;

	public void determineAndSetLocation(Rectangle attached) {
		x = attached.x;
		y = attached.y - balloonTip.getPreferredSize().height;
		
		balloonTip.setBounds(x, y, balloonTip.getPreferredSize().width, balloonTip.getPreferredSize().height);
		balloonTip.revalidate();
	}

	public Point getTipLocation() {
		return new Point(x + 20, y + balloonTip.getPreferredSize().height);
	}

	protected void onStyleChange() {
		balloonTip.getStyle().setHorizontalOffset(20);
		balloonTip.getStyle().setVerticalOffset(20);
	}
}
