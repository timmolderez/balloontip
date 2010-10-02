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

import net.java.balloontip.BalloonTip;
import net.java.balloontip.TablecellBalloonTip;
import net.java.balloontip.examples.complete.CompleteExample;
import net.java.balloontip.positioners.BasicBalloonTipPositioner;
import net.java.balloontip.positioners.Left_Above_Positioner;
import net.java.balloontip.positioners.Left_Below_Positioner;
import net.java.balloontip.positioners.Right_Above_Positioner;
import net.java.balloontip.positioners.Right_Below_Positioner;
import net.java.balloontip.styles.EdgedBalloonStyle;

public class BehaviourTab extends JPanel {
	private final JComboBox alignmentPicker;
	private final JComboBox attachPicker;
	private final JCheckBox offsetCorrection;
	private final JCheckBox orientationCorrection;
	private final TablecellBalloonTip tableBalloon;

	private final static int HOFFSET = 40;
	private final static int VOFFSET = 15;

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
		tableBalloon = new TablecellBalloonTip(table, 
				new JLabel("Use the scrollbars to move me around!"), 32, 16,
				new EdgedBalloonStyle(Color.WHITE, Color.BLUE), 
				BalloonTip.Orientation.LEFT_ABOVE, 
				BalloonTip.AttachLocation.ALIGNED, 
				HOFFSET, VOFFSET, 
				false);
		tableBalloon.setPadding(10);
		tableBalloon.setViewport(tableScrollPane.getViewport());
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
					positioner = new Left_Above_Positioner(HOFFSET, VOFFSET);
					break;
				case 1:
					positioner = new Left_Below_Positioner(HOFFSET, VOFFSET);
					break;
				case 2:
					positioner = new Right_Above_Positioner(HOFFSET, VOFFSET);
					break;
				case 3:
					positioner = new Right_Below_Positioner(HOFFSET, VOFFSET);
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