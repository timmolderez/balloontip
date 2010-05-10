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

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import net.java.balloontip.examples.complete.Complete;

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
		setLayout(new BorderLayout());

		JTabbedPane tabbedPane = new JTabbedPaneEx();
		tabbedPane.setTabPlacement(JTabbedPane.TOP);
		tabbedPane.addTab("Looks", new LooksTab());
		tabbedPane.addTab("Behaviour", new BehaviourTab());
		tabbedPane.addTab("Types", new TypesTab());
		tabbedPane.addTab("Utilities", new UtilitiesTab());
		tabbedPane.addTab("Layered Pane", new LayeredPaneTab());

		add(tabbedPane, BorderLayout.CENTER);
		if (Complete.isDrawnOutsideParent()) {
			add(new RedrawPanel(), BorderLayout.SOUTH);
		}
	}
}
