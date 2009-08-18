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

package net.java.balloontip.utils;

import java.awt.Point;

/**
 * A helper class for flipping/mirroring points
 * @author Bernhard Pauler
 */
public class FlipUtils {

	/*
	 * Disallow instantiating this class
	 */
	private FlipUtils() {};

	/**
	 * Flip a point around a horizontal axis
	 * @param x		X-coordinate of the point
	 * @param y		Y-coordinate of the point
	 * @param flipAxis	Y-coordinate that defines the horizontal axis
	 * @return	the flipped point
	 */
	public static Point flipHorizontally(int x, int y, int flipAxis) {
	    Point p = new Point(x, y);
	    p.move(p.x, flipAxis-p.y);
	    return p;
	}

	/**
	 * Flip a point around a vertical axis
	 * @param x		X-coordinate of the point
	 * @param y		Y-coordinate of the point
	 * @param flipAxis	X-coordinate that defines the vertical axis
	 * @return	the flipped point
	 */
    public static Point flipVertically(int x, int y, int flipAxis) {
        Point p = new Point(x, y);
        p.move(flipAxis-p.x, p.y);
        return p;
    }
}
