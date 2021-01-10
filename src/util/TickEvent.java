package util;

import java.util.EventObject;

/**
 * For transporting messages from Ticker to any listening class.
 * @author John McCullock
 * @version 1.0 2013-10-22
 * @see TickEventListener, TickListener
 */
public class TickEvent extends EventObject
{
	public TickEvent(Object source)
	{
		super(source);
		return;
	}
}
