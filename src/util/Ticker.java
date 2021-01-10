package util;

import java.util.Timer;
import java.util.TimerTask;

/**
 * General timer class, repeatedly running at a fixed rate, and sending a TickerEvent at each interval. 
 * @author John McCullock
 * @version 1.0 2013-10-22
 * @see TickEvent, TickEventListener, TickListener
 */
public class Ticker
{
	private Timer mainTimer = null;
	private tTask aTask = null;
	private int mInterval = 0;
	
	public Ticker(int interval)
	{
		this.setInterval(interval);
		return;
	}
	
	public void startTimer()
	{
		this.stopTimer(); // make sure any previous instances are removed.
		this.aTask = new tTask();
		this.mainTimer = new Timer();
		this.mainTimer.scheduleAtFixedRate(this.aTask, 0, this.mInterval);
		return;
	}
	
	public void stopTimer()
	{
		if(this.mainTimer != null){
			this.mainTimer.cancel();
			this.mainTimer.purge();
			this.mainTimer = null;
		}
		if(this.aTask != null){
			this.aTask.cancel();
			this.aTask = null;
		}
		return;
	}
	
	private void setInterval(int interval)
	{
		this.mInterval = interval;
		return;
	}
	
	public boolean isRunning()
	{
		boolean running = false;
		if(this.mainTimer != null){
			running = true;
		}
		return running;
	}
	
	private class tTask extends TimerTask
	{
		public void run()
		{
			try{
				TickListener.notifyTimerTick(Ticker.this);
			}catch(Exception ex){
				ex.printStackTrace();
			}
			return;
		}
	}
}
