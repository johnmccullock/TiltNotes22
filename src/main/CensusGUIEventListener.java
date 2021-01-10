package main;

import java.util.EventListener;

/**
 * For transporting messages from CensusGUI to any listening class.
 * @author John McCullock
 * @version 1.0 2013-10-22, but new feature to program version 2.0
 * @see CensusGUIEvent, CensusGUIListener
 */
public interface CensusGUIEventListener extends EventListener
{
	abstract void censusGUISendToNetworkNeighborhoodListPerformed(CensusGUIEvent e);
	abstract void censusGUISendToRecipientListPerformed(CensusGUIEvent e);
	abstract void censusGUIClosePerformed(CensusGUIEvent e);
}
