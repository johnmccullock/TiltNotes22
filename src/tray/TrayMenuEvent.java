package tray;

import java.util.EventObject;

public class TrayMenuEvent extends EventObject
{
	private String mTemplateName = null;
	
	public TrayMenuEvent(Object source)
	{
		super(source);
		return;
	}
	
	public TrayMenuEvent(Object source, String template)
	{
		super(source);
		this.setTemplateName(template);
		return;
	}
	
	private void setTemplateName(String template)
	{
		this.mTemplateName = template;
		return;
	}
	
	public String getTemplateName()
	{
		return this.mTemplateName;
	}
}
