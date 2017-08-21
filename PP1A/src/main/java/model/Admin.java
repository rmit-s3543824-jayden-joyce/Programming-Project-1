package model;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Admin extends User{
	
	private String user_ID;
	public Admin(String user_ID, String password, String firstName, String lastName, int age) {
		super(user_ID, password, firstName, lastName, age);
		this.user_ID = user_ID;
	}

	//Search file for a specific player and returns the specific player
	public Player searchPlayer(String user_ID){
		FileTools ft = new FileTools();
	
		Player searchedPlayer = null;
		String[] splitString = null;
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(ft.USER_DATA_FILE));
			String readLine = br.readLine();
			
			while (readLine != null)
			{
				splitString = readLine.split(",");
				if (splitString[0].equals(user_ID) && !splitString[0].equals("user_ID"))
				{
					searchedPlayer = new Player(splitString[0], splitString[1], splitString[2], splitString[3], Integer.parseInt(splitString[4]));
				}
				readLine = br.readLine();
			}

			br.close();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
		return searchedPlayer;
	}
	
	//Search file for all players adds each player into array list as Player object. Return Arraylist of Player
	public ArrayList<Player> viewAllPlayers(){
		ArrayList<Player> allPlayers = new ArrayList<Player>();
		Player player = null;
		FileTools ft = new FileTools();
		String[] splitString;
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(ft.USER_DATA_FILE));
			String readLine = br.readLine();
			
			while (readLine != null)
			{
				splitString = readLine.split(",");
				if (splitString[0].equals(user_ID) && !splitString[0].equals("user_ID"))
				{
					player = new Player(splitString[0], splitString[1], splitString[2], splitString[3], Integer.parseInt(splitString[4]));
					allPlayers.add(player);
				}
				readLine = br.readLine();
			}

			br.close();
		} catch (IOException e) {
			
			e.printStackTrace();
		}		
		return allPlayers;
	}
	
}
