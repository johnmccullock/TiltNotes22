package main;

import javax.swing.JTabbedPane;
import javax.swing.JFrame;

/**
 * 
 * @author John McCuulock
 * @version 2.0 2013-10-19
 */
public class ConfigTabbedPanel extends JTabbedPane
{
	private GeneralSettingsPanel mGeneralSettingsPanel = null;
	private RecipientSettingsPanel mRecipientSettingsPanel = null;
	private DefaultNoteSettingsPanel mDefaultNoteSettingsPanel = null;
	private TemplateSettingsPanel mTemplateSettingsPanel = null;
	private NetworkSettingsPanel mNetworkSettingsPanel = null;
	private CensusPanel mCensusPanel = null;
	
	/**
	 * Creates a new ConfigTabbedPanel and sets basic layout.
	 * @param parentFrame a JFrame reference used by one or more classes hosted on this JTabbedPane.
	 */
	public ConfigTabbedPanel(final JFrame parentFrame)
	{
		super();
		this.setTabPlacement(JTabbedPane.TOP);
		this.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		
		this.initializePanels(parentFrame);
		this.initializeTabs();
		return;
	}
	
	/**
	 * Creates the panels for each tab. 
	 * @param parentFrame a JFrame reference used by one or more classes hosted on this JTabbedPane.
	 */
	public void initializePanels(final JFrame parentFrame)
	{
		this.mGeneralSettingsPanel = new GeneralSettingsPanel();
		this.mRecipientSettingsPanel = new RecipientSettingsPanel(parentFrame);
		this.mDefaultNoteSettingsPanel = new DefaultNoteSettingsPanel(parentFrame);
		this.mTemplateSettingsPanel = new TemplateSettingsPanel(parentFrame);
		this.mNetworkSettingsPanel = new NetworkSettingsPanel(parentFrame);
		this.mCensusPanel = new CensusPanel();
		return;
	}

	public void initializeTabs()
	{
		this.addTab(" General ", this.mGeneralSettingsPanel);
		this.addTab(" Recipients ", this.mRecipientSettingsPanel);
		this.addTab(" Default note settings", this.mDefaultNoteSettingsPanel);
		this.addTab(" Templates ", this.mTemplateSettingsPanel);
		this.addTab(" Network", this.mNetworkSettingsPanel);
		this.addTab(" Currently Active ", this.mCensusPanel);
		return;
	}
	
	public GeneralSettingsPanel getGeneralSettingsPanel()
	{
		return this.mGeneralSettingsPanel;
	}
	
	public RecipientSettingsPanel getRecipientSettingsPanel()
	{
		return this.mRecipientSettingsPanel;
	}
	
	public DefaultNoteSettingsPanel getDefaultNoteSettingsPanel()
	{
		return this.mDefaultNoteSettingsPanel;
	}
	
	public TemplateSettingsPanel getTemplateSettingsPanel()
	{
		return this.mTemplateSettingsPanel;
	}
	
	public NetworkSettingsPanel getNetworkSettingsPanel()
	{
		return this.mNetworkSettingsPanel;
	}
	
	public CensusPanel getCensusPanel()
	{
		return this.mCensusPanel;
	}
}
