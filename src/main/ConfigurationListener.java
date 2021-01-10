package main;

import config.ConfigData;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.EventListener;
import java.util.TreeMap;
import java.util.Vector;

/**
 * Listeners allowing data to be transported from ConfigurationGUI to any listening class.
 * @author John McCullock
 * @version 2.0 2013-10-22
 * @see ConfigurationEvent, ConfigurationEventListener
 */
public class ConfigurationListener
{
	private static Vector<EventListener> mListeners = new Vector<EventListener>();
	
	public static void addListener(ConfigurationEventListener cel)
	{
		mListeners.addElement(cel);
		return;
	}
	
	public static void removeListener(ConfigurationEventListener cel)
	{
		mListeners.removeElement(cel);
		return;
	}
	
	public static void notifyConfigWindowClose(Object source)
	{
		ConfigurationEvent ce = new ConfigurationEvent(source);
		Enumeration<EventListener> en = mListeners.elements();
		
		while(en.hasMoreElements())
		{
			((ConfigurationEventListener)en.nextElement()).configurationGUIClosePerformed(ce);
		}
		return;
	}
	
	public static void notifyConfigWindowSave(Object source,
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
		ConfigurationEvent ce = new ConfigurationEvent(source,
														multicastIP,
														sessionPort,
														censusInterval,
														recipients,
														useNoteBorder,
														useTimeForTitle,
														defaultTitle,
														defaultTitleAlignment,
														defaultNoteColorRed,
														defaultNoteColorGreen,
														defaultNoteColorBlue,
														defaultTitleColorRed,
														defaultTitleColorGreen,
														defaultTitleColorBlue,
														defaultBorderColorRed,
														defaultBorderColorGreen,
														defaultBorderColorBlue,
														defaultBorderWidth,
														defaultNoteFontName,
														defaultNoteFontSize,
														defaultNoteFontBold,
														defaultNoteFontItalic,
														defaultNoteFontUnderline,
														defaultNoteFontStrikeout,
														defaultNoteFontColorRed,
														defaultNoteFontColorGreen,
														defaultNoteFontColorBlue,
														defaultTitleFontName,
														defaultTitleFontSize,
														defaultTitleFontBold,
														defaultTitleFontItalic,
														defaultTitleFontColorRed,
														defaultTitleFontColorGreen,
														defaultTitleFontColorBlue,
														defaultNoteWidth,
														defaultNoteHeight,
														confirmBeforeNoteDeletion,
														showTemplateItemInTrayMenu,
														showSplashAtStartup,
														playSoundOnReceiving,
														notesExpire,
														expirationMinutes);
		Enumeration<EventListener> en = mListeners.elements();
		
		while(en.hasMoreElements())
		{
			((ConfigurationEventListener)en.nextElement()).configurationGUISavePerformed(ce);
		}
		return;
	}
}
