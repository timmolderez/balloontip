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