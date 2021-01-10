package main;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

/**
 * Swing's JPopupMenu needed to be extended to add a variable representing a table row number.
 * @author John McCullock
 * @version 1.0 2013-09-16
 * @see HistoryPopupEvent, HistoryPopupEventListener, HistoryPopupListener
 */

public class HistoryPopupMenu extends JPopupMenu implements ActionListener
{
	private JMenuItem mReopenMenuItem = null;
	private JMenuItem mRefreshMenuItem = null;
	private int mTableRow = 0;
	
	public HistoryPopupMenu()
	{
		super();
		this.initializeMain();
		return;
	}
	
	public void showPopup(Component invoker, int x, int y, int row)
	{
		this.mTableRow = row;
		super.show(invoker, x, y);
		return;
	}
	
	private void initializeMain()
	{
		this.mReopenMenuItem = new JMenuItem("Reopen Note");
		this.mReopenMenuItem.addActionListener(this);
		this.add(this.mReopenMenuItem);
		
		this.addSeparator();
		
		this.mRefreshMenuItem = new JMenuItem("Refresh");
		this.mRefreshMenuItem.addActionListener(this);
		this.add(this.mRefreshMenuItem);
		return;
	}
	
	public void actionPerformed(ActionEvent source)
	{
		if(source.getSource() == (JMenuItem)this.mReopenMenuItem){
			try{
				HistoryPopupListener.notifyReopen(source, this.mTableRow);
			}catch(IllegalMonitorStateException imse){
				imse.printStackTrace();
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}else if(source.getSource() == (JMenuItem)this.mRefreshMenuItem){
			try{
				HistoryPopupListener.notifyRefresh(source);
			}catch(IllegalMonitorStateException imse){
				imse.printStackTrace();
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
		return;
	}
}
