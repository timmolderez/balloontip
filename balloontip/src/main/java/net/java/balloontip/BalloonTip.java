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

package net.java.balloontip;

import java.awt.Color;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.RootPaneContainer;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.java.balloontip.positioners.BalloonTipPositioner;
import net.java.balloontip.positioners.BasicBalloonTipPositioner;
import net.java.balloontip.positioners.Left_Above_Positioner;
import net.java.balloontip.positioners.Left_Below_Positioner;
import net.java.balloontip.positioners.Right_Above_Positioner;
import net.java.balloontip.positioners.Right_Below_Positioner;
import net.java.balloontip.styles.BalloonTipStyle;
import net.java.balloontip.styles.RoundedBalloonStyle;

/**
 * A balloon tip which can be attached to about any JComponent
 * @author Bernhard Pauler
 */
public class BalloonTip extends JPanel {
	/**
	 * Should the balloon be placed above, below, right or left of the attached component?
	 */
	public enum Orientation {LEFT_ABOVE, RIGHT_ABOVE, LEFT_BELOW, RIGHT_BELOW}
	/**
	 * Where should the balloon's tip be located, relative to the attached component
	 * ALIGNED makes sure the balloon's edge is aligned with the attached component
	 */
	public enum AttachLocation {ALIGNED, CENTER, NORTH, NORTHEAST, EAST, SOUTHEAST, SOUTH, SOUTHWEST, WEST, NORTHWEST}

	private static Icon defaultIcon  = new ImageIcon(BalloonTip.class.getResource("/net/java/balloontip/images/close_default.png"));
	private static Icon rolloverIcon = new ImageIcon(BalloonTip.class.getResource("/net/java/balloontip/images/close_rollover.png"));
	private static Icon pressedIcon  = new ImageIcon(BalloonTip.class.getResource("/net/java/balloontip/images/close_pressed.png"));

	protected JLabel label = new JLabel();
	protected JButton closeButton = null;
	protected boolean isVisible = true;
	protected boolean clickToClose = false;
	protected boolean clickToHide = false;
	protected boolean visibilityControl = false;
	// Remember if balloon tip was actually hidden through mouse click
	protected boolean wasClickedToHide = false;

	private final ComponentAdapter attachedComponentListener = new ComponentAdapter() {
		public void componentMoved(ComponentEvent e) {refreshLocation();}
		public void componentResized(ComponentEvent e) {checkVisibility(); /* New size could be zero, so better check visibility */}
		public void componentShown(ComponentEvent e) {checkVisibility();}
		public void componentHidden(ComponentEvent e) {checkVisibility();}
	};
	private final ComponentAdapter topLevelContainerListener = new ComponentAdapter() {
		public void componentResized(ComponentEvent e) {
			refreshLocation();
		}
	};
	private final PropertyChangeListener visibilityListener = new PropertyChangeListener() {
		public void propertyChange(PropertyChangeEvent e) {
			Boolean visible = (Boolean) e.getNewValue();
			if (visibilityControl && visible) {
				checkVisibility();
			}
		}
	};
	private final MouseAdapter clickListener = new MouseAdapter() {
		public void mouseClicked(MouseEvent e) {e.consume();}
		public void mouseReleased(MouseEvent e) {
			if (clickToHide) {
				wasClickedToHide = true;
				setVisible(false);
			} else if (clickToClose) {
				closeBalloon();
			}
		}
	};
	private AncestorListener attachedComponentParentListener = null;
	private ArrayList<JTabbedPane> tabbedPaneParents = new ArrayList<JTabbedPane>();
	private final ChangeListener tabbedPaneListener = new ChangeListener() {
		public void stateChanged(ChangeEvent e) {
			checkVisibility();
		}
	};
	private ActionListener closeButtonActionListener = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			closeBalloon();
		}
	};

	protected BalloonTipStyle style;			// Determines the balloon's looks
	protected BalloonTipPositioner positioner;	// Determines the balloon's position

	protected JLayeredPane topLevelContainer = null;	// The balloon is drawn on this pane
	protected JComponent attachedComponent;				// The balloon is attached to this component

	/**
	 * Constructor
	 * A simple constructor, the balloon tip will get a default look
	 * @param attachedComponent		Attach the balloon tip to this component
	 * @param text					The contents of the balloon tip (may contain HTML)
	 * @exception NullPointerException if attachedComponent is <code>null</code>
	 */
	public BalloonTip(JComponent attachedComponent, String text) {
		this(attachedComponent, text, new RoundedBalloonStyle(5,5,Color.WHITE, Color.BLACK), true);
	}

	/**
	 * Constructor
	 * @param attachedComponent		Attach the balloon tip to this component
	 * @param text					The contents of the balloon tip (may contain HTML)
	 * @param style					The balloon tip's looks
	 * @param useCloseButton		If true, the balloon tip gets a close button
	 * @exception NullPointerException if attachedComponent is <code>null</code>
	 */
	public BalloonTip(JComponent attachedComponent, String text, BalloonTipStyle style, boolean useCloseButton) {
		this(attachedComponent, text, style, Orientation.LEFT_ABOVE, AttachLocation.ALIGNED, 16, 20, useCloseButton);
	}

	/**
	 * Constructor
	 * @param attachedComponent		Attach the balloon tip to this component
	 * @param text					The contents of the balloon tip (may contain HTML)
	 * @param style					The balloon tip's looks
	 * @param orientation			Orientation of the balloon tip
	 * @param attachLocation		Location of the balloon's tip  within the attached component
	 * @param horizontalOffset		Horizontal offset for the balloon's tip
	 * @param verticalOffset		Vertical offset for the balloon's tip
	 * @param useCloseButton		If true, the balloon tip gets a close button
	 * @exception NullPointerException if at least one parameter (except for parameters text and style) is <code>null</code>
	 */
	public BalloonTip(JComponent attachedComponent, String text, BalloonTipStyle style, Orientation orientation, AttachLocation attachLocation,
			int horizontalOffset, int verticalOffset, boolean useCloseButton) {
		super();
		// Setup the appropriate positioner
		BasicBalloonTipPositioner positioner = null;
		float attachX = 0.0f;
		float attachY = 0.0f;
		boolean fixedAttachLocation = true;

		switch (attachLocation) {
		case ALIGNED:
			fixedAttachLocation = false;
			break;
		case CENTER:
			attachX = 0.5f;
			attachY = 0.5f;
			break;
		case NORTH:
			attachX = 0.5f;
			break;
		case NORTHEAST:
			attachX = 1.0f;
			break;
		case EAST:
			attachX = 1.0f;
			attachY = 0.5f;
			break;
		case SOUTHEAST:
			attachX = 1.0f;
			attachY = 1.0f;
			break;
		case SOUTH:
			attachX = 0.5f;
			attachY = 1.0f;
			break;
		case SOUTHWEST:
			attachY = 1.0f;
			break;
		case WEST:
			attachY = 0.5f;
			break;
		case NORTHWEST:
			break;
		}

		switch (orientation) {
		case LEFT_ABOVE:
			positioner = new Left_Above_Positioner(horizontalOffset, verticalOffset);
			break;
		case LEFT_BELOW:
			positioner = new Left_Below_Positioner(horizontalOffset, verticalOffset);
			break;
		case RIGHT_ABOVE:
			positioner = new Right_Above_Positioner(horizontalOffset, verticalOffset);
			break;
		case RIGHT_BELOW:
			positioner = new Right_Below_Positioner(horizontalOffset, verticalOffset);
			break;
		}

		positioner.enableFixedAttachLocation(fixedAttachLocation);
		positioner.setAttachLocation(attachX, attachY);

		initializePhase1(attachedComponent, text, style, positioner, useCloseButton);
	}

	/**
	 * Constructor
	 * @param attachedComponent		Attach the balloon tip to this component
	 * @param text					The contents of the balloon tip (may contain HTML)
	 * @param style					The balloon tip's looks
	 * @param positioner			Determines the way the balloon tip is positioned
	 * @param useCloseButton		If true, the balloon tip gets a close button
	 * @exception NullPointerException if attachedComponent or positioner are <code>null</code>
	 */
	public BalloonTip(JComponent attachedComponent, String text, BalloonTipStyle style, BalloonTipPositioner positioner, boolean useCloseButton) {
		super();
		initializePhase1(attachedComponent, text, style, positioner, useCloseButton);
	}

	protected void finalize() throws Throwable {
		closeBalloon(); // This will remove all of the listeners a balloon tip uses...
		super.finalize();
	}

	/**
	 * Set the text message that should appear in the balloon tip.
	 * HTML formatting is supported.
	 * @param text
	 */
	public void setText(String text) {
		label.setText(text);
		refreshLocation();
	}

	/**
	 * Get the text message that appears in the balloon tip.
	 * @return The text shown in the balloon tip
	 */
	public String getText() {
		return label.getText();
	}

	/**
	 * Set the icon that should appear at the left side of the balloon tip
	 * @param icon		The icon (If it has a null-value, the balloon will not have an icon...)
	 */
	public void setIcon(Icon icon) {
		label.setIcon(icon);
		refreshLocation();
	}

	/**
	 * Get the icon that appears at the left side of the balloon tip
	 * (Returns null if there is no such icon)
	 * @return The balloon tip's icon
	 */
	public Icon getIcon() {
		return label.getIcon();
	}

	/**
	 * Set the distance (in px) between the icon and the text label
	 * @param iconTextGap
	 */
	public void setIconTextGap(int iconTextGap) {
		label.setIconTextGap(iconTextGap);
		refreshLocation();
	}

	/**
	 * Get the distance (in px) between the icon and the text label
	 * @return The distance between the balloon tip's icon and its text
	 */
	public int getIconTextGap() {
		return label.getIconTextGap();
	}

	/**
	 * Set the balloon tip's style
	 * @param style
	 */
	public void setStyle(BalloonTipStyle style) {
		BalloonTipStyle oldStyle = this.style;
		this.style = style;
		setBorder(this.style);
		// Notify property listeners that the style has changed
		firePropertyChange("style", oldStyle, style);
		refreshLocation();
	}

	/**
	 * Get the balloon tip's style
	 * @return The balloon tip's style
	 */
	public BalloonTipStyle getStyle() {
		return style;
	}

	/**
	 * Set a new BalloonTipPositioner
	 * @param positioner
	 * @exception NullPointerException if positioner is <code>null</code>
	 */
	public void setPositioner(BalloonTipPositioner positioner) {
		// Make sure the value is not null
		if (positioner == null) {
			throw new NullPointerException();
		}
		BalloonTipPositioner oldPositioner = this.positioner;
		this.positioner = positioner;
		this.positioner.setBalloonTip(this);
		// Notify property listeners that the positioner has changed
		firePropertyChange("positioner", oldPositioner, positioner);
		refreshLocation();
	}

	/**
	 * Retrieve the BalloonTipPositioner that is used by this BalloonTip
	 * @return The balloon tip's positioner
	 */
	public BalloonTipPositioner getPositioner() {
		return positioner;
	}

	/**
	 * Set the amount of padding in this balloon tip
	 * @param padding
	 */
	public void setPadding(int padding) {
		label.setBorder(new EmptyBorder(padding, padding, padding, padding));
		refreshLocation();
	}

	/**
	 * Get the amount of padding in this balloon tip
	 * @return The amount of padding in the balloon tip
	 */
	public int getPadding() {
		return label.getBorder().getBorderInsets(this).left;
	}

	/**
	 * Hide the balloon tip just by clicking anywhere on it
	 * @param enabled	if true, the balloon hides when it's clicked
	 */
	public void enableClickToHide(boolean enabled) {
		clickToHide = enabled;
	}

	/**
	 * Permanently close the balloon tip just by clicking anywhere on it
	 * @param enabled	if true, the balloon permanently closes when it's clicked
	 */
	public void enableClickToClose(boolean enabled) {
		clickToClose = enabled;
	}

	/**
	 * When the balloon tip is explicitely made visible, through setVisible(true),
	 * control if the attached component is also visible (and showing on screen), otherwise hide the balloon tip.
	 * The balloon tip will be shown again as soon as attached component will become visible.
	 * @param enabled	if true, the balloon will be shown on screen only if attached component is also showing on screen
	 */
	public void enableVisibilityControl(boolean enabled) {
		visibilityControl = enabled;
		// Start with a visibility control
		if (visibilityControl && isVisible) {
			checkVisibility();
		}
	}

	/**
	 * If you want to permanently close the balloon, use this method.
	 * (If you just need to hide the balloon, use setVisible(false);)
	 * You cannot use this BalloonTip-instance anymore after calling this method!
	 */
	public void closeBalloon() {
		setVisible(false);
		if (attachedComponentParentListener != null) {
			attachedComponent.removeAncestorListener(attachedComponentParentListener);
		}
		if (topLevelContainer != null) {
			topLevelContainer.remove(this);
			topLevelContainer.removeComponentListener(topLevelContainerListener);
		}
		for (JTabbedPane p : tabbedPaneParents) {
			p.removeChangeListener(tabbedPaneListener);
		}
		tabbedPaneParents.clear();
		attachedComponent.removeComponentListener(attachedComponentListener);
		removePropertyChangeListener("visible", visibilityListener);
		removeMouseListener(clickListener);
		if (closeButton != null) {
			closeButton.removeActionListener(closeButtonActionListener);
		}
	}

	/**
	 * Set the close-button icons for all balloon tips
	 * @param normal
	 * @param pressed
	 * @param rollover		If you don't want a rollover, just set this to null...
	 */
	public static void setCloseButtonIcons(Icon normal, Icon pressed, Icon rollover) {
		defaultIcon  = normal;
		rolloverIcon = rollover;
		pressedIcon  = pressed;
	}

	/**
	 * Set the border of the balloon tip's close button.
	 * If no close button is used, nothing will happen.
	 * @param top
	 * @param left
	 * @param bottom
	 * @param right
	 */
	public void setCloseButtonBorder(int top, int left, int bottom, int right) {
		if (closeButton != null) {
			closeButton.setBorder(BorderFactory.createEmptyBorder(top, left, bottom, right));
			refreshLocation();
		}
	}

	/**
	 * Replace the close button's behaviour
	 * (The default behaviour is to permanently close the balloon...)
	 * with your own action listener
	 * @param act
	 */
	public void setCloseButtonActionListener(ActionListener act) {
		if (closeButton != null) {
			closeButton.removeActionListener(closeButtonActionListener);
			closeButtonActionListener = act;
			closeButton.addActionListener(closeButtonActionListener);
		}
	}

	/**
	 * Retrieve the component this balloon tip is attached to
	 * @return The attached component
	 */
	public JComponent getAttachedComponent() {
		return attachedComponent;
	}

	/**
	 * Set the container this balloon tip is drawn on
	 * @param topLevelContainer
	 * @exception NullPointerException if topLevelContainer is <code>null</code>
	 */
	public void setTopLevelContainer(JLayeredPane topLevelContainer) {
		// Make sure the value is not null
		if (topLevelContainer == null) {
			throw new NullPointerException();
		}
		if (this.topLevelContainer != null) {
			this.topLevelContainer.remove(this);
			this.topLevelContainer.removeComponentListener(topLevelContainerListener);
		}
		this.topLevelContainer = topLevelContainer;
		// If the window is resized, we should check if the balloon still fits
		this.topLevelContainer.addComponentListener(topLevelContainerListener);
		// We use the popup layer of the top level container (frame or dialog) to show the balloon tip
		this.topLevelContainer.add(this, JLayeredPane.POPUP_LAYER);
	}

	/**
	 * Retrieve the container this balloon tip is drawn on
	 * If the balloon tip hasn't determined this container yet, null is returned
	 * @return The balloon tip's top level container
	 */
	public JLayeredPane getTopLevelContainer() {
		return topLevelContainer;
	}

	/**
	 * Redetermines and sets the balloon tip's location.
	 * Must be able to update balloon tip's location
	 * even if the balloon tip or the attached component
	 * are not showing on screen.
	 */
	public void refreshLocation() {
		Point location = SwingUtilities.convertPoint(attachedComponent, getLocation(), this);
		try {
			positioner.determineAndSetLocation(new Rectangle(location.x, location.y, attachedComponent.getWidth(), attachedComponent.getHeight()));
		} catch (NullPointerException exc) {}
	}

	public void setVisible(boolean visible) {
		boolean wasVisible = isVisible;
		isVisible = visible;
		if (visible) {
			wasClickedToHide = false;
		}
		super.setVisible(visible);
		// Notify property listeners that the visibility has changed
		firePropertyChange("visible", wasVisible, visible);
	}

	/**
	 * Gets a value indicating whether or not the attached component is
	 * showing on screen AND its area is greater than zero.
	 * @return <code>true</code> if the <code>attached component</code> is
	 *         showing on screen and if its area is greater than zero. Otherwise
	 *         <code>false</code>
	 */
	protected boolean isAttachedComponentReallyShowing() {
		return attachedComponent.isShowing()
		&& attachedComponent.getWidth() > 0
		&& attachedComponent.getHeight() > 0 /* To be seen, the area of the attached component must be > 0 */;
	}

	/**
	 * Shows the balloon if the attached component is visible; hides the balloon if the attached component is invisible...
	 */
	protected void checkVisibility() {
		// If we can see the attached component, the balloon tip is not closed AND we want it to be visible, then show it...
		if (isAttachedComponentReallyShowing() && isVisible) {
			super.setVisible(true);
			refreshLocation();
		} else {
			super.setVisible(false);
		}
	}

	/*
	 * Helper method for constructing a BalloonTip
	 */
	private void initializePhase1(final JComponent attachedComponent, String text, BalloonTipStyle style, BalloonTipPositioner positioner, boolean useCloseButton) {
		this.attachedComponent = attachedComponent;
		this.style = style;
		this.positioner = positioner;

		setBorder(this.style);
		setOpaque(false);
		setLayout(new GridBagLayout());

		label.setBorder(new EmptyBorder(5, 5, 5, 5));
		label.setText(text);
		add(label, new GridBagConstraints(0, 0, 1, 1, 1, 1, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

		if (useCloseButton) {
			closeButton = new JButton();
			closeButton.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 5));
			closeButton.setContentAreaFilled(false);
			closeButton.setIcon(defaultIcon);
			closeButton.setRolloverIcon(rolloverIcon);
			closeButton.setPressedIcon(pressedIcon);
			closeButton.addActionListener(closeButtonActionListener);
			add(closeButton, new GridBagConstraints(1, 0, 1, 1, 0, 0, GridBagConstraints.NORTHEAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		}

		// Attempt to run initializePhase2() ...
		try {
			initializePhase2();
		} catch (NullPointerException exc) {
			/* If we failed to determine the top level container, it's because attachedComponent.getParent() returned null...
			 * We'll just have to wait until the parent is set and try again...
			 */
			attachedComponentParentListener = new AncestorListener() {
				public void ancestorAdded(AncestorEvent event) {
					initializePhase2();
					// Remove yourself
					attachedComponent.removeAncestorListener(attachedComponentParentListener);
					attachedComponentParentListener = null;
					repaint();
				}
				public void ancestorMoved(AncestorEvent event) {}
				public void ancestorRemoved(AncestorEvent event) {}
			};
			attachedComponent.addAncestorListener(attachedComponentParentListener);
		}
	}

	/*
	 * Helper method for constructing a BalloonTip; when this method finishes, the balloon tip is ready for use
	 *
	 * The main task here is to attempt to determine the top level container, which is where the balloon tip is drawn.
	 * (This is done by following the path of parent Components, starting at the attached component...)
	 */
	private void initializePhase2() {
		JLayeredPane newTopLevelContainer = null;
		ArrayList<JTabbedPane> newTabbedPaneParents = new ArrayList<JTabbedPane>();

		Container parent = attachedComponent.getParent();
		// Follow the path of parents of the attached component until you find the top level container
		while (true) {
			// If you're a top level container (JFrame, JDialog, JInternalFrame, JApplet or JWindow)
			if (parent instanceof RootPaneContainer) {
				newTopLevelContainer = ((RootPaneContainer)parent).getLayeredPane();
				// Exit the infinite loop
				break;
				// If you're a tab
			} else if (parent instanceof JTabbedPane) {
				/* Due to a bug in JTabbedPane, switching tabs does not cause component events
				 * that tell which components are now visible / invisible.
				 * This piece of code is a workaround. We'll check our attached component's visibility by listening to the JTabbedPane...
				 */
				newTabbedPaneParents.add((JTabbedPane)parent);
			}
			parent = parent.getParent();
		}

		// If user previously set the top level container, through setTopLevelContainer(),
		// don't overwrite it ...
		boolean topLevelAlreadySet = (topLevelContainer != null);
		if (topLevelAlreadySet) {
			newTopLevelContainer = topLevelContainer;
		} else {
			// ... and don't re-add topLevelContainerListener !
			// If the window is resized, we should check if the balloon still fits
			newTopLevelContainer.addComponentListener(topLevelContainerListener);
		}

		// At this point, it's sure there's a top level container,
		// otherwise a NullPointerException would have been thrown.
		topLevelContainer = newTopLevelContainer;
		tabbedPaneParents = newTabbedPaneParents;
		for (JTabbedPane p : tabbedPaneParents) {
			p.addChangeListener(tabbedPaneListener);
		}

		// We use the popup layer of the top level container (frame or dialog) to show the balloon tip.
		// NB : if user previously set the top level container, through setTopLevelContainer(),
		// don't re-add the balloon tip !
		if (!topLevelAlreadySet) {
			topLevelContainer.add(this, JLayeredPane.POPUP_LAYER);
		}
		// If the attached component is moved/hidden/shown, the balloon tip should act accordingly
		attachedComponent.addComponentListener(attachedComponentListener);
		// If the balloon tip is explicitely made visible, through setVisible(true),
		// check if the attached component is also visible, otherwise hide the balloon tip
		addPropertyChangeListener("visible", visibilityListener);
		// Don't allow to click 'through' the component; will also enable to close the balloon when it's clicked
		addMouseListener(clickListener);
		// Finally pass the balloon tip to its positioner
		positioner.setBalloonTip(this);

		refreshLocation();
	}
}