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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;

import net.java.balloontip.BalloonTip;
import net.java.balloontip.TablecellBalloonTip;
import net.java.balloontip.examples.complete.CompleteExample;
import net.java.balloontip.styles.EdgedBalloonStyle;

/**
 * Contents tab of the demo application; demonstrates that a balloon tip can contain all sorts of components
 * @author Tim Molderez
 */
public class ContentsTab extends JPanel {
	/**
	 * Default constructor
	 */
	public ContentsTab() {
		super();
		setLayout(new GridBagLayout());
		int gridY = 0;

		/*
		 * Draw the GUI
		 */

		// Description label
		add(new JLabel("<html>A balloon tip can contain many types of JComponent beside just text.</html>"), new GridBagConstraints(0,gridY,2,1,1.0,0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10,10,25,0), 0, 0));
		++gridY;

		// Balloon tip 1
		JLabel label1 = new JLabel("A balloon tip with an icon");
		add(label1, new GridBagConstraints(0,gridY,1,1,1.0,1.0, GridBagConstraints.SOUTH, GridBagConstraints.NONE, new Insets(0,0,80,0), 0, 0));
		
		JLabel contents1= new JLabel("I've got an icon!");
		contents1.setIcon(new ImageIcon(CompleteExample.class.getResource("/net/java/balloontip/images/frameIcon.png")));
		contents1.setIconTextGap(10);
		new BalloonTip(label1, contents1,
				new EdgedBalloonStyle(Color.WHITE, Color.BLUE), 
				BalloonTip.Orientation.LEFT_ABOVE, 
				BalloonTip.AttachLocation.ALIGNED, 
				15, 15, 
				false);
		
		// Balloon tip 2
		JLabel label2 = new JLabel("A balloon tip with tabs");
		add(label2, new GridBagConstraints(1,gridY,1,1,1.0,1.0, GridBagConstraints.SOUTH, GridBagConstraints.NONE, new Insets(0,0,80,0), 0, 0));
		++gridY;
		
		JTabbedPane contents2 = new JTabbedPane();
		JPanel tab1 = new JPanel();
		tab1.add(new JCheckBox());
		tab1.add(new JLabel("Tabs? But of course!"));
		JPanel tab2 = new JPanel();
		tab2.add(new JLabel("Because we can!"));
		contents2.addTab("FirstTab", tab1);
		contents2.addTab("SecondTab", tab2);
		new BalloonTip(label2, contents2,
				new EdgedBalloonStyle(Color.WHITE, Color.BLUE), 
				BalloonTip.Orientation.LEFT_ABOVE, 
				BalloonTip.AttachLocation.ALIGNED, 
				15, 15, 
				false);

		// Balloon tip 3
		JLabel label3 = new JLabel("A balloon tip a table");
		add(label3, new GridBagConstraints(0,gridY,1,1,1.0,1.0, GridBagConstraints.SOUTH, GridBagConstraints.NONE, new Insets(0,0,80,0), 0, 0));
		
		JTable table = new JTable(8,2);
		table.setTableHeader(null);
		table.getModel().setValueAt("Balloon tip", 0, 0);
		table.getModel().setValueAt("with", 1, 1);
		table.getModel().setValueAt("a", 2, 0);
		table.getModel().setValueAt("table", 3, 1);
		JScrollPane tableScrollPane = new JScrollPane(table);
		tableScrollPane.setPreferredSize(new Dimension(150,100));
		tableScrollPane.setBackground(Color.WHITE);
		new BalloonTip(label3, tableScrollPane,
				new EdgedBalloonStyle(Color.WHITE, Color.BLUE), 
				BalloonTip.Orientation.LEFT_ABOVE, 
				BalloonTip.AttachLocation.ALIGNED, 
				15, 15, 
				false);
		
		// Balloon tip 4
		JLabel label4 = new JLabel("Nesting balloon tips");
		add(label4, new GridBagConstraints(1,gridY,1,1,1.0,1.0, GridBagConstraints.SOUTH, GridBagConstraints.NONE, new Insets(0,0,80,0), 0, 0));
		++gridY;
		JTable comp1 = new JTable(8,2);
		comp1.setTableHeader(null);
		comp1.getModel().setValueAt("This", 0, 0);
		comp1.getModel().setValueAt("just", 1, 1);
		comp1.getModel().setValueAt("might", 2, 0);
		JScrollPane panel1 = new JScrollPane(comp1);
		panel1.setPreferredSize(new Dimension(100, 75));
		panel1.setBackground(Color.WHITE);
		new BalloonTip(label4, panel1,
				new EdgedBalloonStyle(Color.WHITE, Color.BLUE), 
				BalloonTip.Orientation.LEFT_ABOVE, 
				BalloonTip.AttachLocation.ALIGNED, 
				15, 15, 
				false);
		
		JPanel panel2 = new JPanel();
		panel2.setBackground(Color.WHITE);
		final JLabel comp2 = new JLabel("be a bit");
		comp2.setIcon(new ImageIcon(CompleteExample.class.getResource("/net/java/balloontip/images/frameIcon.png")));
		panel2.add(comp2);
		final TablecellBalloonTip tableBt = new TablecellBalloonTip(comp1, panel2, 1,1,
				new EdgedBalloonStyle(Color.WHITE, Color.BLUE), 
				BalloonTip.Orientation.LEFT_ABOVE, 
				BalloonTip.AttachLocation.ALIGNED, 
				15, 15, 
				false);
		tableBt.setPadding(0);
		tableBt.setViewport(panel1.getViewport());
		
		JTabbedPane panel3 = new JTabbedPane();
		JPanel panel3Tab1 = new JPanel();
		panel3Tab1.add(new JLabel("overkill?"));
		JPanel panel3Tab2 = new JPanel();
		panel3Tab2.add(new JLabel("Don't you think?"));
		panel3.addTab("A", panel3Tab1);
		panel3.addTab("B", panel3Tab2);
		new BalloonTip(comp2, panel3,
				new EdgedBalloonStyle(Color.WHITE, Color.BLUE), 
				BalloonTip.Orientation.LEFT_ABOVE, 
				BalloonTip.AttachLocation.ALIGNED, 
				15, 15, 
				false);
	}
}
