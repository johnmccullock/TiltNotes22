package main;

import java.util.EventObject;

/**
 * For transporting messages from TemplateSettingsPanel to any listening class.
 * @author John McCullock
 * @version 1.0 2013-10-22
 * @see TemplateSettingsEventListener, TemplateSettingsListener
 */
public class TemplateSettingsEvent extends EventObject
{
	private String mTemplateName = null;
	
	public TemplateSettingsEvent(Object source)
	{
		super(source);
		return;
	}
	
	public TemplateSettingsEvent(Object source, final String templateName)
	{
		super(source);
		this.setTemplateName(templateName);
		return;
	}
	
	private void setTemplateName(final String templateName)
	{
		this.mTemplateName = templateName;
		return;
	}
	
	public String getTemplateName()
	{
		return this.mTemplateName;
	}
}
