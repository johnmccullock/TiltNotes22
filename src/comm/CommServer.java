package comm;

import java.io.IOException;

public class CommServer
{
	public CommServer(String content, String multicastIP, String hostIP, int port, int bufferSize) throws NullPointerException, IOException, Exception
	{
		new CommThread(content, multicastIP, hostIP, port, bufferSize).start();
		return;
	}
}
