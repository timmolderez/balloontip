/**
 * Copyright (c) 2011-2013 Bernhard Pauler, Tim Molderez.
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
import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.table.TableColumn;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import net.java.balloontip.BalloonTip;
import net.java.balloontip.CustomBalloonTip;
import net.java.balloontip.ListItemBalloonTip;
import net.java.balloontip.TableCellBalloonTip;
import net.java.balloontip.TreeNodeBalloonTip;
import net.java.balloontip.examples.complete.Utils;

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
		regularPanel.add(new JLabel("<html>A " + Utils.monospace("BalloonTip") + " can attach itself to " + Utils.monospace("JComponent") + "s (buttons, labels, text fields, checkboxes, ...).</html>"), 
				new GridBagConstraints(0,0,1,1,1.0,0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10,10,10,0), 0, 0));
		final JLabel label = new JLabel("A label");
		label.setBorder(BorderFactory.createBevelBorder(1));
		JPanel labelPanel = new JPanel(new GridBagLayout());
		labelPanel.add(label, new GridBagConstraints(0,0,1,1,1.0,1.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0,0,0,0), 0, 0));
		regularPanel.add(labelPanel, new GridBagConstraints(0,1,1,1,1.0,1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0,0,0,0), 0, 0));
		regularPanel.setBorder(BorderFactory.createTitledBorder("Regular balloon tip:"));
		add(regularPanel, new GridBagConstraints(0,gridY,1,1,1.0,1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(10,10,10,10), 0, 0));
		++gridY;
		
		// Custom balloon tip
		JPanel customPanel = new JPanel();
		customPanel.setLayout(new GridBagLayout());
		customPanel.add(new JLabel("<html>A " + Utils.monospace("CustomBalloonTip") + " is just like a regular " + Utils.monospace("BalloonTip") 
				+ ", but it can attach itself to a rectangular offset within a " + Utils.monospace("JComponent") + " rather than the component in its entirety.</html>"), 
				new GridBagConstraints(0,0,1,1,1.0,0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10,10,10,0), 0, 0));
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

		// Table cell balloon tip
		JPanel tableCellPanel = new JPanel();
		tableCellPanel.setLayout(new GridBagLayout());
		tableCellPanel.add(new JLabel("<html>A " + Utils.monospace("TableCellBalloonTip") + " can attach itself to a " + Utils.monospace("JTable") + " cell.</html>"), 
				new GridBagConstraints(0,0,1,1,1.0,0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10,10,10,0), 0, 0));
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
		tableCellPanel.setBorder(BorderFactory.createTitledBorder("Table cell balloon tip:"));
		add(tableCellPanel, new GridBagConstraints(0,gridY,1,1,1.0,1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(10,10,10,10), 0, 0));
		++gridY;
		
		// List item balloon tip
		JPanel listPanel = new JPanel();
		listPanel.setLayout(new GridBagLayout());
		listPanel.add(new JLabel("<html>A " + Utils.monospace("ListItemBalloonTip") + " can attach itself to an item within a " + Utils.monospace("JList") 
				+ ". The balloon tip will adjust itself when the list is modified.</html>"), 
				new GridBagConstraints(0,0,1,1,1.0,0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10,10,10,0), 0, 0));

		DefaultListModel<String> listModel = new DefaultListModel<String>();
        for (int i = 0; i < 10; i++) {
			listModel.addElement("Item " + i);	
		}

        JList<String> list = new JList<String>(listModel);
		listPanel.add(new JScrollPane(list), new GridBagConstraints(0,1,1,1,1.0,1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(10,10,10,10), 0, 0));
		listPanel.setBorder(BorderFactory.createTitledBorder("List item balloon tip:"));

		add(listPanel, new GridBagConstraints(0,gridY,1,1,1.0,1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(10,10,10,10), 0, 0));
		++gridY;
		
		// Tree node balloon tip
		JPanel treePanel = new JPanel();
		treePanel.setLayout(new GridBagLayout());
		treePanel.add(new JLabel("<html>Given a " + Utils.monospace("TreePath") + ", a " + Utils.monospace("TreeNodeBalloonTip") 
				+ " can be used to attach a balloon tip to a " + Utils.monospace("JTree") + " node. The balloon tip will adjust itself when the tree is modified, or nodes are expanded/collapsed.</html>"), 
				new GridBagConstraints(0,0,1,1,1.0,0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10,10,10,0), 0, 0));

		DefaultMutableTreeNode treeRoot = new DefaultMutableTreeNode("Root");
		JTree tree = new JTree(treeRoot);
		for(int i=0; i<3; i++) {
			DefaultMutableTreeNode child = new DefaultMutableTreeNode("Child " + i);
			treeRoot.add(child);
			
			for(int j=0; j<3; j++) {
				DefaultMutableTreeNode leaf = new DefaultMutableTreeNode("Leaf " + j);
				child.add(leaf);
			}
		}
		for (int i = 0; i < tree.getRowCount(); i++) {
			tree.expandRow(i);
		}
		
		treePanel.add(new JScrollPane(tree), new GridBagConstraints(0,1,1,1,1.0,1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(10,10,10,10), 0, 0));
		treePanel.setBorder(BorderFactory.createTitledBorder("Tree node balloon tip:"));

		add(treePanel, new GridBagConstraints(0,gridY,1,1,1.0,1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(10,10,10,10), 0, 0));
		++gridY;

		/*
		 * Add the GUI's behaviour
		 */

		// Regular balloon tip
		new BalloonTip(label, new JLabel("<html>I'm a " + Utils.monospace("BalloonTip") + ".</html>"),
				Utils.createBalloonTipStyle(),
				Utils.createBalloonTipPositioner(), 
				null);
		
		// Custom balloon tip
		new CustomBalloonTip(customComponent, 
				new JLabel("<html>I'm a " + Utils.monospace("CustomBalloonTip") + ".</html>"),
				customOffset,
				Utils.createBalloonTipStyle(),
				Utils.createBalloonTipPositioner(), 
				null);
		customComponent.addAncestorListener(new AncestorListener() {
			public void ancestorAdded(AncestorEvent event) {
				customComponent.scrollRectToVisible(new Rectangle(500,80,10,10));
			}
			public void ancestorMoved(AncestorEvent event) {}
			public void ancestorRemoved(AncestorEvent event) {}

		});

		// Tablecell balloon tip
		new TableCellBalloonTip(table, new JLabel("<html>I'm a " + Utils.monospace("TableCellBalloonTip") + ".</html>"), 4, 16,
				Utils.createBalloonTipStyle(),
				Utils.createBalloonTipPositioner(), 
				null);
		table.addAncestorListener(new AncestorListener() {
			public void ancestorAdded(AncestorEvent event) {
				table.scrollRectToVisible(table.getCellRect(5, 20, true));
			}
			public void ancestorMoved(AncestorEvent event) {}
			public void ancestorRemoved(AncestorEvent event) {}
		});
		
		// List item balloon tip
		new ListItemBalloonTip(list, 
				new JLabel("<html>I'm a " + Utils.monospace("ListItemBalloonTip") + ".</html>"),3, 
        		Utils.createBalloonTipStyle(),
        		Utils.createBalloonTipPositioner(), null);
		
		// Tree node balloon tip
		TreePath path = tree.getPathForRow(3);
		new TreeNodeBalloonTip(tree, 
				new JLabel("<html>I'm a " + Utils.monospace("TreeNodeBalloonTip") + ".</html>"), 
				path,
				Utils.createBalloonTipStyle(),
        		Utils.createBalloonTipPositioner(), null);
	}
}
