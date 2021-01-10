package main;

import config.ConfigData;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class NetworkSettingsPanel extends JPanel
{
	private JFrame mParentFrame = null;
	private JTextField mMulticastIPBox = null;
	private JTextField mMulticastPortBox = null;
	private JTextField mCensusIntervalBox = null;
	private JButton mRestoreDefaultsButton = null;
	private JTextArea mMulticastAddressNotes = null;
	
	public NetworkSettingsPanel(JFrame parentFrame)
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
		this.setBorder(BorderFactory.createEmptyBorder(50, 5, 5, 5));
		
		try{
			this.mMulticastIPBox = new JTextField();
			this.mMulticastIPBox.setColumns(15);
			this.add(this.getRowPanel("Multicast IP address :", this.mMulticastIPBox, JTextField.CENTER));
			
			this.add(Box.createVerticalStrut(10));
			
			this.mMulticastPortBox = new JTextField();
			this.mMulticastPortBox.setColumns(5);
			this.add(this.getRowPanel("Multicast port :", this.mMulticastPortBox, JTextField.CENTER));
			
			this.add(Box.createVerticalStrut(10));
			
			this.add(Box.createVerticalStrut(10));
			
			this.mCensusIntervalBox = new JTextField();
			this.mCensusIntervalBox.setColumns(5);
			this.mCensusIntervalBox.setToolTipText("This interval defines how often the network is checked for participating users.");
			this.add(this.getRowPanel("Census interval (milliseconds) :", this.mCensusIntervalBox, JTextField.CENTER));
			
			this.add(this.createRestoreDefaultsButtonRow());
			
			this.add(this.createMulticastAddressNotesPanel());
			
			this.mMulticastAddressNotes.setText(Globals.MULTICAST_ADDRESS_NOTES);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return;
	}
	
	private JPanel getRowPanel(String labelText, JTextField textField, int fieldAlignment) throws Exception
	{
		JPanel aPanel = new JPanel();
		aPanel.setLayout(new BoxLayout(aPanel, BoxLayout.X_AXIS));
		aPanel.setPreferredSize(new Dimension(450, 25));
		aPanel.setMaximumSize(new Dimension(450, 25));
		
		try{
			JPanel bPanel = new JPanel();
			bPanel.setLayout(new BoxLayout(bPanel, BoxLayout.X_AXIS));
			bPanel.setPreferredSize(new Dimension(250, 25));
			bPanel.setMaximumSize(new Dimension(250, 25));
			JLabel aLabel = new JLabel(labelText);
			bPanel.add(aLabel);
			bPanel.add(Box.createVerticalStrut(16));
			aPanel.add(bPanel);
			
			bPanel = new JPanel();
			bPanel.setLayout(new BoxLayout(bPanel, BoxLayout.X_AXIS));
			bPanel.setPreferredSize(new Dimension(200, 25));
			bPanel.setMaximumSize(new Dimension(200, 25));
			textField.setHorizontalAlignment(fieldAlignment);
			bPanel.add(textField);
			bPanel.add(Box.createVerticalStrut(16));
			aPanel.add(bPanel);
		}catch(Exception ex){
			throw ex;
		}
		
		return aPanel;
	}
	
	private JPanel createRestoreDefaultsButtonRow()
	{
		JPanel aPanel = new JPanel();
		aPanel.setLayout(new BoxLayout(aPanel, BoxLayout.X_AXIS));
		aPanel.setPreferredSize(new Dimension(450, 75));
		aPanel.setMaximumSize(new Dimension(450, 75));
		aPanel.setBorder(BorderFactory.createEmptyBorder(50, 0, 0, 0));
		
		aPanel.add(Box.createVerticalStrut(16));
		
		this.mRestoreDefaultsButton = new JButton("Use defaults");
		this.mRestoreDefaultsButton.setToolTipText("Restore settings to factory defaults.");
		this.mRestoreDefaultsButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				requestRestoreDefaults();
				return;
			}
		});
		aPanel.add(this.mRestoreDefaultsButton);
		
		return aPanel;
	}
	
	private JPanel createMulticastAddressNotesPanel()
	{
		JPanel aPanel = new JPanel();
		aPanel.setLayout(new BoxLayout(aPanel, BoxLayout.X_AXIS));
		aPanel.setPreferredSize(new Dimension(450, 150));
		aPanel.setMaximumSize(new Dimension(450, 300));
		aPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 8, 0));
		
		this.mMulticastAddressNotes = new JTextArea();
		this.mMulticastAddressNotes.setEditable(false);
		this.mMulticastAddressNotes.setLineWrap(true);
		this.mMulticastAddressNotes.setWrapStyleWord(true);
		this.mMulticastAddressNotes.setOpaque(false);
		this.mMulticastAddressNotes.setMargin(new Insets(8, 8, 8, 8));
		aPanel.add(new JScrollPane(this.mMulticastAddressNotes,
					                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
					                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED));
		
		return aPanel;
	}
	
	/**
	 *  Queries the user to confirm overwrite before calling restoreDefaults().
	 */
	private void requestRestoreDefaults()
	{
		int response = JOptionPane.showOptionDialog(this.mParentFrame, 
													"Are you sure you want to overwrite the current settings with the originally installed values?", 
													"Confirm Restore", 
													JOptionPane.YES_NO_OPTION, 
													JOptionPane.WARNING_MESSAGE,
													null,
													null,
													null);
		if(response == JOptionPane.YES_OPTION){
			this.restoreDefaults();
		}
		return;
	}
	
	/**
	 * Sets form values to match those in Globals.
	 */
	private void restoreDefaults()
	{
		this.mMulticastIPBox.setText(Globals.UNCHOSEN_MULTICAST_IP);
		this.mMulticastPortBox.setText(String.valueOf(Globals.UNCHOSEN_MULTICAST_PORT));
		this.mCensusIntervalBox.setText(String.valueOf(Globals.UNCHOSEN_CENSUS_INTERVAL));
		return;
	}
	
	/**
	 * Attempts to set form values matching those in ConfigData, or defaulting to values in
	 * Globals if failed.
	 */
	private void retrieveInitialValues()
	{
		if(ConfigData.getMulticastIP() != null){
			this.mMulticastIPBox.setText(ConfigData.getMulticastIP());
		}else{
			this.mMulticastIPBox.setText(Globals.UNCHOSEN_MULTICAST_IP);
		}
		
		try{
			this.mMulticastPortBox.setText(String.valueOf(ConfigData.getSessionPort()));
		}catch(Exception ex){
			this.mMulticastPortBox.setText(String.valueOf(Globals.UNCHOSEN_MULTICAST_PORT));
			ex.printStackTrace();
		}
		
		try{
			this.mCensusIntervalBox.setText(String.valueOf(ConfigData.getCensusInterval()));
		}catch(Exception ex){
			this.mCensusIntervalBox.setText(String.valueOf(Globals.UNCHOSEN_CENSUS_INTERVAL));
			ex.printStackTrace();
		}
		return;
	}
	
	public String getMutlicastIP()
	{
		return this.mMulticastIPBox.getText();
	}
	
	public int getMulticastPort()
	{
		return Integer.parseInt(this.mMulticastPortBox.getText());
	}
	
	public int getCensusInterval()
	{
		return Integer.parseInt(this.mCensusIntervalBox.getText());
	}
	
	/**
	 * Checks mMulticastPortBox and mCensusIntervalBox for valid numbers.
	 * Their validity is based on arbitrary rules: (1) that ports can number between 0 and 2^16, and
	 * (2) that a census interval should be from 0 to 99.999 seconds.
	 * @return String containing any error descriptions if they occur.
	 */
	public String getFormErrors()
	{
		String errorText = null;
		
		if(!this.tryParsingNumericField(this.mMulticastPortBox)){
			errorText = "Multicast port must be a number.";
		}else{
			int x = Integer.parseInt(this.mMulticastPortBox.getText());
			if(x < 0 || x >= 65536){
				errorText = "Multicast port value is out of range: " + x;
			}
		}
		
		if(!this.tryParsingNumericField(this.mCensusIntervalBox)){
			errorText = "Census interval must be a number.";
		}else{
			int x = Integer.parseInt(this.mCensusIntervalBox.getText());
			if(x < 1 || x >= 99999){
				errorText = "Census interval value is out of range: " + x;
			}
		}
		
		return errorText;
	}
	
	/**
	 * Relies solely on Integer.parseInt() to determine if the input is a valid integer.
	 * @param textField a JTextField which will have its getText() method called.
	 * @return boolean true if Integer.parseInt() works without error, false otherwise.
	 */
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
