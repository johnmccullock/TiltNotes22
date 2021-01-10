package main;

import java.util.ArrayList;
import java.util.EventObject;
import java.util.TreeMap;

import config.ConfigData;

/**
 * Event object for passing data back to listening classes.
 * @author John McCullock
 * @version 2.0 08/06/2013
 * @see ConfigurationEventListener
 * @see ConfigurationListener
 */
public class ConfigurationEvent extends EventObject
{
	public String multicastIP = null;
	public int sessionPort = 0;
	public int censusInterval = 0;
	public TreeMap<String, ArrayList<String>> recipients = null;
	public boolean useNoteBorder = true;
	public boolean useTimeForTitle = true;
	public String defaultTitle = null;
	public ConfigData.Align defaultTitleAlignment = null;
	public int defaultNoteColorRed = 0;
	public int defaultNoteColorGreen = 0;
	public int defaultNoteColorBlue = 0;
	public int defaultTitleColorRed = 0;
	public int defaultTitleColorGreen = 0;
	public int defaultTitleColorBlue = 0;
	public int defaultBorderColorRed = 0;
	public int defaultBorderColorGreen = 0;
	public int defaultBorderColorBlue = 0;
	public int defaultBorderWidth = 0;
	public String defaultNoteFontName = null;
	public int defaultNoteFontSize = 0;
	public boolean defaultNoteFontBold = false;
	public boolean defaultNoteFontItalic = false;
	public boolean defaultNoteFontUnderline = false;
	public boolean defaultNoteFontStrikeout = false;
	public int defaultNoteFontColorRed = 0;
	public int defaultNoteFontColorGreen = 0;
	public int defaultNoteFontColorBlue = 0;
	public String defaultTitleFontName = null;
	public int defaultTitleFontSize = 0;
	public boolean defaultTitleFontBold = false;
	public boolean defaultTitleFontItalic = false;
	public int defaultTitleFontColorRed = 0;
	public int defaultTitleFontColorGreen = 0;
	public int defaultTitleFontColorBlue = 0;
	public int defaultNoteWidth = 300;
	public int defaultNoteHeight = 200;
	public boolean confirmBeforeNoteDeletion = false;
	public boolean showTemplateItemInTrayMenu = false;
	public boolean showSplashAtStartup = true;
	public boolean playSoundOnReceiving = false;
	public boolean notesExpire = true;
	public int expirationMinutes = 0;
	
	/**
	 * Empty constructor for creating a new ConfigurationEvent object.
	 * @param source the object referenced as the source/caller/host of the event.
	 */
	public ConfigurationEvent(Object source)
	{
		super(source);
		return;
	}
	
	public ConfigurationEvent(Object source,
								String multicastIP,
								int sessionPort,
								int censusInterval,
								TreeMap<String, ArrayList<String>> recipients,
								boolean useNoteBorder,
								boolean useTimeForTitle,
								String defaultTitle,
								ConfigData.Align defaultTitleAlignment,
								int defaultNoteColorRed,
								int defaultNoteColorGreen, 
								int defaultNoteColorBlue, 
								int defaultTitleColorRed,
								int defaultTitleColorGreen, 
								int defaultTitleColorBlue, 
								int defaultBorderColorRed,
								int defaultBorderColorGreen,
								int defaultBorderColorBlue,
								int defaultBorderWidth,
								String defaultNoteFontName,
								int defaultNoteFontSize, 
								boolean defaultNoteFontBold, 
								boolean defaultNoteFontItalic, 
								boolean defaultNoteFontUnderline, 
								boolean defaultNoteFontStrikeout, 
								int defaultNoteFontColorRed, 
								int defaultNoteFontColorGreen, 
								int defaultNoteFontColorBlue, 
								String defaultTitleFontName, 
								int defaultTitleFontSize, 
								boolean defaultTitleFontBold,
								boolean defaultTitleFontItalic, 
								int defaultTitleFontColorRed, 
								int defaultTitleFontColorGreen, 
								int defaultTitleFontColorBlue, 
								int defaultNoteWidth,
								int defaultNoteHeight,
								boolean confirmBeforeNoteDeletion,
								boolean showTemplateItemInTrayMenu,
								boolean showSplashAtStartup,
								boolean playSoundOnReceiving,
								boolean notesExpire,
								int expirationMinutes)
	{
		super(source);
		this.multicastIP = multicastIP;
		this.sessionPort = sessionPort;
		this.censusInterval = censusInterval;
		this.recipients = recipients;
		this.useNoteBorder = useNoteBorder;
		this.useTimeForTitle = useTimeForTitle;
		this.defaultTitle = defaultTitle;
		this.defaultTitleAlignment = defaultTitleAlignment;
		this.defaultNoteColorRed = defaultNoteColorRed;
		this.defaultNoteColorGreen = defaultNoteColorGreen;
		this.defaultNoteColorBlue = defaultNoteColorBlue;
		this.defaultTitleColorRed = defaultTitleColorRed;
		this.defaultTitleColorGreen = defaultTitleColorGreen;
		this.defaultTitleColorBlue = defaultTitleColorBlue;
		this.defaultBorderColorRed = defaultBorderColorRed;
		this.defaultBorderColorGreen = defaultBorderColorGreen;
		this.defaultBorderColorBlue = defaultBorderColorBlue;
		this.defaultBorderWidth = defaultBorderWidth;
		this.defaultNoteFontName = defaultNoteFontName;
		this.defaultNoteFontSize = defaultNoteFontSize;
		this.defaultNoteFontBold = defaultNoteFontBold;
		this.defaultNoteFontItalic = defaultNoteFontItalic;
		this.defaultNoteFontUnderline = defaultNoteFontUnderline;
		this.defaultNoteFontStrikeout = defaultNoteFontStrikeout;
		this.defaultNoteFontColorRed = defaultNoteFontColorRed;
		this.defaultNoteFontColorGreen = defaultNoteFontColorGreen;
		this.defaultNoteFontColorBlue = defaultNoteFontColorBlue;
		this.defaultTitleFontName = defaultTitleFontName;
		this.defaultTitleFontSize = defaultTitleFontSize;
		this.defaultTitleFontBold = defaultTitleFontBold;
		this.defaultTitleFontItalic = defaultTitleFontItalic;
		this.defaultTitleFontColorRed = defaultTitleFontColorRed;
		this.defaultTitleFontColorGreen = defaultTitleFontColorGreen;
		this.defaultTitleFontColorBlue = defaultTitleFontColorBlue;
		this.defaultNoteWidth = defaultNoteWidth;
		this.defaultNoteHeight = defaultNoteHeight;
		this.confirmBeforeNoteDeletion = confirmBeforeNoteDeletion;
		this.showTemplateItemInTrayMenu = showTemplateItemInTrayMenu;
		this.showSplashAtStartup = showSplashAtStartup;
		this.playSoundOnReceiving = playSoundOnReceiving;
		this.notesExpire = notesExpire;
		this.expirationMinutes = expirationMinutes;
		return;
	}
}
