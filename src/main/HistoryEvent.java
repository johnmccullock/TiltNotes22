package main;

import java.util.EventObject;
import java.util.UUID;

/**
 * For transporting messages from HistoryGUI to any listening class.
 * @author John McCullock
 * @version 2.0 2013-10-22
 * @see HistoryEventListener, HistoryListener
 */
public class HistoryEvent extends EventObject
{
	private UUID mSessionID = null;
	
	public HistoryEvent(Object source)
	{
		super(source);
		return;
	}
	
	public HistoryEvent(Object source, UUID sessionID)
	{
		super(source);
		this.setSessionID(sessionID);
		return;
	}
	
	private void setSessionID(UUID sessionID)
	{
		try{
			if(sessionID == null){
				throw new Exception("Parameter cannot be null.");
			}
			this.mSessionID = sessionID;
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return;
	}
	
	public UUID getSessionID()
	{
		return this.mSessionID;
	}
}
