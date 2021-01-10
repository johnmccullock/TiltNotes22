package main;

import java.util.EventListener;

/**
 * For transporting messages from HistoryGUI to any listening class.
 * @author John McCullock
 * @version 2.0 2013-10-22
 * @see HistoryEvent, HistoryListener
 */
public interface HistoryEventListener extends EventListener
{
	abstract void historyGUIRefreshPerformed(HistoryEvent e);
	abstract void historyGUIReopenPerformed(HistoryEvent e);
	abstract void historyGUIClosePerformed(HistoryEvent e);
	abstract void historyGUIClearPerformed(HistoryEvent e);
}
