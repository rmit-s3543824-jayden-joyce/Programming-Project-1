package model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import org.json.JSONObject;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

public class FileTools {
	public static final String USER_DATA_FILE = "src/main/resources/userData.csv";
	public static final String ASX_COMPANIES_DATA_FILE = "src/main/resources/ASXListedCompanies.csv";
    public static final String ASX_JSON_PATH = "data.asx.com.au/data/1/share/";
    public static final String ASX_JSON_PAST_DAILY = "/prices?interval=daily&count=";
	
    //fetch data from a JSON in a url using its ASX code, if ASX code is not a key, then return null
	public JSONObject fetchShareData(String ASXcode) throws IOException
	{
		JSONObject json = null;
		URL url = new URL("http://" + ASX_JSON_PATH + ASXcode);
		InputStream is;
		BufferedReader br;
		
		if (!urlIsValid(url))
		{
			return null;
		}
		
		is = url.openStream();
		br = new BufferedReader(new InputStreamReader(is));
		json = new JSONObject(br.readLine());	
		
		if (!json.has("open_price"))
		{
			return null;
		}
		
		br.close();
		
		return json;
	}
	
	//checks if url is valid
	public boolean urlIsValid(URL url) throws IOException
	{
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("HEAD");
		return (con.getResponseCode() == HttpURLConnection.HTTP_OK);
	}
	
	//Separate function for writing changes to csv file after fetching data
	public void overwriteCSV(List<String[]> companiesCSVlist, String filePath) throws IOException
	{
		CSVWriter csvWriter = new CSVWriter(new FileWriter(filePath));
		csvWriter.writeAll(companiesCSVlist);
		csvWriter.flush();
		csvWriter.close();
	}
	
	//fetch all data from from JSON url and save them to file
	public void fetchAllShareData()
	{
		try {
			JSONObject json = null;
			List<String[]> companiesCSVlist;
			//read from csv and add edit vals
			CSVReader csvReader = new CSVReader(new FileReader(ASX_COMPANIES_DATA_FILE));
			companiesCSVlist = csvReader.readAll();
			
			for (int i = 1; i < companiesCSVlist.size(); i++)
			{
				json = fetchShareData(companiesCSVlist.get(i)[1]);
				if (json != null)
				{
					companiesCSVlist.get(i)[3] = json.get("open_price").toString();
				}
			}
			
			csvReader.close();
			
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
	}
	
	// Putting the trading account into a string format
	public String trAccToString(TradingAcc tr){
		String trAcc = null;
		return trAcc;
	}
}
