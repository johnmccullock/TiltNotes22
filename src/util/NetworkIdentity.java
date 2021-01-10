package util;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class NetworkIdentity
{
	
	public static String getLocalHostName() throws UnknownHostException, Exception
	{
		String hostName = "";
		try{
			//Get an instance of InetAddress for the local computer.
			InetAddress localMachine = InetAddress.getLocalHost();
			
			//Get local host name string.
			hostName = localMachine.getHostName();
		}catch(UnknownHostException uhe){
			throw uhe;
		}catch(Exception ex){
			throw ex;
		}
		return hostName;
	}
	
	public static String getLocalHostIPAddress() throws Exception
	{
		String ipAddress = "";
		try{
			//Get an instance of InetAddress for the local computer.
			InetAddress localMachine = InetAddress.getLocalHost();  
		  
			//Get a string representation of the IP address.
			ipAddress = localMachine.getHostAddress();
		}catch(Exception ex){
			throw ex;
		}
		return ipAddress;
	}
}
