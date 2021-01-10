package util;

import java.util.EventListener;

/**
 * For transporting messages from Ticker to any listening class.
 * @author John McCullock
 * @version 1.0 2013-10-22
 * @see TickEvent, TickListener
 */
public interface TickEventListener extends EventListener
{
	abstract void tickPerformed(TickEvent te);
}
