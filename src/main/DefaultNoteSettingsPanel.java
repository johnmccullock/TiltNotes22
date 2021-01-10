package main;

import config.ConfigData;
import dialogs.FontData;
import dialogs.FontDialog;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.SystemColor;
import java.awt.Toolkit;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ListSelectionModel;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class DefaultNoteSettingsPanel extends JPanel implements DocumentListener
{
	private static final String PREVIEW_SAMPLE_TEXT = "AaBbCcXxYyZz";
	
	private JFrame mParentFrame = null;
	
	private JRadioButton mUseNoteBorderYes = null;
	private JRadioButton mUseNoteBorderNo = null;
	
	private JRadioButton mTimeIsDefaultTitle = null;
	private JRadioButton mCustomDefaultTitle = null;
	private JTextField mCustomTitleField = null;
	
	private JRadioButton mTitleAlignLeft = null;
	private JRadioButton mTitleAlignCenter = null;
	private JRadioButton mTitleAlignRight = null;
	
	private JButton mNoteColorButton = null;
	private JLabel mNoteColorPreview = null;
	private JButton mTitleColorButton = null;
	private JLabel mTitleColorPreview = null;
	private JButton mBorderColorButton = null;
	private JLabel mBorderColorPreview = null;
	
	private JButton mNoteFontButton = null;
	private JTextPane mNoteFontPreview = null;
	private JButton mTitleFontButton = null;
	private JLabel mTitleFontPreview = null;
	
	private JTextField mNoteWidthBox = null;
	private JTextField mNoteHeightBox = null;
	
	private JTextField mBorderWidthBox = null;
	
	private JButton mRestoreOriginalSettingsButton = null;
	private static final String RESTORE_SETTINGS_BUTTON = "Restore these settings to their originally installed values.";
	
	private int mNoteColorRed = 0;
	private int mNoteColorGreen = 0;
	private int mNoteColorBlue = 0;
	private int mTitleColorRed = 0;
	private int mTitleColorGreen = 0;
	private int mTitleColorBlue = 0;
	private int mBorderColorRed = 0;
	private int mBorderColorGreen = 0;
	private int mBorderColorBlue = 0;
	private String mNoteFontName = null;
	private int mNoteFontSize = 0;
	private boolean mNoteFontBold = false;
	private boolean mNoteFontItalic = false;
	private boolean mNoteFontUnderline = false;
	private boolean mNoteFontStrikeout = false;
	private int mNoteFontColorRed = 0;
	private int mNoteFontColorGreen = 0;
	private int mNoteFontColorBlue = 0;
	private String mTitleFontName = null;
	private int mTitleFontSize = 0;
	private boolean mTitleFontBold = false;
	private boolean mTitleFontItalic = false;
	private int mTitleFontColorRed = 0;
	private int mTitleFontColorGreen = 0;
	private int mTitleFontColorBlue = 0;
	
	public DefaultNoteSettingsPanel(JFrame parentFrame)
	{
		super();
		this.mParentFrame = parentFrame;
		this.initializeMain();
		this.retrieveInitialValues();
		return;
	}
	
	private void initializeMain()
	{
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		
		this.add(this.initializeRow1Panel());
		this.add(this.createColorChooserRow("Default border color :", 
											this.createBorderColorButton(), 
											this.mBorderColorPreview = new JLabel()));
		this.add(this.initializeRow3Panel());
		
		JSeparator sep = new JSeparator(JSeparator.HORIZONTAL);
		sep.setMinimumSize(new Dimension(400, 4));
		sep.setMaximumSize(new Dimension(2000, 4));
		this.add(sep);
		
		this.add(this.initializeRow4Panel());
		this.add(this.initializeRow5Panel());
		this.add(this.createColorChooserRow("Default title color :", 
											this.createTitleColorButton(), 
											this.mTitleColorPreview = new JLabel()));
		this.add(this.initializeRow7Panel());
		
		sep = new JSeparator(JSeparator.HORIZONTAL);
		sep.setMinimumSize(new Dimension(400, 4));
		sep.setMaximumSize(new Dimension(2000, 4));
		this.add(sep);
		
		this.add(this.createColorChooserRow("Default note color :", 
											this.createNoteColorButton(), 
											this.mNoteColorPreview = new JLabel()));
		
		this.add(this.initializeRow8Panel());
		this.add(this.initializeRow9Panel());
		
		this.add(Box.createHorizontalStrut(16));
		
		this.add(this.initializeRow10Panel());
		
		return;
	}
	
	private void retrieveInitialValues()
	{
		if(ConfigData.getUseNoteBorder()){
			this.mUseNoteBorderYes.setSelected(true);
		}else{
			this.mUseNoteBorderNo.setSelected(true);
		}
		
		this.mBorderColorRed = ConfigData.getDefaultBorderColorRed();
		this.mBorderColorGreen = ConfigData.getDefaultBorderColorGreen();
		this.mBorderColorBlue = ConfigData.getDefaultBorderColorBlue();
		this.mBorderColorPreview.setBackground(new Color(this.mBorderColorRed, this.mBorderColorGreen, this.mBorderColorBlue));
		this.mBorderWidthBox.setText(String.valueOf(ConfigData.getDefaultBorderWidth()));
		
		if(ConfigData.getUseTimeForTitle()){
			this.mTimeIsDefaultTitle.setSelected(true);
			this.mCustomTitleField.setText("");
		}else{
			this.mCustomDefaultTitle.setSelected(true);
			this.mCustomTitleField.setText(ConfigData.getDefaultTitle());
		}
		
		if(ConfigData.getDefaultTitleAlignment() == ConfigData.Align.LEFT){
			this.mTitleAlignLeft.setSelected(true);
		}else if(ConfigData.getDefaultTitleAlignment() == ConfigData.Align.RIGHT){
			this.mTitleAlignRight.setSelected(true);
		}else{
			this.mTitleAlignCenter.setSelected(true);
		}
		
		this.mNoteColorRed = ConfigData.getDefaultNoteColorRed();
		this.mNoteColorGreen = ConfigData.getDefaultNoteColorGreen();
		this.mNoteColorBlue = ConfigData.getDefaultNoteColorBlue();
		
		this.mNoteColorPreview.setBackground(new Color(this.mNoteColorRed, this.mNoteColorGreen, this.mNoteColorBlue));
		
		this.mTitleColorRed = ConfigData.getDefaultTitleColorRed();
		this.mTitleColorGreen = ConfigData.getDefaultTitleColorGreen();
		this.mTitleColorBlue = ConfigData.getDefaultTitleColorBlue();
		
		this.mTitleColorPreview.setBackground(new Color(this.mTitleColorRed, this.mTitleColorGreen, this.mTitleColorBlue));
		
		if(ConfigData.getDefaultNoteFontName() != null){
			this.mNoteFontName = ConfigData.getDefaultNoteFontName();
			this.mNoteFontSize = ConfigData.getDefaultNoteFontSize();
			this.mNoteFontBold = ConfigData.getDefaultNoteFontBold();
			this.mNoteFontItalic = ConfigData.getDefaultNoteFontItalic();
			this.mNoteFontUnderline = ConfigData.getDefaultNoteFontUnderline();
			this.mNoteFontStrikeout = ConfigData.getDefaultNoteFontStrikeout();
		}else{
			this.mNoteFontName = Globals.UNCHOSEN_NOTE_FONT_FACE;
			this.mNoteFontSize = Globals.UNCHOSEN_NOTE_FONT_SIZE;
			this.mNoteFontBold = Globals.UNCHOSEN_NOTE_FONT_BOLD;
			this.mNoteFontItalic = Globals.UNCHOSEN_NOTE_FONT_ITALIC;
			this.mNoteFontUnderline = Globals.UNCHOSEN_NOTE_FONT_UNDERLINE;
			this.mNoteFontStrikeout = Globals.UNCHOSEN_NOTE_FONT_STRIKEOUT;
		}
		try{
			this.mNoteFontButton.setText(this.mNoteFontName);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		if(ConfigData.getDefaultTitleFontName() != null){
			this.mTitleFontName = ConfigData.getDefaultTitleFontName();
			this.mTitleFontSize = ConfigData.getDefaultTitleFontSize();
			this.mTitleFontBold = ConfigData.getDefaultTitleFontBold();
			this.mTitleFontItalic = ConfigData.getDefaultTitleFontItalic();
		}else{
			this.mTitleFontName = Globals.UNCHOSEN_TITLE_FONT_FACE;
			this.mTitleFontSize = Globals.UNCHOSEN_TITLE_FONT_SIZE;
			this.mTitleFontBold = Globals.UNCHOSEN_TITLE_FONT_BOLD;
			this.mTitleFontItalic = Globals.UNCHOSEN_TITLE_FONT_ITALIC;
		}
		try{
			this.mTitleFontButton.setText(this.mTitleFontName);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		this.mNoteFontColorRed = ConfigData.getDefaultNoteFontColorRed();
		this.mNoteFontColorGreen = ConfigData.getDefaultNoteFontColorGreen();
		this.mNoteFontColorBlue = ConfigData.getDefaultNoteFontColorBlue();
		
		this.mNoteFontButton.setForeground(new Color(this.mNoteFontColorRed, this.mNoteFontColorGreen, this.mNoteFontColorBlue));
		
		this.mTitleFontColorRed = ConfigData.getDefaultTitleFontColorRed();
		this.mTitleFontColorGreen = ConfigData.getDefaultTitleFontColorGreen();
		this.mTitleFontColorBlue = ConfigData.getDefaultTitleFontColorBlue();
		
		this.mTitleFontButton.setForeground(new Color(this.mTitleFontColorRed, this.mTitleFontColorGreen, this.mTitleFontColorBlue));
		
		this.mNoteWidthBox.setText(String.valueOf(ConfigData.getDefaultNoteWidth()));
		this.mNoteHeightBox.setText(String.valueOf(ConfigData.getDefaultNoteHeight()));
		
		this.updateNoteFontPreviewPanel();
		this.updateTitleFontPreviewPanel();
		return;
	}
	
	private JPanel initializeRow1Panel()
	{
		JPanel aPanel = new JPanel();
		aPanel.setLayout(new BoxLayout(aPanel, BoxLayout.X_AXIS));
		aPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
		
		JPanel bPanel = new JPanel();
		bPanel.setLayout(new BoxLayout(bPanel, BoxLayout.X_AXIS));
		bPanel.setPreferredSize(new Dimension(150, 25));
		bPanel.setMaximumSize(new Dimension(150, 25));
		JLabel aLabel = new JLabel("Default note border :");
		bPanel.add(aLabel);
		bPanel.add(Box.createVerticalStrut(16));
		aPanel.add(bPanel);
		
		ButtonGroup group = new ButtonGroup();
		
		bPanel = new JPanel();
		bPanel.setLayout(new BoxLayout(bPanel, BoxLayout.X_AXIS));
		bPanel.setPreferredSize(new Dimension(80, 25));
		bPanel.setMaximumSize(new Dimension(80, 25));
		this.mUseNoteBorderYes = new JRadioButton("Yes");
		group.add(this.mUseNoteBorderYes);
		bPanel.add(this.mUseNoteBorderYes);
		bPanel.add(Box.createVerticalStrut(16));
		aPanel.add(bPanel);
		
		bPanel = new JPanel();
		bPanel.setLayout(new BoxLayout(bPanel, BoxLayout.X_AXIS));
		bPanel.setPreferredSize(new Dimension(80, 25));
		bPanel.setMaximumSize(new Dimension(80, 25));
		this.mUseNoteBorderNo = new JRadioButton("No");
		group.add(this.mUseNoteBorderNo);
		bPanel.add(this.mUseNoteBorderNo);
		bPanel.add(Box.createVerticalStrut(16));
		aPanel.add(bPanel);
		
		aPanel.add(Box.createVerticalStrut(16));
		
		return aPanel;
	}
	
	private JPanel initializeRow3Panel()
	{
		JPanel aPanel = new JPanel();
		aPanel.setLayout(new BoxLayout(aPanel, BoxLayout.X_AXIS));
		aPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
		
		JPanel bPanel = new JPanel();
		bPanel.setLayout(new BoxLayout(bPanel, BoxLayout.X_AXIS));
		bPanel.setPreferredSize(new Dimension(150, 25));
		bPanel.setMaximumSize(new Dimension(150, 25));
		JLabel aLabel = new JLabel("Default border width :");
		bPanel.add(aLabel);
		bPanel.add(Box.createVerticalStrut(16));
		aPanel.add(bPanel);
		
		bPanel = new JPanel();
		bPanel.setLayout(new BoxLayout(bPanel, BoxLayout.X_AXIS));
		bPanel.setPreferredSize(new Dimension(80, 25));
		bPanel.setMaximumSize(new Dimension(80, 25));
		bPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));
		this.mBorderWidthBox = new JTextField();
		this.mBorderWidthBox.setColumns(4);
		this.mBorderWidthBox.setHorizontalAlignment(JTextField.CENTER);
		bPanel.add(this.mBorderWidthBox);
		bPanel.add(Box.createVerticalStrut(16));
		aPanel.add(bPanel);
		
		aPanel.add(Box.createVerticalStrut(16));
		
		return aPanel;
	}
	
	private JPanel initializeRow4Panel()
	{
		JPanel aPanel = new JPanel();
		aPanel.setLayout(new BoxLayout(aPanel, BoxLayout.X_AXIS));
		aPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
		
		JPanel bPanel = new JPanel();
		bPanel.setLayout(new BoxLayout(bPanel, BoxLayout.X_AXIS));
		bPanel.setPreferredSize(new Dimension(150, 25));
		bPanel.setMaximumSize(new Dimension(150, 25));
		JLabel aLabel = new JLabel("Default note title :");
		bPanel.add(aLabel);
		bPanel.add(Box.createVerticalStrut(16));
		aPanel.add(bPanel);
		
		ButtonGroup group = new ButtonGroup();
		
		bPanel = new JPanel();
		bPanel.setLayout(new BoxLayout(bPanel, BoxLayout.X_AXIS));
		bPanel.setPreferredSize(new Dimension(80, 25));
		bPanel.setMaximumSize(new Dimension(80, 25));
		this.mTimeIsDefaultTitle = new JRadioButton("Time");
		group.add(this.mTimeIsDefaultTitle);
		bPanel.add(this.mTimeIsDefaultTitle);
		bPanel.add(Box.createVerticalStrut(16));
		aPanel.add(bPanel);
		
		bPanel = new JPanel();
		bPanel.setLayout(new BoxLayout(bPanel, BoxLayout.X_AXIS));
		bPanel.setPreferredSize(new Dimension(180, 25));
		bPanel.setMaximumSize(new Dimension(180, 25));
		this.mCustomDefaultTitle = new JRadioButton();
		group.add(this.mCustomDefaultTitle);
		bPanel.add(this.mCustomDefaultTitle);
		this.mCustomTitleField = new JTextField();
		this.mCustomTitleField.setPreferredSize(new Dimension(150, 25));
		this.mCustomTitleField.setMaximumSize(new Dimension(150, 25));
		this.mCustomTitleField.setMargin(new Insets(0, 4, 0, 4));
		this.mCustomTitleField.getDocument().addDocumentListener(this);
		bPanel.add(this.mCustomTitleField);
		bPanel.add(Box.createVerticalStrut(16));
		aPanel.add(bPanel);
		
		aPanel.add(Box.createVerticalStrut(16));
		
		return aPanel;
	}
	
	private JPanel initializeRow5Panel()
	{
		JPanel aPanel = new JPanel();
		aPanel.setLayout(new BoxLayout(aPanel, BoxLayout.X_AXIS));
		aPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
		
		JPanel bPanel = new JPanel();
		bPanel.setLayout(new BoxLayout(bPanel, BoxLayout.X_AXIS));
		bPanel.setPreferredSize(new Dimension(150, 25));
		bPanel.setMaximumSize(new Dimension(150, 25));
		JLabel aLabel = new JLabel("Default note title align :");
		bPanel.add(aLabel);
		bPanel.add(Box.createVerticalStrut(16));
		aPanel.add(bPanel);
		
		ButtonGroup group = new ButtonGroup();
		
		bPanel = new JPanel();
		bPanel.setLayout(new BoxLayout(bPanel, BoxLayout.X_AXIS));
		bPanel.setPreferredSize(new Dimension(80, 25));
		bPanel.setMaximumSize(new Dimension(80, 25));
		this.mTitleAlignLeft = new JRadioButton("Left");
		group.add(this.mTitleAlignLeft);
		bPanel.add(this.mTitleAlignLeft);
		bPanel.add(Box.createVerticalStrut(16));
		aPanel.add(bPanel);
		
		bPanel = new JPanel();
		bPanel.setLayout(new BoxLayout(bPanel, BoxLayout.X_AXIS));
		bPanel.setPreferredSize(new Dimension(80, 25));
		bPanel.setMaximumSize(new Dimension(80, 25));
		this.mTitleAlignCenter = new JRadioButton("Center");
		group.add(this.mTitleAlignCenter);
		bPanel.add(this.mTitleAlignCenter);
		bPanel.add(Box.createVerticalStrut(16));
		aPanel.add(bPanel);
		
		bPanel = new JPanel();
		bPanel.setLayout(new BoxLayout(bPanel, BoxLayout.X_AXIS));
		bPanel.setPreferredSize(new Dimension(80, 25));
		bPanel.setMaximumSize(new Dimension(80, 25));
		this.mTitleAlignRight = new JRadioButton("Right");
		group.add(this.mTitleAlignRight);
		bPanel.add(this.mTitleAlignRight);
		bPanel.add(Box.createVerticalStrut(16));
		aPanel.add(bPanel);
		
		aPanel.add(Box.createVerticalStrut(16));
		
		return aPanel;
	}
	
	private JPanel initializeRow7Panel()
	{
		JPanel aPanel = new JPanel();
		aPanel.setLayout(new BoxLayout(aPanel, BoxLayout.X_AXIS));
		aPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 6, 0));
		
		JPanel bPanel = new JPanel();
		bPanel.setLayout(new BoxLayout(bPanel, BoxLayout.X_AXIS));
		bPanel.setPreferredSize(new Dimension(150, 30));
		bPanel.setMaximumSize(new Dimension(150, 30));
		JLabel aLabel = new JLabel("Default title font :");
		bPanel.add(aLabel);
		bPanel.add(Box.createVerticalStrut(16));
		aPanel.add(bPanel);
		
		bPanel = new JPanel();
		bPanel.setLayout(new BoxLayout(bPanel, BoxLayout.X_AXIS));
		bPanel.setPreferredSize(new Dimension(100, 30));
		bPanel.setMaximumSize(new Dimension(100, 30));
		this.mTitleFontButton = new JButton();
		this.mTitleFontButton.setPreferredSize(new Dimension(96, 25));
		this.mTitleFontButton.setMaximumSize(new Dimension(96, 25));
		this.mTitleFontButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				try{
					// Title font doesn't have underline or strikeout attributes, so "false" was substituted.
					FontDialog fd = new FontDialog(mParentFrame, 
													ConfigData.getWebSafeFontNames(), 
													new FontData(mTitleFontName, 
																mTitleFontSize, 
																mTitleFontBold, 
																mTitleFontItalic, 
																false,
																false, 
																mTitleFontColorRed, 
																mTitleFontColorGreen, 
																mTitleFontColorBlue),
													false,
													false);
					FontData results = fd.showDialog();
					fd = null;
					if(results != null){
						mTitleFontName = results.fontFace;
						mTitleFontSize = results.fontSize;
						mTitleFontBold = results.isBold;
						mTitleFontItalic = results.isItalic;
						mTitleFontColorRed = results.redValue;
						mTitleFontColorGreen = results.greenValue;
						mTitleFontColorBlue = results.blueValue;
						mTitleFontButton.setText(mTitleFontName);
						updateTitleFontPreviewPanel();
					}
				}catch(Exception ex){
					ex.printStackTrace();
				}
				return;
			}
		});
		bPanel.add(this.mTitleFontButton);
		aPanel.add(bPanel);
		
		aPanel.add(Box.createRigidArea(new Dimension(5, 16)));
		
		bPanel = new JPanel();
		bPanel.setLayout(new BoxLayout(bPanel, BoxLayout.X_AXIS));
		bPanel.setPreferredSize(new Dimension(200, 30));
		bPanel.setMaximumSize(new Dimension(200, 30));
		bPanel.add(Box.createVerticalStrut(16));
		this.mTitleFontPreview = new JLabel(DefaultNoteSettingsPanel.PREVIEW_SAMPLE_TEXT);
		bPanel.add(this.mTitleFontPreview);
		bPanel.add(Box.createVerticalStrut(16));
		aPanel.add(bPanel);
		
		aPanel.add(Box.createVerticalStrut(16));
		
		return aPanel;
	}
	
	private JPanel createColorChooserRow(String text, JButton button, JLabel preview)
	{
		JPanel aPanel = new JPanel();
		aPanel.setLayout(new BoxLayout(aPanel, BoxLayout.X_AXIS));
		aPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
		
		JPanel bPanel = new JPanel();
		bPanel.setLayout(new BoxLayout(bPanel, BoxLayout.X_AXIS));
		bPanel.setPreferredSize(new Dimension(150, 25));
		bPanel.setMaximumSize(new Dimension(150, 25));
		bPanel.add(new JLabel(text));
		bPanel.add(Box.createVerticalStrut(16));
		aPanel.add(bPanel);
		
		bPanel = new JPanel();
		bPanel.setLayout(new BoxLayout(bPanel, BoxLayout.X_AXIS));
		bPanel.setPreferredSize(new Dimension(200, 25));
		bPanel.setMaximumSize(new Dimension(200, 25));
		button.setPreferredSize(new Dimension(80, 25));
		button.setMaximumSize(new Dimension(80, 25));
		bPanel.add(button);
		
		bPanel.add(Box.createRigidArea(new Dimension(8, 16)));
		
		preview.setBorder(BorderFactory.createEtchedBorder(1));
		preview.setPreferredSize(new Dimension(100, 25));
		preview.setMinimumSize(new Dimension(100, 25));
		preview.setMaximumSize(new Dimension(100, 25));
		preview.setOpaque(true);
		bPanel.add(preview);
		bPanel.add(Box.createVerticalStrut(16));
		aPanel.add(bPanel);
		
		aPanel.add(Box.createVerticalStrut(16));
		
		return aPanel;
	}
	
	private JPanel initializeRow8Panel()
	{
		JPanel aPanel = new JPanel();
		aPanel.setLayout(new BoxLayout(aPanel, BoxLayout.X_AXIS));
		aPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
		
		JPanel bPanel = new JPanel();
		bPanel.setLayout(new BoxLayout(bPanel, BoxLayout.X_AXIS));
		bPanel.setPreferredSize(new Dimension(150, 30));
		bPanel.setMaximumSize(new Dimension(150, 30));
		JLabel aLabel = new JLabel("Default note font :");
		bPanel.add(aLabel);
		bPanel.add(Box.createVerticalStrut(16));
		aPanel.add(bPanel);
		
		bPanel = new JPanel();
		bPanel.setLayout(new BoxLayout(bPanel, BoxLayout.X_AXIS));
		bPanel.setPreferredSize(new Dimension(100, 30));
		bPanel.setMaximumSize(new Dimension(100, 30));
		this.mNoteFontButton = new JButton();
		this.mNoteFontButton.setPreferredSize(new Dimension(96, 25));
		this.mNoteFontButton.setMaximumSize(new Dimension(96, 25));
		this.mNoteFontButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				try{
					//DialogListener.addListener(this);
					FontDialog fd = new FontDialog(mParentFrame, 
													ConfigData.getWebSafeFontNames(), 
													new FontData(mNoteFontName, 
																mNoteFontSize, 
																mNoteFontBold, 
																mNoteFontItalic, 
																mNoteFontUnderline, 
																mNoteFontStrikeout, 
																mNoteFontColorRed, 
																mNoteFontColorGreen, 
																mNoteFontColorBlue),
													true,
													true);
					FontData results = fd.showDialog();
					fd = null;
					if(results != null){
						mNoteFontName = results.fontFace;
						mNoteFontSize = results.fontSize;
						mNoteFontBold = results.isBold;
						mNoteFontItalic = results.isItalic;
						mNoteFontUnderline = results.isUnderline;
						mNoteFontStrikeout = results.isStrikeout;
						mNoteFontColorRed = results.redValue;
						mNoteFontColorGreen = results.greenValue;
						mNoteFontColorBlue = results.blueValue;
						mNoteFontButton.setText(mNoteFontName);
						mNoteFontButton.setForeground(new Color(mNoteFontColorRed, mNoteFontColorGreen, mNoteFontColorBlue));
						updateNoteFontPreviewPanel();
					}
				}catch(Exception ex){
					ex.printStackTrace();
				}
				return;
			}
		});
		bPanel.add(this.mNoteFontButton);
		aPanel.add(bPanel);
		
		aPanel.add(Box.createRigidArea(new Dimension(5, 16)));
		
		bPanel = new JPanel();
		bPanel.setLayout(new BoxLayout(bPanel, BoxLayout.X_AXIS));
		bPanel.setPreferredSize(new Dimension(200, 30));
		bPanel.setMaximumSize(new Dimension(200, 30));
		this.mNoteFontPreview = new JTextPane();
		this.mNoteFontPreview.setMaximumSize(new Dimension(196, 30));
		this.mNoteFontPreview.setPreferredSize(new Dimension(196, 30));
		this.mNoteFontPreview.setBackground(SystemColor.control);
		this.mNoteFontPreview.setEditable(false);
		bPanel.add(this.mNoteFontPreview);
		aPanel.add(bPanel);
		
		aPanel.add(Box.createVerticalStrut(16));
		
		return aPanel;
	}
	
	private JPanel initializeRow9Panel()
	{
		JPanel aPanel = new JPanel();
		aPanel.setLayout(new BoxLayout(aPanel, BoxLayout.X_AXIS));
		aPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
		
		try{
			JPanel bPanel = new JPanel();
			bPanel.setLayout(new BoxLayout(bPanel, BoxLayout.X_AXIS));
			bPanel.setPreferredSize(new Dimension(150, 25));
			bPanel.setMaximumSize(new Dimension(150, 25));
			JLabel aLabel = new JLabel("Default note size :");
			bPanel.add(aLabel);
			bPanel.add(Box.createVerticalStrut(16));
			aPanel.add(bPanel);
			
			bPanel = new JPanel();
			bPanel.setLayout(new BoxLayout(bPanel, BoxLayout.X_AXIS));
			bPanel.setPreferredSize(new Dimension(120, 25));
			bPanel.setMaximumSize(new Dimension(120, 25));
			bPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));
			aLabel = new JLabel("Width :");
			bPanel.add(aLabel);
			bPanel.add(Box.createRigidArea(new Dimension(5, 16)));
			this.mNoteWidthBox = new JTextField();
			this.mNoteWidthBox.setColumns(4);
			this.mNoteWidthBox.setHorizontalAlignment(JTextField.CENTER);
			bPanel.add(this.mNoteWidthBox);
			bPanel.add(Box.createVerticalStrut(16));
			aPanel.add(bPanel);
			
			bPanel = new JPanel();
			bPanel.setLayout(new BoxLayout(bPanel, BoxLayout.X_AXIS));
			bPanel.setPreferredSize(new Dimension(120, 25));
			bPanel.setMaximumSize(new Dimension(120, 25));
			aLabel = new JLabel("Height :");
			bPanel.add(aLabel);
			bPanel.add(Box.createRigidArea(new Dimension(5, 16)));
			this.mNoteHeightBox = new JTextField();
			this.mNoteHeightBox.setColumns(4);
			this.mNoteHeightBox.setHorizontalAlignment(JTextField.CENTER);
			bPanel.add(this.mNoteHeightBox);
			bPanel.add(Box.createVerticalStrut(16));
			aPanel.add(bPanel);
			
			aPanel.add(Box.createVerticalStrut(16));
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		return aPanel;
	}
	
	private JPanel initializeRow10Panel()
	{
		JPanel aPanel = new JPanel();
		aPanel.setLayout(new BoxLayout(aPanel, BoxLayout.X_AXIS));
		aPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
		
		aPanel.add(Box.createRigidArea(new Dimension(25, 25)));
		aPanel.add(Box.createVerticalStrut(16));
		
		this.mRestoreOriginalSettingsButton = new JButton("Restore");
		this.mRestoreOriginalSettingsButton.setMinimumSize(new Dimension(100, 20));
		this.mRestoreOriginalSettingsButton.setPreferredSize(new Dimension(100, 35));
		this.mRestoreOriginalSettingsButton.setToolTipText(DefaultNoteSettingsPanel.RESTORE_SETTINGS_BUTTON);
		this.mRestoreOriginalSettingsButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				int response = JOptionPane.showOptionDialog(mParentFrame, 
															"Are you sure you want to overwrite the current settings with the originally installed values?", 
															"Confirm Restore", 
															JOptionPane.YES_NO_OPTION, 
															JOptionPane.WARNING_MESSAGE,
															null,
															null,
															null);
				if(response == JOptionPane.YES_OPTION){
					restoreSettingsToOriginalValues();
				}
				return;
			}
		});
		aPanel.add(this.mRestoreOriginalSettingsButton);
		
		return aPanel;
	}
	
	private JButton createBorderColorButton()
	{
		this.mBorderColorButton = new JButton("Set...");
		this.mBorderColorButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				Color aColor = JColorChooser.showDialog(mBorderColorButton, "Choose Background Color", new Color(mBorderColorRed, mBorderColorGreen, mBorderColorBlue));
				if(aColor != null){
					mBorderColorRed = aColor.getRed();
					mBorderColorGreen = aColor.getGreen();
					mBorderColorBlue = aColor.getBlue();
					mBorderColorPreview.setBackground(new Color(mBorderColorRed, mBorderColorGreen, mBorderColorBlue));
				}
				return;
			}
		});
		
		return this.mBorderColorButton;
	}
	
	private JButton createTitleColorButton()
	{
		this.mTitleColorButton = new JButton("Set...");
		this.mTitleColorButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				Color aColor = JColorChooser.showDialog(mTitleColorButton, "Choose Background Color", new Color(mTitleColorRed, mTitleColorGreen, mTitleColorBlue));
				if(aColor != null){
					mTitleColorRed = aColor.getRed();
					mTitleColorGreen = aColor.getGreen();
					mTitleColorBlue = aColor.getBlue();
					mTitleColorPreview.setBackground(new Color(mTitleColorRed, mTitleColorGreen, mTitleColorBlue));
				}
				return;
			}
		});
		
		return this.mTitleColorButton;
	}
	
	private JButton createNoteColorButton()
	{
		this.mNoteColorButton = new JButton("Set...");
		this.mNoteColorButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				Color aColor = JColorChooser.showDialog(mNoteColorButton, "Choose Background Color", new Color(mNoteColorRed, mNoteColorGreen, mNoteColorBlue));
				if(aColor != null){
					mNoteColorRed = aColor.getRed();
					mNoteColorGreen = aColor.getGreen();
					mNoteColorBlue = aColor.getBlue();
					mNoteColorPreview.setBackground(new Color(mNoteColorRed, mNoteColorGreen, mNoteColorBlue));
				}
				return;
			}
		});
		
		return this.mNoteColorButton;
	}
	
	private void restoreSettingsToOriginalValues()
	{
		if(Globals.UNCHOSEN_USE_BORDER){
			this.mUseNoteBorderYes.setSelected(true);
		}else{
			this.mUseNoteBorderNo.setSelected(true);
		}
		
		this.mBorderColorRed = Globals.UNCHOSEN_BORDER_COLOR_RED;
		this.mBorderColorGreen = Globals.UNCHOSEN_BORDER_COLOR_GREEN;
		this.mBorderColorBlue = Globals.UNCHOSEN_BORDER_COLOR_BLUE;
		this.mBorderColorPreview.setBackground(new Color(this.mBorderColorRed, this.mBorderColorGreen, this.mBorderColorBlue));
		this.mBorderWidthBox.setText(String.valueOf(Globals.UNCHOSEN_BORDER_WIDTH));
		
		if(Globals.UNCHOSEN_USE_TIME_TITLE){
			this.mTimeIsDefaultTitle.setSelected(true);
			this.mCustomTitleField.setText("");
		}else{
			this.mCustomDefaultTitle.setSelected(true);
			this.mCustomTitleField.setText(Globals.UNCHOSEN_CUSTOM_TITLE);
		}
		
		if(Globals.UNCHOSEN_TITLE_ALIGNMENT == ConfigData.Align.LEFT){
			this.mTitleAlignLeft.setSelected(true);
		}else if(Globals.UNCHOSEN_TITLE_ALIGNMENT == ConfigData.Align.RIGHT){
			this.mTitleAlignRight.setSelected(true);
		}else{
			this.mTitleAlignCenter.setSelected(true);
		}
		
		this.mNoteColorRed = Globals.UNCHOSEN_NOTE_COLOR_RED;
		this.mNoteColorGreen = Globals.UNCHOSEN_NOTE_COLOR_GREEN;
		this.mNoteColorBlue = Globals.UNCHOSEN_NOTE_COLOR_BLUE;
		
		this.mNoteColorPreview.setBackground(new Color(this.mNoteColorRed, this.mNoteColorGreen, this.mNoteColorBlue));
		
		this.mTitleColorRed = Globals.UNCHOSEN_TITLE_COLOR_RED;
		this.mTitleColorGreen = Globals.UNCHOSEN_TITLE_COLOR_GREEN;
		this.mTitleColorBlue = Globals.UNCHOSEN_TITLE_COLOR_BLUE;
		
		this.mTitleColorPreview.setBackground(new Color(this.mTitleColorRed, this.mTitleColorGreen, this.mTitleColorBlue));
		
		this.mNoteFontName = Globals.UNCHOSEN_NOTE_FONT_FACE;
		this.mNoteFontSize = Globals.UNCHOSEN_NOTE_FONT_SIZE;
		this.mNoteFontBold = Globals.UNCHOSEN_NOTE_FONT_BOLD;
		this.mNoteFontItalic = Globals.UNCHOSEN_NOTE_FONT_ITALIC;
		this.mNoteFontUnderline = Globals.UNCHOSEN_NOTE_FONT_UNDERLINE;
		this.mNoteFontStrikeout = Globals.UNCHOSEN_NOTE_FONT_STRIKEOUT;
		
		try{
			this.mNoteFontButton.setText(this.mNoteFontName);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		this.mTitleFontName = Globals.UNCHOSEN_TITLE_FONT_FACE;
		this.mTitleFontSize = Globals.UNCHOSEN_TITLE_FONT_SIZE;
		this.mTitleFontBold = Globals.UNCHOSEN_TITLE_FONT_BOLD;
		this.mTitleFontItalic = Globals.UNCHOSEN_TITLE_FONT_ITALIC;
		try{
			this.mTitleFontButton.setText(this.mTitleFontName);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		this.mNoteFontColorRed = Globals.UNCHOSEN_NOTE_FONT_COLOR_RED;
		this.mNoteFontColorGreen = Globals.UNCHOSEN_NOTE_FONT_COLOR_GREEN;
		this.mNoteFontColorBlue = Globals.UNCHOSEN_NOTE_FONT_COLOR_BLUE;
		
		this.mNoteFontButton.setForeground(new Color(this.mNoteFontColorRed, this.mNoteFontColorGreen, this.mNoteFontColorBlue));
		
		this.mTitleFontColorRed = Globals.UNCHOSEN_TITLE_FONT_COLOR_RED;
		this.mTitleFontColorGreen = Globals.UNCHOSEN_TITLE_FONT_COLOR_GREEN;
		this.mTitleFontColorBlue = Globals.UNCHOSEN_TITLE_FONT_COLOR_BLUE;
		
		this.mTitleFontButton.setForeground(new Color(this.mTitleFontColorRed, this.mTitleFontColorGreen, this.mTitleFontColorBlue));
		
		this.mNoteWidthBox.setText(String.valueOf(Globals.UNCHOSEN_NOTE_WIDTH));
		this.mNoteHeightBox.setText(String.valueOf(Globals.UNCHOSEN_NOTE_HEIGHT));
		
		this.updateNoteFontPreviewPanel();
		this.updateTitleFontPreviewPanel();
		
		return;
	}
	
	private void updateNoteFontPreviewPanel()
	{
		this.mNoteFontPreview.setText("");
		this.mNoteFontPreview.getEditorKit().createDefaultDocument();
		StyledDocument doc = this.mNoteFontPreview.getStyledDocument();
		SimpleAttributeSet sas = new SimpleAttributeSet();
		try{
			StyleConstants.setFontFamily(sas, this.mNoteFontName);
		}catch(Exception ex){
			StyleConstants.setFontFamily(sas, Globals.UNCHOSEN_NOTE_FONT_FACE);
		}
		try{
			StyleConstants.setFontSize(sas, this.mNoteFontSize);
		}catch(Exception ex){
			StyleConstants.setFontSize(sas, Globals.UNCHOSEN_NOTE_FONT_SIZE);
		}
		StyleConstants.setBold(sas, this.mNoteFontBold);
		StyleConstants.setItalic(sas, this.mNoteFontItalic);
		StyleConstants.setUnderline(sas, this.mNoteFontUnderline);
		StyleConstants.setStrikeThrough(sas, this.mNoteFontStrikeout);
		try{
			StyleConstants.setForeground(sas, new Color(this.mNoteFontColorRed, this.mNoteFontColorGreen, this.mNoteFontColorBlue));
		}catch(Exception ex){
			StyleConstants.setForeground(sas, new Color(Globals.UNCHOSEN_NOTE_FONT_COLOR_RED, Globals.UNCHOSEN_NOTE_FONT_COLOR_GREEN, Globals.UNCHOSEN_NOTE_FONT_COLOR_BLUE));
		}
		try{
			int start = this.mNoteFontPreview.getStyledDocument().getDefaultRootElement().getStartOffset();
			doc.insertString(start, DefaultNoteSettingsPanel.PREVIEW_SAMPLE_TEXT, sas);
			sas = new SimpleAttributeSet();
			StyleConstants.setAlignment(sas, StyleConstants.ALIGN_CENTER);
			doc.setParagraphAttributes(start + DefaultNoteSettingsPanel.PREVIEW_SAMPLE_TEXT.length() - 1, 1, sas, true);
		}catch(BadLocationException ble){
			ble.printStackTrace();
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return;
	}
	
	private void updateTitleFontPreviewPanel()
	{
		try{
			int style = 0;
			if(this.mTitleFontBold){
				style += Font.BOLD;
			}
			if(this.mTitleFontItalic){
				style += Font.ITALIC;
			}
			this.mTitleFontPreview.setFont(new Font(this.mTitleFontName, style, this.mTitleFontSize));
		}catch(Exception ex){
			int style = 0;
			if(Globals.UNCHOSEN_TITLE_FONT_BOLD){
				style += Font.BOLD;
			}
			if(Globals.UNCHOSEN_TITLE_FONT_ITALIC){
				style += Font.ITALIC;
			}
			this.mTitleFontPreview.setFont(new Font(Globals.UNCHOSEN_TITLE_FONT_FACE, style, Globals.UNCHOSEN_TITLE_FONT_SIZE));
		}
		
		try{
			this.mTitleFontPreview.setForeground(new Color(this.mTitleFontColorRed, this.mTitleFontColorGreen, this.mTitleFontColorBlue));
		}catch(Exception ex){
			this.mTitleFontPreview.setForeground(new Color(Globals.UNCHOSEN_TITLE_FONT_COLOR_RED, Globals.UNCHOSEN_TITLE_FONT_COLOR_GREEN, Globals.UNCHOSEN_TITLE_FONT_COLOR_BLUE));
		}
		this.mTitleFontPreview.repaint();
		return;
	}
	
	public boolean useNoteBorder()
	{
		boolean value = false;
		if(this.mUseNoteBorderYes.isSelected()){
			value = true;
		}else if(this.mUseNoteBorderNo.isSelected()){
			value = false;
		}
		return value;
	}
	
	public boolean useTimeForTitle()
	{
		boolean value = false;
		if(this.mTimeIsDefaultTitle.isSelected()){
			value = true;
		}else if(this.mCustomDefaultTitle.isSelected()){
			value = false;
		}
		return value;
	}
	
	public String getCustomTitle()
	{
		return this.mCustomTitleField.getText();
	}
	
	public ConfigData.Align titleAlignment()
	{
		ConfigData.Align alignment = null;
		if(this.mTitleAlignLeft.isSelected()){
			alignment = ConfigData.Align.LEFT;
		}else if(this.mTitleAlignCenter.isSelected()){
			alignment = ConfigData.Align.CENTER;
		}else if(this.mTitleAlignRight.isSelected()){
			alignment = ConfigData.Align.RIGHT;
		}
		return alignment;
	}
	
	public int getDefaultNoteColorRed()
	{
		return this.mNoteColorRed;
	}
	
	public int getDefaultNoteColorGreen()
	{
		return this.mNoteColorGreen;
	}
	
	public int getDefaultNoteColorBlue()
	{
		return this.mNoteColorBlue;
	}
	
	public int getDefaultTitleColorRed()
	{
		return this.mTitleColorRed;
	}
	
	public int getDefaultTitleColorGreen()
	{
		return this.mTitleColorGreen;
	}
	
	public int getDefaultTitleColorBlue()
	{
		return this.mTitleColorBlue;
	}
	
	public int getDefaultBorderColorRed()
	{
		return this.mBorderColorRed;
	}
	
	public int getDefaultBorderColorGreen()
	{
		return this.mBorderColorGreen;
	}
	
	public int getDefaultBorderColorBlue()
	{
		return this.mBorderColorBlue;
	}
	
	public int getDefaultBorderWidth()
	{
		return Integer.parseInt(this.mBorderWidthBox.getText());
	}
	
	public String getNoteFontName()
	{
		return this.mNoteFontName;
	}
	
	public int getNoteFontSize()
	{
		return this.mNoteFontSize;
	}
	
	public boolean getNoteFontBold()
	{
		return this.mNoteFontBold;
	}
	
	public boolean getNoteFontItalic()
	{
		return this.mNoteFontItalic;
	}
	
	public boolean getNoteFontUnderline()
	{
		return this.mNoteFontUnderline;
	}
	
	public boolean getNoteFontStrikeout()
	{
		return this.mNoteFontStrikeout;
	}
	
	public String getTitleFontName()
	{
		return this.mTitleFontName;
	}
	
	public int getTitleFontSize()
	{
		return this.mTitleFontSize;
	}
	
	public boolean getTitleFontBold()
	{
		return this.mTitleFontBold;
	}
	
	public boolean getTitleFontItalic()
	{
		return this.mTitleFontItalic;
	}
	
	public int getNoteFontColorRed()
	{
		return this.mNoteFontColorRed;
	}
	
	public int getNoteFontColorGreen()
	{
		return this.mNoteFontColorGreen;
	}
	
	public int getNoteFontColorBlue()
	{
		return this.mNoteFontColorBlue;
	}
	
	public int getTitleFontColorRed()
	{
		return this.mTitleFontColorRed;
	}
	
	public int getTitleFontColorGreen()
	{
		return this.mTitleFontColorGreen;
	}
	
	public int getTitleFontColorBlue()
	{
		return this.mTitleFontColorBlue;
	}
	
	public int getNoteWidth()
	{
		return Integer.parseInt(this.mNoteWidthBox.getText());
	}
	
	public int getNoteHeight()
	{
		return Integer.parseInt(this.mNoteHeightBox.getText());
	}
	
	public String getFormErrors()
	{
		String errorText = null;
		
		if(!this.tryParsingNumericField(this.mNoteWidthBox)){
			errorText = "Note width value must be a number.";
		}else{
			int x = Integer.parseInt(this.mNoteWidthBox.getText());
			if(x < 300 || x > 9999){
				errorText = "Note width value must be greater than 300 and less than 9999.";
			}
		}
		
		if(errorText == null){
			if(!this.tryParsingNumericField(this.mNoteHeightBox)){
				errorText = "Note height value must be a number.";
			}else{
				int y = Integer.parseInt(this.mNoteHeightBox.getText());
				if(y < 200 || y > 9999){
					errorText = "Note height value must be greater than 200 and less than 9999.";
				}
			}
		}
		
		return errorText;
	}
		
	private boolean tryParsingNumericField(JTextField textField)
	{
		boolean successful = true;
		try{
			if(textField.getText() != null){
				int x = Integer.parseInt(textField.getText());
			}
			successful = true;
		}catch(Exception ex){
			successful = false;
		}
		return successful;
	}
	
	public void changedUpdate(DocumentEvent e) { return; }
	
	public void insertUpdate(DocumentEvent e)
	{
		if(!this.mCustomDefaultTitle.isSelected()){
			this.mCustomDefaultTitle.setSelected(true);
		}
		return;
	}
	
	public void removeUpdate(DocumentEvent e) { return; }
}
