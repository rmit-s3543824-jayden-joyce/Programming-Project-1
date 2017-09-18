package model;

import java.util.ArrayList;

public class NoSharesException extends Exception{
	private ArrayList<String> sharesOwned;
	
	public NoSharesException (ArrayList<String> sharesOwned){
		this.sharesOwned = sharesOwned;
	}
	
	public ArrayList<String> getSharesOwned(){
		return sharesOwned;
	}
}
