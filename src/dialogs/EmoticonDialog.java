package dialogs;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.UIManager;

import java.util.Vector;

public class EmoticonDialog extends JDialog implements ActionListener, EmoticonListEventListener
{
	public static enum Response{Okay, Cancel};
	
	private JPanel mBasePanel = null;
	private EmoticonList mThumbList = null;
	private JScrollPane mThumbListScrollPane = null;
	private JButton mOkayButton = null;
	private static final String OKAY_BUTTON = "Use selected item.";
	private JButton mCancelButton = null;
	private static final String CANCEL_BUTTON = "Cancel and close this window.";
	
	private Vector<Emoticon> mEmoticons = null;
	private EmoticonDialog.Response mResponse = EmoticonDialog.Response.Cancel;
	
	public EmoticonDialog(JFrame parent, Vector<Emoticon> emoticons) throws Exception
	{
		super(parent, true); // true = window is modal.
		try{
			this.setEmoticonVector(emoticons);
			
			this.initializeMain();
		}catch(Exception ex){
			throw ex;
		}
		return;
	}
	
	public EmoticonDialog.Response showDialog()
	{
		this.setLocationRelativeTo(null); // set center screen.
		this.setVisible(true);
		this.pack();
		
		return this.mResponse;
	}
	
	public String getSelectedItem()
	{
		return this.mThumbList.getSelectedListItem().fileName;
	}
	
	private void setEmoticonVector(Vector<Emoticon> emoticons) throws NullPointerException, Exception
	{
		try{
			if(emoticons == null){
				throw new NullPointerException("Parameter cannot be null.");
			}
			this.mEmoticons = emoticons;
		}catch(NullPointerException npe){
			throw npe;
		}catch(Exception ex){
			throw ex;
		}
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
		this.setTitle("Choose Emoticon");
		this.setResizable(false);
		
		this.mBasePanel = new JPanel();
		this.mBasePanel.setLayout(new BoxLayout(this.mBasePanel, BoxLayout.Y_AXIS));
		this.mBasePanel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
		this.add(this.mBasePanel);
		
		this.mBasePanel.add(this.getThumbListPanel());
		EmoticonListListener.addListener(this);
		
		this.mBasePanel.add(Box.createHorizontalStrut(16));
		
		this.mBasePanel.add(this.getCommandRow());
		
		KeyStroke ksEscape = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
		this.getRootPane().registerKeyboardAction(this, EmoticonDialog.CANCEL_BUTTON, ksEscape, JComponent.WHEN_IN_FOCUSED_WINDOW);
		KeyStroke ksEnter = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0);
		this.getRootPane().registerKeyboardAction(this, EmoticonDialog.OKAY_BUTTON, ksEnter, JComponent.WHEN_IN_FOCUSED_WINDOW);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setMinimumSize(new Dimension(340, 450));
		this.setMaximumSize(new Dimension(1000, 1000));
		this.setPreferredSize(new Dimension(400, 450));
		this.setResizable(true);
		this.getContentPane().add(this.mBasePanel);
		return;
	}
	
	private JPanel getThumbListPanel()
	{
		JPanel aPanel = new JPanel();
		aPanel.setLayout(new BoxLayout(aPanel, BoxLayout.X_AXIS));
		
		this.mThumbList = new EmoticonList();
		this.mThumbListScrollPane = new JScrollPane(this.mThumbList);
		aPanel.add(this.mThumbListScrollPane);
		
		try{
			int count = this.mEmoticons.size();
			for(int i = 0; i < count; i++)
			{
				this.mThumbList.addListItem(this.mEmoticons.get(i));
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		return aPanel;
	}
	
	private JPanel getCommandRow()
	{
		JPanel aPanel = new JPanel();
		aPanel.setLayout(new BoxLayout(aPanel, BoxLayout.X_AXIS));
		aPanel.setMinimumSize(new Dimension(440, 30));
		aPanel.setMaximumSize(new Dimension(440, 30));
		aPanel.setPreferredSize(new Dimension(440, 30));
		
		aPanel.add(Box.createVerticalStrut(16));
		
		this.mOkayButton = new JButton("Okay");
		this.mOkayButton.setMinimumSize(new Dimension(100, 25));
		this.mOkayButton.setMaximumSize(new Dimension(100, 25));
		this.mOkayButton.setPreferredSize(new Dimension(100, 25));
		this.mOkayButton.setToolTipText(EmoticonDialog.OKAY_BUTTON);
		this.mOkayButton.setActionCommand(EmoticonDialog.OKAY_BUTTON);
		this.mOkayButton.addActionListener(this);
		aPanel.add(this.mOkayButton);
		
		aPanel.add(Box.createHorizontalStrut(10));
		
		this.mCancelButton = new JButton("Cancel");
		this.mCancelButton.setMinimumSize(new Dimension(100, 25));
		this.mCancelButton.setMaximumSize(new Dimension(100, 25));
		this.mCancelButton.setPreferredSize(new Dimension(100, 25));
		this.mCancelButton.setToolTipText(EmoticonDialog.CANCEL_BUTTON);
		this.mCancelButton.setActionCommand(EmoticonDialog.CANCEL_BUTTON);
		this.mCancelButton.addActionListener(this);
		aPanel.add(this.mCancelButton);
		
		aPanel.add(Box.createVerticalStrut(16));
		
		return aPanel;
	}
	
	public void actionPerformed(ActionEvent source)
	{
		if(source.getActionCommand() == EmoticonDialog.OKAY_BUTTON && this.mThumbList.getSelectedListItem() != null){
			this.mResponse = EmoticonDialog.Response.Okay;
			this.disposeWindow();
		}
		if(source.getActionCommand() == EmoticonDialog.CANCEL_BUTTON){
			this.mResponse = EmoticonDialog.Response.Cancel;
			this.disposeWindow();
		}
		return;
	}
	
	public void thumbListClickPerformed(EmoticonListEvent tle){ return; }
	
	public void thumbListDoubleClickPerformed(EmoticonListEvent tle)
	{
		try{
			this.mResponse = EmoticonDialog.Response.Okay;
			this.disposeWindow();
		}catch(IllegalMonitorStateException imse){
			imse.printStackTrace();
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return;
	}
	
	public void disposeWindow()
	{
		EmoticonListListener.removeListener(this);
		this.dispose();
		return;
	}
}
