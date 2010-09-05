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

package net.java.balloontip.examples.complete.panels;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

/**
 * Panel that contains all tabs of the Balloontip example application
 * @author Tim Molderez
 */
public class MainPanel extends JPanel {
	/**
	 * Default constructor
	 */
	public MainPanel() {
		super();
		setLayout(new GridBagLayout());
		
		JTabbedPane tabbedPane = new JTabbedPaneEx();
		tabbedPane.setTabPlacement(JTabbedPane.TOP);
		tabbedPane.addTab("Looks", new LooksTab());
		tabbedPane.addTab("Behaviour", new BehaviourTab());
		tabbedPane.addTab("Types", new TypesTab());
		tabbedPane.addTab("Utilities", new UtilitiesTab());
		tabbedPane.addTab("Layered Pane", new LayeredPaneTab());
		
		add(tabbedPane, new GridBagConstraints(0,0,1,1,1,1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(10,10,10,10), 0, 0));
	}
}
