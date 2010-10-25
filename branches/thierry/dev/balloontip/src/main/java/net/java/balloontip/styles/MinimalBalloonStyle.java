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

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.geom.GeneralPath;


/**
 * A minimal anti-aliased balloon tip style with no border, also supports a transparent fill color
 * @author Tim Molderez
 */
public class MinimalBalloonStyle extends BalloonTipStyle {
	private final int arcWidth;
	private final Color fillColor;

	/**
	 * Constructor
	 * @param fillColor		fill color (transparent colors are allowed)
	 * @param arcWidth		the radius of a rounded corner
	 */
	public MinimalBalloonStyle(Color fillColor, int arcWidth) {
		super();
		this.fillColor = fillColor;
		this.arcWidth = arcWidth;
	}

	public Insets getBorderInsets(Component c) {
		if (flipY) {
			return new Insets(verticalOffset+arcWidth, arcWidth, arcWidth, arcWidth);
		}
		return new Insets(arcWidth, arcWidth, arcWidth+verticalOffset, arcWidth);
	}

	public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		int yTop;		// Y-coordinate of the top side of the balloon
		int yBottom;	// Y-coordinate of the bottom side of the balloon
		if (flipY) {
			yTop = y + verticalOffset;
			yBottom = y + height;
		} else {
			yTop = y;
			yBottom = y + height - verticalOffset;
		}

		// Draw the outline of the balloon
		GeneralPath outline = new GeneralPath();
		outline.moveTo(x + arcWidth, yTop);

		// Top left corner
		outline.quadTo(x, yTop, x, yTop + arcWidth);

		// Left side
		outline.lineTo(x, yBottom - arcWidth);

		// Bottom left corner
		outline.quadTo(x, yBottom, x + arcWidth, yBottom);

		// Bottom side
		if (!flipX && !flipY) {
			outline.lineTo(x + horizontalOffset - verticalOffset, yBottom);
			outline.lineTo(x + horizontalOffset, yBottom + verticalOffset);
			outline.lineTo(x + horizontalOffset + verticalOffset, yBottom);
		} else if (flipX && !flipY) {
			outline.lineTo(x + width - horizontalOffset - verticalOffset, yBottom);
			outline.lineTo(x + width - horizontalOffset, yBottom + verticalOffset);
			outline.lineTo(x + width - horizontalOffset + verticalOffset, yBottom);
		}
		outline.lineTo(x + width - arcWidth, yBottom);

		// Bottom right corner
		outline.quadTo(x + width, yBottom, x + width, yBottom - arcWidth);

		// Right side
		outline.lineTo(x + width, yTop + arcWidth);

		// Top right corner
		outline.quadTo(x + width, yTop, x + width - arcWidth, yTop);

		// Top side
		if (!flipX && flipY) {
			outline.lineTo(x + horizontalOffset + verticalOffset, yTop);
			outline.lineTo(x + horizontalOffset, yTop - verticalOffset);
			outline.lineTo(x + horizontalOffset - verticalOffset, yTop);	
		} else if (flipX && flipY) {
			outline.lineTo(x + width - horizontalOffset + verticalOffset, yTop);
			outline.lineTo(x + width - horizontalOffset, yTop - verticalOffset);
			outline.lineTo(x + width - horizontalOffset - verticalOffset, yTop);
		}

		outline.closePath();

		g2d.setPaint(fillColor);
		g2d.fill(outline);
	}

	public int getMinimalHorizontalOffset() {
		return arcWidth + verticalOffset;
	}

	public boolean isBorderOpaque() {
		return fillColor.getAlpha()==255;
	}
}
