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
import java.awt.event.MouseListener;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

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
 * @author Tim Molderez
 * @author Thierry Blind
 */
public class BalloonTip extends JPanel {
	/** Should the balloon be placed above, below, right or left of the attached component? */
	public enum Orientation {LEFT_ABOVE, RIGHT_ABOVE, LEFT_BELOW, RIGHT_BELOW}
	
	/** Where should the balloon's tip be located, relative to the attached component
	 * ALIGNED makes sure the balloon's edge is aligned with the attached component */
	public enum AttachLocation {ALIGNED, CENTER, NORTH, NORTHEAST, EAST, SOUTHEAST, SOUTH, SOUTHWEST, WEST, NORTHWEST}

	protected JComponent contents = null;
	protected JButton closeButton = null; 
	protected VisibilityControl visibilityControl = new VisibilityControl();
	protected BalloonTipStyle style;					// Determines the balloon's looks
	protected BalloonTipPositioner positioner;			// Determines the balloon's position
	protected JLayeredPane topLevelContainer = null;	// The balloon is drawn on this pane
	protected JComponent attachedComponent;				// The balloon is attached to this component
	
	private static Icon defaultCloseIcon  = new ImageIcon(BalloonTip.class.getResource("/net/java/balloontip/images/close_default.png"));
	private static Icon rolloverCloseIcon = new ImageIcon(BalloonTip.class.getResource("/net/java/balloontip/images/close_rollover.png"));
	private static Icon pressedCloseIcon  = new ImageIcon(BalloonTip.class.getResource("/net/java/balloontip/images/close_pressed.png"));

	// Only show a balloon tip when the component it's attached to is visible
	private final ComponentAdapter attachedComponentListener = new ComponentAdapter() {
		public void componentMoved(ComponentEvent e) {refreshLocation();}
		public void componentResized(ComponentEvent e) {
			visibilityControl.setCriteriumAndUpdate("attachedComponentShowing",
					e.getComponent().isShowing()
					&& e.getComponent().getWidth() > 0
					&& e.getComponent().getHeight() > 0);
		}
		public void componentShown(ComponentEvent e) {visibilityControl.setCriteriumAndUpdate("attachedComponentShowing",true);}
		public void componentHidden(ComponentEvent e) {visibilityControl.setCriteriumAndUpdate("attachedComponentShowing",false);}
	};
	
	// Adjust the balloon tip when the top level container (window) is adjusted
	private final ComponentAdapter topLevelContainerListener = new ComponentAdapter() {
		public void componentResized(ComponentEvent e) {
			refreshLocation();
		}
	};
	
	// Adjust the balloon tip's visibility when switching tabs
	private ComponentAdapter tabbedPaneListener = null; 
	
	// Behaviour when the balloon tip is clicked
	private MouseAdapter clickListener = null;
	
	// Manages and controls when a balloon tip should be shown or hidden
	protected class VisibilityControl {
		private HashMap<String, Boolean> criteria = new HashMap<String, Boolean>(); // A list of criteria determining a balloon tip's visibility
		
		/**
		 * Sets the value of a particular visibility criterium and checks whether the balloon tip should still be visible or not
		 * @param criterium		the visibility criterium..
		 * @param value			value of the criterium..			
		 */
		public void setCriteriumAndUpdate(String criterium, Boolean value) {
			System.out.println(criterium + ":" + value);
			criteria.put(criterium, value);
			update();
		}
		
		/**
		 * Makes sure the balloon tip's visibility is updated
		 * If any of the visibility criteria is false, the balloon tip should be invisible
		 * Only if all criteria are true, the balloon tip can be visible
		 */
		public void update() {
			Iterator<Boolean> i = criteria.values().iterator();
			while (i.hasNext()) {
				if (!i.next()) {
					forceSetVisible(false);
					return;
				}
			}
			forceSetVisible(true);
		}
	}

	/**
	 * Constructor
	 * The simplest constructor, a balloon tip with some text and a default look
	 * @param attachedComponent		attach the balloon tip to this component
	 * @param text					the contents of the balloon tip (may contain HTML)
	 */
	public BalloonTip(JComponent attachedComponent, String text) {
		this(attachedComponent, text, new RoundedBalloonStyle(5,5,Color.WHITE, Color.BLACK), true);
	}

	/**
	 * Constructor
	 * A fairly simple constructor for a balloon tip containing text
	 * @param attachedComponent		attach the balloon tip to this component
	 * @param text					the contents of the balloon tip (may contain HTML)
	 * @param style					the balloon tip's looks
	 * @param useCloseButton		if true, the balloon tip gets a default close button
	 */
	public BalloonTip(JComponent attachedComponent, String text, BalloonTipStyle style, boolean useCloseButton) {
		this(attachedComponent, new JLabel(text), style, useCloseButton);
	}
	
	/**
	 * Constructor
	 * @param attachedComponent		attach the balloon tip to this component
	 * @param component				the balloon tip's contents; this can be any JComponent
	 * @param style					the balloon tip's looks
	 * @param useCloseButton		if true, the balloon tip gets a close button
	 */
	public BalloonTip(JComponent attachedComponent, JComponent component, BalloonTipStyle style, boolean useCloseButton) {
		this(attachedComponent, component, style, Orientation.LEFT_ABOVE, AttachLocation.ALIGNED, 16, 20, useCloseButton);
	}

	/**
	 * Constructor
	 * @param attachedComponent		Attach the balloon tip to this component
	 * @param component				the balloon tip's contents; this can be any JComponent
	 * @param style					The balloon tip's looks
	 * @param orientation			Orientation of the balloon tip
	 * @param attachLocation		Location of the balloon's tip  within the attached component
	 * @param horizontalOffset		Horizontal offset for the balloon's tip
	 * @param verticalOffset		Vertical offset for the balloon's tip
	 * @param useCloseButton		If true, the balloon tip gets a close button
	 */
	public BalloonTip(final JComponent attachedComponent, final JComponent component, final BalloonTipStyle style, Orientation orientation, AttachLocation attachLocation,
			int horizontalOffset, int verticalOffset, final boolean useCloseButton) {
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

		/*Timer t = new Timer(1000, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setup(attachedComponent, component, style, new Left_Above_Positioner(5, 5), 
						useCloseButton?getDefaultCloseButton():null);
			}
		});
		t.setRepeats(false);
		t.start();*/
		setup(attachedComponent, component, style, positioner, 
				useCloseButton?getDefaultCloseButton():null);
	}

	/**
	 * Constructor
	 * The most complex, most customizable balloon tip constructor
	 * @param attachedComponent		Attach the balloon tip to this component
	 * @param text					The contents of the balloon tip (may contain HTML)
	 * @param style					The balloon tip's looks
	 * @param positioner			Determines the way the balloon tip is positioned
	 * @param useCloseButton		If true, the balloon tip gets a close button
	 */
	public BalloonTip(JComponent attachedComponent, JComponent component, BalloonTipStyle style, BalloonTipPositioner positioner, JButton closeButton) {
		super();
		setup(attachedComponent, component, style, positioner, closeButton);
	}

	protected void finalize() throws Throwable {
		closeBalloon(); // This will remove all of the listeners a balloon tip uses...
		super.finalize();
	}

	/**
	 * Sets the contents of this balloon tip.
	 * @param contents		a JComponent that represents the balloon tip's contents
	 */
	public void setContents(JComponent contents) {
		this.contents=contents;
		remove(this.contents);
		add(this.contents, new GridBagConstraints(0, 0, 1, 1, 1, 1, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		refreshLocation();
	}

	/**
	 * Retrieve this balloon tip's contents
	 * @return				the JComponent representing the contents of this balloon tip
	 */
	public JComponent getContents() {
		return this.contents;
	}
	
	/**
	 * Set the amount of padding in this balloon tip
	 * (by attaching an empty border to the balloon tip's contents...)
	 * @param padding	the amount of padding in pixels	
	 */
	public void setPadding(int padding) {
		contents.setBorder(new EmptyBorder(padding, padding, padding, padding));
		refreshLocation();
	}

	/**
	 * Get the amount of padding in this balloon tip
	 * @return			the amount of padding in pixels
	 */
	public int getPadding() {
		return contents.getBorder().getBorderInsets(this).left;
	}

	/**
	 * Set the balloon tip's style
	 * (Calling this method will fire a "style" property change event.)
	 * @param style			a BalloonTipStyle
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
	 * @return				the balloon tip's style
	 */
	public BalloonTipStyle getStyle() {
		return style;
	}

	/**
	 * Set a new BalloonTipPositioner, repsonsible for the balloon tip's positioning
	 * (Calling this method will fire a "positioner" property change event.)
	 * @param positioner	a BalloonTipPositioner
	 */
	public void setPositioner(BalloonTipPositioner positioner) {
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
	 * If you want to permanently close the balloon, you can use this method.
	 * (It will be called automatically once Java's garbage collector can clean up this balloon tip,
	 * so you don't have to call it yourself..) 
	 * Please note, you shouldn't use this instance anymore after calling this method!
	 * (If you just want to hide the balloon tip, simply use setVisible(false);)
	 */
	public void closeBalloon() {
		forceSetVisible(false);
		setCloseButton(null); // Remove the close button
		for(MouseListener m : getMouseListeners()) {
			removeMouseListener(m);
		}
		tearDownHelper();
	}
	
	/**
	 * Sets this balloon tip's close button (which will call closeBalloon() when clicked)
	 * You don't need add any behaviour to the button yourself; this is added for you.
	 * @param button		the new close button; if null, the balloon tip's close button is removed (if it had one)
	 */
	public void setCloseButton (JButton button) {
		setCloseButton(button, true, false);
	}
	
	/**
	 * Sets this balloon tip's close button
	 * @param button			the new close button; if null, the balloon tip's close button is removed (if it had one)
	 * @param permanentClose	if true, the default behaviour of the button is to close the balloon tip permanently by calling closeBalloon()
	 * 							if false, the default behaviour is to just hide the balloon tip by calling setVisible(false)
	 * @param noDefault			if true, no default behaviour is added and you'll have to set it yourself
	 */
	public void setCloseButton (JButton button, boolean permanentClose, boolean noDefault) {
		// Remove the current button
		if (closeButton!=null) {
			for (ActionListener a: closeButton.getActionListeners()) {
				closeButton.removeActionListener(a);
			}
			remove(closeButton);
			closeButton = null;
		}
		
		// Set the new button
		if (button!=null) {
			closeButton = button;
			if (!noDefault && permanentClose) {
				closeButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						closeBalloon();	
					}
				});
			} else if (!noDefault && !permanentClose) {
				closeButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						setVisible(false);
					}
				});
			}
			add(closeButton, new GridBagConstraints(1, 0, 1, 1, 0, 0, GridBagConstraints.NORTHEAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));	
		}
	}
	
	/**
	 * Retrieve this balloon tip's close button
	 * @return		the close button (null if not present)
	 */
	public JButton getCloseButton() {
		return closeButton;
	}
	
	/**
	 * Creates a default close button (without any behaviour)
	 * @return	the close button
	 */
	public static JButton getDefaultCloseButton() {
		JButton button = new JButton();
		button.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 5));
		button.setContentAreaFilled(false);
		button.setIcon(defaultCloseIcon);
		button.setRolloverIcon(rolloverCloseIcon);
		button.setPressedIcon(pressedCloseIcon);
		
		return button;
	}
	
	/**
	 * Set the icons for the default close button
	 * (This only affects balloon tips created after calling this method.)
	 * @param normal		regular icon
	 * @param pressed		icon when clicked
	 * @param rollover		icon when hovering over the button
	 */
	public static void setDefaultCloseButtonIcons(Icon normal, Icon pressed, Icon rollover) {
		defaultCloseIcon  = normal;
		rolloverCloseIcon = rollover;
		pressedCloseIcon  = pressed;
	}
	
	/**
	 * Adds a mouse listener that will close this balloon tip when clicked.
	 * @param permanentClose	if true, the default behaviour is to close the balloon tip permanently by calling closeBalloon()
	 * 							if false, the default behaviour is to just hide the balloon tip by calling setVisible(false)
	 */
	public void addDefaultMouseListener(boolean permanentClose) {
			removeMouseListener(clickListener);
			if (permanentClose) {
				clickListener = new MouseAdapter() {
					public void mouseClicked(MouseEvent e) {
						e.consume();
						closeBalloon();
					}
				};
			} else {
				clickListener = new MouseAdapter() {
					public void mouseClicked(MouseEvent e) {
						e.consume();
						setVisible(false);
					}
				};	
			}
			addMouseListener(clickListener);
	}
	
	/**
	 * Change the component this balloon tip is attached to
	 * (The top-level container will be re-determined during this process;
	 * if you set it manually, you'll have to set it again..)
	 * @param component		the new component to attach to (may not be null)
	 */
	public void setAttachedComponent(JComponent component) {
		tearDownHelper(); // Remove any listeners related to the old attached component
		this.attachedComponent = component;
		setupHelper(); // Reinstall the listeners
		refreshLocation();
	}

	/**
	 * Retrieve the component this balloon tip is attached to
	 * @return The attached component
	 */
	public JComponent getAttachedComponent() {
		return attachedComponent;
	}
	
	/**
	 * Set the container on which this balloon tip should be drawn
	 * @param tlc			the top-level container: may not be null; must be valid (isValid() must return true)
	 */
	public void setTopLevelContainer(JLayeredPane tlc) {
		if (topLevelContainer != null) {
			topLevelContainer.remove(this);
			topLevelContainer.removeComponentListener(topLevelContainerListener);
		}
 
		this.topLevelContainer = tlc;

		// If the window is resized, we should check if the balloon still fits
		topLevelContainer.addComponentListener(topLevelContainerListener);
		// We use the popup layer of the top level container (frame or dialog) to show the balloon tip
		topLevelContainer.add(this, JLayeredPane.POPUP_LAYER);
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
	 * (Is able to update balloon tip's location
	 * even if the balloon tip is not shown.)
	 */
	public void refreshLocation() {
		Point location = SwingUtilities.convertPoint(attachedComponent, getLocation(), this);
			positioner.determineAndSetLocation(new Rectangle(location.x, location.y, attachedComponent.getWidth(), attachedComponent.getHeight()));
	}
	
	/**
	 * Set this balloon tip's visibility
	 * @param visible		visible if true (and if the listeners associated with this balloon tip have no reason to hide the balloon tip!
	 * 						For example, it makes no sense to show balloon tip if the component it's attached to is hidden..); invisible otherwise
	 */
	public void setVisible(boolean visible) {
		visibilityControl.setCriteriumAndUpdate("manual", visible);
	}
	
	/*
	 * Sets the balloon tip's visibility by calling super.setVisible()
	 * @param visible	true if the balloon tip should be visible
	 */
	protected void forceSetVisible(boolean visible) {
		super.setVisible(visible);
	}

	/*
	 * Helper method for constructing a BalloonTip
	 */
	private void setup(final JComponent attachedComponent, JComponent contents, BalloonTipStyle style, BalloonTipPositioner positioner, JButton closeButton) {
		this.attachedComponent = attachedComponent;
		this.contents = contents;
		this.style = style;
		this.positioner = positioner;
		
		positioner.setBalloonTip(this);
		setBorder(this.style);
		setOpaque(false);
		setLayout(new GridBagLayout());

		add(this.contents, new GridBagConstraints(0, 0, 1, 1, 1, 1, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		setCloseButton(closeButton);
		
		// Don't allow to click 'through' the balloon tip
		clickListener = new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {e.consume();}
		};
		addMouseListener(clickListener);

		// Attempt to run setupHelper() ...
		if (attachedComponent.isValid()) {
			setupHelper();
			refreshLocation();
		} else {
			/* We can't determine the top-level container yet.
			 * We'll just have to wait until the parent is set and try again... */
			final AncestorListener attachedComponentParentListener = new AncestorListener() {
				public void ancestorAdded(AncestorEvent e) {
					setupHelper();
					refreshLocation();
					//repaint();
					e.getComponent().removeAncestorListener(this); // Remove yourself
				}
				public void ancestorMoved(AncestorEvent e) {}
				public void ancestorRemoved(AncestorEvent e) {}
			};
			attachedComponent.addAncestorListener(attachedComponentParentListener);
		}
	}
	
	/*
	 * Helper method for setup() and changeAttachedComponent()
	 */
	private void setupHelper() {
		// Set the pane on which the balloon tip is drawn
		setTopLevelContainer(attachedComponent.getRootPane().getLayeredPane());
		
		// If the attached component is moved/hidden/shown, the balloon tip should act accordingly
		attachedComponent.addComponentListener(attachedComponentListener);
		
		// Prepare these variables, in case this balloon tip is embedded in a tab
		boolean embeddedInTab = false;
		tabbedPaneListener = new ComponentAdapter() {
			public void componentShown(ComponentEvent e) {visibilityControl.setCriteriumAndUpdate("tabShowing",true);}
			public void componentHidden(ComponentEvent e) {visibilityControl.setCriteriumAndUpdate("tabShowing",false);}
		};

		/* Follow the path of parents of the attached component to find any JTabbedPanes 
		 * in which the component may be contained.. */
		Container current = attachedComponent.getParent();
		Container previous = null;
		while (current!=null) {
			if (current instanceof JTabbedPane) {
				embeddedInTab = true;
				/* Switching tabs only tells the JPanel representing the contents of each tab whether it went invisible or not.
				 * Unfortunately, it doesn't propagate such events to each and every component within each tab.
				 * Because of this, we'll have to add a listener to the JPanel of this tab. If it goes invisible, so should the balloon tip. */
				previous.addComponentListener(tabbedPaneListener);
			}
			previous = current;
			current = current.getParent();
		}
		
		// Ditch the tabbedPaneListener instance if you're not going to use it
		if (!embeddedInTab) {
			tabbedPaneListener = null;
		}
	}
	
	/*
	 * Helper method for closeBalloon() and changeAttachedComponent()
	 * Removes a number of listeners attached to the baloon tip.
	 */
	private void tearDownHelper() {
		attachedComponent.removeComponentListener(attachedComponentListener);
		
		// Remove tabbedPaneListener(s), if any
		if (tabbedPaneListener!=null) {
			Container current = attachedComponent.getParent();
			Container previous = null;
			while (current!=null) {
				if (current instanceof JTabbedPane) {
					previous.removeComponentListener(tabbedPaneListener);
				}
				previous = current;
				current = current.getParent();
			}
			tabbedPaneListener = null;
		}

		if (topLevelContainer != null) {
			topLevelContainer.remove(this);
			topLevelContainer.removeComponentListener(topLevelContainerListener);
		}
	}
}