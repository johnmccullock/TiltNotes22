package util;

import java.util.ArrayList;
import java.util.Hashtable;

/**
 * Yeah. It's quick and dirty, but it works fine.
 * @author John McCullock
 * @version 2.2 2013-10-22
 */
public class FauxXMLTool
{
	public FauxXMLTool() { return; }
	
	/**
	 * Retrieves string contents between a beginning <> and ending </> tag.
	 * Searches for Name only.
	 * Ex.: <tagName></tagName>
	 * Grabs only first instance of target tags encountered.
	 * @param text - a String to search.
	 * @param name - the name of the tag.
	 * @return String.
	 */
	public static String retrieveSection(String text, String name)
	{
		String section = null;
		int start = text.indexOf("<" + name + ">", 0);
		if(start >= 0){
			int end = text.indexOf("</" + name + ">", start);
			if(end >= 0 && end > start){
				section = text.substring(start + ("<" + name + ">").length(), end);
			}
		}
		return section;
	}
	
	/**
	 * Retrieves string contents between a beginning <> and ending </> tag.
	 * Searches for Name, Attribute Name and Attribute Value.
	 * Ex.: <tagName attributeName="" attributeValue=""></tagName>
	 * Grabs only first instance of target tags encountered.
	 * @param text - String to search.
	 * @param name - the name of the tag.
	 * @param attrName - the name of the attribute name (attributeName as in above example); usually "name"
	 * @param attrValue - the name of the attribute value (attributeValue in above example).
	 * @return String.
	 */
	public static String retrieveSection(String text, String name, String attrName, String attrValue)
	{
		String section = null;
		int start = 0;
		int end = 0;
		boolean done = false;
		while(!done)
		{
			start = text.indexOf("<" + name, start);
			if(start > -1){
				end = text.indexOf(">", start + 1);
				if(end > -1){
					String temp = text.substring(start, end);
					if(temp.contains(attrName + "=\"" + attrValue + "\"")){
						end = text.indexOf("</" + name + ">", start + 1);
						section = text.substring(start, end + ("</" + name + ">").length());
						done = true;
					}
				}else{
					// error.  Return null.
					done = true;
				}
			}else{
				// no match found.  Return null.
				done = true;
			}
			start = end;
		}
		
		return section;
	}
	
	/**
	 * Retrieves the attribute value from a tag.
	 * Searches for Name, Attribute Name and Attribute Value.
	 * Ex.: <tagName attributeName="this" attributeValue="that"></tagName> would return "that".
	 * @param text - String to search.
	 * @param name - the name of the tag.
	 * @param attrName - the name of any attribute within the tag
	 * @return String.
	 */
	public static String retrieveValue(String text, String name, String attrName)
	{
		String value = null;
		int start = 0;
		int end = 0;
		boolean done = false;
		while(!done)
		{
			start = text.indexOf("<" + name, start);
			if(start > -1){
				//end = text.indexOf("><", start + 1);
				end = text.indexOf(">", start + 1);
				if(end > -1){
					String temp = text.substring(start, end + 1);
					if(temp.contains(attrName + "=\"")){
						start = temp.indexOf(attrName) + (attrName + "=\"").length();
						if(temp.indexOf("\" ", start) == -1){
							if(temp.contains("=\">\"")){	// the actual VALUE is ">".
								end = start + 1;
							}else{
								end = temp.indexOf("\">", start); // otherwise, just an end-delimiter.
							}
						}else{
							end = temp.indexOf("\" ", start);
						}
						if(start > -1 && end > -1){
							value = temp.substring(start, end);
						}
						done = true;
					}
				}else{
					// error.  Return null.
					done = true;
				}
			}else{
				// no match found.  Return null.
				done = true;
			}
			start = end;
		}
		return value;
	}
	
	/**
	 * Uses the "split" function on a string, and places the tokens into an ArrayList.
	 * Ex.: A whole text like this:
	 * 		<item attrName="this" attrValue="that 1"></item>
	 * 		<item attrName="this" attrValue="that 2"></item>
	 * 		<item attrName="this" attrValue="that 3"></item>
	 * 		<item attrName="this" attrValue="that 4"></item>
	 * would be split into an array of tag Strings that look like:
	 * 		'<item attrName="this" attrValue="that #"><'
	 * In this example, the delimiter would be "item", and the split partitions by end tag: "/item>"
	 * @param text - String to search.
	 * @param delimiter - string for the split function to use.  In this context, it's usually a tag name.
	 * @param attrName1 - the name of any attribute within the tag.
	 * @return ArrayList<String>
	 */
	public static ArrayList<String> retrieveStringArrayList(String text, String delimiter, String attrName1)
	{
		ArrayList<String> array = new ArrayList<String>();
		String[] tags = text.split("/" + delimiter + ">");
		for(String tag : tags)
		{
			String value1 = FauxXMLTool.retrieveValue(tag, delimiter, attrName1);
			if(value1 != null){
				array.add(value1);
			}
		}
		return array;
	}
	
	/**
	 * Similar to retrieveStringArrayList() except is fills the ArrayList according to the order of split array.
	 * This function does not use any sorting algorithm.
	 * Ex.: A whole text like this:
	 * 		<item attrName="this" attrValue="that 1"></item>
	 * 		<item attrName="this" attrValue="that 2"></item>
	 * 		<item attrName="this" attrValue="that 3"></item>
	 * 		<item attrName="this" attrValue="that 4"></item>
	 * would be split into an array of tag Strings that look like:
	 * 		'<item attrName="this" attrValue="that #"><'
	 * In this example, the delimiter would be "item", and the split partitions by end tag: "/item>"
	 * @param text - String to search.
	 * @param delimiter - string for the split function to use.  In this context, it's usually a tag name.
	 * @param attrName1 - the name of any attribute within the tag.
	 * @return ArrayList<String>
	 */
	public static ArrayList<String> retrieveStringOrderedArrayList(String text, String delimiter, String attrName1)
	{
		ArrayList<String> array = new ArrayList<String>();
		String[] tags = text.split("/" + delimiter + ">");
		for(int i = 0; i < tags.length; i++)
		{
			String value1 = FauxXMLTool.retrieveValue(tags[i], delimiter, attrName1);
			if(value1 != null){
				array.add(value1);
			}
		}
		return array;
	}
	
	/**
	 * Retrieves key-value pairs (i.e.: name-value pairs) from multiple tags, and uses them to create a Hashtable.
	 * Searches for Name, Attribute Name and Attribute Value name.
	 * This function has limited use, in that it was intended for tags that have numbers for attribute names, 
	 * and "yes" or "no" as attribute values.
	 * Ex.: <item recordID="25" value="yes"></item>
	 * In this example, the delimiter would be "item", and the split partitions by end tag: "/item>"
	 * @param text - String to search.
	 * @param delimiter - string for the split function to use.  In this context, it's usually a tag name.
	 * @param attrName1 - the name of the first attribute within the tag.  "recordID" in above example.
	 * @param attrName2 - the name of the second attribute within the tag.  "value" in above example.
	 * @return Hashtable<Integer, Boolean>
	 */
	public static Hashtable<Integer, Boolean> retrieveIntegerTable(String text, String delimiter, String attrName1, String attrName2)
	{
		Hashtable<Integer, Boolean> table = new Hashtable<Integer, Boolean>();

		String[] items = text.split("/" + delimiter + ">");
		for(String item : items)
		{
			String value1 = FauxXMLTool.retrieveValue(item, delimiter, attrName1);
			String value2 = FauxXMLTool.retrieveValue(item, delimiter, attrName2);
			if(value1 != null && value2 != null){
				int id = 0;
				if(value1.length() > 0){
					id = Integer.parseInt(value1);
					if(id < 1){
						id = 0;
					}
				}
				boolean flag = false;
				if(value2.compareToIgnoreCase("yes") == 0){
					flag = true;
				}
				table.put(id, flag);
			}
		}
		
		return table;
	}
	
	/**
	 * Similar to the <Integer, Boolean> version of retrieveIntegerTable(), but returns a Hashtable<String, Boolean>.
	 * Searches for Name, Attribute Name and Attribute Value name.
	 * This function has limited use, in that it was intended for tags that have Strings for attribute names, 
	 * and "yes" or "no" as attribute values.
	 * Ex.: <item name="enabled" value="yes"></item>
	 * In this example, the delimiter would be "item", and the split partitions by end tag: "/item>"
	 * @param text - String to search.
	 * @param delimiter - string for the split function to use.  In this context, it's usually a tag name.
	 * @param attrName1 - the name of the first attribute within the tag.  "name" in above example.
	 * @param attrName2 - the name of the second attribute within the tag.  "value" in above example.
	 * @return Hashtable<String, Boolean>
	 */
	public static Hashtable<String, Boolean> retrieveStringTable(String text, String delimiter, String attrName1, String attrName2)
	{
		Hashtable<String, Boolean> table = new Hashtable<String, Boolean>();

		String[] items = text.split("/" + delimiter + ">");
		for(String item : items)
		{
			String value1 = FauxXMLTool.retrieveValue(item, delimiter, attrName1);
			String value2 = FauxXMLTool.retrieveValue(item, delimiter, attrName2);
			if(value1 != null && value2 != null){
				boolean excluded = false;
				if(value2.compareToIgnoreCase("yes") == 0){
					excluded = true;
				}
				table.put(value1, excluded);
			}
		}
		
		return table;
	}
	
	public static Hashtable<String, String> retrieveStringStringTable(String text, String delimiter, String attrName1, String attrName2)
	{
		Hashtable<String, String> table = new Hashtable<String, String>();

		String[] items = text.split("/" + delimiter + ">");
		for(String item : items)
		{
			String value1 = FauxXMLTool.retrieveValue(item, delimiter, attrName1);
			String value2 = FauxXMLTool.retrieveValue(item, delimiter, attrName2);
			if(value1 != null && value2 != null){
				table.put(value1, value2);
			}
		}
		
		return table;
	}
	
	/**
	 * Uses the "split" function on a string, and places the tokens into a Hashtable<Integer, String> where the
	 * split arrays indexes are the keys, and the tokens are the values.
	 * Ex.: A whole text like this:
	 * 		<item attrName="this" attrValue="that 1"></item>
	 * 		<item attrName="this" attrValue="that 2"></item>
	 * 		<item attrName="this" attrValue="that 3"></item>
	 * 		<item attrName="this" attrValue="that 4"></item>
	 * would be split into an array of tag Strings that look like:
	 * 		'<item attrName="this" attrValue="that #"><'
	 * In this example, the delimiter would be "item", and the split partitions by end tag: "/item>"
	 * @param text - String to search.
	 * @param delimiter - string for the split function to use.  In this context, it's usually a tag name.
	 * @param attrName1 - the name of the first attribute within the tag.  "attrName" in above example.
	 * @return Hashtable<Integer, String>
	 */
	public static Hashtable<Integer, String> retrieveStringOrderedTable(String text, String delimiter, String attrName1)
	{
		Hashtable<Integer, String> list = new Hashtable<Integer, String>();
		String[] sessions = text.split("/" + delimiter + ">");
		for(int i = 0; i < sessions.length; i++)
		{
			String value1 = FauxXMLTool.retrieveValue(sessions[i], delimiter, attrName1);
			if(value1 != null){
				list.put(i, value1);
			}
		}
		return list;
	}
	
	public String toString()
	{
		return new String("FauxXMLTool.  String functions that behave somewhat, kind of, sort of like, XML parser-ish on actual XML content.");
	}
}
