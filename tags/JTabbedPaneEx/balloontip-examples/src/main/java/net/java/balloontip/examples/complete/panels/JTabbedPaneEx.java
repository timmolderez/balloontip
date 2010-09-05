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

import java.awt.Component;
import java.awt.Container;
import java.awt.FocusTraversalPolicy;

import javax.swing.JComponent;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;

/**
 * This class overrides JTabbedPane to correct following bug
 * under Java 1.5 (and only in this version of Java !) :
 * "switching (JTabbedPane) tabs does not cause component events"
 * References :
 * "requestFocusInWindow() fails for comp. on JTabbedPane aftertab switch"
 * http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=5089436
 *
 * The correction was made by implementing/backporting the "visible Component" visComp
 * introduced in Java 1.6.
 * Code was taken and adapted from Java 1.6.0.14 SwingUtilities2 & JTabbedPane classes
 * Code is :
 * Copyright 2006 Sun Microsystems, Inc. All rights reserved.
 *
 * @author Thierry Blind
 */
public class JTabbedPaneEx extends JTabbedPane {

	private static final long serialVersionUID = -737693676859360737L;
	/* The component that is currently visible */
	private Component visComp = null;
	/**
	 * Can be overridden to manually disable the implemented JTabbedPane bug correction.
	 */
	public static boolean ADD_JAVA_CORRECTION = (SystemUtils.IS_JAVA_1_4 && SystemUtils.JAVA_VERSION_FLOAT >= 1.42f) || SystemUtils.IS_JAVA_1_5;

	public JTabbedPaneEx() {
		super();
	}

	public JTabbedPaneEx(int tabPlacement) {
		super(tabPlacement);
	}

	public JTabbedPaneEx(int tabPlacement, int tabLayoutPolicy) {
		super(tabPlacement, tabLayoutPolicy);
	}

	// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	// BEGIN CODE TAKEN FROM JAVA 1.6 SwingUtilities2 CLASS
	// References : http://www.java2s.com/Open-Source/Java-Document/6.0-JDK-Modules-sun/swing/sun/swing/SwingUtilities2.java.htm
	// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

	// At this point we need this method here. But we assume that there
	// will be a common method for this purpose in the future releases.
	private Component compositeRequestFocus(Component component) {
		if (component instanceof  Container) {
			Container container = (Container) component;
			if (container.isFocusCycleRoot()) {
				FocusTraversalPolicy policy = container
				.getFocusTraversalPolicy();
				Component comp = policy.getDefaultComponent(container);
				if (comp != null) {
					comp.requestFocus();
					return comp;
				}
			}
			Container rootAncestor = container
			.getFocusCycleRootAncestor();
			if (rootAncestor != null) {
				FocusTraversalPolicy policy = rootAncestor
				.getFocusTraversalPolicy();
				Component comp = policy.getComponentAfter(rootAncestor,
						container);

				if (comp != null
						&& SwingUtilities.isDescendingFrom(comp,
								container)) {
					comp.requestFocus();
					return comp;
				}
			}
		}
		if (component.isFocusable()) {
			component.requestFocus();
			return component;
		}
		return null;
	}

	/**
	 * Change focus to the visible component in {@code JTabbedPane}.
	 * This is not a general-purpose method and is here only to permit
	 * sharing code.
	 */
	private boolean tabbedPaneChangeFocusTo(Component comp) {
		if (comp != null) {
			if (comp.isFocusTraversable()) {
				compositeRequestFocus(comp);
				return true;
			} else if (comp instanceof  JComponent
					&& ((JComponent) comp).requestDefaultFocus()) {

				return true;
			}
		}

		return false;
	}

	// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	// END CODE TAKEN FROM JAVA 1.6 SwingUtilities2 CLASS
	// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

	// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	// BEGIN CODE TAKEN FROM JDK 1.6.0.14
	// References : JTabbedPane source code file
	// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

	/**
	 * Sends a {@code ChangeEvent}, with this {@code JTabbedPane} as the source,
	 * to each registered listener. This method is called each time there is
	 * a change to either the selected index or the selected tab in the
	 * {@code JTabbedPane}. Usually, the selected index and selected tab change
	 * together. However, there are some cases, such as tab addition, where the
	 * selected index changes and the same tab remains selected. There are other
	 * cases, such as deleting the selected tab, where the index remains the
	 * same, but a new tab moves to that index. Events are fired for all of
	 * these cases.
	 *
	 * @see #addChangeListener
	 * @see EventListenerList
	 */
	private void fireStateChangedEx() {
		/* --- Begin code to deal with visibility --- */

		/* This code deals with changing the visibility of components to
		 * hide and show the contents for the selected tab. It duplicates
		 * logic already present in BasicTabbedPaneUI, logic that is
		 * processed during the layout pass. This code exists to allow
		 * developers to do things that are quite difficult to accomplish
		 * with the previous model of waiting for the layout pass to process
		 * visibility changes; such as requesting focus on the new visible
		 * component.
		 *
		 * For the average code, using the typical JTabbedPane methods,
		 * all visibility changes will now be processed here. However,
		 * the code in BasicTabbedPaneUI still exists, for the purposes
		 * of backward compatibility. Therefore, when making changes to
		 * this code, ensure that the BasicTabbedPaneUI code is kept in
		 * synch.
		 */

		int selIndex = getSelectedIndex();

		/* if the selection is now nothing */
		if (selIndex < 0) {
			/* if there was a previous visible component */
			if (visComp != null && visComp.isVisible()) {
				/* make it invisible */
				visComp.setVisible(false);
			}

			/* now there's no visible component */
			visComp = null;

			/* else - the selection is now something */
		} else {
			/* Fetch the component for the new selection */
			Component newComp = getComponentAt(selIndex);

			/* if the new component is non-null and different */
			if (newComp != null && newComp != visComp) {
				boolean shouldChangeFocus = false;

				/* Note: the following (clearing of the old visible component)
				 * is inside this if-statement for good reason: Tabbed pane
				 * should continue to show the previously visible component
				 * if there is no component for the chosen tab.
				 */

				/* if there was a previous visible component */
				if (visComp != null) {
					shouldChangeFocus =
						(SwingUtilities.findFocusOwner(visComp) != null);

					/* if it's still visible */
					if (visComp.isVisible()) {
						/* make it invisible */
						visComp.setVisible(false);
					}
				}

				if (!newComp.isVisible()) {
					newComp.setVisible(true);
				}

				if (shouldChangeFocus) {
					tabbedPaneChangeFocusTo(newComp);
				}

				visComp = newComp;
			} /* else - the visible component shouldn't changed */
		}

		/* --- End code to deal with visibility --- */

		// Guaranteed to return a non-null array
		Object[] listeners = listenerList.getListenerList();
		// Process the listeners last to first, notifying
		// those that are interested in this event
		for (int i = listeners.length-2; i>=0; i-=2) {
			if (listeners[i]==ChangeListener.class) {
				// Lazily create the event:
				if (changeEvent == null)
					changeEvent = new ChangeEvent(this);
				((ChangeListener)listeners[i+1]).stateChanged(changeEvent);
			}
		}
	}

	/**
	 * Removes the tab at <code>index</code>.
	 * After the component associated with <code>index</code> is removed,
	 * its visibility is reset to true to ensure it will be visible
	 * if added to other containers.
	 * @param index the index of the tab to be removed
	 * @exception IndexOutOfBoundsException if index is out of range
	 *            (index < 0 || index >= tab count)
	 *
	 * @see #addTab
	 * @see #insertTab
	 */
	private void removeTabAtEx(int index) {
		Component component = getComponentAt(index);
		boolean shouldChangeFocus = false;

		/* if we're about to remove the visible component */
		if (component == visComp) {
			shouldChangeFocus = (SwingUtilities.findFocusOwner(visComp) != null);
			visComp = null;
		}

		super.removeTabAt(index);

		if (shouldChangeFocus) {
			tabbedPaneChangeFocusTo(getSelectedComponent());
		}

		revalidate();
		repaint();
	}

	/**
	 * Sets the component at <code>index</code> to <code>component</code>.
	 * An internal exception is raised if there is no tab at that index.
	 *
	 * @param index the tab index where this component is being placed
	 * @param component the component for the tab
	 * @exception IndexOutOfBoundsException if index is out of range
	 *            (index &lt; 0 || index &gt;= tab count)
	 *
	 * @see #getComponentAt
	 * @beaninfo
	 *    attribute: visualUpdate true
	 *  description: The component at the specified tab index.
	 */
	private void setComponentAtEx(int index, Component component) {
		if (component != getComponentAt(index)) {
			boolean shouldChangeFocus = false;

			if (getComponentAt(index) != null) {
				shouldChangeFocus =
					(SwingUtilities.findFocusOwner(getComponentAt(index)) != null);
			}

			// IMPORTANT : Under Java 1.5, component = null is not permitted !
			super.setComponentAt(index, component);

			boolean selectedPage = (getSelectedIndex() == index);

			if (selectedPage) {
				this.visComp = component;
			}

			if (component != null) {
				if (shouldChangeFocus) {
					tabbedPaneChangeFocusTo(component);
				}
			} else {
				repaint();
			}

			revalidate();
		}
	}

	// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	// END CODE TAKEN FROM JDK 1.6.0.14
	// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

	protected void fireStateChanged() {
		if (ADD_JAVA_CORRECTION) {
			fireStateChangedEx();
		} else {
			super.fireStateChanged();
		}
	}

	public void removeTabAt(int index) {
		if (ADD_JAVA_CORRECTION) {
			removeTabAtEx(index);
		} else {
			super.removeTabAt(index);
		}
	}

	public void setComponentAt(int index, Component component) {
		if (ADD_JAVA_CORRECTION) {
			setComponentAtEx(index, component);
		} else {
			super.setComponentAt(index, component);
		}
	}
}
