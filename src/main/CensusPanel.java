package main;

import util.Ticker;
import util.TickEvent;
import util.TickEventListener;
import util.TickListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import java.util.Vector;

/**
 * Panel for displaying all other users currently online.
 * 
 * MainApp sends and receives census messages on all participating network machines (i.e.; Users).
 * Eventually the User signal is passed on to this class where its data is displayed
 * on a table describing all the currently participating Users.
 * 
 * If a signal hasn't been received for a given User for a specified amount of time, the User is dropped from 
 * the table.
 * 
 * This new version no longer uses the CensusClient class as it did in version 1.0.  Both SessionClient and 
 * CensusClient have been replaced by CommClient.
 * 
 * @author John McCullock
 * @version 2.0 08/19/2013
 */
public class CensusPanel extends JPanel implements TickEventListener
{
	private JTable mCensusTable = null;
	private CensusTableModel mCensusTableModel = null;
	
	private Vector<User> mUsers = null;
	private Ticker mTicker = null;
	
	public CensusPanel()
	{
		super();
		this.initializeMain();
		this.mUsers = new Vector<User>();
		TickListener.addListener(this);
		this.mTicker = new Ticker(Globals.CENSUS_TABLE_REFRESH_INTERVAL); // in milliseconds.
		this.mTicker.startTimer();
		return;
	}
	
	private void initializeMain()
	{
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.setBorder(BorderFactory.createEmptyBorder(20, 8, 20, 8));
		
		this.initializeTable();
		return;
	}
	
	private void initializeTable()
	{
		this.mCensusTable = new JTable();
		this.mCensusTable.setFont(Globals.CENSUS_TABLE_FONT);
		this.mCensusTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.mCensusTable.setRowHeight(Globals.CENSUS_TABLE_ROW_HEIGHT);
		
		this.add(new JScrollPane(this.mCensusTable,
					                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
					                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED));
		return;
	}
	
	/**
	 * Adds a row to the table if a duplicate value is not already present.
	 * @param thatUser User object.
	 * @see User.
	 */
	public void addUser(User thatUser)
	{
		// Avoid for duplicates.
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
	
	private void fillTable(String[] columns, Vector<Vector<Object>> users)
	{
		this.mCensusTable.setVisible(false); // setVisible seems to be the only sure way to get table data to refresh.
		
		this.mCensusTableModel = new CensusTableModel();
		for(int i = 0; i < columns.length; i++)
		{
			this.mCensusTableModel.setColumn(columns[i]);
		}
		this.mCensusTableModel.setUsersVector(users);
		this.mCensusTable.setModel(this.mCensusTableModel);
		
		this.mCensusTable.setVisible(true); // setVisible seems to be the only sure way to get table data to refresh.
		
		return;
	}
	
	public void selectCell(int row, int col)
	{
		if(row != -1 && col != -1){
			this.mCensusTable.setRowSelectionInterval(row, row);
			this.mCensusTable.setColumnSelectionInterval(col, col);
		}
		return;
	}
	
	public void revalidateTable()
	{
		this.mCensusTable.setVisible(false); // setVisible seems to be the only sure way to get table data to refresh.
		this.mCensusTable.setVisible(true);
		return;
	}
	
	/**
	 * Removes User objects representing network users whose acknowledgment signals haven't been 
	 * received after a set period of time.
	 */
	private void removeOldUsers()
	{
		boolean done = true;
		while(!done)
		{
			if(!this.mUsers.isEmpty()){
				for(int i = 0; i < this.mUsers.size(); i++)
				{
					if(this.mUsers.get(i).age > Globals.CENSUS_TABLE_MAX_AGE){
						this.mUsers.remove(i);
						break;
					}
					if(i >= this.mUsers.size() - 1){
						done = true;
					}
				}
			}else{
				done = true;
			}
		}
		return;
	}
	
	public void shutdownTicker()
	{
		TickListener.removeListener(this);
		this.mTicker.stopTimer();
		return;
	}
	
	public void tickPerformed(TickEvent te)
	{
		this.removeOldUsers();
		
		Vector<Vector<Object>> rows = new Vector<Vector<Object>>();
		for(User aUser : this.mUsers)
		{
			Vector<Object> row1 = new Vector<Object>();
			row1.add(aUser.userName);
			row1.add(aUser.userIP);
			rows.add(row1);
			aUser.age++; // update the User's age value
		}
		
		String[] columns = new String[]{"Name", "IP"};
		this.fillTable(columns, rows);
		
		return;
	}
}
