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
import org.supercsv.io.CsvListReader;
import org.supercsv.io.CsvListWriter;
import org.supercsv.prefs.CsvPreference;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

public class FileTools {
	public static final String USER_DATA_FILE = "src/main/resources/userData.csv";
	public static final String USER_ACC_FILE = "src/main/resources/accountData.csv";
	public static final String USER_TRANSACTION_LOG = "src/main/resources/transactionLog.csv";
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
		ArrayList<String[]> searchedPlayers = searchFile(user_ID, USER_DATA_FILE);
		String[] parameters = null;
		String password;
		String firstName;
		String lastName;
		int age;
		
		for (String[] player : searchedPlayers)
		{
			if (player[0].equals(user_ID))
			{
				parameters = player;
				break;
			}
		}
		
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
	public ArrayList<String[]> searchFile(String id, String filepath)
	{		
		ArrayList<String[]> matching = new ArrayList<String[]>();
		String[] splitString = null;
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(filepath));
			String readLine = br.readLine();
			
			while (readLine != null)
			{
				splitString = readLine.split(",");
				if (splitString[0].contains(id) && !splitString[0].equals("user_ID"))
				{
					//adding matching players to the list
					matching.add(splitString);
				}
				readLine = br.readLine();
			}

			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//error handling of for null 0 elements matching
		if(matching.size() == 0){
			return null;
		}
		return matching;
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
		ArrayList<String> sharesOwned = tr.getSharesOwned();
		
		trAccString = tr.getUser_ID() + "," + tr.getCurrBal();
		//Listing all shares under player's possession
		int i = 0;
		while(i <= sharesOwned.size()-1){
			trAccString = trAccString + "," + sharesOwned.get(i);
			i ++;
		}
		return trAccString;
	}
	
	//writing Trading account to file, need to do it differently because it has variable columns
	public void trAccToFile(TradingAcc trAcc) throws IOException{
		CsvListReader  listReader = new CsvListReader(new FileReader(FileTools.USER_ACC_FILE), CsvPreference.STANDARD_PREFERENCE);
		List<List<String>> csvContents = new ArrayList<List<String>>();
		List<String> line;
		CsvListWriter listWriter;
		boolean found = false;
		
		//read CSV contents and alter line if changes found, then close reader
		while ((line = listReader.read()) != null)
		{
			if (line.contains(trAcc.getUser_ID()))
			{
				line = Arrays.asList(trAccToString(trAcc).split(","));
				found = true;
			}
			csvContents.add(line);
		}
		listReader.close();
		
		//if not found append to contents
		if (!found)
		{
			csvContents.add(Arrays.asList(trAccToString(trAcc).split(",")));
		}
		
		//write to csv
		listWriter = new CsvListWriter(new FileWriter(FileTools.USER_ACC_FILE), CsvPreference.STANDARD_PREFERENCE);
		for (List<String> row : csvContents)
		{
			listWriter.write(row);
		}
		listWriter.close();
		
	}
	
	public void updateTransCSV(Transaction transaction, String filePath) throws IOException
	{
		List<String[]>transList = readCSV(USER_TRANSACTION_LOG);
		String[] newTrans = {transaction.getID(), transaction.getTransType().toString(), transaction.getASXcode(), transaction.getCompName(), transaction.getShareVal().toString(), transaction.getDateTime()};
		transList.add(newTrans);
		overwriteCSV(transList, USER_TRANSACTION_LOG);
	}
}
