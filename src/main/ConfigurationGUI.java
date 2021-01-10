package main;

import java.awt.event.ComponentListener;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowListener;
import java.awt.event.WindowEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Image;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.UIManager;

import util.ImageLoader;

/**
 * A window allowing access to various program settings.
 * @author John McCullock
 * @version 2.0 2013-10-22
 */
public class ConfigurationGUI implements ActionListener, ComponentListener, TabEventListener, WindowListener
{
	private JFrame mFrame = null;
	private JPanel mBasePanel = null;
	private ConfigTabbedPanel mTabPanel = null;
	private JButton mSaveButton = null;
	private static final String SAVE_BUTTON = "Save any changes you've made.";
	private JButton mCancelButton = null;
	private static final String CANCEL_BUTTON = "Cancel and close.";
	private AboutWindow mAboutWindow = null;
	
	public ConfigurationGUI()
	{
		this.initializeMain();
		
		return;
	}
	
	private void initializeMain()
	{
		try{
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		}catch(Exception e){
			try{
				JFrame.setDefaultLookAndFeelDecorated(true);
			}catch(Exception ex){
				System.out.println("Error on look and feel");
			}
		}
		
		this.mFrame = new JFrame();
		this.mFrame.setTitle(Globals.CONFIGURATION_GUI_TITLE);
		try{
			Image anImage = ImageLoader.getImageFromResourcePath(this.getClass().getResource(Globals.APPLICATION_ICON_32x32));
			this.mFrame.setIconImage(anImage);	
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		this.mBasePanel = new JPanel();
		this.mBasePanel.setLayout(new BoxLayout(this.mBasePanel, BoxLayout.Y_AXIS));
		this.mBasePanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		
		this.mTabPanel = new ConfigTabbedPanel(this.mFrame);
		this.mBasePanel.add(this.mTabPanel);
		
		this.mBasePanel.add(this.initializeButtonRow());
		
		TabListener.addListener(this);
		
		KeyStroke ksEscape = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
		this.mFrame.getRootPane().registerKeyboardAction(this, ConfigurationGUI.CANCEL_BUTTON, ksEscape, JComponent.WHEN_IN_FOCUSED_WINDOW);
		KeyStroke ksEnter = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0);
		this.mFrame.getRootPane().registerKeyboardAction(this, ConfigurationGUI.SAVE_BUTTON, ksEnter, JComponent.WHEN_IN_FOCUSED_WINDOW);
		this.mFrame.addComponentListener(this);
		this.mFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		this.mFrame.getContentPane().add(this.mBasePanel);
		this.mFrame.setResizable(false);
		this.mFrame.setMinimumSize(Globals.CONFIGURATION_GUI_MINIMUM_SIZE);
		this.mFrame.setPreferredSize(Globals.CONFIGURATION_GUI_MINIMUM_SIZE); // this causes a weird screen placement.
		this.mFrame.setLocationRelativeTo(null); // set center screen.
		this.mFrame.setVisible(true);
		this.mFrame.pack();
		return;
	}
	
	private JPanel initializeButtonRow()
	{
		JPanel aPanel = new JPanel();
		aPanel.setLayout(new BoxLayout(aPanel, BoxLayout.X_AXIS));
		aPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 5, 0));
		
		this.mSaveButton = new JButton("Save and apply changes");
		this.mSaveButton.setToolTipText(ConfigurationGUI.SAVE_BUTTON);
		this.mSaveButton.setActionCommand(ConfigurationGUI.SAVE_BUTTON);
		this.mSaveButton.addActionListener(this);
		aPanel.add(this.mSaveButton);
		
		aPanel.add(Box.createHorizontalStrut(10));
		
		this.mCancelButton = new JButton("Cancel");
		this.mCancelButton.setToolTipText(ConfigurationGUI.CANCEL_BUTTON);
		this.mCancelButton.setActionCommand(ConfigurationGUI.CANCEL_BUTTON);
		this.mCancelButton.addActionListener(this);
		aPanel.add(this.mCancelButton);
		
		return aPanel;
	}
	
	public void requestWindowFocus()
	{
		this.mFrame.requestFocus();
		return;
	}
	
	private String checkForFormErrors()
	{
		String errorText = null;
		if(errorText == null){
			errorText = this.mTabPanel.getGeneralSettingsPanel().getFormErrors();
		}
		if(errorText == null){
			errorText = this.mTabPanel.getDefaultNoteSettingsPanel().getFormErrors();
		}
		if(errorText == null){
			errorText = this.mTabPanel.getNetworkSettingsPanel().getFormErrors();
		}
		
		return errorText;
	}
	
	public void addUserToCensusTable(User newUser)
	{
		this.mTabPanel.getCensusPanel().addUser(newUser);
		this.mTabPanel.getRecipientSettingsPanel().addUserToCensusTable(newUser);
		return;
	}
	
	public void refillTemplatesTable()
	{
		this.mTabPanel.getTemplateSettingsPanel().fillTable();
		return;
	}
	
	private void sendConfigurationSaveEvent()
	{
		try{
			ConfigurationListener.notifyConfigWindowSave(this,
													this.mTabPanel.getNetworkSettingsPanel().getMutlicastIP(), 
													this.mTabPanel.getNetworkSettingsPanel().getMulticastPort(), 
													this.mTabPanel.getNetworkSettingsPanel().getCensusInterval(),
													this.mTabPanel.getRecipientSettingsPanel().getRecipientsMap(), 
													this.mTabPanel.getDefaultNoteSettingsPanel().useNoteBorder(), 
													this.mTabPanel.getDefaultNoteSettingsPanel().useTimeForTitle(), 
													this.mTabPanel.getDefaultNoteSettingsPanel().getCustomTitle(), 
													this.mTabPanel.getDefaultNoteSettingsPanel().titleAlignment(), 
													this.mTabPanel.getDefaultNoteSettingsPanel().getDefaultNoteColorRed(),
													this.mTabPanel.getDefaultNoteSettingsPanel().getDefaultNoteColorGreen(),
													this.mTabPanel.getDefaultNoteSettingsPanel().getDefaultNoteColorBlue(),
													this.mTabPanel.getDefaultNoteSettingsPanel().getDefaultTitleColorRed(),
													this.mTabPanel.getDefaultNoteSettingsPanel().getDefaultTitleColorGreen(),
													this.mTabPanel.getDefaultNoteSettingsPanel().getDefaultTitleColorBlue(),
													this.mTabPanel.getDefaultNoteSettingsPanel().getDefaultBorderColorRed(),
													this.mTabPanel.getDefaultNoteSettingsPanel().getDefaultBorderColorGreen(),
													this.mTabPanel.getDefaultNoteSettingsPanel().getDefaultBorderColorBlue(),
													this.mTabPanel.getDefaultNoteSettingsPanel().getDefaultBorderWidth(),
													this.mTabPanel.getDefaultNoteSettingsPanel().getNoteFontName(), 
													this.mTabPanel.getDefaultNoteSettingsPanel().getNoteFontSize(),
													this.mTabPanel.getDefaultNoteSettingsPanel().getNoteFontBold(),
													this.mTabPanel.getDefaultNoteSettingsPanel().getNoteFontItalic(),
													this.mTabPanel.getDefaultNoteSettingsPanel().getNoteFontUnderline(),
													this.mTabPanel.getDefaultNoteSettingsPanel().getNoteFontStrikeout(),
													this.mTabPanel.getDefaultNoteSettingsPanel().getNoteFontColorRed(), 
													this.mTabPanel.getDefaultNoteSettingsPanel().getNoteFontColorGreen(),
													this.mTabPanel.getDefaultNoteSettingsPanel().getNoteFontColorBlue(),
													this.mTabPanel.getDefaultNoteSettingsPanel().getTitleFontName(),
													this.mTabPanel.getDefaultNoteSettingsPanel().getTitleFontSize(),
													this.mTabPanel.getDefaultNoteSettingsPanel().getTitleFontBold(),
													this.mTabPanel.getDefaultNoteSettingsPanel().getTitleFontItalic(),
													this.mTabPanel.getDefaultNoteSettingsPanel().getTitleFontColorRed(),
													this.mTabPanel.getDefaultNoteSettingsPanel().getTitleFontColorGreen(),
													this.mTabPanel.getDefaultNoteSettingsPanel().getTitleFontColorBlue(),
													this.mTabPanel.getDefaultNoteSettingsPanel().getNoteWidth(), 
													this.mTabPanel.getDefaultNoteSettingsPanel().getNoteHeight(),
													this.mTabPanel.getGeneralSettingsPanel().getDeleteConfirmation(),
													this.mTabPanel.getGeneralSettingsPanel().getTemplateInTrayMenu(),
													this.mTabPanel.getGeneralSettingsPanel().getSplashAtStartup(),
													this.mTabPanel.getGeneralSettingsPanel().getPlaySoundOnReceive(),
													this.mTabPanel.getGeneralSettingsPanel().getNotesExpire(),
													this.mTabPanel.getGeneralSettingsPanel().getExpirationMinutes());
		}catch(IllegalMonitorStateException imse){
			imse.printStackTrace();
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return;
	}
	
	public void actionPerformed(ActionEvent source)
	{
		if(source.getActionCommand() == ConfigurationGUI.CANCEL_BUTTON){
			this.disposeWindow();
		}else if(source.getActionCommand() == ConfigurationGUI.SAVE_BUTTON){
			this.mTabPanel.getRecipientSettingsPanel().synchronizeLists();
			String errorText = this.checkForFormErrors();
			if(errorText == null){
				this.sendConfigurationSaveEvent();
				this.disposeWindow();
			}else{
				JOptionPane.showMessageDialog(this.mFrame, errorText, "Errors Found", JOptionPane.WARNING_MESSAGE);
			}
		}
		return;
	}
	
	public void tabEventAboutButtonPerformed(TabEvent te)
	{
		if(this.mAboutWindow != null){
			this.mAboutWindow.disposeWindow();
			this.mAboutWindow = null;
		}
		this.mAboutWindow = new AboutWindow(this.mFrame);
		return;
	}
	
	public void componentHidden(ComponentEvent e)
	{
		this.disposeWindow();
		return;
	}
	
	public void componentShown(ComponentEvent e)	{ return; }
	public void componentMoved(ComponentEvent e)	{ return; }
	public void componentResized(ComponentEvent e)	{ return; }
	
	public void windowActivated(WindowEvent e)		{ return; }
	public void windowClosed(WindowEvent e)			{ return; }
    public void windowDeactivated(WindowEvent e)	{ return; }
    public void windowDeiconified(WindowEvent e)	{ return; }
    public void windowIconified(WindowEvent e)		{ return; }
    public void windowOpened(WindowEvent e)			{ return; }
    
    public void windowClosing(WindowEvent e)
    {
    	this.disposeWindow();
    }
    
    public void disposeWindow()
	{
		TabListener.removeListener(this);
		this.mTabPanel.getRecipientSettingsPanel().shutdownCensusGUI();
		this.mTabPanel.getTemplateSettingsPanel().shutdownTemplatePopupListener();
		this.mTabPanel.getCensusPanel().shutdownTicker();
		if(this.mAboutWindow != null){
			this.mAboutWindow.disposeWindow();
			this.mAboutWindow = null;
		}
		ConfigurationListener.notifyConfigWindowClose(this);
		this.mFrame.dispose();
		return;
	}
}
