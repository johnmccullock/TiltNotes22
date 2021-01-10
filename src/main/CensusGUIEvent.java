package main;

import java.util.EventObject;

/**
 * For transporting messages from CensusGUI to any listening class.
 * @author John McCullock
 * @version 1.0 2013-10-22, but new feature to program version 2.0
 * @see CensusGUIEventListener, CensusGUIListener
 */
public class CensusGUIEvent extends EventObject
{
	private String[] mData = null;
	
	public CensusGUIEvent(Object source)
	{
		super(source);
		return;
	}
	
	public CensusGUIEvent(Object source, String[] data)
	{
		super(source);
		this.setData(data);
		return;
	}
	
	private void setData(String[] data)
	{
		this.mData = data;
		return;
	}
	
	public String[] getData()
	{
		return this.mData;
	}
}
