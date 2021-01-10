package main;

import java.util.Enumeration;
import java.util.EventListener;
import java.util.Vector;

/**
 * For transporting messages from CensusGUI to any listening class.
 * @author John McCullock
 * @version 1.0 2013-10-22, but new feature to program version 2.0
 * @see CensusGUIEvent, CensusGUIEventListener
 */
public class CensusGUIListener
{
	private static Vector<EventListener> mListeners = new Vector<EventListener>();
	
	public static void addListener(CensusGUIEventListener e)
	{
		mListeners.addElement(e);
		return;
	}
	
	public static void removeListener(CensusGUIEventListener e)
	{
		mListeners.removeElement(e);
		return;
	}
	
	public static void notifySendToNetworkNeighborHoodList(Object source, String[] data)
	{
		CensusGUIEvent e = new CensusGUIEvent(source, data);
		Enumeration<EventListener> en = mListeners.elements();
		
		while(en.hasMoreElements())
		{
			((CensusGUIEventListener)en.nextElement()).censusGUISendToNetworkNeighborhoodListPerformed(e);
		}
		return;
	}
	
	public static void notifySendToRecipientList(Object source, String[] data)
	{
		CensusGUIEvent e = new CensusGUIEvent(source, data);
		Enumeration<EventListener> en = mListeners.elements();
		
		while(en.hasMoreElements())
		{
			((CensusGUIEventListener)en.nextElement()).censusGUISendToRecipientListPerformed(e);
		}
		return;
	}
	
	public static void notifyCensusGUIClose(Object source)
	{
		CensusGUIEvent e = new CensusGUIEvent(source);
		Enumeration<EventListener> en = mListeners.elements();
		
		while(en.hasMoreElements())
		{
			((CensusGUIEventListener)en.nextElement()).censusGUIClosePerformed(e);
		}
		return;
	}
}
