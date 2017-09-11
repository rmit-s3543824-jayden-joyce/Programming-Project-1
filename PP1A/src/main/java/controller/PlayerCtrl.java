package controller;

import java.util.Map;

import spark.Request;
import model.FileTools;
import model.Player;

public class PlayerCtrl extends UserCtrl{

	public void initPlayerDetails(Map<String, Object> model, Request req)
	{
		FileTools fileTool = new FileTools();
		Player player = (Player) fileTool.LoadUser(req.session().attribute("username"));
		
		super.initUserDetails(model, req, player);
		
		if (player.getTradingAcc() != null)
		{
			model.put("currBal", player.getTradingAcc().getCurrBal());
			model.put("sharesOwned", player.getTradingAcc().getSharesOwned());
		}
		
	}
}
