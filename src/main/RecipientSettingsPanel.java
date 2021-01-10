package main;

import config.ConfigData;
import main.gui.text.DataItem;
import main.gui.text.ListTransferable;
import main.gui.text.ListTransferHandler;

import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.Insets;

import java.io.File;
import java.io.IOException;
import java.io.FileNotFoundException;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.DropMode;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener; 
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.TransferHandler;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.TreeMap;

/**
 * 
 * @author John McCullock
 * @version 2.0 2013-08-30
 * 
 * Because this version includes drag and drop functionality, the method of updating lists has changed.
 * Changes are now made to the component list models, but the master list, a TreeMap<String, ArrayList<String>>,
 * is only updated when (1) the ComboBox selection changes, or (2) when the window closes.  In previous
 * versions, the master list was modified first, then component list models were updated.
 */
public class RecipientSettingsPanel extends JPanel implements CensusGUIEventListener
{
	private JFrame mParentFrame = null;
	private JButton mNewListButton = null;
	private static final String NEW_LIST_BUTTON = "Create a new blank list.";
	private JButton mRemoveListButton = null;
	private static final String REMOVE_LIST_BUTTON = "Remove selected list.";
	private JButton mImportAllButton = null;
	private static final String IMPORT_All_BUTTON = "Import recipient database from file.";
	private JButton mExportAllButton = null;
	private static final String EXPORT_ALL_BUTTON = "Export recipient database to file.";
	private JComboBox<String> mListNames = null;
	private ItemListener mListNamesItemListener = null; // made this for easier avoidance of ConcurrentModificationExceptions.
	
	private JButton mBrowseCensusButton = null;
	private static final String CENSUS_BUTTON = "View users currently online.";
	private JButton mAddRecipientButton = null;
	private static final String ADD_BUTTON = "Add a new recipient.";
	private JButton mRemoveRecipientButton = null;
	private static final String REMOVE_BUTTON = "Remove selected recipients.";
	private JButton mIncludeRecipientButton = null;
	private static final String INCLUDE_BUTTON = "Include selected recipients.";
	private JButton mIncludeAllRecipientsButton = null;
	private static final String INCLUDE_ALL_BUTTON = "Include all recipients.";
	private JButton mExcludeRecipientButton = null;
	private static final String EXCLUDE_BUTTON = "Exclude selected recipients.";
	private JButton mExcludeAllRecipientsButton = null;
	private static final String EXCLUDE_ALL_BUTTON = "Exclude all recipients.";
	
	private JTextField mRecipientNameTextBox = null;
	
	private JList<String> mRecipientsList = null;
	private DefaultListModel<String> mRecipientsListModel = null;
	private JScrollPane mRecipientsListScrollPane = null;
	private JList<String> mSelectedList = null;
	private DefaultListModel<String> mSelectedListModel = null;
	private JScrollPane mSelectedListScrollPane = null;
	
	private CensusGUI mCensusGUI = null;
	
	private TreeMap<String, ArrayList<String>> mRecipients = null;
	
	public RecipientSettingsPanel(JFrame parentFrame)
	{
		super();
		this.mParentFrame = parentFrame;
		this.initializeMain();
		this.fillLocalRecipientLists();
		this.fillRecipientsList();
		this.fillSelectionListNames();
		this.fillSelectedList();
		
		// This listener is instantiated here in order to avoid timing issues.
		// this.fillSelectedList() must be called first (see above) or else it synchronizes an empty list.
		// The listener needs to be instantiated after everything else is finished loading to avoid
		// a ConcurrentModificationException.
		this.mListNames.addItemListener(this.mListNamesItemListener);
		return;
	}
	
	private void initializeMain()
	{
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		
		this.add(this.initializeRow1Panel());
		this.add(this.initializeRow2Panel());
		return;
	}
	
	private JPanel initializeRow1Panel()
	{
		JPanel aPanel = new JPanel();
		aPanel.setLayout(new BoxLayout(aPanel, BoxLayout.X_AXIS));
		
		aPanel.add(this.getRow1LeftPanel());
		aPanel.add(this.getRow1CenterPanel());
		aPanel.add(this.getRow1RightPanel());
		
		return aPanel;
	}
	
	private JPanel getRow1LeftPanel()
	{
		JPanel aPanel = new JPanel();
		aPanel.setLayout(new BoxLayout(aPanel, BoxLayout.Y_AXIS));
		aPanel.setMaximumSize(new Dimension(190, 300));
		aPanel.setPreferredSize(new Dimension(190, 300));
		
		JPanel bPanel = new JPanel();
		bPanel.setLayout(new BoxLayout(bPanel, BoxLayout.X_AXIS));
		bPanel.add(Box.createVerticalStrut(16));
		JLabel aLabel = new JLabel("Computer Name:");
		aLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 3, 0));
		bPanel.add(aLabel);
		bPanel.add(Box.createVerticalStrut(16));
		aPanel.add(bPanel);
		
		bPanel = new JPanel();
		bPanel.setLayout(new BoxLayout(bPanel, BoxLayout.X_AXIS));
		this.mRecipientNameTextBox = new JTextField();
		this.mRecipientNameTextBox.setPreferredSize(new Dimension(170, 20));
		this.mRecipientNameTextBox.setMaximumSize(new Dimension(170, 20));
		this.mRecipientNameTextBox.setMargin(new Insets(0, 10, 0, 10));
		bPanel.add(this.mRecipientNameTextBox);
		this.mBrowseCensusButton = new JButton("...");
		this.mBrowseCensusButton.setPreferredSize(new Dimension(20, 20));
		this.mBrowseCensusButton.setMaximumSize(new Dimension(20, 20));
		this.mBrowseCensusButton.setToolTipText(RecipientSettingsPanel.CENSUS_BUTTON);
		this.mBrowseCensusButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				shutdownCensusGUI();
				mCensusGUI = new CensusGUI();
				addCensusGUIListener();
				return;
			}
		});
		bPanel.add(this.mBrowseCensusButton);
		aPanel.add(bPanel);
		
		bPanel = new JPanel();
		bPanel.setLayout(new BoxLayout(bPanel, BoxLayout.X_AXIS));
		this.mAddRecipientButton = new JButton("Add");
		this.mAddRecipientButton.setPreferredSize(new Dimension(75, 25));
		this.mAddRecipientButton.setMaximumSize(new Dimension(75, 25));
		this.mAddRecipientButton.setToolTipText(RecipientSettingsPanel.ADD_BUTTON);
		this.mAddRecipientButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				addRecipientToJList(mRecipientsList, mRecipientNameTextBox.getText());
				mRecipientNameTextBox.setText("");
				return;
			}
		});
		bPanel.add(this.mAddRecipientButton);
		bPanel.add(Box.createVerticalStrut(16));
		this.mRemoveRecipientButton = new JButton("Remove");
		this.mRemoveRecipientButton.setPreferredSize(new Dimension(75, 25));
		this.mRemoveRecipientButton.setMaximumSize(new Dimension(75, 25));
		this.mRemoveRecipientButton.setToolTipText(RecipientSettingsPanel.REMOVE_BUTTON);
		this.mRemoveRecipientButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				removeRecipientFromNetworkList();
				return;
			}
		});
		bPanel.add(this.mRemoveRecipientButton);
		aPanel.add(bPanel);
		
		bPanel = new JPanel();
		bPanel.setLayout(new BoxLayout(bPanel, BoxLayout.X_AXIS));
		bPanel.add(Box.createVerticalStrut(16));
		aLabel = new JLabel("Network Neighborhood");
		aLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 3, 0));
		bPanel.add(aLabel);
		bPanel.add(Box.createVerticalStrut(16));
		aPanel.add(bPanel);
		
		this.mRecipientsListModel = new DefaultListModel<String>();
		this.mRecipientsListModel.addListDataListener(new ListDataListener()
		{
			public void contentsChanged(ListDataEvent e) { return; }
			public void intervalAdded(ListDataEvent e)
			{
				// If an item is added to mRecipientsList,
				// it's counterpart should automatically be added to the Everyone list.
				synchronizeRecipientListWithJList("Everyone", mRecipientsListModel);
				fillSelectedList();
				return;
			}
			
			public void intervalRemoved(ListDataEvent e)
			{
				// If an item is removed from mRecipientsList, 
				// its counterpart should be removed from mSelectedList too.
				mSelectedList.setVisible(false);
				for(int i = mSelectedListModel.getSize() - 1; i >= 0; i--)
				{
					boolean found = false;
					for(int j = 0; j < mRecipientsListModel.getSize(); j++)
					{
						if(mSelectedListModel.get(i).compareToIgnoreCase(mRecipientsListModel.getElementAt(j)) == 0){
							found = true;
							break;
						}
					}
					if(!found){
						mSelectedListModel.removeElementAt(i);
					}
				}
				mSelectedList.setVisible(true);
				return;
			}
		});
		
		this.mRecipientsList = new JList<String>(this.mRecipientsListModel);
		this.mRecipientsList.setVisibleRowCount(10);
		this.mRecipientsList.setPrototypeCellValue("List Item WWWWWW");
		this.mRecipientsList.setDragEnabled(true);
		this.mRecipientsList.setDropMode(DropMode.INSERT);
		this.mRecipientsList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		this.mRecipientsList.addListSelectionListener(new ListSelectionListener()
		{
			public void valueChanged(ListSelectionEvent e)
			{
				if(!e.getValueIsAdjusting()){
					StringBuilder text = new StringBuilder();
					for(int i = 0; i < mRecipientsList.getSelectedValuesList().size(); i++)
					{
						if(i == 0){
							text.append(mRecipientsList.getSelectedValuesList().get(i));
						}else{
							text.append(", " + mRecipientsList.getSelectedValuesList().get(i));
						}
					}
					mRecipientNameTextBox.setText(text.toString());
				}
				return;
			}
		});
		this.mRecipientsList.setTransferHandler(new ListTransferHandler(TransferHandler.COPY)
		{
			public boolean importData(TransferHandler.TransferSupport support)
			{
				boolean isSuccessful = true;
				try{
					if(!canImport(support)){
						throw new Exception("Cannot import");
					}
					
					DataItem dataItem = ((DataItem)support.getTransferable().getTransferData(ListTransferable.FLAVOR));
					
					JList list = (JList)support.getComponent();
					DefaultListModel model = (DefaultListModel)list.getModel();
					if(dataItem.getSource() != support.getComponent()){
						for(int i = 0; i < dataItem.getData().length; i++)
						{
							if(!this.isDuplicate(model, dataItem.getData()[i])){
								model.addElement(dataItem.getData()[i]);
							}
							if(dataItem.getSource() == (JList)mSelectedList){
								this.removeItemFromList(mSelectedList, dataItem.getData()[i]);
							}
						}
					}
				}catch(UnsupportedFlavorException e){
					isSuccessful = false;
				}catch(IOException e){
					isSuccessful = false;
				}catch(Exception ex){
					isSuccessful = false;
				}
				
				return isSuccessful;
			}
		});
		this.mRecipientsListScrollPane = new JScrollPane(this.mRecipientsList);
		aPanel.add(this.mRecipientsListScrollPane);
		
		aPanel.add(Box.createHorizontalStrut(16));
		
		return aPanel;
	}
	
	private JPanel getRow1CenterPanel()
	{
		JPanel aPanel = new JPanel();
		aPanel.setLayout(new BoxLayout(aPanel, BoxLayout.Y_AXIS));
		aPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
		
		this.mIncludeRecipientButton = new JButton("Add");
		this.mIncludeRecipientButton.setPreferredSize(new Dimension(90, 25));
		this.mIncludeRecipientButton.setMaximumSize(new Dimension(90, 25));
		this.mIncludeRecipientButton.setToolTipText(RecipientSettingsPanel.INCLUDE_BUTTON);
		this.mIncludeRecipientButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				includeRecipientsToSelectedList();
				return;
			}
		});
		aPanel.add(this.mIncludeRecipientButton);
		
		this.mIncludeAllRecipientsButton = new JButton("Add All");
		this.mIncludeAllRecipientsButton.setPreferredSize(new Dimension(90, 25));
		this.mIncludeAllRecipientsButton.setMaximumSize(new Dimension(90, 25));
		this.mIncludeAllRecipientsButton.setToolTipText(RecipientSettingsPanel.INCLUDE_ALL_BUTTON);
		this.mIncludeAllRecipientsButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				includeAllRecipientsToSelectedList();
				return;
			}
		});
		aPanel.add(this.mIncludeAllRecipientsButton);
		
		this.mExcludeRecipientButton = new JButton("Remove");
		this.mExcludeRecipientButton.setPreferredSize(new Dimension(90, 25));
		this.mExcludeRecipientButton.setMaximumSize(new Dimension(90, 25));
		this.mExcludeRecipientButton.setToolTipText(RecipientSettingsPanel.EXCLUDE_BUTTON);
		this.mExcludeRecipientButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				excludeRecipientsFromSelectedList();
				return;
			}
		});
		aPanel.add(this.mExcludeRecipientButton);
		
		this.mExcludeAllRecipientsButton = new JButton("Remove All");
		this.mExcludeAllRecipientsButton.setPreferredSize(new Dimension(90, 25));
		this.mExcludeAllRecipientsButton.setMaximumSize(new Dimension(90, 25));
		this.mExcludeAllRecipientsButton.setToolTipText(RecipientSettingsPanel.EXCLUDE_ALL_BUTTON);
		this.mExcludeAllRecipientsButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				excludedAllRecipientsFromSelectedList();
				return;
			}
		});
		aPanel.add(this.mExcludeAllRecipientsButton);
		
		return aPanel;
	}
	
	private JPanel getRow1RightPanel()
	{
		JPanel aPanel = new JPanel();
		aPanel.setLayout(new BoxLayout(aPanel, BoxLayout.Y_AXIS));
		aPanel.setMaximumSize(new Dimension(190, 300));
		aPanel.setPreferredSize(new Dimension(190, 300));
		aPanel.setBorder(BorderFactory.createEmptyBorder(25, 0, 0, 0));
		
		JPanel bPanel = new JPanel();
		bPanel.setLayout(new BoxLayout(bPanel, BoxLayout.X_AXIS));
		bPanel.add(Box.createVerticalStrut(16));
		JLabel aLabel = new JLabel("Select Group:");
		aLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 3, 0));
		bPanel.add(aLabel);
		bPanel.add(Box.createVerticalStrut(16));
		aPanel.add(bPanel);
		
		this.mListNames = new JComboBox<String>();
		this.mListNames.setPreferredSize(new Dimension(190, 20));
		this.mListNames.setMaximumSize(new Dimension(190, 20));
		this.mListNames.setEditable(false);
		this.mListNames.setMaximumRowCount(5);
		this.mListNamesItemListener = this.createListNamesItemListener();
		aPanel.add(this.mListNames);
		
		bPanel = new JPanel();
		bPanel.setLayout(new BoxLayout(bPanel, BoxLayout.X_AXIS));
		bPanel.add(Box.createVerticalStrut(16));
		aLabel = new JLabel("Note Recipients:");
		aLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 3, 0));
		bPanel.add(aLabel);
		bPanel.add(Box.createVerticalStrut(16));
		aPanel.add(bPanel);
		
		this.mSelectedListModel = new DefaultListModel<String>();
		this.mSelectedList = new JList<String>(this.mSelectedListModel);
		this.mSelectedList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		this.mSelectedList.setPrototypeCellValue("List Item WWWWWW");
		this.mSelectedList.setVisibleRowCount(10);
		this.mSelectedList.setDropMode(DropMode.INSERT);
		this.mSelectedList.setDragEnabled(true);
		this.mSelectedList.setTransferHandler(new ListTransferHandler(TransferHandler.COPY)
		{
			public boolean importData(TransferHandler.TransferSupport support)
			{
				boolean isSuccessful = true;
				try{
					if(!canImport(support)){
						throw new Exception("Cannot import");
					}
	
					DataItem dataItem = ((DataItem)support.getTransferable().getTransferData(ListTransferable.FLAVOR));
					
					JList list = (JList)support.getComponent();
					DefaultListModel model = (DefaultListModel)list.getModel();
					if(dataItem.getSource() != support.getComponent()){
						for(int i = 0; i < dataItem.getData().length; i++)
						{
							if(!this.isDuplicate(model, dataItem.getData()[i])){
								model.addElement(dataItem.getData()[i]);
							}
						}
					}
				}catch(UnsupportedFlavorException e){
					isSuccessful = false;
				}catch(IOException e){
					isSuccessful = false;
				}catch(Exception ex){
					isSuccessful = false;
				}

				return isSuccessful;
			}
		});
		this.mSelectedListScrollPane = new JScrollPane(this.mSelectedList);
		aPanel.add(this.mSelectedListScrollPane);
		
		aPanel.add(Box.createHorizontalStrut(16));
		
		return aPanel;
	}
	
	private ItemListener createListNamesItemListener()
	{
		return new ItemListener()
		{
			public void itemStateChanged(ItemEvent e)
			{
				synchronizeRecipientListWithJList((String)e.getItem(), mSelectedListModel);
				fillSelectedList();
				return;
			}
		};
	}
	
	private JPanel initializeRow2Panel()
	{
		JPanel aPanel = new JPanel();
		aPanel.setLayout(new BoxLayout(aPanel, BoxLayout.X_AXIS));
		
		this.mImportAllButton = new JButton("Import all...");
		this.mImportAllButton.setToolTipText(RecipientSettingsPanel.IMPORT_All_BUTTON);
		this.mImportAllButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				importListConfirm();
				return;
			}
		});
		aPanel.add(this.mImportAllButton);
		
		this.mExportAllButton = new JButton("Export all...");
		this.mExportAllButton.setToolTipText(RecipientSettingsPanel.EXPORT_ALL_BUTTON);
		this.mExportAllButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				exportRecipientsTreeMap();
				return;
			}
		});
		aPanel.add(this.mExportAllButton);
		
		aPanel.add(Box.createVerticalStrut(16));
		
		this.mNewListButton = new JButton("New Group...");
		this.mNewListButton.setToolTipText(RecipientSettingsPanel.NEW_LIST_BUTTON);
		this.mNewListButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				String name = promptUserForNewListName();
				if(name != null){
					mRecipients.put(name, new ArrayList<String>());
					mListNames.removeItemListener(mListNamesItemListener); // Avoiding a ConcurrentModificationException.
					fillSelectionListNames();
					mListNames.addItemListener(mListNamesItemListener);
					int index = getListIndexForStringValue(mListNames, name);
					mListNames.setSelectedIndex(index);
				}
				return;
			}
		});
		aPanel.add(this.mNewListButton);
		
		this.mRemoveListButton = new JButton("Remove Group");
		this.mRemoveListButton.setToolTipText(RecipientSettingsPanel.REMOVE_LIST_BUTTON);
		this.mRemoveListButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				removeSelectedList();
				return;
			}
		});
		aPanel.add(this.mRemoveListButton);
		
		return aPanel;
	}
	
	public TreeMap<String, ArrayList<String>> getRecipientsMap()
	{
		return this.mRecipients;
	}
	
	private void fillLocalRecipientLists()
	{
		this.mRecipients = new TreeMap<String, ArrayList<String>>();
		ArrayList<String> listNames = ConfigData.getRecipientLists();
		for(String listName : listNames)
		{
			ArrayList<String> recipientNames = ConfigData.getRecipients(listName);
			this.mRecipients.put(listName, recipientNames);
		}
		return;
	}
	
	private void fillRecipientsList()
	{
		this.mRecipientsList.setVisible(false);
		
		this.mRecipientsListModel.clear();
		for(String name : this.mRecipients.get("_network"))
		{
			this.mRecipientsListModel.addElement(name);
		}
		this.mRecipientsList.setModel(this.mRecipientsListModel);
		
		this.mRecipientsList.setVisible(true);
		return;
	}
	
	private void fillSelectionListNames()
	{
		this.mListNames.setVisible(false);
		
		this.mListNames.removeAllItems();
		for(String name : this.mRecipients.keySet())
		{
			if(name.compareToIgnoreCase("_network") != 0){
				this.mListNames.addItem(name);
			}
		}
		
		this.mListNames.setVisible(true);
		return;
	}
	
	private void fillSelectedList()
	{
		if(this.mSelectedList != null && this.mRecipients != null){
			this.mSelectedList.setVisible(false);
			this.mSelectedListModel.removeAllElements();
			if(this.mListNames.getSelectedItem() != null){
				for(String name : this.mRecipients.get((String)this.mListNames.getSelectedItem()))
				{
					this.mSelectedListModel.addElement(name);
				}
			}
			this.mSelectedList.setVisible(true);
		}
		return;
	}
	
	private void addRecipientToJList(JList<String> aList, String entry)
	{
		try{
			if(entry == null || entry.isEmpty()){
				throw new Exception("Cannot add recipient.  No name specified.");
			}
			if(this.isDuplicateListEntry(aList, entry)){
				throw new Exception("Duplicate entry.  This item already exists in the list.");
			}
			aList.setVisible(false);
			((DefaultListModel<String>)aList.getModel()).addElement(entry);
			aList.setVisible(true);
		}catch(Exception ex){
			//ex.printStackTrace();
			JOptionPane.showMessageDialog(this, ex.getMessage(), "List Entry Error", JOptionPane.WARNING_MESSAGE);
		}
		return;
	}
	
	// Overloaded function.
	// Does not check for empty string values.
	private boolean isDuplicateListEntry(JList<String> aList, String entry)
	{
		boolean isDuplicate = false;
		
		for(int i = 0; i < aList.getModel().getSize(); i++)
		{
			if(aList.getModel().getElementAt(i).compareToIgnoreCase(entry) == 0){
				isDuplicate = true;
				break;
			}
		}
		
		return isDuplicate;
	}
	
	// Overloaded function.
	// Does not check for empty string values.
	private boolean isDuplicateListEntry(JComboBox<String> aList, String entry)
	{
		boolean isDuplicate = false;
		
		for(int i = 0; i < aList.getModel().getSize(); i++)
		{
			if(aList.getModel().getElementAt(i).compareToIgnoreCase(entry) == 0){
				isDuplicate = true;
				break;
			}
		}
		
		return isDuplicate;
	}
	
	// Overloaded function.
	// Does not check for empty string values.
	private boolean isDuplicateListEntry(ArrayList<String> aList, String entry)
	{
		boolean isDuplicate = false;
		
		for(int i = 0; i < aList.size(); i++)
		{
			if(aList.get(i).compareToIgnoreCase(entry) == 0){
				isDuplicate = true;
				break;
			}
		}
		
		return isDuplicate;
	}
	
	private void removeRecipientFromNetworkList()
	{
		try{
			if(this.mRecipientsList.getSelectedIndices().length <= 0){
				throw new Exception("No items selected for removal.");
			}
			this.mRecipientsList.setVisible(false);
			int[] selected = this.mRecipientsList.getSelectedIndices();
			
			for(int i = selected.length - 1; i >= 0; i--)
			{
				this.mRecipientsListModel.removeElementAt(selected[i]);
			}
			
			this.mRecipientsList.setVisible(true);
		}catch(Exception ex){
			//ex.printStackTrace();
		}
		return;
	}
	
	private void includeRecipientsToSelectedList()
	{
		try{
			int[] recipientIndices = this.mRecipientsList.getSelectedIndices();
			if(recipientIndices.length <= 0){
				throw new Exception("No list items selected.");
			}
			this.mSelectedList.setVisible(false);
			for(int index : recipientIndices)
			{
				if(!this.isDuplicateListEntry(this.mSelectedList, this.mRecipientsListModel.get(index))){
					this.mSelectedListModel.addElement(this.mRecipientsListModel.get(index));
				}
			}
			this.mSelectedList.setVisible(true);
		}catch(Exception ex){
			System.out.println(ex.getMessage());
		}
		return;
	}
	
	private void includeAllRecipientsToSelectedList()
	{
		this.mSelectedList.setVisible(false);
		
		for(String name : this.mRecipients.get("_network"))
		{
			if(!this.isDuplicateListEntry(this.mSelectedList, name)){
				this.mSelectedListModel.addElement(name);
			}
		}
		
		this.mSelectedList.setVisible(true);
		return;
	}
	
	private void excludeRecipientsFromSelectedList()
	{
		try{
			int[] selected = this.mSelectedList.getSelectedIndices();
			if(selected.length <= 0){
				throw new Exception("No list items selected.");
			}
			this.mSelectedList.setVisible(false);
			
			for(int i = selected.length - 1; i >= 0; i--)
			{
				this.mSelectedListModel.removeElementAt(selected[i]);
			}
			
			this.mSelectedList.setVisible(true);
		}catch(Exception ex){
			//ex.printStackTrace();
		}
		return;
	}
	
	private void excludedAllRecipientsFromSelectedList()
	{
		this.mSelectedList.setVisible(false);
		this.mSelectedListModel.removeAllElements();
		this.mSelectedList.setVisible(true);
		return;
	}
	
	private int getListIndexForStringValue(JComboBox<String> aList, String value)
	{
		int index = -1;
		for(int i = 0; i < aList.getModel().getSize(); i++)
		{
			if(aList.getModel().getElementAt(i).compareToIgnoreCase(value) == 0){
				index = i;
				break;
			}
		}
		return index;
	}
	
	private String promptUserForNewListName()
	{
		String name = null;
		boolean done = false;
		
		while(!done)
		{
			name = JOptionPane.showInputDialog(this, "Please enter name for new list :", "New List", JOptionPane.OK_CANCEL_OPTION);
			try{
				if(name == null || name.isEmpty()){
					throw new NullPointerException("cancelled");
				}
				if(name.compareToIgnoreCase("_network") == 0){
					throw new InvalidParameterException("This name is already reserved for program use.  Please choose another.");
				}
				if(this.isDuplicateListEntry(this.mListNames, name)){
					throw new Exception("This name already exists in the list.  Please choose another.");
				}
				// Accepted.
				done = true;
			}catch(InvalidParameterException ipe){
				// Cannot use reserved name: "_network"
				JOptionPane.showMessageDialog(this, ipe.getMessage(), "Error", JOptionPane.WARNING_MESSAGE);
			}catch(NullPointerException npe){
				// Cancelled by user.
				done = true;
			}catch(Exception ex){
				// Duplicate, try again.
				JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.WARNING_MESSAGE);
			}
		}
		
		return name;
	}
	
	private void removeSelectedList()
	{
		try{
			int selected = this.mListNames.getSelectedIndex();
			String value = this.mListNames.getModel().getElementAt(selected);
			if(value.compareToIgnoreCase("Everyone") == 0){
				JOptionPane.showMessageDialog(this.mParentFrame, "Cannot remove \"Everyone\" list.", "Removal Error", JOptionPane.ERROR_MESSAGE);
				throw new Exception("Cannot remove \"Everyone\" list.");
			}
			if(selected > -1){
				int n = JOptionPane.showConfirmDialog(
						this,
						"Are you sure you want to delete the \"" + value + "\" list?",
						"Confirm Deletion",
						JOptionPane.YES_NO_OPTION);
				if(n == JOptionPane.YES_OPTION){
					this.mRecipients.remove(value);
				}
				mListNames.removeItemListener(mListNamesItemListener); // Avoiding a ConcurrentModificationException.
				this.fillSelectionListNames();
				mListNames.addItemListener(mListNamesItemListener);
				fillSelectedList(); // done manually to avoid a ConcurrentModificationException.
			}
		}catch(Exception ex){
			System.out.println(ex.getMessage());
		}
		return;
	}
	
	private void synchronizeRecipientListWithJList(String listName, DefaultListModel<String> model)
	{
		ArrayList<String> items = new ArrayList<String>();
		for(int i = 0; i < model.getSize(); i++)
		{
			items.add(model.get(i));
		}
		this.mRecipients.remove(listName);
		this.mRecipients.put(listName, items);
		return;
	}
	
	// This ugly beast traverses through the TreeMap<String, ArrayList<String>> mRecipients,
	// ensuring that any remnants of a deleted item from "_network" ArrayList get deleted from
	// the other ArrayLists.
	private void cleanUpAfterRecipientRemoval()
	{
		for(String key : this.mRecipients.keySet())
		{
			if(key.compareToIgnoreCase("_network") == 0) { continue; }
			
			for(int i = this.mRecipients.get(key).size() - 1; i >= 0; i--)
			{
				boolean found = false;
				for(int j = 0; j < this.mRecipients.get("_network").size(); j++)
				{
					if(this.mRecipients.get(key).get(i).compareToIgnoreCase(this.mRecipients.get("_network").get(j)) == 0){
						found = true;
						break;
					}
				}
				if(!found){
					this.mRecipients.get(key).remove(i);
				}
			}
		}
		return;
	}
	
	public void synchronizeLists()
	{
		// rewrite mRecipients->_network with mRecipientsListModel contents
		this.synchronizeRecipientListWithJList("_network", this.mRecipientsListModel);
		
		// rewrite mRecipients->(mListNames->selected) with mSelectedListModel contents
		if(this.mListNames.getSelectedIndex() > -1){
			this.synchronizeRecipientListWithJList(String.valueOf(this.mListNames.getSelectedItem()), this.mSelectedListModel);
		}
		
		// check all mRecipients-> lists, to make sure that any items removed from _network get removed from
		// those lists too.
		this.cleanUpAfterRecipientRemoval();
		return;
	}
	
	private void importListConfirm()
	{
		int response = JOptionPane.showOptionDialog(this.mParentFrame, 
													"Are you sure you want to import a new recipient list?\n\nThis would overwrite your current list.", 
													"Confirm Import", 
													JOptionPane.YES_NO_OPTION, 
													JOptionPane.QUESTION_MESSAGE,
													null,
													null,
													null);
		if(response == JOptionPane.YES_OPTION){
			this.importRecipientsTreeMap();
		}
		return;
	}
	
	private void importRecipientsTreeMap()
	{
		try{
			String rawText = RecipientsFileFactory.openRecipientsDatabaseFile(this.mParentFrame);
			if(rawText == null){
				throw new Exception("Unable to open recipient database from this file.");
			}
			this.mRecipients = XMLTransact.retrieveRecipientTreeMapFromXML(rawText);
			this.fillRecipientsList();
			this.fillSelectionListNames();
			this.mListNames.setSelectedIndex(0);
		}catch(FileNotFoundException fnfe){
			JOptionPane.showMessageDialog(this, fnfe.getMessage(), "File Not Found", JOptionPane.WARNING_MESSAGE);
		}catch(IOException ioe){
			JOptionPane.showMessageDialog(this, ioe.getMessage(), "Open File Error", JOptionPane.WARNING_MESSAGE);
		}catch(Exception ex){
			JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.WARNING_MESSAGE);
		}
		return;
	}
	
	public void exportRecipientsTreeMap()
	{
		try{
			File filePath = RecipientsFileFactory.getFilenameForRecipientsDatabase(this.mParentFrame);
			if(filePath != null){
				String xml = XMLTransact.parseRecipientTreeMapToXML(this.mRecipients);
				RecipientsFileFactory.writeRecipientsTreeMapToFile(filePath, xml);
			}
		}catch(Exception ex){
			JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.WARNING_MESSAGE);
		}
		return;
	}
	
	public void addUserToCensusTable(User newUser)
	{
		if(this.mCensusGUI != null){
			this.mCensusGUI.addUser(newUser);
		}
		return;
	}
	
	public void shutdownCensusGUI()
	{
		if(this.mCensusGUI != null){
			CensusGUIListener.removeListener(this);
			this.mCensusGUI.disposeWindow();
			this.mCensusGUI = null;
		}
		return;
	}
	
	private void addCensusGUIListener()
	{
		CensusGUIListener.addListener(this);
		return;
	}
	
	public void censusGUISendToNetworkNeighborhoodListPerformed(CensusGUIEvent e)
	{
		for(String item : e.getData())
		{
			this.addRecipientToJList(this.mRecipientsList, item);
		}
		return;
	}
	
	public void censusGUISendToRecipientListPerformed(CensusGUIEvent e)
	{
		for(String item : e.getData())
		{
			this.addRecipientToJList(this.mSelectedList, item);
		}
		return;
	}
	
	public void censusGUIClosePerformed(CensusGUIEvent e)
	{
		CensusGUIListener.removeListener(this);
		return;
	}
	
}
