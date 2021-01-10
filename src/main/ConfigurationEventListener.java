package main;

import java.util.EventListener;

/**
 * Event listener interface defining the events to listen for.
 * @author John McCullock
 * @version 2.0 2013-10-22
 * @see ConfigurationEvent
 * @see ConfigurationListener
 */
public interface ConfigurationEventListener extends EventListener
{
	abstract void configurationGUIClosePerformed(ConfigurationEvent ce);
	abstract void configurationGUISavePerformed(ConfigurationEvent ce);
}
