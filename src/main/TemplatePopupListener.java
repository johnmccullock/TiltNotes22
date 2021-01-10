package main;

import java.util.Enumeration;
import java.util.EventListener;
import java.util.Vector;

/**
 * For transporting messages from TemplatePopupMenu to any listening class.
 * @author John McCullock
 * @version 1.0 2013-10-22
 * @see TemplatePopupEvent, TemplatePopupEventListener
 */
public class TemplatePopupListener
{
	private static Vector<EventListener> mListeners = new Vector<EventListener>();
	
	public static void addListener(TemplatePopupEventListener e)
	{
		mListeners.addElement(e);
		return;
	}
	
	public static void removeListener(TemplatePopupEventListener e)
	{
		mListeners.removeElement(e);
		return;
	}
	
	public static void notifyOpen(Object source, int row)
	{
		TemplatePopupEvent e = new TemplatePopupEvent(source, row);
		Enumeration<EventListener> en = mListeners.elements();
		
		while(en.hasMoreElements())
		{
			((TemplatePopupEventListener)en.nextElement()).templatePopupOpenPerformed(e);
		}
		return;
	}
	
	public static void notifyDelete(Object source, int row)
	{
		TemplatePopupEvent e = new TemplatePopupEvent(source, row);
		Enumeration<EventListener> en = mListeners.elements();
		
		while(en.hasMoreElements())
		{
			((TemplatePopupEventListener)en.nextElement()).templatePopupDeletePerformed(e);
		}
		return;
	}
}
