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

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JTable;
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
 * Provides similar functionality as a CustomBalloonTip, but attaches itself to a cell in a JTable.
 * @author Tim Molderez
 */
public class TablecellBalloonTip extends CustomBalloonTip {

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
	 * @param table		The table to attach the balloon tip to
	 * @param row		Which row is the balloon tip attached to
	 * @param column	Which column is the balloon tip attached to
	 * @exception NullPointerException if at least one parameter (except for parameters text and style) is <code>null</code>
	 */
	public TablecellBalloonTip(JTable table, JComponent component, int row, int column, BalloonTipStyle style, Orientation alignment, AttachLocation attachLocation, int horizontalOffset, int verticalOffset, boolean useCloseButton) {
		super(table, component, table.getCellRect(row, column, true), style, alignment, attachLocation, horizontalOffset, verticalOffset, useCloseButton);
		setup(table, row, column);
	}

	/**
	 * @see net.java.balloontip.BalloonTip#BalloonTip(JComponent, JComponent, BalloonTipStyle, BalloonTipPositioner, boolean)
	 * @param table		The table to attach the balloon tip to
	 * @param row		Which row is the balloon tip attached to
	 * @param column	Which column is the balloon tip attached to
	 * @exception NullPointerException if at least one parameter (except for parameters text and style) is <code>null</code>
	 */
	public TablecellBalloonTip(JTable table, JComponent component, int row, int column, BalloonTipStyle style, BalloonTipPositioner positioner, JButton closeButton) {
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
	 * @param table		The table to which this balloon tip attaches itself to
	 * @param row		The row of the cell to which this balloon tip attaches itself to
	 * @param column	The column of the cell to which this balloon tip attaches itself to
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
}