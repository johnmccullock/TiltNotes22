package main;

import util.ListFilesInFolder;
import util.ReadWriteFile;

import java.io.File;

import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * Utility functions related to opening and saving recipient database files.
 * @author John McCullock
 * @version 1.0 2013-10-22
 */
public class RecipientsFileFactory
{
	/**
	 * Displays a JFileChooser to get a .recipient file, and retrieves its contents.
	 * @param parent JFrame used as host for JFileChooser.
	 * @return String containing contents of chosen .recipient file, or null if cancelled or exception thrown.
	 * @throws Exception can be thrown on file i/o errors.
	 */
	public static String openRecipientsDatabaseFile(final JFrame parent) throws Exception
	{
		String rawText = null;
		
		try{
			JFileChooser fc = new JFileChooser();
			fc.setDialogTitle("Open Database From File");
			fc.setCurrentDirectory(new File("."));
			fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
			final FileNameExtensionFilter filter = new FileNameExtensionFilter("TiltNotes recipients", "recipients");
			fc.setFileFilter(filter);
			fc.setAcceptAllFileFilterUsed(false);
			int response = fc.showOpenDialog(parent);
			if(response == JFileChooser.APPROVE_OPTION){
				File aFile = fc.getSelectedFile();
				if(aFile != null){
					rawText = ReadWriteFile.read(aFile);
				}
			}
		}catch(Exception ex){
			throw ex;
		}
		
		return rawText;
	}
	
	/**
	 * Displays a JFileChooser to get a new file name.  Checks for duplicate file names in current directory.
	 * If a name is found to be duplicate, the JFileChooser is repeatedly shown again until a unique name is
	 * chosen or the user cancels.
	 * @param parent JFrame used as host for JFileChooser.
	 * @return File object obtained from JFileChooser, or null if cancelled or exception thrown.
	 * @throws Exception can be thrown on file i/o errors.
	 */
	public static File getFilenameForRecipientsDatabase(final JFrame parent) throws Exception
	{
		File recipientsFile = null;
		
		try{
			boolean done = false;
			boolean cancelled = false;
			JFileChooser fc = new JFileChooser();
			fc.setDialogTitle("Save Database To File");
			fc.setCurrentDirectory(new File("."));
			fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
			final FileNameExtensionFilter filter = new FileNameExtensionFilter("TiltNotes recipients", "recipients");
			fc.setFileFilter(filter);
			fc.setAcceptAllFileFilterUsed(false);
			
			while(!done)
			{
				cancelled = false;
				int response = fc.showSaveDialog(parent);
				if(response == JFileChooser.APPROVE_OPTION){
					String newName = fc.getSelectedFile().getName();
					
					// Just in case the user hasn't already included the ".recipients" file name extension.
					String test = newName;
					if(!newName.endsWith(".recipients")){
						test = test + ".recipients";
					}
					
					if(RecipientsFileFactory.newNameIsDuplicate(test, fc.getCurrentDirectory())){
						int response2 = JOptionPane.showConfirmDialog(parent, "A file by this name, " + test + 
																	", already exists.\n\n" +
																	"Do you want to overwrite the existing file?", 
																	"Duplicate File Name", 
																	JOptionPane.YES_NO_OPTION, 
																	JOptionPane.WARNING_MESSAGE);
						if(response2 == JOptionPane.YES_OPTION){
							done = true;
						}else{
							cancelled = true;
						}
					}else{
						done = true;
					}
				}else{
					cancelled = true;
					done = true;
				}
			}
			
			if(done && !cancelled){
				recipientsFile = fc.getSelectedFile();
			}
		}catch(Exception ex){
			throw ex;
		}
		
		return recipientsFile;
	}
	
	/**
	 * Checks newName against an array of names from currentDirectory.getAbsolutePath() for duplicate names.
	 * @param newName String candidate to be check for duplicate name.
	 * @param currentDirectory File object pointing to a directory to extract names from.
	 * @return boolean true if newName is a duplicate matching any file in currentDirectory.getAbsolutePath(), false if unique. 
	 * @throws Exception can be thrown on file i/o errors.
	 */
	private static boolean newNameIsDuplicate(final String newName, final File currentDirectory) throws Exception
	{
		boolean isDuplicate = false;
		try{
			String[] currentNames = ListFilesInFolder.getFileNamesInFolder(currentDirectory.getAbsolutePath());
			for(String name : currentNames)
			{
				if(newName.compareToIgnoreCase(name) == 0){
					isDuplicate = true;
					break;
				}
			}
		}catch(Exception ex){
			throw ex;
		}
		return isDuplicate;
	}
	
	/**
	 * Attempts to write text to a specified file.  In this context, the assumption is that the text is
	 * XML-formatted data, ready to be written.
	 * @param filePath File object with a pre-chosen file name.
	 * @param xml String containing XML-formatted data.
	 * @throws Exception the ReadWriteFile can throw IOExceptions and other Exceptions.
	 */
	public static void writeRecipientsTreeMapToFile(final File filePath, final String xml) throws Exception
	{
		try{
			// If the user hasn't already appended the name with the ".recipients" extension, append it.
			if(filePath.getAbsolutePath().endsWith(".recipients")){
				ReadWriteFile.write(filePath.getAbsolutePath(), xml);
			}else{
				ReadWriteFile.write(filePath.getAbsolutePath() + ".recipients", xml);
			}
		}catch(Exception ex){
			throw ex;
		}
		return;
	}
}
