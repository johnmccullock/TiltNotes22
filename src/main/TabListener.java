package main;

import java.util.EventListener;
import java.util.Vector;
import java.util.Enumeration;

/**
 * For transporting messages from ConfigurationGUI tabbed panel panes back to the host ConfigurationGUI.
 * Tasked solely with launching an About window in this version.
 * @author John McCullock
 * @version 2.0 2013-10-22
 * @see TabEventListener, TabListener
 */
public class TabListener
{
	private static Vector<EventListener> mListeners = new Vector<EventListener>();
	
	public static void addListener(TabEventListener tel)
	{
		mListeners.addElement(tel);
		return;
	}
	
	public static void removeListener(TabEventListener tel)
	{
		mListeners.removeElement(tel);
		return;
	}
	
	public static void notifyAboutWindow(Object source)
	{
		TabEvent te = new TabEvent(source);
		Enumeration<EventListener> en = mListeners.elements();
		
		while(en.hasMoreElements())
		{
			((TabEventListener)en.nextElement()).tabEventAboutButtonPerformed(te);
		}
		return;
	}
}
