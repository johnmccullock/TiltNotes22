package main;

import dialogs.Emoticon;
import util.ImageLoader;

import java.awt.Color;

import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

/**
 * Translates XML markup created by the ConvertRTFToXML methods, back into document elements.
 * 
 * Adapted from code by Stanislav Lapitsky, found at: http://java-sl.com/editor_kit_tutorial_example.html.
 * 
 * @author John McCullock
 * @version 1.0, 2013-08-01, new to program version 2.x
 * @see main.ExtStyledDocument for XML naming constants.
 */
public class ConvertXMLToRTF
{
	// pos = where in the current document the input text should be inserted.
	public static void writeDoc(StyledDocument doc, String xml, int pos)
	{
		try{
			ArrayList<String> pars = ConvertXMLToRTF.retrieveStringOrderedArrayList(xml, "par");
			int offset = pos;
			for(int i = 0; i < pars.size(); i++)
			{
				offset += ConvertXMLToRTF.writePar(doc, offset, pars.get(i));
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return;
	}
	
	public static int writePar(StyledDocument doc, int pos, String par) throws BadLocationException
	{
		ArrayList<String> texts = ConvertXMLToRTF.retrieveStringOrderedArrayList(par, "text");
		int len = 0;
		for(int i = 0; i < texts.size(); i++)
		{
			len += ConvertXMLToRTF.writeText(doc, pos + len, texts.get(i));
		}
		doc.setParagraphAttributes(pos + len - 1, 1, ConvertXMLToRTF.getParagraphAttributes(par, "par"), true);
		return len;
	}
	
	public static int writeText(StyledDocument doc, int pos, String text) throws BadLocationException
	{
		int len = 0;
		
		if(text != null){
			String value = ConvertXMLToRTF.retrieveSection(text, "text");
			value = ConvertXMLToRTF.unescapeForXML(value);
			doc.insertString(pos, value, ConvertXMLToRTF.getCharacterAttributes(text, "text"));
			len = value.length();
		}else{
			len = 0;
		}
		
		return len;
	}
	
	public static SimpleAttributeSet getParagraphAttributes(String par, String tagName)
	{
		SimpleAttributeSet sas = new SimpleAttributeSet();
		
		String value = ConvertXMLToRTF.retrieveValue(par, tagName, ExtStyledDocument.ATTR_NAME_ALIGN);
		StyleConstants.setAlignment(sas, Integer.parseInt(value));
		value = ConvertXMLToRTF.retrieveValue(par, tagName, ExtStyledDocument.ATTR_NAME_ABOVE);
		StyleConstants.setSpaceAbove(sas, Float.parseFloat(value));
		value = ConvertXMLToRTF.retrieveValue(par, tagName, ExtStyledDocument.ATTR_NAME_BELOW);
		StyleConstants.setSpaceBelow(sas, Float.parseFloat(value));
		value = ConvertXMLToRTF.retrieveValue(par, tagName, ExtStyledDocument.ATTR_NAME_LEFT);
		StyleConstants.setLeftIndent(sas, Float.parseFloat(value));
		value = ConvertXMLToRTF.retrieveValue(par, tagName, ExtStyledDocument.ATTR_NAME_RIGHT);
		StyleConstants.setRightIndent(sas, Float.parseFloat(value));
		value = ConvertXMLToRTF.retrieveValue(par, tagName, ExtStyledDocument.ATTR_NAME_LINE_SPACING);
		StyleConstants.setLineSpacing(sas, Float.parseFloat(value));
		
		return sas;
	}
	
	public static SimpleAttributeSet getCharacterAttributes(String text, String tagName)
	{
		SimpleAttributeSet sas = new SimpleAttributeSet();
		int red = 0;
		int green = 0;
		int blue = 0;
		
		String value = ConvertXMLToRTF.retrieveValue(text, tagName, ExtStyledDocument.ATTR_NAME_ICON);
		if(value != null && !value.isEmpty()){
			try{
				Emoticon em = new Emoticon(new ImageIcon(ImageLoader.getImageFromResourcePath(ConvertXMLToRTF.class.getClass().getResource(value))).getImage(), value);
				StyleConstants.setIcon(sas, em);
				
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
		
		value = ConvertXMLToRTF.retrieveValue(text, tagName, ExtStyledDocument.ATTR_NAME_FONT_SIZE);
		StyleConstants.setFontSize(sas, Integer.parseInt(value));
		value = ConvertXMLToRTF.retrieveValue(text, tagName, ExtStyledDocument.ATTR_NAME_FONT_FAMILY);
		StyleConstants.setFontFamily(sas, value);
		value = ConvertXMLToRTF.retrieveValue(text, tagName, ExtStyledDocument.ATTR_NAME_BOLD);
		StyleConstants.setBold(sas, Boolean.parseBoolean(value));
		value = ConvertXMLToRTF.retrieveValue(text, tagName, ExtStyledDocument.ATTR_NAME_ITALIC);
		StyleConstants.setItalic(sas, Boolean.parseBoolean(value));
		value = ConvertXMLToRTF.retrieveValue(text, tagName, ExtStyledDocument.ATTR_NAME_UNDERLINE);
		StyleConstants.setUnderline(sas, Boolean.parseBoolean(value));
		try{
			value = ConvertXMLToRTF.retrieveValue(text, tagName, ExtStyledDocument.ATTR_NAME_FONT_COLOR_RED);
			red = value != null ? Integer.parseInt(value) : 0;
			value = ConvertXMLToRTF.retrieveValue(text, tagName, ExtStyledDocument.ATTR_NAME_FONT_COLOR_GREEN);
			green = value != null ? Integer.parseInt(value) : 0;
			value = ConvertXMLToRTF.retrieveValue(text, tagName, ExtStyledDocument.ATTR_NAME_FONT_COLOR_BLUE);
			blue = value != null ? Integer.parseInt(value) : 0;
			StyleConstants.setForeground(sas, new Color(red, green, blue));
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return sas;
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
				res.replace(i, i + 5, "&");
			}else if(s.startsWith("&qout;")){
				res.replace(i, i + 6, "\"");
			}else if(s.startsWith("&#")){
				int charEnd = res.indexOf(";", i);
				if (charEnd >= 0){
					String cStr = res.substring(i + 2, charEnd);
					char c = (char)Integer.parseInt(cStr);
					res.replace(i, charEnd + 1, c + "");
				}
			}
 
			i = res.indexOf("&", i + 1);
		}
		return res.toString();
	}
	
	/**
	 * A convenience method for converting a UTF8-encoded array of bytes back into String form. 
	 * @param bytes array of bytes encoded with UTF8 Unicode format.
	 * @return String form of the byte array.
	 * @throws Exception a NullPointerException if a null or empty byte array is used for input.
	 */
	public static String bytes2String(byte[] bytes) throws Exception
	{
		StringBuffer sb = new StringBuffer();
		try{
			for(int i = 0; i < bytes.length; i++)
			{
				sb.append((char)bytes[i]);
			}
		}catch(Exception ex){
			throw ex;
		}
		return sb.toString();
	}
}
