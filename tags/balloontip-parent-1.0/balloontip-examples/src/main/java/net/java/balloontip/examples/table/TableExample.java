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

package net.java.balloontip.examples.table;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableColumn;

import net.java.balloontip.BalloonTip;
import net.java.balloontip.TablecellBalloonTip;
import net.java.balloontip.styles.MinimalBalloonStyle;

/**
 * Simple application demonstrating a TablecellBalloonTip
 * @author Tim Molderez
 */
public class TableExample {
	private static TablecellBalloonTip tableBalloon = null;
	
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
				
				// Create the TableCellBalloonTip
				tableBalloon = new TablecellBalloonTip(table, "A TablecellBalloonTip", 16, 16,
						new MinimalBalloonStyle(new Color(255, 0, 0, 180), 10),
						BalloonTip.Orientation.LEFT_ABOVE, 
						BalloonTip.AttachLocation.ALIGNED, 
						40, 20, 
						false);
				tableBalloon.setViewport(tableScrollPane.getViewport());
				
				frame.pack();
				frame.setSize(640, 260);
				frame.setLocationRelativeTo(null);
				frame.setVisible(true);
			}
		});
	}
}
