package fontcombo;

import java.awt.Color;
import java.awt.Component;
import java.awt.SystemColor;

import javax.swing.JList;
import javax.swing.ListCellRenderer;

public class FontListCellRenderer implements ListCellRenderer
{
	private Color mSelected = SystemColor.textHighlight;
	private Color mOpposite = SystemColor.window;
	
	public FontListCellRenderer(Color selected, Color opposite)
	{
		if(selected != null){
			this.mSelected = selected;
		}
		if(opposite != null){
			this.mOpposite = opposite;
		}
		return;
	}
	
	public Component getListCellRendererComponent(JList list, 
												  Object value, 
												  int index, 
												  boolean isSelected, 
												  boolean cellHasFocus)
	{
		Component component = (Component)value;
		component.setBackground(isSelected ? this.mSelected : this.mOpposite);
		component.setForeground(isSelected ? this.mOpposite : this.mSelected);
		return component;
	}
}
