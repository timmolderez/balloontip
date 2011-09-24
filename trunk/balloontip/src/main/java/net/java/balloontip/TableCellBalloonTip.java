/**
 * Copyright (c) 2011 Bernhard Pauler, Tim Molderez.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the 3-Clause BSD License
 * which accompanies this distribution, and is available at
 * http://www.opensource.org/licenses/BSD-3-Clause
 */

package net.java.balloontip;

import java.awt.Point;
import java.awt.Rectangle;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.event.TableColumnModelListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import net.java.balloontip.positioners.BalloonTipPositioner;
import net.java.balloontip.styles.BalloonTipStyle;

/**
 * Provides similar functionality as a CustomBalloonTip, but attaches itself to a cell in a JTable
 * @author Tim Molderez
 */
public class TableCellBalloonTip extends CustomBalloonTip {

	protected int row;
	protected int column;
	private AncestorListener attachedComponentParentListener = null;

	// If someone messes with the table's columns, this might screw up the balloon tip's position, so we'll just close it..
	private final TableColumnModelListener columnListener = new TableColumnModelListener()  {
		public void columnAdded(TableColumnModelEvent e) {closeBalloon();}
		public void columnMarginChanged(ChangeEvent e) {setCellPosition(row, column);}
		public void columnMoved(TableColumnModelEvent e) {closeBalloon();}
		public void columnRemoved(TableColumnModelEvent e) {closeBalloon();}
		public void columnSelectionChanged(ListSelectionEvent e) {}
	};

	// If someone adds/removes rows, ...
	private final TableModelListener tableModelListener = new TableModelListener() {
		public void tableChanged(TableModelEvent e) {
			if (e.getType()==TableModelEvent.INSERT || e.getType()==TableModelEvent.DELETE) {
				closeBalloon();
			}
		}
	};

	// This class is needed when the table hasn't been set up yet during the constructor of this balloon tip
	private class ConstructorHelper implements AncestorListener {
		public void ancestorAdded(AncestorEvent event) {
			addListeners();
			// Don't forget to reposition yourself! During the constructor the table wasn't set up yet, so you couldn't determine the table cell's position...
			setCellPosition(row, column);
			// Remove yourself
			((JTable)attachedComponent).removeAncestorListener(this);
		}
		public void ancestorMoved(AncestorEvent event) {}
		public void ancestorRemoved(AncestorEvent event) {}
	}

	/**
	 * @see net.java.balloontip.BalloonTip#BalloonTip(JComponent, JComponent, BalloonTipStyle, Orientation, AttachLocation, int, int, boolean)
	 * @param table		the table to attach the balloon tip to (may not be null)
	 * @param row		which row is the balloon tip attached to
	 * @param column	which column is the balloon tip attached to
	 */
	public TableCellBalloonTip(JTable table, JComponent component, int row, int column, BalloonTipStyle style, Orientation alignment, AttachLocation attachLocation, int horizontalOffset, int verticalOffset, boolean useCloseButton) {
		super(table, component, table.getCellRect(row, column, true), style, alignment, attachLocation, horizontalOffset, verticalOffset, useCloseButton);
		setup(table, row, column);
	}

	/**
	 * @see net.java.balloontip.BalloonTip#BalloonTip(JComponent, JComponent, BalloonTipStyle, BalloonTipPositioner, JButton)
	 * @param table		the table to attach the balloon tip to (may not be null)
	 * @param row		which row is the balloon tip attached to
	 * @param column	which column is the balloon tip attached to
	 */
	public TableCellBalloonTip(JTable table, JComponent component, int row, int column, BalloonTipStyle style, BalloonTipPositioner positioner, JButton closeButton) {
		super(table, component, table.getCellRect(row, column, true), style, positioner, closeButton);
		setup(table, row, column);
	}

	/**
	 * Set the table cell the balloon tip should attach to
	 * @param row		row of the table cell
	 * @param column	column of the table cell
	 */
	public void setCellPosition(int row, int column) {
		offset = ((JTable)attachedComponent).getCellRect(row, column, true);
		refreshLocation();
	}

	public void closeBalloon() {
		attachedComponent.removeAncestorListener(attachedComponentParentListener);
		removeListeners();
		super.closeBalloon();
	}

	/*
	 * A helper method needed when constructing a TablecellBalloonTip instance
	 * @param table		the table to which this balloon tip attaches itself to
	 * @param row		the row of the cell to which this balloon tip attaches itself to
	 * @param column	the column of the cell to which this balloon tip attaches itself to
	 */
	private void setup(JTable table, int row, int column) {
		this.row = row;
		this.column = column;

		// We can only add the columnListener if we're sure the table has already been properly set up (which is the case if our super-constructor was able to determine the top level container...)
		if (getTopLevelContainer() != null) {
			addListeners();
		} else {
			attachedComponentParentListener = new ConstructorHelper();
			table.addAncestorListener(attachedComponentParentListener);
		}
	}

	/*
	 * Adds the necessary listeners to the attached JTable, such that
	 * this balloon tip will adjust itself to changes in the JTable
	 */
	private void addListeners() {
		JTable attachedTable=((JTable)attachedComponent);
		attachedTable.getColumnModel().addColumnModelListener(columnListener);
		attachedTable.getModel().addTableModelListener(tableModelListener);
	}

	/*
	 * Removes all listeners from the attached JTable
	 */
	private void removeListeners() {
		JTable attachedTable=((JTable)attachedComponent);
		attachedTable.getColumnModel().removeColumnModelListener(columnListener);
		attachedTable.getModel().removeTableModelListener(tableModelListener);
	}
	
	public Rectangle getAttachedRectangle() {
		Point location = SwingUtilities.convertPoint(attachedComponent, getLocation(), this);
		return new Rectangle(location.x + offset.x, location.y + offset.y, offset.width, offset.height);
	}
	
	public void refreshLocation() {
		if (topLevelContainer!=null) {
			positioner.determineAndSetLocation(getAttachedRectangle());
		}
	}
}
