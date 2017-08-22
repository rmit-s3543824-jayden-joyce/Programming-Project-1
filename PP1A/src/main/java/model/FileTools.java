package model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.json.JSONObject;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

public class FileTools {
	public static final String USER_DATA_FILE = "src/main/resources/userData.csv";
	public static final String USER_ACC_FILE = "src/main/resources/accountData.csv";
	public static final String USER_SHARES_FILE = "src/main/resources/userShares.csv";
	public static final String ASX_COMPANIES_DATA_FILE = "src/main/resources/ASXListedCompanies.csv";
	public static final String ALPHA_ADVANTAGE_API_KEY = "MP9H93RQEUUFGX07";
    public static final String URL_JSON_PATH_P1 = "https://www.alphavantage.co/query?function=TIME_SERIES_INTRADAY&symbol=";
    public static final String URL_JSON_PATH_P2 = ".AX&interval=60min&apikey=" + ALPHA_ADVANTAGE_API_KEY;
	
    //fetch data from a JSON in a url using its ASX code, if ASX code is not a key, then return null
	public JSONObject fetchShareData(String ASXcode) throws IOException
	{
		JSONObject json = null;
		URL url = new URL(URL_JSON_PATH_P1 + ASXcode + URL_JSON_PATH_P2);
		InputStream is;
		BufferedReader br;
		
		is = url.openConnection().getInputStream();
		br = new BufferedReader(new InputStreamReader(is));
		
		StringBuilder jsonStringBuilder = new StringBuilder();
		String line;
		
		while ((line = br.readLine()) != null)
		{
			jsonStringBuilder.append(line);
		}
		
		json = new JSONObject(jsonStringBuilder.toString());	
		
		if (!json.has("Time Series (60min)"))
		{
			return null;
		}
		
		br.close();
		is.close();
		
		return json;
	}
	
	//Separate function for writing changes to csv file after fetching data
	public void overwriteCSV(List<String[]> companiesCSVlist, String filePath) throws IOException
	{
		CSVWriter csvWriter = new CSVWriter(new FileWriter(filePath));
		csvWriter.writeAll(companiesCSVlist);
		csvWriter.flush();
		csvWriter.close();
	}
	
	//reads entire csv file and return its contents as a List of string arrays
	public List<String[]> readCSV(String filePath)throws IOException
	{
		CSVReader csvReader = new CSVReader(new FileReader(filePath));
		List<String[]> csvContents = csvReader.readAll();
		csvReader.close();
		return csvContents;
	}
	
	//fetch all data from from JSON url and save them to file
	public void fetchAllShareData()
	{
		try {
			JSONObject json = null;
			List<String[]> companiesCSVlist;
			Object[] jsonArray;
			//read from csv and add edit vals
			companiesCSVlist = readCSV(ASX_COMPANIES_DATA_FILE);
			
			for (int i = 1; i < companiesCSVlist.size(); i++)
			{
				json = fetchShareData(companiesCSVlist.get(i)[1]);
				if (json != null)
				{
					jsonArray = json.getJSONObject("Time Series (60min)").keySet().toArray();
					Arrays.sort(jsonArray, Collections.reverseOrder());
					companiesCSVlist.get(i)[3] = ((JSONObject) json.getJSONObject("Time Series (60min)").get(jsonArray[0].toString())).get("1. open").toString();
				}
				else
				{
					companiesCSVlist.get(i)[3] = null;
				}
				System.out.println(i);
			}
						
			//write to csv (rewrite everything)
			overwriteCSV(companiesCSVlist, ASX_COMPANIES_DATA_FILE);
		}
		catch (IOException e){
			
		}
	}

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
		
		return null;
	}
	
	//convert to CVS format
	public String toCSV(User player){
		String cvsString = null;
		cvsString = player.getID() + "," + 
					player.getPassword() + "," + 
					player.getFName() + "," + 
					player.getLName() + "," +
					player.getAge();
		return cvsString;
	}
	//write to end of file (Append to File)
	public void playerToFile(User player) throws IOException{
		FileWriter fw =  new FileWriter(USER_DATA_FILE,true);
		BufferedWriter bw = new BufferedWriter(fw);
		
		bw.write(toCSV(player));
		bw.close();
	}
	
	// Putting the trading account into a string format
	public String trAccToString(TradingAcc tr){
		String trAccString = null;
		//getting player's shares
		ArrayList<Shares> sharesOwned = tr.getSharesOwned();
		
		trAccString = tr.getUser_ID() + "," + tr.getCurrBal();
		//Listing all shares under player's possession
		int i = 0;
		while(i <= sharesOwned.size()-1){
			trAccString = trAccString + "," + sharesOwned.get(i).getCompName();
			i ++;
		}
		return trAccString;
	}
	
	//writing Trading account to file
	public void trAccToFile(TradingAcc trAcc) throws IOException{
		FileWriter fw =  new FileWriter(USER_ACC_FILE,true);
		BufferedWriter bw = new BufferedWriter(fw);
		
		bw.write(trAccToString(trAcc));
		bw.close();
	}
}
