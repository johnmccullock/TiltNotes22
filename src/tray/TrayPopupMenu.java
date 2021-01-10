package tray;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Menu;
import java.awt.MenuItem;
import java.awt.PopupMenu;

import java.util.ArrayList;
import java.util.Collections;



import config.ConfigData;

public class TrayPopupMenu extends PopupMenu implements ActionListener
{
	private MenuItem mNewNoteMenuItem = null;
	private Menu mTemplatesMenu = null;
	private MenuItem mHistoryMenuItem = null;
	private MenuItem mSettingsMenuItem = null;
	private MenuItem mExitMenuItem = null;
	
	public TrayPopupMenu(boolean showTemplatesMenu) throws NullPointerException, Exception
	{
		super();
		try{
			this.initializeMenuItems(showTemplatesMenu);
		}catch(NullPointerException npe){
			throw npe;
		}catch(Exception ex){
			throw ex;
		}
		return;
	}
	
	private void initializeMenuItems(boolean showTemplatesMenu)
	{
		this.mNewNoteMenuItem = new MenuItem("New Note");
		this.mNewNoteMenuItem.addActionListener(this);
		this.add(this.mNewNoteMenuItem);
		
		if(showTemplatesMenu){
			this.mTemplatesMenu = new Menu("Templates");
			this.add(this.mTemplatesMenu);
		}
		
		this.addSeparator();
		
		this.mHistoryMenuItem = new MenuItem("History...");
		this.mHistoryMenuItem.addActionListener(this);
		this.add(this.mHistoryMenuItem);
		
		this.mSettingsMenuItem = new MenuItem("Settings...");
		this.mSettingsMenuItem.addActionListener(this);
		this.add(this.mSettingsMenuItem);
		
		this.addSeparator();
		
		this.mExitMenuItem = new MenuItem("Exit");
		this.mExitMenuItem.addActionListener(this);
		this.add(this.mExitMenuItem);
		return;
	}
	
	public void resetTemplateMenu()
	{
		// Clear out the previous menu items.
		this.mTemplatesMenu.removeAll();
	}
	
	public void createTemplateMenuItem(final String name, boolean isUrgent)
	{
		try{
			MenuItem item = null;
			if(isUrgent){
				item = new MenuItem(name + " (urgent)");
			}else{
				item = new MenuItem(name);
			}
			item.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					try{
						TrayMenuListener.notifyTemplate(this, name);
					}catch(IllegalMonitorStateException imse){
						imse.printStackTrace();
					}catch(Exception ex){
						ex.printStackTrace();
					}
					return;
				}
			});
			this.mTemplatesMenu.add(item);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return;
	}
	
	public void setTemplateMenuEnable(final boolean enabled)
	{
		this.mTemplatesMenu.setEnabled(enabled);
		return;
	}
	
	public void actionPerformed(ActionEvent source)
	{
		if(source.getSource() == (MenuItem)this.mNewNoteMenuItem){
			try{
				TrayMenuListener.notifyNewNote(source);
			}catch(IllegalMonitorStateException imse){
				imse.printStackTrace();
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}else if(source.getSource() == (MenuItem)this.mHistoryMenuItem){
			try{
				TrayMenuListener.notifyHistory(source);
			}catch(IllegalMonitorStateException imse){
				imse.printStackTrace();
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}else if(source.getSource() == (MenuItem)this.mSettingsMenuItem){
			try{
				TrayMenuListener.notifySettings(source);
			}catch(IllegalMonitorStateException imse){
				imse.printStackTrace();
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}else if(source.getSource() == (MenuItem)this.mExitMenuItem){
			try{
				TrayMenuListener.notifyExit(source);
			}catch(IllegalMonitorStateException imse){
				imse.printStackTrace();
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
		return;
	}
}
