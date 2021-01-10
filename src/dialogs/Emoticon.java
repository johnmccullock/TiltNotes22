package dialogs;

import java.awt.Image;
import javax.swing.ImageIcon;

public class Emoticon extends ImageIcon
{
	public String fileName = null;
	
	public Emoticon(Image anImage, String fileName)
	{
		super(anImage);
		this.fileName = fileName;
	}
}
