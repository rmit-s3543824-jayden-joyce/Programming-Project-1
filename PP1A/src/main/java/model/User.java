package model;

public abstract class User {

	private String user_ID;
	private String user_fname;
	private String user_lname;
	private int age;
	private String password;

	public User(String user_ID, String password, String firstName, String lastName, int age)
	{
		this.user_ID = user_ID;
		this.user_fname = firstName;
		this.user_lname = lastName;
		this.age = age;
		this.password = password;
	}
	
	
	// Getters and setters
	public String getID()
	{
		return user_ID;
	}
	
	public String getFName()
	{
		return user_fname;
	}
	
	public String getLName()
	{
		return user_lname;
	}
	
	public int getAge()
	{
		return age;
	}
	
	public String getPassword()
	{
		return password;
	}
}
