package main;

import java.util.EventListener;

/**
 * For transporting messages from TemplatePopupMenu to any listening class.
 * @author John McCullock
 * @version 1.0 2013-10-22
 * @see TemplatePopupEvent, TemplatePopupListener
 */
public interface TemplatePopupEventListener extends EventListener
{
	abstract void templatePopupOpenPerformed(TemplatePopupEvent e);
	abstract void templatePopupDeletePerformed(TemplatePopupEvent e);
}
