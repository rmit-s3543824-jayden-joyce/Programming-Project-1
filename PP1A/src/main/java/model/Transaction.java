package model;

import java.math.BigDecimal;
import java.util.Date;

public class Transaction {
	String user_ID;
	String transType;
	String seller;
	String buyer;
	BigDecimal shareVal;	
	Date dateTime;
	
	public Transaction(String user_ID, String transType, 
			           String seller, String buyer, 
			           BigDecimal shareVal, Date dateTime){
		this.user_ID = user_ID;
		this.transType = transType;
		this.seller = seller;
		this.buyer = buyer;
		this.shareVal = shareVal;
		this.dateTime = dateTime;
	}
	
	public String getID(){
		return user_ID;
	}
    
	public String getTransType(){
		return transType;
	}
	
	public String getSeller(){
		return seller;
	}
	
	public String buyer(){
		return buyer;
	}
	
	public BigDecimal getShareVal(){
		return shareVal;
	}
	
	public Date getDateTime(){
		return dateTime;
	}
	
}
