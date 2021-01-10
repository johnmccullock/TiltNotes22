package main.gui.text;

import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.InputEvent;

import java.io.IOException;

import javax.swing.JComponent;
import javax.swing.JTextPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.Style;
import javax.swing.text.StyledDocument;
import javax.swing.TransferHandler;

import java.util.List;

public class StyleTransferHandler extends TransferHandler
{
	// NOTE: the class "StyledString" must have appropriate package name attached.
	public String mimeType = DataFlavor.javaJVMLocalObjectMimeType + ";class=main.gui.text.StyledString";
	public DataFlavor styledStringFlavor;
 
	public StyleTransferHandler()
	{
		try{
			styledStringFlavor = new DataFlavor(mimeType);
		}catch(ClassNotFoundException e){
			System.out.println("Unable to create styledStringFlavor");
		}
	}
 
	public boolean canImport(JComponent comp, DataFlavor[] transferFlavors)
	{
		for(int j = 0; j < transferFlavors.length; j++) {
			if(styledStringFlavor.equals(transferFlavors[j]))
				return true;
		}
		return false;
	}
 
	protected Transferable createTransferable(JComponent c)
	{
		JTextPane textPane = (JTextPane)c;
		int start = textPane.getSelectionStart();
		int end = textPane.getSelectionEnd();
		StyledString ss = new StyledString("");
		if(start != -1 && start != end){
			String text = textPane.getSelectedText();
			ss = new StyledString(text);
			StyledDocument doc = textPane.getStyledDocument();
			extractAttributes(doc, start, end, ss);
		}
		return new StyledStringTransferable(ss);
	}
 
	private void extractAttributes(StyledDocument doc, int selectionStart, int selectionEnd, StyledString styledStr)
	{
		int pos = selectionStart;
		styledStr.logicalStyle = doc.getLogicalStyle(pos);
		while(pos < selectionEnd)
		{
			Element element = doc.getCharacterElement(pos);
			AttributeSet attrs = element.getAttributes();
			int endOffset = element.getEndOffset();
			int end = (endOffset < selectionEnd) ? endOffset : selectionEnd;
			styledStr.addAttributes(attrs, pos, end);
			pos = end;
		}
	}
 
	/**
	 * MOVE is not supported in superclass implementation
	 * and exportDone is implemented to do nothing - see api.
	 */
	public void exportAsDrag(JComponent comp, InputEvent e, int action)
	{
		super.exportAsDrag(comp, e, action);
		Clipboard clip = comp.getToolkit().getSystemClipboard();
		exportDone(comp, clip, action);
	}
 
	/**
	 * MOVE is not supported in superclass implementation
	 * and exportDone is implemented to do nothing - see api.
	 */
	public void exportToClipboard(JComponent comp, Clipboard clip, int action)
	{
		super.exportToClipboard(comp, clip, action);
		exportDone(comp, clip, action);
	}
 
	public void exportDone(JComponent comp, Clipboard clip, int action)
	{
		JTextPane textPane = (JTextPane)comp;
		if(action == MOVE){
			int offset = textPane.getSelectionStart();
			int length = textPane.getSelectionEnd() - offset;
			StyledDocument doc = textPane.getStyledDocument();
			try{
				doc.remove(offset, length);
			}catch(BadLocationException e){
				System.out.printf("BadLocation error: %s%n", e.getMessage());
			}
		}
	}
 
	public int getSourceActions(JComponent c)
	{
		return COPY_OR_MOVE;
	}
 
	public boolean importData(JComponent comp, Transferable t)
	{
		if(canImport(comp, t.getTransferDataFlavors())){
			StyledString styledStr = null;
			try{
				styledStr = (StyledString)t.getTransferData(styledStringFlavor);
				List<AttributeSet> attrs = styledStr.attrs;
				List<Location> locs = styledStr.locs;
				JTextPane textPane = (JTextPane)comp;
				int pos = textPane.getCaretPosition();
				StyledDocument doc = textPane.getStyledDocument();
				Style logicalStyle = styledStr.logicalStyle;
				// Insert the text.
				try{
					doc.insertString(pos, styledStr.text, logicalStyle);
				}catch(BadLocationException e){
					System.out.printf("BadLocation error: %s%n", e.getMessage());
				}
				// Apply the style runs to the inserted text.
				for(int j = 0; j < attrs.size(); j++)
				{
					AttributeSet as = attrs.get(j);
					Location loc = locs.get(j);
					doc.setCharacterAttributes(pos, loc.length, as, false);
					pos += loc.length;
				}
				return true;
			}catch(UnsupportedFlavorException ufe){
				System.out.println("importData UnsupportedFlavor: " + ufe.getMessage());
			} catch(IOException ioe){
				System.out.println("importData IO Error: " + ioe.getMessage());
			}
		}
		return false;
	}
	
	private class StyledStringTransferable implements Transferable
	{
		private StyledString styledString;
	 
		public StyledStringTransferable(StyledString ss)
		{
			styledString = ss;
		}
	 
		public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException
		{
			if(!isDataFlavorSupported(flavor))
				throw new UnsupportedFlavorException(flavor);
			
			return styledString;
		}
	 
		public DataFlavor[] getTransferDataFlavors()
		{
			return new DataFlavor[] { styledStringFlavor };
		}
	 
		public boolean isDataFlavorSupported(DataFlavor flavor)
		{
			return styledStringFlavor.equals(flavor);
		}
	 
		public String toString()
		{
			return "StyledStringTransferable: " + styledString;
		}
	}
}
