package dialogs;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class EmoticonListCell extends JPanel
{
	private Emoticon mEmoticon = null;
	
	public EmoticonListCell(Emoticon emoticon) throws Exception
	{
		super();
		this.setEmoticon(emoticon);
		this.initializeMain();
		return;
	}
	
	private void setEmoticon(Emoticon emoticon) throws NullPointerException, Exception
	{
		try{
			if(emoticon == null){
				throw new NullPointerException("Parameter cannot be null.");
			}
			this.mEmoticon = emoticon;
		}catch(NullPointerException npe){
			throw npe;
		}catch(Exception ex){
			throw ex;
		}
		return;
	}
	
	public Emoticon getEmoticon()
	{
		return this.mEmoticon;
	}
	
	private void initializeMain()
	{
		this.setBackground(Color.white);
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.setMinimumSize(new Dimension(48, 48));
		this.setMaximumSize(new Dimension(48, 48));
		
		this.add(Box.createHorizontalStrut(1));
		
		JPanel aPanel = new JPanel();
		aPanel.setLayout(new BoxLayout(aPanel, BoxLayout.X_AXIS));
		aPanel.setMinimumSize(new Dimension(this.mEmoticon.getIconWidth(), this.mEmoticon.getIconHeight()));
		aPanel.setMaximumSize(new Dimension(this.mEmoticon.getIconWidth(), this.mEmoticon.getIconHeight()));
		aPanel.setOpaque(false);
		aPanel.add(Box.createVerticalStrut(1));
		
		JLabel aLabel = new JLabel();
		aLabel.setIcon(this.mEmoticon);
		aPanel.add(aLabel);
		
		aPanel.add(Box.createVerticalStrut(1));
		
		this.add(aPanel);
		
		this.add(Box.createHorizontalStrut(1));
		
		return;
	}
}
