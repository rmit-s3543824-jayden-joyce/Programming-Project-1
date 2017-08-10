package model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileTools {
	public static final String USER_DATA_FILE = "src/main/resources/userData.csv";

	//use user ID to find player in a file and return it
	public User LoadPlayer(String user_ID)
	{
		User user = null;
		String[] parameters = searchFile(user_ID, USER_DATA_FILE);
		String password;
		String firstName;
		String lastName;
		int age;
		
		//return null if can't find user_ID in file
		if (parameters == null)
		{
			return user;
		}
		
		//use these variables to make it easier to read
		password = parameters[1];
		firstName = parameters[2];
		lastName = parameters[3];
		age = Integer.parseInt(parameters[4]);
		
		if (user_ID.equals("admin"))
		{
			user = new Admin(user_ID, password, firstName, lastName, age);
		}
		else
		{
			user = new Player(user_ID, password, firstName, lastName, age);
		}
		
		return user;
	}
	
	//search a file for a specified id string and return row as a string array
	public String[] searchFile(String id, String filepath)
	{
		String[] splitString = null;
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(filepath));
			String readLine = br.readLine();
			
			while (readLine != null)
			{
				splitString = readLine.split(",");
				if (splitString[0].equals(id) && !splitString[0].equals("user_ID"))
				{
					return splitString;
				}
				readLine = br.readLine();
			}

			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return splitString;
	}
	
	//convert to CVS format
	public String toCVS(User player){
		String cvsString = null;
		cvsString = player.user_ID + "," + 
					player.password + "," + 
					player.user_fname + "," + 
					player.user_lname + "," +
					player.age;
		return cvsString;
	}
	//write to end of file (Append to File)
	public void playerToFile(User player) throws IOException{
		FileWriter fw =  new FileWriter(USER_DATA_FILE,true);
		BufferedWriter bw = new BufferedWriter(fw);
		
		bw.write(toCVS(player));
	}
}
