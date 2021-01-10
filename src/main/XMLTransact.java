package main;

import config.ConfigData;
import util.FauxXMLTool;

import java.awt.Color;

import java.util.ArrayList;
import java.util.TreeMap;
import java.util.UUID;

/**
 * This class contains static methods for XML functions specific to this application, wrapping the
 * more general function of the FauxXMLTool class.
 * 
 * The "fallback" arguments are arbitrary default values used if a function fails to retrieve the
 * targeted value from the XML markup.
 * 
 * @author John McCullock
 * @version 2.0 2013-07-28
 *
 */
public class XMLTransact
{
	public static enum MessageType{SESSION, DELETION, CENSUS, SHUTDOWN, UNKNOWN};
	public static enum DeletionType{ONLY_HERE, EVERYWHERE, EVERYWHERE_BUT_HERE, EXPIRED};
	
	public static String retrieveStringFromXML(final String xml, final String tag, final String fallback)
	{
		String value = null;
		
		String innerXML = FauxXMLTool.retrieveSection(xml, tag);
		try{
			if(innerXML == null || innerXML.isEmpty()){
				throw new Exception(tag + " value failed to read from XML source.");
			}
			value = innerXML;
		}catch(Exception ex){
			value = fallback;
			System.out.println(ex.getMessage());
		}
		return value;
	}
	
	public static int retrieveIntegerFromXML(final String xml, final String tag, final int fallback)
	{
		int value = -1;
		
		String innerXML = FauxXMLTool.retrieveSection(xml, tag);
		try{
			value = Integer.parseInt(innerXML);
		}catch(Exception ex){
			value = fallback;
			System.out.println(tag + " value failed to read from XML source");
		}
		
		return value;
	}
	
	public static double retrieveDoubleFromXML(final String xml, final String tag, final double fallback)
	{
		double value = -1.0;
		
		String innerXML = FauxXMLTool.retrieveSection(xml, tag);
		try{
			value = Double.parseDouble(innerXML);
		}catch(Exception ex){
			value = fallback;
			System.out.println(tag + " value failed to read from XML source");
		}
		
		return value;
	}
	
	public static boolean retrieveBooleanFromXML(final String xml, final String tag, final boolean fallback)
	{
		boolean flag = false;
		
		String innerXML = FauxXMLTool.retrieveSection(xml, tag);
		try{
			flag = Boolean.parseBoolean(innerXML);
		}catch(Exception ex){
			flag = fallback;
			System.out.println(tag + " value failed to read from XML source");
		}
		
		return flag;
	}
	
	public static Color retrieveColorFromXML(final String xml, final String tag, final Color fallback)
	{
		Color aColor = null;
		String data = "";
		int red = 0;
		int green = 0;
		int blue = 0;
		
		String innerXML = FauxXMLTool.retrieveSection(xml, tag);
		boolean successful = true;
		if(successful){
			data = FauxXMLTool.retrieveSection(innerXML, "red");
			try{
				red = Integer.parseInt(data);
			}catch(Exception ex){
				successful = false;
				System.out.println("XMLTransact.retrieveColorFromXML " + tag + "(red) value failed to read from XML source");
			}
		}
		if(successful){
			data = FauxXMLTool.retrieveSection(innerXML, "green");
			try{
				green = Integer.parseInt(data);
			}catch(Exception ex){
				successful = false;
				System.out.println("XMLTransact.retrieveColorFromXML " + tag + "(green) value failed to read from XML source");
			}
		}
		if(successful){
			data = FauxXMLTool.retrieveSection(innerXML, "blue");
			try{
				blue = Integer.parseInt(data);
			}catch(Exception ex){
				successful = false;
				System.out.println("XMLTransact.retrieveColorFromXML " + tag + "(blue) value failed to read from XML source");
			}
		}
		try{
			if(successful){
				aColor = new Color(red, green, blue, 255);
			}else{
				throw new Exception("XMLTransact.retrieveColorFromXML " + tag + " values failed to read from XML source");
			}
		}catch(Exception ex){
			aColor = fallback;
			System.out.println("XMLTransact.retrieveColorFromXML " + tag + " values failed to read from XML source");
		}
		
		return aColor;
	}
	
	public static SessionPriority retrievePriorityFromXML(final String xml, final String tag, final SessionPriority fallback)
	{
		SessionPriority priority = null;
		
		String innerXML = FauxXMLTool.retrieveSection(xml, tag);
		try{
			if(innerXML.compareToIgnoreCase(SessionPriority.NORMAL.name()) == 0){
				priority = SessionPriority.NORMAL;
			}else if(innerXML.compareToIgnoreCase(SessionPriority.URGENT.name()) == 0){
				priority = SessionPriority.URGENT;
			}else{
				priority = fallback;
			}
		}catch(Exception ex){
			priority = fallback;
			System.out.println(tag + " value failed to read from XML source");
		}
		
		return priority;
	}
	
	public static SessionMode retrieveSessionModeFromXML(final String xml, final String tag, final SessionMode fallback)
	{
		SessionMode sMode = null;
		
		String innerXML = FauxXMLTool.retrieveSection(xml, tag);
		try{
			if(innerXML.compareToIgnoreCase(SessionMode.NORMAL.name()) == 0){
				sMode = SessionMode.NORMAL;
			}else if(innerXML.compareToIgnoreCase(SessionMode.EDIT_TEMPLATE.name()) == 0){
				sMode = SessionMode.EDIT_TEMPLATE;
			}else{
				sMode = fallback;
			}
		}catch(Exception ex){
			sMode = fallback;
			System.out.println(tag + " value failed to read from XML source");
		}
		
		return sMode;
	}
	
	public static TreeMap<String, ArrayList<String>> retrieveRecipientTreeMapFromXML(final String rawText) throws Exception
	{
		TreeMap<String, ArrayList<String>> recipients = new TreeMap<String, ArrayList<String>>();
		
		try{
			String rootText = FauxXMLTool.retrieveSection(rawText, "root");
			String networkText = FauxXMLTool.retrieveSection(rootText, "_network");
			ArrayList<String> network = FauxXMLTool.retrieveStringOrderedArrayList(networkText, "recipient", "name");
			recipients.put("_network", network);
			
			String listsText = FauxXMLTool.retrieveSection(rootText, "lists");
			String[] listStrings = listsText.split("</list>");
			for(String listString : listStrings)
			{
				String listName = FauxXMLTool.retrieveValue(listString, "list", "name");
				if(listName == null){
					break;
				}
				ArrayList<String> listArray = FauxXMLTool.retrieveStringOrderedArrayList(listString, "recipient", "name");
				recipients.put(listName, listArray);
			}
		}catch(Exception ex){
			throw ex;
		}
		
		return recipients;
	}
	
	public static String parseRecipientTreeMapToXML(TreeMap<String, ArrayList<String>> recipients)
	{
		StringBuilder xml = new StringBuilder();
		
		xml.append("<root>\n");
		
		xml.append("\t<_network>\n");
		for(String name : ConfigData.getRecipients("_network"))
		{
			xml.append("\t\t<recipient name=\"" + name + "\"></recipient>\n");
		}
		xml.append("\t</_network>\n");
		
		xml.append("\t<lists>\n");
		for(String listName : ConfigData.getRecipientLists())
		{
			if(listName.compareToIgnoreCase("_network") == 0) { continue; }
			xml.append("\t\t<list name=\"" + listName + "\">\n");
			for(String name : ConfigData.getRecipients(listName))
			{
				xml.append("\t\t\t<recipient name=\"" + name + "\"></recipient>\n");
			}
			xml.append("\t\t</list>\n");
		}
		xml.append("\t</lists>\n");
		
		xml.append("</root>\n");
		
		return xml.toString();
	}
	
	// Checks if String input is a Tilt Notes message.
	// Returns enum value associated with message_type tag contents.
	// Returns enum value "unknown" if fails. 
	public static XMLTransact.MessageType getMessageType(final String xml)
	{
		XMLTransact.MessageType msgType = XMLTransact.MessageType.UNKNOWN;
		
		String root = FauxXMLTool.retrieveSection(xml, "tilt_notes");
		if(root != null && !root.isEmpty()){
			String type = FauxXMLTool.retrieveSection(root, "message_type");
			if(type != null && !type.isEmpty()){
				XMLTransact.MessageType[] types = XMLTransact.MessageType.values();
				for(XMLTransact.MessageType aType : types)
				{
					if(type.compareToIgnoreCase(aType.name()) == 0){
						msgType = aType;
						break;
					}
				}
			}
		}
		return msgType;
	}
	
	public static String parseSessionDataToXML(final SessionData sData, final long updateTime)
	{
		StringBuilder xml = new StringBuilder();
		xml.append("<tilt_notes>");
		xml.append("<message_type>" + XMLTransact.MessageType.SESSION.name() + "</message_type>");
		xml.append("<session>");
		xml.append("<head>");
		xml.append("<id>" + sData.getID().toString() + "</id>");
		//xml += "<modified>" + String.valueOf(sData.getModified()) + "</modified>";
		// Actually, you'll want the time of broadcast.  That way, replies will keep the
		// session alive.
		xml.append("<modified>" + String.valueOf(updateTime) + "</modified>");
		
		xml.append("<note_color>");
		xml.append("<red>" + String.valueOf(sData.getNoteColorRed()) + "</red>");
		xml.append("<green>" + String.valueOf(sData.getNoteColorGreen()) + "</green>");
		xml.append("<blue>" + String.valueOf(sData.getNoteColorBlue()) + "</blue>");
		xml.append("</note_color>");
		
		xml.append("<title_color>");
		xml.append("<red>" + String.valueOf(sData.getTitleColorRed()) + "</red>");
		xml.append("<green>" + String.valueOf(sData.getTitleColorGreen()) + "</green>");
		xml.append("<blue>" + String.valueOf(sData.getTitleColorBlue()) + "</blue>");
		xml.append("</title_color>");
		
		xml.append("<border_color>");
		xml.append("<red>" + String.valueOf(sData.getBorderColorRed()) + "</red>");
		xml.append("<green>" + String.valueOf(sData.getBorderColorGreen()) + "</green>");
		xml.append("<blue>" + String.valueOf(sData.getBorderColorBlue()) + "</blue>");
		xml.append("</border_color>");
		xml.append("<border_width>" + String.valueOf(sData.getBorderWidth()) + "</border_width>");
		
		xml.append("<note_font>");
		xml.append("<face>" + sData.getNoteFontFace() + "</face>");
		xml.append("<size>" + String.valueOf(sData.getNoteFontSize()) + "</size>");
		xml.append("<bold>" + String.valueOf(sData.getNoteFontBold()) + "</bold>");
		xml.append("<italic>" + String.valueOf(sData.getNoteFontItalic()) + "</italic>");
		xml.append("<underline>" + String.valueOf(sData.getNoteFontUnderline()) + "</underline>");
		xml.append("</note_font>");
		
		xml.append("<title_font>");
		xml.append("<face>" + sData.getTitleFontFace() + "</face>");
		xml.append("<size>" + String.valueOf(sData.getTitleFontSize()) + "</size>");
		xml.append("<bold>" + String.valueOf(sData.getTitleFontBold()) + "</bold>");
		xml.append("<italic>" + String.valueOf(sData.getTitleFontItalic()) + "</italic>");
		xml.append("</title_font>");
		
		xml.append("<note_font_color>");
		xml.append("<red>" + String.valueOf(sData.getNoteFontColorRed()) + "</red>");
		xml.append("<green>" + String.valueOf(sData.getNoteFontColorGreen()) + "</green>");
		xml.append("<blue>" + String.valueOf(sData.getNoteFontColorBlue()) + "</blue>");
		xml.append("</note_font_color>");
		
		xml.append("<title_font_color>");
		xml.append("<red>" + String.valueOf(sData.getTitleFontColorRed()) + "</red>");
		xml.append("<green>" + String.valueOf(sData.getTitleFontColorGreen()) + "</green>");
		xml.append("<blue>" + String.valueOf(sData.getTitleFontColorBlue()) + "</blue>");
		xml.append("</title_font_color>");
		
		xml.append("<note_width>" + String.valueOf(sData.getNoteWidth()) + "</note_width>");
		xml.append("<note_height>" + String.valueOf(sData.getNoteHeight()) + "</note_height>");
		
		xml.append("<custom_title>" + sData.getCustomTitle() + "</custom_title>");
		
		xml.append("<recipients>");
		for(String r : sData.getRecipients())
		{
			xml.append("<recipient value=\"" + r + "\"></recipient>");
		}
		xml.append("</recipients>");
		
		xml.append("<session_mode>" + sData.getSessionMode().name() + "</session_mode>");
		
		xml.append("<session_priority>" + sData.getSessionPriority().name() + "</session_priority>");
		
		xml.append("</head>");
		xml.append("<body>");
		for(Entry e : sData.getEntries())
		{
			xml.append("<entry source=\"" + e.source + "\" value=\"" + e.text + "\"></entry>");
		}
		xml.append("</body>");
		xml.append("</session>");
		xml.append("</tilt_notes>");
		return xml.toString();
	}
	
	public static SessionData parseXMLToSessionData(final String xml)
	{
		SessionData sData = null;
		try{
			String sessionText = FauxXMLTool.retrieveSection(xml, "session");
			String headSectionText = FauxXMLTool.retrieveSection(sessionText, "head");
			String idText = FauxXMLTool.retrieveSection(headSectionText, "id");
			UUID id = UUID.fromString(idText);
			String modifiedText = FauxXMLTool.retrieveSection(headSectionText, "modified");
			Long modified = Long.valueOf(modifiedText);
			
			String recipientsSection = FauxXMLTool.retrieveSection(headSectionText, "recipients");
			ArrayList<String> recipients = FauxXMLTool.retrieveStringOrderedArrayList(recipientsSection, "recipient", "value");
			String bodySection = FauxXMLTool.retrieveSection(sessionText, "body");
			ArrayList<String> sources = FauxXMLTool.retrieveStringOrderedArrayList(bodySection, "entry", "source");
			ArrayList<String> entryLines = FauxXMLTool.retrieveStringOrderedArrayList(bodySection, "entry", "value");
			ArrayList<Entry> entries = new ArrayList<Entry>();
			for(int i = 0; i < sources.size(); i++)
			{
				entries.add(new Entry(sources.get(i), entryLines.get(i)));
			}
			
			sData = new SessionData(id, modified, recipients, entries);
			String colorText = FauxXMLTool.retrieveSection(headSectionText, "note_color");
			sData.setNoteColorRed(XMLTransact.retrieveIntegerFromXML(colorText, "red", Globals.UNCHOSEN_NOTE_COLOR_RED));
			sData.setNoteColorGreen(XMLTransact.retrieveIntegerFromXML(colorText, "green", Globals.UNCHOSEN_NOTE_COLOR_GREEN));
			sData.setNoteColorBlue(XMLTransact.retrieveIntegerFromXML(colorText, "blue", Globals.UNCHOSEN_NOTE_COLOR_BLUE));
			
			colorText = FauxXMLTool.retrieveSection(headSectionText, "title_color");
			sData.setTitleColorRed(XMLTransact.retrieveIntegerFromXML(colorText, "red", Globals.UNCHOSEN_TITLE_COLOR_RED));
			sData.setTitleColorGreen(XMLTransact.retrieveIntegerFromXML(colorText, "green", Globals.UNCHOSEN_TITLE_COLOR_GREEN));
			sData.setTitleColorBlue(XMLTransact.retrieveIntegerFromXML(colorText, "blue", Globals.UNCHOSEN_TITLE_COLOR_BLUE));
			
			colorText = FauxXMLTool.retrieveSection(headSectionText, "border_color");
			sData.setBorderColorRed(XMLTransact.retrieveIntegerFromXML(colorText, "red", Globals.UNCHOSEN_BORDER_COLOR_RED));
			sData.setBorderColorGreen(XMLTransact.retrieveIntegerFromXML(colorText, "green", Globals.UNCHOSEN_BORDER_COLOR_GREEN));
			sData.setBorderColorBlue(XMLTransact.retrieveIntegerFromXML(colorText, "blue", Globals.UNCHOSEN_BORDER_COLOR_BLUE));
			sData.setBorderWidth(XMLTransact.retrieveIntegerFromXML(headSectionText, "border_width", Globals.UNCHOSEN_BORDER_WIDTH));
			
			String fontText = FauxXMLTool.retrieveSection(headSectionText, "note_font");
			sData.setNoteFontFace(XMLTransact.retrieveStringFromXML(fontText, "face", Globals.UNCHOSEN_NOTE_FONT_FACE));
			sData.setNoteFontSize(XMLTransact.retrieveIntegerFromXML(fontText, "size", Globals.UNCHOSEN_NOTE_FONT_SIZE));
			sData.setNoteFontBold(XMLTransact.retrieveBooleanFromXML(fontText, "bold", Globals.UNCHOSEN_NOTE_FONT_BOLD));
			sData.setNoteFontItalic(XMLTransact.retrieveBooleanFromXML(fontText, "italic", Globals.UNCHOSEN_NOTE_FONT_ITALIC));
			sData.setNoteFontUnderline(XMLTransact.retrieveBooleanFromXML(fontText, "underline", Globals.UNCHOSEN_NOTE_FONT_UNDERLINE));
			
			fontText = FauxXMLTool.retrieveSection(headSectionText, "title_font");
			sData.setTitleFontFace(XMLTransact.retrieveStringFromXML(fontText, "face", Globals.UNCHOSEN_TITLE_FONT_FACE));
			sData.setTitleFontSize(XMLTransact.retrieveIntegerFromXML(fontText, "size", Globals.UNCHOSEN_TITLE_FONT_SIZE));
			sData.setTitleFontBold(XMLTransact.retrieveBooleanFromXML(fontText, "bold", Globals.UNCHOSEN_TITLE_FONT_BOLD));
			sData.setTitleFontItalic(XMLTransact.retrieveBooleanFromXML(fontText, "italic", Globals.UNCHOSEN_TITLE_FONT_ITALIC));
			
			colorText = FauxXMLTool.retrieveSection(headSectionText, "note_font_color");
			sData.setNoteFontColorRed(XMLTransact.retrieveIntegerFromXML(colorText, "red", Globals.UNCHOSEN_NOTE_FONT_COLOR_RED));
			sData.setNoteFontColorGreen(XMLTransact.retrieveIntegerFromXML(colorText, "green", Globals.UNCHOSEN_NOTE_FONT_COLOR_GREEN));
			sData.setNoteFontColorBlue(XMLTransact.retrieveIntegerFromXML(colorText, "blue", Globals.UNCHOSEN_NOTE_FONT_COLOR_BLUE));
			
			colorText = FauxXMLTool.retrieveSection(headSectionText, "title_font_color");
			sData.setTitleFontColorRed(XMLTransact.retrieveIntegerFromXML(colorText, "red", Globals.UNCHOSEN_TITLE_FONT_COLOR_RED));
			sData.setTitleFontColorGreen(XMLTransact.retrieveIntegerFromXML(colorText, "green", Globals.UNCHOSEN_TITLE_FONT_COLOR_GREEN));
			sData.setTitleFontColorBlue(XMLTransact.retrieveIntegerFromXML(colorText, "blue", Globals.UNCHOSEN_TITLE_FONT_COLOR_BLUE));
			
			sData.setNoteWidth(XMLTransact.retrieveIntegerFromXML(headSectionText, "note_width", Globals.UNCHOSEN_NOTE_WIDTH));
			sData.setNoteHeight(XMLTransact.retrieveIntegerFromXML(headSectionText, "note_height", Globals.UNCHOSEN_NOTE_HEIGHT));
			sData.setCustomTitle(FauxXMLTool.retrieveSection(headSectionText, "custom_title"));
			
			sData.setSessionMode(XMLTransact.retrieveSessionModeFromXML(headSectionText, "session_mode", SessionMode.NORMAL));
			
			sData.setSessionPriority(XMLTransact.retrievePriorityFromXML(headSectionText, "session_priority", SessionPriority.NORMAL));
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		return sData;
	}
	
	public static String parseDeleteRequestToXML(UUID sessionID, XMLTransact.DeletionType type)
	{
		StringBuilder xml = new StringBuilder();
		
		xml.append("<tilt_notes>");
		xml.append("<message_type>" + XMLTransact.MessageType.DELETION.name() + "</message_type>");
		xml.append("<message_source>" + ConfigData.getHostName() + "</message_source>");
		xml.append("<deletion>");
		xml.append("<deletion_type>" + type.name() + "</deletion_type>");
		xml.append("<id>" + sessionID.toString() + "</id>");
		xml.append("</deletion>");
		xml.append("</tilt_notes>");
		
		return xml.toString();
	}
	
	public static XMLTransact.DeletionType retrieveDeletionRequestTypeFromXML(final String xml)
	{
		XMLTransact.DeletionType returnType = null;
		try{
			String deletionText = FauxXMLTool.retrieveSection(xml, "deletion");
			if(deletionText != null && !deletionText.isEmpty()){
				String thatType = FauxXMLTool.retrieveSection(deletionText, "deletion_type");
				if(thatType != null && !thatType.isEmpty()){
					XMLTransact.DeletionType[] types = XMLTransact.DeletionType.values();
					for(XMLTransact.DeletionType thisType : types)
					{
						if(thisType.name().compareToIgnoreCase(thatType) == 0){
							returnType = thisType;
							break;
						}
					}
				}	
			}
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return returnType;
	}
	
	public static UUID retrieveDeletionRequestIDFromXML(final String xml)
	{
		UUID id = null;
		try{
			String deletionText = FauxXMLTool.retrieveSection(xml, "deletion");
			String idText = FauxXMLTool.retrieveSection(deletionText, "id");
			id = UUID.fromString(idText);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return id;
	}
	
	public static String parseCensusMessageToXML(final String name, final String ipAddress)
	{
		StringBuilder xml = new StringBuilder();
		
		xml.append("<tilt_notes>");
		xml.append("<message_type>" + XMLTransact.MessageType.CENSUS.name() + "</message_type>");
		xml.append("<census>");
		xml.append("<name>" + name + "</name>");
		xml.append("<ip>" + ipAddress + "</ip>");
		xml.append("</census>");
		xml.append("</tilt_notes>");
		
		return xml.toString();
	}
	
	public static User retrieveCensusUserFromXML(final String xml)
	{
		User aUser = null;
		try{
			String censusText = FauxXMLTool.retrieveSection(xml, "census");
			String nameText = FauxXMLTool.retrieveSection(censusText, "name");
			String ipText = FauxXMLTool.retrieveSection(censusText, "ip");
			aUser = new User(nameText, ipText);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return aUser;
	}
	
	public static ArrayList<String> parseXMLToEmoticonNames(final String xml)
	{
		String rootText = FauxXMLTool.retrieveSection(xml, "root");
		ArrayList<String> names = FauxXMLTool.retrieveStringOrderedArrayList(rootText, "emoticon", "name");
		return names;
	}
	
	public static TemplateData retrieveTemplateDataFromXML(final String xml)
	{
		TemplateData data = null;
		try{
			String headText = FauxXMLTool.retrieveSection(xml, "head");
			if(headText == null){
				throw new Exception("Head section of template xml is unreadable.");
			}
			String bodyText = FauxXMLTool.retrieveSection(xml, "body");
			if(bodyText == null){
				throw new Exception("Body section of template xml is unreadable.");
			}
			String templateName = FauxXMLTool.retrieveSection(headText, "name");
			if(templateName == null){
				throw new Exception("Unable to read template name.");
			}
			data = new TemplateData();
			
			data.setTemplateName(templateName);
			
			String section = FauxXMLTool.retrieveSection(headText, "note_color");
			data.setNoteColorRed(XMLTransact.retrieveIntegerFromXML(section, "red", Globals.UNCHOSEN_NOTE_COLOR_RED));
			data.setNoteColorGreen(XMLTransact.retrieveIntegerFromXML(section, "green", Globals.UNCHOSEN_NOTE_COLOR_GREEN));
			data.setNoteColorBlue(XMLTransact.retrieveIntegerFromXML(section, "blue", Globals.UNCHOSEN_NOTE_COLOR_BLUE));
			
			section = FauxXMLTool.retrieveSection(headText, "title_color");
			data.setTitleColorRed(XMLTransact.retrieveIntegerFromXML(section, "red", Globals.UNCHOSEN_TITLE_COLOR_RED));
			data.setTitleColorGreen(XMLTransact.retrieveIntegerFromXML(section, "green", Globals.UNCHOSEN_TITLE_COLOR_GREEN));
			data.setTitleColorBlue(XMLTransact.retrieveIntegerFromXML(section, "blue", Globals.UNCHOSEN_TITLE_COLOR_BLUE));
			
			section = FauxXMLTool.retrieveSection(headText, "border_color");
			data.setBorderColorRed(XMLTransact.retrieveIntegerFromXML(section, "red", Globals.UNCHOSEN_BORDER_COLOR_RED));
			data.setBorderColorGreen(XMLTransact.retrieveIntegerFromXML(section, "green", Globals.UNCHOSEN_BORDER_COLOR_GREEN));
			data.setBorderColorBlue(XMLTransact.retrieveIntegerFromXML(section, "blue", Globals.UNCHOSEN_BORDER_COLOR_BLUE));
			
			data.setBorderWidth(XMLTransact.retrieveIntegerFromXML(headText, "border_width", Globals.UNCHOSEN_BORDER_WIDTH));
			
			section = FauxXMLTool.retrieveSection(headText, "note_font");
			data.setNoteFontFace(XMLTransact.retrieveStringFromXML(section, "face", Globals.UNCHOSEN_NOTE_FONT_FACE));
			data.setNoteFontSize(XMLTransact.retrieveIntegerFromXML(section, "size", Globals.UNCHOSEN_NOTE_FONT_SIZE));
			data.setNoteFontBold(XMLTransact.retrieveBooleanFromXML(section, "bold", Globals.UNCHOSEN_NOTE_FONT_BOLD));
			data.setNoteFontItalic(XMLTransact.retrieveBooleanFromXML(section, "italic", Globals.UNCHOSEN_NOTE_FONT_ITALIC));
			data.setNoteFontUnderline(XMLTransact.retrieveBooleanFromXML(section, "underline", Globals.UNCHOSEN_NOTE_FONT_UNDERLINE));
			
			section = FauxXMLTool.retrieveSection(headText, "title_font");
			data.setTitleFontFace(XMLTransact.retrieveStringFromXML(section, "face", Globals.UNCHOSEN_TITLE_FONT_FACE));
			data.setTitleFontSize(XMLTransact.retrieveIntegerFromXML(section, "size", Globals.UNCHOSEN_TITLE_FONT_SIZE));
			data.setTitleFontBold(XMLTransact.retrieveBooleanFromXML(section, "bold", Globals.UNCHOSEN_TITLE_FONT_BOLD));
			data.setTitleFontItalic(XMLTransact.retrieveBooleanFromXML(section, "italic", Globals.UNCHOSEN_TITLE_FONT_ITALIC));
			
			section = FauxXMLTool.retrieveSection(headText, "note_font_color");
			data.setNoteFontColorRed(XMLTransact.retrieveIntegerFromXML(section, "red", Globals.UNCHOSEN_NOTE_FONT_COLOR_RED));
			data.setNoteFontColorGreen(XMLTransact.retrieveIntegerFromXML(section, "green", Globals.UNCHOSEN_NOTE_FONT_COLOR_GREEN));
			data.setNoteFontColorBlue(XMLTransact.retrieveIntegerFromXML(section, "blue", Globals.UNCHOSEN_NOTE_FONT_COLOR_BLUE));
			
			section = FauxXMLTool.retrieveSection(headText, "title_font_color");
			data.setTitleFontColorRed(XMLTransact.retrieveIntegerFromXML(section, "red", Globals.UNCHOSEN_TITLE_FONT_COLOR_RED));
			data.setTitleFontColorGreen(XMLTransact.retrieveIntegerFromXML(section, "green", Globals.UNCHOSEN_TITLE_FONT_COLOR_GREEN));
			data.setTitleFontColorBlue(XMLTransact.retrieveIntegerFromXML(section, "blue", Globals.UNCHOSEN_TITLE_FONT_COLOR_BLUE));
			
			data.setNoteWidth(XMLTransact.retrieveIntegerFromXML(headText, "note_width", Globals.UNCHOSEN_NOTE_WIDTH));
			data.setNoteHeight(XMLTransact.retrieveIntegerFromXML(headText, "note_height", Globals.UNCHOSEN_NOTE_HEIGHT));
			
			data.setCustomTitle(FauxXMLTool.retrieveSection(headText, "custom_title"));
			
			data.setSessionPriority(XMLTransact.retrievePriorityFromXML(headText, "priority", SessionPriority.NORMAL));
			
			section = FauxXMLTool.retrieveSection(headText, "recipients");
			data.setRecipients(FauxXMLTool.retrieveStringOrderedArrayList(section, "recipient", "value"));
			
			data.setEntry(FauxXMLTool.retrieveValue(bodyText, "entry", "value"));
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return data;
	}
	
	public static String parseTemplateDataToXML(final TemplateData data)
	{
		StringBuilder xml = new StringBuilder();
		
		xml.append("<template>\n");
		xml.append("\t<head>\n");
		xml.append("\t\t<name>" + data.getTemplateName() + "</name>\n");
		xml.append("\t\t<note_color>\n");
		xml.append("\t\t\t<red>" + String.valueOf(data.getNoteColorRed()) + "</red>\n");
		xml.append("\t\t\t<green>" + String.valueOf(data.getNoteColorGreen()) + "</green>\n");
		xml.append("\t\t\t<blue>" + String.valueOf(data.getNoteColorBlue()) + "</blue>\n");
		xml.append("\t\t</note_color>\n");
		xml.append("\t\t<title_color>\n");
		xml.append("\t\t\t<red>" + String.valueOf(data.getTitleColorRed()) + "</red>\n");
		xml.append("\t\t\t<green>" + String.valueOf(data.getTitleColorGreen()) + "</green>\n");
		xml.append("\t\t\t<blue>" + String.valueOf(data.getTitleColorBlue()) + "</blue>\n");
		xml.append("\t\t</title_color>\n");
		xml.append("\t\t<border_color>\n");
		xml.append("\t\t\t<red>" + String.valueOf(data.getBorderColorRed()) + "</red>\n");
		xml.append("\t\t\t<green>" + String.valueOf(data.getBorderColorGreen()) + "</green>\n");
		xml.append("\t\t\t<blue>" + String.valueOf(data.getBorderColorBlue()) + "</blue>\n");
		xml.append("\t\t</border_color>\n");
		xml.append("\t\t<border_width>" + String.valueOf(data.getBorderWidth()) + "</border_width>\n");
		xml.append("\t\t<note_font>\n");
		xml.append("\t\t\t<face>" + data.getNoteFontFace() + "</face>\n");
		xml.append("\t\t\t<size>" + String.valueOf(data.getNoteFontSize()) + "</size>\n");
		xml.append("\t\t\t<bold>" + String.valueOf(data.getNoteFontBold()) + "</bold>\n");
		xml.append("\t\t\t<italic>" + String.valueOf(data.getNoteFontItalic()) + "</italic>\n");
		xml.append("\t\t\t<underline>" + String.valueOf(data.getNoteFontUnderline()) + "</underline>\n");
		xml.append("\t\t</note_font>\n");
		xml.append("\t\t<title_font>\n");
		xml.append("\t\t\t<face>" + data.getTitleFontFace() + "</face>\n");
		xml.append("\t\t\t<size>" + String.valueOf(data.getTitleFontSize()) + "</size>\n");
		xml.append("\t\t\t<bold>" + String.valueOf(data.getTitleFontBold()) + "</bold>\n");
		xml.append("\t\t\t<italic>" + String.valueOf(data.getTitleFontItalic()) + "</italic>\n");
		xml.append("\t\t</title_font>\n");
		xml.append("\t\t<note_font_color>\n");
		xml.append("\t\t\t<red>" + String.valueOf(data.getNoteFontColorRed()) + "</red>\n");
		xml.append("\t\t\t<green>" + String.valueOf(data.getNoteFontColorGreen()) + "</green>\n");
		xml.append("\t\t\t<blue>" + String.valueOf(data.getNoteFontColorBlue()) + "</blue>\n");
		xml.append("\t\t</note_font_color>\n");
		xml.append("\t\t<title_font_color>\n");
		xml.append("\t\t\t<red>" + String.valueOf(data.getTitleFontColorRed()) + "</red>\n");
		xml.append("\t\t\t<green>" + String.valueOf(data.getTitleFontColorGreen()) + "</green>\n");
		xml.append("\t\t\t<blue>" + String.valueOf(data.getTitleFontColorBlue()) + "</blue>\n");
		xml.append("\t\t</title_font_color>\n");
		xml.append("\t\t<note_width>" + String.valueOf(data.getNoteWidth()) + "</note_width>\n");
		xml.append("\t\t<note_height>" + String.valueOf(data.getNoteHeight()) + "</note_height>\n");
		xml.append("\t\t<priority>" + data.getSessionPriority().name() + "</priority>\n");
		xml.append("\t\t<custom_title>" + data.getCustomTitle() + "</custom_title>\n");
		xml.append("\t\t<recipients>\n");
		for(String recipient : data.getRecipients())
		{
			xml.append("\t\t\t<recipient value=\"" + recipient + "\"></recipient>\n");
		}
		xml.append("\t\t</recipients>\n");
		xml.append("\t</head>\n");
		xml.append("\t<body>\n");
		xml.append("\t\t<entry value=\"" + data.getEntry() + "\"></entry>\n");
		xml.append("\t</body>\n");
		xml.append("</template>");
		
		return xml.toString();
	}
	
	public static StringBuffer parseConfigDataToXML()
	{
		StringBuffer xml = new StringBuffer();
		
		xml.append("<root>\n");
		
		xml.append("\t<settings>\n");
		xml.append("\t\t<multicast>" + ConfigData.getMulticastIP() + "</multicast>\n");
		xml.append("\t\t<session_port>" + ConfigData.getSessionPort() + "</session_port>\n");
		xml.append("\t\t<census_interval>" + ConfigData.getCensusInterval() + "</census_interval>\n");
		xml.append("\t\t<use_border>" + String.valueOf(ConfigData.getUseNoteBorder()) + "</use_border>\n");
		xml.append("\t\t<use_time_title>" + String.valueOf(ConfigData.getUseTimeForTitle()) + "</use_time_title>\n");
		xml.append("\t\t<default_title>" + ConfigData.getDefaultTitle() + "</default_title>\n");
		xml.append("\t\t<title_align>" + ConfigData.getDefaultTitleAlignment().toString().toLowerCase() + "</title_align>\n");
		xml.append("\t\t<note_color>\n");
		xml.append("\t\t\t<red>" + ConfigData.getDefaultNoteColorRed() + "</red>\n");
		xml.append("\t\t\t<green>" + ConfigData.getDefaultNoteColorGreen() + "</green>\n");
		xml.append("\t\t\t<blue>" + ConfigData.getDefaultNoteColorBlue() + "</blue>\n");
		xml.append("\t\t</note_color>\n");
		xml.append("\t\t<title_color>\n");
		xml.append("\t\t\t<red>" + ConfigData.getDefaultTitleColorRed() + "</red>\n");
		xml.append("\t\t\t<green>" + ConfigData.getDefaultTitleColorGreen() + "</green>\n");
		xml.append("\t\t\t<blue>" + ConfigData.getDefaultTitleColorBlue() + "</blue>\n");
		xml.append("\t\t</title_color>\n");
		xml.append("\t\t<border_color>\n");
		xml.append("\t\t\t<red>" + ConfigData.getDefaultBorderColorRed() + "</red>\n");
		xml.append("\t\t\t<green>" + ConfigData.getDefaultBorderColorGreen() + "</green>\n");
		xml.append("\t\t\t<blue>" + ConfigData.getDefaultBorderColorBlue() + "</blue>\n");
		xml.append("\t\t</border_color>\n");
		xml.append("\t\t<border_width>" + ConfigData.getDefaultBorderWidth() + "</border_width>\n");
		xml.append("\t\t<note_font>\n");
		xml.append("\t\t\t<face>" + ConfigData.getDefaultNoteFontName() + "</face>\n");
		xml.append("\t\t\t<size>" + String.valueOf(ConfigData.getDefaultNoteFontSize()) + "</size>\n");
		xml.append("\t\t\t<bold>" + String.valueOf(ConfigData.getDefaultNoteFontBold()) + "</bold>\n");
		xml.append("\t\t\t<italic>" + String.valueOf(ConfigData.getDefaultNoteFontItalic()) + "</italic>\n");
		xml.append("\t\t\t<underline>" + String.valueOf(ConfigData.getDefaultNoteFontUnderline()) + "</underline>\n");
		xml.append("\t\t\t<strikeout>" + String.valueOf(ConfigData.getDefaultNoteFontStrikeout()) + "</strikeout>\n");
		xml.append("\t\t</note_font>\n");
		xml.append("\t\t<title_font>\n");
		xml.append("\t\t\t<face>" + ConfigData.getDefaultTitleFontName() + "</face>\n");
		xml.append("\t\t\t<size>" + String.valueOf(ConfigData.getDefaultTitleFontSize()) + "</size>\n");
		xml.append("\t\t\t<bold>" + String.valueOf(ConfigData.getDefaultTitleFontBold()) + "</bold>\n");
		xml.append("\t\t\t<italic>" + String.valueOf(ConfigData.getDefaultTitleFontItalic()) + "</italic>\n");
		xml.append("\t\t</title_font>\n");
		xml.append("\t\t<note_font_color>\n");
		xml.append("\t\t\t<red>" + ConfigData.getDefaultNoteFontColorRed() + "</red>\n");
		xml.append("\t\t\t<green>" + ConfigData.getDefaultNoteFontColorGreen() + "</green>\n");
		xml.append("\t\t\t<blue>" + ConfigData.getDefaultNoteFontColorBlue() + "</blue>\n");
		xml.append("\t\t</note_font_color>\n");
		xml.append("\t\t<title_font_color>\n");
		xml.append("\t\t\t<red>" + ConfigData.getDefaultTitleFontColorRed() + "</red>\n");
		xml.append("\t\t\t<green>" + ConfigData.getDefaultTitleFontColorGreen() + "</green>\n");
		xml.append("\t\t\t<blue>" + ConfigData.getDefaultTitleFontColorBlue() + "</blue>\n");
		xml.append("\t\t</title_font_color>\n");
		xml.append("\t\t<note_width>" + ConfigData.getDefaultNoteWidth() + "</note_width>\n");
		xml.append("\t\t<note_height>" + ConfigData.getDefaultNoteHeight() + "</note_height>\n");
		xml.append("\t\t<confirm_delete>" + String.valueOf(ConfigData.getConfirmBeforeNoteDeletion()) + "</confirm_delete>\n");
		xml.append("\t\t<template_menu>" + String.valueOf(ConfigData.getShowTemplateItemInTrayMenu()) + "</template_menu>\n");
		xml.append("\t\t<show_splash>" + String.valueOf(ConfigData.getShowSplashAtStartup()) + "</show_splash>\n");
		xml.append("\t\t<play_sound>" + String.valueOf(ConfigData.getPlaySoundUponReceiving()) + "</play_sound>\n");
		xml.append("\t\t<notes_expire>" + String.valueOf(ConfigData.getNotesExpire()) + "</notes_expire>\n");
		xml.append("\t\t<expire_minutes>" + ConfigData.getExpirationMinutes() + "</expire_minutes>\n");
		xml.append("\t</settings>\n");
		
		xml.append("\t<_network>\n");
		for(String name : ConfigData.getRecipients("_network"))
		{
			xml.append("\t\t<recipient name=\"" + name + "\"></recipient>\n");
		}
		xml.append("\t</_network>\n");
		
		xml.append("\t<lists>\n");
		for(String listName : ConfigData.getRecipientLists())
		{
			if(listName.compareToIgnoreCase("_network") == 0) { continue; }
			xml.append("\t\t<list name=\"" + listName + "\">\n");
			for(String name : ConfigData.getRecipients(listName))
			{
				xml.append("\t\t\t<recipient name=\"" + name + "\"></recipient>\n");
			}
			xml.append("\t\t</list>\n");
		}
		xml.append("\t</lists>\n");
		
		xml.append("</root>\n");
		
		return xml;
	}
	
	/**
	 * In the event no config.xml file exists, or is corrupt, this methods is intended to generate the XML
	 * for a new config.xml file.
	 * Output is "pretty-printed", thus the tabs and new-lines.
	 * @return String value containing generated XML.
	 */
	public static String createBlankConfigXML()
	{
		StringBuilder xml = new StringBuilder();
		
		xml.append("<root>\n");
		
		xml.append("\t<settings>\n");
		xml.append("\t\t<multicast>" + Globals.UNCHOSEN_MULTICAST_IP + "</multicast>\n");
		xml.append("\t\t<session_port>" + String.valueOf(Globals.UNCHOSEN_MULTICAST_PORT) + "</session_port>\n");
		xml.append("\t\t<census_interval>" + String.valueOf(Globals.UNCHOSEN_CENSUS_INTERVAL) + "</census_interval>\n");
		xml.append("\t\t<use_border>" + String.valueOf(Globals.UNCHOSEN_USE_BORDER) + "</use_border>\n");
		xml.append("\t\t<use_time_title>" + String.valueOf(Globals.UNCHOSEN_USE_TIME_TITLE) + "</use_time_title>\n");
		xml.append("\t\t<default_title>" + Globals.UNCHOSEN_CUSTOM_TITLE + "</default_title>\n");
		xml.append("\t\t<title_align>" + Globals.UNCHOSEN_TITLE_ALIGNMENT.name() + "</title_align>\n");
		xml.append("\t\t<note_color>\n");
		xml.append("\t\t\t<red>" + String.valueOf(Globals.UNCHOSEN_NOTE_COLOR_RED) + "</red>\n");
		xml.append("\t\t\t<green>" + String.valueOf(Globals.UNCHOSEN_NOTE_COLOR_GREEN) + "</green>\n");
		xml.append("\t\t\t<blue>" + String.valueOf(Globals.UNCHOSEN_NOTE_COLOR_BLUE) + "</blue>\n");
		xml.append("\t\t</note_color>\n");
		xml.append("\t\t<title_color>\n");
		xml.append("\t\t\t<red>" + String.valueOf(Globals.UNCHOSEN_TITLE_COLOR_RED) + "</red>\n");
		xml.append("\t\t\t<green>" + String.valueOf(Globals.UNCHOSEN_TITLE_COLOR_GREEN) + "</green>\n");
		xml.append("\t\t\t<blue>" + String.valueOf(Globals.UNCHOSEN_TITLE_COLOR_BLUE) + "</blue>\n");
		xml.append("\t\t</title_color>\n");
		xml.append("\t\t<border_color>\n");
		xml.append("\t\t\t<red>" + String.valueOf(Globals.UNCHOSEN_BORDER_COLOR_RED) + "</red>\n");
		xml.append("\t\t\t<green>" + String.valueOf(Globals.UNCHOSEN_BORDER_COLOR_GREEN) + "</green>\n");
		xml.append("\t\t\t<blue>" + String.valueOf(Globals.UNCHOSEN_BORDER_COLOR_BLUE) + "</blue>\n");
		xml.append("\t\t</border_color>\n");
		xml.append("\t\t<border_width>" + String.valueOf(Globals.UNCHOSEN_BORDER_WIDTH) + "</border_width>\n");
		xml.append("\t\t<note_font>\n");
		xml.append("\t\t\t<face>" + Globals.UNCHOSEN_NOTE_FONT_FACE + "</face>\n");
		xml.append("\t\t\t<size>" + String.valueOf(Globals.UNCHOSEN_NOTE_FONT_SIZE) + "</size>\n");
		xml.append("\t\t\t<bold>" + String.valueOf(Globals.UNCHOSEN_NOTE_FONT_BOLD) + "</bold>\n");
		xml.append("\t\t\t<italic>" + String.valueOf(Globals.UNCHOSEN_NOTE_FONT_ITALIC) + "</italic>\n");
		xml.append("\t\t\t<underline>" + String.valueOf(Globals.UNCHOSEN_NOTE_FONT_UNDERLINE) + "</underline>\n");
		xml.append("\t\t\t<strikeout>" + String.valueOf(Globals.UNCHOSEN_NOTE_FONT_STRIKEOUT) + "</strikeout>\n");
		xml.append("\t\t</note_font>\n");
		xml.append("\t\t<title_font>\n");
		xml.append("\t\t\t<face>" + Globals.UNCHOSEN_TITLE_FONT_FACE + "</face>\n");
		xml.append("\t\t\t<size>" + String.valueOf(Globals.UNCHOSEN_TITLE_FONT_SIZE) + "</size>\n");
		xml.append("\t\t\t<bold>" + String.valueOf(Globals.UNCHOSEN_TITLE_FONT_BOLD) + "</bold>\n");
		xml.append("\t\t\t<italic>" + String.valueOf(Globals.UNCHOSEN_TITLE_FONT_ITALIC) + "</italic>\n");
		xml.append("\t\t</title_font>\n");
		xml.append("\t\t<note_font_color>\n");
		xml.append("\t\t\t<red>" + String.valueOf(Globals.UNCHOSEN_NOTE_FONT_COLOR_RED) + "</red>\n");
		xml.append("\t\t\t<green>" + String.valueOf(Globals.UNCHOSEN_NOTE_FONT_COLOR_GREEN) + "</green>\n");
		xml.append("\t\t\t<blue>" + String.valueOf(Globals.UNCHOSEN_NOTE_FONT_COLOR_BLUE) + "</blue>\n");
		xml.append("\t\t</note_font_color>\n");
		xml.append("\t\t<title_font_color>\n");
		xml.append("\t\t\t<red>" + String.valueOf(Globals.UNCHOSEN_TITLE_FONT_COLOR_RED) + "</red>\n");
		xml.append("\t\t\t<green>" + String.valueOf(Globals.UNCHOSEN_TITLE_FONT_COLOR_GREEN) + "</green>\n");
		xml.append("\t\t\t<blue>" + String.valueOf(Globals.UNCHOSEN_TITLE_FONT_COLOR_BLUE) + "</blue>\n");
		xml.append("\t\t</title_font_color>\n");
		xml.append("\t\t<note_width>" + String.valueOf(Globals.UNCHOSEN_NOTE_WIDTH) + "</note_width>\n");
		xml.append("\t\t<note_height>" + String.valueOf(Globals.UNCHOSEN_NOTE_HEIGHT) + "</note_height>\n");
		xml.append("\t\t<confirm_delete>" + String.valueOf(Globals.UNCHOSEN_CONFIRM_BEFORE_DELETE) + "</confirm_delete>\n");
		xml.append("\t\t<template_menu>" + String.valueOf(Globals.UNCHOSEN_SHOW_TEMPLATE_ITEM_IN_TRAY_MENU) + "</template_menu>\n");
		xml.append("\t\t<show_splash>" + String.valueOf(Globals.UNCHOSEN_SHOW_SPLASH_AT_STARTUP) + "</show_splash>\n");
		xml.append("\t\t<play_sound>" + String.valueOf(Globals.UNCHOSEN_PLAY_SOUND_ON_RECEIVING) + "</play_sound>\n");
		xml.append("\t\t<notes_expire>" + String.valueOf(Globals.UNCHOSEN_NOTES_EXPIRE) + "</notes_expire>\n");
		xml.append("\t\t<expire_minutes>" + String.valueOf(Globals.UNCHOSEN_EXPIRATION_MINUTES) + "</expire_minutes>\n");
		xml.append("\t</settings>\n");
		
		xml.append("\t<_network>\n");
		xml.append("\t</_network>\n");
		
		xml.append("\t<lists>\n");
		xml.append("\t\t<list name=\"Everyone\">\n");
		xml.append("\t\t</list>\n");
		xml.append("\t</lists>\n");
		
		xml.append("</root>\n");
		
		return xml.toString();
	}
}
