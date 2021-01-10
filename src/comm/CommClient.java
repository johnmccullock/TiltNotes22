package comm;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;

/**
 * A multicast client thread that listens for datagram packets.
 * 
 * This version was made to be extendible, where previous versions were stand alone and only relayed events.
 * 
 * The run() method should be overridden to tailor functionality.
 * @author John McCullock
 * @version 1.3 05/04/2013
 */
public class CommClient extends Thread
{
	// "volatile" is used to indicate that a variable's value will be modified by different threads.
	// Slightly different than "synchronized".
	protected volatile boolean mRequestStop = false;
	
	protected MulticastSocket mSocket = null;
	protected InetAddress mAddress = null;
	protected String mMulticastIP = null;
	protected int mPort = 0;
	protected int mBufferSize = 0;
	
	public CommClient() { return; }
	
	public CommClient(String multicastIP, int port, int bufferSize) throws Exception
	{
		this.setMulticastIPAddress(multicastIP);
		this.setPort(port);
		this.setBufferSize(bufferSize);
		return;
	}
	
	public void requestStop()
	{
		this.mRequestStop = true;
		return;
	}
	
	protected void initializeSocket()
	{
		try{
			this.mSocket = new MulticastSocket(this.mPort);
			//System.out.println("Default TTL = " + this.mSocket.getTimeToLive());
			this.mAddress = InetAddress.getByName(this.mMulticastIP);
			this.mSocket.joinGroup(this.mAddress);
		
			this.mSocket.setReceiveBufferSize(this.mBufferSize);
			//System.out.println("Max size = " + this.mSocket.getReceiveBufferSize());
		}catch(UnknownHostException uhe){
			uhe.printStackTrace();
		}catch(IOException ioe){
			ioe.printStackTrace();
		}
		return;
	}
	
	public void run()
	{
		this.initializeSocket();
	
		DatagramPacket packet;
		boolean done = false;
		
		while(!done){
			try{
				byte[] buf = new byte[this.mBufferSize];
				packet = new DatagramPacket(buf, buf.length);
				this.mSocket.receive(packet);
				
				//System.out.println("Received data.");
				CommClientListener.notifyContentReceived(this, buf);
			}catch(Exception ie){
				ie.printStackTrace();
				done = true;
			}
			if(this.mRequestStop){
				done = true;
			}
		}
	
		this.closeOpenSocket();
	}
	
	protected void closeOpenSocket()
	{
		try{
			this.mSocket.leaveGroup(this.mAddress);
			this.mSocket.close();
		}catch(IOException ioe){
			ioe.printStackTrace();
		}
		return;
	}
	
	protected void setMulticastIPAddress(String multicastIP) throws NullPointerException, Exception
	{
		try{
			if(multicastIP == null){
				throw new NullPointerException("Parameter cannot be null.");
			}
			this.mMulticastIP = multicastIP;
		}catch(NullPointerException npe){
			throw npe;
		}catch(Exception ex){
			throw ex;
		}
		return;
	}
	
	protected void setPort(int port) throws Exception
	{
		this.mPort = port;
		return;
	}
	
	protected void setBufferSize(int bufferSize)
	{
		this.mBufferSize = bufferSize;
		return;
	}
}
