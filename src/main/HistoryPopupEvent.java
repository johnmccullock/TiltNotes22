package main;

import java.util.EventObject;

/**
 * For transporting messages from HistoryPopupMenu to any listening class.
 * @author John McCullock
 * @version 1.0 2013-10-22
 * @see HistoryPopupEventListener, HistoryPopupListener
 */
public class HistoryPopupEvent extends EventObject
{
	private int mTableRow = 0;
	
	public HistoryPopupEvent(Object source)
	{
		super(source);
		return;
	}
	
	public HistoryPopupEvent(Object source, int row)
	{
		super(source);
		this.mTableRow = row;
		return;
	}
	
	public int getTableRow()
	{
		return this.mTableRow;
	}
}
