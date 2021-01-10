package main.gui.text;

public class DataItem
{
	private String[] mData = null;
	private Object mSource = null;
	
	public DataItem(String[] data, Object source)
	{
		this.mData = data;
		this.mSource = source;
		return;
	}
	
	public String[] getData()
	{
		return this.mData;
	}
	
	public Object getSource()
	{
		return this.mSource;
	}
}