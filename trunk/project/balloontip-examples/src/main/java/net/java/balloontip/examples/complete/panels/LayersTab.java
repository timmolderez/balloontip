/*
 * Copyright (c) 1995 - 2008 Sun Microsystems, Inc.  All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Sun Microsystems nor the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package net.java.balloontip.examples.complete.panels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

import net.java.balloontip.BalloonTip;
import net.java.balloontip.styles.EdgedBalloonStyle;

/**
 * Class taken and adapted from the Sun tutorial "How to Use Layered Panes".
 * Source: http://java.sun.com/docs/books/tutorial/uiswing/components/layeredpane.html
 */
public class LayersTab extends JPanel {
	private String[] layerStrings = { "Yellow (0)", "Magenta (1)",	"Cyan (2)", "Red (3)", "Green (4)" };
	private Color[] layerColors = { Color.yellow, Color.magenta, Color.cyan, Color.red, Color.green };

	private JLayeredPane layeredPane;
	private JLabel dukeLabel;
	private JComboBox layerList;
	private BalloonTip balloonTip;

	// Adjustments to the balloon's relative position to Duke
	private int xFudge = 40;
	private int yFudge = 57;

	public LayersTab()    {
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		setOpaque(true); // Content panes must be opaque

		// Create and load the duke icon.
		final ImageIcon icon = new ImageIcon(LayersTab.class.getResource("/net/java/balloontip/images/dukeWaveRed.gif"));

		// Create and set up the layered pane.
		layeredPane = new JLayeredPane();
		layeredPane.setPreferredSize(new Dimension(300, 400));
		layeredPane.setBorder(BorderFactory.createTitledBorder("Move the mouse to make Duke follow:"));
		
		// Make Duke follow the cursor
		
		layeredPane.addMouseMotionListener(new MouseMotionAdapter() {
			final int paddingTop = 50;
			public void mouseMoved(MouseEvent e) {
				if (e.getY() > paddingTop) {
					dukeLabel.setLocation(e.getX()-xFudge/2, e.getY()-yFudge/2);
				}
			}
		});

		// This is the origin of the first label added.
		Point origin = new Point(100, 80);

		// This is the offset for computing the origin for the next label.
		int offset = 35;

		// Add several overlapping, colored labels to the layered pane using absolute positioning/sizing.
		for (int i = 0; i < layerStrings.length; i++) {
			JLabel label = createColoredLabel(layerStrings[i],
					layerColors[i], origin);
			layeredPane.add(label, new Integer(i));
			origin.x += offset;
			origin.y += offset;
		}

		// Create and add the Duke label to the layered pane.
		dukeLabel = new JLabel(icon);
		dukeLabel.setBounds(40, 225,
				icon.getIconWidth(),
				icon.getIconHeight());

		layeredPane.add(dukeLabel, new Integer(2), 0);
		balloonTip = new BalloonTip(dukeLabel, new JLabel("Ready for action!"),
				new EdgedBalloonStyle(Color.WHITE, Color.BLUE),
				BalloonTip.Orientation.LEFT_ABOVE,
				BalloonTip.AttachLocation.ALIGNED,
				15, 15,
				false);
		balloonTip.setTopLevelContainer(layeredPane);
		balloonTip.setPadding(4);
		layeredPane.setLayer(balloonTip, 2, 0);

		// Add control pane and layered pane to this JPanel.
		add(createControlPanel());
		add(layeredPane);
	}

	// Create and set up a colored label.
	private JLabel createColoredLabel(String text,
			Color color,
			Point origin) {
		JLabel label = new JLabel(text);
		label.setVerticalAlignment(JLabel.TOP);
		label.setHorizontalAlignment(JLabel.CENTER);
		label.setOpaque(true);
		label.setBackground(color);
		label.setForeground(Color.black);
		label.setBorder(BorderFactory.createLineBorder(Color.black));
		label.setBounds(origin.x, origin.y, 140, 140);
		return label;
	}

	// Create the control pane for the top of the frame.
	private JPanel createControlPanel() {

		layerList = new JComboBox(layerStrings);
		layerList.setSelectedIndex(2); //cyan layer
		// Behaviour of the layer selection list
		layerList.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				layeredPane.setLayer(dukeLabel,
						layerList.getSelectedIndex(),
						0);
				layeredPane.setLayer(balloonTip,
						layerList.getSelectedIndex(),
						0);
			}});

		JPanel controls = new JPanel();
		controls.setLayout(new GridBagLayout());
		controls.add(new JLabel("<html>The pane on which a balloon tip is drawn can be set manually to, for instance, a JLayeredPane. In this example, you can make a balloon tip switch between different layers of such a JLayeredPane.</html>"), new GridBagConstraints(0,0,3,1,1.0,0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10,10,10,0), 0, 0));
		
		controls.add(new JLabel("Choose Duke's layer:"), new GridBagConstraints(0,1,1,1,0.0,1.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10,10,10,0), 0, 0));
		layerList.setPreferredSize(new Dimension(100,25));
		controls.add(layerList, new GridBagConstraints(1,1,1,1,0.0,1.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10,10,10,0), 0, 0));
		
		controls.setBorder(BorderFactory.createTitledBorder(
		"Setting a layer:"));
		return controls;
	}
}
