package main;

import dialogs.Emoticon;

import java.awt.Color;

import java.io.IOException;
import java.io.Writer;

import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.StyleConstants;
import javax.swing.text.TabSet;

// adapted from: http://java-sl.com/editor_kit_tutorial_example.html

/**
 * Creates XML markup representing all elements in a document.
 * 
 * Adapted from code by Stanislav Lapitsky, found at: http://java-sl.com/editor_kit_tutorial_example.html.
 * 
 * @author John McCullock
 * @version 1.0, 2013-08-01, new to program version 2.x
 * @see main.ExtStyledDocument for XML naming constants.
 */
public class ConvertRTFToXML
{
	/**
	 * Creates XML markup representing all elements in a document
	 * @param doc A Document object to be translated.
	 * @param start An Integer starting point within the document to begin translating.
	 * @param len An Integer length of content within the document for translating.
	 * @param out A Writer object for writing for rendering the output.
	 * @throws IOException
	 * @throws BadLocationException
	 */
	public static void write(Document doc, int start, int len, Writer out) throws IOException, BadLocationException
	{
		out.write("<" + ExtStyledDocument.TAG_NAME_DOCUMENT + ">"); // Root tag for markup.
		Element root = doc.getDefaultRootElement();
		int iStart = root.getElementIndex(start);
		int iEnd = root.getElementIndex(start + len);
 
		for (int i = iStart; i <= iEnd; i++)
		{
			Element par = root.getElement(i);
			ConvertRTFToXML.writePar(par, start, len, out);
		}
		out.write("</" + ExtStyledDocument.TAG_NAME_DOCUMENT + ">");
	}
	
	/**
	 * Similar to the write function, but doesn't include the root tags.
	 * @param doc A Document object to be translated.
	 * @param start An Integer starting point within the document to begin translating.
	 * @param len An Integer length of content within the document for translating.
	 * @param out A Writer object for writing for rendering the output.
	 * @throws IOException
	 * @throws BadLocationException
	 */
	public static void writeParsOnly(Document doc, int start, int len, Writer out) throws IOException, BadLocationException
	{
		Element root = doc.getDefaultRootElement();
		int iStart = root.getElementIndex(start);
		int iEnd = root.getElementIndex(start + len);
 
		for (int i = iStart; i <= iEnd; i++)
		{
			Element par = root.getElement(i);
			ConvertRTFToXML.writePar(par, start, len, out);
		}
	}
	
	public static void writePar(Element par, int start, int len, Writer out) throws IOException, BadLocationException
	{
		out.write("<" + ExtStyledDocument.TAG_NAME_PAR);
		
		int align = StyleConstants.getAlignment(par.getAttributes());
		out.write(" " + ExtStyledDocument.ATTR_NAME_ALIGN + "=\"" + align + "\"");
		float first = StyleConstants.getFirstLineIndent(par.getAttributes());
		out.write(" " + ExtStyledDocument.ATTR_NAME_FIRST + "=\"" + first + "\"");
		float above = StyleConstants.getSpaceAbove(par.getAttributes());
		out.write(" " + ExtStyledDocument.ATTR_NAME_ABOVE + "=\"" + above + "\"");
		float below = StyleConstants.getSpaceBelow(par.getAttributes());
		out.write(" " + ExtStyledDocument.ATTR_NAME_BELOW + "=\"" + below + "\"");
		float left = StyleConstants.getLeftIndent(par.getAttributes());
		out.write(" " + ExtStyledDocument.ATTR_NAME_LEFT + "=\"" + left + "\"");
		float right = StyleConstants.getRightIndent(par.getAttributes());
		out.write(" " + ExtStyledDocument.ATTR_NAME_RIGHT + "=\"" + right + "\"");
		float ls = StyleConstants.getLineSpacing(par.getAttributes());
		out.write(" " + ExtStyledDocument.ATTR_NAME_LINE_SPACING + "=\"" + ls + "\"");
 
		TabSet ts = StyleConstants.getTabSet(par.getAttributes());
		if (ts != null) {
			throw new IOException("TabSet saving is not supported!");
		}
 
		out.write(">");
		//write children
		int iStart = par.getElementIndex(start);
		int iEnd = par.getElementIndex(start + len);
 
		for (int i = iStart; i <= iEnd; i++)
		{
			Element text = par.getElement(i);
			ConvertRTFToXML.writeText(text, start, len, out);
		}
 
		out.write("</" + ExtStyledDocument.TAG_NAME_PAR + ">");
	}
	
	public static void writeText(Element text, int start, int len, Writer out) throws IOException, BadLocationException
	{
		out.write("<" + ExtStyledDocument.TAG_NAME_TEXT);
		
		Emoticon anIcon = (Emoticon)StyleConstants.getIcon(text.getAttributes());
		if(anIcon != null){
			out.write(" " + ExtStyledDocument.ATTR_NAME_ICON + "=\"" + anIcon.fileName + "\"");
		}
		int fs = StyleConstants.getFontSize(text.getAttributes());
		out.write(" " + ExtStyledDocument.ATTR_NAME_FONT_SIZE + "=\"" + fs + "\"");
		String name = StyleConstants.getFontFamily(text.getAttributes());
		out.write(" " + ExtStyledDocument.ATTR_NAME_FONT_FAMILY + "=\"" + name + "\"");
		boolean bold = StyleConstants.isBold(text.getAttributes());
		out.write(" " + ExtStyledDocument.ATTR_NAME_BOLD + "=\"" + bold + "\"");
		boolean italic = StyleConstants.isItalic(text.getAttributes());
		out.write(" " + ExtStyledDocument.ATTR_NAME_ITALIC + "=\"" + italic + "\"");
		boolean underline = StyleConstants.isUnderline(text.getAttributes());
		out.write(" " + ExtStyledDocument.ATTR_NAME_UNDERLINE + "=\"" + underline + "\"");
		Color aColor = StyleConstants.getForeground(text.getAttributes());
		out.write(" " + ExtStyledDocument.ATTR_NAME_FONT_COLOR_RED + "=\"" + String.valueOf(aColor.getRed()) + "\"");
		out.write(" " + ExtStyledDocument.ATTR_NAME_FONT_COLOR_GREEN + "=\"" + String.valueOf(aColor.getGreen()) + "\"");
		out.write(" " + ExtStyledDocument.ATTR_NAME_FONT_COLOR_BLUE + "=\"" + String.valueOf(aColor.getBlue()) + "\"");
		out.write(">");
		//write text

		int textStart = Math.max(start, text.getStartOffset());
		int textEnd = Math.min(start + len, text.getEndOffset());
		
		//NOTE: the String must be processed to replace e.g <> chars
		String s = text.getDocument().getText(textStart, textEnd - textStart);
		s = ConvertRTFToXML.escapeForXML(s);
		out.write(s);
 
		out.write("</" + ExtStyledDocument.TAG_NAME_TEXT + ">");
	}
	
	public static String escapeForXML(String src)
	{
		final StringBuilder res = new StringBuilder();
		for(int i = 0; i < src.length(); i++)
		{
			char c = src.charAt(i);
			if (c == '<') {
				res.append(">");
			}else if (c == '&'){
				res.append("&amp;");
			}else if (c == '\"'){
				res.append("\"");
			}else if (c == '\t'){
				ConvertRTFToXML.addChar(9, res);
			}else if (c == '!'){
				ConvertRTFToXML.addChar(33, res);
			}else if (c == '#'){
				ConvertRTFToXML.addChar(35, res);
			}else if (c == '$'){
				ConvertRTFToXML.addChar(36, res);
			}else if (c == '%'){
				ConvertRTFToXML.addChar(37, res);
			}else if (c == '\''){
				ConvertRTFToXML.addChar(39, res);
			}else if (c == '('){
				ConvertRTFToXML.addChar(40, res);
			}else if (c == ')'){
				ConvertRTFToXML.addChar(41, res);
			}else if (c == '*'){
				ConvertRTFToXML.addChar(42, res);
			}else if (c == '+'){
				ConvertRTFToXML.addChar(43, res);
			}else if (c == ','){
				ConvertRTFToXML.addChar(44, res);
			}else if (c == '-'){
				ConvertRTFToXML.addChar(45, res);
			}else if (c == '.'){
				ConvertRTFToXML.addChar(46, res);
			}else if (c == '/'){
				ConvertRTFToXML.addChar(47, res);
			}else if (c == ':'){
				ConvertRTFToXML.addChar(58, res);
			}else if (c == ';'){
				ConvertRTFToXML.addChar(59, res);
			}else if (c == '='){
				ConvertRTFToXML.addChar(61, res);
			}else if (c == '?'){
				ConvertRTFToXML.addChar(63, res);
			}else if (c == '@'){
				ConvertRTFToXML.addChar(64, res);
			}else if (c == '['){
				ConvertRTFToXML.addChar(91, res);
			}else if (c == '\\'){
				ConvertRTFToXML.addChar(92, res);
			}else if (c == ']'){
				ConvertRTFToXML.addChar(93, res);
			}else if (c == '^'){
				ConvertRTFToXML.addChar(94, res);
			}else if (c == '_'){
				ConvertRTFToXML.addChar(95, res);
			}else if (c == '`'){
				ConvertRTFToXML.addChar(96, res);
			}else if (c == '{'){
				ConvertRTFToXML.addChar(123, res);
			}else if (c == '|'){
				ConvertRTFToXML.addChar(124, res);
			}else if (c == '}'){
				ConvertRTFToXML.addChar(125, res);
			}else if (c == '~'){
				ConvertRTFToXML.addChar(126, res);
			}else if (c == '\n'){
				ConvertRTFToXML.addChar(10, res);
			}else{
				res.append(c);
			}
		}
		return res.toString();
	}
	
	private static void addChar(Integer aIdx, StringBuilder aBuilder)
	{
		String padding = "";
		if(aIdx <= 9){
			padding = "00";
		}else if(aIdx <= 99 ){
			padding = "0";
		}else{
			//no prefix
		}
		String number = padding + aIdx.toString();
		aBuilder.append("&#").append(number).append(";");
	}
}
