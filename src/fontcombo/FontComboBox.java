package fontcombo;

import java.awt.Component;
import java.awt.Font;
import java.awt.SystemColor;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;

public class FontComboBox extends JComboBox<FontListCell>
{
	private static final int DEFAULT_CELL_FONT_SIZE = 14;
	
	private DefaultComboBoxModel<FontListCell> mListItems = null;
	private int mCellFontSize = FontComboBox.DEFAULT_CELL_FONT_SIZE;
	
	public FontComboBox() { return; }
	
	public FontComboBox(String[] presetFontList, int fontSize) throws Exception
	{
		super();
		this.setRenderer(new FontListCellRenderer(SystemColor.textHighlight, SystemColor.window));
		this.setCellFontSize(fontSize);
		this.setList(presetFontList);
		return;
	}
	
	public void setList(String[] fontFamilyNames) throws Exception
	{
		try{
			this.mListItems = new DefaultComboBoxModel<FontListCell>();
			for(String fontName : fontFamilyNames)
			{
				Font aFont = new Font(fontName, Font.PLAIN, this.mCellFontSize);
				if(aFont.canDisplayUpTo("abcABCxyzXYZ123789") == -1){
				//if(aFont.canDisplay('#')){
					FontListCell cell = new FontListCell(aFont);
					this.mListItems.addElement(cell);
				}
			}
			this.setModel(this.mListItems);		// resets the list control.
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
			this.setModel(this.mListItems);		// resets the list control.
		}catch(Exception ex){
			throw ex;
		}
		return;
	}
	
	public Component getListItem()
	{
		Component item = null;
		if(this.getSelectedIndex() > -1){
			if(this.mListItems.getElementAt(this.getSelectedIndex()) instanceof FontListCell){
				item = this.mListItems.getElementAt(this.getSelectedIndex());
			}
		}
		return item;
	}
	
	public String getListValue()
	{
		String value = null;
		if(this.getSelectedIndex() > -1){
			if(this.mListItems.getElementAt(this.getSelectedIndex()) instanceof FontListCell){
				value = this.mListItems.getElementAt(this.getSelectedIndex()).getLabel().getText();
			}
		}
		return value;
	}
	
	public void removeItem(int index) throws Exception
	{
		try{
			if(this.mListItems.getSize() <= 0){
				throw new Exception("The list is empty.");
			}
			if(index < 0 || index >= this.mListItems.getSize()){
				throw new Exception("Index out of bounds");
			}
			this.mListItems.removeElementAt(index);
			this.setModel(this.mListItems);		// resets the list control.
		}catch(Exception ex){
			throw ex;
		}
		return;
	}
	
	public void clearList()
	{
		this.mListItems = new DefaultComboBoxModel<FontListCell>();
		this.setModel(this.mListItems);		// resets the list control.
		return;
	}
	
	// Returns array index of first matching value.
	// Returns -1 if not found.
	public int find(String value)
	{
		int index = -1;
		if(this.mListItems.getSize() > 0 && value != null){
			for(int i = 0; i < this.mListItems.getSize(); i++)
			{
				if(this.mListItems.getElementAt(i).getLabel().getText().compareToIgnoreCase(value) == 0){
					index = i;
					break;
				}
			}
		}
		return index;
	}
	
	public void setCellFontSize(int fontSize)
	{
		try{
			if(fontSize <= 0){
				throw new Exception("Parameter must be greater than zero. " + 
									fontSize + ". Instead the default size of " + 
									FontComboBox.DEFAULT_CELL_FONT_SIZE + " will be used.");
			}
			this.mCellFontSize = fontSize;
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return;
	}
}
