package main;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Data class representing a text messaging session.
 * Each SessionGUI gets one SessionData object with its own ID (UUID) to differentiate it from the others.
 * @author John McCullock
 * @version 2.1 09/16/2013
 *
 */
public class SessionData
{
	private UUID mID = null;
	private long mModified = 0; // Last modified.
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
	private ArrayList<String> mRecipients = null;
	private ArrayList<Entry> mEntries = null;
	
	private long mHiddenTime = 0;
	private String mHiddenEvent = null;
	private long mDeletedTime = 0;
	private String mDeletionCommand = null;
	private String mDeletionSource = null;
	private SessionPriority mPriority = null;
	private SessionMode mMode = null;
	private TemplateEditMode mEditMode = null;
	private String mTemplateName = null;
	
	/**
	 * Creates a new SessionData object.
	 * @param id a UUID uniquely identifying this object.
	 * @param modified a long representation of the current time.
	 * @param recipients ArrayList<String> of recipient names and or groups
	 * @param entries ArrayList<Entry> of Entry objects detailing the messaging session among users.
	 * @see Entry
	 */
	public SessionData(final UUID id, final long modified, final ArrayList<String> recipients, final ArrayList<Entry> entries)
	{
		this.setID(id);
		this.setModified(modified);
		this.setRecipients(recipients);
		this.setEntries(entries);
		return;
	}
	
	public void setID(final UUID id)
	{
		try{
			if(id == null){
				throw new Exception("Parameter cannot be null.");
			}
			this.mID = id;
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return;
	}
	
	public UUID getID()
	{
		return this.mID;
	}
	
	public void setModified(final long modified)
	{
		try{
			if(modified <= 0){
				throw new Exception("Value must be larger than zero.");
			}
			this.mModified = modified;
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return;
	}
	
	public long getModified()
	{
		return this.mModified;
	}
	
	public void setNoteColorRed(final int redValue)
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
	
	public void setNoteColorGreen(final int greenValue)
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
	
	public void setNoteColorBlue(final int blueValue)
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
	
	public void setTitleColorRed(final int redValue)
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
	
	public void setTitleColorGreen(final int greenValue)
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
	
	public void setTitleColorBlue(final int blueValue)
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
	
	public void setNoteFontFace(final String noteFont)
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
	
	public void setNoteFontSize(final int fontSize)
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
	
	public void setNoteFontBold(final boolean isBold)
	{
		this.mNoteFontBold = isBold;
	}
	
	public boolean getNoteFontBold()
	{
		return this.mNoteFontBold;
	}
	
	public void setNoteFontItalic(final boolean isItalic)
	{
		this.mNoteFontItalic = isItalic;
		return;
	}
	
	public boolean getNoteFontItalic()
	{
		return this.mNoteFontItalic;
	}
	
	public void setNoteFontUnderline(final boolean isUnderline)
	{
		this.mNoteFontUnderline = isUnderline;
		return;
	}
	
	public boolean getNoteFontUnderline()
	{
		return this.mNoteFontUnderline;
	}
	
	public void setTitleFontFace(final String titleFont)
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
	
	public void setTitleFontSize(final int fontSize)
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
	
	public void setTitleFontBold(final boolean isBold)
	{
		this.mTitleFontBold = isBold;
		return;
	}
	
	public boolean getTitleFontBold()
	{
		return this.mTitleFontBold;
	}
	
	public void setTitleFontItalic(final boolean isItalic)
	{
		this.mTitleFontItalic = isItalic;
		return;
	}
	
	public boolean getTitleFontItalic()
	{
		return this.mTitleFontItalic;
	}
	
	public void setNoteFontColorRed(final int redValue)
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
	
	public void setNoteFontColorGreen(final int greenValue)
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
	
	public void setNoteFontColorBlue(final int blueValue)
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
	
	public void setTitleFontColorRed(final int redValue)
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
	
	public void setTitleFontColorGreen(final int greenValue)
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
	
	public void setTitleFontColorBlue(final int blueValue)
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
	
	public void setNoteWidth(final int width)
	{
		this.mNoteWidth = width;
		return;
	}
	
	public int getNoteWidth()
	{
		return this.mNoteWidth;
	}
	
	public void setNoteHeight(final int height)
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
	
	// recipients can be null or empty.
	public void setRecipients(final ArrayList<String> recipients)
	{
		this.mRecipients = recipients;
		return;
	}
	
	public ArrayList<String> getRecipients()
	{
		return this.mRecipients;
	}
	
	public void setEntries(final ArrayList<Entry> entries)
	{
		try{
			if(entries == null){
				throw new Exception("Parameter cannot be null.");
			}
			this.mEntries = entries;
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return;
	}
	
	public ArrayList<Entry> getEntries()
	{
		return this.mEntries;
	}
	
	public void setHiddenTime(final long hidden)
	{
		try{
			if(hidden < 0){
				throw new Exception("Parameter must be a positive number: " + hidden);
			}
			this.mHiddenTime = hidden;
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return;
	}
	
	public long getHiddenTime()
	{
		return this.mHiddenTime;
	}
	
	// eventType can be null.
	public void setHiddenEvent(final String eventType)
	{
		try{
			this.mHiddenEvent = eventType;
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return;
	}
	
	public String getHiddenEvent()
	{
		return this.mHiddenEvent;
	}
	
	public void setDeletionTime(final long deletion)
	{
		try{
			if(deletion < 0){
				throw new Exception("Parameter must be a positive number: " + deletion);
			}
			this.mDeletedTime = deletion;
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return;
	}
	
	public long getDeletionTime()
	{
		return this.mDeletedTime;
	}
	
	// command can be null.
	public void setDeletionCommand(final String command)
	{
		try{
			this.mDeletionCommand = command;
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return;
	}
	
	public String getDeletionCommand()
	{
		return this.mDeletionCommand;
	}
	
	// source can be null.
	public void setDeletionSource(final String source)
	{
		try{
			this.mDeletionSource = source;
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return;
	}
	
	public String getDeletionSource()
	{
		return this.mDeletionSource;
	}
	
	public void setSessionPriority(SessionPriority priority)
	{
		try{
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
	
	public void setSessionMode(SessionMode mode)
	{
		try{
			this.mMode = mode;
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return;
	}
	
	public SessionMode getSessionMode()
	{
		return this.mMode;
	}
	
	public void setTemplateEditMode(TemplateEditMode editMode)
	{
		try{
			this.mEditMode = editMode;
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return;
	}
	
	public TemplateEditMode getTemplateEditMode()
	{
		return this.mEditMode;
	}
	
	public void setTemplateName(String name)
	{
		this.mTemplateName = name;
		return;
	}
	
	public String getTemplateName()
	{
		return this.mTemplateName;
	}
}
