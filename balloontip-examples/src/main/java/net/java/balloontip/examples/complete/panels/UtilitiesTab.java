/**
 * Copyright (c) 2011-2013 Bernhard Pauler, Tim Molderez.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the 3-Clause BSD License
 * which accompanies this distribution, and is available at
 * http://www.opensource.org/licenses/BSD-3-Clause
 */

package net.java.balloontip.examples.complete.panels;

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
import net.java.balloontip.examples.complete.Utils;
import net.java.balloontip.utils.FadingUtils;
import net.java.balloontip.utils.TimingUtils;
import net.java.balloontip.utils.ToolTipUtils;

/**
 * Utilities tab of the demo application; demonstrates the various utility functions of Balloon tip
 * @author Tim Molderez
 */
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
		timePanel.add(new JLabel("<html>Any " + Utils.monospace("BalloonTip") + " can be given a time-out value.</html>"), new GridBagConstraints(0,0,3,1,1.0,0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10,10,10,0), 0, 0));
		timePanel.add(new JLabel("Time-out (ms):"), new GridBagConstraints(0,1,1,1,0.0,0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10,10,10,0), 0, 0));
		final JTextField timeout = new JTextField("3000");
		timeout.setPreferredSize(new Dimension(50,25));
		timePanel.add(timeout, new GridBagConstraints(1,1,1,1,0.0,0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10,10,10,0), 0, 0));
		final JButton showBalloon = new JButton("Start timer");
		timePanel.add(showBalloon, new GridBagConstraints(0,2,3,1,1.0,0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(10,10,10,0), 0, 0));
		timePanel.setBorder(BorderFactory.createTitledBorder("Timed balloon tip:"));
		add(timePanel, new GridBagConstraints(0,gridY,1,1,1.0,1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(10,10,10,10), 0, 0));
		++gridY;

		// Fading balloon tip
		JPanel fadePanel = new JPanel();
		fadePanel.setLayout(new GridBagLayout());
		fadePanel.add(new JLabel("Fade effects can be added to balloon tips."), new GridBagConstraints(0,0,3,1,1.0,0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10,10,10,0), 0, 0));
		final JButton fadeBalloon = new JButton("Fade balloon");
		fadePanel.add(fadeBalloon, new GridBagConstraints(0,1,3,1,1.0,1.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(10,10,10,0), 0, 0));
		fadePanel.setBorder(BorderFactory.createTitledBorder("Fading balloon tips:"));
		add(fadePanel, new GridBagConstraints(0,gridY,1,1,1.0,1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(10,10,10,10), 0, 0));
		++gridY;

		// Balloon tooltip
		JPanel tooltipPanel = new JPanel();
		tooltipPanel.setLayout(new GridBagLayout());
		tooltipPanel.add(new JLabel("<html>Any " + Utils.monospace("BalloonTip") + " can be turned into a tooltip.</html>"), new GridBagConstraints(0,0,3,1,1.0,0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10,10,10,0), 0, 0));
		final JLabel tooltipLabel = new JLabel("Hover me to show the tooltip.");
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
							new JLabel("I will dissapear in " + timeoutVal/1000 + " seconds."),
							Utils.createBalloonTipStyle(),
							Utils.createBalloonTipPositioner(), 
							null);
					TimingUtils.showTimedBalloon(balloonTip, timeoutVal);
				} catch (Exception exc) {
					if (!timeout.getText().equals("")) {
						Utils.showErrorMessage(timeout, "Please enter a positive amount of milliseconds");
					}
				}
			}
		});
		
		// Fading balloon tip
		final BalloonTip fadingBalloonTip = new BalloonTip(fadeBalloon, new JLabel("I'm a fading balloon tip!"),
				Utils.createBalloonTipStyle(),
				Utils.createBalloonTipPositioner(), 
				null);
		fadingBalloonTip.setOpacity(0.0f);
		
		fadeBalloon.addActionListener(new ActionListener() {
			private boolean isShown = false;
			private ActionListener onStop = new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					isShown = !isShown;
					fadeBalloon.setEnabled(true);
				}
			};

			public void actionPerformed(ActionEvent e) {
				fadeBalloon.setEnabled(false);
				if (isShown) {
					FadingUtils.fadeOutBalloon(fadingBalloonTip, onStop , 500, 24);
				} else {
					FadingUtils.fadeInBalloon(fadingBalloonTip, onStop , 500, 24);
				}
			}

		});

		// Balloon tooltip
		tooltipBalloon = new BalloonTip(tooltipLabel, new JLabel("I'm a balloon tooltip!"),
				Utils.createBalloonTipStyle(),
				Utils.createBalloonTipPositioner(), 
				null);
		ToolTipUtils.balloonToToolTip(tooltipBalloon, 200, 3000);

	}
}
