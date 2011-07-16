/**
 * Copyright (c) 2011 Bernhard Pauler, Tim Molderez.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the 3-Clause BSD License
 * which accompanies this distribution, and is available at
 * http://www.opensource.org/licenses/BSD-3-Clause
 */

package net.java.balloontip.examples.list;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import net.java.balloontip.BalloonTip.AttachLocation;
import net.java.balloontip.BalloonTip.Orientation;
import net.java.balloontip.ListItemBalloonTip;
import net.java.balloontip.examples.table.TableExample;
import net.java.balloontip.styles.EdgedBalloonStyle;

/**
 * An example demonstrating a ListItemBalloonTip
 * @author Tim Molderez
 */
public class ListExample {

    /**
	 * Main method
	 * @param args		command-line arguments (unused)
	 */
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
            	// Create and set up the window
                JFrame frame = new JFrame("ListitemBalloonTip");
                frame.setIconImage(new ImageIcon(TableExample.class.getResource("/net/java/balloontip/images/frameicon.png")).getImage());
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                // Setup the content pane
				JPanel contentPane = new JPanel();
				contentPane.setLayout(new BorderLayout());
				frame.setContentPane(contentPane);
				
		        // Create a list
				DefaultListModel listModel = new DefaultListModel();
		        for (int i = 0; i < 20; i++) {
					listModel.addElement("Item " + i);
				}

		        JList list = new JList(listModel);
		        list.setSelectedIndex(0);
		        list.setVisibleRowCount(10);
		        JScrollPane listScrollPane = new JScrollPane(list);
		        listScrollPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		        contentPane.add(listScrollPane, BorderLayout.CENTER);
		        
		        // Create a ListItemBalloonTip
		        new ListItemBalloonTip(list, 
		        		new JLabel("Testing"),8, 
		        		new EdgedBalloonStyle(Color.WHITE,Color.BLACK),
		        		Orientation.LEFT_ABOVE,AttachLocation.ALIGNED,10,10,false);

                //Display the window
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
    }
}
