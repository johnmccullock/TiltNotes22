package main.gui.text;

import javax.swing.text.AttributeSet;
import javax.swing.text.Style;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class StyledString
{
	public String text;
	public List<AttributeSet> attrs;
	public List<Location> locs;
	public Style logicalStyle;
 
	public StyledString(String text)
	{
		this.text = text;
		this.attrs = new ArrayList<AttributeSet>();
		this.locs  = new ArrayList<Location>();
	}
 
	public void addAttributes(AttributeSet atts, int start, int end)
	{
		this.attrs.add(atts);
		this.locs.add(new Location(start, end));
	}
 
	public String toString()
	{
		StringBuilder sb = new StringBuilder("StyledString[");
		for(int j = 0; j < attrs.size(); j++)
		{
			sb.append("Attributes[");
			Enumeration e = attrs.get(j).getAttributeNames();
			while(e.hasMoreElements())
			{
				Object key = e.nextElement();
				Object value = attrs.get(j).getAttribute(key);
				sb.append("key:" + key + ", value:" + value + ";");
			}
			sb.append("]");
			sb.append(" for " + locs.get(j));
			if(j < attrs.size() -1)
				sb.append("\n");
		}
		return sb.toString();
	}
}
