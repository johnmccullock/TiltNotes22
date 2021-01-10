package main;

/**
 * General data class used to describe a user.
 * @author John McCullock
 * @version 1.0 04/26/2013
 * @see CurrentlyActiveUsersPanel.
 */
public class User
{
	public String userName = "";
	public String userIP = "";
	public int age = 0;
	
	public User()	{ return; }
	
	public User(String name, String ip)
	{
		this.userName = name;
		this.userIP = ip;
		return;
	}
}
