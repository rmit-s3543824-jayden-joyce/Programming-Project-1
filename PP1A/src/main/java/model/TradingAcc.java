package model;

import java.math.BigDecimal;
import java.util.ArrayList;

public class TradingAcc {
	public static final BigDecimal INIT_BAL = new BigDecimal(1000000);
	private String user_ID;
	private BigDecimal currBal;
	private ArrayList<String> sharesOwned;
	
	public TradingAcc(String user_ID){
		this.user_ID = user_ID;
	}
	
	public Transaction buyShares(){
		Transaction buy = null;
		return buy;
	}
	
	public Transaction sellShares(){
		Transaction sell = null;
		return sell;
	}
	
	public Transaction showLastTrans(String inputFile){
		Transaction lastTrans = null;
		return lastTrans;
	}
	
	public BigDecimal showCurrStockVal(ArrayList<Shares> sharesOwned){
		BigDecimal currStockVal = null;
		return null;
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
