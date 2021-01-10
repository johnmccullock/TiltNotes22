package util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateFormatTool
{
	public DateFormatTool() { return; }
	
	public static String getSimpleDateTime(Date aDate) throws Exception, NullPointerException, IllegalArgumentException
	{
		String dateString = null;
		try{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			dateString = sdf.format(aDate);
		}catch(NullPointerException npe){
			throw npe;
		}catch(IllegalArgumentException iae){
			throw iae;
		}catch(Exception ex){
			throw ex;
		}
		return dateString;
	}
	
	public static String getSimple24HourTime(Date aDate) throws Exception, NullPointerException, IllegalArgumentException
	{
		String dateString = null;
		try{
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
			dateString = sdf.format(aDate);
		}catch(NullPointerException npe){
			throw npe;
		}catch(IllegalArgumentException iae){
			throw iae;
		}catch(Exception ex){
			throw ex;
		}
		return dateString;
	}
	
	public static String getSimple12HourTime(Date aDate) throws Exception, NullPointerException, IllegalArgumentException
	{
		String dateString = null;
		try{
			SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss a");
			dateString = sdf.format(aDate);
		}catch(NullPointerException npe){
			throw npe;
		}catch(IllegalArgumentException iae){
			throw iae;
		}catch(Exception ex){
			throw ex;
		}
		return dateString;
	}
	
	public static Date getDateFromSimpleDate(String simple) throws Exception
	{
		Date aDate = null;
		
		try{
			int[] values = DateFormatTool.parseSimpleDate(simple);
			GregorianCalendar gc = new GregorianCalendar(values[0], values[1], values[2], values[3], values[4], values[5]);
			aDate = gc.getTime();
		}catch(Exception ex){
			throw ex;
		}
		
		return aDate;
	}
	
	private static int[] parseSimpleDate(String simple) throws Exception
	{
		int[] values = null;
		
		try{
			String[] halves = simple.split(" ");
			String[] dateValues = halves[0].split("-");
			String[] timeValues = halves[1].split(":");
			values = new int[6];
			values[0] = Integer.parseInt(dateValues[0]);
			values[1] = Integer.parseInt(dateValues[1]);
			values[2] = Integer.parseInt(dateValues[2]);
			values[3] = Integer.parseInt(timeValues[0]);
			values[4] = Integer.parseInt(timeValues[1]);
			values[5] = Integer.parseInt(timeValues[2]);
		}catch(Exception ex){
			throw ex;
		}
		
		return values;
	}
	
	
}
