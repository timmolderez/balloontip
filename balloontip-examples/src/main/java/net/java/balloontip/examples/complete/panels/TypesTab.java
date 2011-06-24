/**
 * Copyright (c) 2011 Bernhard Pauler, Tim Molderez.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the 3-Clause BSD License
 * which accompanies this distribution, and is available at
 * http://www.opensource.org/licenses/BSD-3-Clause
 */

package net.java.balloontip.examples.complete.panels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.table.TableColumn;

import net.java.balloontip.BalloonTip;
import net.java.balloontip.CustomBalloonTip;
import net.java.balloontip.TablecellBalloonTip;
import net.java.balloontip.examples.complete.CompleteExample;

/**
 * Types tab of the demo application; demonstrates the different balloon tip types
 * @author Tim Molderez
 */
public class TypesTab extends JPanel {
	/**
	 * Default constructor
	 */
	public TypesTab() {
		super();
		setLayout(new GridBagLayout());
		int gridY = 0;

		/*
		 * Draw the GUI
		 */

		// Regular balloon tip
		JPanel regularPanel = new JPanel();
		regularPanel.setLayout(new GridBagLayout());
		regularPanel.add(new JLabel("A BalloonTip can attach itself to JComponents (buttons, labels, text fields, ...)."), new GridBagConstraints(0,0,1,1,1.0,0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10,10,10,0), 0, 0));
		final JLabel label = new JLabel("A label");
		label.setBorder(BorderFactory.createBevelBorder(1));
		JPanel labelPanel = new JPanel(new GridBagLayout());
		labelPanel.add(label, new GridBagConstraints(0,0,1,1,1.0,1.0, GridBagConstraints.SOUTH, GridBagConstraints.NONE, new Insets(10,10,10,10), 0, 0));
		regularPanel.add(labelPanel, new GridBagConstraints(0,1,1,1,1.0,1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(10,10,10,10), 0, 0));
		regularPanel.setBorder(BorderFactory.createTitledBorder("Regular balloon tip:"));
		add(regularPanel, new GridBagConstraints(0,gridY,1,1,1.0,1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(10,10,10,10), 0, 0));
		++gridY;

		// Tablecell balloon tip
		JPanel tableCellPanel = new JPanel();
		tableCellPanel.setLayout(new GridBagLayout());
		tableCellPanel.add(new JLabel("A TablecellBalloonTip can attach itself to a JTable cell."), new GridBagConstraints(0,0,1,1,1.0,0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10,10,10,0), 0, 0));
		final JTable table = new JTable(8,32);
		table.getModel().setValueAt("A cell", 4, 16);
		table.setTableHeader(null); // Remove the table header
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		TableColumn column = null;
		for (int i=0; i<32; ++i) {
			column = table.getColumnModel().getColumn(i);
			column.setPreferredWidth(50);
		}		
		final JScrollPane tableScrollPane = new JScrollPane(table);


		tableCellPanel.add(tableScrollPane, new GridBagConstraints(0,1,1,1,1.0,1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(10,10,10,10), 0, 0));
		tableCellPanel.setBorder(BorderFactory.createTitledBorder("Table-cell balloon tip:"));

		add(tableCellPanel, new GridBagConstraints(0,gridY,1,1,1.0,1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(10,10,10,10), 0, 0));
		++gridY;

		// Custom balloon tip
		JPanel customPanel = new JPanel();
		customPanel.setLayout(new GridBagLayout());
		customPanel.add(new JLabel("A CustomBalloonTip can attach itself to a rectangular shape within a JComponent."), new GridBagConstraints(0,0,1,1,1.0,0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10,10,10,0), 0, 0));
		final Rectangle customOffset = new Rectangle(320, 60, 50, 16);
		final JComponent customComponent = new JComponent() {
			protected void paintComponent(Graphics g) {
				Rectangle clip = g.getClipBounds();
				g.setColor(Color.WHITE);
				g.fillRect(clip.x, clip.y, clip.width, clip.height);
				g.setColor(Color.BLUE);
				g.fillRect(customOffset.x, customOffset.y, customOffset.width, customOffset.height);
			}
		};
		customComponent.setPreferredSize(new Dimension(640, 120));
		final JScrollPane customScrollPane = new JScrollPane(customComponent);
		customPanel.add(customScrollPane, new GridBagConstraints(0,1,1,1,1.0,1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(10,10,10,10), 0, 0));
		customPanel.setBorder(BorderFactory.createTitledBorder("Custom balloon tip:"));

		add(customPanel, new GridBagConstraints(0,gridY,1,1,1.0,1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(10,10,10,10), 0, 0));
		++gridY;

		/*
		 * Add the GUI's behaviour
		 */

		// Regular balloon tip
		new BalloonTip(label, new JLabel("I'm a BalloonTip!"),
				CompleteExample.createBalloonTipStyle(),
				CompleteExample.createBalloonTipPositioner(), 
				null);

		// Tablecell balloon tip
		new TablecellBalloonTip(table, new JLabel("I'm a TableCellBalloonTip!"), 4, 16,
				CompleteExample.createBalloonTipStyle(),
				CompleteExample.createBalloonTipPositioner(), 
				null);
		table.addAncestorListener(new AncestorListener() {
			public void ancestorAdded(AncestorEvent event) {
				table.scrollRectToVisible(table.getCellRect(5, 20, true));
			}
			public void ancestorMoved(AncestorEvent event) {}
			public void ancestorRemoved(AncestorEvent event) {}
		});

		// Custom balloon tip
		new CustomBalloonTip(customComponent, 
				new JLabel("I'm a CustomBalloonTip!"),
				customOffset,
				CompleteExample.createBalloonTipStyle(),
				CompleteExample.createBalloonTipPositioner(), 
				null);
		customComponent.addAncestorListener(new AncestorListener() {
			public void ancestorAdded(AncestorEvent event) {
				customComponent.scrollRectToVisible(new Rectangle(500,80,10,10));
			}
			public void ancestorMoved(AncestorEvent event) {}
			public void ancestorRemoved(AncestorEvent event) {}

		});
	}
}
