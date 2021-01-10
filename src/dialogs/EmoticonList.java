package dialogs;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.Component;
import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;

import javax.swing.ImageIcon;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;

import java.util.Vector;

public class EmoticonList extends JList<EmoticonListCell> implements MouseListener
{
	private Vector<EmoticonListCell> mListItems = null;
	
	public EmoticonList()
	{
		this.initializeMain();
		this.addMouseListener(this);
		return;
	}
	
	private void initializeMain()
	{
		this.setCellRenderer(new EmoticonListCellRenderer());
		this.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		this.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		this.setVisibleRowCount(0); // Zero, or less, ensures the desired horizontal wrap.
		
		this.mListItems = new Vector<EmoticonListCell>();
		this.setListData(this.mListItems);
		
		return;
	}
	
	public void addListItem(Emoticon emoticon) throws Exception
	{
		try{
			EmoticonListCell cell = new EmoticonListCell(emoticon);
			this.mListItems.addElement(cell);
			this.setListData(this.mListItems);		// resets the listview control.
		}catch(Exception ex){
			throw ex;
		}
		return;
	}
	
	public Emoticon getSelectedListItem()
	{
		Emoticon e = null;
		
		if(this.getSelectedIndex() > -1){
			e = this.getSelectedValue().getEmoticon();
		}
		
		return e;
	}
	
	public void mouseClicked(MouseEvent e)
	{
		if(e.getClickCount() == 2){
			try{
				EmoticonListListener.notifyDoubleClick(this);
			}catch(IllegalMonitorStateException imse){
				imse.printStackTrace();
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
		return;
	}
	
	public void mousePressed(MouseEvent e)	{ return; }
	public void mouseReleased(MouseEvent e)	{ return; }
	public void mouseEntered(MouseEvent e)	{ return; }
	public void mouseExited(MouseEvent e)	{ return; }
}
