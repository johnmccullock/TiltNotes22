package main.gui.text;

public class Location
{
	public int start;
	public int length;
 
	public Location(int start, int end)
	{
		this.start = start;
		this.length = end - start;
	}
 
	public String toString()
	{
		return "Location[start:" + this.start + ", length:" + this.length + "]";
	}
}
