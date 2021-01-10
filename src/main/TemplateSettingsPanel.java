package main;

import config.ConfigData;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.GridLayout;

import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class TemplateSettingsPanel extends JPanel implements TemplatePopupEventListener
{
	private static final String CARET_DESCRIPTION = "<html><p>Type carets \"^\" in your template to indicate text entry markers.  Users will use the F12 key to jump from caret to caret, replacing them with their entries.</p></html>";
	private static final String PRIORITY_DESCRIPTION = "<html><p color=\"#CC0000\">Urgent notes cannot use carets for user entry, Normal notes can.</p></html>";
	
	private JFrame mParentFrame = null;
	private JButton mNewButton = null;
	private static final String NEW_BUTTON = "New";
	private JButton mOpenButton = null;
	private static final String OPEN_BUTTON = "Open";
	private JButton mDeleteButton = null;
	private static final String DELETE_BUTTON = "Delete";
	private JTable mTemplateTable = null;
	private TemplateTableModel mTemplateTableModel = null;
	private TemplatePopupMenu mTablePopup = null;
	
	public TemplateSettingsPanel(JFrame parentFrame)
	{
		super();
		this.mParentFrame = parentFrame;
		this.initializeMain();
		
		this.fillTable();
		return;
	}
		
	private void initializeMain()
	{
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		this.add(this.createTopHalfPanel());
		this.add(this.createRow2Panel());
		this.add(this.createRow3Panel());
		this.add(Box.createHorizontalStrut(16));
		return;
	}
	
	private JPanel createTopHalfPanel()
	{
		JPanel aPanel = new JPanel();
		aPanel.setLayout(new BoxLayout(aPanel, BoxLayout.X_AXIS));
		aPanel.setMinimumSize(new Dimension(450, 100));
		aPanel.setMaximumSize(new Dimension(2000, 200));
		aPanel.setPreferredSize(new Dimension(450, 200));
		
		aPanel.add(this.createLeftSidePanel());
		aPanel.add(this.createRightSidePanel());
		
		return aPanel;
	}
	
	private JPanel createLeftSidePanel()
	{
		JPanel aPanel = new JPanel();
		aPanel.setLayout(new BoxLayout(aPanel, BoxLayout.Y_AXIS));
		
		JPanel bPanel = new JPanel();
		bPanel.setLayout(new GridLayout(3, 1, 0, 10));
		bPanel.setMinimumSize(new Dimension(100, 80));
		bPanel.setMaximumSize(new Dimension(120, 100));
		bPanel.setPreferredSize(new Dimension(120, 100));
		bPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));
		this.mNewButton = new JButton(TemplateSettingsPanel.NEW_BUTTON);
		this.mNewButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				TemplateSettingsListener.notifyNew(this);
				return;
			}
		});
		bPanel.add(this.mNewButton);
		this.mOpenButton = new JButton(TemplateSettingsPanel.OPEN_BUTTON);
		this.mOpenButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				requestOpenTemplate(mTemplateTable.getSelectedRow());
				return;
			}
		});
		this.mOpenButton.setEnabled(false);
		bPanel.add(this.mOpenButton);
		this.mDeleteButton = new JButton(TemplateSettingsPanel.DELETE_BUTTON);
		this.mDeleteButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				requestDeleteTemplate(mTemplateTable.getSelectedRow());
				return;
			}
		});
		this.mDeleteButton.setEnabled(false);
		bPanel.add(this.mDeleteButton);
		aPanel.add(bPanel);
		
		aPanel.add(Box.createHorizontalStrut(16));
		
		return aPanel;
	}
	
	private JPanel createRightSidePanel()
	{
		JPanel aPanel = new JPanel();
		aPanel.setLayout(new BoxLayout(aPanel, BoxLayout.Y_AXIS));
		aPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
		aPanel.setMinimumSize(new Dimension(300, 100));
		aPanel.setMaximumSize(new Dimension(2000, 200));
		aPanel.setPreferredSize(new Dimension(2000, 200));
		
		this.mTemplateTable = new JTable();
		this.mTemplateTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		ListSelectionModel selModel = this.mTemplateTable.getSelectionModel();
		selModel.addListSelectionListener(new ListSelectionListener()
		{
			public void valueChanged(ListSelectionEvent e)
			{
				if(mTemplateTable.getSelectedRow() > -1){
					mOpenButton.setEnabled(true);
					mDeleteButton.setEnabled(true);
				}
				return;
			}
		});
		this.mTablePopup = new TemplatePopupMenu();
		this.mTemplateTable.addMouseListener(new MouseListener()
		{
			public void mouseClicked(MouseEvent e)			{ return; }
			public void mouseEntered(MouseEvent e)			{ return; }
			public void mouseExited(MouseEvent e)			{ return; }
			public void mousePressed(MouseEvent e)			{ return; }
			
			public void mouseReleased(MouseEvent e)
			{
				if(e.isPopupTrigger()){
					if(mTemplateTable.getMousePosition() != null){
						int row = mTemplateTable.rowAtPoint(e.getPoint());
						int column = mTemplateTable.columnAtPoint(e.getPoint());
						if(!mTemplateTable.isRowSelected(row)){
							mTemplateTable.changeSelection(row, column, false, false);
						}
						mTablePopup.showPopup(e.getComponent(), e.getX(), e.getY(), row);
					}
				}
				
				return;
			}
		});
		TemplatePopupListener.addListener(this);
		aPanel.add(new JScrollPane(this.mTemplateTable,
					                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
					                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED));
		
		aPanel.add(Box.createHorizontalStrut(16));
		
		return aPanel;
	}
	
	private JPanel createRow2Panel()
	{
		JPanel aPanel = new JPanel();
		aPanel.setLayout(new BoxLayout(aPanel, BoxLayout.X_AXIS));
		aPanel.setBorder(BorderFactory.createEmptyBorder(30, 0, 15, 0));
		
		aPanel.add(new JLabel(TemplateSettingsPanel.CARET_DESCRIPTION));
		
		return aPanel;
	}
	
	private JPanel createRow3Panel()
	{
		JPanel aPanel = new JPanel();
		aPanel.setLayout(new BoxLayout(aPanel, BoxLayout.X_AXIS));
		aPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
		
		aPanel.add(new JLabel(TemplateSettingsPanel.PRIORITY_DESCRIPTION));
		
		return aPanel;
	}
	
	public void fillTable()
	{
		this.mTemplateTable.setVisible(false); // setVisible seems to be the only sure way to get table data to refresh.
		
		this.mTemplateTableModel = new TemplateTableModel();
		for(String name : ConfigData.getTemplateNames())
		{
			this.mTemplateTableModel.addRowToModel(ConfigData.getTemplate(name));
		}
		this.mTemplateTable.setModel(this.mTemplateTableModel);
		this.mTemplateTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		this.mTemplateTable.getColumnModel().getColumn(0).setPreferredWidth(120);
		this.mTemplateTable.getColumnModel().getColumn(1).setPreferredWidth(200);
		this.mTemplateTable.getColumnModel().getColumn(2).setPreferredWidth(80);
		
		this.mTemplateTable.setVisible(true); // setVisible seems to be the only sure way to get table data to refresh.
		return;
	}
	
	private void requestOpenTemplate(int row)
	{
		String name = String.valueOf(mTemplateTable.getValueAt(row, TemplateTableModel.NAME));
		TemplateSettingsListener.notifyOpen(this, name);
		return;
	}
	
	private void requestDeleteTemplate(int row)
	{
		try{
			String name = String.valueOf(mTemplateTable.getValueAt(row, TemplateTableModel.NAME));
			int response = JOptionPane.showOptionDialog(this.mParentFrame, 
														"Are you sure you want to delete this template: \"" + name + "\" ?", 
														"Confirm Deletion", 
														JOptionPane.YES_NO_OPTION, 
														JOptionPane.QUESTION_MESSAGE,
														null,
														null,
														null);
			if(response == JOptionPane.YES_OPTION){
				this.completeTemplateDeletion(name);
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return;
	}
	
	// YOU NEED TO COME BACK TO THIS - DELETION NOT WORKING WHILE IN ECLIPSE
	private void completeTemplateDeletion(String name)
	{
		try{
			String filename = "./" + name + ".template";
			File target = new File(filename);
			if(target.exists() && target.canWrite()){
				boolean success = target.delete();
				System.out.println(success);
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return;
	}
	
	public void templatePopupOpenPerformed(TemplatePopupEvent e)
	{
		this.requestOpenTemplate(e.getTableRow());
		return;
	}
	
	public void templatePopupDeletePerformed(TemplatePopupEvent e)
	{
		this.requestDeleteTemplate(e.getTableRow());
		return;
	}
	
	public void shutdownTemplatePopupListener()
	{
		TemplatePopupListener.removeListener(this);
		return;
	}
}
