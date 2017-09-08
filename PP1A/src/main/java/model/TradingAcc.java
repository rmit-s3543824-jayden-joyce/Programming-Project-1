package model;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TradingAcc {
	public static final BigDecimal INIT_BAL = new BigDecimal(1000000);
	private String user_ID;
	private BigDecimal currBal;
	private ArrayList<String> sharesOwned;
	FileTools fileTool = new FileTools();
	
	public TradingAcc(String user_ID){
		this.user_ID = user_ID;
	}
	
	public Transaction buyShares(Shares share){
		String currTimeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		BigDecimal newBal;
		Transaction buy;
		
		//return null if player don't have enough money
		if (currBal.doubleValue() < share.getShareVal().doubleValue())
		{
			return null;
		}
		else if (sharesOwned == null)
		{
			sharesOwned = new ArrayList<String>();
		}
		
		//set balance to (current balance - share value), add share to list and create a transaction object
		newBal = currBal.subtract(share.getShareVal());
		setCurrBal(newBal);
		sharesOwned.add(share.getASX_code());
		buy = new Transaction(user_ID, Transaction.TransType.BUYING, share.getASX_code(), share.getCompName(), share.getShareVal(), currTimeStamp);

		//save transaction to file and update user account data file
		try {
			fileTool.addToTransCSV(buy, FileTools.USER_TRANSACTION_LOG);
			fileTool.trAccToFile(this);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return buy;
	}
	
	public Transaction sellShares(Shares share){
		String currTimeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		BigDecimal newBal;
		Transaction sell;
		
		// if no shares, return null
		if (sharesOwned == null)
		{
			return null;
		}
		
		//set balance to (current balance + share value), remove share from list and create a transaction object if share in possession, else return null
		if (sharesOwned.remove(share.getASX_code()))
		{
			newBal = currBal.add(share.getShareVal());
			setCurrBal(newBal);
			sell = new Transaction(user_ID, Transaction.TransType.SELLING, share.getASX_code(), share.getCompName(), share.getShareVal(), currTimeStamp);
		}
		else
		{
			return null;
		}
		
		//save transaction to file and update user account data file
		try {
			fileTool.addToTransCSV(sell, FileTools.USER_TRANSACTION_LOG);
			fileTool.trAccToFile(this);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return sell;
	}
	
	public void updateId(Player player)
	{
		user_ID = player.getID();
	}
	
	public Transaction showLastTrans(String inputFile){
		Transaction lastTrans = null;
		return lastTrans;
	}
	
	public BigDecimal showCurrStockVal(ArrayList<Shares> sharesOwned){
		BigDecimal currStockVal = new BigDecimal(0);
		List<String[]> allShares = null;
		int priceIndex = 3;
		int codeIndex = 0;
		
		//read asx companies list
		try {
			allShares = fileTool.readCSV(FileTools.ASX_COMPANIES_DATA_FILE);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//return null if can't read asx companies list or it is empty
		if (allShares == null || allShares.isEmpty())
		{
			return null;
		}
		
		//calculate total stock val
		for (String ownedShare : this.sharesOwned)
		{
			for (String[] share : allShares)
			{
				if (share[codeIndex].equals(ownedShare))
				{
					currStockVal.add(new BigDecimal(share[priceIndex]));
					break;
				}
			}
		}
		
		return currStockVal;
	}
	
	public String getUser_ID(){
		return user_ID;
	}
	
	public BigDecimal getCurrBal(){
		return currBal;
	}
	
	public BigDecimal setCurrBal(BigDecimal newBal){
		currBal = newBal;
		return currBal;
	}
	
	public ArrayList<String> getSharesOwned(){
		return sharesOwned;
	}
	
	public ArrayList<String> setSharesOwned(ArrayList<String> newShares){
		sharesOwned = newShares;
		return sharesOwned;
	}
}
