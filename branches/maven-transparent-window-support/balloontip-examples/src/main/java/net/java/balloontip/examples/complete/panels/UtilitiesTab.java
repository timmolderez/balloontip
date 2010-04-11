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

package net.java.balloontip.examples.complete.panels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.java.balloontip.BalloonTip;
import net.java.balloontip.examples.complete.Complete;
import net.java.balloontip.examples.complete.CompleteExample;
import net.java.balloontip.styles.EdgedBalloonStyle;
import net.java.balloontip.utils.TimingUtils;
import net.java.balloontip.utils.ToolTipUtils;

public class UtilitiesTab extends JPanel {
	private final BalloonTip tooltipBalloon;

	/**
	 * Default constructor
	 */
	public UtilitiesTab() {
		super();
		setLayout(new GridBagLayout());
		int gridY = 0;

		/*
		 * Draw the GUI
		 */

		// Timed balloon tip
		JPanel timePanel = new JPanel();
		timePanel.setLayout(new GridBagLayout());
		timePanel.add(new JLabel("Any BalloonTip can be given a time-out value."), new GridBagConstraints(0,0,3,1,1.0,0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10,10,10,0), 0, 0));
		timePanel.add(new JLabel("Time-out (ms):"), new GridBagConstraints(0,1,1,1,0.0,0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10,10,10,0), 0, 0));
		final JTextField timeout = new JTextField("3000");
		timeout.setPreferredSize(new Dimension(50,25));
		timePanel.add(timeout, new GridBagConstraints(1,1,1,1,0.0,0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10,10,10,0), 0, 0));
		final JButton showBalloon = new JButton("Start timer");
		timePanel.add(showBalloon, new GridBagConstraints(2,1,1,1,1.0,1.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(10,10,10,0), 0, 0));

		timePanel.setBorder(BorderFactory.createTitledBorder("Timed balloon tip:"));

		add(timePanel, new GridBagConstraints(0,gridY,1,1,1.0,1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(10,10,10,10), 0, 0));
		++gridY;

		// Balloon tooltip
		JPanel tooltipPanel = new JPanel();
		tooltipPanel.setLayout(new GridBagLayout());
		tooltipPanel.add(new JLabel("Any BalloonTip can be turned into a tooltip"), new GridBagConstraints(0,0,3,1,1.0,0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10,10,10,0), 0, 0));
		final JLabel tooltipLabel = new JLabel("Hover me to show the tooltip!");
		tooltipLabel.setBorder(BorderFactory.createBevelBorder(1));
		JPanel labelPanel = new JPanel(new GridBagLayout());
		labelPanel.add(tooltipLabel, new GridBagConstraints(0,0,1,1,1.0,1.0, GridBagConstraints.SOUTH, GridBagConstraints.NONE, new Insets(10,10,10,10), 0, 0));
		tooltipPanel.add(labelPanel, new GridBagConstraints(2,1,1,1,1.0,1.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(10,10,10,0), 0, 0));

		tooltipPanel.setBorder(BorderFactory.createTitledBorder("Balloon tooltip:"));

		add(tooltipPanel, new GridBagConstraints(0,gridY,1,1,1.0,1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(10,10,10,10), 0, 0));
		++gridY;


		/*
		 * Add the GUI's behaviour
		 */

		// Timed balloon tip
		showBalloon.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					int timeoutVal =  Integer.parseInt(timeout.getText());
					BalloonTip balloonTip = new BalloonTip(showBalloon, 
							"I will dissapear in " + timeoutVal/1000 + " seconds.",
							new EdgedBalloonStyle(Color.WHITE, Color.BLUE), 
							BalloonTip.Orientation.LEFT_ABOVE, 
							BalloonTip.AttachLocation.ALIGNED, 
							20, 20, 
							false,
							Complete.isDrawnOutsideParent());
					TimingUtils.showTimedBalloon(balloonTip, timeoutVal);
				} catch (Exception exc) {
					if (!timeout.getText().equals("")) {
						CompleteExample.showErrorMessage(timeout, "Please enter a positive amount of milliseconds");
					}
				}
			}
		});

		// Balloon tooltip
		tooltipBalloon = new BalloonTip(tooltipLabel, "I'm a balloon tooltip!",
				new EdgedBalloonStyle(Color.WHITE, Color.BLUE), 
				BalloonTip.Orientation.LEFT_ABOVE, 
				BalloonTip.AttachLocation.ALIGNED, 
				20, 20, 
				false,
				Complete.isDrawnOutsideParent());
		ToolTipUtils.balloonToToolTip(tooltipBalloon, 500, 3000);

	}
}
