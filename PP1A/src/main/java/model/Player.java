package model;

public class Player extends User{
	
	String user_ID;
	public Player(String user_ID, String password, String firstName, String lastName, int age) {
		super(user_ID, password, firstName, lastName, age);
		// TODO Auto-generated constructor stub
		this.user_ID = user_ID;
	}
	
	public Player editProfile(String user_ID, String user_fname, 
			                  String user_lname, String password){	
		Player editedPlayer = null;
		return editedPlayer;
	}
	
	public TradingAcc openTradeAcc(String user_ID){
		TradingAcc newAcc = new TradingAcc(user_ID);
		return newAcc;
	}
	
}
