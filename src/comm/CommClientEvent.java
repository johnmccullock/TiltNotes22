package comm;

import java.util.EventObject;

public class CommClientEvent extends EventObject
{
	private byte[] mContent = null;
	
	public CommClientEvent(Object source, byte[] content)
	{
		super(source);
		this.setContent(content);
		return;
	}
	
	private void setContent(byte[] content)
	{
		this.mContent = content;
		return;
	}
	
	public byte[] getContent()
	{
		return this.mContent;
	}
}
