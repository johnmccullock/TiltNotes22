package dialogs;

import java.util.EventListener;

public interface EmoticonListEventListener extends EventListener
{
	abstract void thumbListClickPerformed(EmoticonListEvent tle);
	abstract void thumbListDoubleClickPerformed(EmoticonListEvent tle);
}
