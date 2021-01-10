package util;

import java.awt.Font;

public class FontTool
{
	public static Font getFont(String face, int size, boolean isBold, boolean isItalic)
	{
		Font aFont = null;
		
		if(isBold && isItalic){
			aFont = new Font(face, Font.BOLD + Font.ITALIC, size);
		}else if(isBold && !isItalic){
			aFont = new Font(face, Font.BOLD, size);
		}else if(!isBold && isItalic){
			aFont = new Font(face, Font.ITALIC, size);
		}else{
			aFont = new Font(face, Font.PLAIN, size);
		}
		
		return aFont;
	}
}
