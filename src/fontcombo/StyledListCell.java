package fontcombo;

import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class StyledListCell extends JPanel
{
	public static enum CellAlign{LEFT, CENTER, RIGHT};
	
	private JLabel mCellLabel = null;
	private Insets mCellInsets = new Insets(0, 0, 0, 0);
	private Font mCellFont = new Font(Font.SANS_SERIF, Font.PLAIN, 14);
	private StyledListCell.CellAlign mCellAlign = StyledListCell.CellAlign.LEFT;
	private Object mCellValue = null;
	private Color mCellForeground = Color.BLACK;
	private Color mCellBackground = Color.WHITE;
	
	public StyledListCell() { return; }
	
	// value cannot be null.
	// All other parameters can be null.
	public StyledListCell(Object value, StyledListCell.CellAlign align, Insets insets, Font font)
	{
		this.setCellValue(value);
		this.setCellInsets(insets);
		this.setCellFont(font);
		this.setCellAlignment(align);
		try{
			this.createNewPanel();
		}catch(Exception ex){
			throw ex;
		}
		return;
	}
	
	// value cannot be null.
	// All other parameters can be null.
	public StyledListCell(Object value, StyledListCell.CellAlign align, Insets insets, Font font, Color foreground, Color background)
	{
		this.setCellValue(value);
		this.setCellInsets(insets);
		this.setCellFont(font);
		this.setCellAlignment(align);
		this.setCellForeground(foreground);
		this.setCellBackground(background);
		try{
			this.createNewPanel();
		}catch(Exception ex){
			throw ex;
		}
		return;
	}
	
	public void createNewPanel()
	{
		this.setForeground(this.mCellForeground);
		this.setBackground(this.mCellBackground);
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		this.setBorder(BorderFactory.createEmptyBorder(this.mCellInsets.top, this.mCellInsets.left, this.mCellInsets.bottom, this.mCellInsets.right));
		try{
			this.mCellLabel = new JLabel(this.mCellValue.toString());
			this.mCellLabel.setFont(this.mCellFont);
			this.mCellLabel.setHorizontalTextPosition(JLabel.CENTER);
			this.mCellLabel.setVerticalTextPosition(JLabel.CENTER);
			if(this.mCellAlign.compareTo(StyledListCell.CellAlign.RIGHT) == 0 || this.mCellAlign.compareTo(StyledListCell.CellAlign.CENTER) == 0){
				this.add(Box.createVerticalStrut(4));
			}
			this.add(this.mCellLabel);
			if(this.mCellAlign.compareTo(StyledListCell.CellAlign.LEFT) == 0 || this.mCellAlign.compareTo(StyledListCell.CellAlign.CENTER) == 0){
				this.add(Box.createVerticalStrut(4));
			}
			this.setRequestFocusEnabled(true);
		}catch(Exception ex){
			throw ex;
		}
		
		return;
	}
	
	public void setCellValue(Object value)
	{
		try{
			if(value == null){
				throw new Exception("Parameter cannot be null.");
			}
			this.mCellValue = value;
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return;
	}
	
	public Object getCellValue()
	{
		return this.mCellValue;
	}
	
	public void setCellAlignment(StyledListCell.CellAlign align)
	{
		if(align != null){
			this.mCellAlign = align;
		}
		return;
	}
	
	public void setCellInsets(Insets insets)
	{
		if(insets != null){
			this.mCellInsets = insets;
		}
		return;
	}
	
	public void setCellFont(Font font)
	{
		if(font != null){
			this.mCellFont = font;
		}
		return;
	}
	
	public void setCellForeground(Color foreground)
	{
		if(foreground != null){
			this.mCellForeground = foreground;
		}
		return;
	}
	
	public void setCellBackground(Color background)
	{
		if(background != null){
			this.mCellBackground = background;
		}
		return;
	}
	
	public JLabel getLabel()
	{
		return this.mCellLabel;
	}
}
