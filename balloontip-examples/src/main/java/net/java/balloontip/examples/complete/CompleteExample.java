/**
 * Copyright (c) 2011-2013 Bernhard Pauler, Tim Molderez.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the 3-Clause BSD License
 * which accompanies this distribution, and is available at
 * http://www.opensource.org/licenses/BSD-3-Clause
 */

package net.java.balloontip.examples.complete;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.UIManager;

import net.java.balloontip.examples.complete.panels.MainPanel;

/**
 * Main class for the Balloontip example application
 * @author Tim Molderez
 */
public class CompleteExample {
	/**
	 * Main method
	 * @param args		command-line arguments (unused)
	 */
	public static void main(String[] args) {
		// Switch to the OS default look & feel
		try {
	        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	    } catch (Exception e) {}
		
	    // Now create the GUI
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				JFrame frame = new JFrame("BalloonTip example");
				frame.setIconImage(new ImageIcon(CompleteExample.class.getResource("/net/java/balloontip/images/frameIcon.png")).getImage());
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setContentPane(new MainPanel());
				frame.pack();
				frame.setSize(480, 640);
				frame.setLocationRelativeTo(null); // Centers the frame on the screen
				frame.setVisible(true);
			}
		});
	}
}
