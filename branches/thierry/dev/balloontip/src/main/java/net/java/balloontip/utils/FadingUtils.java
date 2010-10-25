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
 * A utility class for adding simple linear fade-in/out effects to balloon tips
 * @author Tim Molderez
 */
public class FadingUtils {
	
	/*
	 * Disallow instantiating this class
	 */
	private FadingUtils() {}
	
	/**
	 * Execute a fade-in effect on a balloon tip
	 * @param balloon		the balloon tip
	 * @param onStop		this action listener is triggered once the effect has stopped
	 * @param time			the duration of the fade-in effect (in ms)
	 * @param refreshRate	at how many frames-per-second should the effect run
	 */
	public static void fadeInBalloon(final BalloonTip balloon, final ActionListener onStop, final int time, final int refreshRate) {
		balloon.setOpacity(0.0f);
		balloon.setVisible(true);
		
		final int timeDelta = 1000/refreshRate;
		// Trigger this timer at the desired refresh rate and stop it once full opacity is reached.
		final Timer timer = new Timer(timeDelta, new ActionListener () {
			int curTime=0;
			public void actionPerformed(ActionEvent e) {
				curTime += timeDelta;
				float newOpacity = ((float)curTime)/time; // f(time)=curTime/time 
				if (newOpacity >= 0.9999999f || Float.isNaN(newOpacity)) {
					((Timer)e.getSource()).stop();
					/* Because of some weird bug, possibly in AlphaComposite, the balloon tip is shifted 1px when the opacity is 1.0f
					 * We'll just use something as close to 1 as a workaround, for now.. */
					balloon.setOpacity(0.9999999f);
					onStop.actionPerformed(e);
				} else {
					balloon.setOpacity(newOpacity);
				}
			}
		});
		timer.setRepeats(true);
		timer.start();
	}
	
	/**
	 * Execute a fade-in effect on a balloon tip
	 * @param balloon		the balloon tip
	 * @param onStop		this action listener is triggered once the effect has stopped
	 * @param time			the duration of the fade-out effect (in ms)
	 * @param refreshRate	at how many frames-per-second should the effect run
	 */
	public static void fadeOutBalloon(final BalloonTip balloon, final ActionListener onStop, final int time, final int refreshRate) {
		balloon.setOpacity(0.9999999f);
		balloon.setVisible(true);
		
		final int timeDelta = 1000/refreshRate;
		final Timer timer = new Timer(timeDelta, new ActionListener () {
			int curTime=0;
			public void actionPerformed(ActionEvent e) {
				curTime += timeDelta;
				float newOpacity = (-1.0f/time)*curTime+1.0f; // f(time)=(-1/time)*curTime+1
				if (newOpacity <= 0.0f || Float.isNaN(newOpacity)) {
					((Timer)e.getSource()).stop();
					balloon.setOpacity(0.0f);
					onStop.actionPerformed(e);
				} else {
					balloon.setOpacity(newOpacity);
				}
			}
		});
		timer.setRepeats(true);
		timer.start();
	}
}
