package dialogs;

import java.awt.Color;
import java.awt.Component;
import java.awt.SystemColor;

import javax.swing.JList;
import javax.swing.ListCellRenderer;

public class EmoticonListCellRenderer implements ListCellRenderer
{
	//private static final Color SELECTED = new Color(86, 112, 204);
	private static final Color SELECTED = SystemColor.textHighlight;
	
	public Component getListCellRendererComponent(JList list, 
												  Object value, 
												  int index, 
												  boolean isSelected, 
												  boolean cellHasFocus)
	{
		Component component = (Component)value;
		component.setBackground(isSelected ? SELECTED : Color.white);
		component.setForeground(isSelected ? Color.white : SELECTED);
		return component;
	}
}
