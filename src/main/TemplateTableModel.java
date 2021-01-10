package main;

import javax.swing.table.AbstractTableModel;

import java.util.Vector;

import org.apache.commons.codec.binary.Base64;

public class TemplateTableModel extends AbstractTableModel
{
	public static final int NAME = 0;
	public static final int MESSAGE = 1;
	public static final int PRIORITY = 2;
	private static final String[] COLUMN_NAMES = new String[]{"Name", "Message", "Priority"};
	
	private Vector<TemplateData> mRows = null;
	
	public TemplateTableModel()
	{
		this.mRows = new Vector<TemplateData>();
		return;
	}
	
	public void setRowsVector(Vector<TemplateData> rows)
	{
		this.mRows = rows;
		return;
	}
	
	public void addRowToModel(TemplateData aRow)
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
		return TemplateTableModel.COLUMN_NAMES.length;
	}
	
	public boolean isCellEditable(int rowIndex, int columnIndex)
	{
		return false;
	}
	
	public String getColumnName(int columnIndex)
	{
		String name = null;
		try{
			if(columnIndex < 0 || columnIndex > TemplateTableModel.COLUMN_NAMES.length - 1){
				throw new Exception("Parameter out of range: " + columnIndex);
			}
			name = TemplateTableModel.COLUMN_NAMES[columnIndex];
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
				if(columnIndex == TemplateTableModel.NAME){
					retObject = this.mRows.get(rowIndex).getTemplateName();
				}else if(columnIndex == TemplateTableModel.MESSAGE){
					if(this.mRows.get(rowIndex).getEntry() != null && !this.mRows.get(rowIndex).getEntry().isEmpty()){
						String decoded = this.decodeBase64ToTextOnly(this.mRows.get(rowIndex).getEntry());
						retObject = decoded;
					}else{
						retObject = new String(""); // just makes a blank table cell.
					}
				}else if(columnIndex == TemplateTableModel.PRIORITY){
					retObject = this.mRows.get(rowIndex).getSessionPriority().name();
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
	
	// Since the table isn't editable, this event shouldn't get used.
	public void setValueAt(Object aValue, int rowIndex,	int columnIndex)
	{
		return;
	}
}
