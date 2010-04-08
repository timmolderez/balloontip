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
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.TexturePaint;
import java.awt.image.BufferedImage;

import net.java.balloontip.utils.FlipUtils;
import net.java.balloontip.utils.ImageUtils;


/**
 * A simple rounded rectangle balloon tip, which can have an image as background
 * @author Bernhard Pauler
 * @author Tim Molderez
 */
public class TexturedBalloonStyle extends BalloonTipStyle {

	private final int arcWidth;
	private final int arcHeight;

	private final BufferedImage bg;
	private final Rectangle bgBounds;
	private final Color borderColor;

	/**
	 * Constructor
	 * @param arcWidth		width of the rounded corner
	 * @param arcHeight		height of the rounded color
	 * @param background	the background image that's used (it's used as a pattern, so the image will repeat...)
	 * @param borderColor	line color
	 */
	public TexturedBalloonStyle(int arcWidth, int arcHeight, Image background, Color borderColor) {
		super();
		this.arcWidth = arcWidth;
		this.arcHeight = arcHeight;
		this.bg = ImageUtils.toBufferedImage(background);
		bgBounds = new Rectangle(0,0, bg.getWidth(), bg.getHeight());
		this.borderColor = borderColor;
	}

	public Insets getBorderInsets(Component c) {
		if (flipY) {
			return new Insets(verticalOffset+arcHeight, arcWidth, arcHeight, arcWidth);
		} else {
			return new Insets(arcHeight, arcWidth, arcHeight+verticalOffset, arcWidth);
		}
	}

	public boolean isBorderOpaque() {
		return true;
	}

	public void paintBorder(Component c, Graphics g, int x, int y, int bWidth, int bHeight) {
		Graphics2D g2d = (Graphics2D) g;
		
		int rectY = y;
		if (flipY) {
			rectY = y + verticalOffset;
		}
		g2d.setPaint(new TexturePaint(bg, bgBounds));
		g2d.fillRoundRect(x, rectY, bWidth, bHeight-verticalOffset, arcWidth*2, arcHeight*2);
		g2d.setColor(borderColor);
		g2d.drawRoundRect(x, rectY, bWidth-1, bHeight-verticalOffset-1, arcWidth*2, arcHeight*2);

		int[] triangleX = {x+horizontalOffset, x+horizontalOffset+verticalOffset, x+horizontalOffset};
		int[] triangleY = {y+bHeight-verticalOffset-1, y+bHeight-verticalOffset-1, y+bHeight-1};

		if (flipY) {
			int flipAxis = bHeight-1;
			for (int i = 0; i < triangleX.length; i++) {
				Point flippedPoint = FlipUtils.flipHorizontally(triangleX[i], triangleY[i], flipAxis);
				triangleX[i] = flippedPoint.x;
				triangleY[i] = flippedPoint.y;
			}
		}

		if (flipX) {
			int flipAxis = bWidth-1;
			for (int i = 0; i < triangleX.length; i++) {
				Point flippedPoint = FlipUtils.flipVertically(triangleX[i], triangleY[i], flipAxis);
				triangleX[i] = flippedPoint.x;
				triangleY[i] = flippedPoint.y;
			}
		}

		g2d.setPaint(new TexturePaint(bg, bgBounds));
		g2d.fillPolygon(triangleX, triangleY, 3);
		g2d.setColor(borderColor);
		g2d.drawLine(triangleX[0], triangleY[0], triangleX[2], triangleY[2]);
		g2d.drawLine(triangleX[1], triangleY[1], triangleX[2], triangleY[2]);

		// Bug workaround, Java Bug Database ID 6644471
		g2d.setPaint(new TexturePaint(bg, bgBounds));
		g2d.fillPolygon(triangleX, triangleY, 3);
		g2d.setColor(borderColor);
		g2d.drawLine(triangleX[0], triangleY[0], triangleX[2], triangleY[2]);
		g2d.drawLine(triangleX[1], triangleY[1], triangleX[2], triangleY[2]);
	}
	
	public int getMinimalHorizontalOffset() {
		return arcWidth + verticalOffset;
	}
}
