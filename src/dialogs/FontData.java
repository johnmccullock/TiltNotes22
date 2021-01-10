package dialogs;

public class FontData
{
	public String fontFace = null;
	public int fontSize = 10;
	public boolean isBold = false;
	public boolean isItalic = false;
	public boolean isUnderline = false;
	public boolean isStrikeout = false;
	public int redValue = 0;
	public int greenValue = 0;
	public int blueValue = 0;
	
	public FontData() { return; }
	
	public FontData(String face, int size, boolean bold, boolean italic, boolean underline, boolean strikeout)
	{
		this.fontFace = face;
		this.fontSize = size;
		this.isBold = bold;
		this.isItalic = italic;
		this.isUnderline = underline;
		this.isStrikeout = strikeout;
		return;
	}
	
	public FontData(String face, int size, boolean bold, boolean italic, boolean underline, boolean strikeout, int red, int green, int blue)
	{
		this.fontFace = face;
		this.fontSize = size;
		this.isBold = bold;
		this.isItalic = italic;
		this.isUnderline = underline;
		this.isStrikeout = strikeout;
		this.redValue = red;
		this.greenValue = green;
		this.blueValue = blue;
		return;
	}
}
