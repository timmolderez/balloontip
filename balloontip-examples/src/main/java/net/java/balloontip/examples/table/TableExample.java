/**
 * Copyright (c) 2011 Bernhard Pauler, Tim Molderez.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the 3-Clause BSD License
 * which accompanies this distribution, and is available at
 * http://www.opensource.org/licenses/BSD-3-Clause
 */

package net.java.balloontip.examples.table;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableColumn;

import net.java.balloontip.BalloonTip;
import net.java.balloontip.TableCellBalloonTip;
import net.java.balloontip.styles.MinimalBalloonStyle;

/**
 * Simple application demonstrating a TableCellBalloonTip
 * @author Tim Molderez
 */
public class TableExample {
	
	/**
	 * Main method
	 * @param args		command-line arguments (unused)
	 */
	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				// Setup the application's window
				JFrame frame = new JFrame("TablecellBalloonTip example");
				frame.setIconImage(new ImageIcon(TableExample.class.getResource("/net/java/balloontip/images/frameicon.png")).getImage());
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				
				// Setup the content pane
				JPanel contentPane = new JPanel();
				contentPane.setLayout(new GridBagLayout());
				frame.setContentPane(contentPane);
				
				// Create a table
				final JTable table = new JTable(32,32);
				table.getModel().setValueAt("A cell", 16, 16);
				table.setTableHeader(null); // Remove the table header
				table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
				TableColumn column = null;
				for (int i=0; i<32; ++i) {
					column = table.getColumnModel().getColumn(i);
				    column.setPreferredWidth(100);
				}
				final JScrollPane tableScrollPane = new JScrollPane(table);
				tableScrollPane.getHorizontalScrollBar().setUnitIncrement(3);
				contentPane.add(tableScrollPane, new GridBagConstraints(0,0,1,1,1.0,1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0,0,0,0), 0, 0));		
				table.scrollRectToVisible(table.getCellRect(8, 16, true));
				
				// Create the TablecellBalloonTip
				new TableCellBalloonTip(table, 
						new JLabel("A TablecellBalloonTip"), 
						16, 16,
						new MinimalBalloonStyle(new Color(255, 0, 0, 180), 10),
						BalloonTip.Orientation.LEFT_ABOVE, 
						BalloonTip.AttachLocation.ALIGNED, 
						40, 20, 
						false);
				
				// Display the window
				frame.pack();
				frame.setSize(640, 260);
				frame.setLocationRelativeTo(null);
				frame.setVisible(true);
			}
		});
	}
}
