package main;

import java.util.Enumeration;
import java.util.EventListener;
import java.util.UUID;
import java.util.Vector;

/**
 * For transporting messages from HistoryGUI to any listening class.
 * @author John McCullock
 * @version 2.0 2013-10-22
 * @see HistoryEvent, HistoryEventListener
 */
public class HistoryListener
{
	private static Vector<EventListener> mListeners = new Vector<EventListener>();
	
	public static void addListener(HistoryEventListener e)
	{
		mListeners.addElement(e);
		return;
	}
	
	public static void removeListener(HistoryEventListener e)
	{
		mListeners.removeElement(e);
		return;
	}
	
	public static void notifyTableRefresh(Object source)
	{
		HistoryEvent e = new HistoryEvent(source);
		Enumeration<EventListener> en = mListeners.elements();
		
		while(en.hasMoreElements())
		{
			((HistoryEventListener)en.nextElement()).historyGUIRefreshPerformed(e);
		}
		return;
	}
	
	public static void notifyReopen(Object source, UUID sessionID)
	{
		HistoryEvent e = new HistoryEvent(source, sessionID);
		Enumeration<EventListener> en = mListeners.elements();
		
		while(en.hasMoreElements())
		{
			((HistoryEventListener)en.nextElement()).historyGUIReopenPerformed(e);
		}
		return;
	}
	
	public static void notifyGUIClose(Object source)
	{
		HistoryEvent e = new HistoryEvent(source);
		Enumeration<EventListener> en = mListeners.elements();
		
		while(en.hasMoreElements())
		{
			((HistoryEventListener)en.nextElement()).historyGUIClosePerformed(e);
		}
		return;
	}
	
	public static void notifyClear(Object source)
	{
		HistoryEvent e = new HistoryEvent(source);
		Enumeration<EventListener> en = mListeners.elements();
		
		while(en.hasMoreElements())
		{
			((HistoryEventListener)en.nextElement()).historyGUIClearPerformed(e);
		}
		return;
	}
}
