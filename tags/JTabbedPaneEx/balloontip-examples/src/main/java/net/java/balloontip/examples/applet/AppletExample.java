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

package net.java.balloontip.examples.applet;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JPanel;

import net.java.balloontip.BalloonTip;
import net.java.balloontip.styles.EdgedBalloonStyle;

/**
 * Simple application demonstrating a BalloonTip in a JApplet
 * @author Tim Molderez
 */
public class AppletExample extends JApplet {
	public void init() {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {	
				// Setup the content pane
				JPanel contentPane = new JPanel();
				contentPane.setLayout(new GridBagLayout());
				setContentPane(contentPane);
				
				// Add a button
				final JButton button = new JButton("Show balloon tip");
				button.setSelected(true);
				contentPane.add(button, new GridBagConstraints(0,0,1,1,1,1, GridBagConstraints.SOUTHEAST, GridBagConstraints.NONE, new Insets(0,0,60,120), 0, 0));
				
				/*** Balloon tip creation - START ***/
				
				// Create the look for our balloon tip
				EdgedBalloonStyle style = new EdgedBalloonStyle(Color.WHITE, Color.BLUE);
				
				// Now construct the balloon tip
				final BalloonTip balloonTip = new BalloonTip(
					button,
					"<html>This <font color=\"#0000ff\">balloon tip</font> is attached to the button.</html>",
					style,
					BalloonTip.Orientation.LEFT_ABOVE,
					BalloonTip.AttachLocation.ALIGNED,
					30, 10,
					true
				);
				
				// Don't close the balloon when clicking the close-button, you just need to hide it
				balloonTip.setCloseButtonActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						balloonTip.setVisible(false);
					}
				});
				
				/*** Balloon tip creation - END ***/
				
				button.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						balloonTip.setVisible(true);
					}
				});
			}
		});
	}
}
