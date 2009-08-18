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

package net.java.balloontip.styles;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Point;

import net.java.balloontip.utils.FlipUtils;


/**
 * A simple rounded rectangle balloon tip
 * @author Bernhard Pauler
 */
public class RoundedBalloonStyle extends BalloonTipStyle {

	private int arcWidth;
	private int arcHeight;

	private Color fillColor;
	private Color borderColor;

	/**
	 * Constructor
	 * @param arcWidth		width of the rounded corner
	 * @param arcHeight		height of the rounded color
	 * @param fillColor		fill color
	 * @param borderColor	border line color
	 */
	public RoundedBalloonStyle(int arcWidth, int arcHeight, Color fillColor, Color borderColor) {
		this.arcWidth = arcWidth;
		this.arcHeight = arcHeight;
		this.fillColor = fillColor;
		this.borderColor = borderColor;
	}

	public Insets getBorderInsets(Component c) {
		if (flipY) {
			return new Insets(verticalOffset+arcHeight, arcWidth, arcHeight, arcWidth);
		} else {
			return new Insets(arcHeight, arcWidth, arcHeight+verticalOffset, arcWidth);
		}
	}

	public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
		int rectY = y;
		if (flipY) {
			rectY = y + verticalOffset;
		}
		g.setColor(fillColor);
		g.fillRoundRect(x, rectY, width, height-verticalOffset, arcWidth*2, arcHeight*2);
		g.setColor(borderColor);
		g.drawRoundRect(x, rectY, width-1, height-verticalOffset-1, arcWidth*2, arcHeight*2);

		int[] triangleX = {x+horizontalOffset, x+horizontalOffset+verticalOffset, x+horizontalOffset};
		int[] triangleY = {y+height-verticalOffset-1, y+height-verticalOffset-1, y+height-1};

		if (flipY) {
			int flipAxis = height-1;
			for (int i = 0; i < triangleX.length; i++) {
				Point flippedPoint = FlipUtils.flipHorizontally(triangleX[i], triangleY[i], flipAxis);
				triangleX[i] = flippedPoint.x;
				triangleY[i] = flippedPoint.y;
			}
		}

		if (flipX) {
			int flipAxis = width-1;
			for (int i = 0; i < triangleX.length; i++) {
				Point flippedPoint = FlipUtils.flipVertically(triangleX[i], triangleY[i], flipAxis);
				triangleX[i] = flippedPoint.x;
				triangleY[i] = flippedPoint.y;
			}
		}

		g.setColor(fillColor);
		g.fillPolygon(triangleX, triangleY, 3);
		g.setColor(borderColor);
		g.drawLine(triangleX[0], triangleY[0], triangleX[2], triangleY[2]);
		g.drawLine(triangleX[1], triangleY[1], triangleX[2], triangleY[2]);

		// Bug workaround, Java Bug Database ID 6644471
		g.setColor(fillColor);
		g.drawLine(triangleX[0], triangleY[0], triangleX[1], triangleY[1]);
		g.setColor(borderColor);
		g.drawLine(triangleX[0], triangleY[0], triangleX[0], triangleY[0]);
		g.drawLine(triangleX[1], triangleY[1], triangleX[1], triangleY[1]);
	}
	
	public int getMinimalHorizontalOffset() {
		return arcWidth + verticalOffset;
	}
}
