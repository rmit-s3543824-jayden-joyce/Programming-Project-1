package model;

public class Menu {

	FileTools fileTool = new FileTools();

	public Boolean login(String username, String password)
	{
		User user = fileTool.LoadPlayer(username);
		
		if (user.getPassword().equals(password))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public Boolean register()
	{
		
		return false;
	}
	
}
