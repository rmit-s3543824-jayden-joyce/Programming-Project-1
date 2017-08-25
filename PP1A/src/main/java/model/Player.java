package model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Player extends User{
	
	String user_ID;
	public Player(String user_ID, String password, String firstName, String lastName, int age) {
		super(user_ID, password, firstName, lastName, age);
		// TODO Auto-generated constructor stub
		this.user_ID = user_ID;
	}
	
	//Players to edit their own personal details 
	public Player editProfile(String oldId, String user_ID, 
			                  String password, String user_fname, 
			                  String user_lname, int age) throws IOException{	
		
		FileTools ft = new FileTools();
		
		Player editPlayer = null;
		String filePath = FileTools.USER_DATA_FILE;
		
		//loading existing file into memory
		List<String[]> allPlayers = ft.readCSV(filePath);
		
		//iterating and editing player
		int pIdx = 0;
		while(pIdx <= allPlayers.size()-1){
			if(oldId.equals(allPlayers.get(pIdx)[0])){
				//editing player
				allPlayers.get(pIdx)[0] = user_ID;
				allPlayers.get(pIdx)[1] = password;
				allPlayers.get(pIdx)[2] = user_fname;
				allPlayers.get(pIdx)[3] = user_lname;
				allPlayers.get(pIdx)[4] = Integer.toString(age);
				//assigning for return value
				editPlayer = new Player(user_ID, password, user_fname, user_lname, age);
				//writing back to file
				ft.overwriteCSV(allPlayers, filePath);
				return editPlayer;
			}
			pIdx ++;	
		}
		return null;
	}
	
	//Opening a new trading account with initial balance
	public TradingAcc openTradeAcc(String user_ID) throws IOException{
		FileTools ft = new FileTools();
		//assign the account to an ID
		TradingAcc newAcc = new TradingAcc(user_ID);
		//set initial balance
		newAcc.setCurrBal(newAcc.INIT_BAL);
		newAcc.setSharesOwned(new ArrayList<String>());	
		
		ft.trAccToFile(newAcc);
		System.out.println("Successfully Open a Trading Account!");	
		return newAcc;
	}
	
}
