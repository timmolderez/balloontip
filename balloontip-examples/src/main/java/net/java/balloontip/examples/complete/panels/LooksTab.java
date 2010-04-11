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
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
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
import net.java.balloontip.examples.complete.Complete;
import net.java.balloontip.examples.complete.CompleteExample;
import net.java.balloontip.positioners.BasicBalloonTipPositioner;
import net.java.balloontip.styles.EdgedBalloonStyle;
import net.java.balloontip.styles.MinimalBalloonStyle;
import net.java.balloontip.styles.ModernBalloonStyle;
import net.java.balloontip.styles.RoundedBalloonStyle;
import net.java.balloontip.styles.TexturedBalloonStyle;

public class LooksTab extends JPanel {
	private final BalloonTip balloonTip;
	private final JComboBox stylePicker;
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
		final JTextField contents = new JTextField("<html>I'm a <u>balloon tip</u></html>");
		CompleteExample.setToolTip(contents, "The contents of a balloon tip may contain HTML formatting");
		contents.setPreferredSize(new Dimension(250,25));
		add(contents, new GridBagConstraints(1,gridY,1,1,0.0,0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2,10,2,0), 0, 0));
		++gridY;

		// Styles combobox
		add(new JLabel("Style:"), new GridBagConstraints(0,gridY,1,1,0.0,0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0,10,0,0), 0, 0));
		String[] stylesOptions = {"Edged", "Rounded", "Modern", "Minimal", "Textured"};
		stylePicker = new JComboBox(stylesOptions);
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

		// Show icon checkbox
		add(new JLabel("Show an icon:"), new GridBagConstraints(0,gridY,1,1,0.0,0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0,10,0,0), 0, 0));
		final JCheckBox showIcon = new JCheckBox();
		showIcon.setSelected(false);
		add(showIcon, new GridBagConstraints(1,gridY,1,1,0.0,0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2,10,2,0), 0, 0));
		++gridY;

		// Text to icon gap textbox
		add(new JLabel("Icon-text gap:"), new GridBagConstraints(0,gridY,1,1,0.0,0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0,10,0,0), 0, 0));
		final JTextField iconTextGap = new JTextField("10");
		iconTextGap.setPreferredSize(new Dimension(30,25));
		add(iconTextGap, new GridBagConstraints(1,gridY,1,1,0.0,0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2,10,2,0), 0, 0));
		++gridY;

		// Close button border textbox
		add(new JLabel("Close-button border:"), new GridBagConstraints(0,gridY,1,1,0.0,0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0,10,0,0), 0, 0));
		final JTextField closeButtonBorder = new JTextField("5");
		closeButtonBorder.setPreferredSize(new Dimension(30,25));
		add(closeButtonBorder, new GridBagConstraints(1,gridY,1,1,0.0,0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2,10,2,0), 0, 0));
		++gridY;

		// Balloon tip
		final JButton attachedComponent = new JButton("Show balloon tip");
		add(attachedComponent, new GridBagConstraints(0,gridY,2,1,1.0,1.0, GridBagConstraints.SOUTH, GridBagConstraints.NONE, new Insets(50,0,25,0), 0, 0));

		/*
		 * Add the GUI's behaviour
		 */

		// Create the balloon tip
		balloonTip = new BalloonTip(attachedComponent, contents.getText(),
				new EdgedBalloonStyle(Color.WHITE, Color.BLUE), 
				BalloonTip.Orientation.LEFT_ABOVE, 
				BalloonTip.AttachLocation.ALIGNED, 
				20, 10, 
				true,
				Complete.isDrawnOutsideParent());

		// Don't close the balloon when clicking the close-button, you just need to hide it
		balloonTip.setCloseButtonActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				balloonTip.setVisible(false);
			}
		});
		balloonTip.setIconTextGap(Integer.parseInt(iconTextGap.getText()));
		int value = Integer.parseInt(closeButtonBorder.getText());
		balloonTip.setCloseButtonBorder(value, value, value, value);

		// (Re)show the balloon tip when clicking attachedComponent 
		attachedComponent.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				balloonTip.setVisible(true);
			}
		});

		// Contents
		contents.getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent e) {
				balloonTip.setText(contents.getText());
			}
			public void insertUpdate(DocumentEvent e) {
				balloonTip.setText(contents.getText());
			}
			public void removeUpdate(DocumentEvent e) {
				balloonTip.setText(contents.getText());
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
				fillColor = JColorChooser.showDialog(null, "Choose a fill color", fillColor);
				setBalloonTipStyle();
				fillColorButton.setBackground(fillColor);
			}
		});

		// Border color
		borderColorButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				borderColor = JColorChooser.showDialog(null, "Choose a border color", borderColor);
				setBalloonTipStyle();
				borderColorButton.setBackground(borderColor);
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
					balloonTip.revalidate();
					balloonTip.refreshLocation();
				} catch (Exception e) {
					if (!hOffset.getText().equals("")) {
						CompleteExample.showErrorMessage(hOffset, "Please enter a positive horizontal offset");
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
					balloonTip.revalidate();
					balloonTip.refreshLocation();
				} catch (Exception e) {
					if (!vOffset.getText().equals("")) {
						CompleteExample.showErrorMessage(vOffset, "Please enter a positive vertical offset");
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
						CompleteExample.showErrorMessage(padding, "Please enter a positive padding value");
					}
				}
			}
		});

		// Show icon
		showIcon.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (showIcon.isSelected()) {
					balloonTip.setIcon(new ImageIcon(CompleteExample.class.getResource("/net/java/balloontip/images/frameicon.png")));
				} else {
					balloonTip.setIcon(null);
				}
			}
		});

		// Icon-text gap
		iconTextGap.getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent e) {
				setIconTextGap();
			}
			public void insertUpdate(DocumentEvent e) {
				setIconTextGap();
			}
			public void removeUpdate(DocumentEvent e) {
				setIconTextGap();
			}

			private void setIconTextGap() {
				try {
					balloonTip.setIconTextGap(Integer.parseInt(iconTextGap.getText()));
				} catch (Exception e) {
					if (!iconTextGap.getText().equals("")) {
						CompleteExample.showErrorMessage(iconTextGap, "Please enter a positive icon-text gap value");
					}
				}
			}
		});

		// Close-button border
		closeButtonBorder.getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent e) {
				setCloseButtonBorder();
			}
			public void insertUpdate(DocumentEvent e) {
				setCloseButtonBorder();
			}
			public void removeUpdate(DocumentEvent e) {
				setCloseButtonBorder();
			}

			private void setCloseButtonBorder() {
				try {
					int value = Integer.parseInt(closeButtonBorder.getText());
					balloonTip.setCloseButtonBorder(value, value, value, value);
				} catch (Exception e) {
					if (!closeButtonBorder.getText().equals("")) {
						CompleteExample.showErrorMessage(closeButtonBorder, "Please enter a positive close-button border width");
					}
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
			balloonTip.setStyle(new RoundedBalloonStyle(5, 5, fillColor, borderColor));
			break;
		case 2:
			ModernBalloonStyle style = new ModernBalloonStyle(10, 10, fillColor, new Color(230,230,230), borderColor);
			style.setBorderThickness(3);
			style.enableAntiAliasing(true);
			balloonTip.setStyle(style);
			break;
		case 3:
			Color transparentFill = new Color(fillColor.getRed(), fillColor.getGreen(), fillColor.getBlue(), 180);
			balloonTip.setStyle(new MinimalBalloonStyle(transparentFill, 8));
			break;
		case 4:
			balloonTip.setStyle(new TexturedBalloonStyle(5, 5, new ImageIcon(CompleteExample.class.getResource("/net/java/balloontip/images/bgpattern.png")).getImage(), borderColor));
			break;
		}
	}
}
