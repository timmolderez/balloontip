/**
 * Copyright (c) 2011 Bernhard Pauler, Tim Molderez.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the 3-Clause BSD License
 * which accompanies this distribution, and is available at
 * http://www.opensource.org/licenses/BSD-3-Clause
 */

package net.java.balloontip.examples.complete.panels;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.table.TableColumn;

import net.java.balloontip.TableCellBalloonTip;
import net.java.balloontip.examples.complete.CompleteExample;
import net.java.balloontip.positioners.BasicBalloonTipPositioner;
import net.java.balloontip.positioners.LeftAbovePositioner;
import net.java.balloontip.positioners.LeftBelowPositioner;
import net.java.balloontip.positioners.RightAbovePositioner;
import net.java.balloontip.positioners.RightBelowPositioner;

/**
 * Behaviour tab of the demo application; demonstrates different balloon tip positioners
 * @author Tim Molderez
 */
public class BehaviourTab extends JPanel {
	private final JComboBox alignmentPicker;
	private final JComboBox attachPicker;
	private final JCheckBox offsetCorrection;
	private final JCheckBox orientationCorrection;
	private final TableCellBalloonTip tableBalloon;

	private final static int HOFFSET = 40;
	private final static int VOFFSET = 20;

	public BehaviourTab() {
		super();
		setLayout(new GridBagLayout());
		int gridY = 0;

		/*
		 * Draw the GUI
		 */

		// Description label
		add(new JLabel("<html>Scroll around the table and notice the balloon tip's behaviour when it collides with the window's border. You can alter the balloon tip's behaviour using the settings below.</html>"), new GridBagConstraints(0,gridY,2,1,1.0,0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10,10,25,0), 0, 0));
		++gridY;

		// Orientation combobox
		add(new JLabel("Orientation:"), new GridBagConstraints(0,gridY,1,1,0.0,0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0,10,0,0), 0, 0));
		String[] alignmentOptions = {"Left, above component", "Left, below component", "Right, above component", "Right, below component"};
		alignmentPicker = new JComboBox(alignmentOptions);
		add(alignmentPicker, new GridBagConstraints(1,gridY,1,1,0.0,0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2,10,2,0), 0, 0));
		++gridY;

		// Attaching location combobox
		add(new JLabel("Attaching location:"), new GridBagConstraints(0,gridY,1,1,0.0,0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0,10,0,0), 0, 0));
		String[] attachOptions = {"Aligned", "Center", "North", "North east", "East", "South east", "South", "South west", "West", "North west"};
		attachPicker = new JComboBox(attachOptions);
		add(attachPicker, new GridBagConstraints(1,gridY,1,1,0.0,0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2,10,2,0), 0, 0));
		++gridY;

		// Offset correction checkbox
		add(new JLabel("Offset correction:"), new GridBagConstraints(0,gridY,1,1,0.0,0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0,10,0,0), 0, 0));
		offsetCorrection = new JCheckBox();
		offsetCorrection.setSelected(true);
		CompleteExample.setToolTip(offsetCorrection, "<html>Offset correction will adjust the horizontal offset<br>when the balloon tip collides with the window border</html>");
		add(offsetCorrection, new GridBagConstraints(1,gridY,1,1,0.0,0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2,10,2,0), 0, 0));
		++gridY;

		// Orientation correction checkbox
		add(new JLabel("Orientation correction:"), new GridBagConstraints(0,gridY,1,1,0.0,0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0,10,0,0), 0, 0));
		orientationCorrection = new JCheckBox();
		orientationCorrection.setSelected(true);
		CompleteExample.setToolTip(orientationCorrection, "<html>Orientation correction will flip the balloon tip<br>when it collides with the window border</html>");
		add(orientationCorrection, new GridBagConstraints(1,gridY,1,1,0.0,0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2,10,2,0), 0, 0));
		++gridY;

		// Table
		final JTable table = new JTable(64,32);
		table.getModel().setValueAt("Cell with a balloon tip", 32, 16);
		table.setTableHeader(null); // Remove the table header
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		TableColumn column = null;
		for (int i=0; i<32; ++i) {
			column = table.getColumnModel().getColumn(i);
			column.setPreferredWidth(120);
		}
		final JScrollPane tableScrollPane = new JScrollPane(table);
		tableScrollPane.getHorizontalScrollBar().setUnitIncrement(3);
		tableScrollPane.getVerticalScrollBar().setUnitIncrement(3);
		add(tableScrollPane, new GridBagConstraints(0,gridY,2,1,1.0,1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(10,10,10,10), 0, 0));

		/*
		 * Add the GUI's behaviour
		 */

		// Tablecell balloon tip
		tableBalloon = new TableCellBalloonTip(table, 
				new JLabel("Use the scrollbars to move me around!"), 32, 16,
				CompleteExample.createBalloonTipStyle(), 
				new LeftAbovePositioner(HOFFSET, VOFFSET),
				null);
		table.addAncestorListener(new AncestorListener() {
			public void ancestorAdded(AncestorEvent event) {
				table.scrollRectToVisible(table.getCellRect(38, 18, true));
			}
			public void ancestorMoved(AncestorEvent event) {}
			public void ancestorRemoved(AncestorEvent event) {}

		});

		// Alignment combo box
		alignmentPicker.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				BasicBalloonTipPositioner positioner = null;

				switch(alignmentPicker.getSelectedIndex()) {
				case 0:
					positioner = new LeftAbovePositioner(HOFFSET, VOFFSET);
					break;
				case 1:
					positioner = new LeftBelowPositioner(HOFFSET, VOFFSET);
					break;
				case 2:
					positioner = new RightAbovePositioner(HOFFSET, VOFFSET);
					break;
				case 3:
					positioner = new RightBelowPositioner(HOFFSET, VOFFSET);
					break;
				}

				copyPositionerSettings(((BasicBalloonTipPositioner)tableBalloon.getPositioner()), positioner);
				tableBalloon.setPositioner(positioner);
			}
		});

		// Attach location combo box
		attachPicker.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				((BasicBalloonTipPositioner)tableBalloon.getPositioner()).enableFixedAttachLocation(true);

				switch(attachPicker.getSelectedIndex()) {
				case 0:
					((BasicBalloonTipPositioner)tableBalloon.getPositioner()).enableFixedAttachLocation(false);
					break;
				case 1:
					((BasicBalloonTipPositioner)tableBalloon.getPositioner()).setAttachLocation(0.5f, 0.5f);
					break;
				case 2:
					((BasicBalloonTipPositioner)tableBalloon.getPositioner()).setAttachLocation(0.5f, 0.0f);
					break;
				case 3:
					((BasicBalloonTipPositioner)tableBalloon.getPositioner()).setAttachLocation(1.0f, 0.0f);
					break;
				case 4:
					((BasicBalloonTipPositioner)tableBalloon.getPositioner()).setAttachLocation(1.0f, 0.5f);
					break;
				case 5:
					((BasicBalloonTipPositioner)tableBalloon.getPositioner()).setAttachLocation(1.0f, 1.0f);
					break;
				case 6:
					((BasicBalloonTipPositioner)tableBalloon.getPositioner()).setAttachLocation(0.5f, 1.0f);
					break;
				case 7:
					((BasicBalloonTipPositioner)tableBalloon.getPositioner()).setAttachLocation(0.0f, 1.0f);
					break;
				case 8:
					((BasicBalloonTipPositioner)tableBalloon.getPositioner()).setAttachLocation(0.0f, 0.5f);
					break;
				case 9:
					((BasicBalloonTipPositioner)tableBalloon.getPositioner()).setAttachLocation(0.0f, 0.0f);
					break;
				}
				tableBalloon.refreshLocation();
			}
		});

		// Offset correction
		offsetCorrection.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (offsetCorrection.isSelected()) {
					((BasicBalloonTipPositioner)tableBalloon.getPositioner()).enableOffsetCorrection(true);
				} else {
					((BasicBalloonTipPositioner)tableBalloon.getPositioner()).enableOffsetCorrection(false);
				}
				tableBalloon.refreshLocation();
			}
		});

		// Orientation correction
		orientationCorrection.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (orientationCorrection.isSelected()) {
					((BasicBalloonTipPositioner)tableBalloon.getPositioner()).enableOrientationCorrection(true);
				} else {
					((BasicBalloonTipPositioner)tableBalloon.getPositioner()).enableOrientationCorrection(false);
				}
				tableBalloon.refreshLocation();
			}
		});
	}

	private void copyPositionerSettings(final BasicBalloonTipPositioner oldPositioner, BasicBalloonTipPositioner newPositioner) {
		newPositioner.enableOffsetCorrection(oldPositioner.isOffsetCorrected());
		newPositioner.enableOrientationCorrection(oldPositioner.isOrientationCorrected());
		newPositioner.enableFixedAttachLocation(oldPositioner.isFixedAttachLocation());
		newPositioner.setAttachLocation(oldPositioner.getAttachLocationX(), oldPositioner.getAttachLocationY());
	}
}
