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
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import net.java.balloontip.BalloonTip;
import net.java.balloontip.examples.complete.CompleteExample;
import net.java.balloontip.examples.complete.Utils;
import net.java.balloontip.positioners.BasicBalloonTipPositioner;
import net.java.balloontip.styles.EdgedBalloonStyle;
import net.java.balloontip.styles.IsometricBalloonStyle;
import net.java.balloontip.styles.MinimalBalloonStyle;
import net.java.balloontip.styles.ModernBalloonStyle;
import net.java.balloontip.styles.RoundedBalloonStyle;
import net.java.balloontip.styles.TexturedBalloonStyle;
import net.java.balloontip.styles.ToolTipBalloonStyle;

/**
 * Looks tab of the demo application; lets you experiment with a balloon tip's looks
 * @author Tim Molderez
 */
public class LooksTab extends JPanel {
	private final BalloonTip balloonTip;
	private final JComboBox<?> stylePicker;
	private final JButton fillColorButton;
	private final JButton borderColorButton;
	private Color fillColor = Color.WHITE;
	private Color borderColor = Color.BLUE;

	/**
	 * Default constructor
	 */
	public LooksTab() {
		super();
		setLayout(new GridBagLayout());
		int gridY = 0;

		/*
		 * Draw the GUI
		 */

		// Description label
		add(new JLabel("Toy around with these settings to change the balloon tip's looks."), new GridBagConstraints(0,gridY,2,1,1.0,0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10,10,25,0), 0, 0));
		++gridY;

		// Contents textbox
		add(new JLabel("Contents:"), new GridBagConstraints(0,gridY,1,1,0.0,0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0,10,0,0), 0, 0));
		final JTextField contents = new JTextField("<html>I'm a <u>balloon tip</u>!</html>");
		Utils.setToolTip(contents, "The contents of a balloon tip may contain HTML formatting");
		contents.setPreferredSize(new Dimension(250,25));
		add(contents, new GridBagConstraints(1,gridY,1,1,0.0,0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2,10,2,0), 0, 0));
		++gridY;

		// Styles combobox
		add(new JLabel("Style:"), new GridBagConstraints(0,gridY,1,1,0.0,0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0,10,0,0), 0, 0));
		String[] stylesOptions = {"Edged", "Isometric", "Minimal", "Modern", "Rounded", "Textured", "Tooltip"};
		stylePicker = new JComboBox<Object>(stylesOptions);
		add(stylePicker, new GridBagConstraints(1,gridY,1,1,0.0,0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2,10,2,0), 0, 0));
		++gridY;

		// Fill color
		add(new JLabel("Fill color:"), new GridBagConstraints(0,gridY,1,1,0.0,0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2,10,2,0), 0, 0));
		fillColorButton = new JButton();
		fillColorButton.setBackground(fillColor);
		fillColorButton.setPreferredSize(new Dimension(35,25));
		add(fillColorButton, new GridBagConstraints(1,gridY,1,1,0.0,0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2,10,2,0), 0, 0));
		++gridY;

		// Border color
		add(new JLabel("Border color:"), new GridBagConstraints(0,gridY,1,1,0.0,0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0,10,0,0), 0, 0));
		borderColorButton = new JButton();
		borderColorButton.setBackground(borderColor);
		borderColorButton.setPreferredSize(new Dimension(35,25));
		add(borderColorButton, new GridBagConstraints(1,gridY,1,1,0.0,0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2,10,2,0), 0, 0));
		++gridY;

		// Horizontal offset textbox
		add(new JLabel("Horizontal offset:"), new GridBagConstraints(0,gridY,1,1,0.0,0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0,10,0,0), 0, 0));
		final JTextField hOffset = new JTextField("20");
		hOffset.setPreferredSize(new Dimension(30,25));
		add(hOffset, new GridBagConstraints(1,gridY,1,1,0.0,0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2,10,2,0), 0, 0));
		++gridY;

		// Vertical offset textbox
		add(new JLabel("Vertical offset:"), new GridBagConstraints(0,gridY,1,1,0.0,0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0,10,0,0), 0, 0));
		final JTextField vOffset = new JTextField("10");
		vOffset.setPreferredSize(new Dimension(30,25));
		add(vOffset, new GridBagConstraints(1,gridY,1,1,0.0,0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2,10,2,0), 0, 0));
		++gridY;

		// Padding textbox
		add(new JLabel("Padding:"), new GridBagConstraints(0,gridY,1,1,0.0,0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0,10,0,0), 0, 0));
		final JTextField padding = new JTextField("5");
		padding.setPreferredSize(new Dimension(30,25));
		add(padding, new GridBagConstraints(1,gridY,1,1,0.0,0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2,10,2,0), 0, 0));
		++gridY;
		
		// Close button checkbox
		add(new JLabel("Close button:"), new GridBagConstraints(0,gridY,1,1,0.0,0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0,10,0,0), 0, 0));
		final JCheckBox useCloseButton = new JCheckBox("", true);
		add(useCloseButton, new GridBagConstraints(1,gridY,1,1,0.0,0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2,10,2,0), 0, 0));
		++gridY;

		// Balloon tip
		final JButton attachedComponent = new JButton("Show balloon tip");
		add(attachedComponent, new GridBagConstraints(0,gridY,2,1,1.0,1.0, GridBagConstraints.SOUTH, GridBagConstraints.NONE, new Insets(25,0,120,0), 0, 0));

		/*
		 * Add the GUI's behaviour
		 */

		// Create the balloon tip
		balloonTip = new BalloonTip(attachedComponent, new JLabel(contents.getText()),
				new EdgedBalloonStyle(fillColor, borderColor), 
				BalloonTip.Orientation.LEFT_ABOVE, 
				BalloonTip.AttachLocation.ALIGNED, 
				20, 10, 
				true);
		balloonTip.setPadding(5);

		// Don't close the balloon when clicking the close-button, you just need to hide it
		balloonTip.setCloseButton(BalloonTip.getDefaultCloseButton(),false, false);

		// (Re)show the balloon tip when clicking attachedComponent 
		attachedComponent.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				balloonTip.setVisible(true);
			}
		});

		// Contents
		contents.getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent e) {
				balloonTip.setContents(new JLabel(contents.getText()));
			}
			public void insertUpdate(DocumentEvent e) {
				balloonTip.setContents(new JLabel(contents.getText()));
			}
			public void removeUpdate(DocumentEvent e) {
				balloonTip.setContents(new JLabel(contents.getText()));
			}
		});

		// Style
		stylePicker.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setBalloonTipStyle();
			}
		});

		// Fill color
		fillColorButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Color newFillColor = JColorChooser.showDialog(null, "Choose a fill color", fillColor);
				if (newFillColor != null) {
					fillColor = newFillColor;
					setBalloonTipStyle();
					fillColorButton.setBackground(fillColor);
				}
			}
		});

		// Border color
		borderColorButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Color newBorderColor = JColorChooser.showDialog(null, "Choose a border color", borderColor);
				if (newBorderColor != null) {
					borderColor = newBorderColor;
					setBalloonTipStyle();
					borderColorButton.setBackground(borderColor);
				}
			}
		});

		// Horizontal offset
		hOffset.getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent e) {
				setHorizontalOffset();
			}
			public void insertUpdate(DocumentEvent e) {
				setHorizontalOffset();
			}
			public void removeUpdate(DocumentEvent e) {
				setHorizontalOffset();
			}

			private void setHorizontalOffset() {
				try {
					((BasicBalloonTipPositioner)balloonTip.getPositioner()).setPreferredHorizontalOffset(Integer.parseInt(hOffset.getText()));
					balloonTip.refreshLocation();
				} catch (Exception e) {
					if (!hOffset.getText().equals("")) {
						Utils.showErrorMessage(hOffset, "Please enter a positive horizontal offset");
					}
				}
			}
		});

		// Vertical offset
		vOffset.getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent e) {
				setVerticalOffset();
			}
			public void insertUpdate(DocumentEvent e) {
				setVerticalOffset();
			}
			public void removeUpdate(DocumentEvent e) {
				setVerticalOffset();
			}

			private void setVerticalOffset() {
				try {
					((BasicBalloonTipPositioner)balloonTip.getPositioner()).setPreferredVerticalOffset(Integer.parseInt(vOffset.getText()));
					balloonTip.refreshLocation();
				} catch (Exception e) {
					if (!vOffset.getText().equals("")) {
						Utils.showErrorMessage(vOffset, "Please enter a positive vertical offset");
					}
				}
			}
		});

		// Padding
		padding.getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent e) {
				setPadding();
			}
			public void insertUpdate(DocumentEvent e) {
				setPadding();
			}
			public void removeUpdate(DocumentEvent e) {
				setPadding();
			}

			private void setPadding() {
				try {
					balloonTip.setPadding(Integer.parseInt(padding.getText()));
				} catch (Exception e) {
					if (!padding.getText().equals("")) {
						Utils.showErrorMessage(padding, "Please enter a positive padding value");
					}
				}
			}
		});
		
		// Close button
		useCloseButton.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange()==ItemEvent.SELECTED) {
					balloonTip.setCloseButton(BalloonTip.getDefaultCloseButton(), false, false);
				} else {
					balloonTip.setCloseButton(null);
				}
			}
		});
	}

	private void setBalloonTipStyle() {
		switch (stylePicker.getSelectedIndex()) {
		case 0:
			balloonTip.setStyle(new EdgedBalloonStyle(fillColor, borderColor));
			break;
		case 1:
			balloonTip.setStyle(new IsometricBalloonStyle(fillColor, borderColor, 5));
			break;
		case 2:
			Color transparentFill = new Color(fillColor.getRed(), fillColor.getGreen(), fillColor.getBlue(), 180);
			balloonTip.setStyle(new MinimalBalloonStyle(transparentFill, 8));
			break;
		case 3:
			ModernBalloonStyle style = new ModernBalloonStyle(10, 10, fillColor, new Color(230,230,230), borderColor);
			style.setBorderThickness(3);
			style.enableAntiAliasing(true);
			balloonTip.setStyle(style);
			break;
		case 4:
			balloonTip.setStyle(new RoundedBalloonStyle(5, 5, fillColor, borderColor));
			break;
		case 5:
			try {
				balloonTip.setStyle(new TexturedBalloonStyle(5, 5, CompleteExample.class.getResource("/net/java/balloontip/images/bgPattern.png"), borderColor));
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		case 6:
			balloonTip.setStyle(new ToolTipBalloonStyle(fillColor, borderColor));
			break;
		}
	}
}
