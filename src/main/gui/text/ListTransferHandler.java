package main.gui.text;

import java.awt.datatransfer.Transferable;

import javax.activation.DataHandler;
import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.TransferHandler;

public class ListTransferHandler extends TransferHandler
{
	protected int[] mIndices = null;
	protected int mAction = 0;
	
	public ListTransferHandler(int action)
	{
		this.mAction = action;
	}
	
	public int getSourceActions(JComponent comp)
	{
		return TransferHandler.COPY_OR_MOVE;
	}
	
	public Transferable createTransferable(JComponent comp)
	{
		DataHandler handler = null;
		try{
			if(comp instanceof JList){
				this.mIndices = ((JList)comp).getSelectedIndices();
				if(this.mIndices == null || this.mIndices.length <= 0){
					throw new Exception();
				}
				String[] items = new String[this.mIndices.length];
				for(int i = 0; i < this.mIndices.length; i++)
				{
					items[i] = String.valueOf(((JList)comp).getModel().getElementAt(this.mIndices[i]));
				}
				handler = new DataHandler(new DataItem(items, ((JList)comp)), ListTransferable.FLAVOR.getMimeType());
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return handler;
	}
	
	public boolean canImport(TransferHandler.TransferSupport support)
	{
		boolean isSuccessful = true;
		try{
			if(!support.isDrop()){
				throw new Exception("Not drop target.");
			}
			if(!support.isDataFlavorSupported(ListTransferable.FLAVOR)){
				throw new Exception("Data flavor not supported.");
			}
	
			boolean actionSupported = (this.mAction & support.getSourceDropActions()) == this.mAction;
			if(actionSupported){
				support.setDropAction(this.mAction);
			}
		}catch(Exception ex){
			isSuccessful = false;
		}
		return isSuccessful;
	}
	
	protected boolean isDuplicate(DefaultListModel<String> model, String target)
	{
		boolean found = false;
		for(int i = 0; i < model.size(); i++)
		{
			if(model.getElementAt(i).compareToIgnoreCase(target) == 0){
				found = true;
				break;
			}
		}
		return found;
	}
	
	protected int isDuplicate(DefaultListModel<String> model, String target, int ignore)
	{
		int existing = -1;
		for(int i = 0; i < model.size(); i++)
		{
			if(i == ignore){ continue; }
			
			if(model.getElementAt(i).compareToIgnoreCase(target) == 0){
				existing = i;
				break;
			}
		}
		return existing;
	}
	
	protected void removeItemFromList(JList aList, String item)
	{
		DefaultListModel model = (DefaultListModel)aList.getModel();
		for(int i = 0; i < model.getSize(); i++)
		{
			if(((String)model.getElementAt(i)).compareToIgnoreCase(item) == 0){
				model.removeElementAt(i);
				break;
			}
		}
		return;
	}
}

