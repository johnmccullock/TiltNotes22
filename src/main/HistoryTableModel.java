package main;

import util.DateFormatTool;

import javax.swing.table.AbstractTableModel;

import java.util.Date;
import java.util.UUID;
import java.util.Vector;

import org.apache.commons.codec.binary.Base64;

/**
 * AbstractTableModel extended for use with the History GUI's table.
 * @author John McCullock
 * @version 1.0 2013-09-16
 */
public class HistoryTableModel extends AbstractTableModel
{
	private static final int DELETED_AT = 0;
	private static final int SOURCE = 1;
	private static final int MESSAGE = 2;
	private static final int CREATED = 3;
	private static final int HIDDEN_AT = 4;
	private static final int HIDDEN_EVENT = 5;
	private static final int DELETION_COMMAND = 6;
	private static final int DELETION_SOURCE = 7;
	private static final int SESSION_MODE = 8;
	private static final String[] COLUMN_NAMES = new String[]{"Deleted At", "Source", "Message", "Created", "Hidden At", "Hidden Event", "Deletion Command", "Deletion Source", "Mode"};
	
	private Vector<SessionData> mRows = null;
	
	public HistoryTableModel()
	{
		this.mRows = new Vector<SessionData>();
		return;
	}
	
	public void setRowsVector(Vector<SessionData> rows)
	{
		this.mRows = rows;
		return;
	}
	
	public void addRowToIndex(SessionData aRow)
	{
		this.mRows.add(aRow);
		return;
	}
	
	public void clearData()
	{
		this.mRows.clear();
		return;
	}
	
	public int getRowCount()
	{
		return this.mRows.size();
	}
	
	public int getColumnCount()
	{
		return HistoryTableModel.COLUMN_NAMES.length;
	}
	
	public boolean isCellEditable(int rowIndex, int columnIndex)
	{
		return false;
	}
	
	public String getColumnName(int columnIndex)
	{
		String name = null;
		try{
			if(columnIndex < 0 || columnIndex > HistoryTableModel.COLUMN_NAMES.length - 1){
				throw new Exception("Parameter out of range: " + columnIndex);
			}
			name = HistoryTableModel.COLUMN_NAMES[columnIndex];
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return name;
	}
	
	public Object getValueAt(int rowIndex, int columnIndex)
	{
		Object retObject = null;
		try{
			if(this.mRows.elementAt(rowIndex) != null){
				if(columnIndex == HistoryTableModel.DELETED_AT){
					retObject = DateFormatTool.getSimple12HourTime(new Date(this.mRows.get(rowIndex).getDeletionTime()));
				}else if(columnIndex == HistoryTableModel.SOURCE){
					if(this.mRows.get(rowIndex).getEntries().size() > 0){
						retObject = this.mRows.get(rowIndex).getEntries().get(0).source;
					}
				}else if(columnIndex == HistoryTableModel.MESSAGE){
					if(this.mRows.get(rowIndex).getEntries().size() > 0){
						String decoded = this.decodeBase64ToTextOnly(this.mRows.get(rowIndex).getEntries().get(0).text);
						retObject = decoded;
					}
				}else if(columnIndex == HistoryTableModel.CREATED){
					retObject = DateFormatTool.getSimple12HourTime(new Date(this.mRows.get(rowIndex).getModified()));
				}else if(columnIndex == HistoryTableModel.HIDDEN_AT){
					retObject = DateFormatTool.getSimple12HourTime(new Date(this.mRows.get(rowIndex).getHiddenTime()));
				}else if(columnIndex == HistoryTableModel.HIDDEN_EVENT){
					retObject = this.mRows.get(rowIndex).getHiddenEvent();
				}else if(columnIndex == HistoryTableModel.DELETION_COMMAND){
					retObject = this.mRows.get(rowIndex).getDeletionCommand();
				}else if(columnIndex == HistoryTableModel.DELETION_SOURCE){
					retObject = this.mRows.get(rowIndex).getDeletionSource();
				}else if(columnIndex == HistoryTableModel.SESSION_MODE){
					retObject = this.mRows.get(rowIndex).getSessionMode().name();
				}
			}else{
				retObject = new String(""); // just makes a blank table cell.
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		return retObject;
	}
	
	private String decodeBase64ToTextOnly(final String encoded)
	{
		String decoded = null;
		try{
			byte[] parBytes = Base64.decodeBase64(encoded.getBytes("UTF8"));
			decoded = ConvertXMLToRTF.bytes2String(parBytes);
			decoded = ConvertXMLToTXT.getDoc(decoded);
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		return decoded;
	}
	
	public UUID getSessionIDFromRow(final int row)
	{
		UUID sessionID = null;
		try{
			if(row < 0 || row >= this.mRows.size()){
				throw new Exception("Parameter is our of range: " + row);
			}
			sessionID = this.mRows.get(row).getID();
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return sessionID;
	}
	
	// Since the table isn't editable, this event shouldn't get used.
	public void setValueAt(Object aValue, int rowIndex,	int columnIndex)
	{
		return;
	}
}
