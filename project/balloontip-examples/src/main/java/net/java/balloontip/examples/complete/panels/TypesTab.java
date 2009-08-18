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
import net.java.balloontip.styles.EdgedBalloonStyle;

public class TypesTab extends JPanel {
	private TablecellBalloonTip tableBalloon;
	private CustomBalloonTip customBalloon;
	
	/**
	 * Default constructor
	 */
	public TypesTab() {
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
		final JTable table = new JTable(64,64);
		table.getModel().setValueAt("A cell", 32, 32);
		table.setTableHeader(null); // Remove the table header
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		TableColumn column = null;
		for (int i=0; i<64; ++i) {
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
		final Rectangle customOffset = new Rectangle(640, 320, 50, 16);
		final JComponent customComponent = new JComponent() {
			protected void paintComponent(Graphics g) {
				Rectangle clip = g.getClipBounds();
				g.setColor(Color.WHITE);
				g.fillRect(clip.x, clip.y, clip.width, clip.height);
				g.setColor(Color.BLUE);
				g.fillRect(customOffset.x, customOffset.y, customOffset.width, customOffset.height);
			}
		};
		customComponent.setPreferredSize(new Dimension(1280, 640));
		final JScrollPane customScrollPane = new JScrollPane(customComponent);
		customPanel.add(customScrollPane, new GridBagConstraints(0,1,1,1,1.0,1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(10,10,10,10), 0, 0));
		customPanel.setBorder(BorderFactory.createTitledBorder("Custom balloon tip:"));
		
		add(customPanel, new GridBagConstraints(0,gridY,1,1,1.0,1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(10,10,10,10), 0, 0));
		++gridY;
		
		/*
		 * Add the GUI's behaviour
		 */
		
		// Regular balloon tip
		new BalloonTip(label, "I'm a BalloonTip!",
				new EdgedBalloonStyle(Color.WHITE, Color.BLUE), 
				BalloonTip.Orientation.LEFT_ABOVE, 
				BalloonTip.AttachLocation.ALIGNED, 
				20, 20, 
				false);
		
		// Tablecell balloon tip
		tableBalloon = new TablecellBalloonTip(table, "I'm a TableCellBalloonTip!", 32, 32,
				new EdgedBalloonStyle(Color.WHITE, Color.BLUE), 
				BalloonTip.Orientation.LEFT_ABOVE, 
				BalloonTip.AttachLocation.ALIGNED, 
				20, 20, 
				false);
		tableBalloon.setViewport(tableScrollPane.getViewport());
		table.addAncestorListener(new AncestorListener() {
			public void ancestorAdded(AncestorEvent event) {
				table.scrollRectToVisible(table.getCellRect(33, 38, true));
			}
			public void ancestorMoved(AncestorEvent event) {}
			public void ancestorRemoved(AncestorEvent event) {}
			
		});
		
		// Custom balloon tip
		customBalloon = new CustomBalloonTip(customComponent, 
				"I'm a CustomBalloonTip!",
				customOffset,
				new EdgedBalloonStyle(Color.WHITE, Color.BLUE), 
				BalloonTip.Orientation.LEFT_ABOVE, 
				BalloonTip.AttachLocation.ALIGNED, 
				20, 20, 
				false);
		customBalloon.setViewport(customScrollPane.getViewport());
		customComponent.addAncestorListener(new AncestorListener() {
			public void ancestorAdded(AncestorEvent event) {
				customComponent.scrollRectToVisible(new Rectangle(876,340,10,10));
			}
			public void ancestorMoved(AncestorEvent event) {}
			public void ancestorRemoved(AncestorEvent event) {}
			
		});
	}
}
