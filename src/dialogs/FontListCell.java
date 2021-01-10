package dialogs;

import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class FontListCell extends JPanel
{
	private JLabel mCellLabel = null; 
	private Font mFont = null;
	
	public FontListCell()
	{
		super();
		return;
	}
	
	public FontListCell(Font font) throws Exception
	{
		super();
		this.setCellFont(font);
		try{
			this.createNewPanel();
		}catch(Exception ex){
			throw ex;
		}
		return;
	}
	
	public void createNewPanel() throws Exception
	{
		this.setForeground(Color.black);
		this.setBackground(Color.white);
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));
		
		try{
			this.mCellLabel = new JLabel(this.mFont.getFontName());
			this.mCellLabel.setFont(this.mFont);
			this.mCellLabel.setHorizontalTextPosition(JLabel.CENTER);
			this.mCellLabel.setVerticalTextPosition(JLabel.CENTER);
			this.add(this.mCellLabel);
			this.setRequestFocusEnabled(true);
		}catch(Exception ex){
			throw ex;
		}
		return;
	}
	
	public void setCellFont(Font font)
	{
		try{
			if(font == null){
				throw new Exception("Parameter cannot be null.");
			}
			this.mFont = font;
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return;
	}
	
	public Font getFont()
	{
		return this.mFont;
	}
	
	public JLabel getLabel()
	{
		return this.mCellLabel;
	}
}
