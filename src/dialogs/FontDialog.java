package dialogs;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.image.BufferedImage;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Toolkit;

import javax.swing.border.Border;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ListSelectionModel;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.swing.UIManager;

public class FontDialog extends JDialog implements ActionListener, ItemListener, ListSelectionListener
{
	private static final String PREVIEW_SAMPLE_TEXT = "AaBbCcXxYyZz";
	
	private enum UserState{OKAY, CANCEL};
	
	private JPanel mBasePanel = null;
	private JTextField mFontNameBox = null;
	private FontList mFontList = null;
	private JScrollPane mFontScrollPane = null;
	private JButton mColorButton = null;
	private static final String COLOR_BUTTON = "Choose font color.";
	private JLabel mColorPreview = null;
	private JCheckBox mBoldOption = null;
	private JCheckBox mItalicOption = null;
	private JCheckBox mUnderlineOption = null;
	private JCheckBox mStrikeoutOption = null;
	private JTextField mSizeBox = null;
	private JList<String> mSizeList = null;
	private JScrollPane mSizeScrollPane = null;
	private JTextPane mPreviewPanel = null;
	private JButton mOkayButton = null;
	private static final String OKAY_BUTTON = "Close and use selected color.";
	private JButton mCancelButton = null;
	private static final String CANCEL_BUTTON = "Cancel and close.";
	
	private boolean mUnderlineOptionEnabled = false;
	private boolean mStrikeoutOptionEnabled = false;
	private String[] mPresetFontNames = null;
	private FontData mPresetFontData = null;
	private FontDialog.UserState mUserState = FontDialog.UserState.CANCEL;
	
	public FontDialog(JFrame parent, String[] fontNames, boolean enableUnderlineOption, boolean enableStrikeoutOption) throws Exception
	{
		super(parent, true);
		try{
			this.preSetFontList(fontNames);
			this.mUnderlineOptionEnabled = enableUnderlineOption;
			this.mStrikeoutOptionEnabled = enableStrikeoutOption;
			this.initializeMain();
		}catch(Exception ex){
			throw ex;
		}
		return;
	}
	
	public FontDialog(JFrame parent, String[] fontNames, FontData fd, boolean enableUnderlineOption, boolean enableStrikeoutOption) throws Exception
	{
		super(parent, true);
		try{
			this.preSetFontList(fontNames);
			this.presetFontData(fd);
			this.mUnderlineOptionEnabled = enableUnderlineOption;
			this.mStrikeoutOptionEnabled = enableStrikeoutOption;
			this.initializeMain();
		}catch(Exception ex){
			throw ex;
		}
		return;
	}
	
	/**
	 * Shows this dialog window, with modal properties if used.
	 * @return FontData object, if user clicks Okay button, null if Cancel button chosen. 
	 */
	public FontData showDialog()
	{
		FontData fd = null;
		this.setLocationRelativeTo(null); // set center screen.
		this.setVisible(true);
		this.pack();
		
		if(this.mUserState.compareTo(FontDialog.UserState.OKAY) == 0){
			try{
				fd = new FontData();
				fd.fontFace = this.mFontNameBox.getText();
				fd.fontSize = Integer.parseInt(this.mSizeBox.getText());
				fd.isBold = this.mBoldOption.isSelected();
				fd.isItalic = this.mItalicOption.isSelected();
				fd.isUnderline = this.mUnderlineOption.isSelected();
				fd.isStrikeout = this.mStrikeoutOption.isSelected();
				fd.redValue = this.mColorPreview.getBackground().getRed();
				fd.greenValue = this.mColorPreview.getBackground().getGreen();
				fd.blueValue = this.mColorPreview.getBackground().getBlue();
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
		
		return fd;
	}
	
	private void preSetFontList(String[] fontNames) throws Exception
	{
		try{
			if(fontNames == null){
				throw new Exception("Font names array cannot be null.");
			}
			if(fontNames.length <= 0){
				throw new Exception("Font name array must have at least one element.");
			}
			this.mPresetFontNames = fontNames;
		}catch(Exception ex){
			throw ex;
		}
		return;
	}
	
	private void presetFontData(FontData fd) throws Exception
	{
		try{
			if(fd == null){
				throw new Exception("Parameter cannot be null.");
			}
			this.mPresetFontData = fd;
		}catch(Exception ex){
			throw ex;
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
				System.out.println("Error on look and feel");
			}
		}
		this.setTitle("Choose Font");
		this.setResizable(false);
		
		this.mBasePanel = new JPanel();
		this.mBasePanel.setLayout(new BoxLayout(this.mBasePanel, BoxLayout.Y_AXIS));
		this.mBasePanel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
		this.add(this.mBasePanel);
		
		this.mBasePanel.add(this.getTopHalfPanel());
		this.mBasePanel.add(this.getPreviewPanel());
		this.mBasePanel.add(this.getButtonPanel());
		
		this.fillFontList();
		this.presetDialogControlValues();
		this.updatePreviewPanel();
		
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setMinimumSize(new Dimension(440, 420));
		this.setPreferredSize(new Dimension(440, 420));
		this.getContentPane().add(this.mBasePanel);
		return;
	}
	
	private JPanel getTopHalfPanel()
	{
		JPanel aPanel = new JPanel();
		aPanel.setLayout(new BoxLayout(aPanel, BoxLayout.X_AXIS));
		aPanel.setPreferredSize(new Dimension(430, 230));
		aPanel.setMaximumSize(new Dimension(430, 230));
		
		aPanel.add(this.getLeftColumnPanel());
		aPanel.add(this.getCenterColumnPanel());
		aPanel.add(this.getRightColumnPanel());
		
		return aPanel;
	}
	
	private JPanel getLeftColumnPanel()
	{
		JPanel aPanel = new JPanel();
		aPanel.setLayout(new BoxLayout(aPanel, BoxLayout.Y_AXIS));
		aPanel.setPreferredSize(new Dimension(180, 230));
		aPanel.setMinimumSize(new Dimension(180, 230));
		aPanel.setMaximumSize(new Dimension(180, 230));
		
		JPanel bPanel = new JPanel();
		bPanel.setLayout(new BoxLayout(bPanel, BoxLayout.X_AXIS));
		bPanel.setPreferredSize(new Dimension(180, 25));
		bPanel.setMaximumSize(new Dimension(180, 25));
		JLabel aLabel = new JLabel("Font :");
		bPanel.add(aLabel);
		bPanel.add(Box.createVerticalStrut(16));
		aPanel.add(bPanel);
		
		bPanel = new JPanel();
		bPanel.setLayout(new BoxLayout(bPanel, BoxLayout.X_AXIS));
		bPanel.setPreferredSize(new Dimension(180, 25));
		bPanel.setMaximumSize(new Dimension(180, 25));
		this.mFontNameBox = new JTextField();
		this.mFontNameBox.setMargin(new Insets(2, 8, 2, 8));
		bPanel.add(this.mFontNameBox);
		bPanel.add(Box.createVerticalStrut(16));
		aPanel.add(bPanel);
		
		bPanel = new JPanel();
		bPanel.setLayout(new BoxLayout(bPanel, BoxLayout.X_AXIS));
		this.mFontList = new FontList();
		this.mFontList.setVisibleRowCount(6);
		this.mFontList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.mFontList.addListSelectionListener(this);
		this.mFontScrollPane = new JScrollPane(this.mFontList,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		bPanel.add(this.mFontScrollPane);
		bPanel.add(Box.createVerticalStrut(16));
		aPanel.add(bPanel);
		
		aPanel.add(Box.createHorizontalStrut(16));
		
		return aPanel;
	}
	
	private JPanel getCenterColumnPanel()
	{
		JPanel aPanel = new JPanel();
		aPanel.setLayout(new BoxLayout(aPanel, BoxLayout.Y_AXIS));
		aPanel.setPreferredSize(new Dimension(170, 230));
		aPanel.setMaximumSize(new Dimension(170, 230));
		
		JPanel bPanel = new JPanel();
		bPanel.setLayout(new BoxLayout(bPanel, BoxLayout.X_AXIS));
		bPanel.setPreferredSize(new Dimension(170, 25));
		bPanel.setMaximumSize(new Dimension(170, 25));
		bPanel.add(new JLabel("Style & Effects:"));
		bPanel.add(Box.createVerticalStrut(16));
		aPanel.add(bPanel);
		
		aPanel.add(this.createCenterPanelRow(this.mBoldOption = new JCheckBox("Bold")));
		aPanel.add(this.createCenterPanelRow(this.mItalicOption = new JCheckBox("Italic")));
		aPanel.add(this.createCenterPanelRow(this.mUnderlineOption = new JCheckBox("Underline")));
		aPanel.add(this.createCenterPanelRow(this.mStrikeoutOption = new JCheckBox("Strikeout")));
		
		this.mBoldOption.addItemListener(this);
		this.mItalicOption.addItemListener(this);
		this.mUnderlineOption.setEnabled(this.mUnderlineOptionEnabled);
		if(this.mUnderlineOptionEnabled){
			this.mUnderlineOption.addItemListener(this);
		}
		this.mStrikeoutOption.setEnabled(this.mStrikeoutOptionEnabled);
		if(this.mStrikeoutOptionEnabled){
			this.mStrikeoutOption.addItemListener(this);
		}
		
		bPanel = new JPanel();
		bPanel.setLayout(new BoxLayout(bPanel, BoxLayout.X_AXIS));
		bPanel.setMaximumSize(new Dimension(145, 55));
		bPanel.setPreferredSize(new Dimension(145, 55));
		bPanel.setBorder(BorderFactory.createEmptyBorder(10, 25, 0, 0));
		this.mColorButton = new JButton("Color...");
		this.mColorButton.setToolTipText(FontDialog.COLOR_BUTTON);
		this.mColorButton.addActionListener(this);
		bPanel.add(this.mColorButton);
		bPanel.add(Box.createHorizontalStrut(4));
		this.mColorPreview = new JLabel();
		this.mColorPreview.setMinimumSize(new Dimension(25, 20));
		this.mColorPreview.setMaximumSize(new Dimension(25, 20));
		this.mColorPreview.setBorder(BorderFactory.createEtchedBorder());
		this.mColorPreview.setOpaque(true);
		bPanel.add(this.mColorPreview);
		aPanel.add(bPanel);
		
		return aPanel;
	}
	
	private JPanel createCenterPanelRow(JComponent control)
	{
		JPanel aPanel = new JPanel();
		aPanel.setLayout(new BoxLayout(aPanel, BoxLayout.X_AXIS));
		aPanel.setMaximumSize(new Dimension(155, 35));
		aPanel.setPreferredSize(new Dimension(155, 35));
		aPanel.setBorder(BorderFactory.createEmptyBorder(20, 25, 0, 0));
		
		aPanel.add(control);
		
		return aPanel;
	}
	
	private JPanel getRightColumnPanel()
	{
		JPanel aPanel = new JPanel();
		aPanel.setLayout(new BoxLayout(aPanel, BoxLayout.Y_AXIS));
		aPanel.setPreferredSize(new Dimension(70, 230));
		aPanel.setMinimumSize(new Dimension(70, 230));
		aPanel.setMaximumSize(new Dimension(70, 230));
		
		JPanel bPanel = new JPanel();
		bPanel.setLayout(new BoxLayout(bPanel, BoxLayout.X_AXIS));
		bPanel.setPreferredSize(new Dimension(70, 20));
		bPanel.setMaximumSize(new Dimension(70, 20));
		JLabel aLabel = new JLabel("Font size :");
		bPanel.add(aLabel);
		bPanel.add(Box.createVerticalStrut(16));
		aPanel.add(bPanel);
		
		bPanel = new JPanel();
		bPanel.setLayout(new BoxLayout(bPanel, BoxLayout.X_AXIS));
		bPanel.setPreferredSize(new Dimension(70, 30));
		bPanel.setMaximumSize(new Dimension(70, 30));
		bPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
		this.mSizeBox = new JTextField();
		bPanel.add(this.mSizeBox);
		bPanel.add(Box.createVerticalStrut(16));
		aPanel.add(bPanel);
		
		bPanel = new JPanel();
		bPanel.setLayout(new BoxLayout(bPanel, BoxLayout.X_AXIS));
		bPanel.setPreferredSize(new Dimension(70, 165));
		bPanel.setMaximumSize(new Dimension(70, 165));
		this.mSizeList = new JList<String>(new String[]{"8", "9", "10", "11", "12", "14", "16", "18", "20", "22", "24", "26", "28", "36", "48", "72"});
		this.mSizeList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.mSizeList.setVisibleRowCount(6);
		this.mSizeScrollPane = new JScrollPane(this.mSizeList,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		this.mSizeList.addListSelectionListener(this);
		bPanel.add(this.mSizeScrollPane);
		aPanel.add(bPanel);
		
		aPanel.add(Box.createHorizontalStrut(16));
		
		return aPanel;
	}
	
	private JPanel getPreviewPanel()
	{
		JPanel aPanel = new JPanel();
		aPanel.setLayout(new BoxLayout(aPanel, BoxLayout.X_AXIS));
		aPanel.setPreferredSize(new Dimension(430, 100));
		aPanel.setMaximumSize(new Dimension(430, 100));
		Border outer = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Sample");
		Border inner = BorderFactory.createEmptyBorder(5, 5, 5, 5);
		aPanel.setBorder(BorderFactory.createCompoundBorder(outer, inner));
		
		this.mPreviewPanel = new JTextPane();
		this.mPreviewPanel.setPreferredSize(new Dimension(410, 110));
		this.mPreviewPanel.setMinimumSize(new Dimension(350, 90));
		this.mPreviewPanel.setMaximumSize(new Dimension(410, 110));
		this.mPreviewPanel.setEditable(false);
		
		aPanel.add(this.mPreviewPanel);
		
		aPanel.add(Box.createVerticalStrut(16));
		
		return aPanel;
	}
	
	private JPanel getButtonPanel()
	{
		JPanel aPanel = new JPanel();
		aPanel.setLayout(new BoxLayout(aPanel, BoxLayout.X_AXIS));
		aPanel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
		
		aPanel.add(Box.createVerticalStrut(16));
		
		this.mOkayButton = new JButton("Okay");
		this.mOkayButton.setToolTipText(FontDialog.OKAY_BUTTON);
		this.mOkayButton.addActionListener(this);
		aPanel.add(this.mOkayButton);
		
		aPanel.add(Box.createHorizontalStrut(10));
		
		this.mCancelButton = new JButton("Cancel");
		this.mCancelButton.setToolTipText(FontDialog.CANCEL_BUTTON);
		this.mCancelButton.addActionListener(this);
		aPanel.add(this.mCancelButton);
		
		return aPanel;
	}
	
	private void fillFontList()
	{
		// *** Decided not to retrieve every last system font.  To slow, and doesn't
		// *** guarantee the same fonts on all systems.
		//final GraphicsEnvironment g = GraphicsEnvironment.getLocalGraphicsEnvironment();
		//final String[] fontFamilyNames = g.getAvailableFontFamilyNames();
		
		try{
			this.mFontList.setList(this.mPresetFontNames);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return;
	}
	
	private void presetDialogControlValues()
	{
		try{
			int initialIndex = this.mFontList.find(this.mPresetFontData.fontFace);
			if(initialIndex > -1){
				this.mFontList.setSelectedIndex(initialIndex);
			}else{
				this.mFontList.setSelectedIndex(0);
			}

			this.setSizeListIndexByFontSize(this.mPresetFontData.fontSize);
			
			try{
				this.mBoldOption.setSelected(this.mPresetFontData.isBold);
				this.mItalicOption.setSelected(this.mPresetFontData.isItalic);
				if(this.mUnderlineOptionEnabled){
					this.mUnderlineOption.setSelected(this.mPresetFontData.isUnderline);
				}else{
					this.mUnderlineOption.setSelected(false);
				}
				if(this.mStrikeoutOptionEnabled){
					this.mStrikeoutOption.setSelected(this.mPresetFontData.isStrikeout);
				}else{
					this.mStrikeoutOption.setSelected(false);
				}
			}catch(Exception ex){
				ex.printStackTrace();
			}
			this.mColorPreview.setBackground(new Color(this.mPresetFontData.redValue, this.mPresetFontData.greenValue, this.mPresetFontData.blueValue));
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return;
	}
	
	private void setSizeListIndexByFontSize(int size)
	{
		boolean found = false;
		for(int i = 0; i < this.mSizeList.getModel().getSize(); i++)
		{
			if(Integer.valueOf(this.mSizeList.getModel().getElementAt(i)) == size){
				this.mSizeList.setSelectedIndex(i);
				found = true;
				break;
			}
		}
		if(!found){
			this.mSizeList.setSelectedIndex(0);
		}
	}
	
	private void updatePreviewPanel()
	{
		this.mPreviewPanel.setText("");
		this.mPreviewPanel.getEditorKit().createDefaultDocument();
		StyledDocument doc = this.mPreviewPanel.getStyledDocument();
		SimpleAttributeSet sas = new SimpleAttributeSet();
		
		try{
			StyleConstants.setFontFamily(sas, this.mFontNameBox.getText());
		}catch(Exception ex){
			StyleConstants.setFontFamily(sas, "Arial");
		}
		try{
			StyleConstants.setFontSize(sas, Integer.parseInt(this.mSizeBox.getText()));
		}catch(Exception ex){
			StyleConstants.setFontSize(sas, 12);
		}
		StyleConstants.setBold(sas, this.mBoldOption.isSelected());
		StyleConstants.setItalic(sas, this.mItalicOption.isSelected());
		StyleConstants.setUnderline(sas, this.mUnderlineOption.isSelected());
		StyleConstants.setStrikeThrough(sas, this.mStrikeoutOption.isSelected());
		try{
			StyleConstants.setForeground(sas, this.mColorPreview.getBackground());
		}catch(Exception ex){
			StyleConstants.setForeground(sas, new Color(0, 0, 0));
		}
		try{
			int start = this.mPreviewPanel.getStyledDocument().getDefaultRootElement().getStartOffset();
			doc.insertString(start, FontDialog.PREVIEW_SAMPLE_TEXT, sas);
			sas = new SimpleAttributeSet();
			StyleConstants.setAlignment(sas, StyleConstants.ALIGN_CENTER);
			doc.setParagraphAttributes(start + FontDialog.PREVIEW_SAMPLE_TEXT.length() - 1, 1, sas, true);
		}catch(BadLocationException ble){
			ble.printStackTrace();
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		return;
	}
	
	private void getColorForPreviewLabel(JButton button)
	{
		Color aColor = null;
		if(this.mPresetFontData != null){
			aColor = JColorChooser.showDialog(button, "Choose Font Color", new Color(this.mPresetFontData.redValue, this.mPresetFontData.greenValue, this.mPresetFontData.blueValue));
		}else{
			aColor = JColorChooser.showDialog(button, "Choose Font Color", Color.BLACK);
		}
		this.mColorPreview.setBackground(aColor);
		this.updatePreviewPanel();
		return;
	}
	
	public void actionPerformed(ActionEvent source)
	{
		if(source.getSource() == (JButton)this.mOkayButton){
			try{
				this.mUserState = FontDialog.UserState.OKAY;
				this.disposeWindow();
			}catch(IllegalMonitorStateException imse){
				imse.printStackTrace();
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}else if(source.getSource() == (JButton)this.mCancelButton){
			try{
				this.mUserState = FontDialog.UserState.CANCEL;
				this.disposeWindow();
			}catch(IllegalMonitorStateException imse){
				imse.printStackTrace();
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}else if(source.getSource() == (JButton)this.mColorButton){
			this.getColorForPreviewLabel(this.mColorButton);
		}
		return;
	}
	
	public void itemStateChanged(ItemEvent e)
	{
		if(e.getSource() == (JCheckBox)this.mBoldOption){
			this.updatePreviewPanel();
		}else if(e.getSource() == (JCheckBox)this.mItalicOption){
			this.updatePreviewPanel();
		}else if(e.getSource() == (JCheckBox)this.mUnderlineOption){
			this.updatePreviewPanel();
		}else if(e.getSource() == (JCheckBox)this.mStrikeoutOption){
			this.updatePreviewPanel();
		}
		return;
	}
	
	public void valueChanged(ListSelectionEvent e)
	{
		if(e.getSource() == (FontList)this.mFontList){
			if(!e.getValueIsAdjusting()){
				this.mFontNameBox.setText(this.mFontList.getListValue());
				
			}
		}else if(e.getSource() == (JList<String>)this.mSizeList){
			if(!e.getValueIsAdjusting()){
				this.mSizeBox.setText(this.mSizeList.getSelectedValue());
			}
		}
		if(this.mFontNameBox.getText() != null && this.mSizeBox.getText() != null){
			this.updatePreviewPanel();
		}
		return;
	}
	
	public void disposeWindow()
	{
		this.dispose();
		return;
	}
}
