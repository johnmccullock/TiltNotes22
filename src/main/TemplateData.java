package main;

import java.util.ArrayList;

public class TemplateData
{
	private String mTemplateName = null;
	private int mNoteColorRed = 0;
	private int mNoteColorGreen = 0;
	private int mNoteColorBlue = 0;
	private int mTitleColorRed = 0;
	private int mTitleColorGreen = 0;
	private int mTitleColorBlue = 0;
	private int mBorderColorRed = 0;
	private int mBorderColorGreen = 0;
	private int mBorderColorBlue = 0;
	private int mBorderWidth = 0;
	private String mNoteFontFace = null;
	private int mNoteFontSize = 12;
	private boolean mNoteFontBold = false;
	private boolean mNoteFontItalic = false;
	private boolean mNoteFontUnderline = false;
	private String mTitleFontFace = null;
	private int mTitleFontSize = 10;
	private boolean mTitleFontBold = false;
	private boolean mTitleFontItalic = false;
	private int mNoteFontColorRed = 0;
	private int mNoteFontColorGreen = 0;
	private int mNoteFontColorBlue = 0;
	private int mTitleFontColorRed = 0;
	private int mTitleFontColorGreen = 0;
	private int mTitleFontColorBlue = 0;
	private int mNoteWidth = 0;
	private int mNoteHeight = 0;
	
	private String mCustomTitle = null;
	private SessionPriority mPriority = null;
	private ArrayList<String> mRecipients = null;
	private String mEntry = null;
	
	public TemplateData()
	{
		
		return;
	}
	
	public void setTemplateName(String name)
	{
		try{
			if(name == null){
				throw new Exception("Parameter cannot be null.");
			}
			this.mTemplateName = name;
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return;
	}
	
	public String getTemplateName()
	{
		return this.mTemplateName;
	}
	
	public void setNoteColorRed(int redValue)
	{
		try{
			if(redValue < 0 || redValue > 255){
				throw new Exception("Parameter is out of range: " + redValue);
			}
			this.mNoteColorRed = redValue;
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return;
	}
	
	public int getNoteColorRed()
	{
		return this.mNoteColorRed;
	}
	
	public void setNoteColorGreen(int greenValue)
	{
		try{
			if(greenValue < 0 || greenValue > 255){
				throw new Exception("Parameter is out of range: " + greenValue);
			}
			this.mNoteColorGreen = greenValue;
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return;
	}
	
	public int getNoteColorGreen()
	{
		return this.mNoteColorGreen;
	}
	
	public void setNoteColorBlue(int blueValue)
	{
		try{
			if(blueValue < 0 || blueValue > 255){
				throw new Exception("Parameter is out of range: " + blueValue);
			}
			this.mNoteColorBlue = blueValue;
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return;
	}
	
	public int getNoteColorBlue()
	{
		return this.mNoteColorBlue;
	}
	
	public void setTitleColorRed(int redValue)
	{
		try{
			if(redValue < 0 || redValue > 255){
				throw new Exception("Parameter is out of range: " + redValue);
			}
			this.mTitleColorRed = redValue;
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return;
	}
	
	public int getTitleColorRed()
	{
		return this.mTitleColorRed;
	}
	
	public void setTitleColorGreen(int greenValue)
	{
		try{
			if(greenValue < 0 || greenValue > 255){
				throw new Exception("Parameter is out of range: " + greenValue);
			}
			this.mTitleColorGreen = greenValue;
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return;
	}
	
	public int getTitleColorGreen()
	{
		return this.mTitleColorGreen;
	}
	
	public void setTitleColorBlue(int blueValue)
	{
		try{
			if(blueValue < 0 || blueValue > 255){
				throw new Exception("Parameter is out of range: " + blueValue);
			}
			this.mTitleColorBlue = blueValue;
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return;
	}
	
	public int getTitleColorBlue()
	{
		return this.mTitleColorBlue;
	}
	
	public void setBorderColorRed(final int redValue)
	{
		try{
			if(redValue < 0 || redValue > 255){
				throw new Exception("Parameter is out of range: " + redValue);
			}
			this.mBorderColorRed = redValue;
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return;
	}
	
	public int getBorderColorRed()
	{
		return this.mBorderColorRed;
	}
	
	public void setBorderColorGreen(final int greenValue)
	{
		try{
			if(greenValue < 0 || greenValue > 255){
				throw new Exception("Parameter is out of range: " + greenValue);
			}
			this.mBorderColorGreen = greenValue;
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return;
	}
	
	public int getBorderColorGreen()
	{
		return this.mBorderColorGreen;
	}
	
	public void setBorderColorBlue(final int blueValue)
	{
		try{
			if(blueValue < 0 || blueValue > 255){
				throw new Exception("Parameter is out of range: " + blueValue);
			}
			this.mBorderColorBlue = blueValue;
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return;
	}
	
	public int getBorderColorBlue()
	{
		return this.mBorderColorBlue;
	}
	
	public void setBorderWidth(final int borderWidth)
	{
		try{
			if(borderWidth < 0 || borderWidth > 20){
				throw new Exception("Parameter is out of range: " + borderWidth);
			}
			this.mBorderWidth = borderWidth;
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return;
	}
	
	public int getBorderWidth()
	{
		return this.mBorderWidth;
	}
	
	public void setNoteFontFace(String noteFont)
	{
		try{
			if(noteFont == null || noteFont.isEmpty()){
				throw new Exception("Parameter cannot be null or empty.");
			}
			this.mNoteFontFace = noteFont;
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return;
	}
	
	public String getNoteFontFace()
	{
		return this.mNoteFontFace;
	}
	
	public void setNoteFontSize(int fontSize)
	{
		try{
			if(fontSize <= 0){
				throw new Exception("Parameter must be greater than zero.");
			}
			this.mNoteFontSize = fontSize;
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return;
	}
	
	public int getNoteFontSize()
	{
		return this.mNoteFontSize;
	}
	
	public void setNoteFontBold(boolean isBold)
	{
		this.mNoteFontBold = isBold;
	}
	
	public boolean getNoteFontBold()
	{
		return this.mNoteFontBold;
	}
	
	public void setNoteFontItalic(boolean isItalic)
	{
		this.mNoteFontItalic = isItalic;
		return;
	}
	
	public boolean getNoteFontItalic()
	{
		return this.mNoteFontItalic;
	}
	
	public void setNoteFontUnderline(boolean isUnderline)
	{
		this.mNoteFontUnderline = isUnderline;
		return;
	}
	
	public boolean getNoteFontUnderline()
	{
		return this.mNoteFontUnderline;
	}
	
	public void setTitleFontFace(String titleFont)
	{
		try{
			if(titleFont == null || titleFont.isEmpty()){
				throw new Exception("Parameter cannot be null or empty.");
			}
			this.mTitleFontFace = titleFont;
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return;
	}
	
	public String getTitleFontFace()
	{
		return this.mTitleFontFace;
	}
	
	public void setTitleFontSize(int fontSize)
	{
		try{
			if(fontSize <= 0){
				throw new Exception("Parameter must be greater than zero.");
			}
			this.mTitleFontSize = fontSize;
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return;
	}
	
	public int getTitleFontSize()
	{
		return this.mTitleFontSize;
	}
	
	public void setTitleFontBold(boolean isBold)
	{
		this.mTitleFontBold = isBold;
		return;
	}
	
	public boolean getTitleFontBold()
	{
		return this.mTitleFontBold;
	}
	
	public void setTitleFontItalic(boolean isItalic)
	{
		this.mTitleFontItalic = isItalic;
		return;
	}
	
	public boolean getTitleFontItalic()
	{
		return this.mTitleFontItalic;
	}
	
	public void setNoteFontColorRed(int redValue)
	{
		try{
			if(redValue < 0 || redValue > 255){
				throw new Exception("Parameter is out of range: " + redValue);
			}
			this.mNoteFontColorRed = redValue;
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return;
	}
	
	public int getNoteFontColorRed()
	{
		return this.mNoteFontColorRed;
	}
	
	public void setNoteFontColorGreen(int greenValue)
	{
		try{
			if(greenValue < 0 || greenValue > 255){
				throw new Exception("Parameter is out of range: " + greenValue);
			}
			this.mNoteFontColorGreen = greenValue;
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return;
	}
	
	public int getNoteFontColorGreen()
	{
		return this.mNoteFontColorGreen;
	}
	
	public void setNoteFontColorBlue(int blueValue)
	{
		try{
			if(blueValue < 0 || blueValue > 255){
				throw new Exception("Parameter is out of range: " + blueValue);
			}
			this.mNoteFontColorBlue = blueValue;
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return;
	}
	
	public int getNoteFontColorBlue()
	{
		return this.mNoteFontColorBlue;
	}
	
	public void setTitleFontColorRed(int redValue)
	{
		try{
			if(redValue < 0 || redValue > 255){
				throw new Exception("Parameter is out of range: " + redValue);
			}
			this.mTitleFontColorRed = redValue;
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return;
	}
	
	public int getTitleFontColorRed()
	{
		return this.mTitleFontColorRed;
	}
	
	public void setTitleFontColorGreen(int greenValue)
	{
		try{
			if(greenValue < 0 || greenValue > 255){
				throw new Exception("Parameter is out of range: " + greenValue);
			}
			this.mTitleFontColorGreen = greenValue;
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return;
	}
	
	public int getTitleFontColorGreen()
	{
		return this.mTitleFontColorGreen;
	}
	
	public void setTitleFontColorBlue(int blueValue)
	{
		try{
			if(blueValue < 0 || blueValue > 255){
				throw new Exception("Parameter is out of range: " + blueValue);
			}
			this.mTitleFontColorBlue = blueValue;
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return;
	}
	
	public int getTitleFontColorBlue()
	{
		return this.mTitleFontColorBlue;
	}
	
	public void setNoteWidth(int width)
	{
		this.mNoteWidth = width;
		return;
	}
	
	public int getNoteWidth()
	{
		return this.mNoteWidth;
	}
	
	public void setNoteHeight(int height)
	{
		this.mNoteHeight = height;
		return;
	}
	
	public int getNoteHeight()
	{
		return this.mNoteHeight;
	}
	
	// custom title can be null.
	public void setCustomTitle(final String title)
	{
		this.mCustomTitle = title;
		return;
	}
	
	public String getCustomTitle()
	{
		return this.mCustomTitle;
	}
	
	public void setSessionPriority(SessionPriority priority)
	{
		try{
			if(priority == null){
				throw new Exception("Parameter cannot be null.");
			}
			this.mPriority = priority;
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return;
	}
	
	public SessionPriority getSessionPriority()
	{
		return this.mPriority;
	}
	
	public void setRecipients(ArrayList<String> recipients)
	{
		try{
			if(recipients == null){
				throw new Exception("Parameter cannot be null.");
			}
			this.mRecipients = recipients;
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return;
	}
	
	public ArrayList<String> getRecipients()
	{
		return this.mRecipients;
	}
	
	public void setEntry(String entry)
	{
		try{
			if(entry == null){
				throw new Exception("Parameter cannot be null.");
			}
			this.mEntry = entry;
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return;
	}
	
	public String getEntry()
	{
		return this.mEntry;
	}
}
