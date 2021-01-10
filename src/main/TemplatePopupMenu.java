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
 * @see TemplatePopupEvent, TemplatePopupEventListener, TemplatePopupListener
 */
public class TemplatePopupMenu extends JPopupMenu implements ActionListener
{
	private JMenuItem mOpenMenuItem = null;
	private JMenuItem mDeleteMenuItem = null;
	private int mTableRow = 0;
	
	public TemplatePopupMenu()
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
		this.mOpenMenuItem = new JMenuItem("Open");
		this.mOpenMenuItem.addActionListener(this);
		this.add(this.mOpenMenuItem);
		
		this.mDeleteMenuItem = new JMenuItem("Delete");
		this.mDeleteMenuItem.addActionListener(this);
		this.add(this.mDeleteMenuItem);
		return;
	}
	
	public void actionPerformed(ActionEvent source)
	{
		if(source.getSource() == (JMenuItem)this.mOpenMenuItem){
			try{
				TemplatePopupListener.notifyOpen(source, this.mTableRow);
			}catch(IllegalMonitorStateException imse){
				imse.printStackTrace();
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}else if(source.getSource() == (JMenuItem)this.mDeleteMenuItem){
			try{
				TemplatePopupListener.notifyDelete(source, this.mTableRow);
			}catch(IllegalMonitorStateException imse){
				imse.printStackTrace();
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
		return;
	}
}
