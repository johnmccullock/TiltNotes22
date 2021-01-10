package comm;

import java.util.EventListener;

public interface CommClientEventListener extends EventListener
{
	abstract void commClientContentReceived(CommClientEvent cce);
}
