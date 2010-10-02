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

package net.java.balloontip.examples.positioner;

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
 * Simple application demonstrating a BalloonTip with a custom BalloonTipPositioner
 * @author Tim Molderez
 */
public class SimplePositionerExample {
	/**
	 * Main method
	 * @param args		command-line arguments (unused)
	 */
	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				// Setup the application's window
				JFrame frame = new JFrame("Simple BalloonTipPositioner example");
				frame.setIconImage(new ImageIcon(SimplePositionerExample.class.getResource("/net/java/balloontip/images/frameicon.png")).getImage());
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.pack();
				frame.setSize(320, 240);
				frame.setLocationRelativeTo(null);
				
				// Setup the content pane
				JPanel contentPane = new JPanel();
				contentPane.setLayout(new GridBagLayout());
				frame.setContentPane(contentPane);
				
				// Add a button
				final JButton button = new JButton("A button");
				button.setSelected(true);
				contentPane.add(button, new GridBagConstraints(0,0,1,1,1,1, GridBagConstraints.SOUTHEAST, GridBagConstraints.NONE, new Insets(0,0,60,200), 0, 0));
				
				// Now construct the balloon tip, with our own positioner
				final BalloonTip balloonTip = new BalloonTip(
					button, 
					new JLabel("A balloon tip with a custom positioner"),
					new EdgedBalloonStyle(Color.WHITE, Color.BLUE),
					new SimpleTipPositioner(),
					null
				);
				balloonTip.setCloseButton(BalloonTip.getDefaultCloseButton(),false, false);
				
				button.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						balloonTip.setVisible(true);
					}
				});
				
				frame.setVisible(true);
			}
		});
	}
}
