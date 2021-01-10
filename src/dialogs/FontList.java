package dialogs;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.Font;

import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;

import java.util.Locale;
import java.util.Vector;

public class FontList extends JList<FontListCell>
{
	private Vector<FontListCell> mListItems = null;
	
	public FontList()
	{
		this.setCellRenderer(new FontListCellRenderer());
		this.mListItems = new Vector<FontListCell>();
		
		this.setListData(this.mListItems);
		return;
	}
	
	public void setList(String[] fontFamilyNames) throws Exception
	{
		try{
			for(String fontName : fontFamilyNames)
			{
				Font aFont = new Font(fontName, Font.PLAIN, 18);
				if(aFont.canDisplayUpTo("abcABCxyzXYZ123789") == -1){
				//if(aFont.canDisplay('#')){
					FontListCell cell = new FontListCell(aFont);
					this.mListItems.addElement(cell);
				}
			}
			this.setListData(this.mListItems);		// resets the list control.
		}catch(Exception ex){
			throw ex;
		}
		return;
	}
	
	public void addListItem(Font font) throws Exception
	{
		try{
			FontListCell cell = new FontListCell(font);
			this.mListItems.addElement(cell);
			this.setListData(this.mListItems);		// resets the list control.
		}catch(Exception ex){
			throw ex;
		}
		return;
	}
	
	public Component getListItem()
	{
		Component item = null;
		if(this.getSelectedIndex() > -1){
			if(this.mListItems.get(this.getSelectedIndex()) instanceof FontListCell){
				item = this.mListItems.get(this.getSelectedIndex());
			}
		}
		return item;
	}
	
	public String getListValue()
	{
		String value = null;
		if(this.getSelectedIndex() > -1){
			if(this.mListItems.get(this.getSelectedIndex()) instanceof FontListCell){
				value = this.mListItems.get(this.getSelectedIndex()).getLabel().getText();
			}
		}
		return value;
	}
	
	public void removeItem(int Index) throws Exception
	{
		try{
			if(this.mListItems.isEmpty()){
				throw new Exception("Index out of bounds");
			}else{
				this.mListItems.remove(Index);
				this.setListData(this.mListItems);
			}
		}catch(Exception ex){
			throw ex;
		}
		return;
	}
	
	public void clearList()
	{
		this.mListItems.clear();
		this.setListData(this.mListItems);
		return;
	}
	
	// Returns array index of first matching value.
	// Returns -1 if not found.
	public int find(String value)
	{
		int index = -1;
		if(!this.mListItems.isEmpty() && value != null){
			for(int i = 0; i < this.mListItems.size(); i++)
			{
				if(this.mListItems.get(i).getLabel().getText().compareToIgnoreCase(value) == 0){
					index = i;
					break;
				}
			}
		}
		return index;
	}
}
