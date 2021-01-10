package config;

import dialogs.Emoticon;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

import main.Globals;
import main.TemplateData;

// The reason for putting this class in its own package, was to use the "protected" modifier functionality.
// This way, non-subclasses could freely use some methods, but only subclasses could use all methods.
// For instance, a manager class could allow other classes free access to its read methods, but only the manager class
// can use the write methods.

public class ConfigData
{
	public static enum Align{LEFT, CENTER, RIGHT};
	
	private static String mHostName = null;
	private static String mHostIPAddress = null;
	private static String mMulticastIP = null;
	private static int mSessionPort = 0;
	private static int mCensusInterval = 0;
	protected static TreeMap<String, ArrayList<String>> mRecipients = null;
	private static boolean mUseNoteBorder = true;
	private static boolean mUseTimeForTitle = true;
	private static String mDefaultTitle = null;
	private static ConfigData.Align mDefaultTitleAlignment = null;
	private static int mDefaultNoteColorRed = 0;
	private static int mDefaultNoteColorGreen = 0;
	private static int mDefaultNoteColorBlue = 0;
	private static int mDefaultTitleColorRed = 0;
	private static int mDefaultTitleColorGreen = 0;
	private static int mDefaultTitleColorBlue = 0;
	private static int mDefaultBorderColorRed = 0;
	private static int mDefaultBorderColorGreen = 0;
	private static int mDefaultBorderColorBlue = 0;
	private static int mDefaultBorderWidth = 0;
	private static String mDefaultNoteFontName = null;
	private static int mDefaultNoteFontSize = 12;
	private static boolean mDefaultNoteFontBold = false;
	private static boolean mDefaultNoteFontItalic = false;
	private static boolean mDefaultNoteFontUnderline = false;
	private static boolean mDefaultNoteFontStrikeout = false;
	private static int mDefaultNoteFontColorRed = 0;
	private static int mDefaultNoteFontColorGreen = 0;
	private static int mDefaultNoteFontColorBlue = 0;
	private static String mDefaultTitleFontName = null;
	private static int mDefaultTitleFontSize = 12;
	private static boolean mDefaultTitleFontBold = false;
	private static boolean mDefaultTitleFontItalic = false;
	private static int mDefaultTitleFontColorRed = 0;
	private static int mDefaultTitleFontColorGreen = 0;
	private static int mDefaultTitleFontColorBlue = 0;
	private static int mDefaultNoteWidth = 300;
	private static int mDefaultNoteHeight = 200;
	private static boolean mConfirmBeforeNoteDeletion = false;
	private static boolean mShowTemplateItemInTrayMenu = false;
	private static boolean mShowSplashAtStartup = true;
	private static boolean mPlaySoundOnReceiving = false;
	private static boolean mNotesExpire = true;
	private static int mExpirationMinutes = 0;
	private static String[] mWebSafeFontNames = null;
	private static Integer[] mFontSizes = null;
	private static Vector<Emoticon> mEmoticons = null;
	private static TreeMap<String, TemplateData> mTemplates = null;
	
	protected static void setHostName(String hostName)
	{
		try{
			if(hostName == null){
				throw new Exception("Parameter cannot be null.");
			}
			if(hostName.isEmpty()){
				throw new Exception("String parameter cannot be empty.");
			}
			ConfigData.mHostName = hostName;
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return;
	}
	
	public static String getHostName()
	{
		return ConfigData.mHostName;
	}
	
	protected static void setHostIPAddress(String ip)
	{
		try{
			if(ip == null){
				throw new Exception("Parameter cannot be null.");
			}
			if(ip.isEmpty()){
				throw new Exception("String parameter cannot be empty.");
			}
			ConfigData.mHostIPAddress = ip;
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return;
	}
	
	public static String getHostIPAddress()
	{
		return ConfigData.mHostIPAddress;
	}
	
	protected static void setMulticastIP(String multicast)
	{
		try{
			if(multicast == null){
				throw new Exception("Parameter cannot be null.");
			}
			if(multicast.isEmpty()){
				throw new Exception("String parameter cannot be empty.");
			}
			ConfigData.mMulticastIP = multicast;
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return;
	}
	
	public static String getMulticastIP()
	{
		return ConfigData.mMulticastIP;
	}
	
	protected static void setSessionPort(int sessionPort)
	{
		try{
			if(sessionPort < 0 || sessionPort > 65535){
				throw new Exception("Port value out of range: " + sessionPort);
			}
			ConfigData.mSessionPort = sessionPort;
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return;
	}
	
	public static int getSessionPort()
	{
		return ConfigData.mSessionPort;
	}
	
	protected static void setCensusInterval(int milliseconds)
	{
		try{
			if(milliseconds < 0 || milliseconds > 65535){ // "65535" ? Just needed some big number.
				throw new Exception("Millisecond interval value is out of range: " + milliseconds);
			}
			ConfigData.mCensusInterval = milliseconds;
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return;
	}
	
	public static int getCensusInterval()
	{
		return ConfigData.mCensusInterval;
	}
	
	protected static void setRecipients(TreeMap<String, ArrayList<String>> recipients)
	{
		try{
			if(recipients == null){
				throw new Exception("Parameter cannot be null.");
			}
			ConfigData.mRecipients = recipients;
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return;
	}
	
	public static ArrayList<String> getRecipientLists()
	{
		ArrayList<String> list = new ArrayList<String>();
		for(String key : ConfigData.mRecipients.keySet())
		{
			list.add(key);
		}
		return list;
	}
	
	public static ArrayList<String> getRecipients(String listName)
	{
		ArrayList<String> list = null;
		
		if(ConfigData.mRecipients.containsKey(listName)){
			int count = ConfigData.mRecipients.get(listName).size();
			list = new ArrayList<String>();
			for(int i = 0; i < count; i++)
			{
				list.add(ConfigData.mRecipients.get(listName).get(i));
			}
		}
		
		return list;
	}
	
	protected static void setUseNoteBorder(boolean useBorder)
	{
		ConfigData.mUseNoteBorder = useBorder;
	}
	
	public static boolean getUseNoteBorder()
	{
		return ConfigData.mUseNoteBorder;
	}
	
	protected static void setUseTimeForTitle(boolean useTime)
	{
		ConfigData.mUseTimeForTitle = useTime;
	}
	
	public static boolean getUseTimeForTitle()
	{
		return ConfigData.mUseTimeForTitle;
	}
	
	protected static void setDefaultTitle(String title)
	{
		try{
			if(title == null){
				throw new Exception("Parameter cannot be null.");
			}
			ConfigData.mDefaultTitle = title;
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return;
	}
	
	public static String getDefaultTitle()
	{
		return ConfigData.mDefaultTitle;
	}
	
	protected static void setDefaultTitleAlignment(ConfigData.Align alignment)
	{
		try{
			if(alignment == null){
				throw new Exception("Parameter cannot be null.");
			}
			boolean found = false;
			ConfigData.Align[] types = ConfigData.Align.values();
			for(ConfigData.Align type : types)
			{
				if(alignment.compareTo(type) == 0){
					found = true;
					break;
				}
			}
			if(!found){
				throw new Exception("Invalid type parameter passed.");
			}
			ConfigData.mDefaultTitleAlignment = alignment;
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return;
	}
	
	public static ConfigData.Align getDefaultTitleAlignment()
	{
		return ConfigData.mDefaultTitleAlignment;
	}
	
	protected static void setDefaultNoteColorRed(int redValue)
	{
		try{
			if(redValue < 0 || redValue > 255){
				throw new Exception("Parameter is out of range: " + redValue);
			}
			ConfigData.mDefaultNoteColorRed = redValue;
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return;
	}
	
	public static int getDefaultNoteColorRed()
	{
		return ConfigData.mDefaultNoteColorRed;
	}
	
	protected static void setDefaultNoteColorGreen(int greenValue)
	{
		try{
			if(greenValue < 0 || greenValue > 255){
				throw new Exception("Parameter is out of range: " + greenValue);
			}
			ConfigData.mDefaultNoteColorGreen = greenValue;
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return;
	}
	
	public static int getDefaultNoteColorGreen()
	{
		return ConfigData.mDefaultNoteColorGreen;
	}
	
	protected static void setDefaultNoteColorBlue(int blueValue)
	{
		try{
			if(blueValue < 0 || blueValue > 255){
				throw new Exception("Parameter is out of range: " + blueValue);
			}
			ConfigData.mDefaultNoteColorBlue = blueValue;
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return;
	}
	
	public static int getDefaultNoteColorBlue()
	{
		return ConfigData.mDefaultNoteColorBlue;
	}
	
	protected static void setDefaultTitleColorRed(int redValue)
	{
		try{
			if(redValue < 0 || redValue > 255){
				throw new Exception("Parameter is out of range: " + redValue);
			}
			ConfigData.mDefaultTitleColorRed = redValue;
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return;
	}
	
	public static int getDefaultTitleColorRed()
	{
		return ConfigData.mDefaultTitleColorRed;
	}
	
	protected static void setDefaultTitleColorGreen(int greenValue)
	{
		try{
			if(greenValue < 0 || greenValue > 255){
				throw new Exception("Parameter is out of range: " + greenValue);
			}
			ConfigData.mDefaultTitleColorGreen = greenValue;
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return;
	}
	
	public static int getDefaultTitleColorGreen()
	{
		return ConfigData.mDefaultTitleColorGreen;
	}
	
	protected static void setDefaultTitleColorBlue(int blueValue)
	{
		try{
			if(blueValue < 0 || blueValue > 255){
				throw new Exception("Parameter is out of range: " + blueValue);
			}
			ConfigData.mDefaultTitleColorBlue = blueValue;
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return;
	}
	
	public static int getDefaultTitleColorBlue()
	{
		return ConfigData.mDefaultTitleColorBlue;
	}
	
	protected static void setDefaultBorderColorRed(int redValue)
	{
		try{
			if(redValue < 0 || redValue > 255){
				throw new Exception("Parameter is out of range: " + redValue);
			}
			ConfigData.mDefaultBorderColorRed = redValue;
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return;
	}
	
	public static int getDefaultBorderColorRed()
	{
		return ConfigData.mDefaultBorderColorRed;
	}
	
	protected static void setDefaultBorderColorGreen(int greenValue)
	{
		try{
			if(greenValue < 0 || greenValue > 255){
				throw new Exception("Parameter is out of range: " + greenValue);
			}
			ConfigData.mDefaultBorderColorGreen = greenValue;
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return;
	}
	
	public static int getDefaultBorderColorGreen()
	{
		return ConfigData.mDefaultBorderColorGreen;
	}
	
	protected static void setDefaultBorderColorBlue(int blueValue)
	{
		try{
			if(blueValue < 0 || blueValue > 255){
				throw new Exception("Parameter is out of range: " + blueValue);
			}
			ConfigData.mDefaultBorderColorBlue = blueValue;
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return;
	}
	
	public static int getDefaultBorderColorBlue()
	{
		return ConfigData.mDefaultBorderColorBlue;
	}
	
	protected static void setDefaultBorderWidth(int borderWidth)
	{
		try{
			if(borderWidth < 0 || borderWidth > 20){
				throw new Exception("Parameter is out of range: " + borderWidth);
			}
			ConfigData.mDefaultBorderWidth = borderWidth;
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return;
	}
	
	public static int getDefaultBorderWidth()
	{
		return ConfigData.mDefaultBorderWidth;
	}
	
	protected static void setDefaultNoteFontName(String noteFont)
	{
		try{
			if(noteFont == null || noteFont.isEmpty()){
				throw new Exception("Parameter cannot be null or empty.");
			}
			ConfigData.mDefaultNoteFontName = noteFont;
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return;
	}
	
	public static String getDefaultNoteFontName()
	{
		return ConfigData.mDefaultNoteFontName;
	}
	
	protected static void setDefaultNoteFontSize(int fontSize)
	{
		try{
			if(fontSize <= 0){
				throw new Exception("Parameter must be greater than zero.");
			}
			ConfigData.mDefaultNoteFontSize = fontSize;
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return;
	}
	
	public static int getDefaultNoteFontSize()
	{
		return ConfigData.mDefaultNoteFontSize;
	}
	
	protected static void setDefaultNoteFontBold(boolean isBold)
	{
		ConfigData.mDefaultNoteFontBold = isBold;
		return;
	}
	
	public static boolean getDefaultNoteFontBold()
	{
		return ConfigData.mDefaultNoteFontBold;
	}
	
	protected static void setDefaultNoteFontItalic(boolean isItalic)
	{
		ConfigData.mDefaultNoteFontItalic = isItalic;
		return;
	}
	
	public static boolean getDefaultNoteFontItalic()
	{
		return ConfigData.mDefaultNoteFontItalic;
	}
	
	protected static void setDefaultNoteFontUnderline(boolean isUnderline)
	{
		ConfigData.mDefaultNoteFontUnderline = isUnderline;
		return;
	}
	
	public static boolean getDefaultNoteFontUnderline()
	{
		return ConfigData.mDefaultNoteFontUnderline;
	}
	
	protected static void setDefaultNoteFontStrikeout(boolean isStrikeout)
	{
		ConfigData.mDefaultNoteFontStrikeout = isStrikeout;
		return;
	}
	
	public static boolean getDefaultNoteFontStrikeout()
	{
		return ConfigData.mDefaultNoteFontStrikeout;
	}
	
	protected static void setDefaultNoteFontColorRed(int redValue)
	{
		try{
			if(redValue < 0 || redValue > 255){
				throw new Exception("Parameter is out of range: " + redValue);
			}
			ConfigData.mDefaultNoteFontColorRed = redValue;
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return;
	}
	
	public static int getDefaultNoteFontColorRed()
	{
		return ConfigData.mDefaultNoteFontColorRed;
	}
	
	protected static void setDefaultNoteFontColorGreen(int greenValue)
	{
		try{
			if(greenValue < 0 || greenValue > 255){
				throw new Exception("Parameter is out of range: " + greenValue);
			}
			ConfigData.mDefaultNoteFontColorGreen = greenValue;
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return;
	}
	
	public static int getDefaultNoteFontColorGreen()
	{
		return ConfigData.mDefaultNoteFontColorGreen;
	}
	
	protected static void setDefaultNoteFontColorBlue(int blueValue)
	{
		try{
			if(blueValue < 0 || blueValue > 255){
				throw new Exception("Parameter is out of range: " + blueValue);
			}
			ConfigData.mDefaultNoteFontColorBlue = blueValue;
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return;
	}
	
	public static int getDefaultNoteFontColorBlue()
	{
		return ConfigData.mDefaultNoteFontColorBlue;
	}
	
	protected static void setDefaultTitleFontName(String titleFont)
	{
		try{
			if(titleFont == null || titleFont.isEmpty()){
				throw new Exception("Parameter cannot be null.");
			}
			ConfigData.mDefaultTitleFontName = titleFont;
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return;
	}
	
	public static String getDefaultTitleFontName()
	{
		return ConfigData.mDefaultTitleFontName;
	}
	
	protected static void setDefaultTitleFontSize(int fontSize)
	{
		try{
			if(fontSize <= 0){
				throw new Exception("Parameter must be greater than zero.");
			}
			ConfigData.mDefaultTitleFontSize = fontSize;
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return;
	}
	
	public static int getDefaultTitleFontSize()
	{
		return ConfigData.mDefaultTitleFontSize;
	}
	
	protected static void setDefaultTitleFontBold(boolean isBold)
	{
		ConfigData.mDefaultTitleFontBold = isBold;
		return;
	}
	
	public static boolean getDefaultTitleFontBold()
	{
		return ConfigData.mDefaultTitleFontBold;
	}
	
	protected static void setDefaultTitleFontItalic(boolean isItalic)
	{
		ConfigData.mDefaultTitleFontItalic = isItalic;
		return;
	}
	
	public static boolean getDefaultTitleFontItalic()
	{
		return ConfigData.mDefaultTitleFontItalic;
	}
	
	protected static void setDefaultTitleFontColorRed(int redValue)
	{
		try{
			if(redValue < 0 || redValue > 255){
				throw new Exception("Parameter is out of range: " + redValue);
			}
			ConfigData.mDefaultTitleFontColorRed = redValue;
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return;
	}
	
	public static int getDefaultTitleFontColorRed()
	{
		return ConfigData.mDefaultTitleFontColorRed;
	}
	
	protected static void setDefaultTitleFontColorGreen(int greenValue)
	{
		try{
			if(greenValue < 0 || greenValue > 255){
				throw new Exception("Parameter is out of range: " + greenValue);
			}
			ConfigData.mDefaultTitleFontColorGreen = greenValue;
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return;
	}
	
	public static int getDefaultTitleFontColorGreen()
	{
		return ConfigData.mDefaultTitleFontColorGreen;
	}
	
	protected static void setDefaultTitleFontColorBlue(int blueValue)
	{
		try{
			if(blueValue < 0 || blueValue > 255){
				throw new Exception("Parameter is out of range: " + blueValue);
			}
			ConfigData.mDefaultTitleFontColorBlue = blueValue;
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return;
	}
	
	public static int getDefaultTitleFontColorBlue()
	{
		return ConfigData.mDefaultTitleFontColorBlue;
	}
	
	protected static void setDefaultNoteWidth(int width)
	{
		try{
			if(width <= 0){
				throw new Exception("Invalid parameter passed: " + width);
			}
			ConfigData.mDefaultNoteWidth = width;
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return;
	}
	
	public static int getDefaultNoteWidth()
	{
		return ConfigData.mDefaultNoteWidth;
	}
	
	protected static void setDefaultNoteHeight(int height)
	{
		try{
			if(height <= 0){
				throw new Exception("Invalid parameter passed: " + height);
			}
			ConfigData.mDefaultNoteHeight = height;
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return;
	}
	
	public static int getDefaultNoteHeight()
	{
		return ConfigData.mDefaultNoteHeight;
	}
	
	protected static void setConfirmBeforeNoteDeletion(boolean confirm)
	{
		ConfigData.mConfirmBeforeNoteDeletion = confirm;
		return;
	}
	
	public static boolean getConfirmBeforeNoteDeletion()
	{
		return ConfigData.mConfirmBeforeNoteDeletion;
	}
	
	protected static void setShowTemplateItemInTrayMenu(boolean show)
	{
		ConfigData.mShowTemplateItemInTrayMenu = show;
		return;
	}
	
	public static boolean getShowTemplateItemInTrayMenu()
	{
		return ConfigData.mShowTemplateItemInTrayMenu;
	}
	
	protected static void setShowSplashAtStartup(boolean show)
	{
		ConfigData.mShowSplashAtStartup = show;
		return;
	}
	
	public static boolean getShowSplashAtStartup()
	{
		return ConfigData.mShowSplashAtStartup;
	}
	
	protected static void setPlaySoundUponReceiving(boolean play)
	{
		ConfigData.mPlaySoundOnReceiving = play;
		return;
	}
	
	public static boolean getPlaySoundUponReceiving()
	{
		return ConfigData.mPlaySoundOnReceiving;
	}
	
	protected static void setNotesExpire(boolean expires)
	{
		ConfigData.mNotesExpire = expires;
		return;
	}
	
	public static boolean getNotesExpire()
	{
		return ConfigData.mNotesExpire;
	}
	
	protected static void setExpirationMinutes(int minutes)
	{
		try{
			if(minutes < 0 || minutes > 999){
				throw new Exception("Expiration value is out of range (< 0 or > 999): " + minutes);
			}
			ConfigData.mExpirationMinutes = minutes;
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return;
	}
	
	public static int getExpirationMinutes()
	{
		return ConfigData.mExpirationMinutes;
	}
	
	protected static void setWebSafeFontNames(String[] fontNames)
	{
		try{
			if(fontNames == null){
				throw new Exception("Parameter cannot be null.");
			}
			if(fontNames.length <= 0){
				throw new Exception("Array parameter cannot be empty.");
			}
			ConfigData.mWebSafeFontNames = fontNames;
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return;
	}
	
	public static final String[] getWebSafeFontNames()
	{
		return ConfigData.mWebSafeFontNames;
	}
	
	protected static void setFontSizes(Integer[] fontSizes)
	{
		try{
			if(fontSizes == null){
				throw new Exception("Parameter cannot be null.");
			}
			if(fontSizes.length <= 0){
				throw new Exception("Array parameter cannot be empty.");
			}
			ConfigData.mFontSizes = fontSizes;
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return;
	}
	
	public static Integer[] getFontSizes()
	{
		return ConfigData.mFontSizes;
	}
	
	protected static void setEmoticons(Vector<Emoticon> emoticons)
	{
		try{
			if(emoticons == null){
				throw new Exception("Parameter cannot be null.");
			}
			ConfigData.mEmoticons = emoticons;
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return;
	}
	
	public static Vector<Emoticon> getEmoticons()
	{
		Vector<Emoticon> em = null;
		try{
			em = new Vector<Emoticon>();
			int count = ConfigData.mEmoticons.size();
			for(int i = 0; i < count; i++)
			{
				em.add(ConfigData.mEmoticons.get(i));
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return em;
	}
	
	public static Emoticon getEmoticon(int index)
	{
		Emoticon em = null;
		try{
			em = ConfigData.mEmoticons.get(index);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return em;
	}
	
	protected static void setTemplates(TreeMap<String, TemplateData> templates)
	{
		try{
			if(templates == null){
				throw new Exception("Parameter cannot be null.");
			}
			ConfigData.mTemplates = templates;
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return;
	}
	
	public static ArrayList<String> getTemplateNames()
	{
		ArrayList<String> names = new ArrayList<String>();
		
		for(String key : ConfigData.mTemplates.keySet())
		{
			names.add(key);
		}
		
		return names;
	}
	
	public static TemplateData getTemplate(String name)
	{
		TemplateData t = null;
		
		if(ConfigData.mTemplates.containsKey(name)){
			t = ConfigData.mTemplates.get(name);
		}
		
		return t;
	}
}
