/**
 * Balloontip - Balloon tips for Java Swing applications
 * Copyright 2007, 2008, 2010 Bernhard Pauler, Tim Molderez, Thierry Blind
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

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import net.java.balloontip.BalloonTip;

public class RedrawPanel extends JPanel {
	/**
	 * Default constructor
	 */
	public RedrawPanel() {
		super();
		setLayout(new GridBagLayout());

		JPanel redrawPanel = new JPanel();
		redrawPanel.setLayout(new GridBagLayout());
		redrawPanel.add(new JLabel("BalloonTips drawn outside the bounds of this window will again be fully drawn inside."), new GridBagConstraints(0,0,3,1,1.0,0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5,5,5,5), 0, 0));
		final JButton redrawButton = new JButton("Fully draw balloonTips inside the window");
		JPanel labelRedrawPanel = new JPanel(new GridBagLayout());
		labelRedrawPanel.add(redrawButton, new GridBagConstraints(0,0,1,1,1.0,1.0, GridBagConstraints.SOUTH, GridBagConstraints.NONE, new Insets(0,5,5,5), 0, 0));
		redrawPanel.add(labelRedrawPanel, new GridBagConstraints(0,1,1,1,1.0,1.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0,0,0,0), 0, 0));

		add(redrawPanel, new GridBagConstraints(0,0,1,1,1.0,1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0,0,0,0), 0, 0));

		redrawButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Redraw inside the bounds of the parent window
				BalloonTip.drawAllInsideParent();
			}
		});
	}
}
