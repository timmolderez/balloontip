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

import javax.swing.JPanel;
import javax.swing.JScrollPane;
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
		
		JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.setTabPlacement(JTabbedPane.TOP);
		tabbedPane.addTab("Looks", new LooksTab());
		tabbedPane.addTab("Contents", new ContentsTab());
		tabbedPane.addTab("Behaviour", new BehaviourTab());
		
		TypesTab typesTab = new TypesTab();
		typesTab.setPreferredSize(new Dimension(420, 1000));
		JScrollPane typesScrollPane = new JScrollPane(typesTab);
		tabbedPane.addTab("Types", typesScrollPane);
		
		tabbedPane.addTab("Layers", new LayersTab());
		tabbedPane.addTab("Utilities", new UtilitiesTab());
		
		add(tabbedPane, new GridBagConstraints(0,0,1,1,1,1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(10,10,10,10), 0, 0));
	}
}
