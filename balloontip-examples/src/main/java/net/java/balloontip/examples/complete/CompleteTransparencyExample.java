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

package net.java.balloontip.examples.complete;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import net.java.balloontip.examples.complete.panels.MainPanel;

/**
 * Main class for the Balloontip example application.
 * All Balloontips will be drawn on a single transparent window.
 * @author Thierry Blind
 */
public class CompleteTransparencyExample extends Complete {
	/**
	 * Main method
	 * @param args		command-line arguments (unused)
	 */
	public static void main(String[] args) {
		setDrawnOutsideParent(true);

		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				JFrame frame = new JFrame("BalloonTip example");
				frame.setIconImage(new ImageIcon(CompleteTransparencyExample.class.getResource("/net/java/balloontip/images/frameicon.png")).getImage());
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setContentPane(new MainPanel());
				frame.pack();
				frame.setSize(540, 640);
				frame.setLocationRelativeTo(null); // Centers the frame on the screen
				frame.setVisible(true);
			}
		});
	}
}
