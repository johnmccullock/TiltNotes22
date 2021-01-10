package main.gui.text;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.datatransfer.Transferable;

public class ListTransferable implements Transferable
{
	public static DataFlavor FLAVOR = new DataFlavor(DataItem.class, "DataItem");
	private DataFlavor mFlavors[];
	public DataItem mDataItem;
	
	public ListTransferable(String[] data, Object source)
	{
		this.mDataItem = new DataItem(data, source);
		this.mFlavors = new DataFlavor[] { FLAVOR };
    }
	
	public static DataFlavor getFlavor()
	{
		return new DataFlavor(DataItem.class, "DataItem");
	}
	
	@Override
	public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException
	{
		Object item = null;
		try{
			if(!flavor.equals(FLAVOR)){
				throw new UnsupportedFlavorException(flavor);
			}
			item = this.mDataItem;
		}catch(UnsupportedFlavorException ex){
			throw ex;
		}catch(Exception ex){
			throw ex;
		}
		return item;
	}
	
	@Override
	public DataFlavor[] getTransferDataFlavors()
	{
		return this.mFlavors;
	}
	
	@Override
	public boolean isDataFlavorSupported(DataFlavor flavor)
	{
		return flavor.equals(FLAVOR) || flavor.equals(DataFlavor.stringFlavor);
	}
}
