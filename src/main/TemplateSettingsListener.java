package main;

import java.util.Enumeration;
import java.util.EventListener;
import java.util.Vector;

/**
 * For transporting messages from TemplateSettingsPanel to any listening class.
 * @author John McCullock
 * @version 1.0 2013-10-22
 * @see TemplateSettingsEvent, TemplateSettingsEventListener
 */
public class TemplateSettingsListener
{
	private static Vector<EventListener> mListeners = new Vector<EventListener>();
	
	public static void addListener(TemplateSettingsEventListener e)
	{
		mListeners.addElement(e);
		return;
	}
	
	public static void removeListener(TemplateSettingsEventListener e)
	{
		mListeners.removeElement(e);
		return;
	}
	
	public static void notifyNew(Object source)
	{
		TemplateSettingsEvent e = new TemplateSettingsEvent(source);
		Enumeration<EventListener> en = mListeners.elements();
		
		while(en.hasMoreElements())
		{
			((TemplateSettingsEventListener)en.nextElement()).templateSettingsRequestNewPerformed(e);
		}
		return;
	}
	
	public static void notifyOpen(Object source, String templateName)
	{
		TemplateSettingsEvent e = new TemplateSettingsEvent(source, templateName);
		Enumeration<EventListener> en = mListeners.elements();
		
		while(en.hasMoreElements())
		{
			((TemplateSettingsEventListener)en.nextElement()).templateSettingsRequestOpenPerformed(e);
		}
		return;
	}
	
	public static void notifySave(Object source)
	{
		TemplateSettingsEvent e = new TemplateSettingsEvent(source);
		Enumeration<EventListener> en = mListeners.elements();
		
		while(en.hasMoreElements())
		{
			((TemplateSettingsEventListener)en.nextElement()).templateSettingsRequestSavePerformed(e);
		}
		return;
	}
	
	public static void notifyDelete(Object source)
	{
		TemplateSettingsEvent e = new TemplateSettingsEvent(source);
		Enumeration<EventListener> en = mListeners.elements();
		
		while(en.hasMoreElements())
		{
			((TemplateSettingsEventListener)en.nextElement()).templateSettingsRequestDeletePerformed(e);
		}
		return;
	}
}
