package comm;

import java.io.IOException;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;

public class CommServerThread extends Thread
{
	protected MulticastSocket mSocket = null;	// MulticastSocket. This kind of socket is used on the
												// client-side to listen for packets that the server
												// broadcasts to multiple clients.
	protected InetAddress mGroup = null;
	
	protected String mMulticastIPAddress = null;
	protected String mHostIPAddress = null;
	protected int mPort = 0;
	protected int mBufferSize = 0;
	
	public CommServerThread() throws IOException
	{
		super("CommServerThread");
		return;
	}
	
	public CommServerThread(String name, String multicastIP, String hostIP, int port, int bufferSize) throws NullPointerException, IOException, Exception
	{
		try{
			this.setMulticastIPAddress(multicastIP);
			this.setHostIPAddress(hostIP);
			this.setPort(port);
			this.setBufferSize(bufferSize);
			
			InetSocketAddress ina = new InetSocketAddress(this.mHostIPAddress, this.mPort);
			this.mSocket = new MulticastSocket(ina);
			this.mGroup = InetAddress.getByName(this.mMulticastIPAddress);
			this.mSocket.setSendBufferSize(this.mBufferSize);
		}catch(NullPointerException npe){
			throw npe;
		}catch(IOException ioe){
			throw ioe;
		}catch(Exception ex){
			throw ex;
		}
		return;
	}
	
	public void run()
	{
		return;
	}
	
	protected void setMulticastIPAddress(String multicastIP) throws NullPointerException, Exception
	{
		try{
			if(multicastIP == null){
				throw new NullPointerException("Parameter cannot be null.");
			}
			this.mMulticastIPAddress = multicastIP;
		}catch(NullPointerException npe){
			throw npe;
		}catch(Exception ex){
			throw ex;
		}
		return;
	}
	
	private void setHostIPAddress(String hostIP) throws NullPointerException, Exception
	{
		try{
			if(hostIP == null){
				throw new NullPointerException("Parameter cannot be null.");
			}
			this.mHostIPAddress = hostIP;
		}catch(NullPointerException npe){
			throw npe;
		}catch(Exception ex){
			throw ex;
		}
		return;
	}
	
	private void setPort(int port) throws Exception
	{
		this.mPort = port;
		return;
	}
	
	private void setBufferSize(int bufferSize)
	{
		this.mBufferSize = bufferSize;
		return;
	}
}
