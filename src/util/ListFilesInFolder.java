package util;

import java.io.File;
import java.net.URL;

public class ListFilesInFolder
{
	public static String[] getFileNamesInFolder(String path) throws Exception
	{
		String[] itemsFound = null;
		try{
			File aFile = new File(path);
			if(aFile.isDirectory()){
				itemsFound = aFile.list();
			}
		}catch(Exception ex){
			throw ex;
		}
		return itemsFound;
	}
	
	public static String[] getFileNamesInFolder(URL path) throws Exception
	{
		String[] itemsFound = null;
		try{
			File aFile = new File(path.getPath());
			if(aFile.isDirectory()){
				itemsFound = aFile.list();
			}
		}catch(Exception ex){
			throw ex;
		}
		return itemsFound;
	}
}
