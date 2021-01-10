package main;

import java.util.EventListener;

/**
 * For transporting messages from HistoryPopupMenu to any listening class.
 * @author John McCullock
 * @version 1.0 2013-10-22
 * @see HistoryPopupEvent, HistoryPopupListener
 */
public interface HistoryPopupEventListener extends EventListener
{
	abstract void historyPopupReopenPerformed(HistoryPopupEvent e);
	abstract void historyPopupRefreshPerformed(HistoryPopupEvent e);
}
