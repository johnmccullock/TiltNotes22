package util;

import java.util.Enumeration;
import java.util.EventListener;
import java.util.Vector;

/**
 * For transporting messages from Ticker to any listening class.
 * @author John McCullock
 * @version 1.0 2013-10-22
 * @see TickEvent, TickEventListener
 */
public class TickListener
{
	private static Vector<EventListener> mListeners = new Vector<EventListener>();
	
	public static synchronized void addListener(TickEventListener tee)
	{
		mListeners.addElement(tee);
		return;
	}
	
	public static synchronized void removeListener(TickEventListener tee)
	{
		mListeners.removeElement(tee);
		return;
	}
	
	public static synchronized void notifyTimerTick(Object source)
	{
		TickEvent te = new TickEvent(source);
		Enumeration<EventListener> en = mListeners.elements();
		
		while(en.hasMoreElements())
		{
			((TickEventListener)en.nextElement()).tickPerformed(te);
		}
		return;
	}
}
