package main;

import java.util.EventListener;

/**
 * For transporting messages from TemplateSettingsPanel to any listening class.
 * @author John McCullock
 * @version 1.0 2013-10-22
 * @see TemplateSettingsEvent, TemplateSettingsListener
 */
public interface TemplateSettingsEventListener extends EventListener
{
	abstract void templateSettingsRequestNewPerformed(TemplateSettingsEvent e);
	abstract void templateSettingsRequestOpenPerformed(TemplateSettingsEvent e);
	abstract void templateSettingsRequestSavePerformed(TemplateSettingsEvent e);
	abstract void templateSettingsRequestDeletePerformed(TemplateSettingsEvent e);
}
