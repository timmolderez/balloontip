/**
 * Copyright (c) 2011 Bernhard Pauler, Tim Molderez.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the 3-Clause BSD License
 * which accompanies this distribution, and is available at
 * http://www.opensource.org/licenses/BSD-3-Clause
 */

package net.java.balloontip;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import net.java.balloontip.positioners.BalloonTipPositioner;
import net.java.balloontip.styles.BalloonTipStyle;

/**
 * A baloon tip that can attach itself to an item in a JList
 * @author Tim Molderez
 */
public class ListItemBalloonTip extends CustomBalloonTip {

	protected int index; // Index of the list item that this balloon tip is attached to
	
	// If list data is added or removed, adjust the balloon tip
	private final ListDataListener listDataListener = new ListDataListener() {
		public void intervalAdded(ListDataEvent e) {
			// If the balloon tip needs to move down
			if (e.getIndex1() <= index) {
				index+=e.getIndex1()-e.getIndex0()+1;
				setItemPosition(index);
			}
		}

		public void intervalRemoved(ListDataEvent e) {
			// If the balloon tip needs to move up
			if (e.getIndex1() < index) {
				index-=e.getIndex1()-e.getIndex0()+1;
				setItemPosition(index);
			// If the item with the balloon tip is removed
			} else if (index >= e.getIndex0() && index <= e.getIndex1()) {
				closeBalloon();
			} 
		}

		public void contentsChanged(ListDataEvent e) {
			setItemPosition(index); // Refreshes the item's position, in case it might've changed..
		}
	};

	/**
	 * @see net.java.balloontip.BalloonTip#BalloonTip(JComponent, JComponent, BalloonTipStyle, Orientation, AttachLocation, int, int, boolean)
	 * @param list		the list to attach the balloon tip to (may not be null)
	 * @param index		index of the list item (must be valid)
	 */
	public ListItemBalloonTip(JList list, JComponent component, int index, BalloonTipStyle style, Orientation alignment, AttachLocation attachLocation, int horizontalOffset, int verticalOffset, boolean useCloseButton) {
		super(list, component, list.getCellBounds(index, index), style, alignment, attachLocation, horizontalOffset, verticalOffset, useCloseButton);
		setup(list, index);
	}

	/**
	 * @see net.java.balloontip.BalloonTip#BalloonTip(JComponent, JComponent, BalloonTipStyle, BalloonTipPositioner, JButton)
	 * @param table		the list to attach the balloon tip to (may not be null)
	 * @param index		index of the list item (must be valid)
	 */
	public ListItemBalloonTip(JList list, JComponent component, int index, BalloonTipStyle style, BalloonTipPositioner positioner, JButton closeButton) {
		super(list, component, list.getCellBounds(index, index), style, positioner, closeButton);
		setup(list, index);
	}

	/**
	 * Set the list item the balloon tip should attach to
	 * @param index		index of the list item
	 */
	public void setItemPosition(int index) {
		offset = ((JList)attachedComponent).getCellBounds(index, index);
		refreshLocation();
	}

	public void closeBalloon() {
		removeListeners();
		super.closeBalloon();
	}

	/*
	 * A helper method needed when constructing a TablecellBalloonTip instance
	 * @param table		The table to which this balloon tip attaches itself to
	 * @param index		The row of the cell to which this balloon tip attaches itself to
	 */
	private void setup(JList list, int index) {
		this.index = index;
		addListeners();
	}

	/*
	 * Adds the necessary listeners to the attached JTable, such that
	 * this balloon tip will adjust itself to changes in the JTable
	 */
	private void addListeners() {
		JList attachedList=((JList)attachedComponent);
		attachedList.getModel().addListDataListener(listDataListener);
	}

	/*
	 * Removes all listeners from the attached JTable
	 */
	private void removeListeners() {
		JList attachedList=((JList)attachedComponent);
		attachedList.getModel().removeListDataListener(listDataListener);
	}
}
