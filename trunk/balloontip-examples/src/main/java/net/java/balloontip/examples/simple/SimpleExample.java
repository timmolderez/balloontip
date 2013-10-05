/**
 * Copyright (c) 2011-2013 Bernhard Pauler, Tim Molderez.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the 3-Clause BSD License
 * which accompanies this distribution, and is available at
 * http://www.opensource.org/licenses/BSD-3-Clause
 */

package net.java.balloontip.examples.simple;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import net.java.balloontip.BalloonTip;
import net.java.balloontip.styles.EdgedBalloonStyle;

/**
 * Simple application demonstrating a BalloonTip
 * @author Tim Molderez
 */
public class SimpleExample {
	/**
	 * Main method
	 * @param args		command-line arguments (unused)
	 */
	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				// Setup the application's window
				JFrame frame = new JFrame("Simple BalloonTip example");
				frame.setIconImage(new ImageIcon(SimpleExample.class.getResource("/net/java/balloontip/images/frameicon.png")).getImage());
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				
				// Setup the content pane
				JPanel contentPane = new JPanel();
				contentPane.setLayout(new GridBagLayout());
				frame.setContentPane(contentPane);
				
				// Add a button
				final JButton button = new JButton("Show balloon tip");
				contentPane.add(button, new GridBagConstraints(0,0,1,1,1,1, GridBagConstraints.SOUTHEAST, GridBagConstraints.NONE, new Insets(0,0,60,160), 0, 0));
				
				/*** Balloon tip creation - START ***/
				
				// Create the look for our balloon tip
				EdgedBalloonStyle style = new EdgedBalloonStyle(Color.WHITE, Color.BLUE);
				
				// Now construct the balloon tip
				final BalloonTip balloonTip = new BalloonTip(
					button,
					new JLabel("<html>The <font color=\"#0000ff\">balloon tip</font> is attached to the button.</html>"),
					style,
					BalloonTip.Orientation.LEFT_ABOVE,
					BalloonTip.AttachLocation.ALIGNED,
					30, 10,
					true
				);
				
				// Add a close button that hides the balloon tip, rather than permanently close it
				balloonTip.setCloseButton(BalloonTip.getDefaultCloseButton(), false);
				
				/*** Balloon tip creation - END ***/
				
				button.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						balloonTip.setVisible(true);
					}
				});
				
				// Dislay the window
				frame.pack();
				frame.setSize(320, 240);
				frame.setLocationRelativeTo(null);
				frame.setVisible(true);
			}
		});
	}
}
