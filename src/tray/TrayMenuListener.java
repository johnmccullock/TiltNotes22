package tray;

import java.util.Enumeration;
import java.util.EventListener;
import java.util.Vector;


public class TrayMenuListener
{
	private static Vector<EventListener> mListeners = new Vector<EventListener>();
	
	public static void addListener(TrayMenuEventListener tmel)
	{
		mListeners.addElement(tmel);
		return;
	}
	
	public static void removeListener(TrayMenuEventListener tmel)
	{
		mListeners.removeElement(tmel);
		return;
	}
	
	public static void notifyNewNote(Object source)
	{
		TrayMenuEvent tme = new TrayMenuEvent(source);
		Enumeration<EventListener> en = mListeners.elements();
		
		while(en.hasMoreElements())
		{
			((TrayMenuEventListener)en.nextElement()).trayMenuNewNotePerformed(tme);
		}
		return;
	}
	
	public static void notifyTemplate(Object source, String template)
	{
		TrayMenuEvent tme = new TrayMenuEvent(source, template);
		Enumeration<EventListener> en = mListeners.elements();
		
		while(en.hasMoreElements())
		{
			((TrayMenuEventListener)en.nextElement()).trayMenuTemplatePerformed(tme);
		}
		return;
	}
	
	public static void notifyHistory(Object source)
	{
		TrayMenuEvent tme = new TrayMenuEvent(source);
		Enumeration<EventListener> en = mListeners.elements();
		
		while(en.hasMoreElements())
		{
			((TrayMenuEventListener)en.nextElement()).trayMenuHistoryPerformed(tme);
		}
		return;
	}
	
	public static void notifySettings(Object source)
	{
		TrayMenuEvent tme = new TrayMenuEvent(source);
		Enumeration<EventListener> en = mListeners.elements();
		
		while(en.hasMoreElements())
		{
			((TrayMenuEventListener)en.nextElement()).trayMenuSettingsPerformed(tme);
		}
		return;
	}
	
	public static void notifyExit(Object source)
	{
		TrayMenuEvent tme = new TrayMenuEvent(source);
		Enumeration<EventListener> en = mListeners.elements();
		
		while(en.hasMoreElements())
		{
			((TrayMenuEventListener)en.nextElement()).trayMenuExitPerformed(tme);
		}
		return;
	}
}
