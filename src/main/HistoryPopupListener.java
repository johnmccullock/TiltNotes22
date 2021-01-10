package main;

import java.util.Enumeration;
import java.util.EventListener;
import java.util.Vector;

/**
 * For transporting messages from HistoryPopupMenu to any listening class.
 * @author John McCullock
 * @version 1.0 2013-10-22
 * @see HistoryPopupEvent, HistoryPopupEventListener
 */
public class HistoryPopupListener
{
	private static Vector<EventListener> mListeners = new Vector<EventListener>();
	
	public static void addListener(HistoryPopupEventListener e)
	{
		mListeners.addElement(e);
		return;
	}
	
	public static void removeListener(HistoryPopupEventListener e)
	{
		mListeners.removeElement(e);
		return;
	}
	
	public static void notifyReopen(Object source, int row)
	{
		HistoryPopupEvent e = new HistoryPopupEvent(source, row);
		Enumeration<EventListener> en = mListeners.elements();
		
		while(en.hasMoreElements())
		{
			((HistoryPopupEventListener)en.nextElement()).historyPopupReopenPerformed(e);
		}
		return;
	}
	
	public static void notifyRefresh(Object source)
	{
		HistoryPopupEvent e = new HistoryPopupEvent(source);
		Enumeration<EventListener> en = mListeners.elements();
		
		while(en.hasMoreElements())
		{
			((HistoryPopupEventListener)en.nextElement()).historyPopupRefreshPerformed(e);
		}
		return;
	}
}
