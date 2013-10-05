/**
 * Copyright (c) 2011-2013 Bernhard Pauler, Tim Molderez.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the 3-Clause BSD License
 * which accompanies this distribution, and is available at
 * http://www.opensource.org/licenses/BSD-3-Clause
 */

package net.java.balloontip.examples.complete;

import java.awt.Color;

import javax.swing.JComponent;
import javax.swing.JLabel;

import net.java.balloontip.BalloonTip;
import net.java.balloontip.positioners.BalloonTipPositioner;
import net.java.balloontip.positioners.LeftAbovePositioner;
import net.java.balloontip.styles.BalloonTipStyle;
import net.java.balloontip.styles.EdgedBalloonStyle;
import net.java.balloontip.styles.MinimalBalloonStyle;
import net.java.balloontip.utils.TimingUtils;
import net.java.balloontip.utils.ToolTipUtils;

/**
 * A set of utility functions for the CompleteExample application
 * @author Tim Molderez
 */
public class Utils {
	private static BalloonTip errBalloon = null;

	/**
	 * Set a tooltip
	 * @param comp		sets a tooltip for this component
	 * @param text		the contents of the tooltip (you may use html)
	 */
	public static void setToolTip(final JComponent comp, final String text) {
		BalloonTipStyle style = new MinimalBalloonStyle(new Color(169, 205, 221, 220), 5);
		final BalloonTip balloon = new BalloonTip(comp, new JLabel(text), style, BalloonTip.Orientation.LEFT_ABOVE, BalloonTip.AttachLocation.ALIGNED, 15, 10, false);
		balloon.addDefaultMouseListener(false);
		ToolTipUtils.balloonToToolTip(balloon, 500, 3000);
	}

	/**
	 * Display an error balloon tip
	 * @param comp	attach the balloon tip to this component
	 * @param text	error message
	 */
	public static void showErrorMessage(JComponent comp, String text) {
		if (Utils.errBalloon!=null) {
			Utils.errBalloon.closeBalloon();
		}
		BalloonTipStyle style = new MinimalBalloonStyle(new Color(246, 197, 192, 220), 5);
		Utils.errBalloon = new BalloonTip(comp, new JLabel("<html><font color=\"#6f150d\">" + text + "</font></html>"),
				style, BalloonTip.Orientation.LEFT_ABOVE, BalloonTip.AttachLocation.ALIGNED, 15, 10, false);
		Utils.errBalloon.addDefaultMouseListener(false);
		TimingUtils.showTimedBalloon(Utils.errBalloon, 3000);
	}

	/**
	 * Retrieve an instance of the balloon tip style to be used throughout the application
	 * @return	the balloon tip style
	 */
	public static BalloonTipStyle createBalloonTipStyle() {
		return new EdgedBalloonStyle(new Color(255,253,245), new Color(64,64,64));
	}

	/**
	 * Retrieve an instance of the balloon tip positioner to be used throughout the application
	 * @return	the balloon tip positioner
	 */
	public static BalloonTipPositioner createBalloonTipPositioner() {
		return new LeftAbovePositioner(15, 10);
	}

	/**
	 * Wraps a string in a monospace font tag (for use in a HTML-formatted label) 
	 * @param str		the string
	 * @return			the formatted string
	 */
	public static String monospace(String str) {
		return "<font face=\"monospace\">" + str + "</font>"; 
	}

	

}
