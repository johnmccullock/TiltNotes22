package main;

import java.util.ArrayList;

// Strip the formatting tags, leaving only the text.
public class ConvertXMLToTXT
{
	public static String getDoc(String xml)
	{
		String textDoc = new String();
		ArrayList<String> pars = ConvertXMLToTXT.retrieveStringOrderedArrayList(xml, "par");
		for(int i = 0; i < pars.size(); i++)
		{
			textDoc += ConvertXMLToTXT.getPar(textDoc, pars.get(i));
		}
		return textDoc;
	}
	
	public static String getPar(String textDoc, String par)
	{
		String value = "";
		ArrayList<String> texts = ConvertXMLToTXT.retrieveStringOrderedArrayList(par, "text");
		for(int i = 0; i < texts.size(); i++)
		{
			value += ConvertXMLToTXT.getText(textDoc, texts.get(i));
		}
		
		return value;
	}
	
	public static String getText(String textDoc, String text)
	{
		String value = "";
		if(text != null){
			value = ConvertXMLToTXT.retrieveSection(text, "text");
			value = ConvertXMLToTXT.unescapeForXML(value);
		}
		return value;
	}
		
	public static String retrieveSection(String text, String name)
	{
		String section = null;
		
		int start = text.indexOf("<" + name, 0);
		int end = text.indexOf("</" + name, start);
		if(end > start){
			String temp = text.substring(start, end);
			start = temp.indexOf(">") + 1;
			if(start >= 0){
				section = temp.substring(start, temp.length());
			}
		}
		
		return section;
	}
	
	public static ArrayList<String> retrieveStringOrderedArrayList(String text, String tag)
	{
		ArrayList<String> array = new ArrayList<String>();
		boolean done = false;
		int start = 0;
		int end = 0;
		while(!done)
		{
			try{
				String node = null;
				start = text.indexOf("<" + tag, start);
				end = text.indexOf("</" + tag + ">", start);
				node = text.substring(start, end + tag.length() + 3);
				if(node != null){
					array.add(node);
					start = end;
				}else{
					done = true;
				}
			}catch(Exception ex){
				done = true;
			}
		}
		return array;
	}
	
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
	
	public static String unescapeForXML(String src)
	{
		final StringBuilder res = new StringBuilder(src);
		int i = res.indexOf("&");
		while(i >= 0) {
			String s = res.substring(i);
			if(s.startsWith("<")){
				res.replace(i, i + 4, "<");
				res.replace(i, i + 4, ">");
			}else if(s.startsWith("&amp;")){
				res.replace(i, i + 5, " ");
			}else if(s.startsWith("&qout;")){
				res.replace(i, i + 6, "\"");
			}else if(s.startsWith("&#")){
				int charEnd = res.indexOf(";", i);
				if (charEnd >= 0){
					String cStr = res.substring(i + 2, charEnd);
					char c = (char)Integer.parseInt(cStr);
					res.replace(i, charEnd + 1, " ");
				}
			}
 
			i = res.indexOf("&", i + 1);
		}
		return res.toString();
	}
}
