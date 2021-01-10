package main;

import main.gui.text.DataItem;
import main.gui.text.ListTransferable;
import main.gui.text.ListTransferHandler;
import util.ImageLoader;
import util.Ticker;
import util.TickEvent;
import util.TickEventListener;
import util.TickListener;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Image;
import java.awt.Insets;
import java.awt.SystemColor;

import java.io.IOException;

import javax.activation.ActivationDataFlavor;
import javax.activation.DataHandler;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.DefaultListModel;
import javax.swing.DropMode;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.TransferHandler;
import javax.swing.UIManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

/**
 * Displays a list of participating users on the network.
 * 
 * Includes both menu-driven and drag-and-drop functionality, so the user can add the names on the
 * list to corresponding lists/fields on ConfigurationGUI.RecipientSettingsPanel.
 * @author John McCullock
 * @version 1.0 2013-10-22, but a new feature in program version 2.0 in general.
 */

public class CensusGUI extends JDialog implements TickEventListener, 
													WindowListener
{
	private static final String INITIAL_MESSAGE = "<html><p>Please Wait.  Collecting Data.</p></html>";
	private static final String NORMAL_MESSAGE = "<html><p>This is a list of other Tilt Notes users currently online.<br />You can drag and drop names from this list into the Network Neighborhood list or the Note Recipients list.</p></html>";
	
	private JPanel mBasePanel = null;
	private JLabel mDescriptionLabel = null;
	private static final String CLOSE_ACTION = "Close";
	private JList<String> mCensusList = null;
	private JPopupMenu mPopupMenu = null;
	private static final String SEND_TO_NETWORK_NEIGHBORHOOD_LIST = "send to Network Neighborhood list";
	private static final String SEND_TO_RECIPIENT_LIST = "send to Note Recipient list";
	private static final String SORT_ASCENDING = "Sort ascending";
	private static final String SORT_DESCENDING = "Sort descending";
	private static final String REQUEST_REFRESH = "Refresh";
	
	private ArrayList<User> mUsers = null;
	private Ticker mTicker = null;
	private Timer mTimer = null;
	private tTask mTask = null;
	
	public CensusGUI()
	{
		super();
		this.initializeMain();
		this.mUsers = new ArrayList<User>();
		this.reinitializeDataCollection();
		return;
	}
	
	private void initializeMain()
	{
		this.setTitle("Who's Currently Online");
		this.setResizable(true);
		try{
			Image anImage = ImageLoader.getImageFromResourcePath(this.getClass().getResource(Globals.APPLICATION_ICON_32x32));
			this.setIconImage(anImage);	
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		this.mBasePanel = new JPanel();
		this.mBasePanel.setLayout(new BoxLayout(this.mBasePanel, BoxLayout.Y_AXIS));
		this.mBasePanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		this.add(this.mBasePanel);
		
		this.mBasePanel.add(this.createDescriptionPanel());
		this.mBasePanel.add(this.createListPanel());
		
		KeyStroke ksEscape = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
		this.rootPane.registerKeyboardAction(this.getCancelActionListener(), 
												CensusGUI.CLOSE_ACTION, 
												ksEscape, 
												JComponent.WHEN_IN_FOCUSED_WINDOW);
		this.addWindowListener(this);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setMinimumSize(Globals.CENSUS_GUI_MINIMUM_SIZE);
		this.setPreferredSize(Globals.CENSUS_GUI_MINIMUM_SIZE);
		this.getContentPane().add(this.mBasePanel);
		this.setLocationRelativeTo(null); // set center screen.
		this.setVisible(true);
		this.pack();
		return;
	}
	
	/**
	 * Attempts to collect user names for a period of time (Globals.CENSUS_GUI_DATA_COLLECTION_TIME_LIMIT),
	 * after which, the private class tTask calls finishDataCollection().
	 */
	private void reinitializeDataCollection()
	{
		this.mCensusList.setEnabled(false);
		this.mCensusList.setBackground(SystemColor.control);
		this.rootPane.setCursor(new Cursor(Cursor.WAIT_CURSOR));
		this.mDescriptionLabel.setText(CensusGUI.INITIAL_MESSAGE);
		
		TickListener.addListener(this);
		this.mTicker = new Ticker(Globals.CENSUS_TABLE_REFRESH_INTERVAL); // in milliseconds.
		this.mTicker.startTimer();
		
		this.mTask = new tTask();
		this.mTimer = new Timer();
		this.mTimer.schedule(this.mTask, Globals.CENSUS_GUI_DATA_COLLECTION_TIME_LIMIT);
		return;
	}
	
	/**
	 * Called from private class tTask to stop timers and allow user access to census list items.
	 */
	private void finishDataCollection()
	{
		TickListener.removeListener(this);
		this.mTicker.stopTimer();
		this.mTicker = null;
		this.mTimer.cancel();
		this.mTimer.purge();
		this.mTimer = null;
		this.mDescriptionLabel.setText(CensusGUI.NORMAL_MESSAGE);
		this.mCensusList.setBackground(SystemColor.window);
		this.mCensusList.setEnabled(true);
		this.rootPane.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		return;
	}
	
	private JPanel createDescriptionPanel()
	{
		JPanel aPanel = new JPanel();
		aPanel.setLayout(new BoxLayout(aPanel, BoxLayout.X_AXIS));
		aPanel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
		
		this.mDescriptionLabel = new JLabel();
		this.mDescriptionLabel.setMinimumSize(new Dimension(150, 30));
		this.mDescriptionLabel.setMaximumSize(new Dimension(1000, 200));
		this.mDescriptionLabel.setText(CensusGUI.INITIAL_MESSAGE);
		aPanel.add(this.mDescriptionLabel);
		
		return aPanel;
	}
	
	private void createPopupMenu()
	{
		this.mPopupMenu = new JPopupMenu();
		
		JMenuItem menuItem = new JMenuItem(CensusGUI.SEND_TO_NETWORK_NEIGHBORHOOD_LIST);
		menuItem.setActionCommand(CensusGUI.SEND_TO_NETWORK_NEIGHBORHOOD_LIST);
		menuItem.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				try{
					CensusGUIListener.notifySendToNetworkNeighborHoodList(this, getSelectedValuesFromList());
				}catch(IllegalMonitorStateException imse){
					imse.printStackTrace();
				}catch(Exception ex){
					ex.printStackTrace();
				}
				return;
			}
		});
		this.mPopupMenu.add(menuItem);
		
		menuItem = new JMenuItem(CensusGUI.SEND_TO_RECIPIENT_LIST);
		menuItem.setActionCommand(CensusGUI.SEND_TO_RECIPIENT_LIST);
		menuItem.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				try{
					CensusGUIListener.notifySendToRecipientList(this, getSelectedValuesFromList());
				}catch(IllegalMonitorStateException imse){
					imse.printStackTrace();
				}catch(Exception ex){
					ex.printStackTrace();
				}
				return;
			}
		});
		this.mPopupMenu.add(menuItem);
		
		this.mPopupMenu.addSeparator();
		
		menuItem = new JMenuItem(CensusGUI.SORT_ASCENDING);
		menuItem.setActionCommand(CensusGUI.SORT_ASCENDING);
		menuItem.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				sortList(true);
				return;
			}
		});
		this.mPopupMenu.add(menuItem);
		
		menuItem = new JMenuItem(CensusGUI.SORT_DESCENDING);
		menuItem.setActionCommand(CensusGUI.SORT_DESCENDING);
		menuItem.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				sortList(false);
				return;
			}
		});
		this.mPopupMenu.add(menuItem);
		
		this.mPopupMenu.addSeparator();
		
		menuItem = new JMenuItem(CensusGUI.REQUEST_REFRESH);
		menuItem.setActionCommand(CensusGUI.REQUEST_REFRESH);
		menuItem.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				reinitializeDataCollection();
				return;
			}
		});
		this.mPopupMenu.add(menuItem);
		return;
	}
	
	private JPanel createListPanel()
	{
		JPanel aPanel = new JPanel();
		aPanel.setLayout(new BoxLayout(aPanel, BoxLayout.X_AXIS));
		
		this.mCensusList = new JList<String>();
		this.mCensusList.addMouseListener(new MouseListener()
		{
			public void mouseClicked(MouseEvent e)			{ return; }
			public void mouseEntered(MouseEvent e)			{ return; }
			public void mouseExited(MouseEvent e)			{ return; }
			public void mousePressed(MouseEvent e)			{ return; }
			
			public void mouseReleased(MouseEvent e)
			{
				if(e.isPopupTrigger()){
					if(mCensusList.getMousePosition() != null){
						int row = mCensusList.locationToIndex(e.getPoint());
						System.out.println(row);
						if(!mCensusList.isSelectedIndex(row)){
							mCensusList.setSelectedIndex(row);
						}
						mPopupMenu.show(e.getComponent(), e.getX(), e.getY());
					}
				}
				return;
			}
		});
		this.createPopupMenu();
		this.mCensusList.setPrototypeCellValue("List Item WWWWWW");
		this.mCensusList.setVisibleRowCount(10);
		this.mCensusList.setDropMode(DropMode.INSERT);
		this.mCensusList.setDragEnabled(true);
		this.mCensusList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		this.mCensusList.setTransferHandler(new ListTransferHandler(TransferHandler.COPY)
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
							this.removeItemFromList((JList)dataItem.getSource(), dataItem.getData()[i]);
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
		aPanel.add(new JScrollPane(this.mCensusList,
									JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
									JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED));
		
		return aPanel;
	}
	
	private ActionListener getCancelActionListener()
	{
		return new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				disposeWindow();
				return;
			}
		};
	}
	
	/**
	 * Adds a row to the table if a duplicate value is not already present.
	 * @param thatUser User object.
	 * @see User.
	 */
	public void addUser(User thatUser)
	{
		// Avoid duplicates.
		boolean found = false;
		for(User thisUser : this.mUsers)
		{
			if(thisUser.userName.compareToIgnoreCase(thatUser.userName) == 0){
				found = true;
			}
		}
		if(!found){
			this.mUsers.add(thatUser);
		}
		return;
	}
	
	private void fillTable(ArrayList<String> users)
	{
		this.mCensusList.setVisible(false); // setVisible seems to be the only sure way to get table data to refresh.
		
		this.mCensusList.removeAll();
		Collections.sort(users);
		DefaultListModel<String> model = new DefaultListModel<String>();
		for(int i = 0; i < users.size(); i++)
		{
			model.addElement(users.get(i));
		}
		this.mCensusList.setModel(model);
		
		this.mCensusList.setVisible(true); // setVisible seems to be the only sure way to get table data to refresh.
		
		return;
	}
	
	private String[] getSelectedValuesFromList()
	{
		String[] data = new String[this.mCensusList.getSelectedIndices().length];
		for(int i = 0; i < this.mCensusList.getSelectedIndices().length; i++)
		{
			data[i] = this.mCensusList.getModel().getElementAt(i);
		}
		return data;
	}
	
	private void sortList(boolean doAscending)
	{
		try{
			ArrayList<String> list = new ArrayList<String>();
			for(int i = 0; i < this.mCensusList.getModel().getSize(); i++)
			{
				list.add(this.mCensusList.getModel().getElementAt(i));
			}
			Collections.sort(list);
			if(!doAscending){
				Collections.reverse(list);
			}
			DefaultListModel<String> model = new DefaultListModel<String>();
			for(String item : list)
			{
				model.addElement(item);
			}
			this.mCensusList.setModel(model);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return;
	}
	
	public void tickPerformed(TickEvent te)
	{
		if(te.getSource() == (Ticker)this.mTicker){
			ArrayList<String> rows = new ArrayList<String>();
			for(User aUser : this.mUsers)
			{
				rows.add(aUser.userName);
			}
	
			this.fillTable(rows);
		}
		return;
	}
	
	public void windowActivated(WindowEvent e)		{ return; }
	public void windowClosed(WindowEvent e)			{ return; }
	public void windowDeactivated(WindowEvent e)	{ return; }
	public void windowDeiconified(WindowEvent e)	{ return; }
	public void windowIconified(WindowEvent e)		{ return; }
	public void windowOpened(WindowEvent e)			{ return; }
	
	public void windowClosing(WindowEvent e)
	{
		this.disposeWindow();
		return;
	}
	
	public void disposeWindow()
	{
		TickListener.removeListener(this);
		if(this.mTicker != null){
			this.mTicker.stopTimer();
			this.mTicker = null;
		}
		if(this.mTimer != null){
			this.mTimer.cancel();
			this.mTimer.purge();
			this.mTimer = null;
		}
		try{
			CensusGUIListener.notifyCensusGUIClose(this);
		}catch(IllegalMonitorStateException imse){
			imse.printStackTrace();
		}catch(Exception ex){
			ex.printStackTrace();
		}
		this.dispose();
		return;
	}
	
	private class tTask extends TimerTask
	{
		public void run()
		{
			finishDataCollection();
			return;
		}
	}
}
