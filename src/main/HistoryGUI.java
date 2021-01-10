package main;

import util.ImageLoader;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.SystemColor;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.DefaultListModel;
import javax.swing.DropMode;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;

import java.util.UUID;
import java.util.Vector;

/**
 * This window allows the user to explore sessions which have been recently deleted.  Sessions appearing in 
 * this window's table show when they were deleted, who deleted them, and the session's message content, when
 * the session's GUI became invisible, and which window events were involved in the GUI's closing.  
 * 
 * This was originally intended as way of troubleshooting odd session behaviors, such as mysteriously 
 * disappearing sessions.
 * 
 * Other than troubleshooting, users can use this window like and "undo" function just in case the deleted a 
 * session and then changed their mind.
 * 
 * To use this class, it first needs to be instantiated, then it's fillTable method need to be called in order
 * to supply the table with a Vector<SessionData>.
 * 
 * @author John McCullock
 * @version 1.0 2013-09-16
 *
 */
public class HistoryGUI extends JFrame implements HistoryPopupEventListener
{
	private JPanel mBasePanel = null;
	private JTable mHistoryTable = null;
	private HistoryTableModel mHistoryTableModel = null;
	private HistoryPopupMenu mHistoryPopupMenu = null;
	private JButton mCloseButton = null;
	private static final String CLOSE_BUTTON = "Close this window.";
	private JButton mReopenButton = null;
	private static final String REOPEN_BUTTON = "Reopen the selected note.";
	private JButton mClearButton = null;
	private static final String CLEAR_BUTTON = "Clear the history table";
	private JButton mRefreshButton = null;
	private static final String REFRESH_BUTTON = "Freshen the table with current data.";
	
	public HistoryGUI()
	{
		this.initializeMain();
		return;
	}
	
	private void initializeMain()
	{
		try{
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		}catch(Exception e){
			try{
				JFrame.setDefaultLookAndFeelDecorated(true);
			}catch(Exception ex){
				System.out.println("Error on look and feel");
			}
		}
		
		this.setTitle(Globals.HISTORY_GUI_TITLE);
		this.setResizable(true);
		
		try{
			Image anImage = ImageLoader.getImageFromResourcePath(this.getClass().getResource(Globals.APPLICATION_ICON_32x32));
			this.setIconImage(anImage);	
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		this.mBasePanel = new JPanel();
		this.mBasePanel.setLayout(new BoxLayout(this.mBasePanel, BoxLayout.Y_AXIS));
		this.mBasePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		this.add(this.mBasePanel);
		
		this.mBasePanel.add(this.createDescriptionPanel());
		this.mBasePanel.add(this.createTablePanel());
		this.mBasePanel.add(this.createButtonRow());
		
		KeyStroke ksEscape = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
		this.rootPane.registerKeyboardAction(this.getCancelActionListener(), 
												HistoryGUI.CLOSE_BUTTON, 
												ksEscape, 
												JComponent.WHEN_IN_FOCUSED_WINDOW);
		this.addWindowListener(this.createWindowListener());
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.getContentPane().add(this.mBasePanel);
		this.setMinimumSize(Globals.HISTORY_GUI_MINIMUM_SIZE);
		this.setPreferredSize(Globals.HISTORY_GUI_PREFERRED_SIZE);
		this.setLocationByPlatform(true);
		this.setVisible(true);
		this.pack();
		return;
	}
	
	private JPanel createDescriptionPanel()
	{
		JPanel aPanel = new JPanel();
		aPanel.setLayout(new BoxLayout(aPanel, BoxLayout.X_AXIS));
		aPanel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
		
		StringBuilder text = new StringBuilder();
		text.append("<html><p>This table reflects the history of notes sent and received on this machine for the past ");
		text.append(String.valueOf(Globals.HISTORY_EXPIRATION / 1000.0));
		text.append(" minutes.</p></html>");
		aPanel.add(new JLabel(text.toString()));
		
		return aPanel;
	}
	
	private JPanel createTablePanel()
	{
		JPanel aPanel = new JPanel();
		aPanel.setLayout(new BoxLayout(aPanel, BoxLayout.X_AXIS));
		
		this.mHistoryTable = new JTable();
		this.mHistoryPopupMenu = new HistoryPopupMenu();
		this.mHistoryTable.addMouseListener(new MouseListener()
		{
			public void mouseClicked(MouseEvent e)			{ return; }
			public void mouseEntered(MouseEvent e)			{ return; }
			public void mouseExited(MouseEvent e)			{ return; }
			public void mousePressed(MouseEvent e)			{ return; }
			
			public void mouseReleased(MouseEvent e)
			{
				if(e.isPopupTrigger()){
					if(mHistoryTable.getMousePosition() != null){
						int row = mHistoryTable.rowAtPoint(e.getPoint());
						int column = mHistoryTable.columnAtPoint(e.getPoint());
						//System.out.println(row + ", " + column);
						if(!mHistoryTable.isRowSelected(row)){
							mHistoryTable.changeSelection(row, column, false, false);
						}
						mHistoryPopupMenu.showPopup(e.getComponent(), e.getX(), e.getY(), row);
					}
				}
				return;
			}
		});
		HistoryPopupListener.addListener(this);
		this.mHistoryTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		aPanel.add(new JScrollPane(this.mHistoryTable,
					                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
					                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED));
		
		return aPanel;
	}
	
	private JPanel createButtonRow()
	{
		JPanel aPanel = new JPanel();
		aPanel.setLayout(new BoxLayout(aPanel, BoxLayout.X_AXIS));
		aPanel.setBorder(BorderFactory.createEmptyBorder(10, 4, 6, 0));
		
		aPanel.add(Box.createVerticalStrut(16));
		
		JPanel bPanel = new JPanel();
		bPanel.setLayout(new GridLayout(1, 4, 10, 0));
		bPanel.setMaximumSize(new Dimension(350, 30));
		
		this.mClearButton = new JButton("Clear");
		this.mClearButton.setActionCommand(HistoryGUI.CLEAR_BUTTON);
		this.mClearButton.setToolTipText(HistoryGUI.CLEAR_BUTTON);
		this.mClearButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				requestClearHistory();
			}
		});
		bPanel.add(this.mClearButton);
		
		this.mRefreshButton = new JButton("Refresh");
		this.mRefreshButton.setActionCommand(HistoryGUI.REFRESH_BUTTON);
		this.mRefreshButton.setToolTipText(HistoryGUI.REFRESH_BUTTON);
		this.mRefreshButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				requestTableRefresh();
			}
		});
		bPanel.add(this.mRefreshButton);
		
		this.mReopenButton = new JButton("Reopen Note");
		this.mReopenButton.setActionCommand(HistoryGUI.REOPEN_BUTTON);
		this.mReopenButton.setToolTipText(HistoryGUI.REOPEN_BUTTON);
		this.mReopenButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				if(mHistoryTable.getSelectedRow() > -1){
					requestReopen(mHistoryTableModel.getSessionIDFromRow(mHistoryTable.getSelectedRow()));
				}
			}
		});
		bPanel.add(this.mReopenButton);
		
		this.mCloseButton = new JButton("Close");
		this.mCloseButton.setActionCommand(HistoryGUI.CLOSE_BUTTON);
		this.mCloseButton.setToolTipText(HistoryGUI.CLOSE_BUTTON);
		this.mCloseButton.addActionListener(this.getCancelActionListener());
		bPanel.add(this.mCloseButton);
		aPanel.add(bPanel);
		
		return aPanel;
	}
		
	public void fillTable(Vector<SessionData> rows)
	{
		this.mHistoryTable.setVisible(false); // setVisible seems to be the only sure way to get table data to refresh.
		
		this.mHistoryTableModel = new HistoryTableModel();
		this.mHistoryTableModel.setRowsVector(rows);
		this.mHistoryTable.setModel(this.mHistoryTableModel);
		this.mHistoryTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		this.mHistoryTable.getColumnModel().getColumn(0).setPreferredWidth(100);
		this.mHistoryTable.getColumnModel().getColumn(1).setPreferredWidth(100);
		this.mHistoryTable.getColumnModel().getColumn(2).setPreferredWidth(250);
		this.mHistoryTable.getColumnModel().getColumn(3).setPreferredWidth(100);
		this.mHistoryTable.getColumnModel().getColumn(4).setPreferredWidth(100);
		this.mHistoryTable.getColumnModel().getColumn(5).setPreferredWidth(100);
		this.mHistoryTable.getColumnModel().getColumn(6).setPreferredWidth(150);
		this.mHistoryTable.getColumnModel().getColumn(7).setPreferredWidth(150);
		this.mHistoryTable.getColumnModel().getColumn(8).setPreferredWidth(100);
		
		this.mHistoryTable.setVisible(true); // setVisible seems to be the only sure way to get table data to refresh.
		return;
	}
	
	private void requestTableRefresh()
	{
		try{
			HistoryListener.notifyTableRefresh(this);
		}catch(IllegalMonitorStateException imse){
			imse.printStackTrace();
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return;
	}
	
	private void requestReopen(UUID sessionID)
	{
		try{
			HistoryListener.notifyReopen(this, sessionID);
			this.disposeWindow();
		}catch(IllegalMonitorStateException imse){
			imse.printStackTrace();
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return;
	}
	
	private void requestClearHistory()
	{
		try{
			int response = JOptionPane.showOptionDialog(this, 
														"Are you sure you want to delete all items from the history list?", 
														"Confirm Clear History", 
														JOptionPane.YES_NO_OPTION, 
														JOptionPane.WARNING_MESSAGE,
														null,
														null,
														null);
			if(response == JOptionPane.YES_OPTION){
				try{
					HistoryListener.notifyClear(this);
				}catch(IllegalMonitorStateException imse){
					imse.printStackTrace();
				}catch(Exception ex){
					ex.printStackTrace();
				}
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return;
	}
	
	public void requestWindowFocus()
	{
		this.requestFocus();
		return;
	}
	
	private ActionListener getCancelActionListener()
	{
		return new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				disposeWindow();
				return;
			}
		};
	}
	
	public void historyPopupReopenPerformed(HistoryPopupEvent e)
	{
		this.requestReopen(this.mHistoryTableModel.getSessionIDFromRow(e.getTableRow()));
		return;
	}
	
	public void historyPopupRefreshPerformed(HistoryPopupEvent e)
	{
		this.requestTableRefresh();
		return;
	}
	
	private WindowListener createWindowListener()
	{
		return new WindowListener()
		{
			public void windowActivated(WindowEvent e)		{ return; }
			public void windowClosed(WindowEvent e)			{ return; }
			public void windowDeactivated(WindowEvent e)	{ return; }
			public void windowDeiconified(WindowEvent e)	{ return; }
			public void windowIconified(WindowEvent e)		{ return; }
			public void windowOpened(WindowEvent e)			{ return; }
			
			public void windowClosing(WindowEvent e)
			{
				disposeWindow();
				return;
			}
		};
	}
	
	public void disposeWindow()
	{
		HistoryPopupListener.removeListener(this);
		try{
			HistoryListener.notifyGUIClose(this);
		}catch(IllegalMonitorStateException imse){
			imse.printStackTrace();
		}catch(Exception ex){
			ex.printStackTrace();
		}
		this.dispose();
		return;
	}
}
