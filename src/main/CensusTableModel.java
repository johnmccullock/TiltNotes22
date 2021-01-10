package main;

import java.util.Vector;
import javax.swing.table.AbstractTableModel;

/**
 * The "editable" property is false.  This table is for display only.
 * @author John McCullock
 * @version 2.0 2013-10-22
 */
public class CensusTableModel extends AbstractTableModel
{
	private Vector<String> mColumnNames = null;
	private Vector<Vector<Object>> mUsers = null;
	
	public CensusTableModel()
	{
		this.mColumnNames = new Vector<String>();
		this.mUsers = new Vector<Vector<Object>>();
		return;
	}
	
	public void setUsersVector(Vector<Vector<Object>> users)
	{
		this.mUsers = users;
		return;
	}
	
	public void addUserToIndex(Vector<Object> aUser)
	{
		this.mUsers.add(aUser);
		return;
	}
	
	public void clearData()
	{
		this.mUsers.clear();
		return;
	}
	
	public int getRowCount()
	{
		return this.mUsers.size();
	}
	
	public int getColumnCount()
	{
		return this.mColumnNames.size();
	}
	
	public boolean isCellEditable(int rowIndex, int columnIndex)
	{
		return false;
	}
	
	public void setColumn(String name)
	{
		this.mColumnNames.add(name);
		return;
	}
	
	public String getColumnName(int columnIndex)
	{
		String name = null;
		try{
			if(columnIndex < 0 || columnIndex > this.mColumnNames.size() - 1){
				throw new Exception("Parameter out of range: " + columnIndex);
			}
			name = this.mColumnNames.get(columnIndex);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return name;
	}
	
	public Object getValueAt(int rowIndex, int columnIndex)
	{
		Object retObject = null;
		try{
			if(this.mUsers.elementAt(rowIndex) != null){
				Vector<Object> row = (Vector<Object>)this.mUsers.elementAt(rowIndex);
				retObject =  row.get(columnIndex);
			}else{
				retObject = new String(""); // just makes a blank table cell.
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		return retObject;
	}
	
	public void setValueAt(Object aValue, int rowIndex,	int columnIndex)
	{
		Vector<Object> row = (Vector<Object>)this.mUsers.elementAt(rowIndex);
		try{
			row.set(columnIndex, (String)aValue);
			fireTableCellUpdated(rowIndex, columnIndex);
		}catch(ClassCastException cce){
			cce.printStackTrace();
		}
	}
}
