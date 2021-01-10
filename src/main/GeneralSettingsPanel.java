package main;

import config.ConfigData;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

/**
 * 
 * @author John McCullock
 * @version 2.0 2013-10-19
 */

public class GeneralSettingsPanel extends JPanel
{
	private JRadioButton mConfirmBeforeDeletingNotesYes = null;
	private JRadioButton mConfirmBeforeDeletingNotesNo = null;
	
	private JRadioButton mShowTemplatesItemInTrayMenuYes = null;
	private JRadioButton mShowTemplatesItemInTrayMenuNo = null;
	
	private JRadioButton mShowSplashAtStartupYes = null;
	private JRadioButton mShowSplashAtStartupNo = null;
	
	private JRadioButton mPlaySoundUponReceivingYes = null;
	private JRadioButton mPlaySoundUponReceivingNo = null;
	
	private JRadioButton mNotesExpireYes = null;
	private JRadioButton mNotesExpireNo = null;
	private JTextField mExpirationBox = null;
	
	private JButton mAboutButton = null;
	private static final String ABOUT_BUTTON = "About Tilt Notes.";
	
	public GeneralSettingsPanel()
	{
		super();
		this.initializeMain();
		return;
	}
	
	private void initializeMain()
	{
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.setBorder(BorderFactory.createEmptyBorder(25, 5, 5, 5));
		
		this.mConfirmBeforeDeletingNotesYes = new JRadioButton("Yes");
		this.mConfirmBeforeDeletingNotesNo = new JRadioButton("No");
		this.mShowTemplatesItemInTrayMenuYes = new JRadioButton("Yes");
		this.mShowTemplatesItemInTrayMenuNo = new JRadioButton("No");
		this.mShowSplashAtStartupYes = new JRadioButton("Yes");
		this.mShowSplashAtStartupNo = new JRadioButton("No");
		this.mPlaySoundUponReceivingYes = new JRadioButton("Yes");
		this.mPlaySoundUponReceivingNo = new JRadioButton("No");
		
		this.add(this.getRadioButtonRow("Confirm before deleting notes :", this.mConfirmBeforeDeletingNotesNo, this.mConfirmBeforeDeletingNotesYes));
		this.add(this.getRadioButtonRow("Show Templates in tray menu :", this.mShowTemplatesItemInTrayMenuNo, this.mShowTemplatesItemInTrayMenuYes));
		this.add(this.getRadioButtonRow("Show splash at startup :", this.mShowSplashAtStartupNo, this.mShowSplashAtStartupYes));
		this.add(this.getRadioButtonRow("Play sound upon receiving note :", this.mPlaySoundUponReceivingNo, this.mPlaySoundUponReceivingYes));
		this.add(this.getExpirationRow());
		this.add(Box.createHorizontalStrut(16));
		this.add(this.getAboutRowPanel());
		
		if(ConfigData.getConfirmBeforeNoteDeletion()){
			this.mConfirmBeforeDeletingNotesYes.setSelected(true);
		}else{
			this.mConfirmBeforeDeletingNotesNo.setSelected(true);
		}
		
		if(ConfigData.getShowTemplateItemInTrayMenu()){
			this.mShowTemplatesItemInTrayMenuYes.setSelected(true);
		}else{
			this.mShowTemplatesItemInTrayMenuNo.setSelected(true);
		}
		
		if(ConfigData.getShowSplashAtStartup()){
			this.mShowSplashAtStartupYes.setSelected(true);
		}else{
			this.mShowSplashAtStartupNo.setSelected(true);
		}
		
		if(ConfigData.getPlaySoundUponReceiving()){
			this.mPlaySoundUponReceivingYes.setSelected(true);
		}else{
			this.mPlaySoundUponReceivingNo.setSelected(true);
		}
		
		if(ConfigData.getNotesExpire()){
			this.mNotesExpireYes.setSelected(true);
			this.mExpirationBox.setEnabled(true);
			this.mExpirationBox.setText(String.valueOf(ConfigData.getExpirationMinutes()));
		}else{
			this.mNotesExpireNo.setSelected(true);
			this.mExpirationBox.setText(String.valueOf(ConfigData.getExpirationMinutes()));
			this.mExpirationBox.setEnabled(false);
		}
		
		// Temporary disabling until implementation.
		this.mShowSplashAtStartupNo.setEnabled(false);
		this.mShowSplashAtStartupYes.setEnabled(false);
		
		return;
	}
	
	private JPanel getRadioButtonRow(String labelText, JRadioButton no, JRadioButton yes)
	{
		JPanel aPanel = new JPanel();
		aPanel.setLayout(new BoxLayout(aPanel, BoxLayout.X_AXIS));
		
		JPanel bPanel = new JPanel();
		bPanel.setLayout(new BoxLayout(bPanel, BoxLayout.X_AXIS));
		bPanel.setPreferredSize(new Dimension(200, 25));
		bPanel.setMaximumSize(new Dimension(200, 25));
		JLabel aLabel = new JLabel(labelText);
		bPanel.add(aLabel);
		bPanel.add(Box.createVerticalStrut(16));
		aPanel.add(bPanel);
		
		ButtonGroup group = new ButtonGroup();
		
		bPanel = new JPanel();
		bPanel.setLayout(new BoxLayout(bPanel, BoxLayout.X_AXIS));
		bPanel.setPreferredSize(new Dimension(80, 25));
		bPanel.setMaximumSize(new Dimension(80, 25));
		group.add(no);
		bPanel.add(no);
		bPanel.add(Box.createVerticalStrut(16));
		aPanel.add(bPanel);
		
		bPanel = new JPanel();
		bPanel.setLayout(new BoxLayout(bPanel, BoxLayout.X_AXIS));
		bPanel.setPreferredSize(new Dimension(80, 25));
		bPanel.setMaximumSize(new Dimension(80, 25));
		group.add(yes);
		bPanel.add(yes);
		bPanel.add(Box.createVerticalStrut(16));
		aPanel.add(bPanel);
		
		aPanel.add(Box.createVerticalStrut(16));
		
		return aPanel;
	}
	
	private JPanel getExpirationRow()
	{
		JPanel aPanel = new JPanel();
		aPanel.setLayout(new BoxLayout(aPanel, BoxLayout.X_AXIS));
		
		JPanel bPanel = new JPanel();
		bPanel.setLayout(new BoxLayout(bPanel, BoxLayout.X_AXIS));
		bPanel.setPreferredSize(new Dimension(200, 25));
		bPanel.setMaximumSize(new Dimension(200, 25));
		JLabel aLabel = new JLabel("Delete notes upon expire :");
		bPanel.add(aLabel);
		bPanel.add(Box.createVerticalStrut(16));
		aPanel.add(bPanel);
		
		ButtonGroup group = new ButtonGroup();
		
		bPanel = new JPanel();
		bPanel.setLayout(new BoxLayout(bPanel, BoxLayout.X_AXIS));
		bPanel.setPreferredSize(new Dimension(80, 25));
		bPanel.setMaximumSize(new Dimension(80, 25));
		this.mNotesExpireNo = new JRadioButton("No");
		group.add(this.mNotesExpireNo);
		bPanel.add(this.mNotesExpireNo);
		bPanel.add(Box.createVerticalStrut(16));
		aPanel.add(bPanel);
		
		bPanel = new JPanel();
		bPanel.setLayout(new BoxLayout(bPanel, BoxLayout.X_AXIS));
		bPanel.setPreferredSize(new Dimension(50, 25));
		bPanel.setMaximumSize(new Dimension(50, 25));
		this.mNotesExpireYes = new JRadioButton("Yes,");
		group.add(this.mNotesExpireYes);
		bPanel.add(this.mNotesExpireYes);
		bPanel.add(Box.createVerticalStrut(16));
		aPanel.add(bPanel);
		
		try{
			bPanel = new JPanel();
			bPanel.setLayout(new BoxLayout(bPanel, BoxLayout.X_AXIS));
			bPanel.setPreferredSize(new Dimension(120, 25));
			bPanel.setMaximumSize(new Dimension(120, 25));
			aLabel = new JLabel("after ");
			bPanel.add(aLabel);
			this.mExpirationBox = new JTextField();
			this.mExpirationBox.setColumns(3);
			this.mExpirationBox.setHorizontalAlignment(JTextField.CENTER);
			bPanel.add(this.mExpirationBox);
			aLabel = new JLabel(" minutes.");
			bPanel.add(aLabel);
			bPanel.add(Box.createVerticalStrut(16));
			aPanel.add(bPanel);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		aPanel.add(Box.createVerticalStrut(16));
		
		return aPanel;
	}
	
	private JPanel getAboutRowPanel()
	{
		JPanel aPanel = new JPanel();
		aPanel.setLayout(new BoxLayout(aPanel, BoxLayout.X_AXIS));
		
		aPanel.add(Box.createVerticalStrut(16));
		
		this.mAboutButton = new JButton("About...");
		this.mAboutButton.setToolTipText(GeneralSettingsPanel.ABOUT_BUTTON);
		this.mAboutButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				try{
					TabListener.notifyAboutWindow(this);
				}catch(IllegalMonitorStateException imse){
					imse.printStackTrace();
				}catch(Exception ex){
					ex.printStackTrace();
				}
				return;
			}
		});
		aPanel.add(this.mAboutButton);
		
		return aPanel;
	}
	
	public boolean getDeleteConfirmation()
	{
		boolean value = false;
		if(this.mConfirmBeforeDeletingNotesYes.isSelected()){
			value = true;
		}else{
			value = false;
		}
		return value;
	}
	
	public boolean getTemplateInTrayMenu()
	{
		boolean value = false;
		if(this.mShowTemplatesItemInTrayMenuYes.isSelected()){
			value = true;
		}else{
			value = false;
		}
		return value;
	}
	
	public boolean getSplashAtStartup()
	{
		boolean value = false;
		if(this.mShowSplashAtStartupYes.isSelected()){
			value = true;
		}else{
			value = false;
		}
		return value;
	}
	
	public boolean getPlaySoundOnReceive()
	{
		boolean value = false;
		if(this.mPlaySoundUponReceivingYes.isSelected()){
			value = true;
		}else{
			value = false;
		}
		return value;
	}
	
	public boolean getNotesExpire()
	{
		boolean value = false;
		if(this.mNotesExpireYes.isSelected()){
			value = true;
		}else{
			value = false;
		}
		return value;
	}
	
	public int getExpirationMinutes()
	{
		return Integer.parseInt(this.mExpirationBox.getText());
	}
	
	public String getFormErrors()
	{
		String errorText = null;
		
		if(!this.tryParsingNumericField(this.mExpirationBox)){
			errorText = "Minutes before expiration value must be a number.";
		}else{
			int x = Integer.parseInt(this.mExpirationBox.getText());
			if(x < 0 || x > 999){
				errorText = "Expiration value must be greater than -1 and less than 999.";
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
}
