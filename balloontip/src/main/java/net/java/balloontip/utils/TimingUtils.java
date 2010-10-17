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

import javax.swing.Timer;

import net.java.balloontip.BalloonTip;

/**
 * This class provides balloon tips that can time out
 * @author Tim
 */
public class TimingUtils {

	/*
	 * Disallow instantiating this class
	 */
	private TimingUtils() {}

	/**
	 * Displays a balloon tip for a certain time, then close it.
	 * @param balloon			the BalloonTip
	 * @param time				show the balloon for this amount of milliseconds
	 */
	public static void showTimedBalloon(final BalloonTip balloon, int time) {
		showTimedBalloon(balloon, time,new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				balloon.closeBalloon();
			}
		});
	}
	
	/**
	 * Displays a balloon tip for a certain time
	 * @param balloon			the BalloonTip
	 * @param time				show the balloon for this amount of milliseconds
	 * @param onTimeout			this action is taken when time runs out
	 */
	public static void showTimedBalloon(final BalloonTip balloon, int time, ActionListener onTimeout) {
		balloon.setVisible(true);
		Timer timer = new Timer(time, onTimeout);
		timer.setRepeats(false);
		timer.start();
	}
}
