package tray;

import java.util.EventListener;



public interface TrayMenuEventListener extends EventListener
{
	abstract void trayMenuNewNotePerformed(TrayMenuEvent tme);
	abstract void trayMenuTemplatePerformed(TrayMenuEvent tme);
	abstract void trayMenuHistoryPerformed(TrayMenuEvent tme);
	abstract void trayMenuSettingsPerformed(TrayMenuEvent tme);
	abstract void trayMenuExitPerformed(TrayMenuEvent tme);
}
