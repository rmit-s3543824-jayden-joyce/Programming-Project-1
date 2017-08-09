package model;

public abstract class User {

	String user_ID;
	String user_fname;
	String user_lname;
	int age;
	String password;

	public User(String user_ID, String password, String firstName, String lastName, int age)
	{
		this.user_ID = user_ID;
		this.user_fname = firstName;
		this.user_lname = lastName;
		this.age = age;
		this.password = password;
	}
	
}
