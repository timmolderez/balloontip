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

package net.java.balloontip.examples.complete;

import java.awt.Color;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;

import net.java.balloontip.BalloonTip;
import net.java.balloontip.examples.complete.panels.MainPanel;
import net.java.balloontip.styles.BalloonTipStyle;
import net.java.balloontip.styles.MinimalBalloonStyle;
import net.java.balloontip.utils.TimingUtils;
import net.java.balloontip.utils.ToolTipUtils;

/**
 * Main class for the Balloontip example application
 * @author Tim Molderez
 */
public class CompleteExample {
	
	private static BalloonTip errBalloon = null;
	
	/**
	 * Main method
	 * @param args		command-line arguments (unused)
	 */
	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				JFrame frame = new JFrame("BalloonTip example");
				frame.setIconImage(new ImageIcon(CompleteExample.class.getResource("/net/java/balloontip/images/frameicon.png")).getImage());
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setContentPane(new MainPanel());
				frame.pack();
				frame.setSize(540, 640);
				frame.setLocationRelativeTo(null); // Centers the frame on the screen
				frame.setVisible(true);
			}
		});
	}
	
	/**
	 * Set a tooltip
	 * @param comp		sets a tooltip for this component
	 * @param text		the contents of the tooltip (you may use html)
	 */
	public static void setToolTip(final JComponent comp, final String text) {
		BalloonTipStyle style = new MinimalBalloonStyle(new Color(169, 205, 221, 220), 5);
		final BalloonTip balloon = new BalloonTip(comp, text, style, BalloonTip.Orientation.LEFT_ABOVE, BalloonTip.AttachLocation.ALIGNED, 15, 10, false);
		balloon.enableClickToHide(true);
		ToolTipUtils.balloonToToolTip(balloon, 500, 3000);
	}
	
	/**
	 * Display an error balloon tip
	 * @param comp	attach the balloon tip to this component
	 * @param text	error message
	 */
	public static void showErrorMessage(JComponent comp, String text) {
		if (errBalloon!=null) {
			errBalloon.closeBalloon();
		}
		BalloonTipStyle style = new MinimalBalloonStyle(new Color(246, 197, 192, 220), 5);
		errBalloon = new BalloonTip(comp, "<html><font color=\"#6f150d\">" + text + "</font></html>",
				style, BalloonTip.Orientation.LEFT_ABOVE, BalloonTip.AttachLocation.ALIGNED, 15, 10, false);
		errBalloon.enableClickToHide(true);
		TimingUtils.showTimedBalloon(errBalloon, 3000);
	}
}
