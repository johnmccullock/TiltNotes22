package main;

import comm.CommServer;
import config.ConfigData;
import dialogs.Emoticon;
import dialogs.EmoticonDialog;
import frames.BareFrame;
import main.gui.text.StyleTransferHandler;
import util.DateFormatTool;
import util.FontTool;
import util.ImageLoader;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentListener;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowListener;
import java.awt.event.WindowEvent;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Toolkit;

import java.io.File;
import java.io.IOException;

import javax.swing.Action;
import javax.swing.border.Border;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Document;
import javax.swing.text.StyleConstants;
import javax.swing.text.Style;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;
import javax.swing.text.StyledEditorKit;
import javax.swing.UIManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;

import org.apache.commons.codec.binary.Base64;

/**
 * This is the instant messaging GUI which is central to this application.
 * 
 * The static enum Priority has two values: NORMAL and URGENT.  Normally, an instance of SessionGUI would be 
 * opened and wait for user input, but in some cases (like with templates) the message is predefined and the user
 * wants it sent immediately.  The WindowOpened event checks the priority value and multicasts the session data
 * immediately if set to URGENT.
 * 
 * This version includes keyboard event listeners to help alert users that they're keystrokes aren't going
 * where expected.  For non-touch-key typers (who aren't watching the screen as they type), the sudden popups
 * of this window were very annoying.  They'd be typing in one program, then this window pops up and their 
 * subsequent keystrokes were lost.  It's important for any of the first controls to get focus to have 
 * keyboard listeners for this warning to work.
 * 
 * @author John McCullock
 * @version 2.2 07/27/2013
 */

public class SessionGUI extends BareFrame implements ComponentListener,
														KeyListener, 
														WindowListener
{
	private static final int TITLE_BAR_MINIMUM_HEIGHT = 15;
	
	private BareFrame mFrame = null;
	private JPanel mBasePanel = null;
	private JPanel mTitleBar = null;
	private JLabel mTitleBarLabel = null;
	private JPanel mButtonRowPanel = null;
	private JButton mSendSaveButton = null;
	private static final String SEND_BUTTON = "Send";
	private static final String SAVE_BUTTON = "Save";
	private JButton mSendSaveButton2 = null;
	private static final String SAVE_AS_BUTTON = "Save As...";
	private JButton mCloseCancelButton = null;
	private static final String CLOSE_BUTTON = "Delete";
	private static final String CANCEL_BUTTON = "Cancel";
	private JButton mDropButton = null;
	private static final String DROP_BUTTON = "Show more Close and Delete options.";
	private JSplitPane mSplitPane = null;
	private JTextPane mTextEntryPane = null;
	private JTextPane mSessionTextPane = null;
	private JPopupMenu mEntryPopup = null;
	private JPopupMenu mSessionPopup = null;
	private HashMap<String, JMenuItem> mMenuItems = null;
	
	private boolean mBasePanelAutoScrolls = false;
	private Point mMousePoint = null;
	
	private SessionData mSessionData = null;
	
	private boolean mResetEditorAttributes = false;
	
	/**
	 * Creates a new instance of SessionGUI.
	 * @param data a SessionData object for use with this instance of SessionGUI
	 * @param priority a SessionPriority value.  The WindowOpened event checks this value and multicasts immediately if set to URGENT.
	 * @param mode a SessionGUI.Mode value.  This is used to indicate normal use, or template creation mode.
	 * @throws Exception possibly the null pointer exception from the setSessionData method.
	 */
	public SessionGUI(SessionData data) throws Exception
	{
		this.setSessionData(data);
		
		this.initializeMain();
		return;
	}
	
	/**
	 * Sets the session data property mSessionData.
	 * @param data a SessionData object for use with this instance of SessionGUI.
	 * @throws NullPointerException returned if data is null.
	 */
	public void setSessionData(SessionData data) throws Exception
	{
		try{
			if(data == null){
				throw new Exception("Parameter cannot be null.");
			}
			this.mSessionData = data;
		}catch(Exception ex){
			throw ex;
		}
		return;
	}
	
	public SessionData getSessionData()
	{
		return this.mSessionData;
	}
	
	public void setNoteFont(Font aFont)
	{
//		try{
//			if(aFont == null){
//				throw new NullPointerException("Parameter cannot be null.");
//			}
//			this.mSessionTextArea.setFont(aFont);
//		}catch(NullPointerException npe){
//			JOptionPane.showMessageDialog(this.mFrame, "Font choice cannot be null.", "Font Error", JOptionPane.WARNING_MESSAGE);
//			this.mSessionTextArea.setFont(Globals.UNCHOSEN_NOTE_FONT);
//		}catch(Exception ex){
//			JOptionPane.showMessageDialog(this.mFrame, "Unable to apply chosen note font.\n\nApplying built-in default font instead.", "Font Error", JOptionPane.WARNING_MESSAGE);
//			this.mSessionTextArea.setFont(Globals.UNCHOSEN_NOTE_FONT);
//		}
		return;
	}
	
	public void setTitleBackgroundColor(int red, int green, int blue)
	{
		try{
			if(red < 0 || red > 255){
				throw new Exception("Parameter is out of range: " + red);
			}
			if(green < 0 || green > 255){
				throw new Exception("Parameter is out of range: " + green);
			}
			if(blue < 0 || blue > 255){
				throw new Exception("Parameter is out of range: " + blue);
			}
			this.mTitleBar.setBackground(new Color(red, green, blue));
		}catch(Exception ex){
			JOptionPane.showMessageDialog(this.mFrame, "Unable to apply chosen title background color.\n\nApplying built-in default color instead.", "Color Error", JOptionPane.WARNING_MESSAGE);
			this.mTitleBar.setBackground(new Color(Globals.UNCHOSEN_TITLE_COLOR_RED, Globals.UNCHOSEN_TITLE_COLOR_GREEN, Globals.UNCHOSEN_TITLE_FONT_COLOR_BLUE));
		}
		return;
	}
	
	public void setTitleFont(Font aFont)
	{
		try{
			if(aFont == null){
				throw new NullPointerException("Parameter cannot be null.");
			}
			this.mTitleBarLabel.setFont(aFont);
		}catch(NullPointerException npe){
			JOptionPane.showMessageDialog(this.mFrame, "Font choice cannot be null.", "Font Error", JOptionPane.WARNING_MESSAGE);
			this.mTitleBarLabel.setFont(FontTool.getFont(Globals.UNCHOSEN_TITLE_FONT_FACE, Globals.UNCHOSEN_TITLE_FONT_SIZE, Globals.UNCHOSEN_TITLE_FONT_BOLD, Globals.UNCHOSEN_TITLE_FONT_ITALIC));
		}catch(Exception ex){
			JOptionPane.showMessageDialog(this.mFrame, "Unable to apply chosen title font.\n\nApplying built-in default font instead.", "Font Error", JOptionPane.WARNING_MESSAGE);
			this.mTitleBarLabel.setFont(FontTool.getFont(Globals.UNCHOSEN_TITLE_FONT_FACE, Globals.UNCHOSEN_TITLE_FONT_SIZE, Globals.UNCHOSEN_TITLE_FONT_BOLD, Globals.UNCHOSEN_TITLE_FONT_ITALIC));
		}
		return;
	}
	
	public void setTitleAlignment(ConfigData.Align alignment)
	{
		try{
			if(alignment == null){
				throw new NullPointerException("Parameter cannot be null.");
			}
			if(alignment.compareTo(ConfigData.Align.LEFT) == 0){
				this.mTitleBarLabel.setHorizontalAlignment(JLabel.LEFT);
			}else if(alignment.compareTo(ConfigData.Align.CENTER) == 0){
				this.mTitleBarLabel.setHorizontalAlignment(JLabel.CENTER);
			}else if(alignment.compareTo(ConfigData.Align.RIGHT) == 0){
				this.mTitleBarLabel.setHorizontalAlignment(JLabel.RIGHT);
			}
		}catch(NullPointerException npe){
			JOptionPane.showMessageDialog(this.mFrame, "Title alignment choice cannot be null.", "Font Error", JOptionPane.WARNING_MESSAGE);
			this.setBuiltInTitleAlignment();
		}catch(Exception ex){
			JOptionPane.showMessageDialog(this.mFrame, "Unable to apply chosen title alignment.\n\nApplying built-in default alignment instead.", "Text Align Error", JOptionPane.WARNING_MESSAGE);
			this.setBuiltInTitleAlignment();
		}
		return;
	}
	
	private void setBuiltInTitleAlignment()
	{
		if(Globals.UNCHOSEN_TITLE_ALIGNMENT.compareTo(ConfigData.Align.LEFT) == 0){
			this.mTitleBarLabel.setHorizontalAlignment(JLabel.LEFT);
		}else if(Globals.UNCHOSEN_TITLE_ALIGNMENT.compareTo(ConfigData.Align.CENTER) == 0){
			this.mTitleBarLabel.setHorizontalAlignment(JLabel.CENTER);
		}else if(Globals.UNCHOSEN_TITLE_ALIGNMENT.compareTo(ConfigData.Align.RIGHT) == 0){
			this.mTitleBarLabel.setHorizontalAlignment(JLabel.RIGHT);
		}
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
				ex.printStackTrace();
			}
		}
		
		this.mFrame = new BareFrame(ConfigData.getDefaultBorderWidth());
		try{
			Image anImage = ImageLoader.getImageFromResourcePath(this.getClass().getResource(Globals.APPLICATION_ICON_32x32));
			this.mFrame.setIconImage(anImage);	
		}catch(Exception ex){
			ex.printStackTrace();
		}
		this.mBasePanel = new JPanel();
		this.mBasePanel.setLayout(new BoxLayout(this.mBasePanel, BoxLayout.Y_AXIS));
		this.reinitializeBorder();
		this.mBasePanel.setBackground(new Color(this.mSessionData.getNoteColorRed(), this.mSessionData.getNoteColorGreen(), this.mSessionData.getNoteColorBlue()));
		
		this.mBasePanel.add(this.createTitleBar());
		this.mBasePanel.add(this.createButtonRowPanel());
		this.mBasePanel.add(this.createTextPanes());
		
		this.refreshMessageContent();
		
		this.mFrame.addWindowListener(this);
		this.mFrame.addComponentListener(this);
		this.mFrame.setUndecorated(true);
		this.mFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		this.mFrame.getContentPane().add(this.mBasePanel);
		this.mFrame.setMinimumSize(new Dimension(this.mSessionData.getNoteWidth(), this.mSessionData.getNoteHeight()));
		this.mFrame.setLocationByPlatform(true); // otherwise they all appear on top of each other.
		this.mFrame.setVisible(true);
		this.mFrame.toFront();
		this.mFrame.pack();
		return;
	}
	
	private void reinitializeBorder()
	{
		Border inner = BorderFactory.createEmptyBorder(0, 2, 1, 2);
		Border outer = BorderFactory.createLineBorder(new Color(this.mSessionData.getBorderColorRed(), 
																this.mSessionData.getBorderColorGreen(), 
																this.mSessionData.getBorderColorBlue()), 
														this.mSessionData.getBorderWidth());
		this.mBasePanel.setBorder(BorderFactory.createCompoundBorder(outer, inner));
		return;
	}
	
	private MouseListener createMouseListener()
	{
		return new MouseListener()
		{
			public void mouseClicked(MouseEvent e){ return; }
			public void mouseEntered(MouseEvent e){ return; }
			public void mouseExited(MouseEvent e){ return; }
			
			public void mousePressed(MouseEvent e)
			{
				// Popup menus weren't disappearing as expected.  So this function was added.
				hideAllPopupMenus();
				
				//this.mMousePoint = e.getPoint(); // Letting mouseMoved do it instead.
				mBasePanelAutoScrolls = mTitleBar.getAutoscrolls();
				mTitleBar.setAutoscrolls(false);
				return;
			}
			
			public void mouseReleased(MouseEvent e)
			{
				mTitleBar.setAutoscrolls(mBasePanelAutoScrolls);
				return;
			}
		};
	}
	
	private JPanel createTitleBar()
	{
		// Let adjustTitleBarSize() set the title bar size values at the end of this method.
		this.mTitleBar = new JPanel();
		this.mTitleBar.setLayout(new BoxLayout(this.mTitleBar, BoxLayout.X_AXIS));
		this.mTitleBar.setBackground(new Color(this.mSessionData.getTitleColorRed(), this.mSessionData.getTitleColorGreen(), this.mSessionData.getTitleColorBlue()));
		this.mTitleBar.addMouseListener(this.createMouseListener());
		this.mTitleBar.addMouseMotionListener(new MouseMotionListener()
		{
			public void mouseDragged(MouseEvent e)
			{
				mFrame.setLocation(e.getXOnScreen() - (mMousePoint.x + (ConfigData.getDefaultBorderWidth() * 2)), e.getYOnScreen() - (mMousePoint.y + ConfigData.getDefaultBorderWidth()));	
				return;
			}
			
			public void mouseMoved(MouseEvent e)
			{
				mFrame.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
				mMousePoint = e.getPoint();
				return;
			}
		});
		
		this.mTitleBarLabel = new JLabel();
		this.mTitleBarLabel.setMinimumSize(new Dimension(this.mSessionData.getNoteWidth(), SessionGUI.TITLE_BAR_MINIMUM_HEIGHT));
		this.mTitleBarLabel.setMaximumSize(new Dimension(2000, 75));
		if(ConfigData.getUseTimeForTitle() || ConfigData.getDefaultTitle() == null){
			this.mTitleBarLabel.setText(this.setDateStringForTitle());
		}else{
			this.mTitleBarLabel.setText(ConfigData.getDefaultTitle());
		}
		this.setTitleAlignment(ConfigData.getDefaultTitleAlignment());
		this.mTitleBarLabel.setFont(FontTool.getFont(this.mSessionData.getTitleFontFace(), this.mSessionData.getTitleFontSize(), this.mSessionData.getTitleFontBold(), this.mSessionData.getTitleFontItalic()));
		this.mTitleBarLabel.setForeground(new Color(this.mSessionData.getTitleFontColorRed(), this.mSessionData.getTitleFontColorGreen(), this.mSessionData.getTitleFontColorBlue()));
		this.mTitleBar.add(this.mTitleBarLabel);
		
		this.adjustTitleBarSize();
		
		return this.mTitleBar;
	}
	
	private void adjustTitleBarSize()
	{
		FontMetrics fm = getFontMetrics(this.mTitleBarLabel.getFont());
		if(fm.getHeight() > SessionGUI.TITLE_BAR_MINIMUM_HEIGHT){
			mTitleBar.setPreferredSize(new Dimension(mFrame.getWidth(), fm.getHeight()));
		}
		return;
	}
	
	private JPanel createButtonRowPanel()
	{
		this.mButtonRowPanel = new JPanel();
		this.mButtonRowPanel.setLayout(new BoxLayout(this.mButtonRowPanel, BoxLayout.X_AXIS));
		this.mButtonRowPanel.setPreferredSize(new Dimension(this.mSessionData.getNoteWidth(), 27));
		this.mButtonRowPanel.setOpaque(false);
		this.mButtonRowPanel.addMouseListener(this.createMouseListener());
		this.mButtonRowPanel.addMouseMotionListener(new MouseMotionListener()
		{
			public void mouseDragged(MouseEvent e)
			{
				// This mouseDragged version differs from the one for mTitleBar because
				// mTitleBar.getHeight has to be added.
				mFrame.setLocation(e.getXOnScreen() - (mMousePoint.x + (ConfigData.getDefaultBorderWidth() * 2)), e.getYOnScreen() - (mMousePoint.y + ConfigData.getDefaultBorderWidth() + mTitleBar.getHeight()));	
				return;
			}
			
			public void mouseMoved(MouseEvent e)
			{
				mFrame.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
				mMousePoint = e.getPoint();
				return;
			}
		});
		this.mBasePanel.add(this.mButtonRowPanel);
		
		if(this.mSessionData.getSessionMode().compareTo(SessionMode.EDIT_TEMPLATE) == 0){
			this.createSaveButtons();
		}else{
			this.createSendButtons();
		}
		
		this.mButtonRowPanel.add(Box.createVerticalStrut(16));
		
		if(this.mSessionData.getSessionMode().compareTo(SessionMode.EDIT_TEMPLATE) == 0){
			this.createCancelButton();
		}else{
			this.createCloseButton();
		}
		
		this.createDropButton();
		
		return this.mButtonRowPanel;
	}
	
	private void createSaveButtons()
	{
		if(this.mSessionData.getTemplateEditMode().compareTo(TemplateEditMode.NEW_FILE) == 0){
			this.mSendSaveButton = new JButton("<html><p>Save As...</p></html>");
			this.mSendSaveButton.setActionCommand(SessionGUI.SAVE_AS_BUTTON);
			this.mSendSaveButton.setToolTipText("Save template.");
			this.mSendSaveButton = this.createButtonBarButton(this.mSendSaveButton);
			this.mSendSaveButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					saveAsTemplate();
					return;
				}
			});
			this.mButtonRowPanel.add(this.mSendSaveButton);
		}else{
			this.mSendSaveButton = new JButton("<html><p>Save</p></html>");
			this.mSendSaveButton.setActionCommand(SessionGUI.SAVE_BUTTON);
			this.mSendSaveButton.setToolTipText("Save template.");
			this.mSendSaveButton = this.createButtonBarButton(this.mSendSaveButton);
			this.mSendSaveButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					saveAsTemplate();
					return;
				}
			});
			this.mButtonRowPanel.add(this.mSendSaveButton);
			this.mButtonRowPanel.add(Box.createRigidArea(new Dimension(5, 16)));
			this.mSendSaveButton2 = new JButton("<html><p>Save As...</p></html>");
			this.mSendSaveButton2.setActionCommand(SessionGUI.SAVE_AS_BUTTON);
			this.mSendSaveButton2.setToolTipText("Save with specific name.");
			this.mSendSaveButton2 = this.createButtonBarButton(this.mSendSaveButton2);
			this.mSendSaveButton2.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					saveAsTemplate();
					return;
				}
			});
			this.mButtonRowPanel.add(this.mSendSaveButton2);
		}
		return;
	}
	
	private void createSendButtons()
	{
		this.mSendSaveButton = new JButton("<html><p>Send</p></html>");
		this.mSendSaveButton.setActionCommand(SessionGUI.SEND_BUTTON);
		this.mSendSaveButton = this.createButtonBarButton(this.mSendSaveButton);
		JPopupMenu sendPopup = new JPopupMenu();
		this.fillSendMenu(sendPopup);
		this.mSendSaveButton.setComponentPopupMenu(sendPopup);
		this.mSendSaveButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				mSendSaveButton.getComponentPopupMenu().show(mSendSaveButton, mSendSaveButton.getWidth(), 0);
				return;
			}
		});
		if(ConfigData.getRecipients("_network") != null){
			if(!ConfigData.getRecipients("_network").isEmpty()){
				this.mSendSaveButton.setEnabled(true);
				this.mSendSaveButton.setToolTipText(SessionGUI.SEND_BUTTON);
			}else{
				this.mSendSaveButton.setEnabled(false);
				this.mSendSaveButton.setToolTipText("<html><p>There are currently no recipients listed in your TiltNotes settings.</p><p>Please use the TiltNotes Settings window to add recipients.</p></html>");
			}
		}
		this.mButtonRowPanel.add(this.mSendSaveButton);
		return;
	}
	
	private void createCancelButton()
	{
		this.mCloseCancelButton = new JButton("<html><p>Cancel</p></html>");
		this.mCloseCancelButton.setToolTipText(SessionGUI.CANCEL_BUTTON);
		this.mCloseCancelButton = this.createButtonBarButton(this.mCloseCancelButton);
		this.mCloseCancelButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				completeDeletionRequest(XMLTransact.DeletionType.ONLY_HERE);
				return;
			}
		});
		this.mButtonRowPanel.add(this.mCloseCancelButton);
		return;
	}
	
	private void createCloseButton()
	{
		this.mCloseCancelButton = new JButton("<html><p>Close</p></html>");
		this.mCloseCancelButton.setToolTipText(SessionGUI.CLOSE_BUTTON);
		this.mCloseCancelButton = this.createButtonBarButton(this.mCloseCancelButton);
		this.mCloseCancelButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				beginDeletionRequest(XMLTransact.DeletionType.ONLY_HERE);
				return;
			}
		});
		this.mButtonRowPanel.add(this.mCloseCancelButton);
		return;
	}
	
	private JButton createButtonBarButton(JButton aButton)
	{
		aButton.setMaximumSize(new Dimension(70, 25));
		aButton.setPreferredSize(new Dimension(70, 25));
		aButton.setMargin(new Insets(0, 0, 0, 0));
		aButton.setOpaque(false);
		aButton.addKeyListener(this);
		return aButton;
	}
	
	private void createDropButton()
	{
		this.mDropButton = new JButton();
		if(this.mSessionData.getSessionMode().compareTo(SessionMode.EDIT_TEMPLATE) == 0){
			this.mDropButton.setEnabled(false);
		}else{
			this.mDropButton.addKeyListener(this);
			this.mDropButton.setActionCommand(SessionGUI.DROP_BUTTON);
			JPopupMenu deleteMenu = new JPopupMenu("Delete...");
			this.fillDeleteMenu(deleteMenu);
			this.mDropButton.setComponentPopupMenu(deleteMenu);
			this.mDropButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					mDropButton.getComponentPopupMenu().show(mDropButton, 0, 0);
					return;
				}
			});
		}
		try{
			ImageIcon anImage = new ImageIcon(ImageLoader.getImageFromResourcePath(this.getClass().getResource("/images/drop_arrow1.png")));
			this.mDropButton.setIcon(anImage);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		this.mDropButton.setMaximumSize(new Dimension(18, 25));
		this.mDropButton.setPreferredSize(new Dimension(18, 25));
		this.mDropButton.setToolTipText(SessionGUI.DROP_BUTTON);
		this.mDropButton.setOpaque(false);
		
		this.mButtonRowPanel.add(this.mDropButton);
		return;
	}
	
	private JPanel createTextPanes()
	{
		JPanel aPanel = new JPanel();
		aPanel.setLayout(new BoxLayout(aPanel, BoxLayout.X_AXIS));
		aPanel.setMinimumSize(new Dimension(200, 100));
		
		this.mMenuItems = new HashMap<String, JMenuItem>();
		
		this.mEntryPopup = new JPopupMenu();
		this.createTextPanePopupMenu(this.mEntryPopup, true);
		this.mEntryPopup.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		
		this.mSessionPopup = new JPopupMenu();
		this.createTextPanePopupMenu(this.mSessionPopup, false);
		this.mSessionPopup.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		
		this.mSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, 
											this.createTextEntryPanel(), 
											this.createSessionTextPane());
		this.mSplitPane.setDividerLocation(50);
		this.mSplitPane.setBackground(new Color(this.mSessionData.getNoteColorRed(), this.mSessionData.getNoteColorGreen(), this.mSessionData.getNoteColorBlue()));
		this.mSplitPane.setBorder(null);
		
		this.setSplitPaneDividerBackgroundColor(new Color(this.mSessionData.getNoteColorRed(), this.mSessionData.getNoteColorGreen(), this.mSessionData.getNoteColorBlue()));
				
		aPanel.add(this.mSplitPane);
		
		return aPanel;
	}
	
	private JScrollPane createTextEntryPanel()
	{
		this.mTextEntryPane = new JTextPane(new DefaultStyledDocument());
		this.mTextEntryPane.setMinimumSize(new Dimension(200, 30));
		this.mTextEntryPane.setMaximumSize(new Dimension(1000, 1000));
		this.mTextEntryPane.setBackground(new Color(this.mSessionData.getNoteColorRed(), this.mSessionData.getNoteColorGreen(), this.mSessionData.getNoteColorBlue()));
		this.mTextEntryPane.setEditorKit(new StyledEditorKit());
		this.mTextEntryPane.setLogicalStyle(this.setTextEntryPaneDefaultStyle());
		this.mTextEntryPane.addCaretListener(new CaretListener()
		{
			public void caretUpdate(CaretEvent e)
			{
				// This was added to remove previous input attributes remaining in the text entry pane after the
				// clearing it.
				if(mResetEditorAttributes){
					setTextEntryPaneDefaultStyle();
					mResetEditorAttributes = false;
				}
				return;
			}
		});
		this.mTextEntryPane.setTransferHandler(new StyleTransferHandler());
		this.mTextEntryPane.addMouseListener(this.getTextEntryPaneMouseListener());
		this.mTextEntryPane.registerKeyboardAction(this.getTextEntryPaneKeyActionListener(), 
													KeyStroke.getKeyStroke(KeyEvent.VK_F12, 0), 
													JComponent.WHEN_FOCUSED);
		JScrollPane sPanel = new JScrollPane(this.mTextEntryPane,
							                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
							                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		return sPanel;
	}
	
	private Style setTextEntryPaneDefaultStyle()
	{
		Style defStyle = new StyleContext().getStyle(StyleContext.DEFAULT_STYLE);	
		StyleConstants.setFontFamily(defStyle, this.mSessionData.getNoteFontFace());
		StyleConstants.setFontSize(defStyle, this.mSessionData.getNoteFontSize());
		StyleConstants.setBold(defStyle, this.mSessionData.getNoteFontBold());
		StyleConstants.setItalic(defStyle, this.mSessionData.getNoteFontItalic());
		StyleConstants.setUnderline(defStyle, this.mSessionData.getNoteFontUnderline());
		Color fontColor = new Color(this.mSessionData.getNoteFontColorRed(), this.mSessionData.getNoteFontColorGreen(), this.mSessionData.getNoteFontColorBlue());
		StyleConstants.setForeground(defStyle, fontColor);
		return defStyle;
	}
	
	private MouseListener getTextEntryPaneMouseListener()
	{
		return new MouseListener()
		{
			public void mouseClicked(MouseEvent e)			{ return; }
			public void mouseEntered(MouseEvent e)			{ return; }
			public void mouseExited(MouseEvent e)			{ return; }
			public void mousePressed(MouseEvent e)			{ return; }
			
			public void mouseReleased(MouseEvent e)
			{
				if(e.isPopupTrigger()){
					mEntryPopup.show(e.getComponent(), e.getX(), e.getY());
				}else{
					// Popup menu wasn't disappearing as expected.  So this else clause was added.
					hideAllPopupMenus();
				}
				return;
			}
		};
	}
	
	private ActionListener getTextEntryPaneKeyActionListener()
	{
		return new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				String text = mTextEntryPane.getText();
				int pos = text.indexOf("^", 0);
				if(pos > -1){
					mTextEntryPane.setSelectionStart(pos);
					mTextEntryPane.setSelectionEnd(pos + 1);
					mTextEntryPane.replaceSelection("");
				}
				return;
			}
		};
	}
	
	private JScrollPane createSessionTextPane()
	{
		this.mSessionTextPane = new JTextPane();
		this.mSessionTextPane.setMaximumSize(new Dimension(1000, 1000));
		this.mSessionTextPane.setBackground(new Color(this.mSessionData.getNoteColorRed(), this.mSessionData.getNoteColorGreen(), this.mSessionData.getNoteColorBlue()));
		this.mSessionTextPane.setEditable(false);
		this.mSessionTextPane.setTransferHandler(new StyleTransferHandler());
		this.mSessionTextPane.addMouseListener(new MouseListener()
		{
			public void mouseClicked(MouseEvent e)			{ return; }
			
			public void mouseEntered(MouseEvent e)
			{
				// this is used to overcome some odd cursor glitches associated with the resizeable border.
				mSessionTextPane.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
				return;
			}
			
			public void mouseExited(MouseEvent e)			{ return; }
			public void mousePressed(MouseEvent e)			{ return; }
			
			public void mouseReleased(MouseEvent e)
			{
				if(e.isPopupTrigger()){
					mSessionPopup.show(e.getComponent(), e.getX(), e.getY());
				}else{
					// Popup menus weren't disappearing as expected.  So this else clause was added.
					hideAllPopupMenus();
				}
				return;
			}
		});
		
		JScrollPane sPanel = new JScrollPane(this.mSessionTextPane,
							                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
							                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		sPanel.setBorder(null);
		return sPanel;
	}
	
	private JPopupMenu createTextPanePopupMenu(JPopupMenu popup, boolean writeOptionsEnabled)
	{
		if(this.mSessionData.getSessionMode() == SessionMode.NORMAL){
			popup.add(this.createSendMenu("Send"));
			popup.addSeparator();
		}
		
		JMenu aMenu = this.createSessionFontMenu("Edit font");
		if(!writeOptionsEnabled){
			aMenu.setEnabled(writeOptionsEnabled);
		}
		popup.add(aMenu);
		
		popup.add(this.createColorMenu("Edit colors"));
		
		popup.add(this.createEditOtherMenu("Edit other"));
		
		popup.addSeparator();
		
		JMenuItem item = this.createCutCopyPasteMenuItem(new DefaultEditorKit.CutAction(), "Cut", Globals.CUT_ICON_PATH);
		item.setEnabled(writeOptionsEnabled);
		popup.add(item);
		
		popup.add(this.createCutCopyPasteMenuItem(new DefaultEditorKit.CopyAction(), "Copy", Globals.COPY_ICON_PATH));
		
		item = this.createCutCopyPasteMenuItem(new DefaultEditorKit.PasteAction(), "Paste", Globals.PASTE_ICON_PATH);
		item.setEnabled(writeOptionsEnabled);
		popup.add(item);
		
		popup.addSeparator();
		
		item = this.createEmoticonMenuItem("Insert emoticon...");
		if(writeOptionsEnabled){
			item.addActionListener(this.createEmoticonMenuItemActionListener());
		}else{
			item.setEnabled(writeOptionsEnabled);
		}
		popup.add(item);
		
		if(this.mSessionData.getSessionMode() == SessionMode.NORMAL){
			popup.addSeparator();
			
			popup.add(this.createHideMenuItem("Hide note"));
			
			aMenu = new JMenu("Delete");
			this.fillDeleteMenu(aMenu);
			popup.add(aMenu);
		}
		
		return popup;
	}
	
	private JMenu createSendMenu(String caption)
	{
		JMenu aMenu = new JMenu(caption);
		this.fillSendMenu(aMenu);
		try{
			aMenu.setIcon(new ImageIcon(ImageLoader.getImageFromResourcePath(this.getClass().getResource(Globals.EMAIL_SEND_ICON_PATH))));
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return aMenu;
	}
	
	private void fillSendMenu(JComponent aMenu)
	{
		JMenuItem item = new JMenuItem("Everyone");
		item.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				broadcastSessionData("Everyone");
				return;
			}
		});
		if(ConfigData.getRecipients("_network") != null){
			item.setEnabled(!ConfigData.getRecipients("_network").isEmpty());
		}
		aMenu.add(item);
		
		ArrayList<String> listNames = ConfigData.getRecipientLists();
		Collections.sort(listNames);
		for(final String name : listNames)
		{
			if(name.compareToIgnoreCase("everyone") != 0 && name.compareToIgnoreCase("_network") != 0){
				item = new JMenuItem(name);
				item.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						broadcastSessionData(name);
						return;
					}
				});
				aMenu.add(item);
			}
		}
		
		return;
	}
	
	private JMenu createSessionFontMenu(String caption)
	{
		JMenu main = new JMenu(caption);
		
		JMenu submenu = new JMenu("Font");
		for(final String name : ConfigData.getWebSafeFontNames())
		{
			JCheckBoxMenuItem item = new JCheckBoxMenuItem(name);
			item.setFont(new Font(name, Font.PLAIN, 14));
			item.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					Action fontAction = new StyledEditorKit.FontFamilyAction(name, name);
					fontAction.actionPerformed(e);
					return;
				}
			});
			submenu.add(item);
			this.mMenuItems.put("font_name_" + name, item);
		}
		main.add(submenu);
		
		submenu = new JMenu("Size");
		for(final int size : ConfigData.getFontSizes())
		{
			JCheckBoxMenuItem item = new JCheckBoxMenuItem(String.valueOf(size));
			item.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, size));
			item.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					Action fontAction = new StyledEditorKit.FontSizeAction(String.valueOf(size), size);
					fontAction.actionPerformed(e);
					return;
				}
			});
			submenu.add(item);
			this.mMenuItems.put("font_size_" + String.valueOf(size), item);
		}
		main.add(submenu);
		
		JCheckBoxMenuItem item = new JCheckBoxMenuItem("Bold");
		item.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				new StyledEditorKit.BoldAction().actionPerformed(e);
				return;
			}
		});
		main.add(item);
		this.mMenuItems.put("font_style_bold", item);
		
		item = new JCheckBoxMenuItem("Italic");
		item.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				new StyledEditorKit.ItalicAction().actionPerformed(e);
				return;
			}
		});
		main.add(item);
		this.mMenuItems.put("font_style_italic", item);
		
		item = new JCheckBoxMenuItem("Underline");
		item.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				new StyledEditorKit.UnderlineAction().actionPerformed(e);
				return;
			}
		});
		main.add(item);
		this.mMenuItems.put("font_style_underline", item);
		
		JMenuItem item2 = new JMenuItem("Color...");
		item2.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				Color aColor = JColorChooser.showDialog(mFrame, "Choose Note Foreground Color", new Color(mSessionData.getNoteFontColorRed(), mSessionData.getNoteFontColorGreen(), mSessionData.getNoteFontColorBlue()));
				if(aColor != null){
					mSessionData.setNoteFontColorRed(aColor.getRed());
					mSessionData.setNoteFontColorGreen(aColor.getGreen());
					mSessionData.setNoteFontColorBlue(aColor.getBlue());
					new StyledEditorKit.ForegroundAction("CustomColor", aColor).actionPerformed(e);
				}
				return;
			}
		});
		main.add(item2);
		
		return main;
	}
		
	private JMenu createColorMenu(String caption)
	{
		JMenu aMenu = new JMenu(caption);
		try{
			aMenu.setIcon(new ImageIcon(ImageLoader.getImageFromResourcePath(this.getClass().getResource(Globals.BACKGROUND_COLOR_ICON_PATH))));
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		JMenuItem item = new JMenuItem("Note color...");
		item.addActionListener(this.createNoteColorMenuItemActionListener());
		aMenu.add(item);
		
		item = new JMenuItem("Title color...");
		item.addActionListener(this.createTitleColorMenuItemActionListener());
		aMenu.add(item);
		
		item = new JMenuItem("Border color...");
		item.addActionListener(this.createBorderColorMenuItemActionListener());
		aMenu.add(item);
		return aMenu;
	}

	private ActionListener createNoteColorMenuItemActionListener()
	{
		return new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				Color aColor = JColorChooser.showDialog(mFrame, "Choose Note Background Color", new Color(mSessionData.getNoteColorRed(), mSessionData.getNoteColorGreen(), mSessionData.getNoteColorBlue()));
				if(aColor != null){
					mSessionData.setNoteColorRed(aColor.getRed());
					mSessionData.setNoteColorGreen(aColor.getGreen());
					mSessionData.setNoteColorBlue(aColor.getBlue());
					mBasePanel.setBackground(aColor);
					mSplitPane.setBackground(aColor);
					setSplitPaneDividerBackgroundColor(aColor);
					mTextEntryPane.setBackground(aColor);
					mSessionTextPane.setBackground(aColor);
				}
				return;
			}
		};
	}
	
	private ActionListener createTitleColorMenuItemActionListener()
	{
		return new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				Color aColor = JColorChooser.showDialog(mFrame, "Choose Note Background Color", new Color(mSessionData.getTitleColorRed(), mSessionData.getTitleColorGreen(), mSessionData.getTitleColorBlue()));
				if(aColor != null){
					mSessionData.setTitleColorRed(aColor.getRed());
					mSessionData.setTitleColorGreen(aColor.getGreen());
					mSessionData.setTitleColorBlue(aColor.getBlue());
					mTitleBar.setBackground(aColor);
				}
				return;
			}
		};
	}
	
	private ActionListener createBorderColorMenuItemActionListener()
	{
		return new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				Color aColor = JColorChooser.showDialog(mFrame, "Choose Border Color", new Color(mSessionData.getBorderColorRed(), mSessionData.getBorderColorGreen(), mSessionData.getBorderColorBlue()));
				if(aColor != null){
					mSessionData.setBorderColorRed(aColor.getRed());
					mSessionData.setBorderColorGreen(aColor.getGreen());
					mSessionData.setBorderColorBlue(aColor.getBlue());
					Border inner = BorderFactory.createEmptyBorder(0, 2, 1, 2);
					Border outer = BorderFactory.createLineBorder(aColor, mSessionData.getBorderWidth());
					mBasePanel.setBorder(BorderFactory.createCompoundBorder(outer, inner));
				}
				return;
			}
		};
	}
	
	private JMenu createEditOtherMenu(String caption)
	{
		JMenu aMenu = new JMenu("Edit other");
		
		JMenuItem item = new JMenuItem("Title...");
		item.addActionListener(this.createCustomTitleMenuItemActionListener());
		aMenu.add(item);
		
		JMenu bMenu = this.createTitleFontMenu("Title font");
		aMenu.add(bMenu);
		
		item = new JMenuItem("Border width...");
		item.addActionListener(this.createBorderWidthMenuItemActionListener());
		aMenu.add(item);
		return aMenu;
	}
	
	private ActionListener createCustomTitleMenuItemActionListener()
	{
		return new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				try{
					String response = (String)JOptionPane.showInputDialog(mFrame, "Add a title for this note:", "Add Title", JOptionPane.PLAIN_MESSAGE, null, null, null);
					// Cancel button returns null.
					// Okay button returns String.
					if(response != null){
						if(response.isEmpty()){
							throw new Exception("Title cannot be empty.");
						}else{
							mSessionData.setCustomTitle(response);
							mTitleBarLabel.setText(response);
						}
					}
				}catch(Exception ex){
					JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				}
				return;
			}
		};
	}
	
	private JMenu createTitleFontMenu(String caption)
	{
		JMenu main = new JMenu(caption);
		
		JMenu submenu = new JMenu("Font");
		for(final String name : ConfigData.getWebSafeFontNames())
		{
			JCheckBoxMenuItem item = new JCheckBoxMenuItem(name);
			item.setFont(new Font(name, Font.PLAIN, 14));
			item.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					mSessionData.setTitleFontFace(name);
					mTitleBarLabel.setFont(FontTool.getFont(mSessionData.getTitleFontFace(), mSessionData.getTitleFontSize(), mSessionData.getTitleFontBold(), mSessionData.getTitleFontItalic()));
					return;
				}
			});
			submenu.add(item);
		}
		main.add(submenu);
		
		submenu = new JMenu("Size");
		for(final int size : ConfigData.getFontSizes())
		{
			JCheckBoxMenuItem item = new JCheckBoxMenuItem(String.valueOf(size));
			item.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, size));
			item.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					mSessionData.setTitleFontSize(size);
					mTitleBarLabel.setFont(FontTool.getFont(mSessionData.getTitleFontFace(), mSessionData.getTitleFontSize(), mSessionData.getTitleFontBold(), mSessionData.getTitleFontItalic()));
					return;
				}
			});
			submenu.add(item);
		}
		main.add(submenu);
		
		JCheckBoxMenuItem item = new JCheckBoxMenuItem("Bold");
		item.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				mSessionData.setTitleFontBold(!mSessionData.getTitleFontBold());
				mTitleBarLabel.setFont(FontTool.getFont(mSessionData.getTitleFontFace(), mSessionData.getTitleFontSize(), mSessionData.getTitleFontBold(), mSessionData.getTitleFontItalic()));
				return;
			}
		});
		main.add(item);
		
		item = new JCheckBoxMenuItem("Italic");
		item.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				mSessionData.setTitleFontItalic(!mSessionData.getTitleFontItalic());
				mTitleBarLabel.setFont(FontTool.getFont(mSessionData.getTitleFontFace(), mSessionData.getTitleFontSize(), mSessionData.getTitleFontBold(), mSessionData.getTitleFontItalic()));
				return;
			}
		});
		main.add(item);
		
		JMenuItem item2 = new JMenuItem("Color...");
		item2.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				Color aColor = JColorChooser.showDialog(mFrame, "Choose Note Foreground Color", new Color(mSessionData.getTitleFontColorRed(), mSessionData.getTitleFontColorGreen(), mSessionData.getTitleFontColorBlue()));
				if(aColor != null){
					mSessionData.setTitleFontColorRed(aColor.getRed());
					mSessionData.setTitleFontColorGreen(aColor.getGreen());
					mSessionData.setTitleFontColorBlue(aColor.getBlue());
					mTitleBarLabel.setForeground(aColor);
				}
				return;
			}
		});
		main.add(item2);
		
		return main;
	}
	
	private ActionListener createBorderWidthMenuItemActionListener()
	{
		return new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				String response = null;
				try{
					response = (String)JOptionPane.showInputDialog(mFrame, "Choose a number between 4 and 100:", "Border Width", JOptionPane.PLAIN_MESSAGE, null, null, 8);
					int width = Integer.parseInt(response);
					if(width >= 4 && width <= 100){
						mSessionData.setBorderWidth(width);
						reinitializeBorder();
					}
				}catch(Exception ex){
					JOptionPane.showMessageDialog(null, "Unable to read user entry: " + response, "Error", JOptionPane.ERROR_MESSAGE);
				}
				return;
			}
		};
	}
	
	private JMenuItem createCutCopyPasteMenuItem(Action editAction, String title, String iconPath)
	{
		JMenuItem item = new JMenuItem(editAction);
		item.setText(title);
		try{
			ImageIcon anImage = new ImageIcon(ImageLoader.getImageFromResourcePath(this.getClass().getResource(iconPath)));
			item.setIcon(anImage);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return item;
	}
	
	private JMenuItem createEmoticonMenuItem(String caption)
	{
		JMenuItem item = new JMenuItem(caption);
		try{
			ImageIcon anImage = new ImageIcon(ImageLoader.getImageFromResourcePath(this.getClass().getResource(Globals.EMOTICON_ICON_PATH)));
			item.setIcon(anImage);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return item;
	}
	
	private ActionListener createEmoticonMenuItemActionListener()
	{
		return new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				try{
					EmoticonDialog eDialog = new EmoticonDialog(mFrame, ConfigData.getEmoticons());
					EmoticonDialog.Response response = eDialog.showDialog();
					if(response.compareTo(EmoticonDialog.Response.Okay) == 0){
						insertEmoticon(eDialog.getSelectedItem());
					}
					
				}catch(Exception ex){
					ex.printStackTrace();
				}
				return;
			}
		};
	}
	
	private JMenuItem createHideMenuItem(String caption)
	{
		JMenuItem item = new JMenuItem(caption);
		item.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				mFrame.toBack();
				return;
			}
		});
		return item;
	}
	
	private void setSplitPaneDividerBackgroundColor(Color aColor)
	{
		// http://stackoverflow.com/questions/2448502/how-to-set-background-color-to-a-divider-in-jsplitpane
		// Since this is an undocumented feature, a try/catch would be wise.
		try{
			if(aColor != null){
				((BasicSplitPaneDivider)this.mSplitPane.getComponent(2)).setBackground(aColor);
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	private void fillDeleteMenu(JComponent aMenu)
	{
		JMenuItem item = new JMenuItem("Delete here.");
		item.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				beginDeletionRequest(XMLTransact.DeletionType.ONLY_HERE);
				return;
			}
		});
		aMenu.add(item);
		
		item = new JMenuItem("Delete everywhere.");
		item.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				beginDeletionRequest(XMLTransact.DeletionType.EVERYWHERE);
				return;
			}
		});
		aMenu.add(item);
		
		item = new JMenuItem("Delete everywhere but here.");
		item.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				beginDeletionRequest(XMLTransact.DeletionType.EVERYWHERE_BUT_HERE);
				return;
			}
		});
		aMenu.add(item);
		
		return;
	}
	
	/**
	 * This method is used if ConfigData#getUseTimeForTitle() indicates using the current time as 
	 * the SessionGUI title
	 * @return string representing the title.
	 */
	private String setDateStringForTitle()
	{
		String title = "";
		Date aDate = new Date(System.currentTimeMillis());
		try{
			title = DateFormatTool.getSimple12HourTime(aDate);
		}catch(NullPointerException npe){
			npe.printStackTrace();
		}catch(IllegalArgumentException iae){
			iae.printStackTrace();
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return title;
	}
	
	/**
	 * In order to use this method, the following conditions must be met:
	 * (1) SessionGUI is visible.
	 * (2) the mSessionData property must be set via the SessionGUI.setSessionData method.
	 */
	public void refreshGUIProperties()
	{
		this.mBasePanel.setBackground(new Color(this.mSessionData.getNoteColorRed(), this.mSessionData.getNoteColorGreen(), this.mSessionData.getNoteColorBlue()));
		this.mTitleBarLabel.setFont(FontTool.getFont(this.mSessionData.getTitleFontFace(),
									this.mSessionData.getTitleFontSize(),
									this.mSessionData.getTitleFontBold(),
									this.mSessionData.getTitleFontItalic()));
		this.mTitleBarLabel.setForeground(new Color(this.mSessionData.getTitleFontColorRed(), this.mSessionData.getTitleFontColorGreen(), this.mSessionData.getTitleFontColorBlue()));
		this.mSplitPane.setBackground(new Color(this.mSessionData.getNoteColorRed(), this.mSessionData.getNoteColorGreen(), this.mSessionData.getNoteColorBlue()));
		// http://stackoverflow.com/questions/2448502/how-to-set-background-color-to-a-divider-in-jsplitpane
		((BasicSplitPaneDivider)this.mSplitPane.getComponent(2)).setBackground(new Color(this.mSessionData.getNoteColorRed(), this.mSessionData.getNoteColorGreen(), this.mSessionData.getNoteColorBlue()));
		this.mTextEntryPane.setBackground(new Color(this.mSessionData.getNoteColorRed(), this.mSessionData.getNoteColorGreen(), this.mSessionData.getNoteColorBlue()));
		this.mSessionTextPane.setBackground(new Color(this.mSessionData.getNoteColorRed(), this.mSessionData.getNoteColorGreen(), this.mSessionData.getNoteColorBlue()));
		this.reinitializeBorder();
		this.refreshWindowSizePosition();
		return;
	}
	
	private void refreshWindowSizePosition()
	{
		int x = -1;
		int y = -1;
		int width = this.mSessionData.getNoteWidth();
		if(this.mSessionData.getNoteWidth() > Toolkit.getDefaultToolkit().getScreenSize().width){
			width = Toolkit.getDefaultToolkit().getScreenSize().width;
			x = 0;
		}
		int height = this.mSessionData.getNoteHeight();
		if(this.mSessionData.getNoteHeight() > Toolkit.getDefaultToolkit().getScreenSize().height){
			height = Toolkit.getDefaultToolkit().getScreenSize().height;
			y = 0;
		}
		if(x > -1 || y > -1){
			this.mFrame.setLocation(0, 0);
		}
		this.mFrame.setSize(new Dimension(width, height));
		return;
	}
	
	private void refreshMessageContent()
	{
		// There are two cases where message content is displayed in mTextEntryPane instead of mSessionTextPane:
		// (1) The user is editing an existing template
		// (2) The user is intending to send a normal session based on a template.
		
		if(this.mSessionData.getSessionMode().compareTo(SessionMode.EDIT_TEMPLATE) == 0){
			this.refreshTextEntryPaneForTemplateEditing();
		}else if(this.mSessionData.getTemplateName() != null && 
				this.mSessionData.getSessionMode().compareTo(SessionMode.USE_TEMPLATE) == 0 &&
				this.mSessionData.getSessionPriority().compareTo(SessionPriority.URGENT) != 0){
			this.refreshTextEntryPaneForTemplateEditing();
		}else{
			this.refreshSessionTextPane();
		}
		return;
	}
	
	public void refreshTextEntryPaneForTemplateEditing()
	{
		try{
			this.mTextEntryPane.setText("");
			this.mTextEntryPane.getEditorKit().createDefaultDocument();
			StyledDocument doc = this.mTextEntryPane.getStyledDocument();
			if(this.mSessionData.getEntries().size() > 0){
				try{
					// A template is expected to have only a single entry at index zero.
					byte[] parBytes = Base64.decodeBase64(this.mSessionData.getEntries().get(0).text.getBytes("UTF8"));
					String par = ConvertXMLToRTF.bytes2String(parBytes);
					int start = this.mTextEntryPane.getStyledDocument().getDefaultRootElement().getStartOffset();
					ConvertXMLToRTF.writeDoc(doc, par, start);
				}catch(Exception ex){
					ex.printStackTrace();
				}
			}
			this.mTextEntryPane.setCaretPosition(0); // also scrolls viewport to the very top.
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return;
	}
	
	/**
	 * The loop in this method prints out the session data entries in order so the most recent will
	 * appear at the top.
	 */
	public void refreshSessionTextPane()
	{
		int count = this.mSessionData.getEntries().size();
		if(count > 0){
			try{
				this.mSessionTextPane.setText("");
				this.mSessionTextPane.getEditorKit().createDefaultDocument();
				StyledDocument doc = this.mSessionTextPane.getStyledDocument();
				
				StringBuilder pars = new StringBuilder();
				StringBuilder newPar = new StringBuilder();
				String par = null;
				String username = null;
				for(int i = count - 1; i >= 0; i--)
				{
					byte[] parBytes = Base64.decodeBase64(this.mSessionData.getEntries().get(i).text.getBytes("UTF8"));
					par = ConvertXMLToRTF.bytes2String(parBytes);
					username = this.createUserEntryPrefix(this.mSessionData.getEntries().get(i).source);
					int start = this.getBeginningOfParagraph(par, "par");
					newPar.setLength(0);
					newPar.append(par.substring(0, start));
					newPar.append(username);
					newPar.append(par.substring(start, par.length()));
					
					pars.append(newPar);
				}
				
				//System.out.println(pars.toString());
				
				int start = this.mSessionTextPane.getStyledDocument().getDefaultRootElement().getStartOffset();
				ConvertXMLToRTF.writeDoc(doc, pars.toString(), start);
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
		return;
	}
	
	private void hideAllPopupMenus()
	{
		// Popup menus weren't disappearing as expected.  So this method was added.
		if(mEntryPopup != null){
			mEntryPopup.setVisible(false);
		}
		if(mSessionPopup != null){
			mSessionPopup.setVisible(false);
		}
		if(mSendSaveButton.getComponentPopupMenu() != null){
			mSendSaveButton.getComponentPopupMenu().setVisible(false);
		}
		if(mDropButton.getComponentPopupMenu() != null){
			mDropButton.getComponentPopupMenu().setVisible(false);
		}
		return;
	}
	
	private String createUserEntryPrefix(String username)
	{
		StringBuilder prefix = new StringBuilder();
		
		prefix.append("<text font-size=\"12\" font-family=\"Tahoma\" bold=\"true\" italic=\"false\" underline=\"false\">");
		prefix.append("\n" + username + ":   ");
		prefix.append("</text>");
		
		return prefix.toString();
	}
	
	private int getBeginningOfParagraph(String xml, String tagName)
	{
		int index = -1;
		int start = xml.indexOf("<" + tagName, 0);
		int end = xml.indexOf("</" + tagName, start);
		if(end > start){
			String temp = xml.substring(start, end);
			start = temp.indexOf(">") + 1;
			if(start >= 0){
				index = start;
			}
		}
		return index;
	}
	
	private void insertEmoticon(String name)
	{
		try{
			ImageIcon anIcon = new ImageIcon(ImageLoader.getImageFromResourcePath(this.getClass().getResource(name)));
			Emoticon em = new Emoticon(anIcon.getImage(), name);
			this.mTextEntryPane.insertIcon(em);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return;
	}
	
	private void saveAsTemplate()
	{
		// Need to have a priority settings.
		// Need recipients.
		// Need to have a file name.
		boolean isReady = true;
		SessionPriority priority = TemplateFactory.getPrioritySettingForTemplate(this.mFrame);
		File templateFile = null;
		String recipientListName = null;
		
		if(priority == null){
			isReady = false;
		}
		
		if(isReady){
			if(priority.compareTo(SessionPriority.URGENT) == 0){
				recipientListName = TemplateFactory.getRecipientsForTemplate(this.mFrame, ConfigData.getRecipientLists());
			}
		}
		
		if(isReady){
			if(this.mSessionData.getTemplateEditMode().compareTo(TemplateEditMode.NEW_FILE) == 0){
				templateFile = TemplateFactory.getFilenameForTemplate(this.mFrame, ConfigData.getTemplateNames());
				if(templateFile == null){
					isReady = false;
				}
			}else{
				templateFile = new File("./" + this.mSessionData.getTemplateName());
			}
		}
		
		if(isReady){
			try{
				ArrayList<String> recipients = ConfigData.getRecipients("Everyone");
				if(recipientListName != null){
					recipients = ConfigData.getRecipients(recipientListName);
				}
				
				// This part is mostly just to make the titles look right on the tray menu.
				String templateName = templateFile.getName();
				if(templateName.endsWith(".template")){
					templateName = templateName.substring(0, templateName.lastIndexOf(".template"));
				}
				
				TemplateData tData = TemplateFactory.createNewTemplateDataObject(templateName, 
																		recipients, 
																		priority, 
																		this.mSessionData, 
																		this.encodeTextEntryPaneContents());

				TemplateFactory.writeTemplateToFile(tData, templateFile);
				
				TemplateSettingsListener.notifySave(this);
				
				completeDeletionRequest(XMLTransact.DeletionType.ONLY_HERE);
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
		
		return;
	}
	
	private void beginDeletionRequest(XMLTransact.DeletionType type)
	{
		if(ConfigData.getConfirmBeforeNoteDeletion()){
			int response = JOptionPane.showOptionDialog(this.mFrame, 
														"Are you sure you want to delete this note?", 
														"Confirm Deletion", 
														JOptionPane.YES_NO_OPTION, 
														JOptionPane.QUESTION_MESSAGE,
														null,
														null,
														null);
			if(response == JOptionPane.YES_OPTION){
				this.completeDeletionRequest(type);
			}
		}else{
			this.completeDeletionRequest(type);
		}
		return;
	}
	
	private void completeDeletionRequest(XMLTransact.DeletionType type)
	{
		try{
			String content = XMLTransact.parseDeleteRequestToXML(this.mSessionData.getID(), type);
			new CommServer(content, ConfigData.getMulticastIP(), ConfigData.getHostIPAddress(), ConfigData.getSessionPort(), Globals.BUFFER_SIZE);
		}catch(NullPointerException npe){
			npe.printStackTrace();
		}catch(IOException ioe){
			ioe.printStackTrace();
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	/**
	 * Intended for use when priority is set to URGENT.
	 * The message must already be in the session data property, because the text entry box is not 
	 * collected from before sending.
	 * @see #broadcastSessionData(String)
	 */
 	private void broadcastUrgentSessionData()
	{
		try{
			// These two are set to NORMAL at this point in order to ensure proper behavior upon receiving.
			this.mSessionData.setSessionMode(SessionMode.NORMAL);
			this.mSessionData.setSessionPriority(SessionPriority.NORMAL);
			
			String content = XMLTransact.parseSessionDataToXML(this.mSessionData, System.currentTimeMillis());
			new CommServer(content, ConfigData.getMulticastIP(), ConfigData.getHostIPAddress(), ConfigData.getSessionPort(), Globals.BUFFER_SIZE);
		}catch(NullPointerException npe){
			npe.printStackTrace();
		}catch(IOException ioe){
			ioe.printStackTrace();
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return;
	}
	
	/**
	 * If priority is not URGENT, this method would be used to send the message after the user enters it into
	 * the text entry box.
	 * @param recipient a string representing the recipient or group to receive this message
	 * @see #broadcastSessionData()
	 */
	private void broadcastSessionData(String recipient)
	{
		if(this.mTextEntryPane.getText() != null){
			try{
				String pars = this.encodeTextEntryPaneContents();
				if(this.mSessionData.getSessionMode().compareTo(SessionMode.USE_TEMPLATE) == 0){
					this.mSessionData.setEntries(new ArrayList<Entry>());
				}
				this.mSessionData.getEntries().add(new Entry(ConfigData.getHostName(), pars));
				this.mSessionData.setRecipients(ConfigData.getRecipients(recipient));
				// These two are set to NORMAL at this point in order to ensure proper behavior upon receiving.
				this.mSessionData.setSessionMode(SessionMode.NORMAL);
				this.mSessionData.setSessionPriority(SessionPriority.NORMAL);
				String content = XMLTransact.parseSessionDataToXML(this.mSessionData, System.currentTimeMillis());
				new CommServer(content, ConfigData.getMulticastIP(), ConfigData.getHostIPAddress(), ConfigData.getSessionPort(), Globals.BUFFER_SIZE);
				
				this.mResetEditorAttributes = true; // otherwise the last editor kit action remains in the input attributes. 
				this.mTextEntryPane.setText("");
			}catch(NullPointerException npe){
				npe.printStackTrace();
			}catch(IOException ioe){
				ioe.printStackTrace();
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
		
		return;
	}
	
	private String encodeTextEntryPaneContents() throws Exception 
	{
		String encoded = null;
		try{
			if(this.mTextEntryPane.getText() == null){
				throw new NullPointerException();
			}
			Document doc = this.mTextEntryPane.getStyledDocument();
			int start = this.mTextEntryPane.getStyledDocument().getDefaultRootElement().getStartOffset();
			int len = this.mTextEntryPane.getStyledDocument().getDefaultRootElement().getEndOffset() - start;
			java.io.StringWriter writer = new java.io.StringWriter();
			ConvertRTFToXML.writeParsOnly(doc, start, len, writer);
			
			//System.out.println(writer.getBuffer().toString());
			
			encoded = Base64.encodeBase64String(writer.getBuffer().toString().getBytes("UTF8"));
		}catch(BadLocationException ble){
			throw ble;
		}catch(IOException ioe){
			throw ioe;
		}catch(NullPointerException npe){
			throw npe;
		}catch(Exception ex){
			throw ex;
		}
		return encoded;
	}
	
	public void keyPressed(KeyEvent e)
	{
		// The purpose for this is to alert the user that their keystrokes are not going where expected.
		// This program has the unfortunate tendency to popup windows while users are typing into other 
		// programs, and not everyone is a touch-key typer who watches the screen while they type.
		if(e.getSource() != (JTextPane)this.mTextEntryPane){
			Toolkit.getDefaultToolkit().beep();
		}
		return;
	}
	
	public void keyReleased(KeyEvent e) { return; }
	public void keyTyped(KeyEvent e) { return; }
	
	public void componentHidden(ComponentEvent e)
	{
		// Since this frame is undecorated, this event shouldn't happen.
		// The following is for debugging purposes.
		if(this.mSessionData.getHiddenTime() <= 0){
			this.mSessionData.setHiddenTime(System.currentTimeMillis());
			this.mSessionData.setHiddenEvent("componentHidden");
		}
		return;
	}
	
	public void componentShown(ComponentEvent e)	{ return; }
	public void componentMoved(ComponentEvent e)	{ return; }
	
	public void componentResized(ComponentEvent e)
	{
		// Popup menus weren't disappearing as expected.  So this function was added.
		hideAllPopupMenus();
		
		if(this.mFrame.isVisible()){
			this.mSessionData.setNoteWidth(this.mFrame.getWidth());
			this.mSessionData.setNoteHeight(this.mFrame.getHeight());
		}
		return;
	}
	
	public void windowActivated(WindowEvent e)		{ return; }
	public void windowClosed(WindowEvent e)
	{
		if(this.mSessionData.getHiddenTime() <= 0){
			this.mSessionData.setHiddenTime(System.currentTimeMillis());
			this.mSessionData.setHiddenEvent("windowClosed");
		}
		return;
	}
	
	public void windowClosing(WindowEvent e)
	{
		// Since this frame is undecorated, this event shouldn't happen.
		// The following is for debugging purposes.
		if(this.mSessionData.getHiddenTime() <= 0){
			this.mSessionData.setHiddenTime(System.currentTimeMillis());
			this.mSessionData.setHiddenEvent("windowClosing");
		}
		return;
	}
	
	public void windowDeactivated(WindowEvent e)	{ return; }
	public void windowDeiconified(WindowEvent e)	{ return; }
	public void windowIconified(WindowEvent e)
	{
		// Since this frame is undecorated, this event shouldn't happen.
		// The following is for debugging purposes.
		if(this.mSessionData.getHiddenTime() <= 0){
			this.mSessionData.setHiddenTime(System.currentTimeMillis());
			this.mSessionData.setHiddenEvent("windowIconified");
		}
		return;
	}
	
	public void windowOpened(WindowEvent e)
	{
		// The use of this event facilitates the automatic broadcast of session templates.
		if(this.mFrame.isVisible() && this.mSessionData.getSessionPriority().compareTo(SessionPriority.URGENT) == 0){
			this.broadcastUrgentSessionData();
		}
		if(this.mFrame.isVisible()){
			// If this is a new, blank session would the focus go straight to the text entry box.
			// If this is a new session based on a template, the focus would go to the text entry box.
			// Otherwise, the focus would go to some non-text component.
			if(this.mSessionData.getEntries().isEmpty()){
				this.mTextEntryPane.requestFocus();
			}else if(this.mSessionData.getTemplateName() != null && 
					this.mSessionData.getSessionMode().compareTo(SessionMode.USE_TEMPLATE) == 0 &&
					this.mSessionData.getSessionPriority().compareTo(SessionPriority.URGENT) != 0){
				this.mTextEntryPane.requestFocus();
			}else{
				this.mSendSaveButton.requestFocus();
			}
		}
		return;
	}
	
	public void disposeWindow()
	{
		this.mFrame.dispose();
		return;
	}
}
