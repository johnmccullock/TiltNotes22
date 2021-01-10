package main;

import util.ReadWriteFile;

import java.io.File;
import java.util.ArrayList;

import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * Utility function for saving templates to file.
 * @author John McCullock
 * @version 1.0 2013-10-22
 * @see SessionPriority
 */
public class TemplateFactory
{
	/**
	 * Queries the user with a JOptionPane for a SessionPriority value.
	 * @param parent JFrame as host to JOptionPane.
	 * @return SessionPriority value if chosen, or null if cancelled.
	 */
	public static SessionPriority getPrioritySettingForTemplate(final JFrame parent)
	{
		SessionPriority priority = null;
		Object[] options = {"Normal", "Urgent", "Cancel"};
		int response = JOptionPane.showOptionDialog(parent,
												"What priority setting should this note have:  Normal or Urgent?\n\n" +
												"Normal priority notes can use carets \"^\" for user entry, Urgent notes cannot.",
												"Priority Setting",
												JOptionPane.YES_NO_CANCEL_OPTION,
												JOptionPane.QUESTION_MESSAGE,
												null,
												options,
												options[0]);
		if(response == JOptionPane.YES_OPTION){
			priority = SessionPriority.NORMAL;
		}else if(response == JOptionPane.NO_OPTION){
			priority = SessionPriority.URGENT;
		}
		return priority;
	}
	
	/**
	 * Prepares a list of recipient names for use in showRecipientsDialog method.
	 * @param parent JFrame as host to JOptionPane used in showRecipientsDialog method.
	 * @param recipients ArrayList<String> containing recipient names.
	 * @return String chosen name, or null if cancelled.
	 */
	public static String getRecipientsForTemplate(final JFrame parent, final ArrayList<String> recipients)
	{
		String listName = null;
		int count = recipients.size();
		Object[] possibilities = new Object[count - 1];
		for(int i = 0; i < count; i++)
		{
			if(recipients.get(i).compareToIgnoreCase("_network") != 0){
				possibilities[i] = recipients.get(i);
			}
		}
		
		listName = TemplateFactory.showRecipientsDialog(parent, possibilities);
		
		return listName;
	}
	
	/**
	 * Displays a JOptionPane with a list of names to choose from.
	 * @param parent JFrame as host to JOptionPane
	 * @param recipients Object[] containing the names to appear in the list.
	 * @return String chosen name, or null if cancelled.
	 */
	private static String showRecipientsDialog(final JFrame parent, final Object[] recipients)
	{
		String response = (String)JOptionPane.showInputDialog(parent,
											"Since the priority is Urgent,\n" + 
											"the note will be automatically sent to the recipient(s).\n\n" +
											"Who should the recipient(s) be?",
											"Choose Recipients",
											JOptionPane.QUESTION_MESSAGE,
											null,
											recipients,
											recipients[0]);
		return response;
	}
	
	/**
	 * Displays a JFileChooser to get a new file name.  Checks for duplicate names.
	 * If a name is found to be duplicate, the JFileChooser is repeatedly shown again until a unique name is
	 * chosen or the user cancels.
	 * @param parent JFrame used as host for JFileChooser.
	 * @param currentNames ArrayList<String> containing names to compare for duplicates.
	 * @return File object obtained from JFileChooser, or null if cancelled or exception thrown.
	 * @throws Exception can be thrown on file i/o errors.
	 */
	public static File getFilenameForTemplate(final JFrame parent, final ArrayList<String> currentNames)
	{
		boolean done = false;
		boolean cancelled = false;
		File templateFile = null;
		JFileChooser fc = new JFileChooser();
		fc.setDialogTitle("Save As Template");
		fc.setCurrentDirectory(new File("."));
		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		final FileNameExtensionFilter filter = new FileNameExtensionFilter("TiltNotes templates", "template");
		fc.setFileFilter(filter);
		fc.setAcceptAllFileFilterUsed(false);
		
		while(!done)
		{
			cancelled = false;
			int response = fc.showSaveDialog(parent);
			if(response == JFileChooser.APPROVE_OPTION){
				String test = fc.getSelectedFile().getName();
				if(TemplateFactory.newNameIsDuplicate(test, currentNames)){
					JOptionPane.showMessageDialog(parent, 
														"This name, " + test + ", already exists.\n\n" +
														"Please choose a different name.", 
														"Duplicate Name",
														JOptionPane.ERROR_MESSAGE);
				}else{
					done = true;
				}
			}else{
				cancelled = true;
				done = true;
			}
		}
		
		if(done && !cancelled){
			templateFile = fc.getSelectedFile();
		}
		
		return templateFile;
	}
	
	/**
	 * Compares newName to items in currentNames for any duplicates.
	 * @param newName String containing candidate.
	 * @param currentNames ArrayList<String> containing names for comparison.
	 * @return boolean true of match is found, false otherwise.
	 */
	private static boolean newNameIsDuplicate(final String newName, final ArrayList<String> currentNames)
	{
		boolean isDuplicate = false;
		for(String name : currentNames)
		{
			if(newName.compareToIgnoreCase(name) == 0){
				isDuplicate = true;
				break;
			}
		}
		return isDuplicate;
	}
	
	/**
	 * The encodedEntry String is expected to be base64 encoded when this method is used.
	 * @param name String template name.
	 * @param recipients ArrayList<String> recipients for this template
	 * @param priority SessionPriority value
	 * @param sData SessionData object
	 * @param encodedEntry String encoded to base64
	 * @return TemplateData object
	 * @throws Exception
	 */
	public static TemplateData createNewTemplateDataObject(final String name, 
															final ArrayList<String> recipients, 
															final SessionPriority priority, 
															final SessionData sData, 
															final String encodedEntry) throws Exception
	{
		TemplateData data = null;
		try{
			data = new TemplateData();
			data.setTemplateName(name);
			data.setRecipients(recipients);
			data.setSessionPriority(priority);
			data.setNoteColorRed(sData.getNoteColorRed());
			data.setNoteColorGreen(sData.getNoteColorGreen());
			data.setNoteColorBlue(sData.getNoteColorBlue());
			data.setTitleColorRed(sData.getTitleColorRed());
			data.setTitleColorGreen(sData.getTitleColorGreen());
			data.setTitleColorBlue(sData.getTitleColorBlue());
			data.setBorderColorRed(sData.getBorderColorRed());
			data.setBorderColorGreen(sData.getBorderColorGreen());
			data.setBorderColorBlue(sData.getBorderColorBlue());
			data.setBorderWidth(sData.getBorderWidth());
			data.setNoteFontFace(sData.getNoteFontFace());
			data.setNoteFontSize(sData.getNoteFontSize());
			data.setNoteFontBold(sData.getNoteFontBold());
			data.setNoteFontItalic(sData.getNoteFontItalic());
			data.setNoteFontUnderline(sData.getNoteFontUnderline());
			data.setTitleFontFace(sData.getTitleFontFace());
			data.setTitleFontSize(sData.getTitleFontSize());
			data.setTitleFontBold(sData.getTitleFontBold());
			data.setTitleFontItalic(sData.getTitleFontItalic());
			data.setNoteFontColorRed(sData.getNoteFontColorRed());
			data.setNoteFontColorGreen(sData.getNoteFontColorGreen());
			data.setNoteFontColorBlue(sData.getNoteFontColorBlue());
			data.setTitleFontColorRed(sData.getTitleFontColorRed());
			data.setTitleFontColorGreen(sData.getTitleFontColorGreen());
			data.setTitleFontColorBlue(sData.getTitleFontColorBlue());
			data.setNoteWidth(sData.getNoteWidth());
			data.setNoteHeight(sData.getNoteHeight());
			data.setCustomTitle(sData.getCustomTitle());
			data.setEntry(encodedEntry);
		}catch(Exception ex){
			throw ex;
		}
		return data;
	}
	
	/**
	 * Attempts to write text to a specified file.  In this context, the assumption is that the text is
	 * XML-formatted data, ready to be written.
	 * @param tData TemplateData complete with all necessary values.
	 * @param templateFile File object with chosen name and path.
	 * @throws Exception the ReadWriteFile can throw IOExceptions and other Exceptions.
	 */
	public static void writeTemplateToFile(final TemplateData tData, final File templateFile) throws Exception
	{
		try{
			String content = XMLTransact.parseTemplateDataToXML(tData);
			// If the user hasn't already appended the name with the template extension, append it.
			if(templateFile.getAbsolutePath().endsWith(".template")){
				ReadWriteFile.write(templateFile.getAbsolutePath(), content);
			}else{
				ReadWriteFile.write(templateFile.getAbsolutePath() + ".template", content);
			}
		}catch(Exception ex){
			throw ex;
		}
		return;
	}
}
