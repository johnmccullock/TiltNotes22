package main;

import java.util.EventObject;

/**
 * For transporting messages from ConfigurationGUI tabbed panel panes back to the host ConfigurationGUI.
 * Tasked solely with launching an About window in this version.
 * @author John McCullock
 * @version 2.0 2013-10-22
 * @see TabEventListener, TabListener
 */
public class TabEvent extends EventObject
{
	public TabEvent(Object source)
	{
		super(source);
		return;
	}
}
