package main;

import java.util.EventObject;

/**
 * For transporting messages from TemplatePopupMenu to any listening class.
 * @author John McCullock
 * @version 1.0 2013-10-22
 * @see TemplatePopupEventListener, TemplatePopupListener
 */
public class TemplatePopupEvent extends EventObject
{
	private int mTableRow = 0;
	
	public TemplatePopupEvent(Object source, int row)
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
