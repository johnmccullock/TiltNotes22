package comm;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;

public class CommThread extends CommServerThread
{
	private String mContent = null;
	
	public CommThread(String content, String multicastIP, String hostIP, int port, int bufferSize) throws NullPointerException, IOException, Exception
	{
		super("CommThread", multicastIP, hostIP, port, bufferSize);
        this.setContent(content);
    }
	
	public void run()
	{
		try{
			if(this.mContent == null){
				throw new Exception("No content to send.");
			}
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			byte[] buf;
			//System.out.println("Publishing: " + this.mContent);
		
			baos.reset();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.reset();
			oos.writeObject(this.mContent);
			oos.flush();
			buf = baos.toByteArray();
		
			// send it
			DatagramPacket packet = new DatagramPacket(buf, buf.length, super.mGroup, super.mPort);
			super.mSocket.send(packet);
		}catch(IOException ioe){
			ioe.printStackTrace();
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
	}
	
	private void setContent(String content)
	{
		try{
			if(content == null){
				throw new Exception("Parameter cannot be null.");
			}
			this.mContent = content;
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return;
	}
}
