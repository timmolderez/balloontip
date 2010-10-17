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

package net.java.balloontip.utils;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.Timer;

import net.java.balloontip.BalloonTip;

/**
 * This class allows you to use a balloon tip as a tooltip
 * @author Tim Molderez
 */
public class ToolTipUtils {
	
	/*
	 * Disallow instantiating this class
	 */
	private ToolTipUtils() {}

	/*
	 * This class monitors when the balloon tooltip should be shown 
	 */
	private static class ToolTipController extends MouseAdapter {
		private final BalloonTip balloonTip; 
		private final Timer initialTimer;
		private final Timer showTimer;

		/**
		 * Constructor
		 * @param balloonTip	the balloon tip to turn into a tooltip
		 * @param initialDelay	in milliseconds, how long should you hover over the attached component before showing the tooltip
		 * @param showDelay		in milliseconds, how long should the tooltip stay visible
		 */
		public ToolTipController(final BalloonTip balloonTip, int initialDelay, int showDelay) {
			super();
			this.balloonTip = balloonTip;
			initialTimer = new Timer(initialDelay, new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					balloonTip.setVisible(true);
					showTimer.start();
				}
			});
			initialTimer.setRepeats(false);

			showTimer = new Timer(showDelay, new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					balloonTip.setVisible(false);
				}
			});
			showTimer.setRepeats(false);
		}

		public void mouseEntered(MouseEvent e) {
			if (!balloonTip.isVisible()) {
				initialTimer.start();
			}
		}

		public void mouseExited(MouseEvent e) {
			initialTimer.stop();
			showTimer.stop();
			balloonTip.setVisible(false);
		}
	}

	/**
	 * Turns a balloon tip into a tooltip
	 * @param bT			the balloon tip
	 * @param initialDelay	in milliseconds, how long should you hover over the attached component before showing the tooltip
	 * @param showDelay		in milliseconds, how long should the tooltip stay visible
	 */
	public static void balloonToToolTip(final BalloonTip bT, int initialDelay, int showDelay) {
		bT.setVisible(false);
		// Add tooltip behaviour
		bT.getAttachedComponent().addMouseListener(new ToolTipController(bT, initialDelay, showDelay));
	}
}
