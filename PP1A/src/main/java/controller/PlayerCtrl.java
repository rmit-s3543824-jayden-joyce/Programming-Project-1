package controller;

import java.io.IOException;
import java.util.Map;

import spark.Request;
import model.FileTools;
import model.Player;

public class PlayerCtrl extends UserCtrl{

	public void initPlayerDetails(Map<String, Object> model, Request req)
	{
		Player player = (Player) FileTools.LoadUser(req.session().attribute("username"));
		
		super.initUserDetails(model, req, player);
		
		if (player.getTradingAcc() != null)
		{
			model.put("currBal", player.getTradingAcc().getCurrBal());
			model.put("sharesOwned", player.getTradingAcc().getSharesOwned());
		}
	}
	
	public void deleteAccount(String username, Request req)
	{
		Player player = (Player) FileTools.LoadUser(req.session().attribute("username"));
		
		try {
			player.deleteAcc();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
}
