package fontcombo;

import java.awt.Component;
import java.awt.Font;
import java.awt.Insets;
import java.awt.SystemColor;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;

public class FontSizeComboBox extends JComboBox<StyledListCell>
{
	private static final int DEFAULT_CELL_FONT_SIZE = 14;
	
	private DefaultComboBoxModel<StyledListCell> mListItems = null;
	private int mCellFontSize = FontSizeComboBox.DEFAULT_CELL_FONT_SIZE;
	
	public FontSizeComboBox() { return; }
	
	public FontSizeComboBox(Integer[] fontSizes, int cellFontSize) throws Exception
	{
		super();
		this.setRenderer(new FontListCellRenderer(SystemColor.textHighlight, SystemColor.window));
		this.setCellFontSize(cellFontSize);
		this.setList(fontSizes);
		return;
	}
	
	public void setList(Integer[] fontSizes) throws Exception
	{
		try{
			this.mListItems = new DefaultComboBoxModel<StyledListCell>();
			for(Integer size : fontSizes)
			{
				Font aFont = new Font(Font.SANS_SERIF, Font.PLAIN, this.mCellFontSize);
				
				StyledListCell cell = new StyledListCell(size.toString(), StyledListCell.CellAlign.RIGHT, new Insets(2, 5, 2, 5), aFont);
				this.mListItems.addElement(cell);
				
			}
			this.setModel(this.mListItems);		// resets the list control.
		}catch(Exception ex){
			throw ex;
		}
		return;
	}
	
	public void addListItem(Object value, StyledListCell.CellAlign align, Insets insets, Font font) throws Exception
	{
		try{
			StyledListCell cell = new StyledListCell(value, align, insets, font);
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
			if(this.mListItems.getElementAt(this.getSelectedIndex()) instanceof StyledListCell){
				item = this.mListItems.getElementAt(this.getSelectedIndex());
			}
		}
		return item;
	}
	
	public int getListValue()
	{
		int value = 0;
		if(this.getSelectedIndex() > -1){
			if(this.mListItems.getElementAt(this.getSelectedIndex()) instanceof StyledListCell){
				value = Integer.parseInt(this.mListItems.getElementAt(this.getSelectedIndex()).getLabel().getText());
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
		this.mListItems = new DefaultComboBoxModel<StyledListCell>();
		this.setModel(this.mListItems);		// resets the list control.
		return;
	}
	
	// Returns array index of first matching value.
	// Returns -1 if not found.
	public int find(int value)
	{
		int index = -1;
		if(this.mListItems.getSize() > 0){
			for(int i = 0; i < this.mListItems.getSize(); i++)
			{
				if(this.mListItems.getElementAt(i).getLabel().getText().compareToIgnoreCase(String.valueOf(value)) == 0){
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
									FontSizeComboBox.DEFAULT_CELL_FONT_SIZE + " will be used.");
			}
			this.mCellFontSize = fontSize;
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return;
	}
}
