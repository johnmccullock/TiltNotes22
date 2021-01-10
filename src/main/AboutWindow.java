package main;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.awt.Insets;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ImageIcon;

import java.io.IOException;

/**
 * 
 * @author John McCullock
 * @version 2.0 2013-10-22
 */
public class AboutWindow extends JDialog
{
	private JPanel mBasePanel = null;
	private JPanel mTopPanel = null;
	private JPanel mImagePanel = null;
	private JPanel mTopRightPanel = null;
	private JPanel mBottomPanel = null;
	private JTextArea mTopTextArea = null;
	private JTextArea mBottomTextArea = null;
	private JLabel mImageLabel = null;
	
	public AboutWindow(JFrame parent)
	{
		super(parent, true);
		this.initializeMain();
		return;
	}
	
	private void initializeMain()
	{
		this.setTitle(Globals.ABOUT_WINDOW_TITLE);
		try{
			BufferedImage aBMP = ImageIO.read(this.getClass().getResource(Globals.TILT_TECH_LOGO_16x13));
			ImageIcon thumbNail = new ImageIcon(aBMP);
			this.setIconImage(thumbNail.getImage());	
		}catch(IOException ex){
			ex.printStackTrace();
		}
		
		this.mBasePanel = new JPanel();
		this.mBasePanel.setLayout(new BoxLayout(this.mBasePanel, BoxLayout.Y_AXIS));
		this.mBasePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		this.initializeTopHalf();
		this.mBasePanel.add(this.mTopPanel);
		
		this.initializeBottomPanel();
		this.mBasePanel.add(this.mBottomPanel);
		
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.getContentPane().add(this.mBasePanel);
		this.setMinimumSize(new Dimension(Globals.ABOUT_WINDOW_WIDTH, Globals.ABOUT_WINDOW_HEIGHT));
		this.setPreferredSize(new Dimension(Globals.ABOUT_WINDOW_WIDTH, Globals.ABOUT_WINDOW_HEIGHT));
		this.setLocationRelativeTo(null); // set center screen.
		this.setVisible(true);
		this.pack();
		
		return;
	}
	
	private void initializeTopHalf()
	{
		this.mTopPanel = new JPanel();
		this.mTopPanel.setLayout(new GridLayout(1, 2, 10, 0));
		this.initializeImagePanel();
		this.mTopPanel.add(this.mImagePanel);
		this.initializeTopRightPanel();
		this.mTopPanel.add(this.mTopRightPanel);
		return;
	}
	
	private void initializeImagePanel()
	{
		this.mImagePanel = new JPanel();
		
		try{
			BufferedImage aBMP = ImageIO.read(this.getClass().getResource(Globals.LARGE_LOGO_PATH));
			ImageIcon thumbNail = new ImageIcon(aBMP);
			this.mImageLabel = new JLabel(thumbNail);
			this.mImageLabel.setToolTipText("meow");
			this.mImagePanel.add(this.mImageLabel);
			
		}catch(IOException ex){
			ex.printStackTrace();
		}
		
		return;
	}
	
	private void initializeTopRightPanel()
	{
		this.mTopRightPanel = new JPanel();
		this.mTopRightPanel.setLayout(new BoxLayout(this.mTopRightPanel, BoxLayout.Y_AXIS));
		this.mTopRightPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		
		JPanel aPanel = new JPanel();
		aPanel.setLayout(new BoxLayout(aPanel, BoxLayout.X_AXIS));
		this.mTopTextArea = new JTextArea();
		this.mTopTextArea.setEditable(false);
		this.mTopTextArea.setLineWrap(true);
		this.mTopTextArea.setWrapStyleWord(true);
		this.mTopTextArea.setOpaque(false);
		this.mTopTextArea.setFont(new Font(Font.SERIF, Font.PLAIN, 14));
		
		StringBuilder details = new StringBuilder();
		details.append(Globals.APPLICATION_TITLE + "\n");
		details.append(Globals.APPLICATION_VERSION + "\n\n");
		details.append(Globals.APPLICATION_AUTHOR + "\n");
		details.append(Globals.APPLICATION_PUBLISHER + "\n");
		details.append(Globals.APPLICATION_COPYRIGHT);
		this.mTopTextArea.setText(details.toString());
		aPanel.add(this.mTopTextArea);
		this.mTopRightPanel.add(aPanel);
		
		try{
			aPanel = new JPanel();
			aPanel.setLayout(new BoxLayout(aPanel, BoxLayout.X_AXIS));
			BufferedImage aBMP = ImageIO.read(this.getClass().getResource(Globals.PUBLIC_DOMAIN_LOGO));
			ImageIcon thumbNail = new ImageIcon(aBMP);
			JLabel aLabel = new JLabel(thumbNail);
			aLabel.setToolTipText("unlicense.org");
			aPanel.add(aLabel);
			aPanel.add(Box.createVerticalStrut(16));
			thumbNail = new ImageIcon(this.getClass().getResource(Globals.TILT_TECH_LOGO_64x51));
			aLabel = new JLabel(thumbNail);
			aLabel.setToolTipText("Tilt Technologies, Inc.");
			aPanel.add(aLabel);
			this.mTopRightPanel.add(aPanel);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		this.mTopRightPanel.add(Box.createHorizontalStrut(16));
		
		return;
	}
	
	private void initializeBottomPanel()
	{
		this.mBottomPanel = new JPanel();
		this.mBottomPanel.setLayout(new BoxLayout(this.mBottomPanel, BoxLayout.Y_AXIS));
		this.mBottomPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		
		this.mBottomTextArea = new JTextArea();
		this.mBottomTextArea.setEditable(false);
		this.mBottomTextArea.setLineWrap(true);
		this.mBottomTextArea.setWrapStyleWord(true);
		this.mBottomTextArea.setOpaque(false);
		this.mBottomTextArea.setMargin(new Insets(8, 8, 8, 8));
		//this.mBottomTextArea.setFont(new Font(Font.SERIF, Font.PLAIN, 12));
		
		StringBuilder comments = new StringBuilder();
		comments.append(Globals.APPLICATION_TRADEMARK + "\n\n");
		comments.append(Globals.APPLICATION_SUPPORT + "\n\n");
		comments.append(Globals.APPLICATION_THANKS + "\n\n");
		comments.append(Globals.APPLICATION_PUBLIC_DOMAIN_NOTICE);
		this.mBottomTextArea.setText(comments.toString());
		
		this.mBottomPanel.add(new JScrollPane(this.mBottomTextArea,
								                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
								                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED));
		
		this.mBottomTextArea.setCaretPosition(0);
		return;
	}
	
	public void disposeWindow()
	{
		this.dispose();
		return;
	}
}
