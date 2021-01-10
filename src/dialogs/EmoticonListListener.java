package dialogs;

import java.util.Enumeration;
import java.util.EventListener;
import java.util.Vector;

public class EmoticonListListener
{
	private static Vector<EventListener> mListeners = new Vector<EventListener>();
	
	public static void addListener(EmoticonListEventListener tlel)
	{
		mListeners.addElement(tlel);
		return;
	}
	
	public static void removeListener(EmoticonListEventListener tlel)
	{
		mListeners.removeElement(tlel);
		return;
	}
	
	public static void notifyClick(Object source)
	{
		EmoticonListEvent tle = new EmoticonListEvent(source);
		Enumeration<EventListener> en = mListeners.elements();
		
		while(en.hasMoreElements())
		{
			((EmoticonListEventListener)en.nextElement()).thumbListClickPerformed(tle);
		}
		return;
	}
	
	public static void notifyDoubleClick(Object source)
	{
		EmoticonListEvent tle = new EmoticonListEvent(source);
		Enumeration<EventListener> en = mListeners.elements();
		
		while(en.hasMoreElements())
		{
			((EmoticonListEventListener)en.nextElement()).thumbListDoubleClickPerformed(tle);
		}
		return;
	}
}
