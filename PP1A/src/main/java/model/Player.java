package model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

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
		
		Player editedPlayer = null;
		String oldName = ft.USER_DATA_FILE;
		String tempName = "tmpFile.txt";
		
		//Read from database file, and write up a new tmpfile with update
		try {
			String line, newData;
			//manipulating files
			BufferedReader br = new BufferedReader(new FileReader(oldName));
			BufferedWriter bw = new BufferedWriter(new FileWriter(tempName));
			
			Player editPlayer;
			while((line = br.readLine()) != null){
				if(line.contains(oldId)){
					//creating a new edited file
					editPlayer = new Player(user_ID, password, user_fname, user_lname, age);
					//edit player to CSV format
					String strPlayer = ft.toCSV(editPlayer);
					bw.write(strPlayer + "\r\n");
					continue;
				}
				//writing unedited line
			}
			
			br.close();
			bw.close();
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
		}
		//deleting old file
		File oldFile = new File(oldName);
		oldFile.delete();
		//renaming new edited file with the old name
		File tempFile = new File(tempName);
		tempFile.renameTo(oldFile);
		
		return editedPlayer;
	}
	
	//Opening a new trading account with initial balance
	public TradingAcc openTradeAcc(String user_ID){
		
		TradingAcc newAcc = new TradingAcc(user_ID);
		newAcc.setCurrBal(newAcc.INIT_BAL);
		newAcc.setSharesOwned(new ArrayList<Shares>());
		
		System.out.println("Successfully Open a Trading Account!");		
		return newAcc;
	}
	
}
