package main;

import comm.CommClient;
import comm.CommClientEvent;
import comm.CommClientEventListener;
import comm.CommClientListener;
import comm.CommServer;
import config.ConfigData;
import dialogs.Emoticon;
import dialogs.FontData;
import tray.TrayMenuEvent;
import tray.TrayMenuEventListener;
import tray.TrayMenuListener;
import tray.TrayPopupMenu;
import util.FauxXMLTool;
import util.ImageLoader;
import util.ListFilesInFolder;
import util.NetworkIdentity;
import util.ReadWriteFile;
import util.Ticker;
import util.TickEvent;
import util.TickEventListener;
import util.TickListener;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Font;
import java.awt.Image;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.UnknownHostException;
import java.net.URL;

import java.util.ArrayList;
import java.util.Collections;
import java.util.TreeMap;
import java.util.UUID;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public class MainApp extends ConfigData implements ConfigurationEventListener,
													CommClientEventListener,
													HistoryEventListener, 
													TemplateSettingsEventListener, 
													TickEventListener,
													TrayMenuEventListener
{
	private TrayIcon mTrayIcon = null;
	private SystemTray mSystemTray = null;
	private TrayPopupMenu mTrayPopupMenu = null;
	private ConfigurationGUI mConfigGUI = null;
	private HistoryGUI mHistoryGUI = null;
	
	private CommClient mSessionClient = null;
	private Thread mSessionClientThread = null;
	private Ticker mSessionTimer = null;
	private Ticker mCensusTimer = null;
	
	private TreeMap<UUID, SessionGUI> mSessions = null;
	private Vector<SessionData> mHistory = null;
	
	public MainApp()
	{
		boolean isReady = true;
		this.mSessions = new TreeMap<UUID, SessionGUI>();
		this.mHistory = new Vector<SessionData>();
		
		if(isReady){
			// Get system name and IP address on the network.
			isReady = this.retrieveHostInformation();
		}
		
		if(isReady){
			isReady = this.retrieveConfigDataFromFile();
		}
		
		if(isReady){
			this.retrieveTemplatesFromFiles();
		}
		
		if(isReady){
			isReady = this.reinstallTrayIcon();
		}
		
		if(isReady){
			// Initializes emoticon objects and images
			isReady = this.retrieveIcons();
		}
		
		if(isReady){
			// Starts mSessionClient in a new mSessionClientThread, and adds CommClientListener
			isReady = this.reinstallCommClients();
		}
		
		if(isReady){
			// Starts mSessionTimer, mCensusTimer, and adds TickListener
			isReady = this.reinstallTimersAndListeners();
		}
		
		if(isReady){
			// Since both SessionGUI and ConfigurationGUI can send TemplateSettingsEvents, the listener
			// will have to available all the time (not just when ConfigurationGUI is open).
			TemplateSettingsListener.addListener(this);
			isReady = true;
		}
		
		if(!isReady){
			System.exit(1);
		}
		
		return;
	}
	
	/**
	 * Acquires the local machine's name and IP address on the network.
	 * @return boolean value true if successful, false otherwise.
	 */
	private boolean retrieveHostInformation()
	{
		boolean isSuccessful = false;
		
		try{
			ConfigData.setHostName(NetworkIdentity.getLocalHostName());
			ConfigData.setHostIPAddress(NetworkIdentity.getLocalHostIPAddress());
			isSuccessful = true;
		}catch(UnknownHostException uhe){
			uhe.printStackTrace();
			JOptionPane.showMessageDialog(null, "Unable to retrieve this computer's name (e.g: local host name).", "Identification Error", JOptionPane.ERROR_MESSAGE);
			ConfigData.setHostName("Unknown");
		}catch(Exception ex){
			ex.printStackTrace();
			JOptionPane.showMessageDialog(null, "Unable to retrieve this computer's name or IP address.", "Identification Error", JOptionPane.ERROR_MESSAGE);
			if(ConfigData.getHostName() == null || ConfigData.getHostName().isEmpty()){
				ConfigData.setHostName("Unknown");
			}
			if(ConfigData.getHostIPAddress() == null || ConfigData.getHostIPAddress().isEmpty()){
				ConfigData.setHostIPAddress("XXX.XXX.XXX.XXX");
			}
		}
		
		return isSuccessful;
	}
	
	/**
	 * Attempts to open and read from the configuration file at Globals.CONFIG_FILE_PATH.
	 * If the configuration file isn't found, this method attempts to create a new one.
	 * If a new file is created, another attempt is made to open and read from it.
	 * @return boolean value true if successful, false otherwise.
	 */
	private boolean retrieveConfigDataFromFile()
	{
		boolean isSuccessful = false;
		boolean done = false;
		
		while(!done)
		{
			try{
				String rawText = ReadWriteFile.read(Globals.CONFIG_FILE_PATH);
				this.parseConfigFileToData(rawText);
				isSuccessful = true;
				done = true;
			}catch(FileNotFoundException fnfe){
				int response = JOptionPane.showConfirmDialog(null,
														"Could not find config.xml.\n\nWould you like to create a new config.xml file?", 
														"Configuration File Not Found",
														JOptionPane.YES_NO_OPTION);
				if(response == JOptionPane.YES_OPTION){
					isSuccessful = this.createNewConfigurationFile();
					done = false; // go back through the loop and try to open the new file.
				}else{
					isSuccessful = false;
					done = true;
				}
			}catch(IOException ioe){
				done = true;
				JOptionPane.showMessageDialog(null, "Unable to open config.mxl for reading.", "Open File Error", JOptionPane.WARNING_MESSAGE);
			}catch(Exception ex){
				done = true;
				JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.WARNING_MESSAGE);
			}
		}
		
		return isSuccessful;
	}
	
	/**
	 * Creates a new configuration file, named according to Globals.CONFIG_FILE_PATH
	 * @return boolean value: true if successful, false otherwise. 
	 */
	private boolean createNewConfigurationFile()
	{
		boolean isSuccessful = true;
		try{
			String content = XMLTransact.createBlankConfigXML();
			ReadWriteFile.write(Globals.CONFIG_FILE_PATH, content);
		}catch(Exception ex){
			isSuccessful = false;
			ex.printStackTrace();
		}
		return isSuccessful;
	}
	
	// This method is here because only MainApp can write to ConfigData.
	private void parseConfigFileToData(final String rawText)
	{
		String data = "";
		int value = 0;
		boolean flag = false;
		String rootText = FauxXMLTool.retrieveSection(rawText, "root");
		String settingsText = FauxXMLTool.retrieveSection(rootText, "settings");
		
		data = FauxXMLTool.retrieveSection(settingsText, "multicast");
		if(data != null && !data.isEmpty()){
			ConfigData.setMulticastIP(data);
		}else{
			ConfigData.setMulticastIP(Globals.UNCHOSEN_MULTICAST_IP);
			System.out.println("multicast value failed to read from config.xml");
		}
		
		value = XMLTransact.retrieveIntegerFromXML(settingsText, "session_port", Globals.UNCHOSEN_MULTICAST_PORT);
		ConfigData.setSessionPort(value);
		
		value = XMLTransact.retrieveIntegerFromXML(settingsText, "census_interval", Globals.UNCHOSEN_CENSUS_INTERVAL);
		ConfigData.setCensusInterval(value);
		
		flag = XMLTransact.retrieveBooleanFromXML(settingsText, "use_border", Globals.UNCHOSEN_USE_BORDER);
		ConfigData.setUseNoteBorder(flag);
		
		flag = XMLTransact.retrieveBooleanFromXML(settingsText, "use_time_title", Globals.UNCHOSEN_USE_TIME_TITLE);
		ConfigData.setUseTimeForTitle(flag);
		
		data = FauxXMLTool.retrieveSection(settingsText, "default_title");
		if(data != null){ // it can be empty.
			ConfigData.setDefaultTitle(data);
		}else{
			ConfigData.setDefaultTitle("");
			System.out.println("default_title value failed to read from config.xml");
		}
		
		data = FauxXMLTool.retrieveSection(settingsText, "title_align");
		try{
			if(data.compareToIgnoreCase("left") == 0){
				ConfigData.setDefaultTitleAlignment(ConfigData.Align.LEFT);
			}else if(data.compareToIgnoreCase("center") == 0){
				ConfigData.setDefaultTitleAlignment(ConfigData.Align.CENTER);
			}else if(data.compareToIgnoreCase("right") == 0){
				ConfigData.setDefaultTitleAlignment(ConfigData.Align.RIGHT);
			}else{
				throw new Exception("title_align value failed to read from config.xml");
			}
		}catch(Exception ex){
			ConfigData.setDefaultTitleAlignment(Globals.UNCHOSEN_TITLE_ALIGNMENT);
			System.out.println(ex.getMessage());
		}
		
		String colorText = FauxXMLTool.retrieveSection(settingsText, "note_color");
		ConfigData.setDefaultNoteColorRed(XMLTransact.retrieveIntegerFromXML(colorText, "red", Globals.UNCHOSEN_NOTE_COLOR_RED));
		ConfigData.setDefaultNoteColorGreen(XMLTransact.retrieveIntegerFromXML(colorText, "green", Globals.UNCHOSEN_NOTE_COLOR_GREEN));
		ConfigData.setDefaultNoteColorBlue(XMLTransact.retrieveIntegerFromXML(colorText, "blue", Globals.UNCHOSEN_NOTE_COLOR_BLUE));
		
		colorText = FauxXMLTool.retrieveSection(settingsText, "title_color");
		ConfigData.setDefaultTitleColorRed(XMLTransact.retrieveIntegerFromXML(colorText, "red", Globals.UNCHOSEN_TITLE_COLOR_RED));
		ConfigData.setDefaultTitleColorGreen(XMLTransact.retrieveIntegerFromXML(colorText, "green", Globals.UNCHOSEN_TITLE_COLOR_GREEN));
		ConfigData.setDefaultTitleColorBlue(XMLTransact.retrieveIntegerFromXML(colorText, "blue", Globals.UNCHOSEN_TITLE_COLOR_BLUE));
		
		colorText = FauxXMLTool.retrieveSection(settingsText, "border_color");
		ConfigData.setDefaultBorderColorRed(XMLTransact.retrieveIntegerFromXML(colorText, "red", Globals.UNCHOSEN_BORDER_COLOR_RED));
		ConfigData.setDefaultBorderColorGreen(XMLTransact.retrieveIntegerFromXML(colorText, "green", Globals.UNCHOSEN_BORDER_COLOR_GREEN));
		ConfigData.setDefaultBorderColorBlue(XMLTransact.retrieveIntegerFromXML(colorText, "blue", Globals.UNCHOSEN_BORDER_COLOR_BLUE));
		
		String fontText = FauxXMLTool.retrieveSection(settingsText, "note_font");
		ConfigData.setDefaultNoteFontName(XMLTransact.retrieveStringFromXML(fontText, "face", Globals.UNCHOSEN_NOTE_FONT_FACE));
		ConfigData.setDefaultNoteFontSize(XMLTransact.retrieveIntegerFromXML(fontText, "size", Globals.UNCHOSEN_NOTE_FONT_SIZE));
		ConfigData.setDefaultNoteFontBold(XMLTransact.retrieveBooleanFromXML(fontText, "bold", Globals.UNCHOSEN_NOTE_FONT_BOLD));
		ConfigData.setDefaultNoteFontItalic(XMLTransact.retrieveBooleanFromXML(fontText, "italic", Globals.UNCHOSEN_NOTE_FONT_ITALIC));
		ConfigData.setDefaultNoteFontUnderline(XMLTransact.retrieveBooleanFromXML(fontText, "underline", Globals.UNCHOSEN_NOTE_FONT_UNDERLINE));
		ConfigData.setDefaultNoteFontStrikeout(XMLTransact.retrieveBooleanFromXML(fontText, "strikeout", Globals.UNCHOSEN_NOTE_FONT_STRIKEOUT));
				
		fontText = FauxXMLTool.retrieveSection(settingsText, "title_font");
		ConfigData.setDefaultTitleFontName(XMLTransact.retrieveStringFromXML(fontText, "face", Globals.UNCHOSEN_TITLE_FONT_FACE));
		ConfigData.setDefaultTitleFontSize(XMLTransact.retrieveIntegerFromXML(fontText, "size", Globals.UNCHOSEN_TITLE_FONT_SIZE));
		ConfigData.setDefaultTitleFontBold(XMLTransact.retrieveBooleanFromXML(fontText, "bold", Globals.UNCHOSEN_TITLE_FONT_BOLD));
		ConfigData.setDefaultTitleFontItalic(XMLTransact.retrieveBooleanFromXML(fontText, "italic", Globals.UNCHOSEN_TITLE_FONT_ITALIC));
		
		colorText = FauxXMLTool.retrieveSection(settingsText, "note_font_color");
		ConfigData.setDefaultNoteFontColorRed(XMLTransact.retrieveIntegerFromXML(colorText, "red", Globals.UNCHOSEN_NOTE_FONT_COLOR_RED));
		ConfigData.setDefaultNoteFontColorGreen(XMLTransact.retrieveIntegerFromXML(colorText, "green", Globals.UNCHOSEN_NOTE_FONT_COLOR_GREEN));
		ConfigData.setDefaultNoteFontColorBlue(XMLTransact.retrieveIntegerFromXML(colorText, "blue", Globals.UNCHOSEN_NOTE_FONT_COLOR_BLUE));
		
		colorText = FauxXMLTool.retrieveSection(settingsText, "title_font_color");
		ConfigData.setDefaultTitleFontColorRed(XMLTransact.retrieveIntegerFromXML(colorText, "red", Globals.UNCHOSEN_TITLE_FONT_COLOR_RED));
		ConfigData.setDefaultTitleFontColorGreen(XMLTransact.retrieveIntegerFromXML(colorText, "green", Globals.UNCHOSEN_TITLE_FONT_COLOR_GREEN));
		ConfigData.setDefaultTitleFontColorBlue(XMLTransact.retrieveIntegerFromXML(colorText, "blue", Globals.UNCHOSEN_TITLE_FONT_COLOR_BLUE));
		
		value = XMLTransact.retrieveIntegerFromXML(settingsText, "border_width", Globals.UNCHOSEN_BORDER_WIDTH);
		ConfigData.setDefaultBorderWidth(value);
		
		value = XMLTransact.retrieveIntegerFromXML(settingsText, "note_width", Globals.UNCHOSEN_NOTE_WIDTH);
		ConfigData.setDefaultNoteWidth(value);
		
		value = XMLTransact.retrieveIntegerFromXML(settingsText, "note_height", Globals.UNCHOSEN_NOTE_HEIGHT);
		ConfigData.setDefaultNoteHeight(value);
		
		flag = XMLTransact.retrieveBooleanFromXML(settingsText, "confirm_delete", Globals.UNCHOSEN_CONFIRM_BEFORE_DELETE);
		ConfigData.setConfirmBeforeNoteDeletion(flag);
		
		flag = XMLTransact.retrieveBooleanFromXML(settingsText, "template_menu", Globals.UNCHOSEN_SHOW_TEMPLATE_ITEM_IN_TRAY_MENU);
		ConfigData.setShowTemplateItemInTrayMenu(flag);
		
		flag = XMLTransact.retrieveBooleanFromXML(settingsText, "show_splash", Globals.UNCHOSEN_SHOW_SPLASH_AT_STARTUP);
		ConfigData.setShowSplashAtStartup(flag);
		
		flag = XMLTransact.retrieveBooleanFromXML(settingsText, "play_sound", Globals.UNCHOSEN_PLAY_SOUND_ON_RECEIVING);
		ConfigData.setPlaySoundUponReceiving(flag);
		
		flag = XMLTransact.retrieveBooleanFromXML(settingsText, "notes_expire", Globals.UNCHOSEN_NOTES_EXPIRE);
		ConfigData.setNotesExpire(flag);
		
		value = XMLTransact.retrieveIntegerFromXML(settingsText, "expire_minutes", Globals.UNCHOSEN_EXPIRATION_MINUTES);
		ConfigData.setExpirationMinutes(value);
		
		ConfigData.setWebSafeFontNames(Globals.WEB_SAFE_FONTS);
		ConfigData.setFontSizes(Globals.SAFE_FONT_SIZES);
		
		try{
			TreeMap<String, ArrayList<String>> recipients = XMLTransact.retrieveRecipientTreeMapFromXML(rawText);
			ConfigData.setRecipients(recipients);
		}catch(Exception ex){
			System.out.println(ex.getMessage());
		}
		
		return;
	}
	
	private void retrieveTemplatesFromFiles()
	{
		// need to get a list of files, then read from the ones with a ".template" extension.
		try{
			String[] dirList = ListFilesInFolder.getFileNamesInFolder(Globals.TEMPLATES_PATH);
			TreeMap<String, TemplateData> templates = new TreeMap<String, TemplateData>();
			for(String item : dirList)
			{
				if(item.endsWith(".template")){
					this.retrieveTemplate(item, templates);
				}
			}
			ConfigData.setTemplates(templates);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return;
	}
	
	private void retrieveTemplate(String path, TreeMap<String, TemplateData> templates)
	{
		try{
			String rawText = ReadWriteFile.read(path);
			TemplateData template = XMLTransact.retrieveTemplateDataFromXML(rawText);
			if(template != null){
				templates.put(template.getTemplateName(), template);
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return;
	}
	
	private boolean reinstallTrayIcon()
	{
		boolean isSuccessful = true;
		try{
			if(!SystemTray.isSupported()){
				throw new Exception("This system does not support Java system tray operations.");
			}
			
			if(this.mTrayIcon != null){
				this.mSystemTray.remove(this.mTrayIcon);
				this.mTrayPopupMenu = null;
				TrayMenuListener.removeListener(this);
			}
			this.mTrayPopupMenu = new TrayPopupMenu(ConfigData.getShowTemplateItemInTrayMenu());
			if(ConfigData.getShowTemplateItemInTrayMenu()){
				this.recreateTemplatesMenu();
			}
			
			Image anImage = ImageLoader.getImageFromResourcePath(this.getClass().getResource(Globals.APPLICATION_ICON_16x16));
			this.mTrayIcon = new TrayIcon(anImage);
			this.mTrayIcon.setPopupMenu(this.mTrayPopupMenu);
			this.mTrayIcon.setToolTip("Tilt Notes");
			this.mTrayIcon.setImageAutoSize(true);
			this.mTrayIcon.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					createNewSession(SessionMode.NORMAL);
					return;
				}
			});
			this.mSystemTray = SystemTray.getSystemTray();
			this.mSystemTray.add(this.mTrayIcon);
			
			TrayMenuListener.addListener(this);
			
		}catch(AWTException awte){
			awte.printStackTrace();
			JOptionPane.showMessageDialog(null, "Unable to start system tray component.\n\nExiting program.", "Program Failed To Launch", JOptionPane.ERROR_MESSAGE);
			isSuccessful = false;
		}catch(Exception ex){
			ex.printStackTrace();
			JOptionPane.showMessageDialog(null, ex.getMessage() + "\n\nExiting program.", "Program Failed To Launch", JOptionPane.ERROR_MESSAGE);
			isSuccessful = false;
		}
		return isSuccessful;
	}
	
	private void recreateTemplatesMenu()
	{
		try{
			if(this.mTrayPopupMenu == null){
				throw new Exception("Tray popup menu is currently null.");
			}
			this.mTrayPopupMenu.resetTemplateMenu();
			ArrayList<String> templateNames = ConfigData.getTemplateNames();
			Collections.sort(templateNames);
			for(String name : templateNames)
			{
				if(ConfigData.getTemplate(name).getSessionPriority().compareTo(SessionPriority.URGENT) == 0){
					this.mTrayPopupMenu.createTemplateMenuItem(name, true);
				}else{
					this.mTrayPopupMenu.createTemplateMenuItem(name, false);
				}
			}
			if(templateNames.size() <= 0){
				this.mTrayPopupMenu.setTemplateMenuEnable(false);
			}else{
				this.mTrayPopupMenu.setTemplateMenuEnable(true);
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return;
	}
	
	/**
	 * Retrieves emoticon images from resource files named if Globals.EMOTICONS_LIST.
	 * @return boolean value true if successful, false otherwise.
	 */
	private boolean retrieveIcons()
	{
		boolean isSuccessful = false;
		
		try{
			Vector<Emoticon> emoticons = new Vector<Emoticon>();
			int count = Globals.EMOTICONS_LIST.length;
			for(int i = 0; i < count; i++)
			{
				ImageIcon anIcon = new ImageIcon(ImageLoader.getImageFromResourcePath(this.getClass().getResource(Globals.EMOTICONS_LIST[i])));
				emoticons.add(new Emoticon(anIcon.getImage(), Globals.EMOTICONS_LIST[i]));
			}
			ConfigData.setEmoticons(emoticons);
			isSuccessful = true;
		}catch(Exception ex){
			ex.printStackTrace();
			JOptionPane.showMessageDialog(null, "Unable to retrieve some program resources.", "Program Load Error", JOptionPane.ERROR_MESSAGE);
		}
		
		return isSuccessful;
	}
	
	private boolean reinstallCommClients()
	{
		boolean isSuccessful = false;
		
		this.shutdownTimersAndListeners();
		this.shutdownCommClientsAndListeners();
		
		try{
			this.mSessionClient = new CommClient(ConfigData.getMulticastIP(), ConfigData.getSessionPort(), Globals.BUFFER_SIZE);
			this.mSessionClientThread = new Thread(this.mSessionClient);
			CommClientListener.addListener(this);
			this.mSessionClientThread.start();
			isSuccessful = true;
		}catch(Exception ex){
			isSuccessful = false;
			ex.printStackTrace();
		}
		
		return isSuccessful;
	}
	
	private boolean reinstallTimersAndListeners()
	{
		boolean isSuccessful = false;
		
		this.shutdownTimersAndListeners();
		
		try{
			this.mSessionTimer = new Ticker(Globals.SESSION_TIMER_INTERVAL);
			this.mSessionTimer.startTimer();
			this.mCensusTimer = new Ticker(ConfigData.getCensusInterval());
			this.mCensusTimer.startTimer();
			TickListener.addListener(this);
			isSuccessful = true;
		}catch(Exception ex){
			isSuccessful = false;
			ex.printStackTrace();
		}
		
		return isSuccessful;
	}
	
	/**
	 * Creates a new, blank instance of SessionGUI. 
	 * @see MainApp#createNewSession(String)
	 * @see MainApp#createNewSession(SessionData)
	 */
	private void createNewSession(SessionMode mode)
	{
		try{
			UUID id = UUID.randomUUID();
			long now = System.currentTimeMillis();
			ArrayList<String> recipients = ConfigData.getRecipients("Everyone");
			ArrayList<Entry> entries = new ArrayList<Entry>();
			SessionData data = new SessionData(id, now, recipients, entries);
			data.setNoteColorRed(ConfigData.getDefaultNoteColorRed());
			data.setNoteColorGreen(ConfigData.getDefaultNoteColorGreen());
			data.setNoteColorBlue(ConfigData.getDefaultNoteColorBlue());
			data.setTitleColorRed(ConfigData.getDefaultTitleColorRed());
			data.setTitleColorGreen(ConfigData.getDefaultTitleColorGreen());
			data.setTitleColorBlue(ConfigData.getDefaultTitleColorBlue());
			data.setBorderColorRed(ConfigData.getDefaultBorderColorRed());
			data.setBorderColorGreen(ConfigData.getDefaultBorderColorGreen());
			data.setBorderColorBlue(ConfigData.getDefaultBorderColorBlue());
			data.setBorderWidth(ConfigData.getDefaultBorderWidth());
			data.setNoteFontFace(ConfigData.getDefaultNoteFontName());
			data.setNoteFontSize(ConfigData.getDefaultNoteFontSize());
			data.setNoteFontBold(ConfigData.getDefaultNoteFontBold());
			data.setNoteFontItalic(ConfigData.getDefaultNoteFontItalic());
			data.setNoteFontUnderline(ConfigData.getDefaultNoteFontUnderline());
			data.setTitleFontFace(ConfigData.getDefaultTitleFontName());
			data.setTitleFontSize(ConfigData.getDefaultTitleFontSize());
			data.setTitleFontBold(ConfigData.getDefaultTitleFontBold());
			data.setTitleFontItalic(ConfigData.getDefaultTitleFontItalic());
			data.setNoteFontColorRed(ConfigData.getDefaultNoteFontColorRed());
			data.setNoteFontColorGreen(ConfigData.getDefaultNoteFontColorGreen());
			data.setNoteFontColorBlue(ConfigData.getDefaultNoteFontColorBlue());
			data.setTitleFontColorRed(ConfigData.getDefaultTitleFontColorRed());
			data.setTitleFontColorGreen(ConfigData.getDefaultTitleFontColorGreen());
			data.setTitleFontColorBlue(ConfigData.getDefaultTitleFontColorBlue());
			data.setNoteWidth(ConfigData.getDefaultNoteWidth());
			data.setNoteHeight(ConfigData.getDefaultNoteHeight());
			data.setCustomTitle(ConfigData.getDefaultTitle());
			data.setSessionPriority(SessionPriority.NORMAL);
			data.setSessionMode(mode);
			data.setTemplateEditMode(TemplateEditMode.NEW_FILE);
			SessionGUI sGUI = new SessionGUI(data);
			this.mSessions.put(id, sGUI);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return;
	}
	
	/**
	 * Creates an instance of SessionGUI for received session data from other users.
	 * @param sData a SessionData object received from another user.
	 * @see MainApp#createNewSession()
	 * @see MainApp#createNewSession(String)
	 */
	private void createNewSession(SessionData sData)
	{
		try{
			SessionGUI sGUI = new SessionGUI(sData);
			this.mSessions.put(sData.getID(), sGUI);
			if(ConfigData.getPlaySoundUponReceiving()){
				// If this is the session's origin, don't play sound.
				// The assumption is that the first entry would be from the origin.
				// Also, if the list is empty, then this note must have been deleted before 
				// text entry, and has just been reopened.
				if(!sData.getEntries().isEmpty() && ConfigData.getHostName().compareToIgnoreCase(sData.getEntries().get(0).source) != 0){
					Toolkit.getDefaultToolkit().beep();
				}
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return;
	}
	
	/**
	 * This method routes incoming session data to the appropriate session window.
	 * The xml parameter is first converted to a SessionData object.
	 * The UUID is then read from the SessionData object, and is used to choose one of the following functions:
	 * (1) If the UUID belongs to an existing session, the SessionData affects that session.
	 * (2) If the UUID is not found among existing sessions, a new session is created based on the SessionData.
	 * @param xml String containing data ready to be parsed into a SessionData object.
	 */
	private void updateSessions(String xml)
	{
		try{
			SessionData sData = XMLTransact.parseXMLToSessionData(xml);
			// If the UUID belongs to an existing session, update that session.
			// If the UUID isn't found, pop up a new session.
			if(sData.getID() != null){
				if(this.mSessions.containsKey(sData.getID())){
					this.mSessions.get(sData.getID()).setSessionData(sData);
					this.mSessions.get(sData.getID()).refreshSessionTextPane();
					this.mSessions.get(sData.getID()).refreshGUIProperties();
				}else{
					this.createNewSession(sData);
				}
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return;
	}
	
	/**
	 * Opens a session based on a specified SessionData object in the history list.
	 * If the session is successfully created, the SessionData object is then removed
	 * from the history list.
	 * Any values related to the SessionData previous deletion are also reset. 
	 * @param sessionID UUID object uniquely identifying a SessionData Object.
	 */
	private void reopenSession(final UUID sessionID)
	{
		try{
			// Find the SessionData object with this UUID and createNewSession with it.
			boolean found = false;
			int count = this.mHistory.size();
			for(int i = 0; i < count; i++)
			{
				if(this.mHistory.get(i).getID().compareTo(sessionID) == 0){
					SessionData sData = this.mHistory.get(i);
					// Reviving a session requires resetting any previous deletion information.
					sData.setHiddenTime(0);
					sData.setHiddenEvent(null);
					sData.setDeletionTime(0);
					sData.setDeletionCommand(null);
					sData.setDeletionSource(null);
					this.createNewSession(sData);
					this.mSessions.get(this.mHistory.get(i).getID()).refreshSessionTextPane();
					this.mHistory.remove(i);
					found = true;
					break;
				}
			}
			if(!found){
				StringBuilder errorText = new StringBuilder();
				errorText.append("The selected note cannot be reopened because it was removed from the history list.\n\n");
				errorText.append("Deleted notes only remain on the history for ");
				errorText.append(String.valueOf(Globals.HISTORY_EXPIRATION / 1000.0));
				errorText.append(" minutes.");
				JOptionPane.showMessageDialog(null, errorText.toString(), "Cannot Reopen Note", JOptionPane.ERROR_MESSAGE);
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return;
	}
	
	/**
	 * Routes a deletion request based on a XMLTransact.DeletionType.
	 * @param xml String containing an XML-formatted deletion request.
	 */
	private void deleteSessions(String xml)
	{
		try{
			XMLTransact.DeletionType type = XMLTransact.retrieveDeletionRequestTypeFromXML(xml);
			String deletionSource = XMLTransact.retrieveStringFromXML(xml, "message_source", "error");
			UUID id = XMLTransact.retrieveDeletionRequestIDFromXML(xml);
			if(type.compareTo(XMLTransact.DeletionType.EVERYWHERE_BUT_HERE) == 0){
				if(ConfigData.getHostName().compareToIgnoreCase(deletionSource) != 0){
					this.deleteSession(id, deletionSource, type);
				}
			}else if(type.compareTo(XMLTransact.DeletionType.EVERYWHERE) == 0){
				this.deleteSession(id, deletionSource, type);
			}else if(type.compareTo(XMLTransact.DeletionType.ONLY_HERE) == 0){
				if(ConfigData.getHostName().compareToIgnoreCase(deletionSource) == 0){
					this.deleteSession(id, deletionSource, type);
				}
			}else if(type.compareTo(XMLTransact.DeletionType.EXPIRED) == 0){
				this.deleteSession(id, deletionSource, type);
			}else{
				throw new Exception("Deletion type unknown.");
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return;
	}
	
	/**
	 * Moves a SessionData object from the open sessions list to the history list.
	 * @param id UUID uniquely identifying a SessionData object.
	 * @param deletionSource String value containing the name of the computer which initiated the session's deletion.
	 * @param type XMLTransact.DeletionType value describing the nature of the session deletion.
	 * @throws Exception
	 */
	private void deleteSession(UUID id, String deletionSource, XMLTransact.DeletionType type) throws Exception
	{
		if(this.mSessions.containsKey(id)){
			this.mSessions.get(id).disposeWindow();
			this.mSessions.get(id).getSessionData().setDeletionTime(System.currentTimeMillis());
			this.mSessions.get(id).getSessionData().setDeletionSource(deletionSource);
			this.mSessions.get(id).getSessionData().setDeletionCommand(type.name());
			// Insert this SessionData object at the beginning of mHistory to maintain descending order.
			this.mHistory.add(0, this.mSessions.get(id).getSessionData());
			this.mSessions.remove(id);
		}
		return;
	}
	
	/**
	 * Attempts to save ConfigData values to an XML-formatted file.
	 */
	private void saveConfigDataToFile()
	{
		try{
			StringBuffer xml = XMLTransact.parseConfigDataToXML();
			ReadWriteFile.write(Globals.CONFIG_FILE_PATH, xml);
		}catch(IOException ioe){
			JOptionPane.showMessageDialog(null, "Unable to access config.mxl for writing.", "Save File Error", JOptionPane.WARNING_MESSAGE);
		}catch(Exception ex){
			StringWriter trace = new StringWriter();
			ex.printStackTrace(new PrintWriter(trace));
			JOptionPane.showMessageDialog(null, trace, "Open File Error", JOptionPane.WARNING_MESSAGE);
		}
		return;
	}
	
	private SessionData convertTemplateDataToSessionData(final TemplateData template)
	{
		SessionData sData = null;
		try{
			if(template == null){
				throw new Exception("TemplateData object is null.");
			}
			UUID id = UUID.randomUUID();
			long now = System.currentTimeMillis();
			ArrayList<String> recipients = template.getRecipients();
			ArrayList<Entry> entries = new ArrayList<Entry>();
			sData = new SessionData(id, now, recipients, entries);
			sData.setNoteColorRed(template.getNoteColorRed());
			sData.setNoteColorGreen(template.getNoteColorGreen());
			sData.setNoteColorBlue(template.getNoteColorBlue());
			sData.setTitleColorRed(template.getTitleColorRed());
			sData.setTitleColorGreen(template.getTitleColorGreen());
			sData.setTitleColorBlue(template.getTitleColorBlue());
			sData.setBorderColorRed(template.getBorderColorRed());
			sData.setBorderColorGreen(template.getBorderColorGreen());
			sData.setBorderColorBlue(template.getBorderColorBlue());
			sData.setBorderWidth(template.getBorderWidth());
			sData.setNoteFontFace(template.getNoteFontFace());
			sData.setNoteFontSize(template.getNoteFontSize());
			sData.setNoteFontBold(template.getTitleFontBold());
			sData.setNoteFontItalic(template.getNoteFontItalic());
			sData.setNoteFontUnderline(template.getNoteFontUnderline());
			sData.setTitleFontFace(template.getTitleFontFace());
			sData.setTitleFontSize(template.getTitleFontSize());
			sData.setTitleFontBold(template.getTitleFontBold());
			sData.setTitleFontItalic(template.getTitleFontItalic());
			sData.setNoteFontColorRed(template.getNoteFontColorRed());
			sData.setNoteFontColorGreen(template.getNoteFontColorGreen());
			sData.setNoteFontColorBlue(template.getNoteFontColorBlue());
			sData.setTitleFontColorRed(template.getTitleFontColorRed());
			sData.setTitleFontColorGreen(template.getTitleFontColorGreen());
			sData.setTitleFontColorBlue(template.getTitleFontColorBlue());
			sData.setNoteWidth(template.getNoteWidth());
			sData.setNoteHeight(template.getNoteHeight());
			sData.setCustomTitle(template.getCustomTitle());
			sData.setSessionPriority(template.getSessionPriority());
			sData.setSessionMode(SessionMode.EDIT_TEMPLATE);
			sData.getEntries().add(new Entry(ConfigData.getHostName(), template.getEntry()));
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return sData;
	}
	
 	private void stopAndCloseConfigurationGUI() // has not been finished yet !!!
	{
		this.mConfigGUI = null;
		ConfigurationListener.removeListener(this);
		return;
	}
	
 	/**
 	 * Disposes a HistoryGUI instance, and removes the HistoryListener.
 	 */
	private void stopAndCloseHistoryGUI()
	{
		if(this.mHistoryGUI != null){
			HistoryListener.removeListener(this);
			this.mHistoryGUI.disposeWindow();
			this.mHistoryGUI = null;
		}
		return;
	}
	
	private void shutdownCommClientsAndListeners()
	{
		CommClientListener.removeListener(this);
		if(this.mSessionClient != null){
			this.mSessionClient.requestStop();
		}
		return;
	}
	
	private void shutdownTimersAndListeners()
	{
		TickListener.removeListener(this);
		if(this.mSessionTimer != null){
			this.mSessionTimer.stopTimer();
			this.mSessionTimer = null;
		}
		if(this.mCensusTimer != null){
			this.mCensusTimer.stopTimer();
			this.mCensusTimer = null;
		}
	}
	
	/**
	 * Disposes open windows, removes custom listeners, stops timer and CommClient objects, and removes tray icon.
	 * Finishes by calling System.exit(0).
	 */
	private void beginApplicationShutdown()
	{
		this.shutdownCommClientsAndListeners(); // mSessionClient and mSessionClientThread, and removes CommClientListener.
		
		TemplateSettingsListener.removeListener(this);
		
		this.shutdownTimersAndListeners(); // mSessionTimer, mCensusTimer and removes TickListener.
		
		TrayMenuListener.removeListener(this);
		this.stopAndCloseConfigurationGUI(); // Also removes ConfigurationListener.
		this.stopAndCloseHistoryGUI(); // Also removes HistoryListener.
		this.mSystemTray.remove(this.mTrayIcon);
		System.exit(0);
		return;
	}
	
	/**
	 * Routes incoming messages based on their XMLTransact.MessageType value.
	 */
	public void commClientContentReceived(CommClientEvent cce)
	{
		ByteArrayInputStream bais = null;
		ObjectInputStream ois = null;
		String received = null;
		try{
			bais = new ByteArrayInputStream(cce.getContent());
			ois = new ObjectInputStream(bais);
			received = (String) ois.readObject();
			//System.out.println("Received: " + received);
			XMLTransact.MessageType type = XMLTransact.getMessageType(received);
			if(type.compareTo(XMLTransact.MessageType.CENSUS) == 0){
				if(this.mConfigGUI != null){
					User newUser = XMLTransact.retrieveCensusUserFromXML(received);
					this.mConfigGUI.addUserToCensusTable(newUser);
				}
			}else if(type.compareTo(XMLTransact.MessageType.SESSION) == 0){
				this.updateSessions(received);
			}else if(type.compareTo(XMLTransact.MessageType.DELETION) == 0){
				this.deleteSessions(received);
			}else if(type.compareTo(XMLTransact.MessageType.SHUTDOWN) == 0){
				this.beginApplicationShutdown();
			}
			
		}catch(IOException ioe){
			ioe.printStackTrace();
		}catch(ClassNotFoundException cnfe){
			cnfe.printStackTrace();
		}
		return;
	}
	
	public void configurationGUIClosePerformed(ConfigurationEvent ce)
	{
		this.stopAndCloseConfigurationGUI();
		return;
	}
	
	public void configurationGUISavePerformed(ConfigurationEvent ce)
	{
		ConfigData.setMulticastIP(ce.multicastIP);
		ConfigData.setSessionPort(ce.sessionPort);
		ConfigData.setCensusInterval(ce.censusInterval);
		ConfigData.setRecipients(ce.recipients);
		ConfigData.setUseNoteBorder(ce.useNoteBorder);
		ConfigData.setUseTimeForTitle(ce.useTimeForTitle);
		ConfigData.setDefaultTitle(ce.defaultTitle);
		ConfigData.setDefaultTitleAlignment(ce.defaultTitleAlignment);
		ConfigData.setDefaultNoteColorRed(ce.defaultNoteColorRed);
		ConfigData.setDefaultNoteColorGreen(ce.defaultNoteColorGreen);
		ConfigData.setDefaultNoteColorBlue(ce.defaultNoteColorBlue);
		ConfigData.setDefaultTitleColorRed(ce.defaultTitleColorRed);
		ConfigData.setDefaultTitleColorGreen(ce.defaultTitleColorGreen);
		ConfigData.setDefaultTitleColorBlue(ce.defaultTitleColorBlue);
		ConfigData.setDefaultBorderColorRed(ce.defaultBorderColorRed);
		ConfigData.setDefaultBorderColorGreen(ce.defaultBorderColorGreen);
		ConfigData.setDefaultBorderColorBlue(ce.defaultBorderColorBlue);
		ConfigData.setDefaultBorderWidth(ce.defaultBorderWidth);
		ConfigData.setDefaultNoteFontName(ce.defaultNoteFontName);
		ConfigData.setDefaultNoteFontSize(ce.defaultNoteFontSize);
		ConfigData.setDefaultNoteFontBold(ce.defaultNoteFontBold);
		ConfigData.setDefaultNoteFontItalic(ce.defaultNoteFontItalic);
		ConfigData.setDefaultNoteFontUnderline(ce.defaultNoteFontUnderline);
		ConfigData.setDefaultNoteFontStrikeout(ce.defaultNoteFontStrikeout);
		ConfigData.setDefaultNoteFontColorRed(ce.defaultNoteFontColorRed);
		ConfigData.setDefaultNoteFontColorGreen(ce.defaultNoteFontColorGreen);
		ConfigData.setDefaultNoteFontColorBlue(ce.defaultNoteFontColorBlue);
		ConfigData.setDefaultTitleFontName(ce.defaultTitleFontName);
		ConfigData.setDefaultTitleFontSize(ce.defaultTitleFontSize);
		ConfigData.setDefaultTitleFontBold(ce.defaultTitleFontBold);
		ConfigData.setDefaultTitleFontItalic(ce.defaultTitleFontItalic);
		ConfigData.setDefaultTitleFontColorRed(ce.defaultTitleFontColorRed);
		ConfigData.setDefaultTitleFontColorGreen(ce.defaultTitleFontColorGreen);
		ConfigData.setDefaultTitleFontColorBlue(ce.defaultTitleFontColorBlue);
		ConfigData.setDefaultNoteWidth(ce.defaultNoteWidth);
		ConfigData.setDefaultNoteHeight(ce.defaultNoteHeight);
		ConfigData.setConfirmBeforeNoteDeletion(ce.confirmBeforeNoteDeletion);
		ConfigData.setShowTemplateItemInTrayMenu(ce.showTemplateItemInTrayMenu);
		ConfigData.setShowSplashAtStartup(ce.showSplashAtStartup);
		ConfigData.setPlaySoundUponReceiving(ce.playSoundOnReceiving);
		ConfigData.setNotesExpire(ce.notesExpire);
		ConfigData.setExpirationMinutes(ce.expirationMinutes);
		
		this.retrieveTemplatesFromFiles();
		this.reinstallTrayIcon();
		this.reinstallCommClients();
		this.reinstallTimersAndListeners();
		
		this.stopAndCloseConfigurationGUI();
		
		this.saveConfigDataToFile();
		
		return;
	}
	
	public void historyGUIClearPerformed(HistoryEvent e)
	{
		this.mHistory.clear();
		this.historyGUIRefreshPerformed(e);
		return;
	}
	
	public void historyGUIRefreshPerformed(HistoryEvent e)
	{
		if(this.mHistoryGUI != null){
			this.mHistoryGUI.fillTable(this.mHistory);
		}
		return;
	}
	
	public void historyGUIReopenPerformed(HistoryEvent e)
	{
		this.reopenSession(e.getSessionID());
		return;
	}
	
	public void historyGUIClosePerformed(HistoryEvent e)
	{
		this.stopAndCloseHistoryGUI();
		return;
	}
	
	public void templateSettingsRequestNewPerformed(TemplateSettingsEvent e)
	{
		this.createNewSession(SessionMode.EDIT_TEMPLATE);
		return;
	}
	
	public void templateSettingsRequestOpenPerformed(TemplateSettingsEvent e)
	{
		try{
			if(e.getTemplateName() == null){
				throw new Exception("Template name cannot be null.");
			}
			if(ConfigData.getTemplate(e.getTemplateName()) == null){
				throw new Exception("Template name does not exist among collection of valid template names.");
			}
			SessionData sData = this.convertTemplateDataToSessionData(ConfigData.getTemplate(e.getTemplateName()));
			sData.setTemplateName(e.getTemplateName());
			sData.setTemplateEditMode(TemplateEditMode.EXISTING_FILE);
			this.createNewSession(sData);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return;
	}
	
	public void templateSettingsRequestSavePerformed(TemplateSettingsEvent e)
	{
		this.retrieveTemplatesFromFiles();
		if(ConfigData.getShowTemplateItemInTrayMenu()){
			this.recreateTemplatesMenu();
		}
		if(this.mConfigGUI != null){
			this.mConfigGUI.refillTemplatesTable();
		}
		return;
	}
	
	public void templateSettingsRequestDeletePerformed(TemplateSettingsEvent e)
	{
		this.retrieveTemplatesFromFiles();
		this.recreateTemplatesMenu();
		return;
	}
	
	/**
	 * Handles events from both mCensusTimer and mSessionTimer
	 */
	public void tickPerformed(TickEvent te)
	{
		if(te.getSource() == (Ticker)this.mCensusTimer){
			try{
				String xml = XMLTransact.parseCensusMessageToXML(ConfigData.getHostName(), ConfigData.getHostIPAddress());
				new CommServer(xml, ConfigData.getMulticastIP(), ConfigData.getHostIPAddress(), ConfigData.getSessionPort(), Globals.BUFFER_SIZE);
			}catch(NullPointerException npe){
				npe.printStackTrace();
			}catch(IOException ioe){
				ioe.printStackTrace();
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}else if(te.getSource() == (Ticker)this.mSessionTimer){
			if(ConfigData.getNotesExpire()){
				// Look through each of the current sessions to see if any have expired.
				for(UUID id : this.mSessions.keySet())
				{
					long modified = this.mSessions.get(id).getSessionData().getModified();
					if(System.currentTimeMillis() - modified > (ConfigData.getExpirationMinutes() * 60000)){
						String content = XMLTransact.parseDeleteRequestToXML(id, XMLTransact.DeletionType.EXPIRED);
						this.deleteSessions(content);
					}
				}
			}
		}
		return;
	}
	
	public void trayMenuNewNotePerformed(TrayMenuEvent tme)
	{
		this.createNewSession(SessionMode.NORMAL);
		return;
	}
	
	public void trayMenuTemplatePerformed(TrayMenuEvent tme)
	{
		try{
			if(tme.getTemplateName() == null){
				throw new Exception("Template name cannot be null.");
			}
			if(ConfigData.getTemplate(tme.getTemplateName()) == null){
				throw new Exception("Template name does not exist among collection of valid template names.");
			}
			SessionData sData = this.convertTemplateDataToSessionData(ConfigData.getTemplate(tme.getTemplateName()));
			sData.setTemplateName(tme.getTemplateName());
			sData.setSessionMode(SessionMode.USE_TEMPLATE);
			this.createNewSession(sData);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return;
	}
	
	public void trayMenuHistoryPerformed(TrayMenuEvent tme)
	{
		this.stopAndCloseHistoryGUI();
		this.mHistoryGUI = new HistoryGUI();
		this.mHistoryGUI.fillTable(this.mHistory);
		HistoryListener.addListener(this);
		return;
	}
	
	public void trayMenuSettingsPerformed(TrayMenuEvent tme)
	{
		if(this.mConfigGUI == null){
			this.mConfigGUI = new ConfigurationGUI();
			ConfigurationListener.addListener(this);
		}else{
			this.mConfigGUI.requestWindowFocus();
		}
		return;
	}
	
	public void trayMenuExitPerformed(TrayMenuEvent tme)
	{
		this.beginApplicationShutdown();
		return;
	}
	
	public static void main(String[] args)
	{
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable()
        {
            public void run()
            {
            	new MainApp();
            	return;
            }
        });
    }
}
