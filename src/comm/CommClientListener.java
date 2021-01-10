package comm;

import java.util.EventListener;
import java.util.Vector;
import java.util.Enumeration;

public class CommClientListener
{
	private static Vector<EventListener> mListeners = new Vector<EventListener>();
	
	public static void addListener(CommClientEventListener ccel)
	{
		mListeners.addElement(ccel);
		return;
	}
	
	public static void removeListener(CommClientEventListener ccel)
	{
		mListeners.removeElement(ccel);
		return;
	}
	
	public static void notifyContentReceived(Object source, byte[] content)
	{
		CommClientEvent cce = new CommClientEvent(source, content);
		Enumeration<EventListener> en = mListeners.elements();
		
		while(en.hasMoreElements())
		{
			((CommClientEventListener)en.nextElement()).commClientContentReceived(cce);
		}
		return;
	}
}
